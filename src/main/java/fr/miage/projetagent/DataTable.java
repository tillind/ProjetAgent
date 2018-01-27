/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.projetagent;

import java.util.Comparator;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Arthur
 */
public class DataTable{

    private final StringProperty pays;
    private final StringProperty maladie;
    private final StringProperty nombre;

    public DataTable(String pays, String maladie, String nombre) {
        this.pays = new SimpleStringProperty(pays);
        this.maladie = new SimpleStringProperty(maladie);
        this.nombre = new SimpleStringProperty(nombre);
    }

    public StringProperty getPays() {
        return pays;
    }

    public StringProperty getMaladie() {
        return maladie;
    }

    public StringProperty getNombre() {
        return nombre;
    }
    public StringProperty paysProperty(){
        return this.pays;
    }
    public StringProperty maladieProperty(){
        return this.maladie;
    }
    public StringProperty nombreProperty(){
        return this.nombre;
    }


}
