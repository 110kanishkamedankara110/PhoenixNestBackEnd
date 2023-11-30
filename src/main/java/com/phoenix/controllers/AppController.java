package com.phoenix.controllers;

import com.phoenix.dto.*;
import com.phoenix.model.*;
import com.phoenix.util.FileUploader;
import com.phoenix.util.HibernateUtil;
import com.phoenix.util.ReleasesComparator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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
    @Path("release")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Message release(@FormDataParam("appRelease") AppReleaseDto app,
                           @FormDataParam("screenshots") List<FormDataBodyPart> ss,
                           @FormDataParam("apk") InputStream apk,
                           @FormDataParam("apk") FormDataContentDisposition apkContent,
                           @FormDataParam("screenshots") List<FormDataContentDisposition> ssContent

    ) throws Exception {

        String packageName = app.getPackageName();


        File mainLocation = new File("/PhoenixNest");
        File appsFile = new File(mainLocation + "/apps");
        File packageFile = new File(appsFile + "/" + packageName);

        File releasesFile = new File(packageFile + "/releases");
        File releaseVersionCodeFile = new File(releasesFile + "/" + app.getVersionCode());
        File screenShotsFile = new File(releaseVersionCodeFile + "/screenshots");

        if (!mainLocation.exists()) {
            mainLocation.mkdir();
        }

        if (!appsFile.exists()) {
            appsFile.mkdir();
        }
        if (!packageFile.exists()) {
            packageFile.mkdir();
        }
        if (!releasesFile.exists()) {
            releasesFile.mkdir();
        }
        if (!releaseVersionCodeFile.exists()) {
            releaseVersionCodeFile.mkdir();
        }
        if (!screenShotsFile.exists()) {
            screenShotsFile.mkdir();
        }


        AppReleases appReleases = new AppReleases();
        appReleases.setVersion(app.getVersion());
        appReleases.setVersionCode(app.getVersionCode());


        File apkFile = new File(releaseVersionCodeFile + "/" + apkContent.getFileName());
        List<Screenshot> screenShotsLoc = new LinkedList();


        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<AppReleases> apListQuery = s.createQuery("SELECT r FROM AppReleases r WHERE r.app.packageName=:pkg AND r.versionCode=:vc ");
        apListQuery.setParameter("pkg", app.getPackageName());
        apListQuery.setParameter("vc", app.getVersionCode());

        List<AppReleases> l = apListQuery.getResultList();
        String message = "Sucess";
        if (l.size() == 0) {
            App app1 = s.find(App.class, app.getPackageName());

            String apkLocation = FileUploader.upload(apk, apkFile);
            appReleases.setApk(apkContent.getFileName());
            appReleases.setApp(app1);
            appReleases.setScreenshots(screenShotsLoc);
            app1.setAppReleasesList(appReleases);

            for (FormDataBodyPart filePart : ss) {
                InputStream fileInputStream = filePart.getValueAs(InputStream.class);
                FormDataContentDisposition contentDispositionHeader = filePart.getFormDataContentDisposition();

                File ssfile = new File(screenShotsFile + "/" + contentDispositionHeader.getFileName());
                String ssLocName = FileUploader.upload(fileInputStream, ssfile);
                Screenshot scs = new Screenshot();
                scs.setScreenshot(contentDispositionHeader.getFileName());
                scs.setAppRelease(appReleases);
                screenShotsLoc.add(scs);
            }


            Transaction ta = s.beginTransaction();

            try {
                s.persist(app1);
                ta.commit();
            } catch (Exception e) {
                ta.rollback();
                e.printStackTrace();
                message = "error";
            }
        } else {
            message = "Sorry This Version Is already Released";
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

    @GET
    @Path("getAllApps")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppDto> getAllApps() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<App> query = s.createQuery("SELECT a FROM App a WHERE a.isActive ORDER BY a.updated DESC  ", App.class);
        List<App> apps = query.getResultList();
        List<AppDto> appList = new LinkedList();
        final AppReleases[] newRelease = new AppReleases[1];
        apps.forEach(a -> {
            AppDto appDto = new AppDto();

            List<String> screenShots = new LinkedList();
            if (a.getAppDetails() != null) {
                appDto.setDescription(a.getAppDetails().getDescription());
                List<String> categories = new LinkedList();
                a.getAppDetails().getCategories().forEach(c -> {
                    categories.add(c.getCategoryName());
                });
                List<AppReleases> releases = a.getAppReleasesList();

                Collections.sort(releases, new ReleasesComparator());

                for (AppReleases r : releases) {
                    if (r.isApproved()) {
                        newRelease[0] =r;
                        break;
                    }
                }
                appDto.setApk(newRelease[0].getApk());
                appDto.setVersionCode(newRelease[0].getVersionCode());
                appDto.setVersion(newRelease[0].getVersion());
                newRelease[0].getScreenshots().forEach(ss -> {
                    screenShots.add(ss.getScreenshot());
                });
                appDto.setScreenShots(screenShots);
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

    @GET
    @Path("getAllApps/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppDto> getAllAppsFilter(@PathParam("key") String key) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<App> query = s.createQuery("SELECT a FROM App a WHERE (a.user=:user OR a.packageName=:pkgName) AND a.isActive ORDER BY a.updated DESC  ", App.class);
        query.setParameter("user", key);
        query.setParameter("pkgName", key);
        List<App> apps = query.getResultList();
        List<AppDto> appList = new LinkedList();
        AtomicReference<AppReleases> newRelease=null;
        apps.forEach(a -> {
            AppDto appDto = new AppDto();

            List<String> screenShots = new LinkedList();
            if (a.getAppDetails() != null) {
                appDto.setDescription(a.getAppDetails().getDescription());
                List<String> categories = new LinkedList();
                a.getAppDetails().getCategories().forEach(c -> {
                    categories.add(c.getCategoryName());
                });
                List<AppReleases> releases = a.getAppReleasesList();

                Collections.sort(releases, new ReleasesComparator());

                for (AppReleases r : releases) {
                    if (r.isApproved()) {
                        newRelease.set(r);
                        break;
                    }
                }
                appDto.setApk(newRelease.get().getApk());
                appDto.setVersionCode(newRelease.get().getVersionCode());
                appDto.setVersion(newRelease.get().getVersion());
                newRelease.get().getScreenshots().forEach(ss -> {
                    screenShots.add(ss.getScreenshot());
                });
                appDto.setScreenShots(screenShots);
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

    @GET
    @Path("download/{packegeName}/{versionCode}/{apk}")
    @Produces("application/vnd.android.package-archive")
    public InputStream download(@PathParam("packegeName") String packegeName
            ,@PathParam("versionCode") String versionCode
            ,@PathParam("apk") String apk) throws Exception{

        File f=new File("/PhoenixNest/apps/"+packegeName+"/releases/"+versionCode+"/"+apk);
        FileInputStream fd=new FileInputStream(f);
        return fd;
    }
//    public Response download(
//            @PathParam("packegeName") String packageName,
//            @PathParam("versionCode") String versionCode,
//            @PathParam("apk") String apk) {
//
//        // Build the file path
//        String filePath = "/PhoenixNest/apps/" + packageName + "/releases/" + versionCode + "/" + apk;
//
//        File file = new File(filePath);
//
//        if (!file.exists()) {
//            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
//        }
//
//        StreamingOutput stream = output -> {
//            try (InputStream fileInputStream = new FileInputStream(file)) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//                output.flush();
//            }
//        };
//
//        return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
//                .header("content-disposition", "attachment; filename=" + file.getName())
//                .build();
//    }


}
