package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.objects.Section;

import java.util.*;

public class ApplicationSession {
    private String id;
    private String user = null;

    // Work as identity Map;
    private IdentityHashMap imap = new IdentityHashMap();

    public ApplicationSession(String id, String name, IdentityHashMap imap) {
        this.id = id;
        this.user = name;
//        this.address = address;
        this.imap = imap;
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
