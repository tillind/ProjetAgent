package fr.miage.projetagent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NamedQueries({
        @NamedQuery(
                name = "Vol.allVol",
                query = "SELECT v FROM Vol v WHERE v.association.nom = :assocName"),
})

@Entity
public class Vol implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();
    @OneToOne
    private Pays destination;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    private double volumeMax;
    @ManyToOne
    private Association association;


    public Vol() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pays getDestination() {
        return destination;
    }

    public void setDestination(Pays destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getVolumeMax() {
        return volumeMax;
    }

    public void setVolumeMax(double volumeMax) {
        this.volumeMax = volumeMax;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}
