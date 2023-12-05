package com.unimelb.swen90007.reactexampleapi.api.controllers.events;

import com.unimelb.swen90007.reactexampleapi.api.domain.EventLogic;
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
@WebServlet(name = "DeleteEvent", value = "/delete-event")
public class DeleteEvent extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("DeleteEvent GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>DeleteEvent GET page</h3>");
        writer.println("<p>Send a DELETE request to delete an event</p>");
    }

    // DELETE request
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("DeleteEvent DELETE method");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        try {
            writer.print(EventLogic.deleteEvent(request.getParameter("id")));
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(400);
        }
    }
}
