package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.entity.Association;
import fr.miage.projetagent.entity.Envoi;
import fr.miage.projetagent.entity.Maladie;
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
    @Override
    protected void setup() {
        
        //TODO define time
        this.addBehaviour(new BddBehaviour(this, 5));
    }

    //TODO GADEAU get the real assos name
    public static List<String> getAllAssosName(){
       
        Query q = em.createNamedQuery("Association.findAll", Association.class);
        List<Association> results = q.getResultList();
        ArrayList<String> tmp = new ArrayList<>();
        results.forEach((assoc)->{
            tmp.add(assoc.getNom());
        });
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
        
        return new Priority();
    }

    //TODO GADEAU
    public static double getArgent(String assosName){
        Association a =em.find(Association.class,assosName);
        return a.getTresorerie().getSomme();
    }

    //TODO GADEAU
    public static void addData(){
        //TODO initial data (la base est drop-and-create)
        //ajouter maladie, pays, assoc
    }

    //TODO GADEAU
    public static void addVaccin(String nom, Vaccin vaccin){
         Maladie m =em.find(Maladie.class,nom);
         em.getTransaction().begin();
         
         vaccin.setNom(m);
         //em.persist(m);
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
    public static void decreaseMoney(String assosName, int argent){
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


}
