/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent.entity;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Maladie implements Serializable {

    @Id
    private String nom;
    
}
