package fr.miage.projetagent.Dummy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.miage.projetagent.compagnie.VolPropose;
import fr.miage.projetagent.labo.Propose;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiverBehaviour extends CyclicBehaviour {
	
	/**
	 * 
	 */
	final Gson gson = new GsonBuilder().create();

	private static final long serialVersionUID = 1L;
	private final static MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
	private final static MessageTemplate other = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);

	public ReceiverBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage aclMessage = super.myAgent.blockingReceive(mt);
		if (aclMessage != null) {
	        String sender = aclMessage.getSender().toString();
	        System.out.println("Message de "+sender+" : ");
			System.out.println(aclMessage.getContent());
			//System.out.println(aclMessage.getContent());
	        try {
	        	String message = aclMessage.getContent();
				System.out.println(message);
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				ACLMessage response = aclMessage.createReply();
				response.setPerformative(ACLMessage.PROPOSE);
				response.setContent(gson.toJson(new VolPropose()));
				System.out.println(response.getAllReceiver());
				myAgent.send(response);
				System.out.println("its sent");
			} catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
		}
		ACLMessage newf = super.myAgent.blockingReceive(other);
		if (newf != null) {
			System.out.println("i got an accept ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ");
		}
	}
}
