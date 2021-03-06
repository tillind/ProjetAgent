package fr.miage.projetagent.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.compagnie.CompagnieBehaviour;
import fr.miage.projetagent.compagnie.CompagnieMessage;
import fr.miage.projetagent.labo.CFP;
import fr.miage.projetagent.labo.LaboBehaviour;
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
        //for compagnie behaviour, we are not sending a message just yet
        this.addSubBehaviour(new CompagnieBehaviour(myAgent, null)); //compagnie behaviour
    }


    /**
     * Copie l'état actuel de l'association
     * C'est l'objectif à résoudre
     */
    public void init() {
        AssosAgent assocAgent = (AssosAgent) this.myAgent;

        assocAgent.update();

        assocAgent.getEnCours().setPays(assocAgent.getPriority().getPays());
        assocAgent.getEnCours().setDateMort(assocAgent.getPriority().getDate());
        assocAgent.getEnCours().setNombre(assocAgent.getPriority().getNombre() - assocAgent.getPriority().getNbVaccin());
        assocAgent.getEnCours().setVaccin(assocAgent.getPriority().getMaladie());
        assocAgent.getEnCours().setVolume(assocAgent.getPriority().getVolume());
    }

    @Override
    public int onEnd() {
        System.out.println("COMMUNICATION BEHAVIOUR IS RESTARTED");
        reset();
        myAgent.addBehaviour(new CommunicationBehaviour(myAgent));
        return super.onEnd();
    }

    /**
     * Renvoie une liste d'ID des labos ou des compagnies
     *
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
     *
     * @return
     */
    public ACLMessage startLabo() {
        while (getAID(labosType).isEmpty()){
            try {
                System.out.println("WAITING FOR LABO");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AssosAgent assocAgent = (AssosAgent) this.myAgent;
        Objectif objectif = assocAgent.getEnCours();

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        CFP content = new CFP(objectif.getVaccin(), objectif.getNombre(), objectif.getDateMort());

        message.setContent(gson.toJson(content));

        for (AID aid : getAID(labosType)) {
            message.addReceiver(aid);
        }
        return message;
    }

    /**
     * Envoie un message à toutes les compagnies
     *
     * @return
     */
    public ACLMessage startCompagnies() {

        while (getAID(compagnieType).isEmpty()){
            try {
                System.out.println("WAITING FOR COMPAGNIE");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AssosAgent assocAgent = (AssosAgent) this.myAgent;
        Objectif objectif = assocAgent.getEnCours();

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        System.out.println("the date is here " +objectif.getDateSouhaitee());
        System.out.println("volume" +objectif.getVolume());
        CompagnieMessage content = new CompagnieMessage(objectif.getVolume(), objectif.getDateSouhaitee(), objectif.getPays());
        message.setContent(gson.toJson(content));
        for (AID aid : getAID(compagnieType)) {
            message.addReceiver(aid);
        }
        return message;
    }

}
