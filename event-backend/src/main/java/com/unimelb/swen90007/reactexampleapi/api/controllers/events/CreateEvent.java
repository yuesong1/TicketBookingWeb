package com.unimelb.swen90007.reactexampleapi.api.controllers.events;

import com.unimelb.swen90007.reactexampleapi.api.application.Command;
import com.unimelb.swen90007.reactexampleapi.api.application.ReleaseEventCommand;
import com.unimelb.swen90007.reactexampleapi.api.domain.EventLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
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
@WebServlet(name = "CreateEvent", value = "/create-event")
public class CreateEvent extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("CreateEvent GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>CreateEvent GET page</h3>");
        writer.println("<p>Send a POST request to create an event</p>");
    }

    // POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("CreateEvent POST method");
        PrintWriter writer = response.getWriter();

        try {
            Command cmd = new ReleaseEventCommand();
            cmd.init(request, response);
            cmd.process();
            writer.print(request.getSession().getAttribute("event").toString());
            
            response.setStatus(201);
        } catch (OptimisticLockingException e) {
            response.setStatus(405);
        } catch (Exception e) {
            response.setStatus(400);
        }
    }

}
