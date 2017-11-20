package fr.miage.projetagent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class EnregistrerService{

    public static void registerService(Agent agent) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Association");
        sd.setName(agent.getName());

        dfd.addServices(sd);
        try {
            DFService.register(agent, dfd);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            agent.doDelete();
        }

    }
}
