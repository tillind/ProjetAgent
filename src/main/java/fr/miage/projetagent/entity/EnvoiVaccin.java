/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class EnvoiVaccin implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    protected Vaccin vaccin;

    protected Integer nb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vaccin getVaccin() {
        return vaccin;
    }

    public void setVaccin(Vaccin vaccin) {
        this.vaccin = vaccin;
    }

    public Integer getNb() {
        return nb;
    }

    public void setNb(Integer nb) {
        this.nb = nb;
    }
}
