import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.util.DBUtil;
import com.unimelb.swen90007.reactexampleapi.api.mappers.UserMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.*;
import com.unimelb.swen90007.reactexampleapi.api.util.UnitOfWork;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserMapper {
    Connection conn = DBUtil.connection();
    String email = "p.email.com";
    // @Test
    public void testCreate() throws Exception{
//        UUID id = UUID.randomUUID();
//        String stat = "INSERT INTO refresh_token (token_id, username) " +
//                "VALUES (?, ?)" +
//                "RETURNING id;";
//
//        PreparedStatement s = conn.prepareStatement(stat);
//        s.setString(1,"123456789");
//        s.setString(2, "adminUser");
//        s.executeQuery();

//        Customer c = new Customer();
//        c.setEmail("c.email.com");
//        c.setPassword("111111");
//        c.setRole(UserAccess.customer.name());
//        User u = UserMapper.createUser(c, conn);
//        System.out.println(u);
//
//        // Test planner
//        Planner p = new Planner();
//        p.setEmail(email);
//        p.setPassword("222222");
//        p.setRole(UserAccess.planner.name());
//        assertTrue(UserMapper.createPlanner(p, conn));

        // Test planner
        Admin a = new Admin();
        a.setEmail("admin.email");
        a.setPassword("222222");
        a.setRole(UserAccess.admin.name());
//        UserMapper.createUser(a, conn);
        conn.commit();
    }
    // @Test
    public void testView() throws Exception{
        User user = new User();
        Object[] key = {email};
        user = (User) MapperRegistry.getInstance().getMapper(User.class).find(new Key(key));
        System.out.println(user);
        System.out.println("-----------------------");

//        user =  UserMapper.viewOneByEmail(email, UserAccess.customer, conn);
//        System.out.println(user);
//        System.out.println("-----------------------");
        UserMapper userMapper = MapperRegistry.getInstance().getMapper(User.class);
        List<User> users = userMapper.viewAll();
        System.out.println(users);
        System.out.println("-----------------------");
    }
    // @Test
    public void testUpdate() throws Exception{
        Planner p = new Planner();
        p.setEmail(email);
        p.setPassword("123456789");
        p.setRole(UserAccess.planner.name());

        Object[] key = {email};
        MapperRegistry.getInstance().getMapper(User.class).delete(new Key(key), conn);

//        UserMapper.updateUser(p, conn);
    }

    // @Test
    public void testDelete() throws Exception{
//        assertTrue(UserMapper.deleteUser("c.email.com", UserAccess.customer, conn));
        Object[] key = {email};
        User u = (User) MapperRegistry.getInstance().getMapper(User.class).find(new Key(key));
        UnitOfWork.newCurrent();
        u.markRemoved();
        UnitOfWork.getCurrent().commit();
//        MapperRegistry.getInstance().getMapper(User.class).delete(new Key(key), conn);
    }
}
