package com.unimelb.swen90007.reactexampleapi.api.controllers.events;

import com.unimelb.swen90007.reactexampleapi.api.domain.EventLogic;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;

/* Servlet for processing GET requests to /ViewVenue. note capital letters
 */
@CrossOrigin
@WebServlet(name = "ViewEvent", value = "/view-event")
public class ViewEvent extends HttpServlet {

    // GET request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("ViewEvent GET method");
        PrintWriter writer = response.getWriter();
        String output = null;

        try {
            switch (request.getParameter("mode")) {
                case "byPlanner":
                    EventLogic e = new EventLogic();
                    output = e.viewByPlanner(request.getParameter("id")).toString();
                    break;
                case "one":
                    output = EventLogic.viewOne(request.getParameter("id")).toString();
                    break;
                case "search":
                    output = EventLogic.viewSearch(request.getParameter("input")).toString();
                    break;
                case "6months":
                    output = EventLogic.view6Month().toString();
                    break;
                case "all":
                    output = EventLogic.viewAll().toString();
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
