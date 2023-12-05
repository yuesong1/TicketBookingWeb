package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.LockFailureException;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.LockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ViewAllVenueCommand extends BusinessTransactionCommand{
    @Override
    public void process() throws Exception {
        UnitOfWork.newCurrent();
        try {
            startNewBusinessTransaction();
            String venueId = UUID.randomUUID().toString();
            ReadWriteLockManager lock = ReadWriteLockManager.getInstance();
            if (lock.acquireReadAllLock(venueId, AppSessionManager.getSession().getId())) {
                List venues = VenueLogic.viewAllVenue();
                System.out.println("--------test acquire read all lock command " + venues);
                getReq().getSession().setAttribute("venue", venues);
            } else {
                throw new LockFailureException(" Failed to acquire read all lock for venue");
            }
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            DBUtil.getConnection().rollback();
            throw e;
        } finally {
            DBUtil.closeConnection();
        }
    }
}
