package dao.impl.test;
import dao.impl.BillDetailDAOImpl;
import entity.BillDetail;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class BillDetailDAOImplTest {
    
    BillDetailDAOImpl dao;
    int detailId;
    Integer billId = 1; // Giả sử bill ID 1 đã tồn tại
    String prodId = "P01"; // Giả sử P01 đã tồn tại

    @BeforeClass
    public void setUp() {
        dao = new BillDetailDAOImpl();
    }

    // TC_BD01
    @Test(priority = 1)
    public void testAddDetailToBill() {
        BillDetail bd = new BillDetail();
        bd.setBillId(billId);
        bd.setProductId(prodId);
        bd.setQuantity(2);
        bd.setUnitPrice(10000.0);
        
        dao.create(bd);
        
        // Tìm lại để lấy ID tự tăng
        List<BillDetail> list = dao.findByBillId(billId);
        BillDetail last = list.get(list.size()-1);
        detailId = last.getId();
        
        Assert.assertEquals(last.getQuantity(), 2);
    }

    // TC_BD02
    @Test(priority = 2)
    public void testFindByBillId() {
        List<BillDetail> list = dao.findByBillId(billId);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    // TC_BD03
    @Test(priority = 3)
    public void testUpdateQuantity() {
        BillDetail bd = dao.findById(detailId);
        bd.setQuantity(5);
        dao.update(bd);
        
        BillDetail updated = dao.findById(detailId);
        Assert.assertEquals(updated.getQuantity(), 5);
    }

    // TC_BD04
    @Test(priority = 4)
    public void testFindByProductId() {
        List<BillDetail> list = dao.findByProductId(prodId);
        Assert.assertNotNull(list);
        boolean found = false;
        for(BillDetail bd : list) {
            if(bd.getId()==detailId) found = true;
        }
        Assert.assertTrue(found);
    }

    // TC_BD05
    @Test(priority = 5)
    public void testDeleteDetail() {
        dao.deleteById(detailId);
        BillDetail bd = dao.findById(detailId);
        Assert.assertNull(bd, "Sau khi xóa, tìm kiếm phải trả về null");
    }
}