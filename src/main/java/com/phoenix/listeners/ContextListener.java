package com.phoenix.listeners;

import com.phoenix.model.Category;
import com.phoenix.model.CategoryImage;
import com.phoenix.util.Env;
import com.phoenix.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Env.setProperty("application.context",sce.getServletContext().getRealPath(""));
        System.out.println(Env.get("application.context"));

        ServletContextListener.super.contextInitialized(sce);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<Category> query = s.createQuery("SELECT c FROM Category c", Category.class);
        List<Category> categories = query.getResultList();
        System.out.println(categories.size());
        if (categories.size()==0) {
            Transaction ta = s.beginTransaction();
            Category category1 = new Category();
            Category category2 = new Category();
            Category category3 = new Category();
            Category category4 = new Category();
            Category category5 = new Category();

            CategoryImage image1=new CategoryImage();
            image1.setImage("test image1.jpg");
            image1.setCategory(category1);

            CategoryImage image2=new CategoryImage();
            image2.setImage("test image2.jpg");
            image2.setCategory(category2);

            CategoryImage image3=new CategoryImage();
            image3.setImage("test image3.jpg");
            image3.setCategory(category3);

            CategoryImage image4=new CategoryImage();
            image4.setImage("test image4.jpg");
            image4.setCategory(category4);

            CategoryImage image5=new CategoryImage();
            image5.setImage("test imag5.jpg");
            image5.setCategory(category5);


            category1.setCategoryName("Games");
            category2.setCategoryName("Music");
            category3.setCategoryName("Movies");
            category4.setCategoryName("Education");
            category5.setCategoryName("Social");

            category1.setImages(image1);
            category2.setImages(image2);
            category3.setImages(image3);
            category4.setImages(image4);
            category5.setImages(image5);

            try {
                s.persist(category1);
                s.persist(category2);
                s.persist(category3);
                s.persist(category4);
                s.persist(category5);

                ta.commit();
            } catch (Exception e) {
                e.printStackTrace();
                ta.rollback();
            }
        }
    }


}



