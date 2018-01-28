package fr.miage.projetagent.bdd;

import fr.miage.projetagent.entity.Malade;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.TypeMalade;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static fr.miage.projetagent.bdd.HibernateSessionProvider.getSessionFactory;


public class BddBehaviour extends TickerBehaviour {


    public BddBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {

        //supprime malades mort, vaccins périmés, vol dépassé, créer malade

        //this.deleteMadaladeMort();
        //this.deleteVaccinPerimer();
        this.createRandomMalade();
        // }
    }

    public void deleteMadaladeMort() {
        Session session = getSessionFactory().openSession();

        session.getTransaction().begin();
        Query q = session.createNativeQuery("DELETE FROM malade m WHERE m.etat = 'Non_soignable'");
        q.executeUpdate();
        session.getTransaction().commit();

        session.close();

    }

    public void deleteVaccinPerimer() {

        Session session = getSessionFactory().openSession();

        session.getTransaction().begin();
        Query q = session.createNativeQuery("DELETE FROM vaccin v WHERE v.dateFin =  CURRENT_DATE ");
        q.executeUpdate();
        session.getTransaction().commit();

        session.close();
    }


    public void createRandomMalade() {


        Session session = getSessionFactory().openSession();


        System.out.println("Creating sick");
        List<Pays> listPays = session.createQuery("SELECT p FROM Pays p").getResultList();
        List<Maladie> listmal = session.createQuery("SELECT p FROM Maladie p").getResultList();
        Random rm = new Random();
        int nb = rm.nextInt(100) + 100;
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
