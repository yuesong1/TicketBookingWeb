package com.unimelb.swen90007.reactexampleapi.api.util;

import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.objects.Admin;
import com.unimelb.swen90007.reactexampleapi.api.objects.Customer;
import com.unimelb.swen90007.reactexampleapi.api.objects.DomainObject;
import com.unimelb.swen90007.reactexampleapi.api.objects.Planner;
import io.jsonwebtoken.lang.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * If it works, can delete all connection
 */
public class UnitOfWork {

    private static ThreadLocal current = new ThreadLocal();
//    static final Connection conn = DBUtil.connection();
    private List newObjects = new ArrayList();
    private List dirtyObjects = new ArrayList();
    private List removedObjects = new ArrayList();

//    private Connection connection;

    public void registerNew(DomainObject object) {
//        Assert.notNull(object.getPrimaryKey(), "Primary key should not be null.");
        Assert.isTrue(!dirtyObjects.contains(object), "Object is not dirty.");
        Assert.isTrue(!removedObjects.contains(object), "Object is not removed.");
        Assert.isTrue(!newObjects.contains(object), "Object is not already registered as new.");

        newObjects.add(object);
    }
    public void registerDirty(DomainObject object) {
        Assert.notNull(object.getPrimaryKey(), "Primary key should not be null.");
        Assert.isTrue(!removedObjects.contains(object), "Object is not removed.");
        if (!dirtyObjects.contains(object) && !newObjects.contains(object)) {
            dirtyObjects.add(object);
        }
    }
    public void registerRemoved(DomainObject object) {
        Assert.notNull(object.getPrimaryKey(), "Primary key should not be null.");
        if (newObjects.remove(object)) return;
        dirtyObjects.remove(object);
        if (!removedObjects.contains(object)) {
            removedObjects.add(object);
        }
    }
    public void registerClean(DomainObject object) {
        Assert.notNull(object.getPrimaryKey(), "Primary key should not be null.");
    }

    public void commit() throws SQLException {
        insertNew();
        updateDirty();
        deleteRemoved();

        try {
            DBUtil.getConnection().commit();
//            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to commit in UoW");
//            if(conn != null) conn.rollback();

        } finally {
            DBUtil.closeConnection();
        }
    }
    private void insertNew(){
        for (Iterator objects = newObjects.iterator(); objects.hasNext();) {
            DomainObject o = (DomainObject) objects.next();
            MapperRegistry.getInstance().getMapper(o.getClass()).insert(o, DBUtil.getConnection());
        }
    }
    private void updateDirty(){
        for (Iterator objects = dirtyObjects.iterator(); objects.hasNext();) {
            DomainObject o = (DomainObject) objects.next();
            MapperRegistry.getInstance().getMapper(o.getClass()).update(o, DBUtil.getConnection());
        }
    }

    public void rollback() {
        try {
            DBUtil.getConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }

    private void deleteRemoved(){
        for (Iterator objects = removedObjects.iterator(); objects.hasNext();) {
            DomainObject o = (DomainObject) objects.next();
//            System.out.println(o.getClass() + "--------------------uom");
            MapperRegistry.getInstance().getMapper(o.getClass()).delete(o.getPrimaryKey(), DBUtil.getConnection());
        }
    }
    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }
    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }
    public static UnitOfWork getCurrent() {
        return (UnitOfWork) current.get();
    }

//    public static Connection getConn() {
//        return DBUtil.getConnection();
//    }
}
