package fr.miage.projetagent.SecretaireLabo;

import fr.miage.projetagent.EnregistrerService;
import jade.core.Agent;

public class AgentSecretaireLabo extends Agent {


    @Override
    public void setup() {

        EnregistrerService.registerService(this);

        addBehaviour(new FirstBehaviour());

    }


}
