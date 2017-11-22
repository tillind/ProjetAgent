package fr.miage.projetagent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Iterator;

public class ShowAgent extends Agent {

    @Override
    public void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        System.out.println("-------------------I'm looking for them");
        try {
            DFAgentDescription[] result = DFService.search(this, dfd);
            System.out.println("*****"+result.length+"******");
            System.out.println("*****"+result+"******");
            for (int i=0; i<result.length; i++) {
                DFAgentDescription desc = (DFAgentDescription) result[i];
                String out = desc.getName() + "provide";
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    out += " " + sd.getName();
                }
                System.out.println(this.getLocalName()+" "+out);
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }
}
