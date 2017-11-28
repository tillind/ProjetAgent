package fr.miage.projetagent.Agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.Labo.CFP;
import fr.miage.projetagent.Labo.LaboBehaviour;
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
        //TODO troisième behaviour qui envoie et qui reinit
    }


    public void start(){
        AssocAgent assocAgent = (AssocAgent) this.myAgent;
        assocAgent.enCours.setPays(assocAgent.pays);
        assocAgent.enCours.setDateSouhaite(assocAgent.date);
        assocAgent.enCours.setNombre(assocAgent.nb);
        assocAgent.enCours.setVaccin(assocAgent.maladie);
    }



    public ACLMessage startLabo() {
        AssocAgent assocAgent = (AssocAgent) this.myAgent;
        Objectif objectif = assocAgent.enCours;

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        //CFP content = new CFP(objectif.getVaccin(), objectif.getNombre(), objectif.getDateSouhaite());
        CFP content = new CFP("rage", 2, new Date());


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
