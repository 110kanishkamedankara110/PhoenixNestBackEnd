package com.phoenix.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final Configuration CONFIGURATION=new Configuration();
    private static final SessionFactory SESSION_FACTORY;
    static{
        CONFIGURATION.configure();
        SESSION_FACTORY=CONFIGURATION.buildSessionFactory();
    }
    public static SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }

}
