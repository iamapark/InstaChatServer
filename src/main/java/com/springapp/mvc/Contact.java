package com.springapp.mvc;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class Contact {

    private Long id;
    private String email;
    private String regId;

    public Contact() {}

    public Contact(String email, String regId) {
        this.email = email;
        this.regId = regId;
    }

    public static Contact find(String email, EntityManager em) {
        Query q = em.createQuery("select c from Contact c where c.email = :email");
        q.setParameter("email", email);
        List<Contact> result = q.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public String getRegId(){
        return this.regId;
    }

    public void setRegId(String regId){
        this.regId = regId;
    }
}