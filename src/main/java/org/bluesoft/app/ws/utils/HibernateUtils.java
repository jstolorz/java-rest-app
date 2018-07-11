package org.bluesoft.app.ws.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {


    private static final SessionFactory SESSION_FACTORY;

    static {
        Configuration configuration = new Configuration();
        configuration.configure();

        try{
            SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
        }catch (HibernateException e){
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError();
        }
    }

    public static SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }


}
