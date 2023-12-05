package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private final List<Booking> bookings = new ArrayList<>();

    public Customer() { this.setRole("customer"); }
    public Customer(Key key) {
        setPrimaryKey(key);
        this.setRole("customer");
    }

    public List<Booking> getBookings() { return bookings; }
    public void addBookings(List<Booking> bookings) {
        this.bookings.addAll(bookings);
    }
}