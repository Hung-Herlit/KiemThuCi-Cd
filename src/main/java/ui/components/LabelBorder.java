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


public class LabelBorder extends JLabel {
    private int arc = 20;

    public LabelBorder() {
        super("Rounded");
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        super.paintComponent(g);
        g2.dispose();
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    public int getArc() {
        return arc;
    }
}
