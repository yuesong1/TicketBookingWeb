package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import org.springframework.security.access.method.P;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class Event extends DomainObject{
//    private UUID id = null;
    private String name = null;
    private Venue venue = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private String artist = null;
    private String description = null;
    private final List<Booking> bookings = new ArrayList<>();
    private final List<EventSection> eventSections = new ArrayList<>();
    private final List<String> plannerEmails = new ArrayList<>();

    private List<Planner> planners = new ArrayList<>();

    private int version = 0;
    public Event() {
    }

    public Event(UUID id, String name, Venue venue, LocalDateTime startDateTime, LocalDateTime endDateTime,
                 String artist, String description, int version) {
        setPrimaryKey(new Key(id));
        this.name = name;
        this.venue = venue;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.artist = artist;
        this.description = description;
        this.version = version;

//        this.markNew();
    }

    private String eventSectionString() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (EventSection s: eventSections) {
            Section ss = (Section) s.getSection();
            sb.append('{');
            sb.append("\"id\": \"" + s.getSectionID() + "\", ");
            sb.append("\"type\": \"" + ss.getType() + "\", ");
            sb.append("\"capacity\": " + ss.getCapacity() + ", ");
            sb.append("\"price\": " + s.getPrice() + ", ");
            sb.append("\"ticketLeft\": " + s.getAvailability() + ", ");
            sb.append("\"version\": " + s.getVersion());
            sb.append("}, ");
        }
        String out = sb.toString();
        if (out.length() >= 2) out = out.substring(0, out.length() - 2);
        return out + "]";
    }

    public String plannerEmailsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String s: plannerEmails) sb.append("\"" + s + "\"" + ", ");
        String out = sb.toString();
        if (out.length() >= 2) out = out.substring(0, out.length()-2);
        return out + "]";
    }

    @Override
    public String toString() {
        try {
            return "{" +
                    "\"id\": \"" + getId() + "\", " +
                    "\"name\": \"" + name + "\", " +
                    "\"artist\": \"" + artist + "\", " +
                    "\"startDate\": \"" + startDateTime.toString().replace('T', ' ') + "\", " +
                    "\"endDate\": \"" + endDateTime.toString().replace('T', ' ') + "\", " +
                    "\"planners\": " + plannerEmailsString() + ", " +
                    "\"version\": " + version + ", " +
                    "\"venue\": " + "{" +
                        "\"id\": \"" + venue.getId() + "\", " +
                        "\"name\": \"" + venue.getName() + "\", " +
                        "\"address\": \"" + venue.getAddress() + "\", " +
                        "\"version\": \"" + venue.getVersion() + "\", " +
                        "\"sections\": " + eventSectionString() +
                        "}" + ", " +
                    "\"description\": \"" + description +
                    "\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    // getters & setters

    public UUID getId() { return getPrimaryKey().getId(); }

    public void setId(UUID id) {
//        this.id = id;
        setPrimaryKey(new Key(id));
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getPlannerEmails() { return plannerEmails;  }
    public void addPlannerEmail(String p) { this.plannerEmails.add(p); }
    public void addPlannerEmails(List<String> pEmails) { for (String p: pEmails) plannerEmails.add(p); }

    public List<EventSection> getEventSections() { return eventSections; }
    public void addEventSection(EventSection es) { this.eventSections.add(es); }
    public void addEventSections(List<EventSection> eSections) {
        for (EventSection es: eSections) this.eventSections.add(es);
    }
    public void setEventSections(List<EventSection> es) {
        this.eventSections.clear();
        this.eventSections.addAll(es);
    }
    public void setPlannerEmails(List<String> pe) {
        this.plannerEmails.clear();
        this.plannerEmails.addAll(pe);
    }


    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public void setPlanners(List<Planner> planners) {
        this.planners = planners;
    }

    public List<Planner> getPlanners() {
        return planners;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
