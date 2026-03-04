/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components;

/**
 *
 * @author hungddv
 */

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        // Phải có constructor rỗng để NetBeans tạo instance!
        setOpaque(false); // Cho phép nhìn thấy ảnh
        backgroundImage = new ImageIcon("src/main/resources/Icons/background.jpg").getImage(); // Đường dẫn ảnh
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
