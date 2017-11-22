package fr.miage.projetagent.SecretaireLabo;

import fr.miage.projetagent.AssocBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;

public class LaboBehaviour extends ContractNetInitiator {


    public LaboBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println("--------Message is send to all labos");
    }


    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("--------"+responses.size()+"responses");
        System.out.println("--------"+acceptances.size()+"acceptances");
        AssocBehaviour parent = (AssocBehaviour) this.parent;
        this.reset(parent.startLabo());
    }



}
