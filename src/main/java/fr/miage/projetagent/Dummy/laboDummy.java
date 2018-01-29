package fr.miage.projetagent.Dummy;

import fr.miage.projetagent.common.EnregistrerService;
import jade.core.Agent;

public class laboDummy extends Agent {


	@Override
	public void setup() {




		EnregistrerService.registerService(this, "labo", "labo");

		 this.addBehaviour(new laboBehaviour(this));
	 }
	 

}
