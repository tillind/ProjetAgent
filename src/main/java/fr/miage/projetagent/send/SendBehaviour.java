package fr.miage.projetagent.send;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Objectif;
import fr.miage.projetagent.agent.Priority;
import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.entity.Envoi;
import fr.miage.projetagent.entity.Maladie;
import fr.miage.projetagent.entity.Vaccin;
import fr.miage.projetagent.entity.Vol;
import jade.core.behaviours.CyclicBehaviour;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SendBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        //TODO get it from  DB
        List<Vol> vols = new ArrayList<>();

        for (Vol vol : vols) {
            //TODO change condition
            if (vol.getDate().before(new Date())) {

                int volumeToSend = 0;

                //to be sent
                Envoi envoi = new Envoi();
                envoi.setDate(vol.getDate());
                envoi.setPays(vol.getDestination());
                envoi.setVaccins(new HashMap<>());

                List<Maladie> maladiesToCure = getDiseaseToCureSorted(vol.getDestination().getNom());

                //for each disease to cure, check if vaccine
                //the list is sorted according to the priority to cure it
                for (Maladie maladie : maladiesToCure) {

                    int sickPeople = BddAgent.getNombre(vol.getDestination().getNom(), maladie.getNom());
                    List<Vaccin> vaccinsInStock = BddAgent.getVaccins(maladie.getNom());

                    for (Vaccin vaccin : vaccinsInStock) {
                        int nbSendedForThisDease =
                                envoi.getVaccins().get(vaccin.getNom()) == null ? 0
                                        : envoi.getVaccins().get(vaccin.getNom());
                        if (nbSendedForThisDease < sickPeople && volumeToSend + vaccin.getVolume() < vol
                                .getVolumeMax()) {
                            envoi.getVaccins().put(vaccin.getNom(), nbSendedForThisDease + 1);
                            //BddAgent.deleteVaccin(vaccin);
                        }
                    }
                }

                //BddAgent.deleteVol(vol);
                BddAgent.addEnvoi(envoi);

            }
        }
    }


    private List<Maladie> getDiseaseToCureSorted(String pays) {

        List<Maladie> maladiesToCure = new ArrayList<>();

        //get all priorities for this country
        List<Objectif> objectifs = ((AssosAgent) this.myAgent).getPrioritiesDone().stream()
                .filter(p -> p.getPays().equals(pays)).collect(
                        Collectors.toList());

        //for each priority choosen, add it to the diseases to cure
        for (Objectif objectif : objectifs) {
            maladiesToCure.add(BddAgent.getMaladie(objectif.getVaccin()));
        }

        //each disease not in priority is added
        for (Maladie maladie : BddAgent.getMaladiesForCountry(pays)) {
            if (!maladiesToCure.contains(maladie)) {
                maladiesToCure.add(maladie);
            }
        }
        return maladiesToCure;
    }
}
