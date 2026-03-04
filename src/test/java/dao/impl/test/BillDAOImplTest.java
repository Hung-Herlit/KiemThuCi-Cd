package dao.impl.test;

import dao.impl.BillDAOImpl;
import entity.Bill;
import util.XAuth;
import entity.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Date;

public class BillDAOImplTest {
    
    BillDAOImpl dao;
    Integer createdBillId;

    @BeforeClass
    public void setUp() {
        dao = new BillDAOImpl();
        // Mock user đăng nhập để tránh lỗi NullPointer
        User u = new User();
        u.setSdt("0901234567");
        XAuth.user = u;
    }

    // TC_B01
    @Test(priority = 1)
    public void testCreateBill() {
        Bill b = new Bill();
        b.setSdt("0901234567");
        b.setCheckin(new Date());
        b.setStatus(0);
        
        dao.create(b);
        // Do ID tự tăng, ta lấy bill mới nhất hoặc giả lập logic tìm kiếm
        // Ở đây giả sử ta lấy list ra và check cái cuối cùng hoặc logic tương đương
        List<Bill> list = dao.findAll();
        Bill lastBill = list.get(list.size() - 1);
        createdBillId = lastBill.getId();
        
        Assert.assertNotNull(createdBillId);
    }

    // TC_B02
    @Test(priority = 2)
    public void testAutoCreateServicingBill() {
        // ID thẻ bàn giả lập
        Integer cardId = 999; 
        Bill b = dao.findServicingByCardId(cardId);
        
        Assert.assertNotNull(b);
        Assert.assertEquals(b.getStatus(), 0);
        // Lưu lại ID này để cleanup nếu cần
    }

    // TC_B03
    @Test(priority = 3)
    public void testUpdateBillStatus() {
        Bill b = dao.findById(createdBillId);
        b.setStatus(1); // Đã thanh toán
        dao.update(b);
        
        Bill updated = dao.findById(createdBillId);
        Assert.assertEquals(updated.getStatus(), 1);
    }

    // TC_B04
    @Test(priority = 4)
    public void testFindBillById() {
        Bill b = dao.findById(createdBillId);
        Assert.assertNotNull(b);
        Assert.assertEquals(b.getId(), createdBillId);
    }

    // TC_B05
    @Test(priority = 5)
    public void testFindAllBills() {
        List<Bill> list = dao.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }
}