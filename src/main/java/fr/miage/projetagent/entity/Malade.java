/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Malade {

    @Id
    private String id;
    private TypeMalade etat;
    private Pays pays;
    private Maladie maladie;
    private Date dateContamination;

    public Malade() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypeMalade getEtat() {
        return etat;
    }

    public void setEtat(TypeMalade etat) {
        this.etat = etat;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Maladie getMaladie() {
        return maladie;
    }

    public void setMaladie(Maladie maladie) {
        this.maladie = maladie;
    }

    public Date getDateContamination() {
        return dateContamination;
    }

    public void setDateContamination(Date dateContamination) {
        this.dateContamination = dateContamination;
    }
}
