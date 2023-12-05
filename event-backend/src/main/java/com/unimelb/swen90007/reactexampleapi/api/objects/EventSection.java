package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad.ValueHolder;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
// Include a value holder
public class EventSection extends DomainObject {
    private Double price = null;
    private Integer availability = null;

    private Integer version = null;
    private ValueHolder section;

    public EventSection(Double price, int availability, Key key, int version) {
        setPrimaryKey(key);
        this.price = price;
        this.availability = availability;
        this.version = version;

    }
//    private UUID eventID = null;

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getAvailability() { return availability; }
    public void setAvailability(Integer availability) { this.availability = availability; }
    public UUID getEventID() { return (UUID) getPrimaryKey().getKey(0); }

    public UUID getSectionID() { return (UUID) getPrimaryKey().getKey(1); }
    public void setEventID(UUID eventID) {
        Object[] pks = getPrimaryKey().getPks();
        pks[0] = eventID;
        this.setPrimaryKey(new Key(pks));

    }

    public EventSection() {
    }


    public Object getSection() throws SQLException {

        return section.getValue();
    }

    public void setSection(ValueHolder section) {
        this.section = section;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
