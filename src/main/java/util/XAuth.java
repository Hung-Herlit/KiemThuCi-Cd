/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import entity.User;


/**
 *
 * @author Thump
 */
public class XAuth {

    public static User user = User.builder()
            .sdt("user1@gmail.com")
            .password("123")
            .status(true)
            .role(0)
            .fullname("Nguyễn Văn Tèo")
            .photo("trump.png")
            .build(); // biến user này sẽ được thay thế sau khi đăng nhập
    
}
