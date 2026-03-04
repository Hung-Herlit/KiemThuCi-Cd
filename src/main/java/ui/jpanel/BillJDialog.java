/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.jpanel;

import dao.BillDAO;
import dao.BillDetailDAO;
import dao.impl.BillDAOImpl;
import dao.impl.BillDetailDAOImpl;
import entity.Bill;
import entity.BillDetail;
import entity.Product;
import java.awt.Frame;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import lombok.Setter;
import ui.controller.BillDetailController;
import dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import javax.swing.JDialog;
import util.XDate;
import util.XDialog;

/**
 *
 * @author hacke
 */
public class BillJDialog extends JDialog implements BillDetailController {

    /**
     * Creates new form BillJDialog
     */
    public BillJDialog() {
        initComponents();
    }

    public BillJDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @Setter
    Bill bill;
    BillDAO billDao = new BillDAOImpl();
    BillDetailDAO billDetailDao = new BillDetailDAOImpl();
    List<BillDetail> billDetails = List.of();

    void fillBillDetails() {
        billDetails = billDetailDao.findByBillId(bill.getId());
        DefaultTableModel model = (DefaultTableModel) tblBillDetails.getModel();
        model.setRowCount(0);

        billDetails.forEach(d -> {
            // Lấy product từ DAO
            ProductDAO productDao = new ProductDAOImpl(); // hoặc Inject qua constructor nếu dùng Spring
            Product p = productDao.findById(d.getProductId());

            double discount = (p != null) ? p.getDiscount() : 0.0;

            // Tính thành tiền
            Double amt = (d.getQuantity() * d.getUnitPrice()) * (1 - discount / 100);

            Object[] row = {
                false,
                d.getId(),
                d.getProductId(),
                String.format("%.0fvnd", d.getUnitPrice()),
                String.format("%.0f%%", discount),
                d.getQuantity(),
                String.format("%.0fvnd", amt)
            };
            model.addRow(row);
        });

        lblMoney.setText(String.format("Tổng: %.0f VNĐ", calculateTotalAmount()));
    }

    void setForm(Bill bill) { // hiển thị bill lên form
        while (String.valueOf(bill.getId()) == null) {
        }
        txtId.setText(String.valueOf(bill.getId()));
        txtCheckin.setText(XDate.format(bill.getCheckin(), "HH:mm:ss dd-MM-yyyy"));
        txtUsername.setText(bill.getSdt());
        String[] statuses = {"Servicing", "Completed", "Canceled"};
        txtStatus.setText(statuses[bill.getStatus()]);
        if (bill.getCheckout() != null) {
            txtCheckout.setText(XDate.format(bill.getCheckout(), "HH:mm:ss dd-MM-yyyy"));
        }
        boolean editable = (bill.getStatus() == 0);
        btnAdd.setEnabled(editable);
        btnCancel.setEnabled(editable);
        btnCheckout.setEnabled(editable);
        btnRemove.setEnabled(editable);
    }

    @Override
    public void removeDrinks() { // xóa đồ uống được tích chọn
        for (int i = 0; i < tblBillDetails.getRowCount(); i++) {
            Boolean checked = (Boolean) tblBillDetails.getValueAt(i, 0);
            if (checked) {
                billDetailDao.deleteById(billDetails.get(i).getId());
            }
        }
        this.fillBillDetails();
    }

    public float calculateTotalAmount() {
        float total = 0;
        DefaultTableModel model = (DefaultTableModel) tblBillDetails.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            Object value = model.getValueAt(row, 6);

            if (value != null) {
                String text = value.toString().replace("vnd", "").replace(",", "").trim();
                try {
                    total += Float.parseFloat(text);
                } catch (NumberFormatException e) {
                    // bỏ qua dòng lỗi định dạng
                }
            }
        }

        return total;
    }

    @Override
    public void showDrinkJDialog() { // hiển thị cửa sổ chọn và bổ sung đồ uống
        ProductJDialog dialog = new ProductJDialog((Frame) this.getOwner(), true);
        dialog.setBill(bill); // Khai báo vào DrinkJDialog @Setter Bill bill
        dialog.setVisible(true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                BillJDialog.this.fillBillDetails();
            }
        });
    }

    @Override
    public void updateQuantity() { // thay đổi số lượng đồ uống
        if (bill.getStatus() == 0) { // chưa thanh toán hoặc chưa bị canceled
            String input = XDialog.prompt("Số lượng mới?");
            if (input != null && input.length() > 0) {
                BillDetail detail = billDetails.get(tblBillDetails.getSelectedRow());
                detail.setQuantity(Integer.parseInt(input));
                billDetailDao.update(detail);
                this.fillBillDetails();
            }
        }
    }

    @Override
    public void checkout() {
        if (XDialog.confirm("Bạn muốn thanh toán phiếu bán hàng?")) {
            bill.setStatus(1);//0 là chưa thanh toán,1 là thanh toán,2 là hủy-- có thẻ dùng enum cho dễ hiểu
            bill.setCheckout(new Date());
            billDao.update(bill);
            this.setForm(bill);
            lblMoney.setText("Tổng tiền:  ");
        }
    }

    @Override
    public void cancel() {
        if (billDetails.isEmpty()) {
            billDao.deleteById(bill.getId());
            this.dispose();
        } else if (XDialog.confirm("Bạn muốn hủy phiếu bán hàng?")) {
            bill.setStatus(2);//0 là chưa thanh toán,1 là thanh toán,2 là hủy
            billDao.update(bill);
            this.setForm(bill);
        }
    }

    @Override
    public void open() {
        this.setLocationRelativeTo(null);
        this.setForm(bill);
        this.fillBillDetails();
    }

    @Override
    public void close() {
        if (billDetails.isEmpty()) {
            billDao.deleteById(bill.getId());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCheckin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCheckout = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillDetails = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        lblMoney = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Mã phiếu");

        txtId.setEditable(false);

        jLabel2.setText("Nhân viên");

        txtUsername.setEditable(false);

        jLabel4.setText("Trạng thái");

        txtStatus.setEditable(false);

        jLabel5.setText("Thời điểm đặt hàng");

        txtCheckin.setEditable(false);

        jLabel6.setText("Thời điểm thanh toán");

        txtCheckout.setEditable(false);
        txtCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(134, 134, 134)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblBillDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Mã phiếu", "Sản phẩm", "Đơn giá", "Giảm giá", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBillDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBillDetails);

        btnRemove.setText("Xóa sản phẩm");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAdd.setText("Thêm sản phẩm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy phiếu");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnCheckout.setText("Thanh toán");
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCheckout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemove)
                    .addComponent(btnAdd)
                    .addComponent(btnCancel)
                    .addComponent(btnCheckout))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMoney.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14)); // NOI18N
        lblMoney.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblMoney.setText("Tổng tiền : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMoney, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMoney)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCheckoutActionPerformed

    private void tblBillDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillDetailsMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.updateQuantity();
        }
    }//GEN-LAST:event_tblBillDetailsMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        this.removeDrinks();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        this.showDrinkJDialog();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.cancel();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        // TODO add your handling code here:
        this.checkout();
    }//GEN-LAST:event_btnCheckoutActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.open();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BillJDialog dialog = new BillJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMoney;
    private javax.swing.JTable tblBillDetails;
    private javax.swing.JTextField txtCheckin;
    private javax.swing.JTextField txtCheckout;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
