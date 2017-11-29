package fr.miage.projetagent.Agent;

import fr.miage.projetagent.BDD.BddAgent;
import fr.miage.projetagent.Common.EnregistrerService;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AssosAgent extends Agent {


    //est mis à jour par la bdd
    private Statut statut = new Statut();

    //ce que l'association est actuellement en train de traiter
    public Objectif enCours = new Objectif();

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    @Override
    protected void setup() {
        EnregistrerService.registerService(this, "assos", this.getLocalName());
        BddAgent.addAssosAgent(this); //s'enregsitre auprès de la bdd aussi
        this.addBehaviour(new CommunicationBehaviour(this));
    }



    @Override
    protected void takeDown() {
        EnregistrerService.unregisterService(this, "assos", "assos");
        BddAgent.removeAssosAgent(this);
        super.takeDown();
    }
}
