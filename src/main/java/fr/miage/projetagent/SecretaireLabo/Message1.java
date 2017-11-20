package fr.miage.projetagent.SecretaireLabo;

import java.io.Serializable;
import java.util.Date;

public class Message1 implements Serializable {

    private String maladie;
    private int nb;
    private Date date;

    public Message1(String maladie, int nb, Date date) {
        this.maladie = maladie;
        this.nb = nb;
        this.date = date;
    }
}
