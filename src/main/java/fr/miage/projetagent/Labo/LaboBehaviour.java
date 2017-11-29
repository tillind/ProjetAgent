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


    private Objectif objectif = ((AssocAgent) myAgent).enCours;
    private int argent = ((AssocAgent) myAgent).argent;
    private int depense = 0;
    private int achete = 0;

    public LaboBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println("--------Message is sent to all labos");
    }


    private void resetBehaviour() {
        System.out.println("--------Labo behaviour is reseted");
        AssocBehaviour parent = (AssocBehaviour) this.parent;
        parent.init();
        this.reset(parent.startLabo());
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
            resetBehaviour();
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
        Date date = ((AssocAgent) myAgent).enCours.getDateMort();
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


    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {

        boolean atLeastOneInform = false;

        System.out.println("--------" + resultNotifications.size() + "inform/refuse");
        List<Propose> informList = new ArrayList<>();

        for (Object response : resultNotifications) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.INFORM) {
                atLeastOneInform = true;
                Propose propose = (Propose) getDataStore().get(reponseMessage.getConversationId() + "propose");
                informList.add(propose);
            }
        }

        handleInform(informList);

        if (atLeastOneInform) {
            this.done(); //si on a reçu une confirmation, cette behaviour est terminée
        } else {
            resetBehaviour();
        }

    }


    private void handleInform(List<Propose> list){

        Objectif objectif = ((AssocAgent) myAgent).enCours;
        List<Propose> beforeDeath = new ArrayList<>();
        List<Propose> afterDeath = new ArrayList<>();

        int nbTotal = 0;
        int sumTotal = 0;
        int volumTotal = 0;

        for (Propose propose : list) {
            nbTotal += propose.getNombre();
            sumTotal += propose.getPrix()*propose.getNombre();
            volumTotal += propose.getVolume()*propose.getNombre();
            if (propose.getDateLivraison().before(objectif.getDateMort())){
                beforeDeath.add(propose);
            }else{
                afterDeath.add(propose);
            }
        }


        Propose firstPeremption = list.stream()
                .min(Comparator.comparing(Propose::getDatePeremption))
                .orElseThrow(NoSuchElementException::new);

        //si des vaccins se périment avant, on l'envoie avant qu'ils ne soient périmés
        if (firstPeremption.getDatePeremption().before(objectif.getDateMort())){
            objectif.setDateSouhaitee(firstPeremption.getDatePeremption());
        }else{
            //si tous les vaccins se périment après
            if(beforeDeath.size()>0){ //si certains vaccins sont livrés avant la date limite, on prend le dernier
                Propose lastOneBeforeDeath = list.stream()
                        .max(Comparator.comparing(Propose::getDateLivraison))
                        .orElseThrow(NoSuchElementException::new);
                objectif.setDateSouhaitee(lastOneBeforeDeath.getDatePeremption());
            }else{ //si les vaccins sont livrés après la date limite, on envoie dès que le premier arrive
                Propose firstOneAfterDeath = list.stream()
                        .min(Comparator.comparing(Propose::getDateLivraison))
                        .orElseThrow(NoSuchElementException::new);
                objectif.setDateSouhaitee(firstOneAfterDeath.getDatePeremption());

            }
        }


        //TODO diminuer l'argent
        //TODO ajouter le volume à l'objectif
        //TODO ajouter les médicaments achetés à la base


    }

}
