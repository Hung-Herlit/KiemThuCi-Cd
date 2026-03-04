/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Category;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface CategoryDAO extends CrudDAO<Category, String>{
        List<Category> findAll2();
}