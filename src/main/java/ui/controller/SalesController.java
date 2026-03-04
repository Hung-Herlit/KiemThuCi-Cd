/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ui.controller;

import entity.Bill;


/**
 *
 * @author hungddv
 */
public interface SalesController extends CrudController<Bill> {

    void open(); // Xử lý mở cửa sổ

    void setForm(Bill entity); // Hiển thị thực thể lên form

    Bill getForm(); // Tạo thực thể từ dữ liệu form

    void fillToTable(); // Tải dữ liệu và đổ lên bảng

}
