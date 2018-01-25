/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author alex
 */
@Entity
public class EnvoiVaccin implements Serializable  {

    @Id
    private String id = UUID.randomUUID().toString();
    
    @OneToMany
    protected Set<Vaccin> lesVaccins = new HashSet<>();
    
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
    public  Set<Vaccin> getLesVaccins() {
        return lesVaccins;
    }

    /**
     * @param lesVaccins the lesVaccins to set
     */
    public void setLesVaccins(Set<Vaccin> lesVaccins) {
        this.lesVaccins = lesVaccins;
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
