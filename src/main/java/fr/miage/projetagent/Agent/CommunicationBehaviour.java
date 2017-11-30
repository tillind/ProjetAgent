package fr.miage.projetagent.Agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.Labo.CFP;
import fr.miage.projetagent.Labo.LaboBehaviour;
import fr.miage.projetagent.compagnie.CompagnieBehaviour;
import fr.miage.projetagent.compagnie.CompagnieMessage;
import fr.miage.projetagent.entity.Pays;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CommunicationBehaviour extends SequentialBehaviour {

    final Gson gson = new GsonBuilder().create();
    public final String labosType = "labo";
    public final String compagnieType = "compagnie";


    public CommunicationBehaviour(Agent a) {
        super(a);
        init();
        this.addSubBehaviour(new LaboBehaviour(myAgent, startLabo())); //labo behaviour
        this.addSubBehaviour(new CompagnieBehaviour(myAgent, startCompagnies())); //compagnie behaviour
    }

    /**
     * Copie l'état actuel de l'association
     * C'est l'objectif à résoudre
     */
    public void init() {
        AssosAgent assocAgent = (AssosAgent) this.myAgent;
        assocAgent.enCours.setPays(assocAgent.getStatut().getPays());
        assocAgent.enCours.setDateMort(assocAgent.getStatut().getDate());
        assocAgent.enCours.setNombre(assocAgent.getStatut().getNombre());
        assocAgent.enCours.setVaccin(assocAgent.getStatut().getMaladie());
        assocAgent.enCours.setVolume(assocAgent.getStatut().getVolume());
        System.out.println("prioriu = "+assocAgent.getStatut().getNombre());
    }


    /**
     * Renvoie une liste d'ID des labos ou des compagnies
     * @return
     */
    private Set<AID> getAID(String type) {
        Set<AID> labos = new HashSet<AID>();
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(this.myAgent, dfd);
            for (int i = 0; i < result.length; i++) {
                DFAgentDescription desc = (DFAgentDescription) result[i];
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    if (sd.getType().equals(type)) {
                        labos.add(desc.getName());
                    }
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("--------There are " + labos.size() + type);
        return labos;
    }


    /**
     * Envoie un message à tous les labos
     * @return
     */
    public ACLMessage startLabo() {
        AssosAgent assocAgent = (AssosAgent) this.myAgent;
        Objectif objectif = assocAgent.enCours;

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        //CFP content = new CFP(objectif.getVaccin(), objectif.getNombre(), objectif.getDateSouhaite());
        CFP content = new CFP("rage", 2, new Date());


        message.setContent(gson.toJson(content));

        for (AID aid : getAID(labosType)) {
            message.addReceiver(aid);
        }
        return message;
    }

    /**
     * Envoie un message à toutes les compagnies
     * @return
     */
    public ACLMessage startCompagnies() {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        CompagnieMessage content = new CompagnieMessage(0, new Date(), new Pays());
        message.setContent(gson.toJson(content));
        for (AID aid : getAID(compagnieType)) {
            message.addReceiver(aid);
        }
        return message;
    }

}