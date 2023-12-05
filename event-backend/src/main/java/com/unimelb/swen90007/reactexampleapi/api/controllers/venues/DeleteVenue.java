package com.unimelb.swen90007.reactexampleapi.api.controllers.venues;

//import com.unimelb.swen90007.reactexampleapi.api.domain.venues.CreateVenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.InvalidUpdateException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET & POST requests to /create-venue
 */
@CrossOrigin
@WebServlet(name = "DeleteVenue", value = "/delete-venue")
public class DeleteVenue extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("DeleteVenue GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>DeleteVenue GET page</h3>");
        writer.println("<p>Send a DELETE request to delete venue</p>");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("DeleteVenue DELETE method");
        PrintWriter writer = response.getWriter();

        try {
            writer.print(VenueLogic.deleteVenue(request.getParameter("id")));
            response.setStatus(201);
        } catch (InvalidUpdateException e){
            e.printStackTrace();
            response.setStatus(405);
        }
        catch (DoesNotExistException e) {
            e.printStackTrace();
            response.setStatus(404);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

}
