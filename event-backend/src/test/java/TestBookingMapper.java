import com.unimelb.swen90007.reactexampleapi.api.mappers.BookingMapper;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.objects.Booking;
import com.unimelb.swen90007.reactexampleapi.api.objects.Customer;
import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

//public class TestBookingMapper {
//    Connection conn = DBUtil.connection();
//    UUID eId = UUID.randomUUID();
//    UUID sId = UUID.randomUUID();
//
//    String cusEmail = "customer.edu.au";
//    // @Test
//    public void testCreate() throws Exception{
//        Section s = new Section();
//        s.setId(sId);
//        Event e = new Event();
//        e.setId(eId);
//        Customer c = new Customer();
//        c.setEmail(cusEmail);
//        Booking booking = new Booking(s, 2, c, e);
//
////        booking = BookingMapper.createBooking(booking, conn);
//        conn.commit();
////        assertTrue(BookingMapper.checkBooking(booking.getId(), conn));
//    }
//    // @Test
//    public void testUpdate() throws Exception{
//        Section s = new Section();
//        s.setId(sId);
//        Event e = new Event();
//        e.setId(eId);
//        Customer c = new Customer();
//        c.setEmail("new email.com");
//        Booking booking = new Booking(s, 2, c, e);
////        booking.setId(UUID.fromString("0c3ec215-7c18-4972-8c60-863c0aa63acf"));
//
////        assertTrue(BookingMapper.updateBooking(booking, conn));
//        conn.commit();
//    }
//
//    // @Test
//    public void testDelete() throws Exception{
//        UUID bID = UUID.fromString("51ea8b80-e0f8-4b91-a4e4-a18145b8051e");
////        BookingMapper.deleteBooking(bID, conn);
//        conn.commit();
//    }
//    // @Test
//    public void testView() throws Exception{
//        Customer c = new Customer();
//        c.setEmail(cusEmail);
//
//    }


//}
