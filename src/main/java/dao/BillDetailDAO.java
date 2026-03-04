/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.BillDetail;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface BillDetailDAO extends CrudDAO<BillDetail, Integer> {
    List<BillDetail> findByBillId(Integer billId);
    List<BillDetail> findByProductId(String productId);
}
