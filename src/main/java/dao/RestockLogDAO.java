/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import entity.RestockLog;

/**
 *
 * @author hungddv
 */
public interface RestockLogDAO extends CrudDAO<RestockLog, Integer>{
    RestockLog addQuantity(RestockLog entity);
}
