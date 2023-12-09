package com.phoenix.middleware;


import com.phoenix.annotations.Auth;
import com.phoenix.dto.AuthDto;
import com.phoenix.model.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.server.mvc.Viewable;


import java.io.IOException;

@Auth
@Provider
public class IsAuth implements ContainerRequestFilter {
    @Context
    HttpServletRequest req;
    @Context
    HttpServletResponse resp;

    @Override
    public void filter(ContainerRequestContext containerRequestFilter) throws IOException {
        AuthDto a = (AuthDto) req.getSession().getAttribute("auth");
        if(a==null){
            containerRequestFilter.abortWith(
                    Response.
                            status(
                                    Response.
                                            Status.
                                            UNAUTHORIZED
                            ).entity(new Viewable("/views/401")).build());

        }
    }
}
