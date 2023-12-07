package com.phoenix;

import com.phoenix.config.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Server {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
        Context context = tomcat.addWebapp("/PhoenixNestBackEnd", new File("./src/main/webapp/").getAbsolutePath());
        context.setAllowCasualMultipartParsing(true);
        tomcat.addServlet(context, "app", new ServletContainer(new AppConfig()));
        context.addServletMappingDecoded("/*", "app");


        tomcat.start();
    }

}
