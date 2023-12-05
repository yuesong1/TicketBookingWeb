package com.unimelb.swen90007.reactexampleapi.api.controllers.venues;

import com.unimelb.swen90007.reactexampleapi.api.application.*;
import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.LockFailureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET requests to /view-venue.
*/
@CrossOrigin
@WebServlet(name = "ViewVenue", value = "/view-venue")
public class ViewVenue extends HttpServlet {

    // GET request. queries for venue information from database and returns it to frontend as JSON string
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("ViewVenue GET method");
        PrintWriter writer = response.getWriter();
        String output = null;
        Command cmd;
        try {
            switch (request.getParameter("mode")) {
                case "one":
                    cmd = new ViewVenueCommand();
                    cmd.init(request, response);
                    cmd.process();
                    output = request.getSession().getAttribute("venue").toString();
                    break;
                case "all":
                    // Don't need to lock view all, don't need to lock everything.
                    // Only admin will view it and modify it. it will be a sequential process.
                    cmd = new ViewAllVenueCommand();
                    cmd.init(request, response);
                    cmd.process();
                    output = request.getSession().getAttribute("venue").toString();
                    break;
                default:
                    throw new Exception("Invalid mode");
            }

            writer.print(output);
            response.setStatus(200);
        } catch (DoesNotExistException e) {
            e.printStackTrace();
            response.setStatus(400);
        } catch (LockFailureException e) {
            e.printStackTrace();
            response.setStatus(405);      //？？？？？？？？
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("ViewVenue POST method");
//        PrintWriter writer = response.getWriter();
        Command cmd;
        try {
            switch (request.getParameter("mode")) {
                case "one":
                    cmd = new ReleaseVenueCommand();
                    cmd.init(request, response);
                    cmd.process();
                    break;
                case "all":
                    cmd = new ReleaseAllVenueCommand();
                    cmd.init(request, response);
                    cmd.process();
                    break;
                default:
                    throw new Exception("Invalid mode");
            }
//            writer.print(VenueLogic.updateVenue(request));
//            writer.print(request.getSession().getAttribute("newVenue"));
            response.setStatus(201);
        } catch (DoesNotExistException e) {
            e.printStackTrace();
            response.setStatus(400);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }
}
