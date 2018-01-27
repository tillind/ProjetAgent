package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.entity.Argent;
import fr.miage.projetagent.entity.Association;
import fr.miage.projetagent.entity.Envoi;
import fr.miage.projetagent.entity.EnvoiVaccin;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.Vaccin;
import fr.miage.projetagent.entity.Vol;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class BddAgent extends Agent {

    static List<AssosAgent> assosAgent = new ArrayList<>();
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("agentBdd");
    static EntityManager em = entityManagerFactory.createEntityManager();
    static String[] lesPays = {"Guinee","Maraoc","Tunisie","Gambie","Botsawana","Cameroun","Senegal"};
    @Override
    protected void setup() {
        this.addBehaviour(new BddBehaviour(this, 45));
    }

    //TODO GADEAU get the real assos name
    public static List<String> getAllAssosName(){
       
        Query q = em.createNamedQuery("Association.findAll", Association.class);
        List<Association> results = q.getResultList();
        
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(results.get(0).getNom());
        /*results.forEach((assoc)->{
            tmp.add(assoc.getNom());
        });*/
        return tmp;
    }

    public static void addAssosAgent(AssosAgent a){
        assosAgent.add(a);
        a.setPriority(getStatut(a.getLocalName()));
        a.setArgent(getArgent(a.getLocalName()));
    }

    public static void removeAssosAgent(AssosAgent a){
        assosAgent.remove(a);
    }


    //TODO GADEAU
    public static Priority getStatut(String assosName){
        Association a =em.find(Association.class,assosName);
        Priority p = new Priority();
        
        return new Priority();
    }

    //TODO GADEAU
    public static double getArgent(String assosName){
        Association a =em.find(Association.class,assosName);
        return a.getTresorerie().getSomme();
    }

    //TODO GADEAU
    public static void addData(){
        instanciateAssociation();
        instanciateMaladie();
        instanciatePays();
    }

    //TODO GADEAU
    public static void addVaccin(String nom, Vaccin vaccin){
         Maladie m =em.find(Maladie.class,nom);
         em.getTransaction().begin();
         
         vaccin.setNom(m);
         em.persist(vaccin);
         em.getTransaction().commit();
        //TODO ajouter vaccin pour la maladie
    }

    //TODO GADEAU
    public static void deleteVaccin(Vaccin vaccins){
        em.getTransaction().begin();
        em.remove(vaccins);
        em.getTransaction().commit();
    }

    //TODO GADEAU
    public static void deleteVol(Vol vol){
        em.getTransaction().begin();
        em.remove(vol);
        em.getTransaction().commit();
    }

    //TODO GADEAU
    public static void decreaseMoney(String assosName, double argent){
         Association m =em.find(Association.class,assosName);
         em.getTransaction().begin();
      
         m.getTresorerie().setSomme(m.getTresorerie().getSomme()-argent);

         em.getTransaction().commit();

    }

    public static int getNombre(String pays, String maladie){
         Query q = em.createNamedQuery("Malade.nombreMaladeMaladie")
                 .setParameter("nomMa", maladie)
                 .setParameter("nompays", pays);
       int results = (int) q.getSingleResult();
        return results;
    }

    public static List<Vaccin> getVaccins(String maladie){
       Query q = em.createNamedQuery("Vaccin.getVaccinWhereMaladie")
                 .setParameter("nom", maladie);

       List<Vaccin> results =  q.getResultList();
        
        return results ;
    }

    public static List<Maladie> getMaladiesForCountry(String pays){
           Query q = em.createNamedQuery("Malade.getMaladiesForCountry")
                 .setParameter("nom", pays);

       List<Maladie> results =  q.getResultList();
        return results;
    }


    public static Maladie getMaladie(String maladie){ 
        return em.find(Maladie.class, maladie);
    }

    public static void addEnvoi (Envoi envoi){
        em.getTransaction().begin();
        em.persist(envoi);
        em.getTransaction().commit();

    }
    public static void addEnvoi (Envoi envoi,EnvoiVaccin ev){
        em.getTransaction().begin();
        em.persist(envoi);
        em.getTransaction().commit();
        
        em.getTransaction().begin();           
            ev.setEnvoi(envoi);
            em.persist(ev);
        em.getTransaction().commit();

    }
    
    public static void addEnvoi (Envoi envoi, List<EnvoiVaccin> evs){
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
    
    
    private static void instanciateMaladie(){
        Maladie mal = new Maladie();
        mal.setNom("Grippe");
        Vaccin va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
        
        em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
        
         mal = new Maladie();
        mal.setNom("sida");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.1);
       
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
        
         mal = new Maladie();
        mal.setNom("bronchite");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.5);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("choléra");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("coqueluche");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.1);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("diphtérie");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.5);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("encéphalite");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.9);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("fièvre");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.4);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("hépatite A");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("hépatite B");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.3);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("Rage");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.8);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("rubéole");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("varicelle");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.1);
        em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("variole");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("tétanos");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.6);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie(); 
        mal.setNom("oreillons");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.5);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("zona");
        va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.1);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
         mal = new Maladie();
        mal.setNom("fièvre jaune");
         va = new Vaccin();
        va.setNom(mal);
        va.setVolume(0.2);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
        mal = new Maladie();
        mal.setNom("rotavirus");
        va = new Vaccin();
        va.setNom(mal);
        va.setVolume(1.1);
          em.getTransaction().begin();
        em.persist(va);
        em.persist(mal);
        em.getTransaction().commit();
    }

    private static void instanciatePays(){
        Pays tmp;
        for (String pays : lesPays ) {
            tmp = new Pays();
            tmp.setNom(pays);
            
            em.getTransaction().begin();
            em.persist(tmp);
            em.getTransaction().commit();
        }
    }
    private static void instanciateAssociation(){
        Association assocA = new Association();
        assocA.setNom("GrippeSansFrontiére");
        Argent a= new Argent();
        a.setSomme(10000);
        assocA.setTresorerie(a);
       em.getTransaction().begin();
        em.persist(a);
        em.persist(assocA);
        em.getTransaction().commit();
        
        assocA = new Association();
        assocA.setNom("Emmaus");
        a= new Argent();
        a.setSomme(10000);
        assocA.setTresorerie(a);
        em.getTransaction().begin();
        em.persist(a);
        em.persist(assocA);
        em.getTransaction().commit();

        assocA = new Association();
        assocA.setNom("MiageSansFrontiere");
        a= new Argent();
        a.setSomme(10000);
        assocA.setTresorerie(a);
        em.getTransaction().begin();
        em.persist(a);
        em.persist(assocA);
        em.getTransaction().commit();
        
        assocA = new Association();
        assocA.setNom("Helpers");
        a= new Argent();
        a.setSomme(10000);
        assocA.setTresorerie(a);
        em.getTransaction().begin();
        em.persist(a);
        em.persist(assocA);
        em.getTransaction().commit();

    }
}
