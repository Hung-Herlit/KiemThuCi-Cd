package dao.impl.test;

import dao.impl.ProductDAOImpl;
import entity.Product;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class ProductDAOImplTest {
    
    ProductDAOImpl dao;
    String prodId = "P01";

    @BeforeClass
    public void setUp() {
        dao = new ProductDAOImpl();
    }

    // TC_P01
    @Test(priority = 1)
    public void testCreateProduct() {
        Product p = new Product();
        p.setId(prodId);
        p.setName("Den Da");
        p.setUnitPrice(10000.0);
        p.setCategoryId("C01"); // Đảm bảo C01 có trong DB hoặc tạo trước
        p.setStatus(true);
        
        dao.create(p);
        Product result = dao.findById(prodId);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getUnitPrice(), 10000.0);
    }

    // TC_P02
    @Test(priority = 2)
    public void testFindProductByName() {
        Product result = dao.findByName("Den Da");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), prodId);
    }

    // TC_P03
    @Test(priority = 3)
    public void testUpdateProductPrice() {
        Product p = dao.findById(prodId);
        p.setUnitPrice(20000.0);
        dao.update(p);
        
        Product result = dao.findById(prodId);
        Assert.assertEquals(result.getUnitPrice(), 20000.0);
    }

    // TC_P04
    @Test(priority = 4)
    public void testFindByCategory() {
        List<Product> list = dao.findByCategoryId("C01");
        boolean found = false;
        for(Product p : list) {
            if(p.getId().equals(prodId)) found = true;
        }
        Assert.assertTrue(found, "Phải tìm thấy sản phẩm trong danh mục C01");
    }

    // TC_P05
    @Test(priority = 5)
    public void testSoftDeleteProduct() {
        dao.deleteById(prodId);
        Product result = dao.findById(prodId);
        Assert.assertFalse(result.isStatus());
    }
}