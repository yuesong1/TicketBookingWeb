package com.unimelb.swen90007.reactexampleapi.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.api.mappers.*;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.SectionLoader;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.ValueHolder;
import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.ValueLoader;
import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.OptimisticLockingException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.DomainUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.PlatformManagedObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.unimelb.swen90007.reactexampleapi.api.util.DBUtil.connection;

public class EventLogic {


    static EventMapper mapper = MapperRegistry.getInstance().getMapper(Event.class);
    static EventSectionMapper esMapper = MapperRegistry.getInstance().getMapper(EventSection.class);

    static BookingMapper bkMapper = MapperRegistry.getInstance().getMapper(Booking.class);

    public Event createEvent(HttpServletRequest request) {
//        Connection conn = null;
        Event requestedE;
//        UUID eID;
        List<EventSection> eSections;
        List<String> plannerEmails;

        try {
            UnitOfWork.newCurrent();
            requestedE = eventInstant(request);
            eSections = requestedE.getEventSections();

            if (!checkVenueAvail(requestedE.getVenue(), requestedE.getStartDateTime(), requestedE.getEndDateTime())) {
                throw new Exception("venue occupied at this time");
            }
            requestedE.markNew();
            UnitOfWork.getCurrent().commit();
            // Update venue version
            VenueMapper vm = MapperRegistry.getInstance().getMapper(Venue.class);
            vm.updateVersion(requestedE.getVenue().getId(), requestedE.getVenue().getVersion());

            addPlanner(requestedE, true);
            addEventSections(requestedE, eSections);

        } catch (Exception err) {
            err.printStackTrace();
            throw new RuntimeException();

        }

        return requestedE;
    }
    private void addEventSections(Event requestedE, List<EventSection> eSections) throws SQLException {
        UnitOfWork.newCurrent();
        UUID eID = requestedE.getPrimaryKey().getId();
        for (EventSection es: eSections){
            es.setEventID(eID);
            es.markNew();
        }
        UnitOfWork.getCurrent().commit();
    }

    private void addPlanner(Event requestedE, boolean create) throws SQLException{
//        Connection conn = connection();
        List<Planner> planners = new ArrayList<>();
        for(String plannerEmail: requestedE.getPlannerEmails()) {
            System.out.println(plannerEmail);
            Planner p = new Planner(plannerEmail);
            p = (Planner) MapperRegistry.getInstance().getMapper(User.class).find(p.getPrimaryKey());
            System.out.println(p);
            planners.add(p);
        }
        requestedE.setPlanners(planners);
        if(create) {
            mapper.createPlannerEvents(planners, requestedE.getPrimaryKey());
        }else{
//            mapper.deletePlannerEventsByEvent(requestedE.getPrimaryKey());
//            mapper.createPlannerEvents(planners, requestedE.getPrimaryKey());
        }
    }

    private boolean checkVenueAvail(Venue venue, LocalDateTime startDateTime, LocalDateTime endDateTime) throws Exception {
        // Check if the venue's version has changed
        Connection conn = DBUtil.getConnection(); // Assuming you've a way to get a connection. Replace this.
        PreparedStatement pstmt = conn.prepareStatement("SELECT version FROM venues WHERE id = uuid(?)");
        System.out.println("..checkVenueAvail...." + venue.getId().toString());
        pstmt.setString(1, venue.getId().toString());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next() && rs.getInt("version") != venue.getVersion()) {
            throw new OptimisticLockingException("Venue version mismatch. Another operation may have booked the venue.");
        }

        // Check venue availability based on events
        List<Event> vEvents = mapper.findByVenue(venue.getId());
        for (Event e: vEvents) {
            if (e.getStartDateTime().equals(startDateTime) || e.getEndDateTime().equals(endDateTime)) return false;
            if (e.getStartDateTime().isBefore(startDateTime) && e.getEndDateTime().isAfter(startDateTime)) return false;
            if (e.getStartDateTime().isBefore(endDateTime) && e.getEndDateTime().isAfter(endDateTime)) return false;
            if (e.getStartDateTime().isAfter(startDateTime) && e.getEndDateTime().isBefore(endDateTime)) return false;
        }
        return true;
    }

    private boolean checkUpdateVenueAvail(Venue venue, LocalDateTime startDateTime, LocalDateTime endDateTime, String eventID) throws Exception {
        // Check if the venue's version has changed
        Connection conn = DBUtil.getConnection(); // Assuming you've a way to get a connection. Replace this.
        PreparedStatement pstmt = conn.prepareStatement("SELECT version FROM venues WHERE id = uuid(?)");
        System.out.println("..checkVenueAvail...." + venue.getId().toString());
        pstmt.setString(1, venue.getId().toString());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next() && rs.getInt("version") != venue.getVersion()) {
            throw new OptimisticLockingException("Venue version mismatch. Another operation may have booked the venue.");
        }

        // Check venue availability based on events
        List<Event> vEvents = mapper.findByVenue(venue.getId());
        for (Event e: vEvents) {
            if(!e.getId().toString().equals(eventID)) {
                if (e.getStartDateTime().equals(startDateTime) || e.getEndDateTime().equals(endDateTime)) return false;
                if (e.getStartDateTime().isBefore(startDateTime) && e.getEndDateTime().isAfter(startDateTime)) return false;
                if (e.getStartDateTime().isBefore(endDateTime) && e.getEndDateTime().isAfter(endDateTime)) return false;
                if (e.getStartDateTime().isAfter(startDateTime) && e.getEndDateTime().isBefore(endDateTime)) return false;
            }
        }
        return true;
    }


    public Event updateEvent(HttpServletRequest request) throws Exception {

        UnitOfWork.newCurrent();
        Event e;
        UUID eID;
        List<EventSection> eSections, originalES;


            e = eventInstant(request);
            eID = e.getId();

            // Check for bookings
            if (bkMapper.hasBookingsForEvent(eID)) {
                throw new Exception("Cannot update the event as there are existing bookings associated with it.");
            }
            if (!checkUpdateVenueAvail(e.getVenue(), e.getStartDateTime(), e.getEndDateTime(),e.getId().toString())) {
                throw new Exception("venue occupied at this time");
            }

            e.markDirty();
            eSections = e.getEventSections();
            addPlanner(e, false);
//            originalES = esMapper.findByEvent(eID);
//            compareESections(eSections, originalES);
            for (EventSection es : eSections) {
                es.markDirty();
            }
            UnitOfWork.getCurrent().commit();
            VenueMapper vm = MapperRegistry.getInstance().getMapper(Venue.class);
            vm.updateVersion(e.getVenue().getId(), e.getVenue().getVersion());

            mapper.deletePlannerEventsByEvent(e.getPrimaryKey());
            mapper.createPlannerEvents(e.getPlanners(), e.getPrimaryKey());


        return e;
    }

    public static Event deleteEvent(String idStr) throws Exception {
        Event e = null;
        UUID eID;

        try {
            UnitOfWork.newCurrent();
            eID = UUID.fromString(idStr);
            Key eventId = new Key(eID);

            // Check for bookings
            if (bkMapper.hasBookingsForEvent(eID)) {
                throw new Exception("Cannot delete the event as there are existing bookings associated with it.");
            }

            e = mapper.find(eventId);
            e.markRemoved();
            UnitOfWork.getCurrent().commit();
        } catch (Exception err) {
            err.printStackTrace();
            throw err;
        }

        return e;
    }


    // get one event
    public static Event viewOne(String idStr) throws Exception {
        Event e;
        try {
            UUID eId = UUID.fromString(idStr);
            Key key = new Key(eId);
            e = (Event) mapper.find(key);
            System.out.println("event view one==== " + e);
            e.addEventSections(esMapper.findByEvent(eId));
        } catch (Exception err) {
            throw err;
        }

        return e;
    }

    // get all events
    public static List<Event> viewAll() throws Exception {
        Connection conn = null;
        List<Event> events;
        List<EventSection> eSections;

        try {

            events = mapper.viewAll();
            eSections = esMapper.viewAll();

            for (EventSection es: eSections) {
                for (Event ev : events) {
                    // if (ev.getPrimaryKey().equals(es.getEventID())) ev.addEventSection(es);
                    if (ev.getPrimaryKey().getId().equals(es.getEventID())) ev.addEventSection(es); // corrected logic
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return events;
    }

    // get the next 6 months of events
    public static List<Event> view6Month() throws Exception {
        List<Event> allEvents = viewAll(), results = new ArrayList<>();
        LocalDate now = LocalDate.now(), sixMonths = now.plusMonths(6);

        for (Event e : allEvents) {
            if (e.getStartDateTime().toLocalDate().isAfter(now) &&
                e.getStartDateTime().toLocalDate().isBefore(sixMonths)) results.add(e);
        }

        return results;
    }

    // get the next 6 months of events
    public static List<Event> viewSearch(String input) throws Exception {
        List<Event> allEvents = viewAll(), results = new ArrayList<>();

        for (Event e : allEvents) {
            if (e.getName().contains(input)) results.add(e);
        }

        return results;
    }

    public static Event eventInstant(HttpServletRequest request) throws Exception {
        Event e;
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder reqSB = new StringBuilder();

        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) reqSB.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> objParams = mapper.readValue(reqSB.toString(), typeRef);

        try {
            String name = (String) objParams.get("name");
            String artist = (String) objParams.get("artist");
            String des = (String) objParams.get("description");
            LocalDateTime startdate = LocalDateTime.parse((String) objParams.get("startdate"), dtFormatter);
            LocalDateTime enddate = LocalDateTime.parse((String) objParams.get("enddate"), dtFormatter);
            int version = (int) objParams.get("version");

            String idStr = (String) objParams.get("id");
            if (idStr != null) {
                e = new Event();
                e.setId(UUID.fromString(idStr));
                e.setName(name);
                e.setArtist(artist);

                e.setDescription(des);
                e.setStartDateTime(startdate);
                e.setEndDateTime(enddate);
                e.setVersion(version);
                e.markDirty();
            }else{
                UUID id = UUID.randomUUID();
                e = new Event(id, name, null, startdate, enddate, artist, des, version);
            }

            e.addPlannerEmails((ArrayList) objParams.get("planners"));
            e = parseVenue(e, objParams.get("venue"));
        } catch (Exception err) {
            err.printStackTrace();
            throw err;
        }

        return e;
    }

    public static Event parseVenue(Event e, Object venueObj) throws IOException {
        Map<String, Object> objParams = new HashMap<>();
        LinkedHashMap objMap = (LinkedHashMap) venueObj;
        for (Object oKey: objMap.keySet()) {
            objParams.put((String) oKey, objMap.get(oKey));
        }

        System.out.println("objParams.keySet()");
        System.out.println(objParams.keySet());

        Venue v = new Venue();
        v.setAddress((String) objParams.get("address"));
        v.setName((String) objParams.get("name"));
        v.setId(UUID.fromString((String) objParams.get("id")));
        v.setVersion(Integer.valueOf((String) objParams.get("version")));
        e.setVenue(v);

        List<EventSection> eSections = parseSection(objParams.get("sections"), e.getId());
        e.addEventSections(eSections);

        return e;
    }

    public static List<EventSection> parseSection(Object secObj, UUID eID) throws JsonProcessingException {
        List<EventSection> es = new ArrayList<>();
        EventSection newES;
        for (Object eObj: (ArrayList) secObj) {
            Map<String, Object> objParams = new HashMap<>();
            LinkedHashMap objMap = (LinkedHashMap) eObj;
            for (Object oKey: objMap.keySet()) {
                objParams.put((String) oKey, objMap.get(oKey));
            }
            System.out.println(objParams.keySet());

//            Create new ES also in database
            double price;
            try {
                price = (Double) objParams.get("price");
            } catch (Exception e) {
                Object priceObj = objParams.get("price");
                if (priceObj instanceof Integer) {
                    price = Double.valueOf((Integer) priceObj);
                } else if (priceObj instanceof String) {
                    price = Double.valueOf((String) priceObj);
                } else {
                    throw new RuntimeException("Input not valid");
                }
            }
            int ava = (Integer) objParams.get("capacity");
            UUID sId = UUID.fromString((String) objParams.get("id"));

            Integer verObj = (Integer) objParams.get("version");
            int ver;
            if (verObj != null) {
                ver = verObj.intValue();
            } else {
                // Handle the case where version is not provided.
                // You could set a default value, or throw an exception, etc.
                ver = 0; // Setting default version value as 1, adjust as needed.
            }

            newES = new EventSection(price, ava, new Key(new UUID[]{eID, sId}), ver);
            newES.setSection(new ValueHolder((new SectionLoader(sId))));
            es.add(newES);
        }

        return es;
    }

    public  List<Event> viewByPlanner(String emailStr) throws Exception {
        Connection conn = null;
        List<Event> results = new ArrayList<>();
        Map<UUID, Event> allEvents = new HashMap<>();
        List<Object[]> ePlanners;

        try {
            conn = connection();
            ePlanners = EventMapper.viewAllPlannerEvents(conn);
            List<Event> events = mapper.viewAll();
            for(Event e: events){
                allEvents.put(e.getPrimaryKey().getId(), e);
                System.out.println(e);
            }

            for (Object[] pe : ePlanners) {
                List<EventSection> eSections;
                UUID eventID = (UUID) pe[0];
                String pEmail = (String) pe[1];
                if (pEmail.equals(emailStr)) {
                    Event e = allEvents.get(eventID);
                    eSections = esMapper.findByEvent(eventID);
                    e.addEventSections(eSections);
                    e.addPlannerEmail(emailStr);
                    results.add(e);
                };
            }
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }

        return results;
    }
    public static void compareESections(List<EventSection> requested, List<EventSection> original) {
        List<EventSection>[] sectDeleteModifyAdd =
                new ArrayList[]{new ArrayList<EventSection>(), new ArrayList<EventSection>(), new ArrayList<EventSection>()};
        Map<String, EventSection> originalMap = new HashMap<>();
        for (EventSection t: original) originalMap.put(t.getEventID().toString() + t.getSectionID().toString(), t);
        for (EventSection t: requested) {
            if (t.getEventID() == null || t.getSectionID() == null) {
                sectDeleteModifyAdd[2].add(t);
                t.markNew();
            } else if (!originalMap.containsKey(t.getEventID().toString() + t.getSectionID().toString())) {
                sectDeleteModifyAdd[2].add(t);
                t.markNew();
            }
            else {
                originalMap.remove(new String(t.getEventID().toString() + t.getSectionID().toString()));
                t.markDirty();
                sectDeleteModifyAdd[1].add(t);
            }
        }
        for (EventSection t: originalMap.values()) {
            sectDeleteModifyAdd[0].add(t);
            t.markRemoved();
        }
    }
}


