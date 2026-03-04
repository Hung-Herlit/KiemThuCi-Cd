package dao.impl.test;

import dao.impl.CategoryDAOImpl;
import entity.Category;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class CategoryDAOImplTest {
    
    CategoryDAOImpl dao;
    String catId = "C01";

    @BeforeClass
    public void setUp() {
        dao = new CategoryDAOImpl();
    }

    // TC_C01
    @Test(priority = 1)
    public void testCreateCategory() {
        Category c = new Category();
        c.setId(catId);
        c.setName("Coffee");
        c.setStatus(true);
        
        dao.create(c);
        Category result = dao.findById(catId);
        Assert.assertEquals(result.getName(), "Coffee");
    }

    // TC_C02
    @Test(priority = 2)
    public void testUpdateCategoryName() {
        Category c = dao.findById(catId);
        c.setName("Tra Sua");
        dao.update(c);
        
        Category result = dao.findById(catId);
        Assert.assertEquals(result.getName(), "Tra Sua");
    }

    // TC_C03
    @Test(priority = 3)
    public void testFindAllCategories() {
        List<Category> list = dao.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    // TC_C04
    @Test(priority = 4)
    public void testFindAllActive() {
        List<Category> list = dao.findAll2(); // findAll2 chỉ lấy status=1
        for(Category c : list){
            Assert.assertTrue(c.isStatus(), "Danh sách này chỉ được chứa category đang active");
        }
    }

    // TC_C05
    @Test(priority = 5)
    public void testSoftDeleteCategory() {
        dao.deleteById(catId);
        Category c = dao.findById(catId);
        Assert.assertFalse(c.isStatus(), "Category status phải là false");
    }
}