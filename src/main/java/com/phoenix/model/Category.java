package com.phoenix.model;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Entity
public class Category extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoryName;

    public void setImages(List<CategoryImage> images) {
        this.images = images;
    }

    public List<AppDetails> getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(AppDetails appDetails) {
        this.appDetails.add(appDetails);
    }

    @OneToMany(mappedBy = "category")
    @Cascade(CascadeType.ALL)
    private List<CategoryImage> images = new LinkedList();

    @ManyToMany(mappedBy = "categories")
    private List<AppDetails> appDetails=new LinkedList();

    public List<CategoryImage> getImages() {
        return images;
    }

    public void setImages(CategoryImage images) {
        this.images.add(images);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
