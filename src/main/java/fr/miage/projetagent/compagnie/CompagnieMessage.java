package fr.miage.projetagent.compagnie;

import java.io.Serializable;
import java.util.Date;

public class CompagnieMessage implements Serializable {

    private double volume;
    private Date date;
    private String pays;

    public CompagnieMessage(double volume, Date date, String pays) {
        this.volume = volume;
        this.date = date;
        this.pays = pays;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "CompagnieMessage{" +
                "volume=" + volume +
                ", date=" + date +
                ", pays='" + pays + '\'' +
                '}';
    }
}
