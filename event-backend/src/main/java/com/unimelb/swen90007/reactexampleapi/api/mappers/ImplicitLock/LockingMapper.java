//package com.unimelb.swen90007.reactexampleapi.api.mappers.ImplicitLock;
//
//import com.unimelb.swen90007.reactexampleapi.api.application.AppSessionManager;
//import com.unimelb.swen90007.reactexampleapi.api.mappers.AbstractMapper;
//import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
//import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.ConcurrencyException;
//import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
//import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
//import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
//
//public class LockingMapper implements Mapper{
//    private AbstractMapper mapper;
//    public LockingMapper(AbstractMapper impl) {
//        this.mapper = impl;
//    }
//    public DomainObject find(Key key) throws ConcurrencyException {
//        ReadWriteLockManager.getInstance().acquireReadLock(
//                key.getId().toString(), AppSessionManager.getSession().getId());
//        return mapper.find(key);
//    }
//    public void insert(DomainObject obj) {
//        mapper.insert(obj);
//    }
//    public void update(DomainObject obj) {
//        mapper.update(obj);
//    }
//    public void delete(DomainObject obj) {
//        mapper.delete(obj.getPrimaryKey());
//    }
//}
