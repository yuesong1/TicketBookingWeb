package com.unimelb.swen90007.reactexampleapi.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.api.mappers.*;
import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
import com.unimelb.swen90007.reactexampleapi.api.objects.EventSection;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.DoesNotExistException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.InvalidUpdateException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DomainUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static com.unimelb.swen90007.reactexampleapi.api.util.DomainUtil.parseParams;
//import static com.unimelb.swen90007.reactexampleapi.api.util.DBUtil.connection;

public class VenueLogic {
    static VenueMapper venueMapper = MapperRegistry.getInstance().getMapper(Venue.class);
    static SectionMapper sectionMapper = MapperRegistry.getInstance().getMapper(Section.class);

    /**
     * How do we handle delete venue?????
     * @param idStr
     * @return
     * @throws SQLException
     * @throws DoesNotExistException
     */
    public static Venue deleteVenue(String idStr) throws SQLException, DoesNotExistException, InvalidUpdateException {
//        Connection conn = null;
        Venue v;

//        try {
            UnitOfWork.newCurrent();
            Key vID = new Key(UUID.fromString(idStr));
//            conn = connection();
//            v = MapperRegistry.getInstance().deleteVenue(vID, conn);
            v = (Venue) venueMapper.find(vID);
//            v.markRemoved();
//            venueMapper.delete(vID, conn);
            EventMapper em = MapperRegistry.getInstance().getMapper(Event.class);
            if (em.findByVenue(v.getPrimaryKey().getId()) != null)
                throw new InvalidUpdateException("Not allowed to delete venue with created event.");
//            System.out.println("----- test delete venue logic has event " + isEvent);
            v.markRemoved();

            UnitOfWork.getCurrent().commit();
//        } catch (Exception e) {
//            if (conn != null) conn.rollback();
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (conn != null) conn.close();
//        }
        return v;
    }

    public static Venue viewOneVenue(String idStr) throws SQLException {
//        Connection conn = null;
        Venue v;
        List<Section> sections;
//        UUID vID;

        try {
            Key vID = new Key(UUID.fromString(idStr));
//            conn = connection();
            v = (Venue) venueMapper.find(vID);
            sections = sectionMapper.findByVenue(vID.getId());
            v.addSections(sections);
        } catch(Exception e) {
//            if (conn != null) conn.rollback();
            throw e;
        }
        return v;
    }

    public static List<Venue> viewAllVenue() throws SQLException {
        List<Venue> venues;
        try {
            venues =  venueMapper.viewAll();
            for(Venue v: venues){
                List<Section> s = sectionMapper.findByVenue(v.getId());
                v.addSections(s);
            }

        } catch(Exception e) {
            throw e;
        }
        System.out.println("test view all........." + venues);

        return venues;
    }

    public static Venue updateVenue(HttpServletRequest request) throws Exception {
        Venue requestedV, originalV;

            // Start the unit of work
            UnitOfWork.newCurrent();

            // Fetch the requested venue from the request
            requestedV = venueInstant(request);

            // Fetch the original venue and its sections
            originalV = (Venue) venueMapper.find(requestedV.getPrimaryKey());
            originalV.addSections(sectionMapper.findByVenue(requestedV.getId()));

            // Compare sections
            EventMapper em = MapperRegistry.getInstance().getMapper(Event.class);
//            System.out.println("fix event bug  "  + em.findByVenue(requestedV.getPrimaryKey().getId()));
            boolean isEvent = (em.findByVenue(requestedV.getPrimaryKey().getId()) != null);
            System.out.println("----- test venue logic has event " + isEvent);
            compareSections(requestedV.getSections(), originalV.getSections(), requestedV.getId(), isEvent);

            // Mark the venue as dirty and register it to be updated
            requestedV.markDirty();
//
            // Commit changes
            UnitOfWork.getCurrent().commit();

        /** Check if there is an current event */

        return requestedV;
    }


    public static Venue createVenue(HttpServletRequest request) throws Exception {
        Venue v;
        List<Section> sections;

        try {
            // Start the unit of work
            UnitOfWork.newCurrent();

            // Create the venue
            v = venueInstant(request);
            v.markNew();

            // Commit changes to save the venue and get its ID
            sections = v.getSections();
            for (Section s : sections) {
                s.setVenueID(v.getId());
                s.markNew();
            }
            // Commit changes to save the sections
            UnitOfWork.getCurrent().commit();
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return v;
    }


    // creates a Venue instance by first converting it into a map of {String key: String value},
    // then processing the map into a Venue.
    public static Venue venueInstant(HttpServletRequest request) throws Exception {

        Venue v;

        String line;
        StringBuilder reqSB = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) reqSB.append(line);
        }

        System.out.println("Received payload: " + reqSB);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> objParams = mapper.readValue(reqSB.toString(), typeRef);

        v = new Venue();
        String idStr = (String) objParams.get("id");
        if (idStr != null) {
            v.setPrimaryKey(new Key(UUID.fromString(idStr)));
        }else{
            idStr = String.valueOf(UUID.randomUUID());
            v.setPrimaryKey(new Key(UUID.fromString(idStr)));
        }
        v.setName((String) objParams.get("name"));
        v.setAddress((String) objParams.get("address"));
        v.setVersion((Integer) objParams.get("version"));

        // add sections
        v.addSections(parseSections(objParams.get("sections"), idStr));

        return v;
    }

    public static List<Section> parseSections(Object input, String vIdStr) {
        List<Section> sections = new ArrayList<>();
        List<LinkedHashMap> sectionMaps = (ArrayList) input;
        for (LinkedHashMap sMap: sectionMaps) {
            Section s = new Section();
            String idStr = (String) sMap.get("id");
            if (idStr != null) s.setPrimaryKey(new Key(UUID.fromString(idStr)));
            else s.setPrimaryKey(null);
            s.setVenueID(UUID.fromString(vIdStr));
            s.setType((String) sMap.get("type"));
            Object capacityObj = sMap.get("capacity");
            if (capacityObj instanceof String) {
                s.setCapacity(Integer.valueOf((String) capacityObj));
            } else if (capacityObj instanceof Integer) {
                s.setCapacity((Integer) capacityObj);
            } else {
                throw new RuntimeException("Invalid type for capacity");
            }
            sections.add(s);
        }

        return sections;
    }

    public static void compareSections(List<Section> requested, List<Section> original,
                                       UUID eId, boolean isEvent) throws InvalidUpdateException {

//        System.out.println("requested: " + requested);
//        System.out.println("original: " + original);

        List<Section>[] sectDeleteModifyAdd =
                new ArrayList[]{new ArrayList<Section>(), new ArrayList<Section>(), new ArrayList<Section>()};
        Map<Object, Section> originalMap = new HashMap<>();
        for (Section t: original) originalMap.put(t.getId().toString(), t);
        for (Section t: requested) {
            if (t.getPrimaryKey() == null) {
                t.setPrimaryKey(new Key(UUID.randomUUID()));
                // Section cannot be deleted or created when there is event exists.
                if(isEvent)
                    throw new InvalidUpdateException("Fail to create section with existing event.");
                sectDeleteModifyAdd[2].add(t);
                t.setVenueID(eId);
                System.out.println("......add " + t);
                t.markNew();
            }
            else {
                sectDeleteModifyAdd[1].add(t);
//                int oriCapacity = originalMap.get(t.getId()).getCapacity();
//                int newCapacity = t.getCapacity();
//                if(newCapacity < oriCapacity && isEvent)
//                    throw new InvalidUpdateException("Failed to decrement the capacity of section with existing event");
                t.markDirty();
                originalMap.remove(t.getId().toString());
            };
        }
        for (Section t: originalMap.values()){
            sectDeleteModifyAdd[0].add(t);
            if(isEvent && !sectDeleteModifyAdd[0].isEmpty())
                throw new InvalidUpdateException("Fail to delete section with existing event.");
            System.out.println("......remove " + t);
            t.markRemoved();
        }
    }
}
