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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.miage.projetagent.bdd.BddAgent;

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

        choicePays.setToggleGroup(radioGroup);
        choiceMaladie.setToggleGroup(radioGroup);
//        String lesMaladies = "SELECT m FROM Maladie m";
//        String lesPays = "SELECT m FROM Pays m";
//        Query queryMaladies = em.createQuery(lesMaladies);
//        Query queryPays = em.createQuery(lesPays);
        String[] pays = {"Guinee", "Maraoc", "Tunisie", "Gambie", "Botsawana", "Cameroun", "Senegal"};
        String[] associations = {"GrippeSansFrontiére", "Emmaus", "MiageSansFrontiere", "Helpers"};
        String[] maladie={"Grippe","sida","bronchite","choléra","coqueluche","diphtérie","encéphalite","fièvre","hépatite A","hépatite B","Rage","rubéole","varicelle","variole","tétanos","oreillons","zona","fièvre jaune","rotavirus"};
        

        ObservableList<String> itemsPays = FXCollections.observableArrayList();
        itemsPays.addAll(Arrays.asList(pays));
        cbPays.setItems(itemsPays);

        ObservableList<String> itemsMaladie = FXCollections.observableArrayList();
        itemsMaladie.addAll(maladie);
        cbMaladie.setItems(itemsMaladie);

        ObservableList<String> itemsAssoc = FXCollections.observableArrayList();
        itemsAssoc.addAll(associations);
        assoc.setItems(itemsAssoc);

        // table
        tableData.setEditable(true);
        c1.setCellValueFactory(cellData -> cellData.getValue().paysProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().maladieProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        ObservableList<DataTable> data = FXCollections.observableArrayList();
        for(String p:pays){
            for(String m:maladie){
                int res = BddAgent.getNombre(p, m);
                data.add(new DataTable(p, m, String.valueOf(res)));
            }
        }
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
