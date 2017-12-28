package fr.miage.projetagent.common;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnregistrerService {

    private static Logger LOGGER = LoggerFactory.getLogger(EnregistrerService.class);

    public static void registerService(Agent agent, String type, String name) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);

        dfd.addServices(sd);
        try {
            DFService.register(agent, dfd);
            LOGGER.info(agent + " is registered for " + type + "/" + name);
        } catch (FIPAException e) {
            LOGGER.error(agent.getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            agent.doDelete();
        }

    }

    public static void unregisterService(Agent agent, String type, String name) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);

        dfd.addServices(sd);
        try {
            DFService.deregister(agent, dfd);
            System.out.println("--------" + agent + " is unregistered for " + type + "/" + name);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            agent.doDelete();
        }

    }
}

