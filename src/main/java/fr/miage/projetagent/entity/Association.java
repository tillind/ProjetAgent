/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name="Association.findAll",
                query="SELECT a FROM Association a"),
})
public class Association implements Serializable {


    @Id
    private String nom;
    @OneToOne
    private Argent tresorerie;
    @OneToMany (mappedBy = "association")
    private Set<Vaccin> vaccins;
    @OneToMany (mappedBy = "association")
    private Set<Vol> vols;
    @OneToMany (mappedBy = "association")
    private Set<Envoi> envois;
    @OneToOne
    private Metrics metrics;

    public Association() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Argent getTresorerie() {
        return tresorerie;
    }

    public void setTresorerie(Argent tresorerie) {
        this.tresorerie = tresorerie;
    }

    public Set<Vaccin> getVaccins() {
        return vaccins;
    }

    public void setVaccins(Set<Vaccin> vaccins) {
        this.vaccins = vaccins;
    }

    public Set<Vol> getVols() {
        return vols;
    }

    public void setVols(Set<Vol> vols) {
        this.vols = vols;
    }

    public Set<Envoi> getEnvois() {
        return envois;
    }

    public void setEnvois(Set<Envoi> envois) {
        this.envois = envois;
    }
}
