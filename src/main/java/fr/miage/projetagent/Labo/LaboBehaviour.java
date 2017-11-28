package fr.miage.projetagent.Labo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.Agent.AssocAgent;
import fr.miage.projetagent.Agent.AssocBehaviour;
import fr.miage.projetagent.Agent.Objectif;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.*;

public class LaboBehaviour extends ContractNetInitiator {

    final Gson gson = new GsonBuilder().create();


    Objectif objectif = ((AssocAgent) myAgent).enCours;
    int argent = ((AssocAgent) myAgent).argent;
    int depense = 0;
    int achete = 0;

    public LaboBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println("--------Message is send to all labos");
    }


    /**
     * Recoit toutes les réponses
     * Si aucune proposition a été faite, refait une demande
     * Envoie des acceptations
     *
     * @param responses
     * @param acceptances
     */
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("--------" + responses.size() + "responses");
        System.out.println("--------" + acceptances.size() + "acceptances");
        List<ACLMessage> proposeResponse = new ArrayList<>();
        for (Object response : responses) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.PROPOSE) {
                proposeResponse.add(reponseMessage);
            }
        }

        boolean out = choosePropose(proposeResponse);

        if (!out) {
            AssocBehaviour parent = (AssocBehaviour) this.parent;
            this.reset(parent.startLabo());
        }

    }


    /**
     * Renvoie vrai si un Agree a été envoyé
     * False sinon
     *
     * @param list
     * @return
     */
    private boolean choosePropose(List<ACLMessage> list) {
        if (list.size() == 0) {
            return false;
        }

        List<ACLMessage> listBefore = getListDate(list, true);
        List<ACLMessage> listAfter = getListDate(list, false);
        sort(listBefore);
        sort(listAfter);

        boolean send1 = sendResponse(listBefore);
        boolean send2 = sendResponse(listAfter);


        return (send1 || send2);
    }


    /**
     * Renvoie la liste de messages qui se périmment avant ou après la date demandée
     *
     * @param list
     * @param before
     * @return
     */
    private List<ACLMessage> getListDate(List<ACLMessage> list, boolean before) {
        Date date = ((AssocAgent) myAgent).enCours.getDateSouhaite();
        List<ACLMessage> listBefore = new ArrayList<>();
        List<ACLMessage> listAfter = new ArrayList<>();

        for (ACLMessage message : list) {
            String content = message.getContent();
            Propose propose = gson.fromJson(content, Propose.class);
            System.out.println(propose.toString());
            if (propose.getDatePeremption().before(new Date()) || propose.getDatePeremption().equals(new Date())) {
                listBefore.add(message);
            } else {
                listAfter.add(message);
            }
        }
        if (before) {
            return listBefore;
        } else {
            return listAfter;
        }
    }


    /**
     * Trie une liste de messages en fonction de son prix
     *
     * @param list
     */
    private void sort(List<ACLMessage> list) {
        Collections.sort(list, new Comparator<ACLMessage>() {
            @Override
            public int compare(ACLMessage message1, ACLMessage message2) {
                String content1 = message1.getContent();
                String content2 = message2.getContent();
                Propose propose1 = gson.fromJson(content1, Propose.class);
                Propose propose2 = gson.fromJson(content2, Propose.class);
                return propose1.getPrix().compareTo(propose2.getPrix());
            }
        });
    }

    /**
     * Traite une liste de messages triés
     * Accpte si a suffisament d'argent et pas suffisement de médicament
     *
     * @param list
     * @return
     */
    private boolean sendResponse(List<ACLMessage> list) {
        boolean sendSomething = false;
        for (ACLMessage message : list) {
            if (depense < argent && achete < objectif.getNombre()) {
                ACLMessage agree = message.createReply();
                agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                myAgent.send(agree);
                String content = message.getContent();
                Propose propose = gson.fromJson(content, Propose.class);
                achete += propose.getNombre();
                depense += (propose.getNombre() * propose.getPrix());
                sendSomething = true;
                getDataStore().put(message.getConversationId() + "propose", propose);
                System.out.println("-------- ACCEPT Proposal sended");
            } else {
                ACLMessage reject = message.createReply();
                reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                myAgent.send(reject);
                System.out.println("-------- REJECT Proposal sended");
            }
        }
        return sendSomething;
    }


    //TODO ajouter ce que je recup, terminer la méthode pour passer aux compagnies
    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        System.out.println("--------" + resultNotifications.size() + "inform/refuse");
        List<ACLMessage> informList = new ArrayList<>();
        for (Object response : resultNotifications) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.INFORM) {
                informList.add(reponseMessage);
                Propose propose = (Propose) getDataStore().get(reponseMessage.getConversationId());
            }
        }

    }

}
