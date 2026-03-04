/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.BillDAO;
import entity.Bill;
import java.util.Date;
import java.util.List;
import util.XAuth;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class BillDAOImpl implements BillDAO {

    String createSql = "INSERT INTO Bills(Sdt, Checkin, Checkout, Status) VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE Bills SET Sdt=?, SdtCustomer=?, Checkin=?, Checkout=?, Status=? WHERE Id=?";
    String deleteSql = "DELETE FROM Bills WHERE Id=?";
    String findAllSql = "SELECT * FROM Bills";
    String findByIdSql = "SELECT * FROM Bills WHERE Id=?";


    @Override
    public Bill create(Bill entity) {
        Object[] values = {
            entity.getSdt(),
            entity.getCheckin(),
            entity.getCheckout(),
            entity.getStatus()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(Bill entity) {
        Object[] values = {
            entity.getSdt(),
            entity.getSdtCustomer(),
            entity.getCheckin(),
            entity.getCheckout(),
            entity.getStatus(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public Bill findServicingByCardId(Integer cardId) {
        String sql = "SELECT * FROM Bills WHERE Id=?";
        Bill bill = XQuery.getSingleBean(Bill.class, sql, cardId);
        if (bill == null) { // không tìm thấy -> tạo mới
            Bill newBill = new Bill();
            newBill.setCheckin(new Date());
            newBill.setStatus(0); // đang phục vụ
            newBill.setSdt(XAuth.user.getSdt());
            this.create(newBill); // insert
            bill = XQuery.getSingleBean(Bill.class, sql, cardId);
        }
        return bill;
    }
    
    @Override
    public List<Bill> findAll() {
        return XQuery.getBeanList(Bill.class, findAllSql);
    }

    @Override
    public Bill findById(Integer id) {
        return XQuery.getSingleBean(Bill.class, findByIdSql, id);
    }
}
