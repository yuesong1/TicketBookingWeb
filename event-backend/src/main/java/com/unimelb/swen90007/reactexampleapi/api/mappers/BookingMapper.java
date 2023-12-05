package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.StatementSource;
import org.springframework.security.access.method.P;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingMapper extends AbstractMapper {


    // SQL scripts
    private static final String FIND_BOOKING = "SELECT * FROM bookings WHERE id = uuid(?);";
    private static final String FIND_BOOKING_BY_CUSTOMER = "SELECT * FROM bookings WHERE customeremail = ?;";
    private static final String FIND_BOOKING_BY_EVENT = "SELECT * FROM bookings WHERE eventid = uuid(?);";
    private static final String DELETE_BOOKING_BY_EVENT = "DELETE FROM bookings WHERE eventid = uuid(?);";


    @Override
    protected DomainObject doLoad(Key key, ResultSet rs) throws SQLException {
        String cus = rs.getString("customeremail");
        int numTickets = rs.getInt("numtickets");

        // Use Single-Valued Reference for foreign key mapping
        Key sId = new Key(rs.getObject("sectionid", UUID.class));
        Key eId = new Key(rs.getObject("eventid", UUID.class));
        Section s = (Section) MapperRegistry.getInstance().getMapper(Section.class).find(sId);
        Event e = (Event) MapperRegistry.getInstance().getMapper(Event.class).find(eId);

        Booking result = new Booking(s, numTickets, cus, e);
        result.setPrimaryKey(key);
        return result;
    }

    public List findByCustomer(String pattern) {
        return findMany(new BookingMapper.FindBookingsByCustomer(pattern));
    }
    static class FindBookingsByCustomer implements StatementSource {
        private String customerEmail;
        public FindBookingsByCustomer(String customerEmail) {
            this.customerEmail = customerEmail;
        }
        @Override
        public String sqlQuery() {
            return FIND_BOOKING_BY_CUSTOMER;
        }
        public Object[] parameters() {
            Object[] result = {customerEmail};
            return result;
        }
    }

    public List findByEvent(UUID id) {
        return findMany(new BookingMapper.FindBookingsByEvent(id));
    }
    static class FindBookingsByEvent implements StatementSource {
        private UUID eventId;
        public FindBookingsByEvent(UUID eventId) {
            this.eventId = eventId;
        }
        @Override
        public String sqlQuery() {
            return FIND_BOOKING_BY_EVENT;
        }
        public Object[] parameters() {
            Object[] result = {eventId};
            return result;
        }
    }

    public boolean hasBookingsForEvent(UUID eventId) throws SQLException {
        String query = "SELECT COUNT(*) FROM bookings WHERE eventid = uuid(?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, eventId.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Returns true if there are bookings.
            }
        }
        return false; // Default to false if there's an issue.
    }



    private static final String CREATE_BOOKING =
            "INSERT INTO bookings (customeremail, eventid, sectionid, numTickets) " +
                    "VALUES (?, uuid(?), uuid(?), ?) RETURNING *;";
    @Override
    protected void insertData(DomainObject object, PreparedStatement statement) throws SQLException {
        Booking b = (Booking) object;
        statement.setString(1, b.getCustomerEmail());
        statement.setString(2, b.getEvent().getId().toString());
        statement.setString(3, b.getSection().getId().toString());
        statement.setInt(4, b.getNumTickets());
    }

    private static final String UPDATE_BOOKINGS = "UPDATE bookings SET customeremail = ?, eventid = uuid(?), " +
            "sectionid = uuid(?), numTickets = ? WHERE id = uuid(?);";
    @Override
    protected void doUpdate(DomainObject object, PreparedStatement statement) throws SQLException {
        Booking b = (Booking) object;

        statement.setString(1, b.getCustomerEmail());
        statement.setString(2, b.getEvent().getId().toString());
        statement.setString(3, b.getSection().getId().toString());
        statement.setInt(4, b.getNumTickets());
        statement.setString(5, b.getPrimaryKey().getId().toString());
    }

    private static final String DELETE_BOOKING = "DELETE FROM bookings WHERE id = uuid(?);";

    @Override
    protected String findStatement() {
        return FIND_BOOKING;
    }

    @Override
    protected String insertStatement() {
        return CREATE_BOOKING;
    }

    @Override
    protected String updateStatement() {
        return UPDATE_BOOKINGS;
    }

    @Override
    protected String deleteStatement() {
        return DELETE_BOOKING;
    }
}
