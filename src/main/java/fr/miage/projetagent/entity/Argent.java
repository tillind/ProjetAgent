/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;


import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Argent implements Serializable {
    

    private int Somme;
    @Id
    private String id =UUID.randomUUID().toString();

    public Argent(){
        this.Somme=0;
    }

    public int getSomme() {
        return Somme;
    }


    public void setSomme(int Somme) {
        this.Somme = Somme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
