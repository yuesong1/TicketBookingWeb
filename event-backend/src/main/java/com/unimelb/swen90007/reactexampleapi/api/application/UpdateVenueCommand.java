package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.application.AppSessionManager;
import com.unimelb.swen90007.reactexampleapi.api.application.BusinessTransactionCommand;
import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.objects.Exceptions.LockFailureException;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.LockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import jakarta.servlet.RequestDispatcher;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Need an id of what need to be edited.
 */
public class UpdateVenueCommand extends BusinessTransactionCommand {
    @Override
    public void process() throws Exception {
        UnitOfWork.newCurrent();
        try {
            startNewBusinessTransaction();
            String venueId = getReq().getParameter("id");
            LockManager lock = ReadWriteLockManager.getInstance();
            if (lock.acquireWriteLock(venueId, AppSessionManager.getSession().getId())) {
                Venue venue = (Venue) MapperRegistry.getInstance().getMapper(Venue.class).
                        find(new Key(UUID.fromString(venueId)));
                System.out.println("--------test acquire lock command " + venue);
                getReq().getSession().setAttribute("venue", venue);
            } else {
                throw new LockFailureException(" Failed to acquire write lock for venue");
            }

            UnitOfWork.getCurrent().commit();
        }catch (SQLException e){
            DBUtil.getConnection().rollback();
            throw e;
        }finally {
            DBUtil.closeConnection();
        }
//        forward("/editCustomer.jsp");
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/target-servlet");
//        dispatcher.forward(request, response);
    }
}
