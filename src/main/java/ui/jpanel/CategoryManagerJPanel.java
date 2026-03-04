/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.jpanel;

import dao.CategoryDAO;
import dao.impl.CategoryDAOImpl;
import entity.Category;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import ui.components.TextFBorder;
import ui.controller.CategoryController;
import util.XDialog;
import util.XValidate;

/**
 *
 * @author hacke
 */
public class CategoryManagerJPanel extends javax.swing.JPanel implements CategoryController {

    /**
     * Creates new form CategoryManagerJPanel
     */
    private boolean check;

    public CategoryManagerJPanel() {
        check = true;
        initComponents();
        this.open();
        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));// ví dụ: trắng
    }

    CategoryDAO dao = new CategoryDAOImpl();
    List<Category> items = List.of();

    @Override
    public void open() {
        this.fillToTable();
        this.clear();
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblCategories.getModel();
        model.setRowCount(0);
        if (!check) {
            items = dao.findAll();
        }else items = dao.findAll2();
        items.stream()
                .filter(item -> !check || item.isStatus()) // nếu check == true thì lọc status == true
                .forEach(item -> {
                    Object[] rowData = {
                        item.getId(),
                        item.getName(),
                        item.isStatus() ? "Đang kinh doanh" : "Ngừng kinh doanh",
                        false
                    };
                    model.addRow(rowData);
                });
    }

    @Override
    public void edit() {
        Category entity = items.get(tblCategories.getSelectedRow());
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
        for (int i = 0; i < tblCategories.getRowCount(); i++) {
            tblCategories.setValueAt(checked, i, 3);
        }
    }

    @Override
    public void deleteCheckedItems() {
        try {
            if (XDialog.confirm("Bạn thực sự muốn xóa các mục chọn?")) {
                for (int i = 0; i < tblCategories.getRowCount(); i++) {
                    if ((Boolean) tblCategories.getValueAt(i, 3)) {
                        dao.deleteById(items.get(i).getId());
                    }
                }
                this.fillToTable();
            }
        } catch (Exception e) {
            XDialog.alert("Bạn chưa chọn mục nào");
        }

    }

    @Override
    public boolean check() {
        if (XValidate.isNothing(txtId.getText())) {
            XDialog.alert("Không để trống Mã loại sản phẩm");
            return false;
        } else if (XValidate.isNothing(txtName.getText())) {
            XDialog.alert("Không để trống Tên loại sản phẩm");
            return false;
        } else if (!XValidate.isValidLength(txtName.getText(), 3, 50)) {
            XDialog.alert("Tên loại sản phẩm phải từ 3 đến 50 ký tự");
            return false;
        }
        return true;
    }

    @Override
    public void setForm(Category entity) {
        txtId.setText(entity.getId());
        txtName.setText(entity.getName());
        if (entity.isStatus()) {
            rdoEnabled.setSelected(true);
        } else {
            rdoDisabled.setSelected(true);
        }
    }

    @Override
    public Category getForm() {
        Category entity = new Category();
        entity.setId(txtId.getText());
        entity.setName(txtName.getText());
        entity.setStatus(rdoEnabled.isSelected());
        return entity;
    }

    @Override
    public void create() {
        if (!this.check()) {
            return;
        }
        if (dao.findById((txtId.getText())) != null) {
            XDialog.alert("Mã loại sản phẩm đã tồn tại!");
        } else {
            Category entity = this.getForm();
            dao.create(entity);
            this.fillToTable();
            this.clear();
        }

    }

    @Override
    public void update() {
        if (!this.check()) {
            return;
        }
        Category entity = this.getForm();
        dao.update(entity);
        this.fillToTable();
    }

    @Override
    public void clear() {
        this.setForm(new Category());
        this.setEditable(false);
    }

    private void HideShow() {
        check = !check;
        this.fillToTable();
        btnCheckAll1.setText(check ? "Xem dữ liệu ngừng kinh doanh " : "Ẩn dữ liệu ngừng kinh doanh ");
    }

    @Override
    public void setEditable(boolean editable) {
        txtId.setEnabled(!editable);
        btnAdd.setEnabled(!editable);
        btnUpdate.setEnabled(editable);
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
        tblCategories = tblCategories = new javax.swing.JTable();
        tblCategories.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        txtName = new TextFBorder(5);
        jLabel5 = new javax.swing.JLabel();
        rdoEnabled = new javax.swing.JRadioButton();
        rdoDisabled = new javax.swing.JRadioButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnCheckAll = new javax.swing.JButton();
        btnUncheck = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCheckAll1 = new javax.swing.JButton();

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

        tblCategories.setBackground(new Color(0,0,0,0));
        tblCategories.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        tblCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0345223777", "tam19", "oke", null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Name", "Status", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCategories.setOpaque(false);
        tblCategories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCategoriesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCategories);

        textField.setBackground(new Color(0,0,0,0));
        textField.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        textField.setToolTipText("");
        textField.setBorder(null);

        jLabel7.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 3, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("QUẢN LÝ LOẠI SẢN PHẨM");

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
        jLabel1.setText("Mã loại sản phẩm");

        txtId.setBackground(new Color(0,0,0,0));
        txtId.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tên loại sản phẩm:");

        txtName.setBackground(new Color(0,0,0,0));
        txtName.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Trạng thái:");

        buttonGroup1.add(rdoEnabled);
        rdoEnabled.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        rdoEnabled.setSelected(true);
        rdoEnabled.setText("Đang kinh doanh");
        rdoEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEnabledActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoDisabled);
        rdoDisabled.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        rdoDisabled.setText("Ngừng kinh doanh");
        rdoDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDisabledActionPerformed(evt);
            }
        });

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

        btnUpdate.setBackground(Color.decode("#3498DB")
        );
        btnUpdate.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Edit.png"))); // NOI18N
        btnUpdate.setText("Cập nhật");
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
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

        btnCheckAll.setBackground(Color.decode("#1ABC9C")
        );
        btnCheckAll.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnCheckAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/check_square_broken_line.png"))); // NOI18N
        btnCheckAll.setText("Chọn hết");
        btnCheckAll.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAllActionPerformed(evt);
            }
        });

        btnUncheck.setBackground(Color.decode("#16A085")
        );
        btnUncheck.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnUncheck.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/uncheck_square_broken_line.png"))); // NOI18N
        btnUncheck.setText("Bỏ chọn");
        btnUncheck.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUncheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUncheckActionPerformed(evt);
            }
        });

        btnDelete.setBackground(Color.decode("#E74C3C")
        );
        btnDelete.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash.png"))); // NOI18N
        btnDelete.setText("Xóa chọn");
        btnDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnCheckAll1.setBackground(Color.decode("#9c27b0")
        );
        btnCheckAll1.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        btnCheckAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/View.png"))); // NOI18N
        btnCheckAll1.setText("Xem dữ liệu ngừng kinh doanh ");
        btnCheckAll1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCheckAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAll1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdoDisabled)
                            .addComponent(rdoEnabled))
                        .addGap(24, 300, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                        .addComponent(btnCheckAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUncheck, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCheckAll1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckAll1)
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoEnabled))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoDisabled)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnRefresh)
                    .addComponent(btnDelete)
                    .addComponent(btnUncheck)
                    .addComponent(btnCheckAll))
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

    private void tblCategoriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoriesMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.edit();
        }
    }//GEN-LAST:event_tblCategoriesMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        this.create();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnCheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAllActionPerformed
        // TODO add your handling code here:
        this.checkAll();
    }//GEN-LAST:event_btnCheckAllActionPerformed

    private void btnUncheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUncheckActionPerformed
        // TODO add your handling code here:
        this.uncheckAll();
    }//GEN-LAST:event_btnUncheckActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        this.deleteCheckedItems();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void rdoDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDisabledActionPerformed

    private void btnCheckAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAll1ActionPerformed
        // TODO add your handling code here:
        this.HideShow();
    }//GEN-LAST:event_btnCheckAll1ActionPerformed

    private void rdoEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEnabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoEnabledActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCheckAll;
    private javax.swing.JButton btnCheckAll1;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUncheck;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private ui.components.Menu menu1;
    private javax.swing.JRadioButton rdoDisabled;
    private javax.swing.JRadioButton rdoEnabled;
    private javax.swing.JTable tblCategories;
    private javax.swing.JTextField textField;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
