package fr.miage.projetagent.SecretaireLabo;

import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class FirstBehaviour extends Behaviour{

    @Override
    public void action() {

        Message1 messsage = new Message1("",0,new Date());

        ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
        aclMessage.setSender(super.myAgent.getAID());

        try {
            aclMessage.setContentObject(messsage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rechercher les agents à qui envoyer le message
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(super.myAgent, dfd);
            //
            for (int i= 0; i<result.length; i++) {
                DFAgentDescription desc = (DFAgentDescription) result[i];
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription)iter.next();
                    if(sd.getName().equals("todo")){
                        aclMessage.addReceiver(desc.getName());
                        super.myAgent.send(aclMessage);
                    }
                }
            }
        }
        catch (Exception fe) {
            System.err.println(super.myAgent.getLocalName() + " search with DF is not succeeded because of " + fe.getMessage());
            super.myAgent.doDelete();
        }

    }

    @Override
    public boolean done() {
        return false;
    }


}
