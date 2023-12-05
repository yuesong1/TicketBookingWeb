package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.EventSection;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SectionMapper extends AbstractMapper {

    Connection conn = DBUtil.connection();
    // insert
    private static final String CREATE_SECTION =
            "INSERT INTO sections (venueid, type, capacity) " +
                    "VALUES (uuid(?), ?, ?) " +
                    "RETURNING *;";
    @Override
    protected String insertStatement() {
        return CREATE_SECTION;
    }
    @Override
    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        Section s = (Section) object;
        statement.setString(1, s.getVenueID().toString());
        statement.setString(2, s.getType());
        statement.setInt(3, s.getCapacity());
    }

    // Find
    public List viewAll() {
        return findMany(new ViewAllSetion());
    }
    static class ViewAllSetion implements StatementSource {
        public ViewAllSetion() {
        }
        @Override
        public String sqlQuery() {
            return "SELECT * FROM sections;";
        }
        public Object[] parameters() {
            return null;
        }
    }

    private static final String VIEW_SECTION_BY_VENUE = "SELECT * FROM sections WHERE venueID = uuid(?);";
    public List findByVenue(UUID pattern) {
        return findMany(new findByVenue(pattern));
    }
    static class findByVenue implements StatementSource {
        private UUID vId;
        public findByVenue(UUID vId) {
            this.vId = vId;
        }
        @Override
        public String sqlQuery() {
            return VIEW_SECTION_BY_VENUE;
        }
        public Object[] parameters() {
            Object[] result = {vId};
            return result;
        }
    }



    private static final String DELETE_SECTION_BY_VENUE = "DELETE FROM sections WHERE venueID = uuid(?) RETURNING *; ";
    private static final String DELETE_SECTION_BY_ID = "DELETE FROM sections WHERE id = uuid(?);";

    public static List<Section> deleteSectionsByVenue(UUID venueId, Connection conn) throws SQLException {
        List<Section> sections = new ArrayList<>();
        PreparedStatement sectionStatement = null;

        try {
            sectionStatement = conn.prepareStatement(DELETE_SECTION_BY_VENUE);
            sectionStatement.setString(1, venueId.toString());
            ResultSet rs = sectionStatement.executeQuery();

            while (rs.next()) {
                Section s = new Section();
                s.setId(rs.getObject("id", UUID.class));
                s.setVenueID(rs.getObject("venueid", UUID.class));
                s.setType(rs.getString("type"));
                s.setCapacity(rs.getInt("capacity"));
                sections.add(s);
            }
        } finally {
            sectionStatement.close();
        }

        return sections;
    }

    // Update
    private static final String UPDATE_SECTION = "UPDATE sections SET type = ?, capacity = ? WHERE id = uuid(?);";
    @Override
    protected String updateStatement() {
        return UPDATE_SECTION;
    }

    @Override
    protected String deleteStatement() {
        return DELETE_SECTION_BY_ID;
    }

    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        Section s = (Section) object;
        statement.setString(1, s.getType());
        statement.setInt(2, s.getCapacity());
        statement.setString(3, s.getId().toString());
    }

    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        Section s = new Section();
        s.setVenueID((UUID) rs.getObject("venueID"));
        s.setType(rs.getString("type"));
        s.setCapacity(rs.getInt("capacity"));
        s.setPrimaryKey(key);
        return s;
    }

    private static final String VIEW_SECTION_BY_ID = "SELECT * FROM sections WHERE id = uuid(?);";

    @Override
    protected String findStatement() {
        return VIEW_SECTION_BY_ID;
    }

    @Override
    protected void doFind(Key key, PreparedStatement finder) throws SQLException {
        super.doFind(key, finder);
    }

}
