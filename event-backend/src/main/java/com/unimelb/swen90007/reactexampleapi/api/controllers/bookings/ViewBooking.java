package com.unimelb.swen90007.reactexampleapi.api.controllers.bookings;

import com.unimelb.swen90007.reactexampleapi.api.domain.BookingLogic;
import com.unimelb.swen90007.reactexampleapi.api.mappers.BookingMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.Booking;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/* Servlet for processing GET requests to /ViewVenue. note capital letters
 */
@CrossOrigin
@WebServlet(name = "ViewBooking", value = "/view-booking")
public class ViewBooking extends HttpServlet {

    // GET request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("ViewBooking GET method");
        PrintWriter writer = response.getWriter();
        String output = null;
        BookingLogic bookingLogic = new BookingLogic();

        try {
            switch (request.getParameter("mode")) {
                case "byEvent":
                    output = bookingLogic.viewBookingsByEvent(request.getParameter("id")).toString();
                    break;
                case "byCustomer":
                    output = bookingLogic.viewBookingsByCustomer(request.getParameter("id")).toString();
                    break;
                default:
                    throw new Exception("Invalid mode");
            }
            writer.print(output);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

}
