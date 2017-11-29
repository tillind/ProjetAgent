package fr.miage.projetagent.Agent;

import java.util.Date;

public class Statut {

    private Date date;
    private int volume;
    private int nombre;
    private String pays;
    private String maladie;
    private int argent;

    public Statut() {
    }

    public Statut(Date date, int volume, int nombre, String pays, String maladie, int argent) {
        this.date = date;
        this.volume = volume;
        this.nombre = nombre;
        this.pays = pays;
        this.maladie = maladie;
        this.argent = argent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
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

    public int getArgent() {
        return argent;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }
}
