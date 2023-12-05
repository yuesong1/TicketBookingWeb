package com.unimelb.swen90007.reactexampleapi.api.controllers.users;

import com.unimelb.swen90007.reactexampleapi.api.domain.UserLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DuplicateFieldException;
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
@WebServlet(name = "SignUp", value = "/sign-up")
public class SignUp extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("SignUp GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>SignUp GET page</h3>");
        writer.println("<p>Send a POST request to sign up</p>");
    }

    // POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("SignUp POST method");
        PrintWriter writer = response.getWriter();

        try {
            writer.print(UserLogic.signUp(request));
            response.setStatus(201);
        } catch (DuplicateFieldException e) {
//            e.printStackTrace();
            System.out.println("Duplicate email & role combination exists.");
            response.setStatus(409);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Invalid input.");
            response.setStatus(400);
        }
    }

}
