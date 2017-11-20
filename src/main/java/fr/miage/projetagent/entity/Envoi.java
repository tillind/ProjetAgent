package fr.miage.projetagent.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.OneToOne;

@Entity
public class Envoi implements Serializable {

    @Id
    private String id;
    private Pays pays;
    private Date date;
    @OneToOne
    private Maladie vaccin;
    private int nombre;

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

    public Maladie getVaccin() {
        return vaccin;
    }

    public void setVaccin(Maladie vaccin) {
        this.vaccin = vaccin;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}