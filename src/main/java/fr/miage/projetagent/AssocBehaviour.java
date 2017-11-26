package fr.miage.projetagent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.SecretaireLabo.CFP;
import fr.miage.projetagent.SecretaireLabo.LaboBehaviour;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.secretairecompagnie.CompagnieBehaviour;
import fr.miage.projetagent.secretairecompagnie.CompagnieMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Date;

public class AssocBehaviour extends SequentialBehaviour {

    final Gson gson = new GsonBuilder().create();

    public AssocBehaviour(Agent a) {
        super(a);
        this.addSubBehaviour(new LaboBehaviour(myAgent, startLabo())); //labo behaviour
        this.addSubBehaviour(new CompagnieBehaviour(myAgent, startCompagnies())); //compagnie behaviour
    }

    public ACLMessage startLabo() {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        CFP content = new CFP("", 0, new Date());
        message.setContent(gson.toJson(content));
        AssocAgent agent = (AssocAgent) this.myAgent;
        message.setSender(agent.getAID());
        for (AID aid : agent.getLabos()) {
            message.addReceiver(aid);
        }
        return message;
    }

    public ACLMessage startCompagnies() {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        CompagnieMessage content = new CompagnieMessage(0, new Date(), new Pays());
        message.setContent(gson.toJson(content));
        AssocAgent agent = (AssocAgent) this.myAgent;
        message.setSender(agent.getAID());
        for (AID aid : agent.getCompagnies()) {
            message.addReceiver(aid);
        }
        return message;
    }

}
