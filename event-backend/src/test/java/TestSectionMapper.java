// <<<<<<< refineMapper
// //import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
// //import com.unimelb.swen90007.reactexampleapi.api.mappers.SectionMapper;
// //import com.unimelb.swen90007.reactexampleapi.api.mappers.VenueMapper;
// //import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
// //import org.junit.jupiter.api.Test;
// //
// //import java.sql.Connection;
// //import java.util.ArrayList;
// //import java.util.List;
// //import java.util.Map;
// //import java.util.UUID;
// //
// //public class TestSectionMapper {
// //
// //    List<Section> sections = new ArrayList<>();
// //    private Connection conn = DBUtil.connection();
// //    public static final String name1 = "new name2";
// //    public static final String add1 = "Melbourne";
// //    @Test
// //    public void testCreateSection() throws Exception{
// //        Section s1 = new Section();
// //
// //        UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
// //        s1.setVenueID(venueId);
// //        s1.setType("Stand");
// //        s1.setCapacity(500);
// //        sections.add(s1);
// //
// //        Section s2 = new Section();
// //        s2.setVenueID(venueId);
// //        s2.setType("VIP");
// //        s2.setCapacity(100);
// //        sections.add(s2);
// //
// //        this.sections = SectionMapper.createSections(sections, conn);
// //        System.out.println(sections);
// //    }
// //
// //    @Test
// //    public void testDeleteSection() throws Exception{
// //        String name2 = "new name2";
// //        String add2 = "Melbourne";
// //
// //        UUID id = VenueMapper.checkExistence(name2, add2, conn);
// //        SectionMapper.deleteSectionsByVenue(id, conn);
// //    }
// //    @Test
// //    public void testDeleteSectionList() throws Exception{
// //        List<Section> s = new ArrayList<>();
// //        Section s1 = new Section();
// //        UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
// //        s1.setVenueID(venueId);
// //        s1.setType("Stand");
// //        s1.setCapacity(500);
// //        s1.setId(UUID.fromString("98ab129b-6f5d-40ac-8718-59648fd05aa2"));
// //
// //        s.add(s1);
// //        System.out.println(s);
// //
// //        SectionMapper.deleteSectionsList(s, conn);
// //    }
// //
// //    @Test
// //    public void testViewSection() throws Exception{
// //        Map<UUID, Section> sections = SectionMapper.viewSectionAll(conn);
// //        System.out.println(sections);
// //
// //        UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
// //        List<Section> s = SectionMapper.viewSectionByVenue(venueId, conn);
// //        System.out.println(s);
// //    }
// //
// //    @Test
// //    public void testUpdateSection() throws Exception{
// //        List<Section> s = new ArrayList<>();
// //        Section s1 = new Section();
// //        s1.setVenueID(UUID.fromString("7e116edb-10ec-40bd-beda-e9e366b41325"));
// //        s1.setType("Stand");
// //        s1.setCapacity(500);
// //        s1.setId(UUID.fromString("fa9953c1-8adc-4e69-97a0-e7f30bad71e9"));
// //
// //        s.add(s1);
// //        System.out.println(s);
// //
// //        SectionMapper.updateSections(s, conn);
// //    }
// //
// //
// //}
// =======
// import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
// import com.unimelb.swen90007.reactexampleapi.api.mappers.SectionMapper;
// import com.unimelb.swen90007.reactexampleapi.api.mappers.VenueMapper;
// import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
// import org.junit.jupiter.api.Test;

// import java.sql.Connection;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;

// public class TestSectionMapper {

//     List<Section> sections = new ArrayList<>();
//     private Connection conn = DBUtil.connection();
//     public static final String name1 = "new name2";
//     public static final String add1 = "Melbourne";
//     // @Test
//     public void testCreateSection() throws Exception{
//         Section s1 = new Section();

//         UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
//         s1.setVenueID(venueId);
//         s1.setType("Stand");
//         s1.setCapacity(500);
//         sections.add(s1);

//         Section s2 = new Section();
//         s2.setVenueID(venueId);
//         s2.setType("VIP");
//         s2.setCapacity(100);
//         sections.add(s2);

//         this.sections = SectionMapper.createSections(sections, conn);
//         System.out.println(sections);
//     }

//     // @Test
//     public void testDeleteSection() throws Exception{
//         String name2 = "new name2";
//         String add2 = "Melbourne";

//         UUID id = VenueMapper.checkExistence(name2, add2, conn);
//         SectionMapper.deleteSectionsByVenue(id, conn);
//     }
//     // @Test
//     public void testDeleteSectionList() throws Exception{
//         List<Section> s = new ArrayList<>();
//         Section s1 = new Section();
//         UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
//         s1.setVenueID(venueId);
//         s1.setType("Stand");
//         s1.setCapacity(500);
//         s1.setId(UUID.fromString("98ab129b-6f5d-40ac-8718-59648fd05aa2"));

//         s.add(s1);
//         System.out.println(s);

//         SectionMapper.deleteSectionsList(s, conn);
//     }

//     // @Test
//     public void testViewSection() throws Exception{
//         Map<UUID, Section> sections = SectionMapper.viewSectionAll(conn);
//         System.out.println(sections);

//         UUID venueId = VenueMapper.checkExistence(name1, add1, conn);
//         List<Section> s = SectionMapper.viewSectionByVenue(venueId, conn);
//         System.out.println(s);
//     }

//     // @Test
//     public void testUpdateSection() throws Exception{
//         List<Section> s = new ArrayList<>();
//         Section s1 = new Section();
//         s1.setVenueID(UUID.fromString("7e116edb-10ec-40bd-beda-e9e366b41325"));
//         s1.setType("Stand");
//         s1.setCapacity(500);
//         s1.setId(UUID.fromString("fa9953c1-8adc-4e69-97a0-e7f30bad71e9"));

//         s.add(s1);
//         System.out.println(s);

//         SectionMapper.updateSections(s, conn);
//     }


// }
// >>>>>>> main
