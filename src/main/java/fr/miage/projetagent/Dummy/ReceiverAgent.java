package fr.miage.projetagent.Dummy;

import fr.miage.projetagent.common.EnregistrerService;
import jade.core.Agent;

public class ReceiverAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public void setup() {
		 EnregistrerService.registerService(this, "labo", "labo");

		 this.addBehaviour(new ReceiverBehaviour(this));
	 }
	 

}
