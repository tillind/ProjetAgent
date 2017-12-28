package fr.miage.projetagent.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Envoi implements Serializable {

    @Id
    private String id;
    private Pays pays;
    private Date date;
    @ManyToMany
    private Map<Maladie, Integer> vaccins;

    public Envoi() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<Maladie, Integer> getVaccins() {
        return vaccins;
    }

    public void setVaccins(Map<Maladie, Integer> vaccins) {
        this.vaccins = vaccins;
    }
}
