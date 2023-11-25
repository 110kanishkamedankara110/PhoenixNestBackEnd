package com.phoenix.util;

import java.io.InputStream;
import java.util.Properties;

public class Env {
    private static Properties properties=new Properties();
    static {
        try {
            InputStream inputStream=Env.class.getResourceAsStream("application.properties");
            properties.load(inputStream);
        }  catch (Exception e){

        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }
    public static void setProperty(String key,String value){
        properties.setProperty(key,value);
    }

}
