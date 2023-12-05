package com.unimelb.swen90007.reactexampleapi.api.domain;

import com.unimelb.swen90007.reactexampleapi.api.mappers.BookingMapper;
import com.unimelb.swen90007.reactexampleapi.api.mappers.EventMapper;
import com.unimelb.swen90007.reactexampleapi.api.mappers.EventSectionMapper;
import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.objects.Booking;
import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
import com.unimelb.swen90007.reactexampleapi.api.objects.EventSection;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;

import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.unimelb.swen90007.reactexampleapi.api.util.DomainUtil.parseParams;

public class BookingLogic {
    static EventSectionMapper esMapper = MapperRegistry.getInstance().getMapper(EventSection.class);
    static BookingMapper mapper = MapperRegistry.getInstance().getMapper(Booking.class);
    public Booking createBookings(HttpServletRequest request) throws Exception {

        Booking b;

        try {
            UnitOfWork.newCurrent();
            b = bookingInstant(request);

            Object[] keys = {b.getEvent().getId(), b.getSection().getId()};
            EventSection es = (EventSection) esMapper.find(new Key(keys));

            // Check the version number.
            int incomingVersion = b.getVersion(); // Assuming your bookingInstant method sets the version on the Booking object.
            if (es.getVersion() != incomingVersion) {
                DBUtil.getConnection().rollback();
                throw new OptimisticLockingException("The data has been modified by another user. Please refresh and try again.");
            }

            if (es.getAvailability() <= 0) throw new RuntimeException("No available tickets left");
            es.setAvailability(es.getAvailability() - b.getNumTickets());
            int affectedRows = esMapper.updateWithVersionCheck(es, DBUtil.getConnection());  // This method should handle the version-based update

            // Check if the update was successful
            if (affectedRows == 0) {
                DBUtil.getConnection().rollback();
                throw new OptimisticLockingException("The data has been modified by another user. Please refresh and try again.");
            }
            b.markNew();

            UnitOfWork.getCurrent().commit();
        } catch (Exception err) {
            throw err;
        }

        return b;
    }


    public List<Booking> viewBookingsByEvent(String eventID) throws Exception {
        List<Booking> results;
        try {
            results = mapper.findByEvent(UUID.fromString(eventID));
        } catch(Exception e) {
            throw e;
        }
        return results;
    }

    public List<Booking> viewBookingsByCustomer(String customerEmail) throws Exception {
        List<Booking> results;
        try {
            results = mapper.findByCustomer(customerEmail);
        } catch(Exception e) {
            throw e;
        }
        return results;
    }

    public static Booking bookingInstant(HttpServletRequest request) throws Exception {

        Map<String, String> reqParams = parseParams(request);
        Booking b = new Booking();
        Event e = new Event();
        Section s = new Section();

        e.setId(UUID.fromString(reqParams.get("eventid")));
        b.setEvent(e);
        s.setId(UUID.fromString(reqParams.get("sectionid")));
        b.setSection(s);
        b.setCustomerEmail(reqParams.get("customeremail"));
        b.setNumTickets(Integer.parseInt(reqParams.get("numtickets")));
        b.setVersion(Integer.valueOf(reqParams.get("version")));


        return b;
    }

    public static void deleteBooking(String idStr) throws Exception {
        try {
            UnitOfWork.newCurrent();
            Key bId = new Key(UUID.fromString(idStr));
            Booking b = (Booking) mapper.find(bId);
            System.out.println(b);
            //Send the input data to Mapper, so it will be stored in database.
            //call the Booking Mapper to store the booked data.
            if(b == null) throw new RuntimeException("Booking does not exist");
            b.markRemoved();
//            BookingMapper.deleteBooking(UUID.fromString(idStr), conn);
//            conn.commit();

            Object[] keys = {b.getEvent().getId(), b.getSection().getId()};
            EventSection es = (EventSection) esMapper.find(new Key(keys));
            es.setAvailability(es.getAvailability() + b.getNumTickets());
            es.markDirty();

            UnitOfWork.getCurrent().commit();
        } catch (Exception e) {
            throw e;
        }
    }
}
