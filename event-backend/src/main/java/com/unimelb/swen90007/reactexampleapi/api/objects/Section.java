package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.util.UUID;

/* section class with sectionID, type, capacity */
public class Section extends DomainObject {
//    private UUID id = null;
    private UUID venueID = null;
    private String type = null;
    private Integer capacity = null;

    public UUID getId() { return getPrimaryKey().getId(); }

    public void setId(UUID id) { setPrimaryKey(new Key(id)); }

    public UUID getVenueID() { return venueID; }

    public void setVenueID(UUID venueID) { this.venueID = venueID; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Integer getCapacity() { return capacity; }

    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    @Override
    public String toString() { return
        "{" +
        "\"id\": \"" + getId() + "\", " +
        "\"venueID\": \"" + venueID + "\", " +
        "\"type\": \"" + type + "\", " +
        "\"capacity\": " + capacity
        + "}"
        ;
    }
}
