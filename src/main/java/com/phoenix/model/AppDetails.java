package com.phoenix.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.LinkedList;
import java.util.List;

@Entity
public class AppDetails extends BaseModel {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public App getApp() {
        return app;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;



    @ManyToMany
    @JoinTable
    @Cascade(CascadeType.ALL)
    private List<Category> categories;

    @OneToOne
    private App app;
}
