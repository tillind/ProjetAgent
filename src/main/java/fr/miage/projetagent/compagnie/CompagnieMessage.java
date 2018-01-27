package fr.miage.projetagent.compagnie;

import fr.miage.projetagent.entity.Pays;

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
}
