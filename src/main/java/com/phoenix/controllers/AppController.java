package com.phoenix.controllers;

import com.phoenix.dto.AppMain;
import com.phoenix.dto.Message;
import com.phoenix.model.App;
import com.phoenix.util.FileUploader;
import com.phoenix.util.HibernateUtil;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@Path("/api/app/")
public class AppController {

    private File mainLocation;

    @POST
    @Path("addApp")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Message addapp(@FormDataParam("appMain") AppMain app,
                           @FormDataParam("appBanner") InputStream appBanner,
                           @FormDataParam("appIcon") InputStream appIcon,
                           @FormDataParam("appIcon") FormDataContentDisposition contentDispositionHeaderAppIcon,
                           @FormDataParam("appBanner") FormDataContentDisposition contentDispositionHeaderAppBanner
    ) throws Exception {

        String packageName = app.getPakgeName();


        File mainLocation = new File("/PhoenixNest");
        File appsFile = new File(mainLocation + "/apps");
        File packageFile = new File(appsFile + "/" + packageName);
        File appIconFile = new File(packageFile + "/appIcon");
        File appBannerFile = new File(packageFile + "/appBanner");

        if (!mainLocation.exists()) {
            mainLocation.mkdir();
        }

        if (!appsFile.exists()) {
            appsFile.mkdir();
        }
        if (!packageFile.exists()) {
            packageFile.mkdir();
        }
        if (!appIconFile.exists()) {
            appIconFile.mkdir();
        }
        if (!appBannerFile.exists()) {
            appBannerFile.mkdir();
        }

        File appIconImageFile = new File(appIconFile + "/" + contentDispositionHeaderAppIcon.getFileName());
        File appBannerImageFile = new File(appBannerFile + "/" + contentDispositionHeaderAppBanner.getFileName());

        String appIconLocation = FileUploader.upload(appIcon, appIconImageFile);
        String appBannerLocation = FileUploader.upload(appBanner, appBannerImageFile);

        App app1 = new App();
        app1.setAppBanner(appBannerLocation);
        app1.setAppIcon(appIconLocation);
        app1.setAppTitle(app.getAppName());
        app1.setPackageName(app.getPakgeName());
        app1.setUser(app.getUser());
        app1.setMainActivity(app.getMainActivity());

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        App myApp=s.find(App.class,app1.getPackageName());
        String message="Sucess";
        if(myApp==null){
            Transaction ta = s.beginTransaction();

            try {
                s.persist(app1);
                ta.commit();
            } catch (Exception e) {
                ta.rollback();
                e.printStackTrace();
            }

        }else{
            message="This App Already Exists";
        }


        return new Message().setMessage(message);
    }
}
