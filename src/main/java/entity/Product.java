/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author hungddv
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Product {

    private String id; 
    private String name; 
    @Builder.Default 
    private String image = "product.png"; 
    private double unitPrice; 
    private double discount; 
    private int stock; 
    private String shelfId;
    private String categoryId;
    private boolean status;
    
}
