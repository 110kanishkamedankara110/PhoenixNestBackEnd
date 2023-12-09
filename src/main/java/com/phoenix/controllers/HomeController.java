package com.phoenix.controllers;

import com.phoenix.annotations.Auth;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("/admin/Home")
@Auth
public class HomeController {
    @GET
    public Viewable login() {
        return new Viewable("/views/Home");
    }

}
