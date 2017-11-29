package fr.miage.projetagent.BDD;

import fr.miage.projetagent.Agent.AssosAgent;
import fr.miage.projetagent.Agent.Statut;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

public class BddAgent extends Agent {

    static List<AssosAgent> assosAgent = new ArrayList<>();

    @Override
    protected void setup() {
        //TODO define time
        this.addBehaviour(new BddBehaviour(this, 5));
    }

    //TODO GADEAU get the real assos name
    public static String[] getAllAssosName(){
        String[] result = {"assoc1","assoc2"};
        return result;
    }

    public static void addAssosAgent(AssosAgent a){
        System.out.println("BDD : There is a new agent");
        assosAgent.add(a);
        a.setStatut(BddAgent.getStatut(a.getLocalName()));
    }

    public static void removeAssosAgent(AssosAgent a){
        assosAgent.remove(a);
    }


    //TODO GADEAU
    public static Statut getStatut(String assosName){
        //retourner un statut pour l'assos en param
        return new Statut();
    }

    //TODO GADEAU
    public static void addData(){
        //ajouter maladie, pays, assoc
    }

}
