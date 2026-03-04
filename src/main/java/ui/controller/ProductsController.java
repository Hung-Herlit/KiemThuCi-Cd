/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ui.controller;

import entity.Bill;

/**
 *
 * @author hacke
 */
public interface ProductsController {

    void setBill(Bill bill); // nhận bill từ BillJDialog

    void open(); // hiển thị loại và đồ uống

    void fillCategories(); // tải và hiển thị loại đồ uống

    void fillProducts(); // tải và hiển thị đồ uống

    void addProductToBill(); // thêm đồ uống vào bill
}