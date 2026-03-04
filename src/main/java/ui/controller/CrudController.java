/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ui.controller;

/**
 *
 * @author hungddv
 */
public interface CrudController<Entity> {

    void open(); // Xử lý mở cửa sổ

    void setForm(Entity entity); // Hiển thị thực thể lên form

    Entity getForm(); // Tạo thực thể từ dữ liệu form

    void fillToTable(); // Tải dữ liệu và đổ lên bảng

    void edit(); // Hiển thị dữ liệu của hàng được chọn lên form

    void create(); // Tạo thực thể mới

    void update(); // Cập nhật thực thể đang xem

    boolean check(); // Xóa thực thể đang xem

    void clear(); // Xóa trắng form

    void setEditable(boolean editable); // Thay đổi trạng thái form

    void checkAll(); // Tích chọn tất cả các hàng trên bảng

    void uncheckAll(); // Bỏ tích chọn tất cả các hàng trên bảng

    void deleteCheckedItems(); // Xóa các thực thể được tích chọn
}
