package com.unimelb.swen90007.reactexampleapi.api.controllers.bookings;

import com.unimelb.swen90007.reactexampleapi.api.domain.BookingLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/* Servlet for processing GET requests to /. note capital letters
 */
@CrossOrigin
@WebServlet(name = "CreateBooking", value = "/create-booking")
public class CreateBooking extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("CreateBooking GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>CreateBooking GET page</h3>");
        writer.println("<p>Send a POST request to create a booking</p>");
    }

    // POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("CreateBooking POST method");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        BookingLogic bookingLogic = new BookingLogic();

        try {
            writer.println(bookingLogic.createBookings(request));
            response.setStatus(201);
        } catch (Exception e) {
            response.setStatus(400);
        }

    }

}
