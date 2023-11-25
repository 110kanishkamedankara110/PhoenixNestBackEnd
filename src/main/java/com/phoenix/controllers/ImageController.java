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

@Path("/api/image/{image}")
public class ImageController {
    @GET
    @Produces("application/octet-stream")
    public InputStream getImage(@PathParam("image") String image) throws Exception{
        File f=new File("/PhoenixNest/category/"+image);
        FileInputStream fd=new FileInputStream(f);
        return fd;
    }
}
