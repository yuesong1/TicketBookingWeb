package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.util.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.unimelb.swen90007.reactexampleapi.api.objects.UserAccess.customer;

public class UserMapper extends AbstractMapper {
    static PreparedStatement statement;

    // SQL scripts
    public static final String PLANNER_LIST = "planner.token, planner.type, planner.password ";
    private static final String VIEW_USERS = "SELECT * FROM users;";
    private static final String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?;";

    /**
     * Find all users in database.
     * @return
     */
    public List viewAll() {
        return findMany(new ViewAllUsers());
    }
    static class ViewAllUsers implements StatementSource {
        public ViewAllUsers() {
        }
        @Override
        public String sqlQuery() {
            return VIEW_USERS;
        }
        public Object[] parameters() {
            return null;
        }
    }

    // Insertion
    private static final String CREATE_USER =
            "INSERT INTO users (type, email, token, password) " +
                    "VALUES (useraccess(?), ?, ?, ?) RETURNING *;";
    @Override
    protected String insertStatement() {
        return CREATE_USER;
    }
    @Override
    protected void insertKey(DomainObject object, PreparedStatement statement) throws SQLException{
        String email = (String) object.getPrimaryKey().getKey(0);
        statement.setString(2, email);
    }
    @Override
    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        User u = (User) object;
        statement.setString(1, u.getRole().name());
        statement.setString(3, u.getToken());
        statement.setString(4, u.getPassword());
    }

    // Find by primary key
    private static final String FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    @Override
    protected String findStatement() {
        return FIND_USER_BY_EMAIL;
    }
    @Override
    protected void doFind(Key key, PreparedStatement finder) throws SQLException {
        finder.setString(1, (String) key.getKey(0));
    }
    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        User result = null;
//        System.out.println("----------get type" + rs.getString("type"));
        switch (UserAccess.valueOf(rs.getString("type"))) {
            case customer:
                result = new Customer(key);
                break;
            case planner:
                result = new Planner(key);
                break;
            case admin:
                result = new Admin(key);
                break;
        }
        result.setToken(rs.getString("token"));
        result.setPassword(rs.getString("password"));
//        System.out.println("user do load-----------" + key);
//        System.out.println(result);
        result.setPrimaryKey(key);
        return result;
    }
    public static User findUserByToken(String token, Connection conn) throws SQLException {

        ResultSet rs = null;
        User user = new User();

        try {
            statement = conn.prepareStatement(FIND_USER_BY_TOKEN);
            statement.setString(1, token);

            rs = statement.executeQuery();


            if (rs.next()) {
                System.out.println("Reach Here");
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("type"));
                user.setPassword(rs.getString("password"));
                System.out.println(user);

            }
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
        }

        return user;
    }
    // Find by Email

    // Do update
    private static final String UPDATE_USER = "UPDATE users SET password = ?, token = ? WHERE email = ? AND type = useraccess(?);";
    @Override
    protected String updateStatement() {
        return UPDATE_USER;
    }
    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        User u = (User) object;
        statement.setString(1, u.getPassword());
        statement.setString(2, u.getToken());
        statement.setString(3, u.getEmail());
        statement.setString(4, u.getRole().name());

    }

    // Delete User by id
    private static final String DELETE_USER = "DELETE FROM users WHERE email = ?; ";
    @Override
    protected String deleteStatement() {
        return DELETE_USER;
    }

    @Override
    protected void doDelete(Key key, PreparedStatement statement) throws SQLException {
        statement.setString(1, (String) key.getKey(0));
    }


    public static void updateUserToken(String email, String token, Connection conn) {
        PreparedStatement statement = null;
        PreparedStatement insertTokenStatement = null;
        ResultSet resultSet = null;

        try {
            // Check if the token exists in the refresh_token table
            String SELECT_TOKEN = "SELECT id FROM refresh_token WHERE token_id = ? AND username = ?;";
            statement = conn.prepareStatement(SELECT_TOKEN);
            statement.setString(1, token);
            statement.setString(2, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Token exists, update the users table
                String UPDATE_USER_TOKEN = "UPDATE users SET token = ? WHERE email = ?;";
                statement = conn.prepareStatement(UPDATE_USER_TOKEN);
                statement.setObject(1, resultSet.getObject("id")); // Assuming id is a UUID
                statement.setString(2, email);
                statement.executeUpdate();
            } else {
                // Token does not exist, insert it into the refresh_token table
                String INSERT_TOKEN = "INSERT INTO refresh_token (token_id, username) VALUES (?, ?);";
                insertTokenStatement = conn.prepareStatement(INSERT_TOKEN);
                insertTokenStatement.setString(1, token);
                insertTokenStatement.setString(2, email);
                insertTokenStatement.executeUpdate();

                // Get the generated id (assuming id is a UUID)
                resultSet = insertTokenStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    UUID tokenId = (UUID) resultSet.getObject(1);

                    // Update the users table with the new token_id
                    String UPDATE_USER_TOKEN = "UPDATE users SET token = ? WHERE email = ?;";
                    statement = conn.prepareStatement(UPDATE_USER_TOKEN);
                    statement.setObject(1, tokenId);
                    statement.setString(2, email);
                    statement.executeUpdate();
                }
            }

            // Commit the transaction
            conn.commit();
        } catch (SQLException e) {
            // Handle the database error, log the exception, or print a message
            e.printStackTrace();
            try {
                // Rollback the transaction in case of an exception
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (insertTokenStatement != null) {
                    insertTokenStatement.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }


    @Override
    protected Key addKey(ResultSet rs) throws SQLException {
//        System.out.println(rs.getString("email"));
        Object[] key = {rs.getString("email")};
        return new Key(key);
    }

}
