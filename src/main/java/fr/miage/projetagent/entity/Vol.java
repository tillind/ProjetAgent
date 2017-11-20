package fr.miage.projetagent.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.OneToOne;

@Entity
public class Vol implements Serializable {

    @Id
    private String id;
    @OneToOne
    private Pays destination;
    private Date date;
    private int volumeMax;

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

    public int getVolumeMax() {
        return volumeMax;
    }

    public void setVolumeMax(int volumeMax) {
        this.volumeMax = volumeMax;
    }
}
