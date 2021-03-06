package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.entity.*;
import jade.core.Agent;
import org.hibernate.Session;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

import static fr.miage.projetagent.bdd.HibernateSessionProvider.getSessionFactory;

public class BddAgent extends Agent {

    public static String[] lesPays = {"Guinee", "Tunisie", "Gambie", "Cameroun", "Senegal"};
    //public static String[] lesAssos = {"GrippeSansFrontiére", "Emmaus", "MiageSansFrontiere", "Helpers"};
    public static String[] lesAssos = {"MiageSansFrontiere", "Helpers"};
    public static String[] lesMaladies = {"grippe", "bronchite", "rage", "variole", "sida"};
    public static Map<String, Priority> lesprio = new HashMap<>();

    @Override
    protected void setup() {
        //add sick and clean db every 30 seconds
        this.addBehaviour(new BddBehaviour(this, 30000));
    }

    public static List<String> getAllAssosName() {

        Session session = getSessionFactory().openSession();

        Query q = session.createNamedQuery("Association.findAll", Association.class);
        List<Association> results = q.getResultList();

        ArrayList<String> tmp = new ArrayList<>();
        results.forEach((assoc) -> {
            tmp.add(assoc.getNom());
        });

        session.close();
        return tmp;
    }

    public static Association getAssos(String assosName) {
        Session session = getSessionFactory().openSession();

        Association a = (Association) session.createQuery("SELECT a FROM Association  a WHERE a.nom= :assosName")
                .setParameter("assosName", assosName)
                .getSingleResult();

        session.close();

        return a;
    }


    /**
     * Triggered by interface : set choosen priority
     *
     * @param assosName
     * @param pays
     * @param maladie
     */
    public static void getStatut(String assosName, String pays, String maladie) {
        Priority tmp = new Priority();
        tmp.setPays(pays);
        tmp.setMaladie(maladie);
        lesprio.put(assosName, tmp);
    }


    /**
     * Triggered by AssocAgent : is asking for prioriry
     * Set number of sick, number of vaccine, volume of vaccine
     *
     * @param assosName
     * @return
     */
    public static Priority getStatut(String assosName) {

        if (lesprio.get(assosName) == null) {
            lesprio.put(assosName, new Priority());
        }

        Priority p = lesprio.get(assosName);

        if (p == null) {
            p = new Priority();
            lesprio.put(assosName, p);
        }

        int indice = 0;

        Session session = getSessionFactory().openSession();

        Query all = session.createNamedQuery("Malade.getCountryAndDiseaseOrderByNumberOfSick");
        List<Object[]> maladiePays = all.getResultList();

        session.close();

        //if a disease was choosen but not a country
        if (p.getPays() == null && p.getMaladie() != null) {
            boolean found = false;
            for (Object[] tuple : maladiePays) {
                if (tuple[0].toString().equals(p.getMaladie()) && !found) {
                    p.setPays(maladiePays.get(indice)[1].toString());
                    found = true;
                }
            }
        }

        //if a country was choosen but not a disease
        if (p.getPays() != null && p.getMaladie() == null) {
            boolean found = false;
            for (Object[] tuple : maladiePays) {
                if (tuple[1].toString().equals(p.getPays()) && !found) {
                    p.setMaladie(maladiePays.get(indice)[0].toString());
                    found = true;
                }
            }
        }

        //if none was choosen
        if (p.getPays() == null && p.getMaladie() == null) {
            p.setMaladie(maladiePays.get(indice)[0].toString());
            p.setPays(maladiePays.get(indice)[1].toString());
            indice += 1;
        }

        int people = 0;
        int vaccine = 1;

        Session session2 = getSessionFactory().openSession();

        //while there are no people to cure, we are changing priorities
        while (people < vaccine && indice < maladiePays.size()) {

            //get number of sick people and date for the selected country for the selected disease
            Query q = session2.createNativeQuery("SELECT count(m.id), min(m.datecontamination +  (INTERVAL '1m')*desease.delaiincub) AS date, maladie_nom, pays_nom"
                    + " FROM malade m, maladie desease"
                    + " WHERE desease.nom=m.maladie_nom"
                    + " AND m.pays_nom= :pays"
                    + " AND m.maladie_nom= :maladie"
                    + " GROUP BY m.maladie_nom, m.pays_nom");
            q.setParameter("pays", p.getPays());
            q.setParameter("maladie", p.getMaladie());
            List<Object[]> prio = q.getResultList();

            BigInteger nb = (BigInteger) prio.get(0)[0];
            if (prio.get(0) != null && nb != null
                    && (nb).compareTo(BigInteger.valueOf(0l)) > 0) {
                p.setNombre(nb.intValue());
                p.setDate((Date) prio.get(0)[1]);
                people = p.getNombre();
            } else {
                people = 0;
            }

            //get number of vaccine we already have and total volume
            Query q2 = session2.createNativeQuery("SELECT COUNT(v.id) AS nb, SUM(v.volume) AS volume, v.nom_nom"
                    + " FROM  vaccin v WHERE v.nom_nom = :maladie AND v.association_nom = :assocName"
                    + " GROUP BY v.nom_nom");
            q2.setParameter("maladie", p.getMaladie()).setParameter("assocName", assosName);
            List<Object[]> vol = q2.getResultList();
            if (vol.size() > 0) {
                p.setNbVaccin(((BigInteger) vol.get(0)[0]).intValue());
                p.setVolume((double) vol.get(0)[1]);
                vaccine = p.getNbVaccin();
            } else {
                vaccine = 0;
                p.setNbVaccin(0);
                p.setVolume(0);
            }

            if (people < vaccine) {
                p.setMaladie(maladiePays.get(indice)[0].toString());
                p.setPays(maladiePays.get(indice)[1].toString());
            }
            indice += 1;

        }

        session2.close();

        //System.out.println(p.toString());
        return p;

    }

    public static void addVaccin(String nom, Vaccin vaccin) {

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Maladie m = session.find(Maladie.class, nom);

        vaccin.setNom(m);
        session.persist(vaccin);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Delete vaccine from DB
     *
     * @param vaccins
     */
    public static void deleteVaccin(Vaccin vaccins) {

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Vaccin v2 = (Vaccin) session.merge(vaccins);

        session.remove(v2);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Delete flight from DB
     *
     * @param vol
     */
    public static void deleteVol(Vol vol) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        session.remove(vol);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Get all flights
     *
     * @param
     */
    public static List<Vol> allFlight(String assosName) {

        Session session = getSessionFactory().openSession();

        Query q = session.createNamedQuery("Vol.allVol", Vol.class).setParameter("assocName", assosName);
        List<Vol> results = q.getResultList();

        session.close();

        return results;

    }

    /**
     * Add flight from DB
     *
     * @param vol
     */
    public static void addVol(Vol vol, String pays) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Pays result = (Pays) session.createQuery("SELECT p FROM Pays p WHERE p.nom = :pays ").setParameter("pays", pays).getSingleResult();

        vol.setDestination(result);

        session.persist(vol);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Get amount of money available for association
     *
     * @param assosName
     * @return
     */
    public static double getArgent(String assosName) {

        Session session = getSessionFactory().openSession();

        Association a = session.find(Association.class, assosName);

        session.close();

        return a.getTresorerie().getSomme();

    }

    /**
     * Decrease amount of money for association
     *
     * @param assosName
     * @param argent
     */
    public static void decreaseMoney(String assosName, double argent) {

        Session session = getSessionFactory().openSession();

        Association m = session.find(Association.class, assosName);
        session.getTransaction().begin();

        m.getTresorerie().setSomme(m.getTresorerie().getSomme() - argent);

        session.getTransaction().commit();

        session.close();

    }

    /**
     * Increase the amount of money
     *
     * @param assosName
     * @param argent
     */
    public static void increaseMoney(String assosName, double argent) {
        Session session = getSessionFactory().openSession();
        Association m = session.find(Association.class, assosName);
        session.getTransaction().begin();
        m.getTresorerie().setSomme(m.getTresorerie().getSomme() + argent);

        session.getTransaction().commit();

        session.close();
    }

    /**
     * Get number of sick people in this country for this disease
     *
     * @param pays
     * @param maladie
     * @return
     */
    public static long getNombre(String pays, String maladie) {

        Session session = getSessionFactory().openSession();

        Query q = session.createNamedQuery("Malade.nombreMaladeMaladie")
                .setParameter("nomMa", maladie)
                .setParameter("nompays", pays);
        long results = (long) q.getSingleResult();

        session.close();

        return results;
    }

    /**
     * Get all vaccine for this disease
     *
     * @param maladie
     * @return
     */
    public static List<Vaccin> getVaccins(String maladie, String assosName) {

        Session session = getSessionFactory().openSession();

        Query q = session.createNamedQuery("Vaccin.getVaccinWhereMaladie")
                .setParameter("nom", maladie)
                .setParameter("assoc", assosName);

        List<Vaccin> results = q.getResultList();

        session.close();

        return results;
    }

    /**
     * Get all diseases for this country
     *
     * @param pays
     * @return
     */
    public static List<Maladie> getMaladiesForCountry(String pays) {

        Session session = getSessionFactory().openSession();

        Query q = session.createNamedQuery("Malade.getMaladiesForCountry").setParameter("nompays", pays);

        List<Maladie> results = q.getResultList();

        session.close();

        return results;
    }

    /**
     * Get object maladie for the parameter
     *
     * @param maladie
     * @return
     */

    public static Maladie getMaladie(String maladie) {
        Session session = getSessionFactory().openSession();

        Maladie m = session.find(Maladie.class, maladie);

        session.close();

        return m;
    }

    public static void addEnvoi(Envoi envoi, List<EnvoiVaccin> evs) {
        Session session = getSessionFactory().openSession();

        session.getTransaction().begin();
        session.persist(envoi);
        session.getTransaction().commit();

        for (EnvoiVaccin ev : evs) {
            session.getTransaction().begin();
            ev.setEnvoi(envoi);
            session.persist(ev);
            session.getTransaction().commit();
        }

        session.close();
    }

    /**
     * INSERT DATA TO DB
     */


    public static void dropBase() {

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query drop = session.createNativeQuery("DROP TABLE IF EXISTS association, envoi, malade, maladie, vaccin, pays, vol, argent, association_envoi, association_vaccin, association_vol, envoi_envoivaccin, envoivaccin, metrics CASCADE");
        drop.executeUpdate();

        session.getTransaction().commit();
        session.close();

    }


    public static void addData() {
        instanciateAssociation();
        instanciateMaladie();
        instanciatePays();
        instacianteMalade(); //start right away
    }


    /**
     * Add all disease to DB
     */
    private static void instanciateMaladie() {
        Session session = getSessionFactory().openSession();

        Maladie tmp;
        Random rm = new Random();
        for (String maladie : lesMaladies) {
            tmp = new Maladie();
            tmp.setNom(maladie);
            tmp.setDelaiIncub(rm.nextDouble());

            session.getTransaction().begin();
            session.persist(tmp);
            session.getTransaction().commit();
        }

        session.close();
    }

    /**
     * Add all countries to DB
     */
    private static void instanciatePays() {

        Session session = getSessionFactory().openSession();

        Pays tmp;
        for (String pays : lesPays) {
            tmp = new Pays();
            tmp.setNom(pays);

            session.getTransaction().begin();
            session.persist(tmp);
            session.getTransaction().commit();
        }

        session.close();
    }

    /**
     * Add all associations to DB
     */
    private static void instanciateAssociation() {

        Session session = getSessionFactory().openSession();

        Association tmp;
        for (String assos : lesAssos) {
            tmp = new Association();
            tmp.setNom(assos);
            Argent a = new Argent();
            a.setSomme(10000);
            tmp.setTresorerie(a);
            session.getTransaction().begin();
            session.persist(a);
            session.persist(tmp);
            session.getTransaction().commit();
        }

        session.close();
    }

    protected static void instacianteMalade() {

        Session session = getSessionFactory().openSession();

        List<Pays> listPays = session.createQuery("SELECT p FROM Pays p").getResultList();
        List<Maladie> listmal = session.createQuery("SELECT p FROM Maladie p").getResultList();
        Random rm = new Random();
        int nb = rm.nextInt(100) + 200;
        for (int i = 0; i < nb; i++) {

            Malade tmp = new Malade();
            tmp.setEtat(TypeMalade.Soignable);
            tmp.setMaladie(listmal.get(rm.nextInt(listmal.size() - 0)));
            tmp.setPays(listPays.get(rm.nextInt(listPays.size() - 0)));
            tmp.setDateContamination(new Date());
            session.getTransaction().begin();
            session.persist(tmp);
            session.getTransaction().commit();
        }

        session.close();

    }
}
