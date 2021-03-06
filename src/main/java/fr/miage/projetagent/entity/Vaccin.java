/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NamedQueries({

    @NamedQuery(
        name="Vaccin.getVaccinWhereMaladie",
        query="SELECT v FROM Vaccin v JOIN v.nom n WHERE n.nom = :nom AND v.association.nom= :assoc"),
})

@Entity
public class Vaccin implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();
    @OneToOne
    private Maladie nom;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateFin;
    private double volume;
    @ManyToOne
    private Association association;

    public Vaccin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Maladie getNom() {
        return nom;
    }

    public void setNom(Maladie nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}
