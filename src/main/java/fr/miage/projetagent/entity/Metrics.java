package fr.miage.projetagent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Metrics implements Serializable  {

    @Id
    private String id = UUID.randomUUID().toString();

    private int volumeReserve;
    private int volumeEnvoye;
    private int nbJetes;
    private int nbEnvoyes; //donc nb personnes gueries

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the volumeReserve
     */
    public int getVolumeReserve() {
        return volumeReserve;
    }

    /**
     * @param volumeReserve the volumeReserve to set
     */
    public void setVolumeReserve(int volumeReserve) {
        this.volumeReserve = volumeReserve;
    }

    /**
     * @return the volumeEnvoye
     */
    public int getVolumeEnvoye() {
        return volumeEnvoye;
    }

    /**
     * @param volumeEnvoye the volumeEnvoye to set
     */
    public void setVolumeEnvoye(int volumeEnvoye) {
        this.volumeEnvoye = volumeEnvoye;
    }

    /**
     * @return the nbJetes
     */
    public int getNbJetes() {
        return nbJetes;
    }

    /**
     * @param nbJetes the nbJetes to set
     */
    public void setNbJetes(int nbJetes) {
        this.nbJetes = nbJetes;
    }

    /**
     * @return the nbEnvoyes
     */
    public int getNbEnvoyes() {
        return nbEnvoyes;
    }

    /**
     * @param nbEnvoyes the nbEnvoyes to set
     */
    public void setNbEnvoyes(int nbEnvoyes) {
        this.nbEnvoyes = nbEnvoyes;
    }


}
