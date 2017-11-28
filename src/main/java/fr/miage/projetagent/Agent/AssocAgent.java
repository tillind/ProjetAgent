package fr.miage.projetagent.Agent;

import fr.miage.projetagent.Common.EnregistrerService;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AssocAgent extends Agent {

    public final String labosType = "labo";
    public final String compagnieType = "compagnie";


    //TODO mettre argent de l'assoc
    public int argent;

    public String pays;
    public String maladie;
    public int nb;
    public Date date;

    public Objectif enCours = new Objectif();

    @Override
    protected void setup() {
        EnregistrerService.registerService(this, "assos", "assoc");
        this.addBehaviour(new AssocBehaviour(this));
    }

    public Set<AID> getLabos() {
        Set<AID> labos = new HashSet<AID>();
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(this, dfd);
            for (int i = 0; i < result.length; i++) {
                DFAgentDescription desc = (DFAgentDescription) result[i];
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    if (sd.getType().equals(labosType)) {
                        labos.add(desc.getName());
                    }
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("--------There are " + labos.size() + " labos");
        return labos;
    }

    public Set<AID> getCompagnies() {
        Set<AID> compagnies = new HashSet<>();
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(this, dfd);
            for (int i = 0; i < result.length; i++) {
                DFAgentDescription desc = result[i];
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    if (sd.getType().equals(compagnieType)) {
                        compagnies.add(desc.getName());
                    }
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("--------There are " + compagnies.size() + " compagnies");
        return compagnies;
    }

}
