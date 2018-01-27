package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.entity.*;
import jade.core.Agent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashAttributeSet;

public class BddAgent extends Agent {

    static List<AssosAgent> assosAgent = new ArrayList<>();
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("agentBdd");
    static EntityManager em = entityManagerFactory.createEntityManager();

    public static String[] lesPays = {"Guinee", "Maraoc", "Tunisie", "Gambie", "Botsawana", "Cameroun", "Senegal"};
    //public static String[] lesAssos = {"GrippeSansFrontiére", "Emmaus", "MiageSansFrontiere", "Helpers"};
    public static String[] lesAssos = {"MiageSansFrontiere"};
    public static String[] lesMaladies = {"grippe", "bronchite", "rage", "variole", "sida"};
    public static HashMap<String,Priority> lesprio = new HashMap<String,Priority>();

    @Override
    protected void setup() {
        //this.addBehaviour(new BddBehaviour(this, 30));
    }

    public static List<String> getAllAssosName() {

        Query q = em.createNamedQuery("Association.findAll", Association.class);
        List<Association> results = q.getResultList();
        
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(results.get(0).getNom());
        /*results.forEach((assoc)->{
        results.forEach((assoc) -> {
            tmp.add(assoc.getNom());
        });*/
        return tmp;
    }

    public static void addAssosAgent(AssosAgent a) {
        assosAgent.add(a);
        a.setPriority(getStatut(a.getLocalName()));
        a.setArgent(getArgent(a.getLocalName()));
    }

    public static void removeAssosAgent(AssosAgent a) {
        assosAgent.remove(a);
    }


    public static void getStatut(String assosName,String pays,String maladie) {
        Priority tmp = new Priority();

        Query q = em.createNativeQuery("SELECT (datecontamination + interval '1h' * mi.delaiincub) as date_prev, COUNT(*) as nb_malade, pays_nom, maladie_nom FROM malade m \n" +
                                        "LEFT JOIN vaccin v ON v.nom_nom = m.maladie_nom \n" +
                                        "LEFT JOIN maladie mi on m.maladie_nom = mi.nom\n" +
                                        "WHERE pays_nom = :pays \n" +
                                        "AND maladie_nom = :maladie \n" +
                                        "GROUP by maladie_nom,pays_nom,datecontamination,mi.delaiincub\n" +
                                        "ORDER BY date_prev ASC\n");
        q.setParameter("pays", pays);
        q.setParameter("maladie", maladie);

        List<Object[]> prio = q.getResultList();
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        try {
            tmp.setDate(df.parse((String)prio.get(0)[0]));
        } catch (ParseException ex) {
            Logger.getLogger(BddAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        tmp.setNombre((int)prio.get(0)[1]);
        tmp.setPays((String)prio.get(0)[2]);
        tmp.setMaladie((String)prio.get(0)[3]);
        
        Query q2 = em.createNativeQuery("SELECT COUNT(v.volume) as nb_volume,SUM(v.volume) as volume, nom_nom\n" +
                                        "FROM vaccin v \n" +
                                        "WHERE v.nom_nom = :maladie \n" +
                                        "GROUP BY nom_nom\n");
        
        q2.setParameter("maladie", maladie);
        List<Object[]> vol = q2.getResultList();
        
        tmp.setNbVaccin((int)vol.get(0)[0]);
        tmp.setVolume((double)vol.get(0)[1]);
        lesprio.put(assosName, tmp);
    }
    
    public static Priority getStatut(String assosName) {   
        //actualiser la bdd
        getStatut(assosName, lesprio.get(assosName).getPays(),lesprio.get(assosName).getMaladie());
       return lesprio.get(assosName);
 
    }

    public static void addData() {
        instanciateAssociation();
        instanciateMaladie();
        instanciatePays();
        instacianteMalade();
    }

    public static void addVaccin(String nom, Vaccin vaccin) {
        Maladie m = em.find(Maladie.class, nom);
        em.getTransaction().begin();

        vaccin.setNom(m);
        em.persist(vaccin);
        em.getTransaction().commit();
        //TODO ajouter vaccin pour la maladie
    }

    /**
     * Delete vaccine from DB
     *
     * @param vaccins
     */
    public static void deleteVaccin(Vaccin vaccins) {
        em.getTransaction().begin();
        em.remove(vaccins);
        em.getTransaction().commit();
    }

    /**
     * Delete flight from DB
     *
     * @param vol
     */
    public static void deleteVol(Vol vol) {
        em.getTransaction().begin();
        em.remove(vol);
        em.getTransaction().commit();
    }

    /**
     * Get amount of money available for association
     *
     * @param assosName
     * @return
     */
    public static double getArgent(String assosName) {
        Association a = em.find(Association.class, assosName);
        return a.getTresorerie().getSomme();
    }

    /**
     * Decrease amount of money for association
     *
     * @param assosName
     * @param argent
     */
    public static void decreaseMoney(String assosName, double argent) {
        Association m = em.find(Association.class, assosName);
        em.getTransaction().begin();

        m.getTresorerie().setSomme(m.getTresorerie().getSomme() - argent);

        em.getTransaction().commit();

    }

    public static long getNombre(String pays, String maladie) {
        Query q = em.createNamedQuery("Malade.nombreMaladeMaladie")
                .setParameter("nomMa", maladie)
                .setParameter("nompays", pays);
        long results = (long) q.getSingleResult();
        return results;
    }

    public static List<Vaccin> getVaccins(String maladie) {
        Query q = em.createNamedQuery("Vaccin.getVaccinWhereMaladie")
                .setParameter("nom", maladie);

        List<Vaccin> results = q.getResultList();

        return results;
    }

    public static List<Maladie> getMaladiesForCountry(String pays) {
        Query q = em.createNamedQuery("Malade.getMaladiesForCountry")
                .setParameter("nom", pays);

        List<Maladie> results = q.getResultList();
        return results;
    }


    public static Maladie getMaladie(String maladie) {
        return em.find(Maladie.class, maladie);
    }

    public static void addEnvoi(Envoi envoi) {
        em.getTransaction().begin();
        em.persist(envoi);
        em.getTransaction().commit();

    }

    public static void addEnvoi(Envoi envoi, EnvoiVaccin ev) {
        em.getTransaction().begin();
        em.persist(envoi);
        em.getTransaction().commit();

        em.getTransaction().begin();
        ev.setEnvoi(envoi);
        em.persist(ev);
        em.getTransaction().commit();

    }

    public static void addEnvoi(Envoi envoi, List<EnvoiVaccin> evs) {
        em.getTransaction().begin();
        em.persist(envoi);
        em.getTransaction().commit();

        for (EnvoiVaccin ev : evs) {
            em.getTransaction().begin();
            ev.setEnvoi(envoi);
            em.persist(ev);
            em.getTransaction().commit();
        }
    }


    //INSERT DATA TO DATABASE

    /**
     * Add all disease to DB
     */
    private static void instanciateMaladie() {
        Maladie tmp;
        Random rm = new Random();
        for (String maladie : lesMaladies) {
            tmp = new Maladie();
            tmp.setNom(maladie);
            tmp.setDelaiIncub(rm.nextDouble());
            tmp.setVaccin_volume(rm.nextDouble());

            em.getTransaction().begin();
            em.persist(tmp);
            em.getTransaction().commit();
        }
    }

    /**
     * Add all countries to DB
     */
    private static void instanciatePays() {
        Pays tmp;
        for (String pays : lesPays) {
            tmp = new Pays();
            tmp.setNom(pays);

            em.getTransaction().begin();
            em.persist(tmp);
            em.getTransaction().commit();
        }
    }

    /**
     * Add all associations to DB
     */
    private static void instanciateAssociation() {
        Association tmp;
        for (String assos : lesAssos) {
            tmp = new Association();
            tmp.setNom(assos);
            Argent a = new Argent();
            a.setSomme(10000);
            tmp.setTresorerie(a);
            em.getTransaction().begin();
            em.persist(a);
            em.persist(tmp);
            em.getTransaction().commit();
        }
    }
    private static void instacianteMalade(){
                System.out.println("Creating sick");
        List<Pays> listPays = em.createQuery("SELECT p FROM Pays p").getResultList();
        List<Maladie> listmal = em.createQuery("SELECT p FROM Maladie p").getResultList();
        Random rm = new Random();
        int nb = rm.nextInt(100) + 100;
        for (int i = 0; i < nb; i++) {

            Malade tmp = new Malade();
            tmp.setEtat(TypeMalade.Soignable);
            tmp.setMaladie(listmal.get(rm.nextInt(listmal.size() - 0)));
            tmp.setPays(listPays.get(rm.nextInt(listPays.size() - 0)));
            tmp.setDateContamination(new Date());
            em.getTransaction().begin();
            em.persist(tmp);
            em.getTransaction().commit();
        }

    }
}
