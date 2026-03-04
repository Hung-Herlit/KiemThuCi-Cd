/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import ui.jpanel.UserManagerJPanel;
import util.XDialog;

/**
 *
 * @author hungddv
 */
interface MainController {

    void init();

    void logout();

    default void exit() {
        if (XDialog.confirm("Bạn muốn kết thúc?")) {
            System.exit(0);
        }
    }

    default void showJDialog(JDialog dialog) {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showWelcomeJDialog(JFrame frame) {
        this.showJDialog(new Loading(frame, true));
    }

    default void showLoginJDialog(JFrame frame) {
        this.showJDialog(new Login(frame, true));
    }

    void setFormWithFocus(JPanel panel);
}
