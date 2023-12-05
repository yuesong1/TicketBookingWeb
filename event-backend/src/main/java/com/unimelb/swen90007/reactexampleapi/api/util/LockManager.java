package com.unimelb.swen90007.reactexampleapi.api.util;

import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.ConcurrencyException;

public interface LockManager {
    // = (ReadWriteLockManager) Plugins.getPlugin(ReadWriteLockManager.class);

    /**
     * @param lockable The lockable table using uuid primary key that’s unique across the entire system and so
     *                 serves as a nice lockable ID (which must be unique across all types handled by the lock table).
     * @param owner    the HTTP session ID makes a good lock owner as a String type.
     * @return
     */
    boolean acquireReadLock(String lockable, String owner) throws ConcurrencyException;

    boolean acquireWriteLock(String lockable, String owner) throws ConcurrencyException;
    void releaseReadLock(String lockable);

    void releaseWriteLock(String lockable);

    public static LockManager getInstance(){
        return ReadWriteLockManager.getInstance();
    }

}
