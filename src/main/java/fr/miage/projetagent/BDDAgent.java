package fr.miage.projetagent;

import fr.miage.projetagent.entity.Malade;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.TypeMalade;
import jade.core.Agent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class BDDAgent extends Agent{
    
    private EntityManager em;
    private EntityManagerFactory emf;
    
    private List<Maladie> maladies;
    private List<Pays> pays;

    @Override
    protected void setup(){
        emf =  Persistence.createEntityManagerFactory("agentBdd");
        em = emf.createEntityManager();
        
        String lesMaladies = "SELECT m FROM Maladie m"; 
        String lesPays = "SELECT m FROM Pays m"; 
        Query queryMaladies = em.createQuery(lesMaladies);
        maladies = queryMaladies.getResultList();
        Query queryPays = em.createQuery(lesPays);
        pays = queryPays.getResultList();
    }
    
    public void majBdd(){
        
        //intervalle de temps r�gulier
        Date aujourdhui = new Date();
        Random r = new Random();
        int rd = r.nextInt(102-1) + 1;
        Malade tmp;
        
        for (int i = 0; i < rd; i++) {
            //cr�er des malades
            int rM = r.nextInt(maladies.size()-0) + 0;
            int rP = r.nextInt(pays.size()-0) + 0;
            tmp = new Malade();
            tmp.setEtat(TypeMalade.Soignable);
            tmp.setMaladie(maladies.get(rM));
            tmp.setPays(pays.get(rP));
            tmp.setDateContamination(aujourdhui);

            em.getTransaction().begin();
            em.persist(tmp);
            em.getTransaction().commit();
        }
        
        
        //supprimer les malades non gu�rissables
        Query q1 = em.createNamedQuery("Malade.deleteMort");
        q1.getResultList();
        
        //supprimer vaccins d�pass� ==> mettre � jour metrics
        //supprimer vols non utilis�

    }


    //id�e metrics
    //nombre de vaccin non utlis� jete
    //nombre de vaccin envoye
    //nombre nombre d'envoi r�alis�
    //volume envoy�
    //volume r�serv�


    //quand ajoute un envoi
    //supprime les vaccins et le vol,
    //gu�rit des malades
    //mets � jour nb envoy� et volume

    //quand ajoute un vol
    //mettre � jour volume r�serv�
    
    @Override
    protected void takeDown(){
        em.close();
        emf.close();
        System.out.println("Je suis l'agent "+getLocalName()+" je me suis arreter");
   
        
    }
}