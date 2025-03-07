/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewChucNangChinh;

import Model.NXB;
import Model.NgonNgu;
import Repository.SanPhamRepo;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import Model.SanPham;
import Model.TacGia;
import Model.TheLoai;
import Repository.NXBDAO;
import Repository.NgonNguDAO;
import Repository.TacGiaDAO;
import Repository.TheLoaiDAO;
import Utils.DBConnection;
import java.awt.Component;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon; // Import để xử lý hình ảnh
import java.awt.Image; // Import để resize hình ảnh
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer; // Để hiển thị hình ảnh trong JTable
import javax.swing.JTable; // Để thao tác với bảng dữ liệu
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Hoang
 */
public class ViewSanPham extends javax.swing.JPanel {

    private SanPhamUpdateListener sanPhamUpdateListener;

    public void setSanPhamUpdateListener(SanPhamUpdateListener listener) {
        this.sanPhamUpdateListener = listener;
    }

    public void refreshTableData() {
        SwingUtilities.invokeLater(() -> {
            loadSanPham(sanPhamRepo.getAllSanPham()); // Làm mới bảng sản phẩm
            System.out.println("Đã làm mới bảng ViewSanPham");
        });
    }
    private SanPhamRepo sanPhamRepo;
    TheLoaiDAO dao = new TheLoaiDAO();
    NgonNguDAO nndao = new NgonNguDAO();
    NXBDAO nxbdao = new NXBDAO();
    TacGiaDAO tgdao = new TacGiaDAO();

    public ViewSanPham() {
        initComponents();
        initTableTheLoai();  // Khởi tạo bảng Thể Loại
        initTableNgonNgu();   //Khởi tạo bảng ngôn ngữ
        initTableNXB(); // khởi tạo bảng nxb
        initTableTacGia();// khởi tạo bảng tác giả
        sanPhamRepo = new SanPhamRepo();
        loadComboboxData(); // Load dữ liệu cho các combobox khi mở giao diện
        loadSanPham(sanPhamRepo.getAllSanPham());
        loadDataTheLoai();
        loadDataNgonNgu();
        loadDataNXB();
        loadDataTacGia();
        //chinht sửa thông số bảng
        tblSanPham.setRowHeight(50); // Đặt chiều cao của hàng đủ để hiển thị ảnh
        tblSanPham.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText(""); // Xóa văn bản hiển thị nếu có
                } else {
                    setText(value != null ? value.toString() : "");
                    setIcon(null);
                }
            }
        });
    }

    private void loadSanPham(List<SanPham> list) { // Nhận danh sách sản phẩm làm tham số
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        for (SanPham sp : list) {
            ImageIcon imageIcon = null;
            if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) {
                String imagePath = "src/images/" + sp.getHinhAnh(); // Đường dẫn ảnh trong project
                ImageIcon originalIcon = new ImageIcon(imagePath);
                Image img = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize ảnh
                imageIcon = new ImageIcon(img);
            }

            model.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                sp.getSoLuong(),
                imageIcon, // Hiển thị hình ảnh thay vì đường dẫn
                sp.getTenNgonNgu(),
                sp.getTenTheLoai(),
                sp.getTenTacGia(),
                sp.getTenNxb(),
                sp.getGiaBan(),
                sp.isTrangThai() ? "Đang bán" : "Ngưng bán"
            });
        }
    }

    private void loadComboboxData() {
        SanPhamRepo dao = new SanPhamRepo();

        // Load Thể Loại
        List<String> theLoaiList = dao.getAllTheLoai();
        cboTheLoai.removeAllItems();
        for (String item : theLoaiList) {
            cboTheLoai.addItem(item);
        }

        // Load Ngôn Ngữ
        List<String> ngonNguList = dao.getAllNgonNgu();
        cboNgonNgu.removeAllItems();
        for (String item : ngonNguList) {
            cboNgonNgu.addItem(item);
        }

        // Load Nhà Xuất Bản
        List<String> nxbList = dao.getAllNXB();
        cboNxb.removeAllItems();
        for (String item : nxbList) {
            cboNxb.addItem(item);
        }

        // Load Tác Giả
        List<String> tacGiaList = dao.getAllTacGia();
        cboTacGia.removeAllItems();
        for (String item : tacGiaList) {
            cboTacGia.addItem(item);
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
        jLabel12 = new javax.swing.JLabel();
        btngroupTTTheLoai = new javax.swing.ButtonGroup();
        btnGroupTTNgonNgu = new javax.swing.ButtonGroup();
        btnGroupTTTacGia = new javax.swing.ButtonGroup();
        btnGroupTTNXB = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtTenSach = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboNgonNgu = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cboTheLoai = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cboTacGia = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cboNxb = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtGiaBan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        rdoDangBan = new javax.swing.JRadioButton();
        rdoNgungBan = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        lblHinhAnh = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        btnChonAnh = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtMaTheLoai = new javax.swing.JTextField();
        txtTenTheLoai = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTheLoai = new javax.swing.JTable();
        btnThemTheLoai = new javax.swing.JButton();
        btnSuaTheLoai = new javax.swing.JButton();
        btnXoaTheLoai = new javax.swing.JButton();
        btnLamMoiTheLoai = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtTimKiemTheLoai = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        rdoDangBanTheLoai = new javax.swing.JRadioButton();
        rdoNgungBanTheLoai = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtMaNgonNgu = new javax.swing.JTextField();
        txtTenNgonNgu = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNgonNgu = new javax.swing.JTable();
        btnThemNgonNgu = new javax.swing.JButton();
        btnSuaNgonNgu = new javax.swing.JButton();
        btnXoaNgonNgu = new javax.swing.JButton();
        btnLamMoiNgonNgu = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtTimKiemNgonNgu = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        rdoDangBanNgonNgu = new javax.swing.JRadioButton();
        rdoNgungBanNgonNgu = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtMaNXB = new javax.swing.JTextField();
        txtTenNXB = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblNXB = new javax.swing.JTable();
        btnThemNXB = new javax.swing.JButton();
        btnSuaNXB = new javax.swing.JButton();
        btnXoaNXB = new javax.swing.JButton();
        btnLamMoiNXB = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtTimKiemNXB = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        rdoDangBanNXB = new javax.swing.JRadioButton();
        rdoNgungBanNXB = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtMaTacGia = new javax.swing.JTextField();
        txtTenTacGia = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTacGia = new javax.swing.JTable();
        btnThemTacGia = new javax.swing.JButton();
        btnSuaTacGia = new javax.swing.JButton();
        btnXoaTacGia = new javax.swing.JButton();
        btnLamMoiTacGia = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtTimKiemTacGia = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        rdoDangBanTacGia = new javax.swing.JRadioButton();
        rdoNgungBanTacGia = new javax.swing.JRadioButton();

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Hình ảnh Sản Phẩm");

        setMinimumSize(new java.awt.Dimension(750, 650));
        setPreferredSize(new java.awt.Dimension(685, 590));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Mã : ");

        jLabel3.setText("Tên Sách : ");

        jLabel4.setText("Số Lượng : ");

        jLabel5.setText("Ngôn Ngữ : ");

        cboNgonNgu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Việt Nam", "Trung Quốc", "Mỹ", "Nga", "Hàn Quốc", "Nhật Bản" }));

        jLabel6.setText("Thể Loại : ");

        cboTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Văn học", "Truyện tranh", "Trinh Thám" }));

        jLabel7.setText("Tác Giả : ");

        cboTacGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam Cao", "Nguyễn Nhật Ánh", "Vũ Trọng Phụng", "Tô Hoài" }));

        jLabel8.setText("NXB : ");

        cboNxb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kim Đồng", "Fahasa", "WingsBook" }));
        cboNxb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNxbActionPerformed(evt);
            }
        });

        jLabel9.setText("Giá Bán : ");

        jLabel10.setText("Trạng Thái ");

        buttonGroup1.add(rdoDangBan);
        rdoDangBan.setText("Đang bán");

        buttonGroup1.add(rdoNgungBan);
        rdoNgungBan.setText("Ngưng bán");

        jPanel2.setPreferredSize(new java.awt.Dimension(250, 240));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
        );

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã ", "Tên Sách ", "Số Lượng", "Hình Ảnh", "Ngôn Ngữ ", "Thể Loại", "Tác Giả", "NXB", "Giá Bán", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);
        if (tblSanPham.getColumnModel().getColumnCount() > 0) {
            tblSanPham.getColumnModel().getColumn(0).setResizable(false);
            tblSanPham.getColumnModel().getColumn(1).setResizable(false);
            tblSanPham.getColumnModel().getColumn(2).setResizable(false);
            tblSanPham.getColumnModel().getColumn(3).setResizable(false);
            tblSanPham.getColumnModel().getColumn(4).setResizable(false);
            tblSanPham.getColumnModel().getColumn(5).setResizable(false);
            tblSanPham.getColumnModel().getColumn(6).setResizable(false);
            tblSanPham.getColumnModel().getColumn(7).setResizable(false);
            tblSanPham.getColumnModel().getColumn(8).setResizable(false);
            tblSanPham.getColumnModel().getColumn(9).setResizable(false);
        }

        btnThem.setText("Thêm ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá ");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        btnChonAnh.setText("Chọn ảnh");
        btnChonAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNgonNgu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(rdoDangBan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoNgungBan)))
                .addGap(6, 75, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(6, 6, 6)))
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNxb, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(326, 326, 326))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTimKiem)
                    .addComponent(btnThem))
                .addGap(44, 44, 44)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnCapNhat)
                        .addGap(42, 42, 42)
                        .addComponent(btnXoa)
                        .addGap(42, 42, 42)
                        .addComponent(btnLamMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnChonAnh)
                        .addGap(163, 163, 163))))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboNgonNgu, txtMa, txtSoLuong, txtTenSach});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel6)
                                    .addComponent(cboTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtMa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(cboTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(cboNxb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(txtTenSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboNgonNgu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(rdoDangBan)
                            .addComponent(rdoNgungBan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThem)
                            .addComponent(btnCapNhat)
                            .addComponent(btnXoa)
                            .addComponent(btnLamMoi))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTimKiem)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnChonAnh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboNgonNgu, txtMa, txtSoLuong, txtTenSach});

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("Quản lí sản phẩm");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setText("Hình ảnh Sản Phẩm");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(601, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(122, 122, 122))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(629, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(35, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Sản Phẩm", jPanel3);

        jLabel14.setText("Mã : ");

        jLabel15.setText("Tên thể loại:");

        tblTheLoai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTheLoai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTheLoaiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTheLoai);

        btnThemTheLoai.setText("Thêm");
        btnThemTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemTheLoaiActionPerformed(evt);
            }
        });

        btnSuaTheLoai.setText("Sửa");
        btnSuaTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTheLoaiActionPerformed(evt);
            }
        });

        btnXoaTheLoai.setText("Xoá");
        btnXoaTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTheLoaiActionPerformed(evt);
            }
        });

        btnLamMoiTheLoai.setText("Làm mới");
        btnLamMoiTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiTheLoaiActionPerformed(evt);
            }
        });

        jLabel1.setText("Tìm kiếm");

        txtTimKiemTheLoai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemTheLoaiKeyReleased(evt);
            }
        });

        jLabel16.setText("Trạng Thái:");

        btngroupTTTheLoai.add(rdoDangBanTheLoai);
        rdoDangBanTheLoai.setText("Đang bán");

        btngroupTTTheLoai.add(rdoNgungBanTheLoai);
        rdoNgungBanTheLoai.setText("Ngưng bán");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel1))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnThemTheLoai)
                        .addGap(75, 75, 75)
                        .addComponent(btnSuaTheLoai)
                        .addGap(75, 75, 75)
                        .addComponent(btnXoaTheLoai)
                        .addGap(85, 85, 85)
                        .addComponent(btnLamMoiTheLoai))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtMaTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel16)
                        .addGap(39, 39, 39)
                        .addComponent(rdoDangBanTheLoai)
                        .addGap(64, 64, 64)
                        .addComponent(rdoNgungBanTheLoai))
                    .addComponent(txtTenTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaTheLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(rdoDangBanTheLoai)
                        .addComponent(rdoNgungBanTheLoai)))
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenTheLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemTheLoai)
                    .addComponent(btnSuaTheLoai)
                    .addComponent(btnXoaTheLoai)
                    .addComponent(btnLamMoiTheLoai))
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTimKiemTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thể Loại", jPanel4);

        jLabel17.setText("Mã : ");

        jLabel18.setText("Tên Ngôn Ngữ:");

        tblNgonNgu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNgonNguMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblNgonNgu);
        if (tblNgonNgu.getColumnModel().getColumnCount() > 0) {
            tblNgonNgu.getColumnModel().getColumn(0).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(1).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(2).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(3).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(4).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(5).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(6).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(7).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(8).setResizable(false);
            tblNgonNgu.getColumnModel().getColumn(9).setResizable(false);
        }

        btnThemNgonNgu.setText("Thêm");
        btnThemNgonNgu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNgonNguActionPerformed(evt);
            }
        });

        btnSuaNgonNgu.setText("Sửa");
        btnSuaNgonNgu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNgonNguActionPerformed(evt);
            }
        });

        btnXoaNgonNgu.setText("Xoá");
        btnXoaNgonNgu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNgonNguActionPerformed(evt);
            }
        });

        btnLamMoiNgonNgu.setText("Làm mới");
        btnLamMoiNgonNgu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNgonNguActionPerformed(evt);
            }
        });

        jLabel19.setText("Tìm kiếm");

        txtTimKiemNgonNgu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemNgonNguKeyReleased(evt);
            }
        });

        jLabel20.setText("Trạng Thái:");

        btnGroupTTNgonNgu.add(rdoDangBanNgonNgu);
        rdoDangBanNgonNgu.setText("Đang bán");

        btnGroupTTNgonNgu.add(rdoNgungBanNgonNgu);
        rdoNgungBanNgonNgu.setText("Ngưng bán");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19))
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnThemNgonNgu)
                        .addGap(75, 75, 75)
                        .addComponent(btnSuaNgonNgu)
                        .addGap(75, 75, 75)
                        .addComponent(btnXoaNgonNgu)
                        .addGap(85, 85, 85)
                        .addComponent(btnLamMoiNgonNgu))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtMaNgonNgu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel20)
                        .addGap(39, 39, 39)
                        .addComponent(rdoDangBanNgonNgu)
                        .addGap(64, 64, 64)
                        .addComponent(rdoNgungBanNgonNgu))
                    .addComponent(txtTenNgonNgu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemNgonNgu, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(153, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaNgonNgu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20)
                        .addComponent(rdoDangBanNgonNgu)
                        .addComponent(rdoNgungBanNgonNgu)))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenNgonNgu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNgonNgu)
                    .addComponent(btnSuaNgonNgu)
                    .addComponent(btnXoaNgonNgu)
                    .addComponent(btnLamMoiNgonNgu))
                .addGap(54, 54, 54)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTimKiemNgonNgu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ngôn Ngữ", jPanel6);

        jLabel21.setText("Mã : ");

        jLabel22.setText("Tên NXB:");

        tblNXB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNXBMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblNXB);
        if (tblNXB.getColumnModel().getColumnCount() > 0) {
            tblNXB.getColumnModel().getColumn(0).setResizable(false);
            tblNXB.getColumnModel().getColumn(1).setResizable(false);
            tblNXB.getColumnModel().getColumn(2).setResizable(false);
            tblNXB.getColumnModel().getColumn(3).setResizable(false);
            tblNXB.getColumnModel().getColumn(4).setResizable(false);
            tblNXB.getColumnModel().getColumn(5).setResizable(false);
            tblNXB.getColumnModel().getColumn(6).setResizable(false);
            tblNXB.getColumnModel().getColumn(7).setResizable(false);
            tblNXB.getColumnModel().getColumn(8).setResizable(false);
            tblNXB.getColumnModel().getColumn(9).setResizable(false);
        }

        btnThemNXB.setText("Thêm");
        btnThemNXB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNXBActionPerformed(evt);
            }
        });

        btnSuaNXB.setText("Sửa");
        btnSuaNXB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNXBActionPerformed(evt);
            }
        });

        btnXoaNXB.setText("Xoá");
        btnXoaNXB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNXBActionPerformed(evt);
            }
        });

        btnLamMoiNXB.setText("Làm mới");
        btnLamMoiNXB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNXBActionPerformed(evt);
            }
        });

        jLabel23.setText("Tìm kiếm");

        txtTimKiemNXB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemNXBKeyReleased(evt);
            }
        });

        jLabel24.setText("Trạng Thái:");

        btnGroupTTNXB.add(rdoDangBanNXB);
        rdoDangBanNXB.setText("Đang bán");

        btnGroupTTNXB.add(rdoNgungBanNXB);
        rdoNgungBanNXB.setText("Ngưng bán");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23))
                .addGap(22, 22, 22)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnThemNXB)
                        .addGap(75, 75, 75)
                        .addComponent(btnSuaNXB)
                        .addGap(75, 75, 75)
                        .addComponent(btnXoaNXB)
                        .addGap(85, 85, 85)
                        .addComponent(btnLamMoiNXB))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtMaNXB, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel24)
                        .addGap(39, 39, 39)
                        .addComponent(rdoDangBanNXB)
                        .addGap(64, 64, 64)
                        .addComponent(rdoNgungBanNXB))
                    .addComponent(txtTenNXB, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemNXB, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(186, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaNXB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)
                        .addComponent(rdoDangBanNXB)
                        .addComponent(rdoNgungBanNXB)))
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenNXB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(45, 45, 45)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNXB)
                    .addComponent(btnSuaNXB)
                    .addComponent(btnXoaNXB)
                    .addComponent(btnLamMoiNXB))
                .addGap(54, 54, 54)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtTimKiemNXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("NXB", jPanel7);

        jLabel25.setText("Mã : ");

        jLabel26.setText("Tên Tác Giả:");

        tblTacGia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTacGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTacGiaMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblTacGia);

        btnThemTacGia.setText("Thêm");
        btnThemTacGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemTacGiaActionPerformed(evt);
            }
        });

        btnSuaTacGia.setText("Sửa");
        btnSuaTacGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTacGiaActionPerformed(evt);
            }
        });

        btnXoaTacGia.setText("Xoá");
        btnXoaTacGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTacGiaActionPerformed(evt);
            }
        });

        btnLamMoiTacGia.setText("Làm mới");
        btnLamMoiTacGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiTacGiaActionPerformed(evt);
            }
        });

        jLabel27.setText("Tìm kiếm");

        txtTimKiemTacGia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemTacGiaKeyReleased(evt);
            }
        });

        jLabel28.setText("Trạng Thái:");

        btnGroupTTTacGia.add(rdoDangBanTacGia);
        rdoDangBanTacGia.setText("Đang bán");

        btnGroupTTTacGia.add(rdoNgungBanTacGia);
        rdoNgungBanTacGia.setText("Ngưng bán");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27))
                .addGap(22, 22, 22)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnThemTacGia)
                        .addGap(75, 75, 75)
                        .addComponent(btnSuaTacGia)
                        .addGap(75, 75, 75)
                        .addComponent(btnXoaTacGia)
                        .addGap(85, 85, 85)
                        .addComponent(btnLamMoiTacGia))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtMaTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel28)
                        .addGap(39, 39, 39)
                        .addComponent(rdoDangBanTacGia)
                        .addGap(64, 64, 64)
                        .addComponent(rdoNgungBanTacGia))
                    .addComponent(txtTenTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaTacGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28)
                        .addComponent(rdoDangBanTacGia)
                        .addComponent(rdoNgungBanTacGia)))
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenTacGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(45, 45, 45)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemTacGia)
                    .addComponent(btnSuaTacGia)
                    .addComponent(btnXoaTacGia)
                    .addComponent(btnLamMoiTacGia))
                .addGap(54, 54, 54)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtTimKiemTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tác Giả", jPanel8);

        add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 720));
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemNgonNguKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemNgonNguKeyReleased
        String keyword = txtTimKiemNgonNgu.getText();
        DefaultTableModel model = (DefaultTableModel) tblNgonNgu.getModel();
        model.setRowCount(0);
        List<NgonNgu> list = nndao.searchNgonNgu(keyword);
        for (NgonNgu nn : list) {
            model.addRow(new Object[]{nn.getID(), nn.getMaNgonNgu(), nn.getTenNgonNgu(), nn.isTrangThai() ? "Đang bán" : "Ngưng bán"});
        }
    }//GEN-LAST:event_txtTimKiemNgonNguKeyReleased

    private void btnXoaNgonNguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNgonNguActionPerformed
        int selectedRow = tblNgonNgu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngôn ngữ cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa ngôn ngữ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maNgonNgu = txtMaNgonNgu.getText().trim();
            if (nndao.deleteNgonNgu(maNgonNgu)) { // Gọi hàm xóa từ repository
                JOptionPane.showMessageDialog(this, "Xóa ngôn ngữ thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadDataNgonNgu(); // Cập nhật lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa ngôn ngữ thất bại! Hãy kiểm tra ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        loadComboboxData();
    }//GEN-LAST:event_btnXoaNgonNguActionPerformed

    private void btnSuaNgonNguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNgonNguActionPerformed
        String ma = txtMaNgonNgu.getText().trim();
        String ten = txtTenNgonNgu.getText().trim();

        String regexMa = "^[a-zA-Z0-9]+$";
        String regexTen = "^[\\p{L}\\s]+$"; // Chỉ cho phép chữ Unicode và khoảng trắng, KHÔNG cho phép số

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Ngôn ngữ cần sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã Ngôn ngữ chỉ được chứa chữ cái và số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên Ngôn ngữ không được chứa số hoặc ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy trạng thái từ giao diện
        boolean trangThai = rdoDangBanNgonNgu.isSelected();
        System.out.println("Trạng thái trước khi cập nhật: " + trangThai);

        // Cập nhật vào CSDL
        NgonNgu ngonNguMoi = new NgonNgu(0, ma, ten, trangThai);
        if (nndao.updateNgonNgu(ngonNguMoi)) {
            JOptionPane.showMessageDialog(this, "Cập nhật Ngôn ngữ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Load lại dữ liệu trên bảng
            loadDataNgonNgu();

            // Cập nhật trực tiếp combobox thay vì load lại toàn bộ
            loadComboboxData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật Ngôn ngữ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSuaNgonNguActionPerformed

    private void btnThemNgonNguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNgonNguActionPerformed
        String ma = txtMaNgonNgu.getText().trim();
        String ten = txtTenNgonNgu.getText().trim();

        // Kiểm tra điều kiện đầu vào
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra mã và tên ngôn ngữ có hợp lệ không
        String regexMa = "^[a-zA-Z0-9]+$";  // Không dấu, không khoảng trắng, không ký tự đặc biệt
        String regexTen = "^[\\p{L}\\s]+$"; // Chỉ cho phép chữ Unicode và khoảng trắng, KHÔNG cho phép số

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã ngôn ngữ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên ngôn ngữ không được chứa số hoặc ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng mã
        if (nndao.isMaNgonNguExists(ma)) {
            JOptionPane.showMessageDialog(this, "Mã ngôn ngữ đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào CSDL
        NgonNgu ngonNguMoi = new NgonNgu(0, ma, ten, true);
        if (nndao.insertNgonNgu(ngonNguMoi)) {
            JOptionPane.showMessageDialog(this, "Thêm ngôn ngữ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật ngay combobox thay vì load lại toàn bộ
            cboNgonNgu.addItem(ten);
            loadDataNgonNgu();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm ngôn ngữ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnThemNgonNguActionPerformed

    private void tblNgonNguMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNgonNguMouseClicked
        int selectedRow = tblNgonNgu.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có dòng nào được chọn không
            String maNgonNgu = tblNgonNgu.getValueAt(selectedRow, 1).toString();
            String tenNgonNgu = tblNgonNgu.getValueAt(selectedRow, 2).toString();

            txtMaNgonNgu.setText(maNgonNgu);
            txtTenNgonNgu.setText(tenNgonNgu);
            // Kiểm tra trạng thái dựa trên chuỗi
            String trangThai = tblNgonNgu.getValueAt(selectedRow, 3).toString();
            if (trangThai.equalsIgnoreCase("Đang bán")) {
                rdoDangBanNgonNgu.setSelected(true);
            } else {
                rdoNgungBanNgonNgu.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblNgonNguMouseClicked

    private void txtTimKiemTheLoaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemTheLoaiKeyReleased
        String keyword = txtTimKiemTheLoai.getText();
        DefaultTableModel model1 = (DefaultTableModel) tblTheLoai.getModel();
        model1.setRowCount(0);
        List<TheLoai> list = dao.searchTheLoai(keyword);
        for (TheLoai tl : list) {
            model1.addRow(new Object[]{tl.getID(), tl.getMaTheLoai(), tl.getTenTheLoai(), tl.isTrangThai() ? "Đang bán" : "Ngưng bán"});
        }
    }//GEN-LAST:event_txtTimKiemTheLoaiKeyReleased

    private void btnXoaTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTheLoaiActionPerformed
        int selectedRow = tblTheLoai.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thể loại này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maTheLoai = txtMaTheLoai.getText().trim();
            if (dao.deleteTheLoai(maTheLoai)) { // Gọi hàm xóa từ repository
                JOptionPane.showMessageDialog(this, "Xóa thể loại thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadDataTheLoai(); // Cập nhật lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thể loại thất bại! Hãy kiểm tra ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        loadComboboxData();
    }//GEN-LAST:event_btnXoaTheLoaiActionPerformed

    private void btnSuaTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTheLoaiActionPerformed
        String ma = txtMaTheLoai.getText().trim();
        String ten = txtTenTheLoai.getText().trim();

        String regexMa = "^[a-zA-Z0-9]+$";
        String regexTen = "^[\\p{L}0-9\\s\\p{P}\\p{S}&&[^'\"@#^*?~`]]+$"; // Cho phép chữ, số, khoảng trắng, dấu câu, biểu tượng, trừ ', ", @, #, ^, *, ?, ~, `

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại cần sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã thể loại chỉ được chứa chữ cái và số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên thể loại không được chứa dấu nháy đơn ', nháy kép \", hoặc @, #, ^, *, ?, ~, `!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy trạng thái từ giao diện
        boolean trangThai = rdoDangBanTheLoai.isSelected();
        System.out.println("Trạng thái trước khi cập nhật: " + trangThai);

        // Cập nhật vào CSDL
        TheLoai theLoaiMoi = new TheLoai(0, ma, ten, trangThai);
        if (dao.updateTheLoai(theLoaiMoi)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thể loại thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Load lại dữ liệu trên bảng
            loadDataTheLoai();

            // Cập nhật trực tiếp combobox thay vì load lại toàn bộ
            loadComboboxData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thể loại thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSuaTheLoaiActionPerformed

    private void btnThemTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemTheLoaiActionPerformed
        String ma = txtMaTheLoai.getText().trim();
        String ten = txtTenTheLoai.getText().trim();

        // Kiểm tra điều kiện đầu vào
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra mã và tên thể loại có hợp lệ không
        String regexMa = "^[a-zA-Z0-9]+$";  // Không dấu, không khoảng trắng, không ký tự đặc biệt
        String regexTen = "^[\\p{L}0-9\\s\\p{P}\\p{S}&&[^'\"@#^*?~`]]+$"; // Cho phép chữ, số, khoảng trắng, dấu câu, biểu tượng, trừ ', ", @, #, ^, *, ?, ~, `

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã thể loại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên thể loại không được chứa dấu nháy đơn ', nháy kép \", hoặc @, #, ^, *, ?, ~, `!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng mã
        if (dao.isMaTheLoaiExists(ma)) {
            JOptionPane.showMessageDialog(this, "Mã thể loại đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào CSDL
        TheLoai theLoaiMoi = new TheLoai(0, ma, ten, true);
        if (dao.insertTheLoai(theLoaiMoi)) {
            JOptionPane.showMessageDialog(this, "Thêm thể loại thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật ngay combobox thay vì load lại toàn bộ
            cboTheLoai.addItem(ten);
            loadDataTheLoai();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thể loại thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThemTheLoaiActionPerformed

    private void tblTheLoaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTheLoaiMouseClicked
        int selectedRow = tblTheLoai.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có dòng nào được chọn không
            String maTheLoai = tblTheLoai.getValueAt(selectedRow, 1).toString();
            String tenTheLoai = tblTheLoai.getValueAt(selectedRow, 2).toString();

            txtMaTheLoai.setText(maTheLoai);
            txtTenTheLoai.setText(tenTheLoai);
            // Kiểm tra trạng thái dựa trên chuỗi
            String trangThai = tblTheLoai.getValueAt(selectedRow, 3).toString();
            if (trangThai.equalsIgnoreCase("Đang bán")) {
                rdoDangBanTheLoai.setSelected(true);
            } else {
                rdoNgungBanTheLoai.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblTheLoaiMouseClicked

    private void btnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser("src/images");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImageName = selectedFile.getName(); // Lưu tên file (ví dụ: "sach1.jpg")

            // Hiển thị ảnh trên giao diện
            lblHinhAnh.setIcon(new ImageIcon("src/images/" + selectedImageName));
        }
    }//GEN-LAST:event_btnChonAnhActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        if (timer != null) {
            timer.stop(); // Hủy timer trước đó nếu có
        }

        timer = new Timer(300, (ActionEvent e) -> {  // Chờ 300ms trước khi tìm kiếm
            String tuKhoa = txtTimKiem.getText().trim();

            if (tuKhoa.isEmpty()) {
                loadSanPham(sanPhamRepo.getAllSanPham()); // Nếu rỗng, hiển thị tất cả sản phẩm
            } else {
                loadSanPham(sanPhamRepo.timKiemSanPham(tuKhoa)); // Nếu có từ khóa, tìm kiếm
            }
        });

        timer.setRepeats(false); // Chạy 1 lần duy nhất sau khi dừng nhập
        timer.start();
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        //        String tuKhoa = txtTimKiem.getText().trim();
        //        if (tuKhoa.isEmpty()) {
        //            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        //            return;
        //        }
        //        timKiemSanPham(tuKhoa);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật trạng thái!");
            return;
        }

        // Lấy mã sản phẩm từ bảng
        String maSanPham = tblSanPham.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đổi trạng thái sản phẩm này thành Ngưng bán?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SanPhamRepo dao = new SanPhamRepo();
            boolean success = dao.updateTrangThaiSanPham(maSanPham);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                loadSanPham(sanPhamRepo.getAllSanPham()); // Load lại bảng sau khi cập nhật
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại!");
            }
        }
        lamMoiForm();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        try {
            // Kiểm tra dữ liệu không được để trống
            if (txtMa.getText().trim().isEmpty() || txtTenSach.getText().trim().isEmpty()
                    || txtGiaBan.getText().trim().isEmpty() || txtSoLuong.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy giá trị nhập vào
            String maSP = txtMa.getText().trim();
            String tenSP = txtTenSach.getText().trim();
            String giaBanStr = txtGiaBan.getText().trim();
            String soLuongStr = txtSoLuong.getText().trim();

            // Biểu thức chính quy kiểm tra ký tự đặc biệt
            String regexMaSP = "^[a-zA-Z0-9]+$";  // Mã chỉ chứa chữ và số, không dấu, KHÔNG có khoảng trắng
            String regexTenSP = "^[\\p{L}0-9\\s]+$"; // Tên có thể chứa chữ Unicode (tiếng Việt), số và khoảng trắng

            // Kiểm tra mã sản phẩm không chứa ký tự đặc biệt và dấu cách
            if (!maSP.matches(regexMaSP)) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm chỉ được chứa chữ cái và số, không có ký tự đặc biệt hoặc dấu cách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tên sản phẩm không chứa ký tự đặc biệt (cho phép chữ Unicode)
            if (!tenSP.matches(regexTenSP)) {
                JOptionPane.showMessageDialog(this, "Tên sách không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tên sản phẩm trùng (không phân biệt hoa/thường), bỏ qua sản phẩm đang cập nhật
            List<SanPham> allSanPham = sanPhamRepo.getAllSanPham();
            for (SanPham sp : allSanPham) {
                if (!sp.getMaSanPham().equals(maSP) && sp.getTenSanPham().trim().equalsIgnoreCase(tenSP)) {
                    JOptionPane.showMessageDialog(this, "Tên sản phẩm '" + tenSP + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Kiểm tra giá bán và số lượng không được chứa khoảng trắng
            if (giaBanStr.contains(" ") || soLuongStr.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng không được chứa khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng và giá bán có phải số không
            double giaBan;
            int soLuong;
            try {
                giaBan = Double.parseDouble(giaBanStr);
                soLuong = Integer.parseInt(soLuongStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng phải là số hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng và giá bán có âm không
            if (giaBan < 0 || soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng phải lớn hơn hoặc bằng 0!", "Lỗi giá trị", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra dữ liệu trong combobox
            if (cboNxb.getSelectedIndex() == -1 || cboTheLoai.getSelectedIndex() == -1
                    || cboTacGia.getSelectedIndex() == -1 || cboNgonNgu.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thông tin từ các danh sách!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra xem mã sản phẩm có tồn tại không trước khi cập nhật
            if (!sanPhamRepo.kiemTraTonTai(maSP)) {
                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại, không thể cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin từ form
            String nxb = cboNxb.getSelectedItem().toString();
            String theLoai = cboTheLoai.getSelectedItem().toString();
            String tacGia = cboTacGia.getSelectedItem().toString();
            String ngonNgu = cboNgonNgu.getSelectedItem().toString();

            // Kiểm tra nếu có chọn ảnh mới
            String hinhAnh = (selectedImageName != null) ? selectedImageName : "";

            // Trạng thái sản phẩm (true = Đang bán, false = Ngưng bán)
            boolean trangThai = rdoDangBan.isSelected();

            // Tạo đối tượng sản phẩm
            SanPham sp = new SanPham(maSP, tenSP, giaBan, hinhAnh, soLuong, theLoai, ngonNgu, nxb, tacGia, trangThai);

            // Cập nhật sản phẩm vào database
            if (sanPhamRepo.updateSanPham(sp)) {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadSanPham(sanPhamRepo.getAllSanPham()); // Tải lại danh sách sản phẩm
                lamMoiForm();  // Làm mới form sau khi cập nhật

                // Thông báo cho các listener (ví dụ: ViewBanHang) cập nhật
                if (sanPhamUpdateListener != null) {
                    sanPhamUpdateListener.onSanPhamUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        try {
            // Kiểm tra dữ liệu không được để trống
            if (txtMa.getText().trim().isEmpty() || txtTenSach.getText().trim().isEmpty()
                    || txtGiaBan.getText().trim().isEmpty() || txtSoLuong.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maSP = txtMa.getText().trim();
            String tenSP = txtTenSach.getText().trim();
            String giaBanStr = txtGiaBan.getText().trim();
            String soLuongStr = txtSoLuong.getText().trim();

            // Biểu thức chính quy kiểm tra ký tự đặc biệt
            String regexMaSP = "^[a-zA-Z0-9]+$";  // Mã chỉ chứa chữ và số, không dấu, KHÔNG có khoảng trắng
            String regexTenSP = "^[\\p{L}0-9\\s]+$"; // Tên có thể chứa chữ Unicode (tiếng Việt), số và khoảng trắng

            // Kiểm tra mã sản phẩm không chứa ký tự đặc biệt và dấu cách
            if (!maSP.matches(regexMaSP)) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm chỉ được chứa chữ cái và số, không có ký tự đặc biệt hoặc dấu cách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tên sản phẩm không chứa ký tự đặc biệt (cho phép chữ Unicode)
            if (!tenSP.matches(regexTenSP)) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra tên sản phẩm trùng (không phân biệt hoa/thường)
            List<SanPham> allSanPham = sanPhamRepo.getAllSanPham();
            for (SanPham sp : allSanPham) {
                if (sp.getTenSanPham().trim().equalsIgnoreCase(tenSP)) {
                    JOptionPane.showMessageDialog(this, "Tên sản phẩm '" + tenSP + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Kiểm tra số lượng và giá bán không chứa dấu cách
            if (giaBanStr.contains(" ") || soLuongStr.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng không được chứa dấu cách!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng và giá bán có phải số không
            double giaBan;
            int soLuong;
            try {
                giaBan = Double.parseDouble(giaBanStr);
                soLuong = Integer.parseInt(soLuongStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng phải là số hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng và giá bán có âm không
            if (giaBan < 0 || soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán và số lượng phải lớn hơn hoặc bằng 0!", "Lỗi giá trị", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy dữ liệu từ combobox (đảm bảo người dùng đã chọn giá trị hợp lệ)
            if (cboNxb.getSelectedIndex() == -1 || cboTheLoai.getSelectedIndex() == -1
                    || cboTacGia.getSelectedIndex() == -1 || cboNgonNgu.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thông tin từ các danh sách!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nxb = cboNxb.getSelectedItem().toString();
            String theLoai = cboTheLoai.getSelectedItem().toString();
            String tacGia = cboTacGia.getSelectedItem().toString();
            String ngonNgu = cboNgonNgu.getSelectedItem().toString();

            // Kiểm tra xem mã sản phẩm đã tồn tại chưa
            if (sanPhamRepo.kiemTraTonTai(maSP)) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lưu chỉ tên file ảnh vào database
            String hinhAnh = (selectedImageName != null) ? selectedImageName : "";
            // Trạng thái sản phẩm (true = Đang bán, false = Ngưng bán)
            boolean trangThai = rdoDangBan.isSelected();

            // Tạo đối tượng sản phẩm
            SanPham sp = new SanPham(maSP, tenSP, giaBan, hinhAnh, soLuong, theLoai, ngonNgu, nxb, tacGia, trangThai);

            // Thêm sản phẩm vào database
            if (sanPhamRepo.addSanPham(sp)) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadSanPham(sanPhamRepo.getAllSanPham()); // Tải lại danh sách sản phẩm trong ViewSanPham
                lamMoiForm();  // Làm mới form sau khi thêm

                // Thông báo cho các listener (ví dụ: ViewBanHang) cập nhật
                if (sanPhamUpdateListener != null) {
                    sanPhamUpdateListener.onSanPhamUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnThemActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            return; // Không chọn hàng nào thì thoát luôn
        }

        txtMa.setText(tblSanPham.getValueAt(selectedRow, 0).toString());
        txtTenSach.setText(tblSanPham.getValueAt(selectedRow, 1).toString());
        txtGiaBan.setText(tblSanPham.getValueAt(selectedRow, 8).toString());
        txtSoLuong.setText(tblSanPham.getValueAt(selectedRow, 2).toString());
        cboNxb.setSelectedItem(tblSanPham.getValueAt(selectedRow, 7).toString());
        cboTheLoai.setSelectedItem(tblSanPham.getValueAt(selectedRow, 5).toString());
        cboTacGia.setSelectedItem(tblSanPham.getValueAt(selectedRow, 6).toString());
        cboNgonNgu.setSelectedItem(tblSanPham.getValueAt(selectedRow, 4).toString());
        // Lấy tên file ảnh từ cột HinhAnh trong JTable
        Object value = tblSanPham.getValueAt(selectedRow, 3);
        String imageName = "";

        if (value instanceof String) {
            imageName = value.toString();  // Nếu giá trị là chuỗi, lấy luôn
        } else if (value instanceof ImageIcon) {
            // Nếu là ImageIcon, cần lấy từ database hoặc model thay vì JTable
            imageName = layTenAnhTuDatabase(txtMa.getText());
        }

        System.out.println("Tên ảnh đúng: " + imageName); // Debug
        loadImage(imageName);

        // Kiểm tra trạng thái dựa trên chuỗi
        String trangThai = tblSanPham.getValueAt(selectedRow, 9).toString();
        if (trangThai.equalsIgnoreCase("Đang bán")) {
            rdoDangBan.setSelected(true);
        } else {
            rdoNgungBan.setSelected(true);
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void cboNxbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNxbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboNxbActionPerformed

    private void btnLamMoiTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiTheLoaiActionPerformed
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiTheLoaiActionPerformed

    private void btnLamMoiNgonNguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNgonNguActionPerformed
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiNgonNguActionPerformed

    private void tblNXBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNXBMouseClicked
        int selectedRow = tblNXB.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có dòng nào được chọn không
            String maNXB = tblNXB.getValueAt(selectedRow, 1).toString();
            String tenNXB = tblNXB.getValueAt(selectedRow, 2).toString();

            txtMaNXB.setText(maNXB);
            txtTenNXB.setText(tenNXB);
            // Kiểm tra trạng thái dựa trên chuỗi
            String trangThai = tblNXB.getValueAt(selectedRow, 3).toString();
            if (trangThai.equalsIgnoreCase("Đang bán")) {
                rdoDangBanNXB.setSelected(true);
            } else {
                rdoNgungBanNXB.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblNXBMouseClicked

    private void btnThemNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNXBActionPerformed
        String ma = txtMaNXB.getText().trim();
        String ten = txtTenNXB.getText().trim();

        // Kiểm tra điều kiện đầu vào
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra mã thể loại có hợp lệ không
        String regexMa = "^[a-zA-Z0-9]+$";  // Không dấu, không khoảng trắng, không ký tự đặc biệt
        String regexTen = "^[\\p{L}0-9\\s]+$"; // Cho phép chữ Unicode, số và khoảng trắng

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã nxb không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên nxb không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng mã
        if (nxbdao.isMaNXBExists(ma)) {
            JOptionPane.showMessageDialog(this, "Mã nxb đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào CSDL
        NXB nxb = new NXB(0, ma, ten, true);
        if (nxbdao.insertNXB(nxb)) {
            JOptionPane.showMessageDialog(this, "Thêm nxb thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // 🔹 Cập nhật ngay combobox thay vì load lại toàn bộ
            cboNxb.addItem(ten);

            loadDataNXB();

        } else {
            JOptionPane.showMessageDialog(this, "Thêm nxb thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThemNXBActionPerformed

    private void btnSuaNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNXBActionPerformed
        String ma = txtMaNXB.getText().trim();
        String ten = txtTenNXB.getText().trim();

        String regexMa = "^[a-zA-Z0-9]+$";
        String regexTen = "^[\\p{L}0-9\\s]+$";

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nxb cần sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã nxb chỉ được chứa chữ cái và số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên nxb không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy trạng thái từ giao diện
        boolean trangThai = rdoDangBanNXB.isSelected();
        System.out.println("Trạng thái trước khi cập nhật: " + trangThai);

        // Cập nhật vào CSDL
        NXB nxb = new NXB(0, ma, ten, trangThai);
        if (nxbdao.updateNXB(nxb)) {
            JOptionPane.showMessageDialog(this, "Cập nhật nxb thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Load lại dữ liệu trên bảng
            loadDataNXB();

            // Cập nhật trực tiếp combobox thay vì load lại toàn bộ
            loadComboboxData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật nxb thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSuaNXBActionPerformed

    private void btnXoaNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNXBActionPerformed
        int selectedRow = tblNXB.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nxb cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nxb này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maNXB = txtMaNXB.getText().trim();
            if (nxbdao.deleteNXB(maNXB)) { // Gọi hàm xóa từ repository
                JOptionPane.showMessageDialog(this, "Xóa nxb thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadDataNXB(); // Cập nhật lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thể loại thất bại! Hãy kiểm tra ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        loadComboboxData();
    }//GEN-LAST:event_btnXoaNXBActionPerformed

    private void btnLamMoiNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNXBActionPerformed
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiNXBActionPerformed

    private void txtTimKiemNXBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemNXBKeyReleased
        String keyword = txtTimKiemNXB.getText();
        DefaultTableModel model1 = (DefaultTableModel) tblNXB.getModel();
        model1.setRowCount(0);
        List<NXB> list = nxbdao.searchNXB(keyword);
        for (NXB tg : list) {
            model1.addRow(new Object[]{tg.getId(), tg.getMaNXB(), tg.getTenNXB(), tg.isTrangThai() ? "Đang bán" : "Ngưng bán"});
        }
    }//GEN-LAST:event_txtTimKiemNXBKeyReleased

    private void tblTacGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTacGiaMouseClicked
        int selectedRow = tblTacGia.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có dòng nào được chọn không
            String maTacGia = tblTacGia.getValueAt(selectedRow, 1).toString();
            String tenTacGia = tblTacGia.getValueAt(selectedRow, 2).toString();

            txtMaTacGia.setText(maTacGia);
            txtTenTacGia.setText(tenTacGia);
            // Kiểm tra trạng thái dựa trên chuỗi
            String trangThai = tblTacGia.getValueAt(selectedRow, 3).toString();
            if (trangThai.equalsIgnoreCase("Đang bán")) {
                rdoDangBanTacGia.setSelected(true);
            } else {
                rdoNgungBanTacGia.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblTacGiaMouseClicked

    private void btnThemTacGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemTacGiaActionPerformed
        String ma = txtMaTacGia.getText().trim();
        String ten = txtTenTacGia.getText().trim();

        // Kiểm tra điều kiện đầu vào
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra mã thể loại có hợp lệ không
        String regexMa = "^[a-zA-Z0-9]+$";  // Không dấu, không khoảng trắng, không ký tự đặc biệt
        String regexTen = "^[\\p{L}0-9\\s.]+$"; // Cho phép chữ Unicode, số và khoảng trắng

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã tác giả không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên tác giả không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng mã
        if (tgdao.isMaTacGiaExists(ma)) {
            JOptionPane.showMessageDialog(this, "Mã tác giả đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào CSDL
        TacGia tacGia = new TacGia(0, ma, ten, true);
        if (tgdao.insertTacGia(tacGia)) {
            JOptionPane.showMessageDialog(this, "Thêm tác giả thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // 🔹 Cập nhật ngay combobox thay vì load lại toàn bộ
            cboTacGia.addItem(ten);

            loadDataTacGia();

        } else {
            JOptionPane.showMessageDialog(this, "Thêm tác giả thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThemTacGiaActionPerformed

    private void btnSuaTacGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTacGiaActionPerformed
        String ma = txtMaTacGia.getText().trim();
        String ten = txtTenTacGia.getText().trim();

        String regexMa = "^[a-zA-Z0-9]+$";
        String regexTen = "^[\\p{L}0-9\\s.]+$";

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tác giả  cần sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ma.matches(regexMa)) {
            JOptionPane.showMessageDialog(this, "Mã tác giả chỉ được chứa chữ cái và số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ten.matches(regexTen)) {
            JOptionPane.showMessageDialog(this, "Tên tác giả không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy trạng thái từ giao diện
        boolean trangThai = rdoDangBanTacGia.isSelected();
        System.out.println("Trạng thái trước khi cập nhật: " + trangThai);

        // Cập nhật vào CSDL
        TacGia tacGia = new TacGia(0, ma, ten, trangThai);
        if (tgdao.updateTacGia(tacGia)) {
            JOptionPane.showMessageDialog(this, "Cập nhật tác giả thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Load lại dữ liệu trên bảng
            loadDataTacGia();

            // Cập nhật trực tiếp combobox thay vì load lại toàn bộ
            loadComboboxData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật tác giả thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSuaTacGiaActionPerformed

    private void btnXoaTacGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTacGiaActionPerformed
        int selectedRow = tblTacGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tác giả cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tác giả này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maTacGia = txtMaTacGia.getText().trim();
            if (tgdao.deleteTacGia(maTacGia)) { // Gọi hàm xóa từ repository
                JOptionPane.showMessageDialog(this, "Xóa tác giả thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadDataTacGia(); // Cập nhật lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa tác giả thất bại! Hãy kiểm tra ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        loadComboboxData();
    }//GEN-LAST:event_btnXoaTacGiaActionPerformed

    private void btnLamMoiTacGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiTacGiaActionPerformed
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiTacGiaActionPerformed

    private void txtTimKiemTacGiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemTacGiaKeyReleased
        String keyword = txtTimKiemTacGia.getText();
        DefaultTableModel model1 = (DefaultTableModel) tblTacGia.getModel();
        model1.setRowCount(0);
        List<TacGia> list = tgdao.searchTacGia(keyword);
        for (TacGia tl : list) {
            model1.addRow(new Object[]{tl.getID(), tl.getMaTacGia(), tl.getTenTacGia(), tl.isTrangThai() ? "Đang bán" : "Ngưng bán"});
        }
    }//GEN-LAST:event_txtTimKiemTacGiaKeyReleased

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed
    private void timKiemSanPham(String tuKhoa) {
//        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
//        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng
//
//        List<SanPham> dsSanPham = sanPhamRepo.timKiemSanPham(tuKhoa); // Gọi DAO để lấy danh sách sản phẩm phù hợp
//
//        for (SanPham sp : dsSanPham) {
//
//            ImageIcon imageIcon = null;
//            if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) {
//                String imagePath = "src/images/" + sp.getHinhAnh(); // Đường dẫn ảnh trong project
//                ImageIcon originalIcon = new ImageIcon(imagePath);
//                Image img = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize ảnh
//                imageIcon = new ImageIcon(img);
//            }
//            model.addRow(new Object[]{
//                sp.getMaSanPham(), sp.getTenSanPham(), sp.getSoLuong(),
//                imageIcon, sp.getTenNgonNgu(), sp.getTenTheLoai(),
//                sp.getTenTacGia(), sp.getTenNxb(), sp.getGiaBan(),
//                sp.isTrangThai() ? "Còn hàng" : "Hết hàng"
//            });
//        }
//
//        // Thiết lập chiều cao dòng để ảnh hiển thị rõ
//        tblSanPham.setRowHeight(80);

    }

    private ImageIcon loadImage(String imageName, int width, int height) {
        if (imageName != null && !imageName.trim().isEmpty()) {
            String imagePath = "src/images/" + imageName;
            File file = new File(imagePath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        }
        return null;
    }
    private Timer timer; // Timer để tránh gọi tìm kiếm quá nhiều lần
    private String selectedImageName = null;

    private String layTenAnhTuDatabase(String maSP) {
        String imageName = "";
        String sql = "SELECT HinhAnh FROM SanPham WHERE MaSanPham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                imageName = rs.getString("HinhAnh");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageName;
    }

    private void loadImage(String imageName) {
        if (imageName != null && !imageName.trim().isEmpty()) {
            // Ghép đường dẫn đầy đủ
            String imagePath = "src/images/" + imageName;

            File file = new File(imagePath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(lblHinhAnh.getWidth(), lblHinhAnh.getHeight(), Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(img));
            } else {
                System.out.println("Không tìm thấy file ảnh: " + imagePath);
                lblHinhAnh.setIcon(null);
            }
        } else {
            lblHinhAnh.setIcon(null);
        }
    }

    private void lamMoiForm() {
        txtMa.setText("");
        txtTenSach.setText("");
        txtSoLuong.setText("");
        txtGiaBan.setText("");

        // Reset combobox về trạng thái mặc định
        cboNgonNgu.setSelectedIndex(0);
        cboTheLoai.setSelectedIndex(0);
        cboTacGia.setSelectedIndex(0);
        cboNxb.setSelectedIndex(0);

        // Bỏ chọn tất cả radio button
        buttonGroup1.clearSelection(); // btgTrangThai là ButtonGroup của radio button
        btngroupTTTheLoai.clearSelection();
        btnGroupTTNgonNgu.clearSelection();
        btnGroupTTNXB.clearSelection();
        btnGroupTTTacGia.clearSelection();
        // Xóa ảnh sản phẩm (nếu có)
        lblHinhAnh.setIcon(null);

        // Xóa dữ liệu tìm kiếm trên ô tìm kiếm (nếu có)
        txtTimKiem.setText("");

        // Cập nhật lại focus vào ô "Mã sản phẩm"
        txtMa.requestFocus();

        //theloai
        txtMaTheLoai.setText("");
        txtTenTheLoai.setText("");
        //ngonngu
        txtMaNgonNgu.setText("");
        txtTenNgonNgu.setText("");
        //nxb
        txtMaNXB.setText("");
        txtTenNXB.setText("");
        //tacgia
        txtMaTacGia.setText("");
        txtTenTacGia.setText("");
    }

    private void loadDataTheLoai() {
        DefaultTableModel model = (DefaultTableModel) tblTheLoai.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<TheLoai> list = dao.getAllTheLoai();
        for (TheLoai tl : list) {
            String trangThai = tl.isTrangThai() ? "Đang bán" : "Ngưng bán"; // Chuyển trạng thái về dạng chữ
            model.addRow(new Object[]{tl.getID(), tl.getMaTheLoai(), tl.getTenTheLoai(), trangThai});
        }
    }

    private void initTableTheLoai() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Mã", "Tên", "Trạng thái"}); // Thêm cột "Trạng thái"
        tblTheLoai.setModel(model);
    }

    //bảng NgonNgu
    private void loadDataNgonNgu() {
        DefaultTableModel model = (DefaultTableModel) tblNgonNgu.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<NgonNgu> list = nndao.getAllNgonNgu();
        for (NgonNgu nn : list) {
            String trangThai = nn.isTrangThai() ? "Đang bán" : "Ngưng bán"; // Chuyển trạng thái về dạng chữ
            model.addRow(new Object[]{nn.getID(), nn.getMaNgonNgu(), nn.getTenNgonNgu(), trangThai});
        }
    }

    private void initTableNgonNgu() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Mã", "Tên", "Trạng thái"}); // Thêm cột "Trạng thái"
        tblNgonNgu.setModel(model);
    }

    //bảng NXB
    private void loadDataNXB() {
        DefaultTableModel model = (DefaultTableModel) tblNXB.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<NXB> list = nxbdao.getAllNXB();
        for (NXB nxb : list) {
            String trangThai = nxb.isTrangThai() ? "Đang bán" : "Ngưng bán"; // Chuyển trạng thái về dạng chữ
            model.addRow(new Object[]{nxb.getId(), nxb.getMaNXB(), nxb.getTenNXB(), trangThai});
        }
    }

    private void initTableNXB() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Mã", "Tên", "Trạng thái"}); // Thêm cột "Trạng thái"
        tblNXB.setModel(model);
    }

    //bảng TacGia
    private void loadDataTacGia() {
        DefaultTableModel model = (DefaultTableModel) tblTacGia.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<TacGia> list = tgdao.getAllTacGia();
        for (TacGia tg : list) {
            String trangThai = tg.isTrangThai() ? "Đang bán" : "Ngưng bán"; // Chuyển trạng thái về dạng chữ
            model.addRow(new Object[]{tg.getID(), tg.getMaTacGia(), tg.getTenTacGia(), trangThai});
        }
    }

    private void initTableTacGia() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Mã", "Tên", "Trạng thái"}); // Thêm cột "Trạng thái"
        tblTacGia.setModel(model);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnChonAnh;
    private javax.swing.ButtonGroup btnGroupTTNXB;
    private javax.swing.ButtonGroup btnGroupTTNgonNgu;
    private javax.swing.ButtonGroup btnGroupTTTacGia;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLamMoiNXB;
    private javax.swing.JButton btnLamMoiNgonNgu;
    private javax.swing.JButton btnLamMoiTacGia;
    private javax.swing.JButton btnLamMoiTheLoai;
    private javax.swing.JButton btnSuaNXB;
    private javax.swing.JButton btnSuaNgonNgu;
    private javax.swing.JButton btnSuaTacGia;
    private javax.swing.JButton btnSuaTheLoai;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemNXB;
    private javax.swing.JButton btnThemNgonNgu;
    private javax.swing.JButton btnThemTacGia;
    private javax.swing.JButton btnThemTheLoai;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaNXB;
    private javax.swing.JButton btnXoaNgonNgu;
    private javax.swing.JButton btnXoaTacGia;
    private javax.swing.JButton btnXoaTheLoai;
    private javax.swing.ButtonGroup btngroupTTTheLoai;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboNgonNgu;
    private javax.swing.JComboBox<String> cboNxb;
    private javax.swing.JComboBox<String> cboTacGia;
    private javax.swing.JComboBox<String> cboTheLoai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JRadioButton rdoDangBan;
    private javax.swing.JRadioButton rdoDangBanNXB;
    private javax.swing.JRadioButton rdoDangBanNgonNgu;
    private javax.swing.JRadioButton rdoDangBanTacGia;
    private javax.swing.JRadioButton rdoDangBanTheLoai;
    private javax.swing.JRadioButton rdoNgungBan;
    private javax.swing.JRadioButton rdoNgungBanNXB;
    private javax.swing.JRadioButton rdoNgungBanNgonNgu;
    private javax.swing.JRadioButton rdoNgungBanTacGia;
    private javax.swing.JRadioButton rdoNgungBanTheLoai;
    private javax.swing.JTable tblNXB;
    private javax.swing.JTable tblNgonNgu;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblTacGia;
    private javax.swing.JTable tblTheLoai;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMaNXB;
    private javax.swing.JTextField txtMaNgonNgu;
    private javax.swing.JTextField txtMaTacGia;
    private javax.swing.JTextField txtMaTheLoai;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenNXB;
    private javax.swing.JTextField txtTenNgonNgu;
    private javax.swing.JTextField txtTenSach;
    private javax.swing.JTextField txtTenTacGia;
    private javax.swing.JTextField txtTenTheLoai;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiemNXB;
    private javax.swing.JTextField txtTimKiemNgonNgu;
    private javax.swing.JTextField txtTimKiemTacGia;
    private javax.swing.JTextField txtTimKiemTheLoai;
    // End of variables declaration//GEN-END:variables
}
