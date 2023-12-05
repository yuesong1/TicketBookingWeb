package com.unimelb.swen90007.reactexampleapi.api.controllers.venues;

import com.unimelb.swen90007.reactexampleapi.api.application.Command;
import com.unimelb.swen90007.reactexampleapi.api.application.SavedVenueCommand;
import com.unimelb.swen90007.reactexampleapi.api.application.UpdateVenueCommand;
//import com.unimelb.swen90007.reactexampleapi.api.domain.venues.CreateVenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.InvalidUpdateException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.LockFailureException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET & POST requests to /create-venue
 */
@CrossOrigin
@WebServlet(name = "UpdateVenue", value = "/update-venue")
public class UpdateVenue extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("UpdateVenue GET method");
        PrintWriter writer = response.getWriter();
        try {
            Command cmd = new UpdateVenueCommand();
            cmd.init(request, response);
            cmd.process();
            writer.print(request.getSession().getAttribute("venue"));
        } catch(InvalidUpdateException e){
            e.printStackTrace();
            response.setStatus(405);
        } catch (LockFailureException e){
            e.printStackTrace();
            response.setStatus(400);
        } catch (Exception e) {
            writer.println("An exception occurred: " + e.getMessage());
            e.printStackTrace(response.getWriter());
        }
        writer.println("<h3>UpdateVenue GET page</h3>");
        writer.println("<p>Send a POST request to update venue</p>");

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
        System.out.println("UpdateVenue POST method");
        PrintWriter writer = response.getWriter();

        try {
//            writer.print(VenueLogic.updateVenue(request));
            Command cmd = new SavedVenueCommand();
            cmd.init(request, response);
            cmd.process();
            /**
             * Do we really return anything ????
             */
//            writer.print(request.getSession().getAttribute("newVenue"));
            response.setStatus(201);
        }catch (InvalidUpdateException e) {
            e.printStackTrace();
            response.setStatus(405); // ?????????
        } catch (DoesNotExistException e) {
            e.printStackTrace();
            response.setStatus(400);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

}
