/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@NamedQueries({
    @NamedQuery(
        name="Malade.deleteMort",
        query="DELTE FROM Malade m WHERE m.etat = 'Non_soignable' "),
})
@Entity
public class Malade implements Serializable {

    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private TypeMalade etat;
    @OneToOne
    private Pays pays;
    @OneToOne
    private Maladie maladie;
    @Temporal(javax.persistence.TemporalType.DATE)
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
