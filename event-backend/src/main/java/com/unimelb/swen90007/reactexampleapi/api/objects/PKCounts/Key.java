package com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts;

import java.util.UUID;

/** Key class is used to store multiple elements of the primary key in database.
 * Methods are added to create, find, and check keys.*/
public class Key {
    private Object[] pks;

    public Key(Object[] pks) {
        checkKey(pks);
        this.pks = pks;
    }

    public Key(UUID uuid){
        this.pks = new Object[1];
        this.pks[0] = new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    public Object[] getPks() {
        return pks;
    }
    // To Get part of keys
    public Object getKey(int i){
        return pks[i];
    }

    public UUID getId(){
        if(pks.length > 1) throw new IllegalStateException("Unable get one value from composite key.");
        if(!(pks[0] instanceof UUID)) throw new IllegalStateException("Unable to get id from other key type.");
        return (UUID) pks[0];
    }

    // Check whether the given primary keys are the same.
    public boolean equals(Object object){
        if(!(object instanceof Key)) return false;
        Key compareKey = (Key) object;
        if(pks.length != compareKey.getPks().length) return false;
        for(int i = 0; i < pks.length; i++){
            if(! pks[i].equals(compareKey.getPks()[i])) return false;
        }
        return true;
    }

    // Check to make sure that the given primary key is not empty.
    private void checkKey(Object[] keyFields){
        if(keyFields == null) throw new IllegalArgumentException("Cannnot have a null key");
        for(Object f: keyFields){
            if (f == null) throw new IllegalArgumentException("Cannnot have a null element of key");
        }
    }
}
