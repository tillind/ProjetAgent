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

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        String agents = "bdd:fr.miage.projetagent.bdd.BddAgent;";

        for (String assos : BddAgent.getAllAssosName()) {
            agents += assos + ":fr.miage.projetagent.agent.AssosAgent;";
        }

        ProfileImpl profile = new ProfileImpl();
        profile.setParameter("host", "192.168.43.79");
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

}
