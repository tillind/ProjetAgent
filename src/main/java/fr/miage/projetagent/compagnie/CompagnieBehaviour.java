package fr.miage.projetagent.compagnie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.CommunicationBehaviour;
import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.entity.Pays;
import fr.miage.projetagent.entity.Vol;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.lang.reflect.Type;
import java.util.*;

public class CompagnieBehaviour extends ContractNetInitiator {

    private final Gson gson = new GsonBuilder().create();

    private Date lastDate;
    private Double lastVolume;
    private boolean lastIsDate = false;


    public CompagnieBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    /**
     * Reset la behaviour en augmentant la date d'un jour
     */
    private void resetBehaviour() {

        System.out.println(myAgent.getLocalName() + " -------- Compagnie behaviour is reset");
        CommunicationBehaviour parent = (CommunicationBehaviour) this.parent;
        ACLMessage origin = parent.startCompagnies();
        CompagnieMessage cm = gson.fromJson(origin.getContent(), CompagnieMessage.class);
        if (lastDate == null || lastVolume == null) {
            lastDate = cm.getDate();
            lastVolume = cm.getVolume();
        }
        System.out.println("volume " + lastVolume);
        System.out.println("date " + lastDate);
        System.out.println("bool :" + lastIsDate);

        //switch between postponing and decreasing volume
        if (lastIsDate) {
            lastVolume = lastVolume - (lastVolume * 0.1);
            cm.setVolume(lastVolume);
            lastIsDate = false;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(lastDate);
            c.add(Calendar.MINUTE, 1);
            lastDate = c.getTime();
            cm.setDate(lastDate);
            lastIsDate = true;
        }
        System.out.println("volume " + lastVolume);
        System.out.println("date " + lastDate);
        origin.setContent(gson.toJson(cm));
        this.reset(origin);
    }

    @Override
    public void onStart() {
        System.out.println("--------Message is send to all compagnies");
        //now that this behaviour has started, we can send CFP
        CommunicationBehaviour parent = (CommunicationBehaviour) this.parent;
        ACLMessage origin = parent.startCompagnies();
        this.reset(origin);
        super.onStart();
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println(myAgent.getLocalName() + "*Compagnie  --------" + responses.size() + "responses");

        List<ACLMessage> proposeResponse = new ArrayList<>();
        for (Object response : responses) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.PROPOSE) {
                proposeResponse.add(reponseMessage);
            }
        }

        System.out.println(myAgent.getLocalName() + "*Compagnie  --------" + proposeResponse.size() + "propose");


        if (proposeResponse.size() == 0) {
            // Si pas de réponse des compagnies, renvoie du message avec une nouvelle date ou un nouveau volume
            this.resetBehaviour();
        } else {
            VolPropose volMoinsCher = null;
            ACLMessage toRespond = null;
            for (ACLMessage message : proposeResponse) {
                String content = message.getContent();
                Type collectionType = new TypeToken<Collection<VolPropose>>() {
                }.getType();
                Collection<VolPropose> vols = gson.fromJson(content, collectionType);

                // Choix de la proposition la moins chère pour ce message
                VolPropose tmp = vols.stream().min((v1, v2) -> Double.compare(v1.getPrix(), v2.getPrix())).get();

                if ((volMoinsCher == null) ||
                        (volMoinsCher != null && tmp.getPrix() < volMoinsCher.getPrix())) {
                    volMoinsCher = tmp;
                    toRespond = message;
                }
            }
            // Réponse avec l'id du vol accepté
            ACLMessage agree = toRespond.createReply();
            agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            agree.setContent(gson.toJson(volMoinsCher));
            acceptances.add(agree);
            System.out.println(myAgent.getLocalName() + "*Compagnie -------- ACCEPT_PROPOSAL Vol sended");
            getDataStore().put(toRespond.getConversationId() + "vol", volMoinsCher);
            moreAcceptances(acceptances);
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        boolean atLeastOneInform = false;

        System.out.println(myAgent.getLocalName() + "*Compagnie  -------- " + resultNotifications.size() + "refuse/inform");

        VolPropose propose = new VolPropose();

        for (Object response : resultNotifications) {
            ACLMessage reponseMessage = (ACLMessage) response;
            if (reponseMessage.getPerformative() == ACLMessage.INFORM) {
                atLeastOneInform = true;
                propose = (VolPropose) getDataStore().get(reponseMessage.getConversationId() + "vol");
                System.out.println(myAgent.getLocalName() + "*Labo -------- " + 1 + "inform");
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

        Vol vol = new Vol();
        vol.setDate(volPropose.getDateArrivee());

        vol.setVolumeMax(volPropose.getVolume());
        BddAgent.addVol(vol, volPropose.getPays());

        AssosAgent assos = (AssosAgent) myAgent;
        assos.getPrioritiesDone().add(assos.getEnCours());

        System.out.println(myAgent.getLocalName() + "*Compagnie -------- DONE");
    }
}
