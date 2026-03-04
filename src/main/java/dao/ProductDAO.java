/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Product;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface ProductDAO extends CrudDAO<Product, String>{
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByCategoryId2(String categoryId);
    Product findByName(String name);
}