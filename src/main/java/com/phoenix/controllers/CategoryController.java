package com.phoenix.controllers;

import com.phoenix.dto.CategoryDto;
import com.phoenix.model.Category;
import com.phoenix.util.Env;
import com.phoenix.util.HibernateUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


@Path("/api/category")
public class CategoryController{
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
            categoryDto.setId(c.getId());
            categoryDto.setCategory(c.getCategoryName());
            categoryList.add(categoryDto);
        });



        return categoryList;
    }
}
