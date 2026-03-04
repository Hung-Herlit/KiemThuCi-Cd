/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.ProductDAO;
import entity.Product;
import java.util.List;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class ProductDAOImpl implements ProductDAO {

    String createSql = "INSERT INTO Products (Id, Name, UnitPrice, Discount, Image, ShelfId, Stock, CategoryId,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE Products SET Name=?, UnitPrice=?, Discount=?, Image=?, Stock=?, CategoryId=?, ShelfId=?, status=? WHERE Id=?";
    String deleteSql = "UPDATE Products SET status=0 WHERE Id=?";
    String findAllSql = "SELECT * FROM Products";
    String findByIdSql = "SELECT * FROM Products WHERE Id=?";
    String findByNameSql = "SELECT * FROM Products WHERE Name=?";
    String findByCategorySql = "SELECT * FROM Products WHERE CategoryId=?";
    String findByCategorySql2 = "SELECT * FROM Products "
            + " inner join categories on Categories.id=CategoryId"
            + " WHERE CategoryId=? and Categories.status=1";

    @Override
    public Product create(Product entity) {
        Object[] args = {
            entity.getId(),
            entity.getName(),
            entity.getUnitPrice(),
            entity.getDiscount(),
            entity.getImage(),
            entity.getShelfId(),
            entity.getStock(),
            entity.getCategoryId(),
            entity.isStatus()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(Product entity) {
        Object[] args = {
            entity.getName(),
            entity.getUnitPrice(),
            entity.getDiscount(),
            entity.getImage(),
            entity.getStock(),
            entity.getCategoryId(),
            entity.getShelfId(),
            entity.isStatus(),
            entity.getId()
        };
        for (Object arg : args) {
            System.out.print(" " + arg);
        }
        System.out.println("");
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Product> findAll() {
        return XQuery.getBeanList(Product.class, findAllSql);
    }

    @Override
    public Product findById(String id) {
        return XQuery.getSingleBean(Product.class, findByIdSql, id);
    }

    @Override
    public Product findByName(String name) {
        return XQuery.getSingleBean(Product.class, findByNameSql, name);
    }

    @Override
    public List<Product> findByCategoryId(String categoryId) {
        return XQuery.getBeanList(Product.class, findByCategorySql, categoryId);
    }
    
    @Override
    public List<Product> findByCategoryId2(String categoryId) {
        return XQuery.getBeanList(Product.class, findByCategorySql2, categoryId);
    }
}
