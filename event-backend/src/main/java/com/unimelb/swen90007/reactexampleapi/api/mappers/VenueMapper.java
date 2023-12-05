package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;

import java.sql.*;
import java.util.*;

/* Class for venue-related and section-related CRUD functions.
 *
 */
public class VenueMapper extends AbstractMapper {

    // SQL scripts
    private static final String VIEW_VENUE_ALL = "SELECT * FROM venues;";
    private static final String CHECK_VENUE_EXISTENCE = "SELECT * FROM venues WHERE name = ? AND address = ?;";

    // Insert


    // Update Venue
    private static final String UPDATE_VENUE = "UPDATE venues SET name = ?, address = ? WHERE id = uuid(?);";
    @Override
    protected String updateStatement() {
        return UPDATE_VENUE;
    }

    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        Venue v = (Venue) object;
        statement.setString(1, v.getName());
        statement.setString(2, v.getAddress());
        statement.setString(3, v.getPrimaryKey().getId().toString());
    }
    // Delete Venue
    private static final String DELETE_VENUE_BY_ID = "DELETE FROM venues WHERE id = uuid(?);";

    @Override
    protected String deleteStatement() {
        return DELETE_VENUE_BY_ID;
    }

// searching functionality

public List findByName(String pattern) {
    return findMany(new FindVenuesByName(pattern));
}
    static class FindVenuesByName implements StatementSource {
        private String name;
        public FindVenuesByName(String name) {
            this.name = name;
        }
        @Override
        public String sqlQuery() {
            return "SELECT * FROM venues WHERE name = ?;";
        }
        public Object[] parameters() {
            Object[] result = {name};
            return result;
        }
    }

    private static final String FIND_VENUE_BY_ID = "SELECT * FROM venues WHERE id = uuid(?);";

    @Override
    protected String findStatement() {
        return FIND_VENUE_BY_ID;
    }

    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String address = rs.getString("address");
        int version = rs.getInt("version");
        Venue result = new Venue(key, name, address, version);

        return result;
    }
public List viewAll() {
    return findMany(new ViewAllVenues());
}
    static class ViewAllVenues implements StatementSource {
        public ViewAllVenues() {
        }
        @Override
        public String sqlQuery() {
            return VIEW_VENUE_ALL;
        }
        public Object[] parameters() {
            return null;
        }
    }

    /* create venue function. given a venue object, does the following:
     * 1. create venue in database and get its ID
     * 2. create associated sections in database and get their IDs
     * 3. create venue_sections using acquired IDs
     */
    private static final String CREATE_VENUE =
            "INSERT INTO venues (id, name, address, version) " +
                    "VALUES (uuid(?), ?, ?, 0)" +
                    "RETURNING id;";

    @Override
    protected String insertStatement() {
        return CREATE_VENUE;
    }

    @Override
    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        Venue v = (Venue) object;
        statement.setString(2, v.getName());
        statement.setString(3, v.getAddress());
    }

    private static final String UPDATE_VENUE_VERSION = "UPDATE venues SET version = version + 1 WHERE id = uuid(?) AND version = ?;";
    public void updateVersion(UUID venueId, int currentVersion) throws Exception {
        Connection conn = DBUtil.getConnection(); // Get connection here
        PreparedStatement pstmt = conn.prepareStatement(UPDATE_VENUE_VERSION);
        pstmt.setString(1, venueId.toString());
        pstmt.setInt(2, currentVersion);
        int updatedRows = pstmt.executeUpdate();

        if (updatedRows == 0) {
            throw new Exception("Failed to update venue version. Another operation may have booked the venue.");
        }
    }


}
