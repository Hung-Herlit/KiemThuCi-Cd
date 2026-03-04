/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author hungddv
 */
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Revenue {
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ByTao {

        private int id; 
        private String sdt;
        private String category;
        private String productName;
        private Date checkin; 
        private Date checkout; 
        private int quantity; 
        private double revenue; // Doanh thu
    }
}
