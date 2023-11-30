package com.phoenix.util;

import com.phoenix.model.AppReleases;

import java.util.Comparator;

public class ReleasesComparator implements Comparator<AppReleases> {
    @Override
    public int compare(AppReleases ar1, AppReleases ar2) {
        return Integer.parseInt(ar2.getVersionCode())-Integer.parseInt(ar1.getVersionCode());
    }
}
