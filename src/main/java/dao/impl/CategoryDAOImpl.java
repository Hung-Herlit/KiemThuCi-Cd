/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.CategoryDAO;
import entity.Category;
import java.util.List;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class CategoryDAOImpl implements CategoryDAO {

    String createSql = "INSERT INTO Categories(Id, Name, Status) VALUES(?, ?, ?)";
    String updateSql = "UPDATE Categories SET Name=?,Status=? WHERE Id=?";
    String deleteSql = "UPDATE Categories SET Status=0 WHERE Id=?";
    String findAllSql = "SELECT * FROM Categories";
    String findAllSql2 = "SELECT * FROM Categories where status=1";
    String findByIdSql = "SELECT * FROM Categories WHERE Id=?";

    @Override
    public Category create(Category entity) {
        Object[] values = {
            entity.getId(),
            entity.getName(),
            entity.isStatus()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(Category entity) {
        Object[] values = {
            entity.getName(),
            entity.isStatus(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Category> findAll() {
        return XQuery.getBeanList(Category.class, findAllSql);
        //old -- return XQuery.getEntityList(Category.class, findAllSql);
    }

    @Override
    public Category findById(String id) {
        return XQuery.getSingleBean(Category.class, findByIdSql, id);
    }
    @Override
    public List<Category> findAll2() {
        return XQuery.getBeanList(Category.class, findAllSql2);
        //old -- return XQuery.getEntityList(Category.class, findAllSql);
    }
}
