package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;
import org.springframework.security.access.method.P;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EventMapper extends AbstractMapper {
    static PreparedStatement statement = null;

    // SQL scripts
    private static final String VIEW_ALL_EVENT = "SELECT * FROM events;";
    private static final String CREATE_EVENT =
            "INSERT INTO events (id, name, venueid, startdatetime, enddatetime, artist, description, version) " +
                    "VALUES (uuid(?), ?, uuid(?), ?, ?, ?, ?, ?)" +
                    "RETURNING *;";
    private static final String FIND_EVENT_BY_NAME = "SELECT * FROM events WHERE name = ?;";
    private static final String FIND_EVENT_BY_ID = "SELECT * FROM events WHERE id = uuid(?);";
    private static final String UPDATE_EVENT = "UPDATE events SET name = ?, venueid = uuid(?), startdatetime = ?, enddatetime = ?, artist = ?, description = ?, version = version + 1 WHERE " +
            "id = uuid(?) AND version = ?;";

    private static final String VIEW_PLANNER_BY_EVENT = "SELECT pe.planneremail," + UserMapper.PLANNER_LIST +
            "FROM plannerevent pe, users planner WHERE pe.eventid = uuid(?) AND planner.email = pe.planneremail;";

    private static final String CREATE_EVENT_PLANNER =
            "INSERT INTO plannerevent (eventid, planneremail) " +
                    "VALUES (uuid(?), ?);";

    public List viewAll() {
        return findMany(new ViewAllEvents());
    }
    static class ViewAllEvents implements StatementSource {
        public ViewAllEvents() {
        }
        @Override
        public String sqlQuery() {
            return VIEW_ALL_EVENT;
        }
        public Object[] parameters() {
            return null;
        }
    }

    public List findByName(String pattern) {
        return findMany(new FindEventsByName(pattern));
    }
    static class FindEventsByName implements StatementSource {
        private String name;
        public FindEventsByName(String name) {
            this.name = name;
        }
        @Override
        public String sqlQuery() {
            return FIND_EVENT_BY_NAME;
        }
        public Object[] parameters() {
            Object[] result = {name};
            return result;
        }
    }

    public Event find(Key eID){
        return (Event) super.find(eID);
    }
    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String des = rs.getString("description");
        String artist = rs.getString("artist");
        LocalDateTime startTime = rs.getTimestamp("startdatetime").toLocalDateTime();
        LocalDateTime endTime = rs.getTimestamp("enddatetime").toLocalDateTime();
        int version = rs.getInt("version");



        // Use Single-Valued Reference for foreign key mapping
        Key vId = new Key(rs.getObject("venueid", UUID.class));
        Venue v = (Venue) MapperRegistry.getInstance().getMapper(Venue.class).find(vId);
        Event result = new Event(key.getId(),name,v, startTime, endTime, artist, des, version);

        // Association mapping
        // Load the data in association table.
        result.setPlanners(loadPlanners(key));
        return result;
    }

    // Using direct SQL to map association table.
    protected List loadPlanners(Key key) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            List result = new ArrayList();
            statement = conn.prepareStatement(VIEW_PLANNER_BY_EVENT);
            statement.setString(1, key.getId().toString());

            rs = statement.executeQuery();
            while (rs.next()) {
                Object[] pk = {rs.getString("planneremail")};
                Key planner = new Key(pk);
                Planner p = (Planner) MapperRegistry.getInstance().getMapper(User.class).load(planner, rs);
                result.add(p);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load planner according to the event.");
        } finally {
            DBUtil.cleanUp(statement, rs);
        }
    }

    @Override
    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        Event e = (Event) object;
        statement.setString(2, e.getName());
        statement.setString(3, e.getVenue().getId().toString());
        statement.setTimestamp(4, Timestamp.valueOf(e.getStartDateTime()));
        statement.setTimestamp(5, Timestamp.valueOf(e.getEndDateTime()));
        statement.setString(6, e.getArtist());
        statement.setString(7, e.getDescription());
        statement.setInt(8,e.getVersion());
    }

    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        Event e = (Event) object;

        statement.setString(1, e.getName());
        statement.setString(2, e.getVenue().getId().toString());
        statement.setTimestamp(3, Timestamp.valueOf(e.getStartDateTime()));
        statement.setTimestamp(4, Timestamp.valueOf(e.getEndDateTime()));
        statement.setString(5, e.getArtist());
        statement.setString(6, e.getDescription());
        statement.setString(7, e.getPrimaryKey().getId().toString());
        statement.setInt(8, e.getVersion());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new OptimisticLockingException("The event was modified by someone else. Please reload and try again.");
        }

//        statement.executeUpdate();
    }

    @Override
    protected String findStatement() {
        return FIND_EVENT_BY_ID;
    }

    @Override
    protected String insertStatement() {
        return CREATE_EVENT;
    }

    @Override
    protected String updateStatement() {
        return UPDATE_EVENT;
    }

    @Override
    protected String deleteStatement() {
        return DELETE_EVENT_BY_ID;
    }

    private static final String VIEW_PLANNER_EVENTS = "SELECT * FROM plannerevent;";

    private static final String FIND_EVENT_BY_VENUE = "SELECT * FROM events WHERE venueid = uuid(?);";
    public List findByVenue(UUID vId) {
        return findMany(new FindEventsByVenue(vId));
    }
    static class FindEventsByVenue implements StatementSource {
        private UUID vId;
        public FindEventsByVenue(UUID vId) {
            this.vId = vId;
        }
        @Override
        public String sqlQuery() {
            return FIND_EVENT_BY_VENUE;
        }
        public Object[] parameters() {
            Object[] result = {vId};
            return result;
        }
    }

    private static final String DELETE_EVENT_BY_ID = "DELETE FROM events WHERE id = uuid(?);";
    private static final String CHECK_EVENT_PLANNER = "SELECT * FROM plannerevent WHERE " +
            "eventid = uuid(?) AND planneremail = ?;";
    private static final String DELETE_PLANNER_EVENT_BY_EVENT = "DELETE FROM plannerevent WHERE eventid = uuid(?); ";

    public List<String> viewPlannerByEvent(UUID eID, Connection conn) throws SQLException {
        List<String> plannerEmails = new ArrayList<>();
        statement = conn.prepareStatement(VIEW_PLANNER_BY_EVENT);
        statement.setString(1, eID.toString());

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String email = rs.getString("planneremail");
            plannerEmails.add(email);
        }
        if (statement != null) statement.close();

        return plannerEmails;
    }

    public static boolean checkPlannerEvent(Planner planner, Event event, Connection conn) throws SQLException {
        boolean existence;

        statement = conn.prepareStatement(CHECK_EVENT_PLANNER);
        statement.setString(1, event.getId().toString());
        statement.setString(2, planner.getEmail());

        ResultSet rs = statement.executeQuery();
        existence = rs.next();

        if(statement != null) statement.close();
        return existence;
    }

    /**--------------------------------use!!!
     *
     * @param eventID
     * @throws SQLException
     */
    public void createPlannerEvents(List<Planner> planners, Key eventID) throws SQLException {
//        Connection conn = DBUtil.connection();
        statement = DBUtil.getConnection().prepareStatement(CREATE_EVENT_PLANNER);

        for (Planner p: planners) {
            String email = (String) p.getPrimaryKey().getKey(0);
            statement.setString(1, eventID.getId().toString());
            statement.setString(2, email);
//            statement.addBatch();
            statement.executeUpdate();
            DBUtil.getConnection().commit();
        }
//        statement.executeBatch();

//        conn.commit();

        if (statement != null) statement.close();
    }


    /** Use
     *
     * @param eID
     * @return
     * @throws SQLException
     */
    public void deletePlannerEventsByEvent(Key eID) throws SQLException {
        statement = DBUtil.getConnection().prepareStatement(DELETE_PLANNER_EVENT_BY_EVENT);
        statement.setString(1, eID.getId().toString());

        statement.executeUpdate();
        System.out.println("delete PE");
        DBUtil.getConnection().commit();
        if(statement != null) statement.close();
    }

     public static List<Object[]> viewAllPlannerEvents(Connection conn) throws SQLException {
         List<Object[]> plannerEvents = new ArrayList<>();
         PreparedStatement stmt = null;
         try {
             stmt = conn.prepareStatement(VIEW_PLANNER_EVENTS);

             ResultSet rs = stmt.executeQuery();
             while (rs.next()) {
                 Object[] pe = {UUID.fromString(rs.getString("eventID")), rs.getString("plannerEmail")};
                 plannerEvents.add(pe);
             }
         } finally {
             stmt.close();
         }
         return plannerEvents;
     }


    protected Key addKey(ResultSet rs) throws SQLException{
        return new Key(rs.getObject("id", UUID.class));
    }

}
