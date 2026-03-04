/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Shelf;
import java.util.List;

/**
 *
 * @author hungddv
 */
public interface ShelfDAO extends CrudDAO<Shelf, String>{
    List<Shelf> findAll2();
}
