package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

import java.util.ArrayList;
import java.util.List;

public class Planner extends User {

    public Planner() {this.setRole("planner"); }

    public Planner(String email) {
        super(email);
        this.setRole("planner");
    }

    public Planner(Key key) {
        setPrimaryKey(key);
        this.setRole("planner"); }
}
