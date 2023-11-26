package com.phoenix.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Path("/api/image/")
public class ImageController {
    @GET
    @Path("category/{image}")
    @Produces("application/octet-stream")
    public InputStream getImage(@PathParam("image") String image) throws Exception{
        File f=new File("/PhoenixNest/category/"+image);
        FileInputStream fd=new FileInputStream(f);
        return fd;
    }
    @GET
    @Path("appIcon/{packageName}/{image}")
    @Produces("application/octet-stream")
    public InputStream getappImage(@PathParam("image") String image,@PathParam("packageName") String packageName) throws Exception{
        File f=new File("/PhoenixNest/apps/"+packageName+"/appIcon/"+image);
        FileInputStream fd=new FileInputStream(f);
        return fd;
    }
    @GET
    @Path("appBanner/{packageName}/{image}")
    @Produces("application/octet-stream")
    public InputStream getappBanner(@PathParam("image") String image,@PathParam("packageName") String packageName) throws Exception{
        File f=new File("/PhoenixNest/apps/"+packageName+"/appBanner/"+image);
        FileInputStream fd=new FileInputStream(f);
        return fd;
    }
}
