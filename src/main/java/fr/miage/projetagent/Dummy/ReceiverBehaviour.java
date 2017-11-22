package fr.miage.projetagent.Dummy;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiverBehaviour extends CyclicBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

	public ReceiverBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage aclMessage = super.myAgent.blockingReceive(mt);
		if (aclMessage != null) {
	        String sender = aclMessage.getSender().toString();
	        System.out.println("Message de "+sender+" : ");
	        //System.out.println(aclMessage.getContent());
	        try {
	        	String message = aclMessage.getContent();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
		}
	}
}
