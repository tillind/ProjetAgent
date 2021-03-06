/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NamedQueries({
    @NamedQuery(
        name="Malade.deleteMort",
        query="DELETE FROM Malade m WHERE m.etat = 'Non_soignable' "),
    @NamedQuery(
        name="Malade.nombreMaladeMaladie",
        query="SELECT count(m.id) FROM Malade m JOIN m.pays p JOIN m.maladie ma WHERE ma.nom = :nomMa AND p.nom = :nompays"), 
    @NamedQuery(
        name="Malade.getMaladiesForCountry",
        query="SELECT DISTINCT m.maladie FROM Malade m JOIN m.pays p WHERE  p.nom = :nompays"),
    @NamedQuery(
            name="Malade.getCountryAndDiseaseOrderByNumberOfSick",
            query = "SELECT m.maladie.nom, m.pays.nom, count(m.id) FROM Malade m GROUP BY m.maladie, m.pays ORDER BY count(m.id) DESC"
    )

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
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateContamination;

    public Malade() {
        this.id = UUID.randomUUID().toString();
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
