package fr.miage.projetagent.BDD;

import fr.miage.projetagent.Agent.AssosAgent;
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
            a.setStatut(BddAgent.getStatut(a.getLocalName()));
            //TODO GADEAU
            //supprime malades mort, vaccins périmés, vol dépassé, créer malade
        }
    }
}
