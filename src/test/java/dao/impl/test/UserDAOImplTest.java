package dao.impl.test;

import dao.impl.UserDAOImpl;
import entity.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class UserDAOImplTest {
    
    UserDAOImpl userDAO;
    String testSdt = "0901234567";

    @BeforeClass
    public void setUp() {
        userDAO = new UserDAOImpl();
    }

    // TC_U01
    @Test(priority = 1)
    public void testCreateNewUser() {
        User u = new User();
        u.setSdt(testSdt);
        u.setPassword("123");
        u.setFullname("User One");
        u.setRole(1);
        u.setStatus(true);
        
        userDAO.create(u);
        User created = userDAO.findById(testSdt);
        
        Assert.assertNotNull(created, "User tạo ra không được null");
        Assert.assertEquals(created.getFullname(), "User One");
    }

    // TC_U02
    @Test(priority = 2)
    public void testFindUserByValidID() {
        User u = userDAO.findById(testSdt);
        Assert.assertNotNull(u);
        Assert.assertEquals(u.getSdt(), testSdt);
    }

    // TC_U03
    @Test(priority = 3)
    public void testUpdateUserPassword() {
        User u = userDAO.findById(testSdt);
        u.setPassword("999");
        userDAO.update(u);
        
        User updated = userDAO.findById(testSdt);
        Assert.assertEquals(updated.getPassword(), "999");
    }

    // TC_U04
    @Test(priority = 4)
    public void testSoftDeleteUser() {
        userDAO.deleteById(testSdt);
        User u = userDAO.findById(testSdt);
        // Code DAO set status=0
        Assert.assertFalse(u.isStatus(), "Status user phải là false sau khi xóa");
    }

    // TC_U05
    @Test(priority = 5)
    public void testFindAllUsers() {
        List<User> list = userDAO.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0, "Danh sách user không được rỗng");
    }
}