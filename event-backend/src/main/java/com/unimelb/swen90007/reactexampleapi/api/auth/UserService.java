package com.unimelb.swen90007.reactexampleapi.api.auth;

import com.unimelb.swen90007.reactexampleapi.api.domain.UserLogic;
import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.mappers.UserMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.User;
import com.unimelb.swen90007.reactexampleapi.api.objects.UserAccess;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class UserService {
//    UserMapper mapper = MapperRegistry.getInstance().getMapper(User.class);

    public User findUserByEmailAndRole(String email, String role) {
//        try (Connection conn = DBUtil.connection()) {
//            return (User) MapperRegistry.getInstance().getMapper(User.class).find(new Key(new String[]{email}));
////                    UserMapper.viewOneByEmail(email, UserAccess.valueOf(role), conn);
//        } catch (Exception e) {
//            // Handle exception (ideally, log it and return a meaningful error)
//            return null;
//        }
        return (User) MapperRegistry.getInstance().getMapper(User.class).find(new Key(new String[]{email}));
    }

    public void updateUserToken(String userId, String token) {
        try (Connection conn = DBUtil.connection()) {
            UserMapper.updateUserToken(userId, token, conn);
        } catch (Exception e) {
            // Handle exception (ideally, log it and return a meaningful error)
        }
    }

    public User findUserByToken(String token) {
        try (Connection conn = DBUtil.connection()) {
            return UserMapper.findUserByToken(token, conn);
        } catch (Exception e) {
            // Handle exception (ideally, log it and return a meaningful error)
            return null;
        }
    }

}
