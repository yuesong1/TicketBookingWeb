package com.unimelb.swen90007.reactexampleapi.api.application;

import com.unimelb.swen90007.reactexampleapi.api.domain.VenueLogic;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.util.ReadWriteLockManager;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;

import java.sql.SQLException;

public class ReleaseVenueCommand extends BusinessTransactionCommand{
    public void process() throws Exception {
        UnitOfWork.newCurrent();
        try {
            continueBusinessTransaction();
            Venue venue = (Venue) getReq().getSession().getAttribute("venue");
//            String id = getReq().getParameter("id");
            String id = venue.getPrimaryKey().getId().toString();
//        venue.setPrimaryKey(new Key(UUID.fromString(id)));
//        Mapper customerMapper = MapperRegistry.INSTANCE.getMapper(Customer.class);
//        MapperRegistry.getInstance().getMapper(Venue.class).update(venue);
//            Venue venue = VenueLogic.updateVenue(getReq());
            System.out.println("-----test release read lock command " + venue);
            ReadWriteLockManager.getInstance().releaseReadLock(venue.getId().toString());
            UnitOfWork.getCurrent().commit();
        }catch (SQLException e){
            DBUtil.getConnection().rollback();
            throw e;
        }finally {
            DBUtil.closeConnection();
        }
    }
}
