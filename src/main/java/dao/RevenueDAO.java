/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Revenue;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface RevenueDAO {

    /**
     * Truy vấn doanh thu từng loại theo khoảng thời gian
     *
     * @param begin thời gian bắt đầu
     * @param end thời gian kết thúc
     * @return kết quả truy vấn
     */
    List<Revenue.ByTao> getByCategory(Date begin, Date end, String categoryId);

    /**
     * Truy vấn doanh thu từng nhân viên theo khoảng thời gian
     *
     * @param begin thời gian bắt đầu
     * @param end thời gian kết thúc
     * @return kết quả truy vấn
     */
    List<Revenue.ByTao> getByName(Date begin, Date end, String productName);
    
    List<Revenue.ByTao> getByNameAndCategory(Date begin, Date end, String productName, String categoryId);
    
    List<Revenue.ByTao> getByTao(Date begin, Date end);
}
