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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Bill {

    private int id; 
    private String sdt; 
    private String sdtCustomer; 
    @Builder.Default 
    private Date checkin = new Date(); 
    private Date checkout; 
    private int status;
    private int billDiscount;
    
}
