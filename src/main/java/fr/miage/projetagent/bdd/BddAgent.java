package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.entity.Envoi;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Vaccin;
import fr.miage.projetagent.entity.Vol;
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
        String[] result = {"assoc1"};
        return result;
    }

    public static void addAssosAgent(AssosAgent a){
        System.out.println("BDD : There is a new agent");
        assosAgent.add(a);
        a.getPriorities().add(BddAgent.getStatut(a.getLocalName()));
    }

    public static void removeAssosAgent(AssosAgent a){
        assosAgent.remove(a);
    }


    //TODO GADEAU
    public static Priority getStatut(String assosName){
        //retourner un statut pour l'assos en param
        //TODO recup les priorités chez Arthur
        return new Priority();
    }

    //TODO GADEAU
    public static double getArgent(String assosName){

        return 0;
    }

    //TODO GADEAU
    public static void addData(){
        //TODO initial data (la base est drop-and-create)
        //ajouter maladie, pays, assoc
    }

    //TODO GADEAU
    public static void addVaccin(String nom, Vaccin vaccin){
        //TODO ajouter vaccin pour la maladie
    }

    //TODO GADEAU
    public static void deleteVaccin(Vaccin vaccins){
        //TODO supprimer le vaccin
    }

    //TODO GADEAU
    public static void deleteVol(Vol vol){
        //TODO supprimer le vol
    }

    //TODO GADEAU
    public static void decreaseMoney(String assosName, double argent){

    }

    public static int getNombre(String pays, String maladie){
        return 0;
    }

    public static List<Vaccin> getVaccins(String maladie){
        //qui sont dispo et triés par date de péremtion croissante
        return new ArrayList<>();
    }

    public static List<Maladie> getMaladiesForCountry(String pays){
        //qui sont triées par nombre de malade décroissant
        return new ArrayList<>();
    }


    public static Maladie getMaladie(String maladie){
        return new Maladie();
    }

    public static void addEnvoi (Envoi envoi){

    }


}
