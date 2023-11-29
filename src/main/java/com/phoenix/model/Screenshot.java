package com.phoenix.model;

import jakarta.persistence.*;

@Entity
public class Screenshot extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private AppReleases appRelease;
    private String screenshot;

    public AppReleases getAppRelease() {
        return appRelease;
    }

    public void setAppRelease(AppReleases appRelease) {
        this.appRelease = appRelease;
    }

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
