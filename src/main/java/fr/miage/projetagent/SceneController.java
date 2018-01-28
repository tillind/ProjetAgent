/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent;

import fr.miage.projetagent.bdd.BddAgent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SceneController implements Initializable {

    @FXML
    private CheckBox maladieSelect;
    @FXML
    private CheckBox paysSelect;
    @FXML
    private TextArea somme;
    @FXML
    private ChoiceBox<String> cbPays, cbMaladie;
    @FXML
    private ChoiceBox<String> assoc;
    @FXML
    private TableView<DataTable> tableData;
    @FXML
    private TableColumn<DataTable, String> c1, c2, c3;
    final ToggleGroup radioGroup = new ToggleGroup();
    static final String[] pays = BddAgent.lesPays;
    static final String[] associations = BddAgent.lesAssos;
    static final String[] maladie = BddAgent.lesMaladies;


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
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }

        ObservableList<String> itemsPays = FXCollections.observableArrayList();
        itemsPays.addAll(Arrays.asList(pays));
        cbPays.setItems(itemsPays);
        cbPays.getSelectionModel().selectFirst();

        ObservableList<String> itemsMaladie = FXCollections.observableArrayList();
        itemsMaladie.addAll(maladie);
        cbMaladie.setItems(itemsMaladie);
        cbMaladie.getSelectionModel().selectFirst();

        ObservableList<String> itemsAssoc = FXCollections.observableArrayList();
        itemsAssoc.addAll(associations);
        assoc.setItems(itemsAssoc);
        assoc.getSelectionModel().selectFirst();

        // table
        tableData.setEditable(true);
        c1.setCellValueFactory(cellData -> cellData.getValue().paysProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().maladieProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        ObservableList<DataTable> data = FXCollections.observableArrayList();
        for (String p : pays) {
            for (String m : maladie) {
                long res = BddAgent.getNombre(p, m);
                data.add(new DataTable(p, m, String.valueOf(res)));
            }
        }
        tableData.setItems(data);

    }

    public void refresh() {
        ObservableList<DataTable> data = FXCollections.observableArrayList();
        for (String p : pays) {
            for (String m : maladie) {
                long res = BddAgent.getNombre(p, m);
                data.add(new DataTable(p, m, String.valueOf(res)));
            }
        }
        tableData.setItems(data);
        tableData.getItems().sorted();
    }

    public void moreMoney() {
        /**
         * TO DO modifier le compte de l'association
         */
//        String updateMoney ="UPDATE table SET col ="+somme.getText()+"WHERE nomEtreprise ='"+radioGroupAssoc.selectedToggleProperty().getValue().toString()+"'";
//        Query money = em.creatQuery(updateMoney);
    }

    public void getPrio() {
        BddAgent.getStatut(assoc.getValue(), getPays(), getMaladie());
    }

    public String getPays() {
        if (paysSelect.isSelected()) {
            return cbPays.getValue();
        } else {
            return null;
        }
    }

    public String getMaladie() {
        if (maladieSelect.isSelected()) {
            return cbMaladie.getValue();
        } else {
            return null;
        }
    }

}
