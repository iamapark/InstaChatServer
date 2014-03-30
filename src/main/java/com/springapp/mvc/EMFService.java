package com.springapp.mvc;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by jinyoungpark on 14. 3. 30.
 */
public class EMFService {
    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");

    private EMFService() {
    }

    public static EntityManagerFactory get() {
        return emfInstance;
    }
}
