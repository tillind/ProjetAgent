package fr.miage.projetagent.compagnie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.miage.projetagent.Agent.AssosAgent;
import fr.miage.projetagent.Agent.Objectif;
import fr.miage.projetagent.BDD.BddAgent;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.Vol;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.lang.reflect.Type;
import java.util.*;

public class CompagnieBehaviour extends ContractNetInitiator {

    private final Gson gson = new GsonBuilder().create();

    private Objectif objectif = ((AssosAgent) myAgent).enCours;

    public CompagnieBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        System.out.println("--------Message is send to all compagnies");
    }

    /**
     * Reset la behaviour en augmentant la date d'un jour
     */
    private void resetBehaviour() {
        ACLMessage newMessage = new ACLMessage(ACLMessage.CFP);
        newMessage.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        newMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        CompagnieMessage content = new CompagnieMessage(objectif.getVolume(), dt, objectif.getPays());
        newMessage.setContent(gson.toJson(content));
        this.reset(newMessage);
    }

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

        if (proposeResponse.size() == 0) {
            // Si pas de réponse des compagnies, renvoie du message avec une nouvelle date
            this.resetBehaviour();
        } else {
            for (ACLMessage message : proposeResponse) {
                String content = message.getContent();
                Type collectionType = new TypeToken<Collection<VolPropose>>() {
                }.getType();
                Collection<VolPropose> vols = gson.fromJson(content, collectionType);

                // Choix de la proposition la moins chère
                Iterator it = vols.iterator();
                VolPropose volMoinsCher = (VolPropose) it.next();
                while (it.hasNext()) {
                    if (((VolPropose) it.next()).getPrix() < volMoinsCher.getPrix()) {
                        volMoinsCher = (VolPropose) it.next();
                    }
                }

                // Réponse avec l'id du vol accepté
                ACLMessage agree = message.createReply();
                agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                agree.setContent(gson.toJson(volMoinsCher));
                acceptances.add(agree);
                System.out.println("-------- ACCEPT_PROPOSAL Vol sended");
                getDataStore().put(message.getConversationId() + "vol", volMoinsCher);
            }
            moreAcceptances(acceptances);
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        boolean atLeastOneInform = false;

        System.out.println("--------" + resultNotifications.size() + "inform/refuse");
        VolPropose propose = new VolPropose();

        for (Object response : resultNotifications) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.INFORM) {
                atLeastOneInform = true;
                propose = (VolPropose) getDataStore().get(reponseMessage.getConversationId() + "vol");
            }
        }

        handleInform(propose);

        if (atLeastOneInform) {
            this.done();
        } else {
            this.resetBehaviour();
        }
    }

    private void handleInform(VolPropose volPropose) {
        // mise à jour de l'argent
        BddAgent.decreaseMoney(myAgent.getLocalName(), volPropose.getPrix());

        // TODO ajouter le vol à la BD
        Vol vol = new Vol();
        vol.setDate(volPropose.getDateArrivee());
        Pays p = new Pays();
        p.setNom(volPropose.getPays());
        vol.setDestination(p);
        vol.setVolumeMax(volPropose.getVolume());
        //BddAgent.addVol(Vol vol);
    }
}
