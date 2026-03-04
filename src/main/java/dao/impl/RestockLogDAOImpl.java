/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
/**
 *
 * @author hungddv
 */
package dao.impl;

import dao.RestockLogDAO;
import entity.RestockLog;
import java.util.List;
import util.XJdbc;
import util.XQuery;

public class RestockLogDAOImpl implements RestockLogDAO {

    String createSql = "INSERT INTO RestockLog (ProductId, Quantity, Timestamp, Sdt) VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE RestockLog SET ProductId=?, Quantity=?, Timestamp=?, Sdt=? WHERE Id=?";
    String deleteSql = "DELETE FROM RestockLog WHERE Id=?";
    String findAllSql = "SELECT * FROM RestockLog";
    String findByIdSql = "SELECT * FROM RestockLog WHERE Id=?";
    String addQuantity = "update products set stock = stock + ? "
            + " where id=?";

    @Override
    public RestockLog create(RestockLog entity) {
        Object[] args = {
            entity.getProductId(),
            entity.getQuantity(),
            entity.getTimestamp(),
            entity.getSdt(),};
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public RestockLog addQuantity(RestockLog entity) {
        Object[] args = {
            entity.getQuantity(),
            entity.getProductId(),};
        XJdbc.executeUpdate(addQuantity, args);
        return entity;
    }

    @Override
    public void update(RestockLog entity) {
        Object[] args = {
            entity.getProductId(),
            entity.getQuantity(),
            entity.getTimestamp(),
            entity.getSdt(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<RestockLog> findAll() {
        return XQuery.getBeanList(RestockLog.class, findAllSql);
    }

    @Override
    public RestockLog findById(Integer id) {
        return XQuery.getSingleBean(RestockLog.class, findByIdSql, id);
    }
}
