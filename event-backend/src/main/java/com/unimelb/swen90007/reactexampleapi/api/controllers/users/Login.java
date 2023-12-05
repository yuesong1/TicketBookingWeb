package com.unimelb.swen90007.reactexampleapi.api.controllers.users;

import com.unimelb.swen90007.reactexampleapi.api.auth.JwtTokenUtil;
import com.unimelb.swen90007.reactexampleapi.api.domain.UserLogic;

import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.mappers.UserMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.User;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/* Servlet for processing GET requests to /. note capital letters
 */
@CrossOrigin
@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {

    // response to GET request. displays header just to show that it is running
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        System.out.println("Login GET method");
        PrintWriter writer = response.getWriter();
        writer.println("<h3>Login GET page</h3>");
        writer.println("<p>Send a POST request to login</p>");
    }

    // POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        System.out.println("Login POST method");
        PrintWriter writer = response.getWriter();
        Connection conn = DBUtil.connection();

        try {
            User login = UserLogic.userInstant(request);
            User curr = (User) MapperRegistry.getInstance().getMapper(User.class).find(login.getPrimaryKey());

            if (curr != null && curr.getPassword().equals(login.getPassword()) && curr.getRole().equals(login.getRole())) {
                String token = JwtTokenUtil.generateToken(curr.getUsername(), String.valueOf(curr.getRole())); // Generate a token

                // Save the token to the user's database
                curr.setToken(token);
                System.out.println(token);
                UserMapper.updateUserToken(curr.getId(), token, conn);
                System.out.println("Token Updated");

                writer.print(
                        "{" +
                                "\"user\": " + curr + ", " +
                                "\"token\": \"" + token + "\"" +
                                "}"
                );
                response.setStatus(200);
            } else {
                response.setStatus(401); // Unauthorized
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400); // Bad Request
        }
    }

}
