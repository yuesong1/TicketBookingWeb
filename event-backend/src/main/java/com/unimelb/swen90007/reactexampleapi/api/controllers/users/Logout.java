package com.unimelb.swen90007.reactexampleapi.api.controllers.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET requests to /. note capital letters
 */
@CrossOrigin
@WebServlet(name = "Logout", value = "/logout")
public class Logout extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("Logout GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>Logout GET page</h3>");
        writer.println("<p>Send a POST request to logout</p>");
    }

    // POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("Login POST method");
        PrintWriter writer = response.getWriter();

        /// TODO LOG OUT
    }

}
