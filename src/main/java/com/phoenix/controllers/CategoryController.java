package com.phoenix.controllers;

import com.phoenix.dto.AppDto;
import com.phoenix.dto.CategoryDto;
import com.phoenix.model.App;
import com.phoenix.model.AppReleases;
import com.phoenix.model.Category;
import com.phoenix.util.Colors;
import com.phoenix.util.Env;
import com.phoenix.util.HibernateUtil;
import com.phoenix.util.ReleasesComparator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


@Path("/api/category")
public class CategoryController {
    @GET
    @Produces("application/json")
    public List<CategoryDto> category() {


        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<Category> query = s.createQuery("SELECT c FROM Category c", Category.class);


        List<Category> categories = query.getResultList();


        List<CategoryDto> categoryList = new LinkedList();

        categories.forEach(c -> {

            List<String> images = new LinkedList();
            c.getImages().forEach(image -> {
                images.add(image.getImage());
            });
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setImages(images);
            categoryDto.setAppCount(c.getAppDetails().size());
            categoryDto.setId(c.getId());
            categoryDto.setCategory(c.getCategoryName());
            categoryList.add(categoryDto);
        });


        return categoryList;
    }

    @GET
    @Path("/getApps/{category}")
    @Produces("application/json")
    public List<AppDto> apps(@PathParam("category") String category) {


        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<Category> query = s.createQuery("SELECT c FROM Category c WHERE c.categoryName=:catrgory", Category.class);
        query.setParameter("catrgory", category);


        Category cate = query.getSingleResult();

        List<App> apps = new LinkedList();
        cate.getAppDetails().forEach(ad -> {
            apps.add(ad.getApp());
        });


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
                        newRelease[0] = r;
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
            appDto.setDownloads(a.getDownloads());
            appDto.setPackageName(a.getPackageName());
            appDto.setMainActivity(a.getMainActivity());
            try {
                File f = new File("/PhoenixNest/apps/" + a.getPackageName() + "/appBanner/" + a.getAppBanner());


                appDto.setMaxColor(Colors.getMax(f));
                appDto.setMinColor(Colors.getMin(f));

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
