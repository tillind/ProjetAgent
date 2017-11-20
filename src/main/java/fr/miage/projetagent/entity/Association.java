/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Association implements Serializable {


    @Id
    private String nom;
    @OneToOne
    private Argent tresorerie;
    @OneToMany
    private Set<Vaccin> vaccins;
    @OneToMany
    private Set<Vol> vols;
    @OneToMany
    private Set<Envoi> envois;
    

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
