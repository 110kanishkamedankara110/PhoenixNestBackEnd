package com.phoenix.controllers;

import com.phoenix.dto.*;
import com.phoenix.model.App;
import com.phoenix.model.AppDetails;
import com.phoenix.model.Category;
import com.phoenix.util.FileUploader;
import com.phoenix.util.HibernateUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


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
        app1.setAppBanner(contentDispositionHeaderAppBanner.getFileName());
        app1.setAppIcon(contentDispositionHeaderAppIcon.getFileName());
        app1.setAppTitle(app.getAppName());
        app1.setPackageName(app.getPakgeName());
        app1.setUser(app.getUser());
        app1.setMainActivity(app.getMainActivity());

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        App myApp = s.find(App.class, app1.getPackageName());
        String message = "Sucess";
        if (myApp == null) {
            Transaction ta = s.beginTransaction();

            try {
                s.persist(app1);
                ta.commit();
            } catch (Exception e) {
                ta.rollback();
                e.printStackTrace();
            }

        } else {
            message = "This App Already Exists";
        }


        return new Message().setMessage(message);
    }

    @POST
    @Path("addAppDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Message addAppDetails(AppDetailsDto appDetailsDto) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        App a = s.find(App.class, appDetailsDto.getPackageName());
        List<Category> categories = new LinkedList();
        appDetailsDto.getCategories().forEach(c -> {
            Query<Category> q = s.createQuery("SELECT c FROM Category c WHERE c.categoryName=:category");
            q.setParameter("category", c);
            Category category = q.getSingleResult();
            categories.add(category);
        });

        AppDetails appDetails = new AppDetails();
        appDetails.setCategories(categories);
        appDetails.setDescription(appDetailsDto.getDescription());
        appDetails.setApp(a);

        a.setAppDetails(appDetails);

        Transaction ta = s.beginTransaction();
        try {
            s.merge(a);
            ta.commit();

            return new Message().setMessage("Success");

        } catch (Exception e) {
            ta.rollback();
            return new Message().setMessage("Error");

        }


    }
    @POST
    @Path("updateAppDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Message updateAppDetails(AppDetailsDto appDetailsDto) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        App a = s.find(App.class, appDetailsDto.getPackageName());
        List<Category> categories = new LinkedList();
        appDetailsDto.getCategories().forEach(c -> {
            Query<Category> q = s.createQuery("SELECT c FROM Category c WHERE c.categoryName=:category");
            q.setParameter("category", c);
            Category category = q.getSingleResult();
            categories.add(category);
        });

        AppDetails appDetails = a.getAppDetails();
        appDetails.setCategories(categories);
        appDetails.setDescription(appDetailsDto.getDescription());


        Transaction ta = s.beginTransaction();
        try {
            s.merge(a);
            ta.commit();

            return new Message().setMessage("Success");

        } catch (Exception e) {
            ta.rollback();
            return new Message().setMessage("Error");

        }


    }

    @GET
    @Path("getApps")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppDto> getApps() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<App> query = s.createQuery("SELECT a FROM App a ORDER BY a.updated DESC ", App.class);
        List<App> apps = query.getResultList();

        List<AppDto> appList = new LinkedList();

        apps.forEach(a -> {
            AppDto appDto = new AppDto();

            if (a.getAppDetails() != null) {
                appDto.setDescription(a.getAppDetails().getDescription());
                List<String> categories = new LinkedList();
                a.getAppDetails().getCategories().forEach(c -> {
                    categories.add(c.getCategoryName());
                });
                appDto.setCategoryies(categories);
            }
            try {
                File f = new File("/PhoenixNest/apps/" + a.getPackageName() + "/appBanner/" + a.getAppBanner());
                BufferedImage image = ImageIO.read(f);
                int height = image.getHeight();
                int width = image.getWidth();

                appDto.setHeight(height);
                appDto.setWidth(width);

            } catch (Exception e) {
                e.printStackTrace();
            }


            appDto.setAppIcon(a.getAppIcon());
            appDto.setAppBanner(a.getAppBanner());
            appDto.setAppTitle(a.getAppTitle());

            appDto.setPackageName(a.getPackageName());
            appDto.setMainActivity(a.getMainActivity());

            appList.add(appDto);
        });

        return appList;
    }

    @GET
    @Path("getApps/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppDto> getUserApps(@PathParam("key") String key) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<App> query = s.createQuery("SELECT a FROM App a WHERE a.user=:user OR a.packageName=:pkgName ORDER BY a.updated DESC  ", App.class);
        query.setParameter("user", key);
        query.setParameter("pkgName", key);
        List<App> apps = query.getResultList();

        List<AppDto> appList = new LinkedList();

        apps.forEach(a -> {
            AppDto appDto = new AppDto();
            List<String> screenShots = new LinkedList();
            if (a.getAppDetails() != null) {
                appDto.setDescription(a.getAppDetails().getDescription());
                List<String> categories = new LinkedList();
                a.getAppDetails().getCategories().forEach(c -> {
                    categories.add(c.getCategoryName());
                });
                appDto.setCategoryies(categories);
            }


            appDto.setAppIcon(a.getAppIcon());
            appDto.setAppBanner(a.getAppBanner());
            appDto.setAppTitle(a.getAppTitle());

            appDto.setPackageName(a.getPackageName());
            appDto.setMainActivity(a.getMainActivity());
            try {
                File f = new File("/PhoenixNest/apps/" + a.getPackageName() + "/appBanner/" + a.getAppBanner());
                BufferedImage image = ImageIO.read(f);
                int height = image.getHeight();
                int width = image.getWidth();

                appDto.setHeight(height);
                appDto.setWidth(width);

            } catch (Exception e) {
                e.printStackTrace();
            }
            appList.add(appDto);
        });

        return appList;
    }


}
