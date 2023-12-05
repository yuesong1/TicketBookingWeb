package com.unimelb.swen90007.reactexampleapi.api.controllers;

import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET & POST requests to / or to /home.
*/
@CrossOrigin
@WebServlet(name = "Home", value = "/home")
public class Home extends HttpServlet {

    // shows header to indicate that app is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("Event-backend GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>Event-backend homepage GET page</h3>");
        // writer.println("<h3>Send a POST request to re-initialise database</h3>");
    }

    // // initialises database
    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //     System.out.println("Event-backend POST method");
    //     try {
            // DBUtil.initTables();
    //         response.setStatus(201);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         response.setStatus(400);
    //     }
    // }
}
