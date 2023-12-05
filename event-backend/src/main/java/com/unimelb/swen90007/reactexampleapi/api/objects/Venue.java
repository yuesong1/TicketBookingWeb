package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.util.*;

/* venue class with venueID, name, address, and list of sections */
public class Venue extends DomainObject{
//    private UUID id = null;
    private String name = null;
    private String address = null;
    private final List<Section> sections = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();

    public UUID getId() { return getPrimaryKey().getId(); }

    public void setId(UUID id) { setPrimaryKey(new Key(id));}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public List<Section> getSections() { return sections; }

    public void addSection(Section section) { this.sections.add(section); }
    public void addSections(List<Section> sections) { this.sections.addAll(sections); }
    public void clearSections() { this.sections.clear(); }
    public void rmvSection(Section section) { this.sections.remove(section); }
    public void rmvSection(int index) { this.sections.remove(index); }

    public List<Event> getEvents() { return events; }

    private Integer version;

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + getId() + "\", " +
                "\"name\": \"" + name + "\", " +
                "\"address\": \""  + address + "\", " +
                "\"version\": \"" + version + "\", " +
                "\"sections\": " + sections +
                "}";
    }

    public Venue() {
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public Venue(Key id, String name, String address, int version) {
        super(id);
        this.name = name;
        this.address = address;
        this.version = version;

//        this.markNew();
    }
}
