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

        this.deleteVaccinPerimer();
        this.clearSick();
        this.createRandomMalade();

    }

    public void deleteVaccinPerimer() {

        Session session = getSessionFactory().openSession();

        session.getTransaction().begin();
        Query q = session.createNativeQuery("DELETE FROM vaccin v WHERE v.dateFin <  CURRENT_DATE ");
        q.executeUpdate();
        session.getTransaction().commit();

        session.close();
    }

    /**
     * Delete all sick people
     */
    public void clearSick() {

        Session session = getSessionFactory().openSession();

        session.getTransaction().begin();
        Query q = session.createNativeQuery("DELETE FROM malade" +
                " WHERE id IN (SELECT id" +
                " FROM malade m " +
                "JOIN maladie mi ON m.maladie_nom = mi.nom" +
                " WHERE datecontamination  + INTERVAL '1m' * delaiincub < current_timestamp" +
                " OR etat <> 'Soignable') ");
        q.executeUpdate();
        session.getTransaction().commit();

        session.close();
    }


    public void createRandomMalade() {

        BddAgent.instacianteMalade();

    }

}
