package fr.miage.projetagent.secretairecompagnie;

import fr.miage.projetagent.Agent.CommunicationBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;

public class CompagnieBehaviour extends ContractNetInitiator {

    public CompagnieBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println("--------Message is send to all compagnies");
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("--------" + responses.size() + "responses");
        System.out.println("--------" + acceptances.size() + "acceptances");
        CommunicationBehaviour parent = (CommunicationBehaviour) this.parent;
        this.reset(parent.startCompagnies());
    }
}
