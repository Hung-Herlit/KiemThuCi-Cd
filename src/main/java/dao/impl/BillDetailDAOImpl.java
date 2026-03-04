/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.BillDetailDAO;
import entity.BillDetail;
import java.util.List;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class BillDetailDAOImpl implements BillDetailDAO {

    String createSql = "INSERT INTO BillDetails(BillId, ProductId, UnitPrice, Quantity) VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE BillDetails SET BillId=?, ProductId=?, UnitPrice=?, Quantity=? WHERE Id=?";
    String deleteSql = "DELETE FROM BillDetails WHERE Id=?";

    String findAllSql = "SELECT bd.*, p.name AS productName FROM BillDetails bd JOIN Products p ON p.Id=bd.ProductId";
    String findByIdSql = "SELECT bd.*, p.name AS productName FROM BillDetails bd JOIN Products p ON p.Id=bd.ProductId WHERE bd.Id=?";
    String findByBillIdSql = "SELECT bd.*, p.name AS productName FROM BillDetails bd JOIN Products p ON p.Id=bd.ProductId WHERE bd.BillId=?";
    String findByProductIdSql = "SELECT bd.*, p.name AS productName FROM BillDetails bd JOIN Products p ON p.Id=bd.ProductId WHERE bd.ProductId=?";

    @Override
    public BillDetail create(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getProductId(),
            entity.getUnitPrice(),
            entity.getQuantity()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getProductId(),
            entity.getUnitPrice(),
            entity.getQuantity(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<BillDetail> findAll() {
        return XQuery.getBeanList(BillDetail.class, findAllSql);
    }

    @Override
    public BillDetail findById(Integer id) {
        return XQuery.getSingleBean(BillDetail.class, findByIdSql, id);
    }

    @Override
    public List<BillDetail> findByBillId(Integer billId) {
        return XQuery.getBeanList(BillDetail.class, findByBillIdSql, billId);
    }

    @Override
    public List<BillDetail> findByProductId(String drinkId) {
        return XQuery.getBeanList(BillDetail.class, findByProductIdSql, drinkId);
    }
}
