package com.phoenix.util;

import com.phoenix.model.AppReleases;

import java.util.Comparator;
import java.util.Map;

public class ColorValComparator implements Comparator<String> {

    Map<String, Integer> base;

    public ColorValComparator(Map<String, Integer> base) {
        this.base = base;
    }
    @Override
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        }
    }

}
