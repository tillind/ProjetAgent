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
    protected String id = UUID.randomUUID().toString();
    @ManyToOne
    protected Vaccin lesVaccins;
    @ManyToOne
    protected Envoi envoi;
    protected Integer nb ;    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the lesVaccins
     */
    public Vaccin getLesVaccins() {
        return lesVaccins;
    }

    /**
     * @param lesVaccins the lesVaccins to set
     */
    public void setLesVaccins(Vaccin lesVaccins) {
        this.lesVaccins = lesVaccins;
    }

    /**
     * @return the envoi
     */
    public Envoi getEnvoi() {
        return envoi;
    }

    /**
     * @param envoi the envoi to set
     */
    public void setEnvoi(Envoi envoi) {
        this.envoi = envoi;
    }

    /**
     * @return the nb
     */
    public Integer getNb() {
        return nb;
    }

    /**
     * @param nb the nb to set
     */
    public void setNb(Integer nb) {
        this.nb = nb;
    }
}
