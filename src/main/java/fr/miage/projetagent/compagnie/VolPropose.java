package fr.miage.projetagent.compagnie;

import java.util.Date;

public class VolPropose {

    private String pays;
    private Date dateArrivee;
    private Double volume;
    private Double prix;
    private String idVol;

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getIdVol() {
        return idVol;
    }

    public void setIdVol(String idVol) {
        this.idVol = idVol;
    }

    @Override
    public String toString() {
        return "VolPropose{" +
                "pays='" + pays + '\'' +
                ", dateArrivee=" + dateArrivee +
                ", volume=" + volume +
                ", prix=" + prix +
                ", idVol='" + idVol + '\'' +
                '}';
    }
}
