package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;

public abstract class DomainObject {
    private Key primaryKey;

    protected DomainObject (Key primaryKey){
        this.primaryKey = primaryKey;
    }

    protected DomainObject(){}

    public Key getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Key primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "DomainObject{" +
                "primaryKey=" + primaryKey +
                '}';
    }

    /** Marking method to register itself with the current Unit of Work.
     */
    public void markNew() {
        UnitOfWork.getCurrent().registerNew(this);
    }
    public void markClean() {
        UnitOfWork.getCurrent().registerClean(this);
    }
    public void markDirty() {
        UnitOfWork.getCurrent().registerDirty(this);
    }
    public void markRemoved() {
        UnitOfWork.getCurrent().registerRemoved(this);
    }
}
