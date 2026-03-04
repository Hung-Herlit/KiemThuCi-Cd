/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.jpanel;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.impl.CategoryDAOImpl;
import dao.impl.ProductDAOImpl;
import entity.Category;
import entity.Product;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import ui.components.TextFBorder;
import ui.controller.ProductController;
import util.XDialog;
import util.XIcon;
import util.XValidate;

/**
 *
 * @author hungddv
 */
public class ProductManagerJPanel extends javax.swing.JPanel implements ProductController {

    /**
     * Creates new form ProductManagerJPanel
     */
    private boolean check;

    public ProductManagerJPanel() {
        check = true;
        initComponents();
        this.open();
        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));// ví dụ: trắng
    }

    ProductDAO dao = new ProductDAOImpl();
    List<Product> items = List.of();
    List<Category> categories = List.of();

    @Override
    public void open() {
        this.fillCategories();
        this.fillToTable();
        this.clear();
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);
        Category category = categories.get(tblCategories.getSelectedRow());
        if (!check) {
            items = dao.findByCategoryId(category.getId()); // thay vì findAll()
        }else
        {
            items = dao.findByCategoryId2(category.getId());
        }
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        items.stream()
                .filter(item -> !check || item.isStatus()) // nếu check == true thì lọc status == true
                .forEach(item -> {
                    Object[] rowData = {
                        item.getId(),
                        item.getName(),
                        df.format(item.getUnitPrice()),
                        item.getStock(),
                        item.getShelfId(),
                        item.getImage(),
                        item.getDiscount(),
                        item.isStatus() ? "Đang kinh doanh" : "Ngừng kinh doanh",
                        false
                    };
                    model.addRow(rowData);
                });
        this.clear();
    }

    @Override
    public void fillCategories() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboCategories.getModel();
        cboModel.removeAllElements();
        DefaultTableModel tblModel = (DefaultTableModel) tblCategories.getModel();
        tblModel.setRowCount(0);
        CategoryDAO cdao = new CategoryDAOImpl();
        if (!check) {
            categories = cdao.findAll();
        } else {
            categories = cdao.findAll2();
        }
//        categories.forEach(category -> {
//            cboModel.addElement(category);
//            tblModel.addRow(new Object[]{category.getName()});
//        });
        categories.stream()
                .filter(category -> !check || category.isStatus()) // nếu check == true thì lọc status == true
                .forEach(category -> {
                    cboModel.addElement(category);
                    tblModel.addRow(new Object[]{category.getName()});
                });
        tblCategories.setRowSelectionInterval(0, 0);
    }

    @Override
    public void edit() {
        Product entity = items.get(tblProducts.getSelectedRow());
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
        for (int i = 0; i < tblProducts.getRowCount(); i++) {
            tblProducts.setValueAt(checked, i, 8);
        }
    }

    @Override
    public void deleteCheckedItems() {
        if (XDialog.confirm("Bạn thực sự muốn xóa các mục chọn?")) {
            for (int i = 0; i < tblProducts.getRowCount(); i++) {
                if ((Boolean) tblProducts.getValueAt(i, 8)) {
                    System.out.println("" + items.get(i).getId());
                    dao.deleteById(items.get(i).getId());
                }
            }
            this.fillToTable();
        }
    }

    @Override
    public void setForm(Product entity) {
        txtId.setText(entity.getId());
        txtName.setText(entity.getName());
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0); // không hiển thị phần thập phân
        txtPrice.setText(df.format(entity.getUnitPrice()));

        cboCategories.setSelectedIndex(tblCategories.getSelectedRow());
        sldDiscount.setValue((int) entity.getDiscount());
        lblImage.setToolTipText(entity.getImage());
        txtStock.setText(Integer.toString(entity.getStock()));
        txtShelfId.setText(entity.getShelfId());
        XIcon.setIcon(lblImage, new File("images", entity.getImage())); // hiển thị hình từ file
        if (entity.isStatus()) {
            rdoEnabled.setSelected(true);
        } else {
            rdoDisabled.setSelected(true);
        }
    }

    @Override
    public Product getForm() {
        Product entity = new Product();
        entity.setId(txtId.getText());
        entity.setCategoryId(categories.get(cboCategories.getSelectedIndex()).getId());
        entity.setName(txtName.getText());
        entity.setUnitPrice(Double.parseDouble(txtPrice.getText()));
        entity.setDiscount((double) sldDiscount.getValue());
        entity.setImage(lblImage.getToolTipText()); // lấy tên hình từ ToolTipText
        entity.setStock(Integer.parseInt(txtStock.getText()));
        entity.setShelfId(txtShelfId.getText());
        entity.setStatus(rdoEnabled.isSelected());
        return entity;
    }

    @Override
    public boolean check() {
        if (XValidate.isNothing(txtId.getText())) {
            XDialog.alert("Không để trống Mã sản phẩm");
            return false;
        } else if (!XValidate.isValidLength(txtId.getText(), 1, 12)) {
            XDialog.alert("Mã sản phẩm tối đa 12 ký tự");
            return false;
        } else if (XValidate.isNothing(txtName.getText())) {
            XDialog.alert("Không để trống Tên sản phẩm");
            return false;
        } else if (!XValidate.isValidLength(txtName.getText(), 2, 50)) {
            XDialog.alert("Tên sản phẩm phải từ 2 đến 50 ký tự");
            return false;
        } else if (XValidate.isNothing(txtPrice.getText())) {
            XDialog.alert("Không để trống Giá");
            return false;
        } else if (!XValidate.isValidFloat(txtPrice.getText())
                || !XValidate.isInRange(Double.parseDouble(txtPrice.getText()), 0, 999999999)) {
            XDialog.alert("Giá phải là số thực từ 0 đến 999,999,999");
            return false;
        } else if (XValidate.isNothing(txtStock.getText())) {
            XDialog.alert("Không để trống Số lượng tồn kho");
            return false;
        } else if (!XValidate.isInteger(txtStock.getText())
                || !XValidate.isInRange(Integer.parseInt(txtStock.getText()), 0, 999999)) {
            XDialog.alert("Số lượng phải là số nguyên từ 0 đến 999,999");
            return false;
        } else if (XValidate.isNothing(txtShelfId.getText())) {
            XDialog.alert("Không để trống Mã kệ");
            return false;
        } else if (!XValidate.isValidLength(txtShelfId.getText(), 1, 12)) {
            XDialog.alert("Mã kệ tối đa 12 ký tự");
            return false;
        }
        return true;
    }

    @Override
    public void create() {
        if (!this.check()) {
            return;
        }
        if (dao.findById((txtId.getText())) != null) {
            XDialog.alert("Mã sản phẩm đã tồn tại!");
        } else if (dao.findByName((txtName.getText())) != null) {
            XDialog.alert("Tên sản phẩm đã tồn tại");
        } else {
            Product entity = this.getForm();
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
        Product entity = this.getForm();
        System.out.println("" + entity);
        dao.update(entity);
        this.fillToTable();
    }

    @Override
    public void clear() {
        this.setForm(new Product());
        this.setEditable(false);
    }

    @Override
    public void setEditable(boolean editable) {
        txtId.setEnabled(!editable);
        btnAdd.setEnabled(!editable);
        btnUpdate.setEnabled(editable);
        int rowCount = tblProducts.getRowCount();
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

        fileChooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducts = tblProducts = new javax.swing.JTable();
        tblProducts.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        jScrollPane2 = new javax.swing.JScrollPane();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new TextFBorder(5);
        jLabel2 = new javax.swing.JLabel();
        txtName = new TextFBorder(5);
        jLabel3 = new javax.swing.JLabel();
        txtPrice = new TextFBorder(5);
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblImage = new ui.components.LabelBorder();
        btnImage = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnCheckAll = new javax.swing.JButton();
        btnUncheck = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        sldDiscount = new javax.swing.JSlider();
        txtStock = new TextFBorder(5);
        jLabel8 = new javax.swing.JLabel();
        txtShelfId = new TextFBorder(5);
        jLabel9 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        lblDiscount = new javax.swing.JLabel();
        lblPersent = new javax.swing.JLabel();
        btnCheckAll1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        rdoEnabled = new javax.swing.JRadioButton();
        rdoDisabled = new javax.swing.JRadioButton();

        jPanel1.setBackground(Color.decode("#F5F5F5"));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBackground(new Color(0,0,0,0));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 350));

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(255, 51, 0));

        tblProducts.setBackground(new Color(0,0,0,0));
        tblProducts.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Name", "Price", "Stock", "Shelf", "Image", "Status", "Discount", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProducts.setOpaque(false);
        tblProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducts);

        textField.setBackground(new Color(0,0,0,0));
        textField.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        textField.setToolTipText("");
        textField.setBorder(null);

        jLabel7.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 3, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("QUẢN LÝ SẢN PHẨM");

        javax.swing.GroupLayout menu1Layout = new javax.swing.GroupLayout(menu1);
        menu1.setLayout(menu1Layout);
        menu1Layout.setHorizontalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu1Layout.setVerticalGroup(
            menu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
            .addComponent(textField, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane2.setForeground(new java.awt.Color(255, 51, 0));

        tblCategories.setBackground(new Color(0,0,0,0));
        tblCategories.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 1, 14)); // NOI18N
        tblCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Category"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
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
        jScrollPane2.setViewportView(tblCategories);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.add(jPanel2);

        jPanel3.setBackground(new Color(0,0,0,0));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(950, 280));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Mã sản phẩm:");

        txtId.setBackground(new Color(0,0,0,0));
        txtId.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tên sản phẩm:");

        txtName.setBackground(new Color(0,0,0,0));
        txtName.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Giá sản phẩm:");

        txtPrice.setBackground(new Color(0,0,0,0));
        txtPrice.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Loại sản phẩm:");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Giảm giá:");

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

        sldDiscount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldDiscountStateChanged(evt);
            }
        });

        txtStock.setBackground(new Color(0,0,0,0));
        txtStock.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Số lượng:");

        txtShelfId.setBackground(new Color(0,0,0,0));
        txtShelfId.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Mã kệ:");

        lblDiscount.setText("100");

        lblPersent.setText("%");

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

        jLabel10.setFont(new java.awt.Font("JetBrains Mono NL ExtraBold", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Trạng thái:");

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
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(79, 79, 79)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCheckAll1)
                                .addContainerGap())
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtShelfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnCheckAll)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUncheck, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(388, 388, 388)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sldDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiscount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPersent)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoEnabled)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoDisabled)
                                .addGap(89, 89, 89)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(btnImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckAll1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPersent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sldDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtShelfId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoEnabled)
                            .addComponent(rdoDisabled))
                        .addGap(34, 34, 34)))
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

    private void tblProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductsMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.edit();
        }
    }//GEN-LAST:event_tblProductsMouseClicked

    private void tblCategoriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoriesMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.fillToTable();
        }
    }//GEN-LAST:event_tblCategoriesMouseClicked

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        // TODO add your handling code here:
        this.chooseFile();
    }//GEN-LAST:event_btnImageActionPerformed

    private void sldDiscountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldDiscountStateChanged
        // TODO add your handling code here:
        int discount = sldDiscount.getValue();
        lblDiscount.setText("" + discount);
    }//GEN-LAST:event_sldDiscountStateChanged

    private void HideShow() {
        check = !check;
        this.fillToTable();
        btnCheckAll1.setText(check ? "Xem dữ liệu ngừng kinh doanh " : "Ẩn dữ liệu ngừng kinh doanh ");
    }

    private void btnCheckAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAll1ActionPerformed
        // TODO add your handling code here:
        this.HideShow();
    }//GEN-LAST:event_btnCheckAll1ActionPerformed

    private void rdoEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEnabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoEnabledActionPerformed

    private void rdoDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDisabledActionPerformed


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
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDiscount;
    private ui.components.LabelBorder lblImage;
    private javax.swing.JLabel lblPersent;
    private ui.components.Menu menu1;
    private javax.swing.JRadioButton rdoDisabled;
    private javax.swing.JRadioButton rdoEnabled;
    private javax.swing.JSlider sldDiscount;
    private javax.swing.JTable tblCategories;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField textField;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtShelfId;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
