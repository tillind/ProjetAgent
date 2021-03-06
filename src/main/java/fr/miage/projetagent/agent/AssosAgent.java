package fr.miage.projetagent.agent;

import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.common.EnregistrerService;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

public class AssosAgent extends Agent {


    //priorité à traiter
    private Priority priority = new Priority();

    //liste des objectifs réalisés
    private List<Objectif> prioritiesDone = new ArrayList<>();

    //ce que l'association est actuellement en train de traiter
    private Objectif enCours = new Objectif();

    private double argent = 0;

    public Objectif getEnCours() {
        return enCours;
    }

    public void setEnCours(Objectif enCours) {
        this.enCours = enCours;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<Objectif> getPrioritiesDone() {
        return prioritiesDone;
    }

    public void setPrioritiesDone(List<Objectif> prioritiesDone) {
        this.prioritiesDone = prioritiesDone;
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
        this.addBehaviour(new AssosBehaviour(this));
    }

    @Override
    protected void takeDown() {
        EnregistrerService.unregisterService(this, "assos", "assos");
        super.takeDown();
    }

    public void update() {
        this.setPriority(BddAgent.getStatut(this.getLocalName()));
        this.setArgent(BddAgent.getArgent(this.getLocalName()));
        System.out.println(this.getLocalName() + "--------- updated prority");
        System.out.println(this.getPriority().toString());
        System.out.println(this.getArgent());
        System.out.println("---------");
    }
}
