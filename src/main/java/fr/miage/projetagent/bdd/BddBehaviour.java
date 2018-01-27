package fr.miage.projetagent.bdd;

import fr.miage.projetagent.entity.Malade;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.TypeMalade;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static fr.miage.projetagent.bdd.BddAgent.em;

public class BddBehaviour extends TickerBehaviour {


    public BddBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {

        //supprime malades mort, vaccins périmés, vol dépassé, créer malade

        this.deleteMadaladeMort();
        this.deleteVaccinPerimer();
        this.deleteVolDepasser();
        this.createRandomMalade();
        // }
    }

    public void deleteMadaladeMort() {
        em.getTransaction().begin();
        Query q = em.createNativeQuery("DELETE FROM malade m WHERE m.etat = 'Non_soignable'");
        //Query q = em.createNamedQuery("Malade.deleteMort");
        q.executeUpdate();
        em.getTransaction().commit();
    }

    public void deleteVaccinPerimer() {
        em.getTransaction().begin();
        Query q = em.createNativeQuery("DELETE FROM vaccin v WHERE v.dateFin =  CURRENT_DATE ");
        q.executeUpdate();
        em.getTransaction().commit();

    }

    public void deleteVolDepasser() {
        em.getTransaction().begin();
        Query q = em.createNativeQuery("DELETE FROM vol v WHERE v.date >= CURRENT_DATE  ");

        q.executeUpdate();
        em.getTransaction().commit();

    }

    public void createRandomMalade() {

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
