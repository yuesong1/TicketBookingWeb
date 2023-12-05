package com.unimelb.swen90007.reactexampleapi.api.domain;

import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.mappers.UserMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DuplicateFieldException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.unimelb.swen90007.reactexampleapi.api.util.DomainUtil.parseParams;
import static com.unimelb.swen90007.reactexampleapi.api.util.DBUtil.connection;

public class UserLogic {
    static UserMapper userMapper = MapperRegistry.getInstance().getMapper(User.class);

    public static User signUp(HttpServletRequest request) throws Exception {
        User u = new User();
        try {
            UnitOfWork.newCurrent();
            u = userInstant(request);
            u.markNew();
            if (u.getRole() == UserAccess.admin) throw new Exception();

            UnitOfWork.getCurrent().commit();
        } catch (DuplicateFieldException e) {
            // Add a log statement or print the exception message for debugging.
            System.out.println("Duplicate email & role combination exists: " + e.getMessage());
            // You can also re-throw the exception here if needed.
            throw e;
        } catch (Exception e) {
            // Add a log statement or print the exception message for debugging.
            System.out.println("Invalid input: " + e.getMessage());
            // You can also re-throw the exception here if needed.
            throw e;
        }
        return u;
    }

    public static ArrayList[] viewAllUsers() throws Exception {
        List<User> users;
        ArrayList[] adminCustomersPlanners = new ArrayList[3];
        adminCustomersPlanners[0] = new ArrayList<Admin>();
        adminCustomersPlanners[1] = new ArrayList<Customer>();
        adminCustomersPlanners[2] = new ArrayList<Planner>();
        try {
            users = userMapper.viewAll();
            for (User u: users) {
                switch (u.getRole()) {
                    case admin:
                        adminCustomersPlanners[0].add(u);
                        break;
                    case customer:
                        adminCustomersPlanners[1].add(u);
                        break;
                    case planner:
                        adminCustomersPlanners[2].add(u);
                        break;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return adminCustomersPlanners;
    }

    public static User userInstant(HttpServletRequest request) throws Exception {
        Map<String, String> reqParams = parseParams(request);
        Key key = new Key(new String[]{reqParams.get("email")});
        User u = new User(key, UserAccess.valueOf(reqParams.get("type")), reqParams.get("password"));

        return u;
    }

    public static User viewOneUser(String email, String typeStr) throws Exception {
        User u;
        try {
            Object[] key = {email};
            u = (User) userMapper.find(new Key(key));
        } catch (Exception e) {
            throw e;
        }
        return u;
    }
}
