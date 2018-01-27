package fr.miage.projetagent;

import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.entity.Envoi;
import fr.miage.projetagent.entity.EnvoiVaccin;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.Vaccin;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class MainApp extends Application {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("agentBdd");
    static EntityManager em = entityManagerFactory.createEntityManager();

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        String agents = "bdd:fr.miage.projetagent.bdd.BddAgent;";
        BddAgent.addData();

     
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
        for (String assos : BddAgent.getAllAssosName()) {
            agents += assos + ":fr.miage.projetagent.agent.AssosAgent;";
        }
        



        System.out.println("finished sleep");

        ProfileImpl profile = new ProfileImpl();
        profile.setParameter("host", "192.168.0.15");
        profile.setParameter("main", "false");
        profile.setParameter("no-display", "true");
        profile.setParameter("agents", agents);

        Runtime rt = Runtime.instance();

        
        jade.wrapper.AgentContainer cont = rt.createAgentContainer(profile);


        Platform.runLater(() -> {
            Scene scene = new Scene(root);

            stage.setTitle("Eradique 1.0");
            stage.setScene(scene);
            stage.show();
        });


    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void testMethodeAgentEnvoiVaccin() {

        List<Pays> listPersons = em.createQuery("SELECT p FROM Pays p").getResultList();
        List<Vaccin> listv = em.createQuery("SELECT p FROM Vaccin p").getResultList();
        System.out.println(listPersons.get(0).getNom());
        System.out.println(listv.get(0).getNom().getNom());

        Envoi monEnvoi = new Envoi();
        monEnvoi.setPays(listPersons.get(0));
        monEnvoi.setDate(new Date());
        BddAgent.addEnvoi(monEnvoi);

        em.getTransaction().begin();
        EnvoiVaccin ev = new EnvoiVaccin();
        ev.setNb(120);
        ev.setLesVaccins(listv.get(0));
        ev.setEnvoi(monEnvoi);
        EnvoiVaccin ev2 = new EnvoiVaccin();
        ev2.setNb(123);
        ev2.setLesVaccins(listv.get(1));
        ev2.setEnvoi(monEnvoi);
        em.persist(ev);
        em.persist(ev2);
        em.getTransaction().commit();
    }

    public void testMethodeAgentAddVaccin() {
        Vaccin va = new Vaccin();
        va.setVolume(0.1);
        BddAgent.addVaccin("Grippe", va);
    }

    public void testMethodeAgentdecreaseMoney() {
        BddAgent.decreaseMoney("Emmaus", 50);
    }

    public void testMethodeAgentGetVaccin() {
        List<Vaccin> vl = BddAgent.getVaccins("Grippe");
        for (Vaccin vaccin : vl) {
            System.out.println(vaccin.getNom().getNom());
        }
    }

    public void testMethodeAgent() {

    }
}
