package fr.miage.projetagent.agent;

import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.common.EnregistrerService;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.List;

public class AssosAgent extends Agent {


    //liste des status, ajouté en queue, lu en tête
    private List<Priority> priorities = new ArrayList<>();

    //ce que l'association est actuellement en train de traiter
    private Objectif enCours = new Objectif();

    private double argent = 0;

    public Objectif getEnCours() {
        return enCours;
    }

    public void setEnCours(Objectif enCours) {
        this.enCours = enCours;
    }

    public List<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Priority> priorities) {
        this.priorities = priorities;
    }

    public double getArgent() {
        return argent;
    }

    public void setArgent(double argent) {
        this.argent = argent;
    }

    @Override
    protected void setup() {
        EnregistrerService.registerService(this, "assos", this.getLocalName());
        //BddAgent.addAssosAgent(this); //s'enregsitre auprès de la bdd aussi
        this.addBehaviour(new AssosBehaviour(this));
    }



    @Override
    protected void takeDown() {
        EnregistrerService.unregisterService(this, "assos", "assos");
        BddAgent.removeAssosAgent(this);
        super.takeDown();
    }
}
