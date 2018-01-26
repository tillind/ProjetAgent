package fr.miage.projetagent.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Envoi implements Serializable {

    @Id
    private String id;
    private Pays pays;
    private Date date;
    @OneToMany
    private Set<EnvoiVaccin> vaccins = new HashSet<>();

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

    public Set<EnvoiVaccin> getVaccins() {
        return vaccins;
    }

    public void setVaccins(Set<EnvoiVaccin> vaccins) {
        this.vaccins = vaccins;
    }
}
