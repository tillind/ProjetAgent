package fr.miage.projetagent;

import fr.miage.projetagent.bdd.BddAgent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataApp {

    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("agentBdd");
    static EntityManager em = entityManagerFactory.createEntityManager();


    public static void main(String[] args) {
        BddAgent.dropBase();
        BddAgent.addData();
    }
}
