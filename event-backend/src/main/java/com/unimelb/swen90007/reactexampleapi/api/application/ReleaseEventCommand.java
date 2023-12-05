package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.domain.EventLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;

import java.sql.SQLException;

public class ReleaseEventCommand extends BusinessTransactionCommand{
    public void process() throws Exception {
//        UnitOfWork.newCurrent();
        try {
            continueBusinessTransaction();
//            Venue venue = (Venue) getReq().getSession().getAttribute("venue");
//            String id = getReq().getParameter("id");
//            String id = venue.getPrimaryKey().getId().toString();
//        venue.setPrimaryKey(new Key(UUID.fromString(id)));
//        Mapper customerMapper = MapperRegistry.INSTANCE.getMapper(Customer.class);
//        MapperRegistry.getInstance().getMapper(Venue.class).update(venue);
//            Venue venue = VenueLogic.updateVenue(getReq());
            EventLogic logic = new EventLogic();
            Event event = logic.createEvent(getReq());
            getReq().getSession().setAttribute("event", event);
//            System.out.println("-----test release read lock command " + venue);
            ReadWriteLockManager.getInstance().releaseReadAllLock();
            DBUtil.getConnection().commit();
        }catch (SQLException e){
            DBUtil.getConnection().rollback();
            throw e;
        }finally {
            DBUtil.closeConnection();
        }
    }
}
