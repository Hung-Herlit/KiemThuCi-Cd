/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.UserDAO;
import entity.User;
import java.util.List;
import util.XJdbc;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class UserDAOImpl implements UserDAO {

    String createSql = "INSERT INTO Users (Sdt, Password, Status, Fullname, Photo, Role) VALUES (?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE Users SET Password=?, Status=?, Fullname=?, Photo=?, Role=? WHERE Sdt=?";
    String deleteSql = "UPDATE Users SET status=0 WHERE Sdt=?";
    String findAllSql = "SELECT * FROM Users";
    String findAllSql2 = "SELECT * FROM Users where status=1";
    String findByIdSql = "SELECT * FROM Users WHERE Sdt=?";

    @Override
    public User create(User entity) {
        Object[] args = {
            entity.getSdt(),
            entity.getPassword(),
            entity.isStatus(),
            entity.getFullname(),
            entity.getPhoto(),
            entity.getRole()
        };
        XJdbc.executeUpdate(createSql, args);
        return entity;
    }

    @Override
    public void update(User entity) {
        Object[] args = {
            entity.getPassword(),
            entity.isStatus(),
            entity.getFullname(),
            entity.getPhoto(),
            entity.getRole(),
            entity.getSdt()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

    @Override
    public void deleteById(String id) {
        System.out.println(""+id);
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<User> findAll() {
        return XQuery.getBeanList(User.class, findAllSql);
    }
    
    @Override
    public List<User> findAll2() {
        return XQuery.getBeanList(User.class, findAllSql2);
    }

    @Override
    public User findById(String id) {
        return XQuery.getSingleBean(User.class, findByIdSql, id);
    }
}

