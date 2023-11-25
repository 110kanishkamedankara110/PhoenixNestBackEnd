package com.phoenix.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.glassfish.jersey.internal.util.collection.Views;

import java.beans.Transient;

@Entity
public class CategoryImage extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String image;
    @ManyToOne
    private Category category;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
