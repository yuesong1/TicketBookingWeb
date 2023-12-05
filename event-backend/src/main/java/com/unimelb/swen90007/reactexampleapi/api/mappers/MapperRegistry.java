package com.unimelb.swen90007.reactexampleapi.api.mappers;

import com.unimelb.swen90007.reactexampleapi.api.objects.*;


import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    // Singleton instance
    private static MapperRegistry instance;

    // Map to store different types of mappers
    static Map<Class<?>, AbstractMapper> mappers;

    // Private constructor to prevent external instantiation
    private MapperRegistry() {
        mappers = new HashMap<>();
        registerMapper();
//        System.out.println(mappers);
    }

    // Get the singleton instance of MapperRegistry
    public static MapperRegistry getInstance() {
        if (instance == null) instance = new MapperRegistry();
        return instance;
    }

    // Register mappers
    private void registerMapper() {
        mappers.put(User.class, new UserMapper());
        mappers.put(Planner.class, new UserMapper());
        mappers.put(Customer.class, new UserMapper());
        mappers.put(Admin.class, new UserMapper());
        mappers.put(Event.class, new EventMapper());
        mappers.put(Section.class, new SectionMapper());
        mappers.put(Venue.class, new VenueMapper());
        mappers.put(Booking.class, new BookingMapper());
        mappers.put(EventSection.class, new EventSectionMapper());
    }

    // Get a mapper instance by its class
    public  <T extends AbstractMapper> T getMapper(Object o) {
        AbstractMapper mapper = mappers.get(o);
        if (mapper == null)
            throw new IllegalArgumentException("Mapper not found for class: " + o.getClass());
        return (T) mapper;
    }

//    public static void main(String[] args){
//        AbstractMapper m = MapperRegistry.getInstance().getMapper(Venue.class);
//        System.out.println(m);
//    }
}


