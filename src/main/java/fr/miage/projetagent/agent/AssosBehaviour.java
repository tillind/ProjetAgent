package fr.miage.projetagent.agent;

import fr.miage.projetagent.send.SendBehaviour;
import jade.core.behaviours.ParallelBehaviour;

public class AssosBehaviour  extends ParallelBehaviour{

    public AssosBehaviour() {
        //communication avec les labo et les compagnies
        this.addSubBehaviour(new CommunicationBehaviour(myAgent));

        //envoi de vaccin
        this.addSubBehaviour(new SendBehaviour());
    }
}
