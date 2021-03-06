package fr.miage.projetagent.agent;

import java.util.Date;

public class Objectif {

    private String pays;
    private String vaccin;
    private int nombre;
    private Date dateMort;
    private double volume;
    private Date arrivee;
    private Date dateSouhaitee;

    public Objectif() {
    }

    public Objectif(String pays, String vaccin, int nombre, Date dateMort, int volume, Date arrivee, Date dateSouhaitee) {
        this.pays = pays;
        this.vaccin = vaccin;
        this.nombre = nombre;
        this.dateMort = dateMort;
        this.volume = volume;
        this.arrivee = arrivee;
        this.dateSouhaitee = dateSouhaitee;
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

    public Date getDateMort() {
        return dateMort;
    }

    public void setDateMort(Date dateMort) {
        this.dateMort = dateMort;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Date getArrivee() {
        return arrivee;
    }

    public void setArrivee(Date arrivee) {
        this.arrivee = arrivee;
    }

    public Date getDateSouhaitee() {
        return dateSouhaitee;
    }

    public void setDateSouhaitee(Date dateSouhaitee) {
        this.dateSouhaitee = dateSouhaitee;
    }
}
