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
import java.awt.geom.RoundRectangle2D;

public class TextFBorder extends JTextField {
    private Shape shape;

    public TextFBorder(int size) {
        super(size);
        setOpaque(false); // Cho phép vẽ nền tùy chỉnh
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5); // Bo góc 20px
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.GRAY); // Màu viền
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 5, 5);
        }
        return shape.contains(x, y);
    }
}

