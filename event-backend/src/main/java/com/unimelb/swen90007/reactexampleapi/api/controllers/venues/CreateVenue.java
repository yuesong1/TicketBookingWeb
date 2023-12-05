package com.unimelb.swen90007.reactexampleapi.api.controllers.venues;

//import com.unimelb.swen90007.reactexampleapi.api.domain.venues.CreateVenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET & POST requests to /create-venue
*/
@CrossOrigin
@WebServlet(name = "CreateVenue", value = "/create-venue")
public class CreateVenue extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("CreateVenue GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>CreateVenue GET page</h3>");
        writer.println("<p>Send a POST request to create venue</p>");
    }

    /* Processes POST requests to /create-venue. it does the following:
     * 1. create a Venue instance (with associated Sections).
     * 2. upload this Venue onto PostgresSQL as relational data.
     * 3. notify the user that the data is successfully saved via 201 status.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("CreateVenue POST method");
        PrintWriter writer = response.getWriter();

        try {
            writer.print(VenueLogic.createVenue(request));
            response.setStatus(201);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

}
