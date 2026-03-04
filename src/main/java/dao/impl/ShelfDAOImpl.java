/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.ShelfDAO;
import entity.Shelf;
import java.util.List;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class ShelfDAOImpl implements ShelfDAO{
    String createSql = "INSERT INTO Shelf (Id, Name, Status) VALUES (?, ?, ?)";
    String updateSql = "UPDATE Shelf SET Name=?,status=? WHERE Id=?";
    String deleteSql = "UPDATE Shelf SET Status=0 WHERE Id=?";
    String findAllSql = "SELECT * FROM Shelf";
    String findAllSql2 = "SELECT * FROM Shelf where status=1";
    String findByIdSql = "SELECT * FROM Shelf WHERE Id=?";

    @Override
    public Shelf create(Shelf entity) {
        Object[] args = {
            entity.getId(),
            entity.getName(),
            entity.isStatus()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(Shelf entity) {
        Object[] args = {
            entity.getName(),
            entity.isStatus(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Shelf> findAll() {
        return XQuery.getBeanList(Shelf.class, findAllSql);
    }
    
    @Override
    public List<Shelf> findAll2() {
        return XQuery.getBeanList(Shelf.class, findAllSql2);
    }

    @Override
    public Shelf findById(String id) {
        return XQuery.getSingleBean(Shelf.class, findByIdSql, id);
    }
    
}
