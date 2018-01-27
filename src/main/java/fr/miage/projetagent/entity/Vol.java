package fr.miage.projetagent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

@NamedQueries({
    @NamedQuery(
        name="Vaccin.deleteVol",
        query="DELETE FROM Vol v WHERE v.date >= current_date() "), 
})

@Entity
public class Vol implements Serializable {

    @Id
    private String id;
    @OneToOne
    private Pays destination;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
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
