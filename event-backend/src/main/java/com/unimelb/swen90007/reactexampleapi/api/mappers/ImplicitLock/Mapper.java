package com.unimelb.swen90007.reactexampleapi.api.mappers.ImplicitLock;
import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.ConcurrencyException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;

public interface Mapper {
    public DomainObject find(Key key) throws ConcurrencyException;
    public void insert(DomainObject obj);
    public void update(DomainObject obj);
    public void delete(DomainObject obj);
}
