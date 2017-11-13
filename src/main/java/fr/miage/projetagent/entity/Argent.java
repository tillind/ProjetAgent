/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

/**
 *
 * @author alex
 */
public class Argent {

    /**
     * @return the Somme
     */
    public int getSomme() {
        return Somme;
    }

    /**
     * @param Somme the Somme to set
     */
    public void setSomme(int Somme) {
        this.Somme = Somme;
    }
    
    
    private int Somme;
    
    public Argent(){
        this.Somme=0;
    }
    
}
