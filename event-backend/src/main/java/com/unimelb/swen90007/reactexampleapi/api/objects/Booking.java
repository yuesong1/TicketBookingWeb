package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.util.UUID;

public class Booking extends DomainObject{
    private Section section = null;
    private Integer numTickets = null;
    private String customerEmail = null;
    private Event event = null;

    private Integer version = null;

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + getPrimaryKey().getId() + "\", " +
                "\"customerEmail\": \"" + getCustomerEmail() + "\", " +
                "\"eventID\": \"" + event.getId() + "\", " +
                "\"eventName\": \"" + event.getName() + "\", " +
                "\"sectionID\": \"" + section.getId() + "\", " +
                "\"sectionType\": \"" + section.getType() + "\", " +
                "\"numTickets\": " + numTickets +
                "}";
    }

    public Booking() {
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Booking(Section section, Integer numTickets, String customer, Event event) {
        this.section = section;
        this.numTickets = numTickets;
        this.event = event;
        this.customerEmail = customer;
    }

    // getters & setters
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }

//    public UUID getId() { return getPrimaryKey().getId(); }
//    public void setId(UUID id) { setPrimaryKey(new Key(id)); }

    public Integer getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(Integer numTickets) {
        this.numTickets = numTickets;
    }

//    public String getCustomerEmail() { return customerEmail; }
//    public void setCustomerEmail(String email) { this.customerEmail = email; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public int getVersion() {
        return version;
    }

    public void setVersion(Integer version){
        this.version = version;
    }
}
