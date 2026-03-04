/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.jpanel;

import dao.RestockLogDAO;
import dao.impl.RestockLogDAOImpl;
import entity.RestockLog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import ui.components.TextFBorder;
import ui.controller.RestockLogController;
import util.XAuth;
import util.XDate;
import util.XDialog;
import util.XValidate;

/**
 *
 * @author hacke
 */
public class RestockLogManagerJPanel extends javax.swing.JPanel implements RestockLogController {

    /**
     * Creates new form RestockLogManagerJPanel
     */
    public RestockLogManagerJPanel() {
        initComponents();
        this.open();
        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));// ví dụ: trắng
    }

    RestockLogDAO dao = new RestockLogDAOImpl();
    List<RestockLog> items = List.of();

    @Override
    public void open() {
        this.fillToTable();
        this.clear();
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblShelf.getModel();
        model.setRowCount(0);
        items = dao.findAll();
        items.forEach(item -> {
            Object[] rowData = {
                item.getId(),
                item.getQuantity(),
                XDate.format(item.getTimestamp(), "hh:mm:ss dd-MM-yyyy"),
                item.getSdt(),
                item.getProductId(),
                false
            };
            model.addRow(rowData);
        });
    }

    @Override
    public void edit() {
        RestockLog entity = items.get(tblShelf.getSelectedRow());
        this.setForm(entity);
        this.setEditable(true);
    }

    @Override
    public void checkAll() {
        this.setCheckedAll(true);
    }

    @Override
    public void uncheckAll() {
        this.setCheckedAll(false);
    }

    private void setCheckedAll(boolean checked) {
        for (int i = 0; i < tblShelf.getRowCount(); i++) {
            tblShelf.setValueAt(checked, i, 3);
        }
    }

    @Override
    public void deleteCheckedItems() {
        if (XDialog.confirm("Bạn thực sự muốn xóa các mục chọn?")) {
            for (int i = 0; i < tblShelf.getRowCount(); i++) {
                if ((Boolean) tblShelf.getValueAt(i, 3)) {
                    dao.deleteById(items.get(i).getId());
                }
            }
            this.fillToTable();
        }
    }

    @Override
    public boolean check() {
        if (XValidate.isNothing(txtId.getText())) {
            XDialog.alert("Không để trống ID");
            return false;
        } else if (XValidate.isNothing(txtQuantity.getText())) {
            XDialog.alert("Không để trống số lượng sản phẩm");
            return false;
        } else if (!XValidate.isInteger(txtQuantity.getText())
                || !XValidate.isInRange(Integer.parseInt(txtQuantity.getText()), 0, 999)) {
            XDialog.alert("Số lượng phải là số nguyên từ 0 đến 999");
            return false;
        }
        return true;
    }

    @Override
    public void setForm(RestockLog entity) {
        txtId.setText(String.valueOf(entity.getId()));
        txtQuantity.setText(String.valueOf(entity.getQuantity()));
        txtTimestamp.setText(sdf.format(entity.getTimestamp()));
        txtProductId.setText(entity.getProductId());
        txtSdt.setText(entity.getSdt());
    }
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    @Override
    public RestockLog getForm() {
        RestockLog entity = new RestockLog();
        try {
            entity.setQuantity(Integer.parseInt(txtQuantity.getText()));
            entity.setSdt(XAuth.user.getSdt());
            entity.setProductId(txtProductId.getText());
            entity.setTimestamp(sdf.parse(txtTimestamp.getText()));

        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public void create() {
        if (!this.check()) {
            return;
        }{
            RestockLog entity = this.getForm();
            dao.create(entity);
            dao.addQuantity(entity);
            this.fillToTable();
            this.clear();
            
        }

    }

    @Override
    public void update() {
        if (!this.check()) {
            return;
        }
        RestockLog entity = this.getForm();
        dao.update(entity);
        this.fillToTable();
    }

    @Override
    public void clear() {
        this.setForm(new RestockLog());
        this.setEditable(false);
    }

    @Override
    public void setEditable(boolean editable) {
        txtId.setEnabled(!editable);
        btnAdd.setEnabled(!editable);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblShelf = tblShelf = new javax.swing.JTable();
        tblShelf.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = new JLabel(value.toString());
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setForeground(Color.WHITE);

                label = new JLabel(value.toString()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        GradientPaint gp = new GradientPaint(0, 0, Color.decode("#333333"), 0, getHeight(), Color.decode("#999999"));
                        g2.setPaint(gp);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                };

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(false); // Cho phép vẽ gradient
                label.setForeground(Color.WHITE);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                return label;
            }
        });
        ;
        menu1 = new ui.components.Menu();
        jLabel6 = new javax.swing.JLabel();
        textField = new JTextField("Search...");
        textField.setForeground(Color.decode("#500073"));

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Search...")) {
                    textField.setText("");
                    textField.setForeground(Color.decode("#090040"));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.decode("#500073"));
                    textField.setText("Search...");
                }
            }
        });
        ;
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new TextFBorder(5);
        jLabel2 = new javax.swing.JLabel();
        txtQuantity = new TextFBorder(5);
        btnAdd = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        txtTimestamp = new TextFBorder(5);
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSdt = new TextFBorder(5);
        txtProductId = new TextFBorder(5);
        jLabel8 = new javax.swing.JLabel();

        jPanel1.setBackground(Color.decode("#F5F5F5")
        );
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBackground(new Color(0,0,0,0));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 350));

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(255, 51, 0));

        tblShelf.setBackground(new Color(0,0,0,0));
        tblShelf.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        tblShelf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0345223777", null, null, "tam19", "oke", null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Quantity", "Timestamp", "Sdt", "ProductId", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblShelf.setOpaque(false);
        tblShelf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShelfMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblShelf);

        textField.setBackground(new Color(0,0,0,0));
        textField.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        textField.setToolTipText("");
        textField.setBorder(null);

        jLabel7.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 3, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("QUẢN LÝ KỆ HÀNG");

        javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
        menu1.setLayout(menu1Layout);
        menu1Layout.setHorizontalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu1Layout.setVerticalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
            .addComponent(textField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2);

        jPanel3.setBackground(new Color(0,0,0,0));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(950, 280));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Id:");

        txtId.setEditable(false);
        txtId.setBackground(new Color(0,0,0,0));
        txtId.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Số lượng:");

        txtQuantity.setBackground(new Color(0,0,0,0));
        txtQuantity.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        btnAdd.setBackground(Color.decode("#28A745")
        );
        btnAdd.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add_ring_broken_line.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(Color.decode("#2980B9")
        );
        btnRefresh.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Refresh.png"))); // NOI18N
        btnRefresh.setText("Làm mới");
        btnRefresh.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        txtTimestamp.setEditable(false);
        txtTimestamp.setBackground(new Color(0,0,0,0));
        txtTimestamp.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Thời gian:");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Số điện thoại:");

        txtSdt.setBackground(new Color(0,0,0,0));
        txtSdt.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        txtProductId.setBackground(new Color(0,0,0,0));
        txtProductId.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mã sản phẩm:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(137, 137, 137)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(txtId)
                            .addComponent(txtProductId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(344, 344, 344))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnRefresh))
                .addContainerGap())
        );

        jPanel1.add(jPanel3);

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

    private void tblShelfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShelfMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.edit();
        }
    }//GEN-LAST:event_tblShelfMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        this.create();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRefresh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private ui.components.Menu menu1;
    private javax.swing.JTable tblShelf;
    private javax.swing.JTextField textField;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTimestamp;
    // End of variables declaration//GEN-END:variables
}
