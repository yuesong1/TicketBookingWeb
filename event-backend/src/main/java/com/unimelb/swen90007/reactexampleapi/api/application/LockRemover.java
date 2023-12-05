package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
//import jakarta.servlet.http.HttpSessionBindingListener;

public class LockRemover implements HttpSessionBindingListener {
    private String sessionId;
    public LockRemover(String sessionId) {
        this.sessionId = sessionId;
    }
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        try {
//            beginSystemTransaction();
            UnitOfWork.newCurrent();

            System.out.println("-- HttpSessionBindingEvent#valueUnbound() --");
            ReadWriteLockManager.getInstance().releaseAllLock(this.sessionId);

//            commitSystemTransaction();
            UnitOfWork.getCurrent().commit();
        } catch (Exception e) {
//            handleSeriousError(e);
            throw new RuntimeException("Failed to release all locks in in LockRemover.");
        }
    }
}
