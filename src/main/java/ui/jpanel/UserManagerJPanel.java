/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.jpanel;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import entity.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import ui.components.TextFBorder;
import ui.controller.UserController;
import util.XDialog;
import util.XIcon;
import util.XValidate;

/**
 *
 * @author hungddv
 */
public class UserManagerJPanel extends javax.swing.JPanel implements UserController {

    /**
     * Creates new form UserManagerJPanel
     */
    private boolean check;

    public UserManagerJPanel() {
        check = true;
        initComponents();
        this.open();
        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));// ví dụ: trắng
    }

    UserDAO dao = new UserDAOImpl();
    List<User> items = List.of();

    @Override
    public void open() {
        this.fillToTable();
        this.clear();
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0);
        if (!check) {
            items = dao.findAll();
        }else{
            items = dao.findAll2();
        }
        items.stream()
                .filter(item -> !check || item.isStatus()) // nếu check == true thì lọc status == true
                .forEach(item -> {
                    Object[] rowData = {
                        item.getSdt(),
                        item.getPassword(),
                        item.getFullname(),
                        item.getPhoto(),
                        switch (item.getRole()) {
                            case 1 ->
                                "Quản lý";
                            case 2 ->
                                "Khách hàng";
                            default ->
                                "Nhân viên";
                        },
                        item.isStatus() ? "Hoạt động" : "Tạm dừng",
                        false
                    };
                    model.addRow(rowData);
                });
    }

    @Override
    public void edit() {
        User entity = items.get(tblUsers.getSelectedRow());
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
        for (int i = 0; i < tblUsers.getRowCount(); i++) {
            tblUsers.setValueAt(checked, i, 6);
        }
    }

    @Override
    public void deleteCheckedItems() {
        if (XDialog.confirm("Bạn thực sự muốn xóa các mục chọn?")) {
            for (int i = 0; i < tblUsers.getRowCount(); i++) {
                if ((Boolean) tblUsers.getValueAt(i, 6)) {
                    System.out.println("" + items.get(i).getSdt());
                    dao.deleteById(items.get(i).getSdt());
                }
            }
            this.fillToTable();
        }
    }

    @Override
    public void setForm(User entity) {
        txtSdt.setText(entity.getSdt());
        txtPassword.setText(entity.getPassword());
        txtFullname.setText(entity.getFullname());
        cboRole.setSelectedIndex(entity.getRole());
        lblImage.setToolTipText(entity.getPhoto());

        // Trạng thái (enabled)
        if (entity.isStatus()) {
            rdoEnabled.setSelected(true);
        } else {
            rdoDisabled.setSelected(true);
        }

        XIcon.setIcon(lblImage, new File("images", entity.getPhoto())); // hiển thị hình từ file
    }

    @Override
    public User getForm() {
        User entity = new User();
        entity.setSdt(txtSdt.getText());
        entity.setPassword(txtPassword.getText());
        entity.setFullname(txtFullname.getText());
        entity.setRole(cboRole.getSelectedIndex());
        entity.setPhoto(lblImage.getToolTipText());
        entity.setStatus(rdoEnabled.isSelected());

        return entity;
    }

    @Override
    public void create() {
        if (!this.check()) {
            return;
        }
        if (dao.findById((txtSdt.getText())) != null) {
            XDialog.alert("So dien thoai đã tồn tại!");
        } else {
            User entity = this.getForm();
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
        User entity = this.getForm();
        dao.update(entity);
        this.fillToTable();
    }

    @Override
    public boolean check() {
        if (XValidate.isNothing(txtSdt.getText())) {
            XDialog.alert("Không để trống Số điện thoại");
            return false;
        } else if (!XValidate.isValidPhoneNumber(txtSdt.getText())
                || !XValidate.isValidLength(txtSdt.getText(), 10, 12)) {
            XDialog.alert("Định dạng số điện thoại không đúng");
            return false;
        } else if (XValidate.isNothing(txtPassword.getText())) {
            XDialog.alert("Không để trống Mật khẩu");
            return false;
        } else if (!XValidate.isValidPassword(txtPassword.getText())
                || !XValidate.isValidLength(txtPassword.getText(), 6, 40)) {
            XDialog.alert("Mật khẩu tối thiểu 6 tối đa 40 ký tự, ít nhất 1 số, 1 chữ");
            return false;
        } else if (XValidate.isNothing(txtFullname.getText())) {
            XDialog.alert("Không để trống Họ và tên");
            return false;
        } else if (!XValidate.isValidLength(txtFullname.getText(), 1, 50)) {
            XDialog.alert("Họ và tên phải từ 1 đến 50 ký tự");
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        this.setForm(new User());
        this.setEditable(false);
    }

    @Override
    public void setEditable(boolean editable) {
        txtSdt.setEnabled(!editable);
        btnAdd.setEnabled(!editable);
        btnUpdate.setEnabled(editable);
    }

    public void chooseFile() {
        // Tạo bộ lọc chỉ cho phép file ảnh
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false); // Ẩn lựa chọn "Tất cả các tệp"

        // Hiển thị hộp thoại chọn file
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            File file = XIcon.copyTo(selectedFile, "images");
            lblImage.setToolTipText(file.getName());
            XIcon.setIcon(lblImage, file);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        fileChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = tblUsers = new javax.swing.JTable();
        tblUsers.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        txtSdt = new TextFBorder(5);
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new TextFBorder(5);
        jLabel3 = new javax.swing.JLabel();
        txtFullname = new TextFBorder(5);
        jLabel4 = new javax.swing.JLabel();
        cboRole = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        rdoEnabled = new javax.swing.JRadioButton();
        rdoDisabled = new javax.swing.JRadioButton();
        lblImage = new ui.components.LabelBorder();
        btnImage = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnCheckAll = new javax.swing.JButton();
        btnUncheck = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCheckAll1 = new javax.swing.JButton();

        setForeground(new java.awt.Color(255, 255, 255));

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

        tblUsers.setBackground(new Color(0,0,0,0));
        tblUsers.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0345223777", "tam19", "Hoàng Nguyễn Minh Tâm", null, null, "oke", null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "SDT", "Password", "FullName", "Photo", "Role", "Status", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsers.setOpaque(false);
        tblUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsers);

        textField.setBackground(new Color(0,0,0,0));
        textField.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        textField.setToolTipText("");
        textField.setBorder(null);

        jLabel7.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 3, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("QUẢN LÝ TÀI KHOẢN");

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
        jLabel1.setText("Số điện thoại:");

        txtSdt.setBackground(new Color(0,0,0,0));
        txtSdt.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Password:");

        txtPassword.setBackground(new Color(0,0,0,0));
        txtPassword.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Họ và tên:");

        txtFullname.setBackground(new Color(0,0,0,0));
        txtFullname.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Vai trò:");

        cboRole.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        cboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý", "Khách hàng" }));

        jLabel5.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Trạng thái:");

        buttonGroup1.add(rdoEnabled);
        rdoEnabled.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        rdoEnabled.setSelected(true);
        rdoEnabled.setText("Hoạt động");

        buttonGroup1.add(rdoDisabled);
        rdoDisabled.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        rdoDisabled.setText("Ngừng hoạt động");

        lblImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.lightGray, null, java.awt.Color.lightGray));
        lblImage.setText("");

        btnImage.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
        btnImage.setText("Chọn ảnh");
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
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
        btnCheckAll1.setText("Xem tài khoản ngừng hoạt động");
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
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(79, 79, 79)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboRole, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rdoDisabled)
                                            .addComponent(rdoEnabled)))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(btnImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(btnCheckAll1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboRole, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoEnabled))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoDisabled)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
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

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        this.create();
    }//GEN-LAST:event_btnAddActionPerformed

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

    private void tblUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsersMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.edit();
        }
    }//GEN-LAST:event_tblUsersMouseClicked

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        // TODO add your handling code here:
        this.chooseFile();
    }//GEN-LAST:event_btnImageActionPerformed

    private void HideShow() {
        check = !check;
        this.fillToTable();
        btnCheckAll1.setText(check ? "Xem tài khoản ngừng hoạt động" : "Ẩn tài khoản ngừng hoạt động");
    }

    private void btnCheckAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAll1ActionPerformed
        // TODO add your handling code here:
        this.HideShow();
    }//GEN-LAST:event_btnCheckAll1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCheckAll;
    private javax.swing.JButton btnCheckAll1;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUncheck;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboRole;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private ui.components.LabelBorder lblImage;
    private ui.components.Menu menu1;
    private javax.swing.JRadioButton rdoDisabled;
    private javax.swing.JRadioButton rdoEnabled;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextField textField;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtSdt;
    // End of variables declaration//GEN-END:variables
}
