package fr.miage.projetagent.send;

import fr.miage.projetagent.agent.AssosAgent;
import fr.miage.projetagent.agent.Objectif;
import fr.miage.projetagent.bdd.BddAgent;
import fr.miage.projetagent.entity.*;
import jade.core.behaviours.CyclicBehaviour;

import java.util.*;
import java.util.stream.Collectors;

public class SendBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        List<Vol> vols = BddAgent.allFlight(myAgent.getLocalName());

        for (Vol vol : vols) {

            //if the plane left
            if (vol.getDate().before(new Date())) {

                double volumeToSend = 0.0;

                //to be sent
                Envoi envoi = new Envoi();
                envoi.setDate(vol.getDate());
                envoi.setPays(vol.getDestination());
                envoi.setAssociation(BddAgent.getAssos(myAgent.getLocalName()));

                List<Maladie> maladiesToCure = getDiseaseToCureSorted(vol.getDestination().getNom());

                List<EnvoiVaccin> envoiVaccinsList = new ArrayList<>();
                List<Vaccin> toDelete = new ArrayList<>();

                //for each disease to cure, check if vaccine
                //the list is sorted according to the priority to cure it
                for (Maladie maladie : maladiesToCure) {

                    long sickPeople = BddAgent.getNombre(vol.getDestination().getNom(), maladie.getNom());
                    List<Vaccin> vaccinsInStock = BddAgent.getVaccins(maladie.getNom(), myAgent.getLocalName());

                    //prepare a package for this disease
                    EnvoiVaccin envoiVaccin = new EnvoiVaccin();
                    envoiVaccin.setMaladie(maladie.getNom());
                    envoiVaccin.setNb(0);
                    envoiVaccin.setEnvoi(envoi);

                    for (Vaccin vaccin : vaccinsInStock) {

                        int nbSendedForThisDease = envoiVaccin.getNb();

                        if (nbSendedForThisDease < sickPeople
                                && volumeToSend + vaccin.getVolume() < vol.getVolumeMax()) {
                            envoiVaccin.setNb(nbSendedForThisDease + 1);
                            volumeToSend += vaccin.getVolume();
                            toDelete.add(vaccin);
                        }
                    }

                    if (envoiVaccin.getNb()>0){
                        envoiVaccinsList.add(envoiVaccin);
                    }

                }

                if (volumeToSend>0){
                    BddAgent.addEnvoi(envoi, envoiVaccinsList);
                    for (Vaccin vaccin : toDelete){
                        BddAgent.deleteVaccin(vaccin);
                    }
                    System.out.println(myAgent.getLocalName() + "*SEND --------  added envoi to DB");
                }
                BddAgent.deleteVol(vol);

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

        System.out.println("disease to cure ----");
        System.out.println(maladiesToCure.toString());
        System.out.println("disease to cure ---- DONE");

        return maladiesToCure;
    }
}
