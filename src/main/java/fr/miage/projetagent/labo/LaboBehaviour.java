package fr.miage.projetagent.labo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.CommunicationBehaviour;
import fr.miage.projetagent.agent.Objectif;
import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.entity.Vaccin;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.*;

public class LaboBehaviour extends ContractNetInitiator {

    final Gson gson = new GsonBuilder().create();


    private Objectif objectif = ((AssosAgent) myAgent).getEnCours();
    private double argent = ((AssosAgent) myAgent).getArgent();


    public LaboBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println(myAgent.getLocalName() + " -------- Message is sent to all labos");
    }


    /**
     * Reset cette behaviour
     */
    private void resetBehaviour() {
        System.out.println(myAgent.getLocalName() + " -------- Labo behaviour is reset");
        CommunicationBehaviour parent = (CommunicationBehaviour) this.parent;
        parent.init();
        this.reset(parent.startLabo());
    }


    /**
     * Recoit toutes les réponses
     * Si aucune proposition a été faite, reset
     * Envoie des acceptations
     *
     * @param responses
     * @param acceptances
     */
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println(myAgent.getLocalName() + "*Labo  --------" + responses.size() + "responses");
        //System.out.println("--------" + acceptances.size() + "acceptances");
        List<ACLMessage> proposeResponse = new ArrayList<>();
        for (Object response : responses) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.PROPOSE) {
                proposeResponse.add(reponseMessage);
            }
        }

        System.out.println(myAgent.getLocalName() + "*Labo  --------" + proposeResponse.size() + "propose");

        acceptances = choosePropose(proposeResponse);
        moreAcceptances(acceptances);
        boolean atLeastOne = false;
        for (Object response : acceptances) {
            if (((ACLMessage) response).getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                atLeastOne = true;
            }
        }

        if (!atLeastOne) {
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
    private Vector<ACLMessage> choosePropose(List<ACLMessage> list) {
        if (list.size() == 0) {
            return new Vector<>();
        }

        List<ACLMessage> listBefore = getListDate(list, true);
        List<ACLMessage> listAfter = getListDate(list, false);
        sort(listBefore);
        sort(listAfter);

        listBefore.addAll(listAfter);

        Vector<ACLMessage> send1 = sendResponse(listBefore);

        return (send1);
    }


    /**
     * Renvoie la liste de messages qui se périmment avant ou après la date demandée
     *
     * @param list
     * @param before
     * @return
     */
    private List<ACLMessage> getListDate(List<ACLMessage> list, boolean before) {
        Date date = ((AssosAgent) myAgent).getEnCours().getDateMort();
        List<ACLMessage> listBefore = new ArrayList<>();
        List<ACLMessage> listAfter = new ArrayList<>();

        for (ACLMessage message : list) {
            String content = message.getContent();
            Propose propose = gson.fromJson(content, Propose.class);
            System.out.println(propose.toString());
            if (propose.getDatePeremption().before(date) || propose.getDatePeremption().equals(date)) {
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
    private Vector<ACLMessage> sendResponse(List<ACLMessage> list) {
        double depense = 0;
        int achete = 0;

        Vector<ACLMessage> reponse = new Vector<>();

        for (ACLMessage message : list) {
            //s'il y a encore besoin de vaccins et qu'il reste de l'argent
            if (depense < argent && achete < objectif.getNombre()) {
                ACLMessage agree = message.createReply();
                agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reponse.add(agree);
                System.out.println(myAgent.getLocalName() + "*Labo -------- ACCEPT Proposal sended");

                String content = message.getContent();
                Propose propose = gson.fromJson(content, Propose.class);
                achete += propose.getNombre();
                depense += (propose.getNombre() * propose.getPrix());
                getDataStore().put(message.getConversationId() + "propose", propose);

            } else {
                ACLMessage reject = message.createReply();
                reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                reponse.add(reject);
                System.out.println(myAgent.getLocalName() + "*Labo -------- REJECT Proposal sended");
            }
        }
        return reponse;
    }


    /**
     * Attend tous les messages inform/refuse
     *
     * @param resultNotifications
     */
    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {

        System.out.println(myAgent.getLocalName() + "*Labo  --------" + resultNotifications.size() + "refuse/inform");


        boolean atLeastOneInform = false;

        List<Propose> informList = new ArrayList<>();

        for (Object response : resultNotifications) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.INFORM) {
                atLeastOneInform = true;
                Propose propose = (Propose) getDataStore().get(reponseMessage.getConversationId() + "propose");
                informList.add(propose);
            }
        }

        System.out.println(myAgent.getLocalName() + "*Labo --------" + informList.size() + "inform");

        if (atLeastOneInform) {
            handleInform(informList);
            this.done(); //si on a reçu une confirmation, cette behaviour est terminée
        } else {
            resetBehaviour(); //sinon on recherche encore des médicaments
        }

    }


    /**
     * Recoit toutes les informations des commandes de médicaments validées
     * Met à jour les autres informations
     *
     * @param list
     */
    private void handleInform(List<Propose> list) {

        Objectif objectif = ((AssosAgent) myAgent).getEnCours();
        List<Propose> beforeDeath = new ArrayList<>();
        List<Propose> afterDeath = new ArrayList<>();

        int nbTotal = 0;
        int sumTotal = 0;
        int volumTotal = 0;

        //calcule nombre acheté, argent dépensé, volume acheté
        for (Propose propose : list) {
            nbTotal += propose.getNombre();
            sumTotal += propose.getPrix() * propose.getNombre();
            volumTotal += propose.getVolume() * propose.getNombre();
            if (propose.getDateLivraison().before(objectif.getDateMort())) {
                beforeDeath.add(propose);
            } else {
                afterDeath.add(propose);
            }
        }

        //commande qui se périme le plus tôt
        Propose firstPeremption = list.stream()
                .min(Comparator.comparing(Propose::getDatePeremption))
                .orElseThrow(NoSuchElementException::new);

        //si des vaccins se périment avant, on l'envoie avant qu'ils ne soient périmés
        if (firstPeremption.getDatePeremption().before(objectif.getDateMort())) {
            objectif.setDateSouhaitee(firstPeremption.getDatePeremption());
        } else {
            //si tous les vaccins se périment après
            if (beforeDeath.size() > 0) { //si certains vaccins sont livrés avant la date limite, on prend le dernier
                Propose lastOneBeforeDeath = list.stream()
                        .max(Comparator.comparing(Propose::getDateLivraison))
                        .orElseThrow(NoSuchElementException::new);
                objectif.setDateSouhaitee(lastOneBeforeDeath.getDatePeremption());
            } else { //si les vaccins sont livrés après la date limite, on envoie dès que le premier arrive
                Propose firstOneAfterDeath = list.stream()
                        .min(Comparator.comparing(Propose::getDateLivraison))
                        .orElseThrow(NoSuchElementException::new);
                objectif.setDateSouhaitee(firstOneAfterDeath.getDatePeremption());
            }
        }

        //mise à jour de l'objectif
        objectif.setVolume(objectif.getVolume() + volumTotal);
        objectif.setNombre(nbTotal);

        List<Vaccin> vaccins = new ArrayList<>();
        for (Propose propose : list) {
            for (int i = 0; i < propose.getNombre(); i++) {
                Vaccin vaccin = new Vaccin();
                vaccin.setDateDebut(propose.getDateLivraison());
                vaccin.setDateFin(propose.getDatePeremption());
                vaccin.setVolume(propose.getVolume());
                //J'ajoute les vaccins
                BddAgent.addVaccin(objectif.getVaccin(), vaccin);
                System.out.println(myAgent.getLocalName() + "*Labo -------- added envoi to DB");
            }
        }

        BddAgent.decreaseMoney(myAgent.getLocalName(), sumTotal);

        System.out.println(myAgent.getLocalName() + "*Labo -------- DONE");

    }

}
