// <<<<<<< refineMapper
// //import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
// //import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
// //import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
// //import com.unimelb.swen90007.reactexampleapi.api.mappers.EventMapper;
// //import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
// //import com.unimelb.swen90007.reactexampleapi.api.objects.Planner;
// //import com.unimelb.swen90007.reactexampleapi.api.objects.UserAccess;
// //import org.junit.jupiter.api.Test;
// //
// //import java.sql.Connection;
// //import java.util.ArrayList;
// //import java.util.List;
// //import java.util.UUID;
// //
// //import static org.junit.jupiter.api.Assertions.assertFalse;
// //import static org.junit.jupiter.api.Assertions.assertTrue;
// //
// //public class TestEventPlanner {
// //    Connection conn = DBUtil.connection();
// //    UUID eId = UUID.fromString("8839a298-a7dc-4e21-b820-cfda7050f080");
// //    String pEmail = "p.email.com";
// //    String pPass = "222222";
// //
// //    @Test
// //    public void testCreate() throws Exception{
// ////        Event e = EventMapper.viewOneEvent(eId, conn);
// ////        Planner p = new Planner();
// ////        p.setEmail(pEmail);
// ////        p.setRole(UserAccess.planner.name());
// ////        p.setPassword(pPass);
// //        List<String> plannerEmails = new ArrayList<>();
// //        plannerEmails.add(pEmail);
// //        plannerEmails.add("email!");
// //
// ////        EventMapper.createPlannerEvents(plannerEmails, eId, conn);
// //        conn.commit();
// //
// ////        p.setEmail("p2.email");
// ////        p.setRole(UserAccess.planner.name());
// ////        p.setPassword("xxxxxxxxx");
// ////
// ////        EventMapper.createPlannerEvents(p, e, conn);
// //    }
// //
// //    @Test
// //    public void testCheck() throws Exception{
// //        Event e = (Event) MapperRegistry.getInstance().getMapper(Event.class).find(new Key(eId));
// =======
// import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
// import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
// import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
// import com.unimelb.swen90007.reactexampleapi.api.mappers.EventMapper;
// import com.unimelb.swen90007.reactexampleapi.api.objects.Event;
// import com.unimelb.swen90007.reactexampleapi.api.objects.Planner;
// import com.unimelb.swen90007.reactexampleapi.api.objects.UserAccess;
// import org.junit.jupiter.api.Test;

// import java.sql.Connection;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// public class TestEventPlanner {
//     Connection conn = DBUtil.connection();
//     UUID eId = UUID.fromString("8839a298-a7dc-4e21-b820-cfda7050f080");
//     String pEmail = "p.email.com";
//     String pPass = "222222";

//     // @Test
//     public void testCreate() throws Exception{
// //        Event e = EventMapper.viewOneEvent(eId, conn);
// >>>>>>> main
// //        Planner p = new Planner();
// //        p.setEmail(pEmail);
// //        p.setRole(UserAccess.planner.name());
// //        p.setPassword(pPass);
// <<<<<<< refineMapper
// //
// //        assertTrue(EventMapper.checkPlannerEvent(p, e, conn));
// //
// //        p.setEmail("not exist!");
// //        assertFalse(EventMapper.checkPlannerEvent(p, e, conn));
// //    }
// //
// //    @Test
// //    public void testView() throws Exception{
// //
// //        List<String> ps = EventMapper.viewPlannerByEvent(eId, conn);
// //        System.out.println(ps);
// //        System.out.println("---------------------------");
// //
// ////        List<UUID> es = EventMapper.viewEventsByPlanner(pEmail, conn);
// ////        System.out.println(es);
// ////        System.out.println("---------------------------");
// //    }
// //    @Test
// //    public void testDeleteByEvent() throws Exception{
// ////        List<String> planners = EventMapper.deletePlannerEventsByEvent(eId, conn);
// ////        System.out.println(planners);
// //    }
// //
// //    @Test
// //    public void testDeleteByPlanner() throws Exception{
// =======
//         List<String> plannerEmails = new ArrayList<>();
//         plannerEmails.add(pEmail);
//         plannerEmails.add("email!");

// //        EventMapper.createPlannerEvents(plannerEmails, eId, conn);
//         conn.commit();

// //        p.setEmail("p2.email");
// //        p.setRole(UserAccess.planner.name());
// //        p.setPassword("xxxxxxxxx");
// //
// //        EventMapper.createPlannerEvents(p, e, conn);
//     }

//     // @Test
//     public void testCheck() throws Exception{
//         Event e = (Event) MapperRegistry.getInstance().getMapper(Event.class).find(new Key(eId));
//         Planner p = new Planner();
//         p.setEmail(pEmail);
//         p.setRole(UserAccess.planner.name());
//         p.setPassword(pPass);

//         assertTrue(EventMapper.checkPlannerEvent(p, e, conn));

//         p.setEmail("not exist!");
//         assertFalse(EventMapper.checkPlannerEvent(p, e, conn));
//     }

//     // @Test
//     public void testView() throws Exception{

//         List<String> ps = EventMapper.viewPlannerByEvent(eId, conn);
//         System.out.println(ps);
//         System.out.println("---------------------------");

// //        List<UUID> es = EventMapper.viewEventsByPlanner(pEmail, conn);
// //        System.out.println(es);
// //        System.out.println("---------------------------");
//     }
//     // @Test
//     public void testDeleteByEvent() throws Exception{
// //        List<String> planners = EventMapper.deletePlannerEventsByEvent(eId, conn);
// //        System.out.println(planners);
//     }

//     // @Test
//     public void testDeleteByPlanner() throws Exception{
//         Planner p = new Planner();
//         p.setEmail(pEmail);
//         p.setRole(UserAccess.planner.name());
//         p.setPassword(pPass);

// //        List<UUID> event = EventMapper.deletePlannerEventsByPlanner(p, conn);
// //        System.out.println(event);
//     }

//     // @Test
//     public void testDelete() throws Exception{
// //        Event e = EventMapper.viewOneEvent(eId, conn);
// >>>>>>> main
// //        Planner p = new Planner();
// //        p.setEmail(pEmail);
// //        p.setRole(UserAccess.planner.name());
// //        p.setPassword(pPass);
// //
// ////        List<UUID> event = EventMapper.deletePlannerEventsByPlanner(p, conn);
// ////        System.out.println(event);
// //    }
// //
// //    @Test
// //    public void testDelete() throws Exception{
// ////        Event e = EventMapper.viewOneEvent(eId, conn);
// ////        Planner p = new Planner();
// ////        p.setEmail(pEmail);
// ////        p.setRole(UserAccess.planner.name());
// ////        p.setPassword(pPass);
// ////
// ////        assertTrue(EventMapper.deletePlannerEvents(p, e, conn));
// //        List<String> plannerEmails = new ArrayList<>();
// //        plannerEmails.add(pEmail);
// //        plannerEmails.add("email!");
// //
// ////        EventMapper.deletePlannerEmailsList(plannerEmails, conn);
// //        conn.commit();
// //    }
// //}
