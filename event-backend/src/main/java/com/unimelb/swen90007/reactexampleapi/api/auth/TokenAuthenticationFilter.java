package com.unimelb.swen90007.reactexampleapi.api.auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.unimelb.swen90007.reactexampleapi.api.auth.JwtTokenUtil.*;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Bypass the filter for login, signup, and the main page
        if (requestURI.endsWith("/login") || requestURI.endsWith("/sign-up") || requestURI.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HEADER_NAME);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Validate the token
            if (!JwtTokenUtil.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                System.out.println("Token is invalid");
                return;
            }

            // Retrieve the email and role from the token
            String roleFromToken = JwtTokenUtil.getRoleFromToken(token);
            System.out.println(roleFromToken);

            // Deny access to specific routes based on the role
            if ("admin".equalsIgnoreCase(roleFromToken)) {
                if (requestURI.contains("/create-event") ||
                    requestURI.contains("/update-event") ||
                    requestURI.contains("/create-booking")) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                    return;
                }
            } else if ("planner".equalsIgnoreCase(roleFromToken)) {
                if (requestURI.contains("/update-venue") ||
                    requestURI.contains("/create-venue") ||
                    requestURI.contains("/create-booking")) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                    return;
                }
            } else if ("customer".equalsIgnoreCase(roleFromToken)) {
                if (requestURI.contains("/update-venue") ||
                    requestURI.contains("/create-venue") ||
                    requestURI.contains("/create-event") ||
                    requestURI.contains("/update-event") ) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                    return;
                }
            }
        } else {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
            return;
        }
        filterChain.doFilter(request, response);
        }


    }

