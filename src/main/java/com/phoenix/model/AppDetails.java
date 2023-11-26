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

    public List<Screenshot> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(Screenshot screenshots) {
        this.screenshots.add(screenshots);
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @OneToMany(mappedBy = "appDetails")
    @Cascade(CascadeType.ALL)
    private List<Screenshot>screenshots=new LinkedList();

    @OneToOne
    private App app;
}
