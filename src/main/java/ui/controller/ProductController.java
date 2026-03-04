/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ui.controller;

import entity.Product;

/**
 *
 * @author ASUS Vivobook
 */
public interface ProductController extends CrudController<Product>{
    void fillCategories();
}