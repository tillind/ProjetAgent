/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;

import jade.core.Agent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alex
 */
public abstract class Association extends Agent{
    private String nom;
    private Argent tresorerie;
    private Set<Lot> lot;
    
    public Association(){
        lot = new HashSet<>();
    }
    
}
