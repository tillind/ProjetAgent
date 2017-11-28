package fr.miage.projetagent.Agent;

import java.util.Date;

public class Objectif {

    private String pays;
    private String vaccin;
    private int nombre;
    private Date dateSouhaite;
    private int volume;
    private Date arrivee;

    public Objectif() {
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVaccin() {
        return vaccin;
    }

    public void setVaccin(String vaccin) {
        this.vaccin = vaccin;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public Date getDateSouhaite() {
        return dateSouhaite;
    }

    public void setDateSouhaite(Date dateSouhaite) {
        this.dateSouhaite = dateSouhaite;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getArrivee() {
        return arrivee;
    }

    public void setArrivee(Date arrivee) {
        this.arrivee = arrivee;
    }
}
