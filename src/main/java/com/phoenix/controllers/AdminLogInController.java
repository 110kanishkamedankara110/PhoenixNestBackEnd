package com.phoenix.controllers;

import com.phoenix.dto.AuthDto;
import com.phoenix.dto.Message;
import com.phoenix.model.Admin;
import com.phoenix.model.AppReleases;
import com.phoenix.util.HibernateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import org.glassfish.jersey.server.mvc.Viewable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Path("/admin/login")

public class AdminLogInController {
    @GET
    public Viewable login() {
        return new Viewable("/views/index");
    }

    @POST
    public Message adminLogin(AuthDto a, @Context HttpServletRequest req) {
        Message m = new Message();
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Query<Admin> q = s.createQuery("SELECT a FROM Admin" + " a WHERE a.email=:email AND a.password=:password", Admin.class);
        q.setParameter("email", a.getEmail());
        q.setParameter("password", a.getPassword());

        Transaction ta = s.beginTransaction();
        try {
            Admin admin = q.getSingleResult();
            if (admin != null) {

                HttpSession ses = req.getSession();

                ses.setAttribute("auth", a);

                m.setMessage("Success");

            } else {

                m.setMessage("Error");

            }


            ta.commit();
        } catch (Exception e) {

            ta.rollback();
            e.printStackTrace();

        }
        s.close();
        return m;
    }

}
