package fr.miage.projetagent.SecretaireLabo;

import fr.miage.projetagent.AssocBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.HashSet;
import java.util.Set;
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
        Set<ACLMessage> proposeResponse = new HashSet<>();
        for (Object response : responses){
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative()==ACLMessage.PROPOSE){
                proposeResponse.add(reponseMessage);
            }
            ACLMessage agree = reponseMessage.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            myAgent.send(agree);
        }
        //boolean out =choosePropose(proposeResponse);

        //if (!out){
            AssocBehaviour parent = (AssocBehaviour) this.parent;
            this.reset(parent.startLabo());
        //}

    }


    public boolean choosePropose(Set<ACLMessage> proposeResponse){
        return false;
    }

}
