/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.jpanel;

import dao.CategoryDAO;
import dao.RevenueDAO;
import dao.impl.CategoryDAOImpl;
import dao.impl.RevenueDAOImpl;
import entity.Category;
import entity.Revenue;
import entity.TotalRevenue;
import ui.controller.RevenueController;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import util.TimeRange;
import util.XDate;
import util.XQuery;
import util.XValidate;

/**
 *
 * @author ASUS Vivobook
 */
public class RevenueJPanel extends javax.swing.JPanel implements RevenueController {

    /**
     * Creates new form RevenueJPanel
     */
    public RevenueJPanel() {
        initComponents();
        this.open();
    }

    RevenueDAO dao = new RevenueDAOImpl();

    @Override
    public void open() {
        this.selectTimeRange();
        showTotalRevenue();
        this.fillCategories();
    }
    List<Category> categories = List.of();
    private void fillCategories() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboCategories.getModel();
        cboModel.removeAllElements();
        cboModel.addElement("");
        CategoryDAO cdao = new CategoryDAOImpl();
        categories = cdao.findAll2();
//        categories.forEach(category -> {
//            cboModel.addElement(category);
//            tblModel.addRow(new Object[]{category.getName()});
//        });
        categories.stream()
                .filter(category ->category.isStatus()) // nếu check == true thì lọc status == true
                .forEach(category -> {
                    cboModel.addElement(category);
                });
    }
    
    public void showTotalRevenue() {
        String sql = " SELECT CAST(SUM(unitprice * quantity) as float) AS total FROM BillDetails JOIN Bills ON Bills.id = BillDetails.BillId  WHERE Bills.status = 1 AND Bills.checkout IS NOT NULL";
        String sql2 = " SELECT CAST(SUM(BillDetails.unitprice * BillDetails.quantity * (1 - Products.discount / 100.0)) AS float) AS total FROM BillDetails JOIN Bills ON Bills.id = BillDetails.BillId JOIN Products ON Products.id = BillDetails.ProductId WHERE Bills.status = 1 AND Bills.checkout IS NOT NULL";
        
            TotalRevenue tr = XQuery.getSingleBean(TotalRevenue.class, sql);
            double total = tr != null && tr.getTotal() != null ? tr.getTotal() : 0;
            TotalRevenue tr2 = XQuery.getSingleBean(TotalRevenue.class, sql2);
            double total2 = tr2 != null && tr.getTotal() != null ? tr2.getTotal() : 0;
            System.out.println("" + total);
            lblDoanhThu.setText(String.format("%,.0f đ", total));
            lblGiamGia.setText(String.format("%,.0f đ", total - total2));
            lblLoiNhuan.setText(String.format("%,.0f đ", total2));
       

    }

    @Override
    public void selectTimeRange() {
        TimeRange range = TimeRange.today();
        switch (cboTimeRanges.getSelectedIndex()) {
            case 0 ->
                range = TimeRange.today();
            case 1 ->
                range = TimeRange.thisWeek();
            case 2 ->
                range = TimeRange.thisMonth();
            case 3 ->
                range = TimeRange.thisQuarter();
            case 4 ->
                range = TimeRange.thisYear();
        }
        txtBegin.setText(XDate.format(range.getBegin(), "MM/dd/yyyy"));
        txtEnd.setText(XDate.format(range.getEnd(), "MM/dd/yyyy"));
        this.fillRevenue();
    }

    @Override
    public void fillRevenue() {
        Date begin = XDate.parse(txtBegin.getText(), "MM/dd/yyyy");
        Date end = XDate.parse(txtEnd.getText(), "MM/dd/yyyy");
        if (!XValidate.isNothing(txtProductName.getText()) && cboCategories.getSelectedIndex() != 0) {
            this.fillRevenueByNameAndCategory(begin,end,txtProductName.getText() ,categories.get(cboCategories.getSelectedIndex()-1).getId());
        } else if (XValidate.isNothing(txtProductName.getText()) && cboCategories.getSelectedIndex() != 0) {
            this.fillRevenueByCategory(begin,end,categories.get(cboCategories.getSelectedIndex()-1).getId());
        } else if (!XValidate.isNothing(txtProductName.getText()) && cboCategories.getSelectedIndex() == 0) {
            this.fillRevenueByName(begin, end, txtProductName.getText());
        } else {
            this.fillRevenueByTao(begin, end);
        }
    }

    private void fillRevenueByTao(Date begin, Date end) {
        DefaultTableModel model = (DefaultTableModel) tblRevenue.getModel();
        List<Revenue.ByTao> items = dao.getByTao(begin, end);
        model.setRowCount(0);
        items.forEach(item -> {
            Object[] row = {
                item.getId(),
                item.getSdt(),
                item.getCategory(),
                item.getProductName(),
                XDate.format(item.getCheckin(), "hh:mm:ss dd-MM-yyyy"),
                XDate.format(item.getCheckout(), "hh:mm:ss dd-MM-yyyy"),
                item.getQuantity(),
                String.format("$%.2f", item.getRevenue())
            };
            model.addRow(row);
        });
    }

    private void fillRevenueByName(Date begin, Date end, String name) {
        DefaultTableModel model = (DefaultTableModel) tblRevenue.getModel();
        List<Revenue.ByTao> items = dao.getByName(begin, end, name);
        model.setRowCount(0);
        items.forEach(item -> {
            Object[] row = {
                item.getId(),
                item.getSdt(),
                item.getCategory(),
                item.getProductName(),
                XDate.format(item.getCheckin(), "hh:mm:ss dd-MM-yyyy"),
                XDate.format(item.getCheckout(), "hh:mm:ss dd-MM-yyyy"),
                item.getQuantity(),
                String.format("$%.2f", item.getRevenue())
            };
            model.addRow(row);
        });
    }
    
    private void fillRevenueByCategory(Date begin, Date end, String category) {
        DefaultTableModel model = (DefaultTableModel) tblRevenue.getModel();
        List<Revenue.ByTao> items = dao.getByCategory(begin, end, category);
        model.setRowCount(0);
        items.forEach(item -> {
            Object[] row = {
                item.getId(),
                item.getSdt(),
                item.getCategory(),
                item.getProductName(),
                XDate.format(item.getCheckin(), "hh:mm:ss dd-MM-yyyy"),
                XDate.format(item.getCheckout(), "hh:mm:ss dd-MM-yyyy"),
                item.getQuantity(),
                String.format("$%.2f", item.getRevenue())
            };
            model.addRow(row);
        });
    }
    
    private void fillRevenueByNameAndCategory(Date begin, Date end,String name, String category) {
        DefaultTableModel model = (DefaultTableModel) tblRevenue.getModel();
        List<Revenue.ByTao> items = dao.getByNameAndCategory(begin, end,name, category);
        model.setRowCount(0);
        items.forEach(item -> {
            Object[] row = {
                item.getId(),
                item.getSdt(),
                item.getCategory(),
                item.getProductName(),
                XDate.format(item.getCheckin(), "hh:mm:ss dd-MM-yyyy"),
                XDate.format(item.getCheckout(), "hh:mm:ss dd-MM-yyyy"),
                item.getQuantity(),
                String.format("$%.2f", item.getRevenue())
            };
            model.addRow(row);
        });
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRevenue = new javax.swing.JTable();
        menu1 = new ui.components.Menu();
        lblDoanhThu = new javax.swing.JLabel();
        menu2 = new ui.components.Menu();
        lblGiamGia = new javax.swing.JLabel();
        menu3 = new ui.components.Menu();
        lblLoiNhuan = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtBegin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEnd = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        cboTimeRanges = new javax.swing.JComboBox<>();
        cboCategories = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        jPanel1.setBackground(Color.decode("#F5F5F5"));

        tblRevenue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Sdt", "Category", "Product Name", "Checkin", "Checkout", "Quantity", "Revenue"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRevenue);

        lblDoanhThu.setBackground(new Color(0,0,0,0));
        lblDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblDoanhThu.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThu.setText("10000000000");
        lblDoanhThu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doanh thu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
        menu1.setLayout(menu1Layout);
        menu1Layout.setHorizontalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu1Layout.setVerticalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblGiamGia.setBackground(new Color(0,0,0,0));
        lblGiamGia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblGiamGia.setForeground(new java.awt.Color(255, 255, 255));
        lblGiamGia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGiamGia.setText("10000000000");
        lblGiamGia.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giảm giá", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout menu2Layout = new javax.swing.GroupLayout(menu2);
        menu2.setLayout(menu2Layout);
        menu2Layout.setHorizontalGroup(
            menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu2Layout.setVerticalGroup(
            menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblLoiNhuan.setBackground(new Color(0,0,0,0));
        lblLoiNhuan.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblLoiNhuan.setForeground(new java.awt.Color(255, 255, 255));
        lblLoiNhuan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoiNhuan.setText("10000000000");
        lblLoiNhuan.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lợi nhuận", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout menu3Layout = new javax.swing.GroupLayout(menu3);
        menu3.setLayout(menu3Layout);
        menu3Layout.setHorizontalGroup(
            menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLoiNhuan, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu3Layout.setVerticalGroup(
            menu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLoiNhuan, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("Từ ngày:");

        txtBegin.setText("01/01/2025");

        jLabel4.setText("Đến ngày:");

        txtEnd.setText("01/01/2026");
        txtEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndActionPerformed(evt);
            }
        });

        btnFilter.setText("Lọc");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tuần này", "Tháng này", "Quý này", "Năm nay" }));
        cboTimeRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeRangesActionPerformed(evt);
            }
        });

        cboCategories.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Loại sản phẩm:");

        txtProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductNameActionPerformed(evt);
            }
        });

        jLabel7.setText("Tên Sản phẩm:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(186, 186, 186)
                        .addComponent(btnFilter))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                        .addComponent(menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(149, 149, 149)
                        .addComponent(menu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                        .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFilter)
                        .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        this.fillRevenue();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void cboTimeRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeRangesActionPerformed
        // TODO add your handling code here:
        this.selectTimeRange();
    }//GEN-LAST:event_cboTimeRangesActionPerformed

    private void txtProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFilter;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboTimeRanges;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblLoiNhuan;
    private ui.components.Menu menu1;
    private ui.components.Menu menu2;
    private ui.components.Menu menu3;
    private javax.swing.JTable tblRevenue;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtEnd;
    private javax.swing.JTextField txtProductName;
    // End of variables declaration//GEN-END:variables

}
