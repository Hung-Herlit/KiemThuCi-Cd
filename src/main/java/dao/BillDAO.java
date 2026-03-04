/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Bill;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface BillDAO extends CrudDAO<Bill, Integer>{ 
    public Bill findServicingByCardId(Integer billId);
}