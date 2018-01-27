/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Maladie implements Serializable {

    @Id
    private String nom;
    private double delaiIncub;
    protected double vaccin_volume;


    public Maladie() {
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public double getDelaiIncub() {
        return delaiIncub;
    }

    public void setDelaiIncub(double delaiIncub) {
        this.delaiIncub = delaiIncub;
    }

    /**
     * @return the vaccin_volume
     */
    public double getVaccin_volume() {
        return vaccin_volume;
    }

    /**
     * @param vaccin_volume the vaccin_volume to set
     */
    public void setVaccin_volume(double vaccin_volume) {
        this.vaccin_volume = vaccin_volume;
    }
}
