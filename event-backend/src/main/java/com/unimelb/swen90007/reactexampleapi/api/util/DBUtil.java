package com.unimelb.swen90007.reactexampleapi.api.util;

import com.unimelb.swen90007.reactexampleapi.api.mappers.VenueMapper;
import io.jsonwebtoken.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/* Miscellaneous methods for reusing throughout app. currently contains:
 * connection(): returns a connection to the db
 * readSQL(): given a string of file location within src/main/resources folder, output file text as String
 * initTables(): initialises database.
*/
/*
    ThreadLocalRegistry
 */
public class DBUtil {
    private static ThreadLocal<Connection> conn = ThreadLocal.withInitial(() -> {
        // Initialize a connection for each thread
        return connection();
    });
    private static final String PROPERTY_JDBC_URI = "jdbc.uri";
    private static final String PROPERTY_JDBC_USERNAME = "jdbc.username";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";

    public static Connection getConnection(){
        return conn.get();
    }

//    public static void begin() {
//        Assert.isTrue(conn.get() == null);
//        conn.set(new DBUtil());
//    }
    public static void closeConnection() {
        Assert.notNull(getConnection());
        Connection connection = conn.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn.remove();
            }
        }
    }

//     make connection to database
//    public static Connection connection() {
//        Connection conn = null;
//        try {
//            DriverManager.registerDriver(new org.postgresql.Driver());
//            conn = DriverManager.getConnection(
//                    System.getProperty(PROPERTY_JDBC_URI),
//                    System.getProperty(PROPERTY_JDBC_USERNAME),
//                    System.getProperty(PROPERTY_JDBC_PASSWORD)
//            );
//
//            if (conn != null){
//                conn.setAutoCommit(false);
//                System.out.println("Connection established!");
//            } else{
//                System.out.println("Connection failed!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }
    public static Connection connection() {
        Connection conn = null;
        try{
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://dpg-cjg9sg337aks73bca9ug-a.singapore-postgres.render.com:5432/musicbandpostgres",
                    "mbpostgres", "ANMFhmDKomW8PotYk3nHCZqWBHApt4j2"
            );
            if(conn != null){
                conn.setAutoCommit(false);
                System.out.println("Connection established!");
            }else{
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    // given a string of file location within src/main/resources folder, output file text
    public static String readSQL(String SQL_LOCATION) throws IOException {
        InputStream sqlStream;
        sqlStream = VenueMapper.class.getResourceAsStream(SQL_LOCATION);
        assert sqlStream != null;
        return new String(sqlStream.readAllBytes());
    }

    // reads init sql file and executes it to initialise database
    public static void initTables() throws IOException, SQLException {

        PreparedStatement initStatement = null;
        Connection conn = null;
        String sql = readSQL("/InitDB.sql");

        try {
            conn = connection();
            initStatement = conn.prepareStatement(sql);
            initStatement.execute();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
            throw(e);
        } finally {
            try {
                if (initStatement != null) initStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("close connection failed");
            }
        }

    }
    public static void cleanUp(PreparedStatement s, ResultSet rs){
        try {
            if(rs != null) rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to release the ResultSet object.");
        }
        try{
            if(s != null) s.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to release the Statement.");
        }
    }

    public static void cleanUp(PreparedStatement s){
        try{
            if(s != null) s.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to release the Statement.");
        }
    }
}
