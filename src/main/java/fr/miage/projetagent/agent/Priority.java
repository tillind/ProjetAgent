package fr.miage.projetagent.agent;

import java.util.Date;

public class Priority {

    //date where a patient will die
    private Date date;
    //volume of vaccine we already have for this disease and this date
    private double volume;
    //number of people to cure, minus the number of vaccine we already have for this disease and this date
    private int nombre;
    private String pays;
    private String maladie;

    public Priority() {
    }

    public Priority(Date date, int volume, int nombre, String pays, String maladie, int argent) {
        this.date = date;
        this.volume = volume;
        this.nombre = nombre;
        this.pays = pays;
        this.maladie = maladie;
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

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getMaladie() {
        return maladie;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }

}
