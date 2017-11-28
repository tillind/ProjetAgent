package fr.miage.projetagent.Labo;

import java.util.Date;

public class Propose {

    private Integer nb;
    private Integer prix;
    private Date dateDebut;
    private Date dateFin;
    private int volume;

    public Propose() {
    }

    public Propose(Integer nb, Integer prix, Date dateDebut, Date dateFin, int volume) {
        this.nb = nb;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.volume = volume;
    }


    public Integer getNb() {
        return nb;
    }

    public void setNb(Integer nb) {
        this.nb = nb;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
