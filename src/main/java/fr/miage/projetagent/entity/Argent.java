/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Argent implements Serializable {
    

    private double Somme;
    @Id
    private String id =UUID.randomUUID().toString();

    public Argent(){
        this.Somme=0;
    }

    public double getSomme() {
        return Somme;
    }


    public void setSomme(double Somme) {
        this.Somme = Somme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
}
