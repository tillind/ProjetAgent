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
    private Envoi envoi;
    private Integer nb ;
    private String maladie;

    public Envoi getEnvoi() {
        return envoi;
    }

    public void setEnvoi(Envoi envoi) {
        this.envoi = envoi;
    }

    public Integer getNb() {
        return nb;
    }

    public void setNb(Integer nb) {
        this.nb = nb;
    }

    public String getMaladie() {
        return maladie;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }
}
