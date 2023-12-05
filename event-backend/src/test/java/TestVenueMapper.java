import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.mappers.VenueMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.Venue;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;


public class TestVenueMapper {
    private Connection conn = DBUtil.connection();

    public static final String name1 = "new name2";
    public static final String add1 = "Melbourne";
    // @Test
    public void testCreateVenue() throws Exception{

        Venue v = new Venue();
        v.setAddress(add1);
        v.setName(name1);
        v.setPrimaryKey(new Key(UUID.fromString("2d6e9adc-a1ff-44d0-9352-e3ef03abf02b")));
        UnitOfWork.newCurrent();
        v.markRemoved();
        UnitOfWork.getCurrent().commit();
        // make it return id if existence.
//        assertEquals(VenueMapper.checkExistence(name1, add1, conn).toString(), "602c6e3b-ec20-4180-aa1d-0e3b009ab4b1");
//        v = VenueMapper.createVenue(v, conn);
        System.out.println(v);

//        v.setName("new name2");
//        VenueMapper.updateVenue(v, conn);
    }



    // @Test
    public void testFind() throws Exception{
//        Venue v1 = VenueMapper.viewByNameSearch(name1, conn);
//        System.out.println(v1);
//
//        UUID id = UUID.fromString("5fb3b733-1eef-4a94-8495-5e412118016b");
//        v1 = VenueMapper.viewById(id, conn);
//        System.out.println(v1);
    }

    // @Test
    public void testDeleteVenue() throws Exception{
        String name2 = "new name2";
        String add2 = "Melbourne";

//        UUID id = VenueMapper.checkExistence(name2, add2, conn);
//        VenueMapper.deleteVenue(id, conn);
    }


}
