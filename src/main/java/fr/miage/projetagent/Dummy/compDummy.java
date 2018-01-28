package fr.miage.projetagent.Dummy;

import fr.miage.projetagent.common.EnregistrerService;
import jade.core.Agent;

public class compDummy extends Agent {


	@Override
	public void setup() {




		EnregistrerService.registerService(this, "compagnie", "compagnie");

		 this.addBehaviour(new ReceiverBehaviour(this));
	 }
	 

}
