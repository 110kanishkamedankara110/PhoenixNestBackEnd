package com.phoenix.model;

import jakarta.persistence.*;

@Entity
public class Screenshot extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public AppDetails getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(AppDetails appDetails) {
        this.appDetails = appDetails;
    }

    private String screenshot;

    @ManyToOne
    private AppDetails appDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }
}
