package com.unimelb.swen90007.reactexampleapi.api.util;

import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.ConcurrencyException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadWriteLockManager implements LockManager{

    private static final int WRITE = 1;
    private static final int READ = 2;

    private static final int ALL = 3;
    private static ReadWriteLockManager instance;
//    private volatile ConcurrentHashMap<String, Integer> readLock = new ConcurrentHashMap<>();
    public static ReadWriteLockManager getInstance() {
        if (instance == null) {
            synchronized (ReadWriteLockManager.class) {
                if (instance == null) {
                    instance = new ReadWriteLockManager();
                }
            }
        }
        return instance;
    }
    private static final String INSERT_SQL =
            "insert into lock values(uuid(?), ?, ?);";
//    private static final String INSERT_ALL_SQL =
//            "insert into lock values(uuid(?), ?, ?);";
//    "INSERT INTO lock (ownerid, version) " +
//            "VALUES (?, ?);";
    private static final String DELETE_SINGLE_SQL =
            "delete from lock where lockableid = uuid(?) and version = ?;";
    private static final String DELETE_ALL_SQL =
            "delete from lock where ownerid = ?;";
    private static final String DELETE_READALL_SQL =
            "delete from lock where version = 3;";
    private static final String CHECK_LOCK =
            "select lockableid from lock where lockableid = uuid(?);";
    private static final String CHECK_WRITE_LOCK =
            "select lockableid from lock where lockableid = uuid(?) and version = ?;";
    private static final String CHECK_READ_LOCK =
            "select lockableid from lock where lockableid = uuid(?) and version = ?;";

    private static final String CHECK_READALL_LOCK =
            "select lockableid from lock where version = 3;";
    private static final String CHECK_ANYWRITE_LOCK =
            "select lockableid from lock where version = 1;";
    public boolean acquireReadLock(String lockable, String owner) throws ConcurrencyException{
        boolean result = true;
        if(hasReadLock(lockable)){
            return result;
        }else if(!hasWriteLock(lockable)) {
            PreparedStatement stat = null;
            try {
                stat = DBUtil.getConnection().prepareStatement(INSERT_SQL);
                stat.setString(1, lockable);
//                stat.setString(2, owner);
                stat.setInt(2, READ);
                stat.executeUpdate();
            } catch (SQLException sqlEx) {
                result = false;
            } finally {
                DBUtil.cleanUp(stat);
            }
        }else result = false;
        return result;
    }

    public boolean acquireReadAllLock(String lockable, String owner) throws ConcurrencyException{
        boolean result = true;
        if(!hasAnyWriteLock()) {
            PreparedStatement stat = null;
            try {
                stat = DBUtil.getConnection().prepareStatement(INSERT_SQL);
                stat.setString(1, lockable);
                stat.setString(2, owner);
                stat.setInt(3, ALL);
                stat.executeUpdate();
            } catch (SQLException sqlEx) {
                result = false;
            } finally {
                DBUtil.cleanUp(stat);
            }
        }else result = false;
        return result;
    }



    public boolean acquireWriteLock(String lockable, String owner) throws ConcurrencyException {
        // If the owner already has the write lock, this should succeed
        boolean result = true;

        if(!hasLock(lockable)) {
            PreparedStatement stat = null;
            try {
                stat = DBUtil.getConnection().prepareStatement(INSERT_SQL);
                stat.setString(1, lockable);
                stat.setString(2, owner);
                stat.setInt(3, WRITE);
                stat.executeUpdate();
            } catch (SQLException sqlEx) {
                result = false;
            } finally {
                DBUtil.cleanUp(stat);
            }
//        }else if(!hasWriteLock(lockable)){
        }else {
            result = false;
        }
        return result;
    }
    public void releaseWriteLock(String lockable) {
        PreparedStatement pstmt = null;
        try {
            pstmt = DBUtil.getConnection().prepareStatement(DELETE_SINGLE_SQL);
            pstmt.setString(1, lockable);
//            pstmt.setString(2, owner);
            pstmt.setInt(2, WRITE);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("unexpected error releasing write lock on " + lockable);
        } finally {
            DBUtil.cleanUp(pstmt);
//            DBUtil.closeConnection();
        }
    }

    public void releaseReadAllLock() {
        PreparedStatement pstmt = null;
        try {
            pstmt = DBUtil.getConnection().prepareStatement(DELETE_READALL_SQL);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("unexpected error releasing read all lock");
        } finally {
            DBUtil.cleanUp(pstmt);
//            DBUtil.closeConnection();
        }
    }

    public void releaseReadLock(String lockable) {
        PreparedStatement pstmt = null;
        try {
            pstmt = DBUtil.getConnection().prepareStatement(DELETE_SINGLE_SQL);
            pstmt.setString(1, lockable);
//            pstmt.setString(2, owner);
            pstmt.setInt(2, READ);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("unexpected error releasing write lock on " + lockable);
        } finally {
            DBUtil.cleanUp(pstmt);
//            DBUtil.closeConnection();
        }
    }

    public void releaseAllLock(String owner){
        PreparedStatement stat = null;
        try {
            stat = DBUtil.getConnection().prepareStatement(DELETE_ALL_SQL);
            stat.setString(1, owner);
            stat.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("unexpected error releasing all locks on session" + owner);
        }finally {
            DBUtil.cleanUp(stat);
//            DBUtil.closeConnection();
        }
    }

    private boolean hasAnyWriteLock(){
        ResultSet rs = null;
        PreparedStatement checkStatement = null;
        try{
            checkStatement = DBUtil.getConnection().prepareStatement(CHECK_ANYWRITE_LOCK);
            rs = checkStatement.executeQuery();
            if(rs.next()){
                return true;
            }else return false;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to check write lock in database.");
        } finally {
            DBUtil.cleanUp(checkStatement, rs);
        }
    }

    private boolean hasLock(String lockable){
        ResultSet rs = null;
        PreparedStatement checkStatement = null;
        try{
            checkStatement = DBUtil.getConnection().prepareStatement(CHECK_LOCK);
            checkStatement.setString(1, lockable);
//            checkStatement.setString(2, owner);
            rs = checkStatement.executeQuery();
            if(rs.next()){
                return true;
            }else {
                checkStatement = DBUtil.getConnection().prepareStatement(CHECK_READALL_LOCK);
                rs = checkStatement.executeQuery();
                if(rs.next()) return true;
                else return false;
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to check all locks in database.");
        } finally {
            DBUtil.cleanUp(checkStatement, rs);
        }
    }

    private boolean hasWriteLock(String lockable){
        ResultSet rs = null;
        PreparedStatement checkStatement = null;
        try{
            checkStatement = DBUtil.getConnection().prepareStatement(CHECK_WRITE_LOCK);
            checkStatement.setString(1, lockable);
//            checkStatement.setString(2, owner);
            checkStatement.setInt(2, WRITE);
            rs = checkStatement.executeQuery();
            if(rs.next()){
                return true;
            }else return false;
        } catch (SQLException e){
            throw new RuntimeException("Failed to check write lock in database.");
        } finally {
            DBUtil.cleanUp(checkStatement, rs);
        }
    }

    private boolean hasReadLock(String lockable){
        ResultSet rs = null;
        PreparedStatement checkStatement = null;
        try{
            checkStatement = DBUtil.getConnection().prepareStatement(CHECK_READ_LOCK);
            checkStatement.setString(1, lockable);
//            checkStatement.setString(2, owner);
            checkStatement.setInt(2, READ);
            rs = checkStatement.executeQuery();
            if(rs.next()){
                return true;
            }else {
                checkStatement = DBUtil.getConnection().prepareStatement(CHECK_READALL_LOCK);
                rs = checkStatement.executeQuery();
                if(rs.next()) return true;
                else return false;
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to check write lock in database.");
        } finally {
            DBUtil.cleanUp(checkStatement, rs);
        }
    }
}
