package com.unimelb.swen90007.reactexampleapi.api.mappers;

//import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.EventSectionVH;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.SectionLoader;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.ValueHolder;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.ValueLoader;
import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.EventSection;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventSectionMapper extends AbstractMapper {

    // View All
    private static final String VIEW_EVENT_SECTIONS = "SELECT * FROM eventsection;";

    public List viewAll() {
        return findMany(new ViewAllEventSetions());
    }
    static class ViewAllEventSetions implements StatementSource {
        public ViewAllEventSetions() {
        }
        @Override
        public String sqlQuery() {
            return VIEW_EVENT_SECTIONS;
        }
        public Object[] parameters() {
            return null;
        }
    }
    // View by eventId
    private static final String VIEW_EVENT_SECTIONS_BY_EVENT = "SELECT * FROM eventsection WHERE eventid = uuid(?);";
    public List findByEvent(UUID pattern) {
        return findMany(new FindEventSectionByEvent(pattern));
    }
    static class FindEventSectionByEvent implements StatementSource {
        private UUID eId;
        public FindEventSectionByEvent(UUID eId) {
            this.eId = eId;
        }
        @Override
        public String sqlQuery() {
            return VIEW_EVENT_SECTIONS_BY_EVENT;
        }
        public Object[] parameters() {
            Object[] result = {eId};
            return result;
        }
    }

    // Find
    private static final String VIEW_EVENT_SECTIONS_BY_KEY = "SELECT * FROM eventsection WHERE eventid = uuid(?) " +
        "AND sectionid = uuid(?);";



    @Override
    protected String findStatement() {
        return VIEW_EVENT_SECTIONS_BY_KEY;
    }

    @Override
    protected void doFind(Key key, PreparedStatement finder) throws SQLException {
        System.out.println("do find ------- " + key.getKey(0).toString() + "---" + key.getKey(1).toString());
        finder.setString(1, key.getKey(0).toString());
        finder.setString(2, key.getKey(1).toString());
    }

    protected Key addKey(ResultSet rs) throws SQLException{
        Object[] keys = {rs.getObject("eventid", UUID.class),
                rs.getObject("sectionid", UUID.class)};
        return new Key(keys);
    }

    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        Double price = rs.getDouble("price");
        int available = rs.getInt("available");
        int version = rs.getInt("version");
        EventSection result = new EventSection(price, available, key, version);

        // Load the Section
        result.setSection(new ValueHolder(new SectionLoader((UUID) key.getKey(1))));
        System.out.println("---check load " + key.getKey(1));
        return result;
    }

    // Insert
    private static final String CREATE_EVENT_SECTION =
            "INSERT INTO eventsection (eventid, sectionid, price, available, version) " +
                    "VALUES (uuid(?), uuid(?), ?, ?, ?)" +
                    "RETURNING *;";

    private static final String VIEW_EVENT_SECTIONS_BY_KEY_VERSION = VIEW_EVENT_SECTIONS_BY_KEY + " AND version = ?;";
    private static final String UPDATE_EVENT_SECTION_VERSION = "UPDATE eventsection SET price = ?, available = ?, version = version + 1 WHERE eventid = uuid(?) AND sectionid = uuid(?) AND version = ?;";

    @Override
    protected String insertStatement() {
        return CREATE_EVENT_SECTION;
    }

    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        EventSection es = (EventSection) object;
        statement.setDouble(3, es.getPrice());
        statement.setInt(4, es.getAvailability());
        statement.setInt(5, 1);  // Initializing version to 1 for new entries.
    }

    @Override
    protected void insertKey(DomainObject object, PreparedStatement statement) throws SQLException {
        UUID eventId = (UUID) object.getPrimaryKey().getKey(0);
        UUID sectionId = (UUID) object.getPrimaryKey().getKey(1);
        statement.setString(1, eventId.toString());
        statement.setString(2, sectionId.toString());
    }

    // Update
    private static final String UPDATE_EVENT_SECTION = "UPDATE eventsection SET price = ?, available = ? WHERE " +
            "eventid = uuid(?) AND sectionid = uuid(?);";

    @Override
    protected String updateStatement() {
        return UPDATE_EVENT_SECTION_VERSION; // Adjusted to include version checking.
    }
    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        EventSection es = (EventSection) object;
        statement.setDouble(1, es.getPrice());
        statement.setInt(2, es.getAvailability());
        statement.setString(3, es.getEventID().toString());
        statement.setString(4, es.getSectionID().toString());
        statement.setInt(5, es.getVersion());

    }


    // Delete
    private static final String DELETE_EVENT_SECTION_BY_KEY = "DELETE FROM eventsection WHERE eventid = uuid(?) " +
            "AND sectionid = uuid(?); ";

    @Override
    protected void doDelete(Key key, PreparedStatement statement) throws SQLException {
        System.out.println("----------------do delete " +  key.getKey(0) +  key.getKey(1));
        statement.setString(1, key.getKey(0).toString());
        statement.setString(2, key.getKey(1).toString());
    }

    public int updateWithVersionCheck(EventSection es, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(updateStatement());

        doUpdate(es, statement);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
//            DBUtil.getConnection().rollback();
            throw new OptimisticLockingException("The data has been modified by another user. Please refresh and try again.");

        }

        return affectedRows;
    }

    @Override
    protected String deleteStatement() {
        return DELETE_EVENT_SECTION_BY_KEY;
    }

}
