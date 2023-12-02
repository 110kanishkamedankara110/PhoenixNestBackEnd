package com.phoenix.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Colors {

    private static Map<String, Integer> extractColors(File f) throws Exception {
        BufferedImage bi = ImageIO.read(f);
        Map<String, Integer> colors = new HashMap();
        for (int y = 0; y < bi.getHeight(); y = y + 10) {
            for (int x = 0; x < bi.getWidth(); x = x + 10) {
                int p = bi.getRGB(x, y);

                int red = (p >> 16) & 0xff;
                int green = (p >> 8) & 0xff;
                int blue = (p) & 0xff;

                int avg = (red + green + blue) / 3;
                if (avg >= 20 || avg < 10) {
                    String colo = String.format("rgb(%s,%s,%s)", red, green, blue);
                    if (colors.get(colo) == null) {
                        colors.put(colo, 1);
                    } else {
                        int count = (Integer) colors.get(colo) + 1;
                        colors.replace(colo, count);
                    }
                    
                }


            }
        }
        ColorValComparator cvc = new ColorValComparator(colors);
        TreeMap<String, Integer> sorted_colors = new TreeMap(cvc);
        sorted_colors.putAll(colors);
        System.out.println("prev : " + colors);
        System.out.println("sortad : " + sorted_colors);

        return sorted_colors;
    }

    public static String getMax(File f) throws Exception {
        Map<String, Integer> colors = extractColors(f);
        Set<String> keysSet=colors.keySet();
        String[] k= keysSet.toArray(new String[0]);

        return k[k.length-1];

    }

    public static String getMin(File f) throws Exception {
        Map<String, Integer> colors = extractColors(f);
        Set<String> keysSet=colors.keySet();
        String[] k=keysSet.toArray(new String[0]);

        return k[0];
    }

}
