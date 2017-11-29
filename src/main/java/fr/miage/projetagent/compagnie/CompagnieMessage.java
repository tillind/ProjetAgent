package fr.miage.projetagent.compagnie;

import fr.miage.projetagent.entity.Pays;

import java.io.Serializable;
import java.util.Date;

public class CompagnieMessage implements Serializable {

    private int volume;
    private Date date;
    private Pays pays;

    public CompagnieMessage(int volume, Date date, Pays pays) {
        this.volume = volume;
        this.date = date;
        this.pays = pays;
    }
}
