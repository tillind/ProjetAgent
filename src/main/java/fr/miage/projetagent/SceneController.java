/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent;

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
import javax.persistence.Query;

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
    private CheckBox assoc1, assoc2;
    final ToggleGroup radioGroupAssoc = new ToggleGroup();
    final ToggleGroup radioGroup = new ToggleGroup();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("agentBdd");
//        EntityManager em = emf.createEntityManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        choicePays.setToggleGroup(radioGroup);
        choiceMaladie.setToggleGroup(radioGroup);
//        String lesMaladies = "SELECT m FROM Maladie m";
//        String lesPays = "SELECT m FROM Pays m";
//        Query queryMaladies = em.createQuery(lesMaladies);
//        Query queryPays = em.createQuery(lesPays);
        List<Maladie> maladies = new ArrayList<>();
        List<Pays> pays = new ArrayList<>();
//        maladies = queryMaladies.getResultList();
//        pays=queryPays.getResultList();
        Pays p1 = new Pays();
        Pays p2 = new Pays();
        Maladie m1 = new Maladie();
        Maladie m2 = new Maladie();
        p1.setNom("Guinée");
        m1.setNom("Dysentrie");
        pays.add(p1);
        pays.add(p2);
        maladies.add(m1);
        maladies.add(m2);
        // TODO
        ObservableList<String> itemsPays = FXCollections.observableArrayList();
        for (Pays p : pays) {
            itemsPays.add(p.getNom());
        }
        cbPays.setItems(itemsPays);
        ObservableList<String> itemsMaladie = FXCollections.observableArrayList();
        for (Maladie m : maladies) {
            itemsMaladie.add(m.getNom());
        }
        cbMaladie.setItems(itemsMaladie);

    }

    public void moreMoney() {
        /**
         * TO DO modifier le compte de l'association
         */
//        String updateMoney ="UPDATE table SET col ="+somme.getText()+"WHERE nomEtreprise ='"+radioGroupAssoc.selectedToggleProperty().getValue().toString()+"'";
//        Query money = em.creatQuery(updateMoney);
    }

    public ArrayList<String> sendPrio() {
        ArrayList<String> prio = new ArrayList<>();
        if (choicePays.isSelected()) {
            prio.add(cbPays.getValue());
            prio.add(cbMaladie.getValue());
            if (assoc1.isSelected()) {
                prio.add(assoc1.getText());
            }
            if (assoc2.isSelected()) {
                prio.add(assoc2.getText());
            }
        }
        if (choiceMaladie.isSelected()) {
            prio.add(cbMaladie.getValue());
            prio.add(cbPays.getValue());
            if (assoc1.isSelected()) {
                prio.add(assoc1.getText());
            }
            if (assoc2.isSelected()) {
                prio.add(assoc2.getText());
            }
        }
        System.out.println(prio);
        return (prio);

    }
    public String getPays(){
        return cbPays.getValue();
    }
    public String getMaladie(){
        return cbMaladie.getValue();
    }

}
