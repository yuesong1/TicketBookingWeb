package com.unimelb.swen90007.reactexampleapi.api.controllers.events;

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


@CrossOrigin
@WebServlet(name = "UpdateEvent", value = "/update-event")
public class UpdateEvent extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("UpdateEvent GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>UpdateEvent GET page</h3>");
        writer.println("<p>Send a PUT request to update an event</p>");
    }

    // PUT request
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("UpdateEvent PUT method");
        PrintWriter writer = response.getWriter();

        try {
            EventLogic e = new EventLogic();
            writer.print(e.updateEvent(request));
            response.setStatus(201);
        } catch (OptimisticLockingException e){
            response.setStatus(405);
        } catch (Exception e) {
            response.setStatus(400);
        }
    }

}
