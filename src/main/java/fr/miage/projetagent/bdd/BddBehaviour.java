package fr.miage.projetagent.bdd;

import fr.miage.projetagent.agent.AssosAgent;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class BddBehaviour extends TickerBehaviour{


    public BddBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        BddAgent bdd = (BddAgent) myAgent;
        for (AssosAgent a : bdd.assosAgent){
            a.getPriorities().add(BddAgent.getStatut(a.getLocalName()));
            a.setArgent(BddAgent.getArgent(a.getLocalName()));
            //TODO GADEAU
            //supprime malades mort, vaccins périmés, vol dépassé, créer malade
        }
    }
}
