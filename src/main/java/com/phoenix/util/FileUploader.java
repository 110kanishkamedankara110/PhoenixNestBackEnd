package com.phoenix.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileUploader {
    public static String upload(InputStream file,File destination) throws Exception{
        InputStream is = file;
        File f =destination;
        FileOutputStream fos = new FileOutputStream(f);


        ReadableByteChannel rbc = Channels.newChannel(is);
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);


        is.close();
        fos.close();
        fc.close();
        rbc.close();

        return f.getPath();
    }

}
