/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent;

import fr.miage.projetagent.entity.Association;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Pays;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Arthur
 */
public class SceneController implements Initializable {

    @FXML
    private Button giveButton;
    @FXML
    private TextArea somme;
    @FXML
    private RadioButton choicePays, choiceMaladie;
    @FXML
    private ChoiceBox<String> cbPays, cbMaladie;
    @FXML
    private ChoiceBox<String> assoc;
    @FXML
    private TableView<DataTable> tableData;
    @FXML
    private TableColumn<DataTable, String> c1, c2, c3;
    final ToggleGroup radioGroupAssoc = new ToggleGroup();
    final ToggleGroup radioGroup = new ToggleGroup();


    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                try {
            Thread.sleep(45000);
        } catch(InterruptedException e) {
            System.out.println("got interrupted!");
        }
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("agentBdd");
    EntityManager em = entityManagerFactory.createEntityManager();
        choicePays.setToggleGroup(radioGroup);
        choiceMaladie.setToggleGroup(radioGroup);
        //List<Pays> maladies = em.createQuery("SELECT p FROM Pays p").getResultList();
        //List<Maladie> pays = em.createQuery("SELECT p FROM Maladie p").getResultList();
        System.out.println("fr.miage.projetagent.SceneController.initialize()");
        List<Association> associations = em.createQuery("SELECT p FROM Association p").getResultList();
        for (Association association : associations) {
            System.out.println("fr.miage.projetagent.SceneController.initialize()");
            System.out.println(association.getNom());
        }
        // TODO
      /*  ObservableList<String> itemsPays = FXCollections.observableArrayList();
        pays.forEach((p) -> {
            itemsPays.add(p.getNom());
        });
        cbPays.setItems(itemsPays);
        ObservableList<String> itemsMaladie = FXCollections.observableArrayList();
        maladies.forEach((m) -> {
            itemsMaladie.add(m.getNom());
        });
        cbMaladie.setItems(itemsMaladie);*/
        ObservableList<String> itemsAssoc = FXCollections.observableArrayList();
        associations.forEach((a) -> {
            itemsAssoc.add(a.getNom());
        });
        assoc.setItems(itemsAssoc);

        // table
        tableData.setEditable(true);
        c1.setCellValueFactory(cellData -> cellData.getValue().paysProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().maladieProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        ObservableList<DataTable> data = FXCollections.observableArrayList(new DataTable("somalie", "rhume", "100"));
        tableData.setItems(data);

    }

    public void moreMoney() {
        /**
         * TO DO modifier le compte de l'association
         */
//        String updateMoney ="UPDATE table SET col ="+somme.getText()+"WHERE nomEtreprise ='"+radioGroupAssoc.selectedToggleProperty().getValue().toString()+"'";
//        Query money = em.creatQuery(updateMoney);
    }

    public ArrayList<String> getPrio() {
        ArrayList<String> prio = new ArrayList<>();
        prio.add(assoc.getValue());
        if (choicePays.isSelected()) {
            prio.add(getPays());
            prio.add(getMaladie());
        }
        if (choiceMaladie.isSelected()) {
            prio.add(getMaladie());
            prio.add(getPays());
        }
        System.out.println(prio);
        return (prio);

    }

    public String getPays() {
        return cbPays.getValue();
    }

    public String getMaladie() {
        return cbMaladie.getValue();
    }

}
