package fr.miage.projetagent;

import fr.miage.projetagent.bdd.BddAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import javafx.application.Application;

import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));


        Scene scene = new Scene(root);

        stage.setTitle("Eradique 1.0");
        stage.setScene(scene);
        stage.show();

        String agents = "bdd:fr.miage.projetagent.bdd.BddAgent;";
        for (String assos : BddAgent.getAllAssosName()) {
            agents += assos + ":fr.miage.projetagent.agent.AssosAgent;";
        }

        ProfileImpl profile = new ProfileImpl();
        profile.setParameter("host", "10.10.170.129");
        profile.setParameter("main", "false");
        profile.setParameter("no-display", "true");
        profile.setParameter("agents", agents);

        Runtime rt = Runtime.instance();

        jade.wrapper.AgentContainer cont = rt.createAgentContainer(profile);
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
