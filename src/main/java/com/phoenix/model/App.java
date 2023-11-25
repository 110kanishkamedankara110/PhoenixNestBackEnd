package com.phoenix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class App extends BaseModel{
    @Id
    private String packageName;
    private String user;
    private String appTitle;
    private String mainActivity;
    private String appIcon;
    private String appBanner;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppBanner() {
        return appBanner;
    }

    public void setAppBanner(String appBanner) {
        this.appBanner = appBanner;
    }
}
