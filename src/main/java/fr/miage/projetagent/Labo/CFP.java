package fr.miage.projetagent.Labo;

import java.io.Serializable;
import java.util.Date;

public class CFP implements Serializable {

    private String maladie;
    private int nb;
    private Date date;

    public CFP(String maladie, int nb, Date date) {
        this.maladie = maladie;
        this.nb = nb;
        this.date = date;
    }
}
