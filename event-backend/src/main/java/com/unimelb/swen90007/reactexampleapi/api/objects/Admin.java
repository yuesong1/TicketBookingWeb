package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

public class Admin extends User {
    public Admin() { this.setRole("admin"); }
    public Admin(Key key) {
        setPrimaryKey(key);
        this.setRole("admin"); }
}
