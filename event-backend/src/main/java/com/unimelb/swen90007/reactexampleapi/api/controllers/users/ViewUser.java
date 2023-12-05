package com.unimelb.swen90007.reactexampleapi.api.controllers.users;

import com.unimelb.swen90007.reactexampleapi.api.domain.UserLogic;
import com.unimelb.swen90007.reactexampleapi.api.mappers.UserMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.User;
import com.unimelb.swen90007.reactexampleapi.api.objects.UserAccess;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* Servlet for processing GET requests to /. note capital letters
 */
@CrossOrigin
@WebServlet(name = "ViewUser", value = "/view-user")
public class ViewUser extends HttpServlet {

    // GET request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        System.out.println("ViewUser GET method");
        PrintWriter writer = response.getWriter();
        String output = null;

        try {
            switch (request.getParameter("mode")) {
                case "all":
                    output = viewAll();
                    break;
                case "one":
                    output = UserLogic.viewOneUser(request.getParameter("email"), request.getParameter("type")).toString();
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

    // get all
    protected String viewAll() throws Exception {
        ArrayList[] adminCustomersPlanners = UserLogic.viewAllUsers();

        return "{" +
                "\"admin\": " + adminCustomersPlanners[0] + ", " +
                "\"customers\": " + adminCustomersPlanners[1] + ", " +
                "\"planners\": " + adminCustomersPlanners[2] +
                "}";
    }

}
