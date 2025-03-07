/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewChucNangChinh;

import java.io.FileNotFoundException;
// Import từ các lớp Model
import Model.HoaDon;
import Model.HoaDonChiTiet;
import Model.KhachHang;
import Model.NhanVien;
import Model.PhieuGiamGia;
import Model.SanPham;
import Model.TableCellListener;

// Import từ các lớp Repository
import Repository.HoaDonChiTietRepo;
import Repository.HoaDonRepo;
import Repository.KhachHangRepo;
import Repository.NhanVienRepo;
import Repository.PhieuGiamGiaRepo;
import Repository.SanPhamRepo;

// Import từ các lớp Utility
import Utils.Auth;
import Utils.Validate;

// Import các lớp Java tiêu chuẩn
import static java.awt.Color.WHITE;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

// Import từ thư viện iTextPDF
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// Import từ Swing và Table
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hoang
 */
public class ViewBanHang extends javax.swing.JPanel {

    private boolean isSPfilter1;
    private boolean isSPfilter2;
    private boolean isSPfilter3;
    private boolean isSPfilter4;
    boolean isNVSearch, isKHSearch, isTKDateSearch, isTKIDSearch, isSPfilter = false;
    private PhieuGiamGiaRepo phieuGiamGiaRepo = new PhieuGiamGiaRepo();
    private NhanVienRepo nhanVienRepo = new NhanVienRepo();

    private NhanVienUpdateListener nhanVienUpdateListener;

    public void registerNhanVienListener(ViewNhanVien viewNhanVien) {
        viewNhanVien.setNhanVienUpdateListener(new NhanVienUpdateListener() {
            @Override
            public void onNhanVienUpdated() {
                SwingUtilities.invokeLater(() -> {
                    updateNhanVienComboBox(); // Cập nhật lại combobox nhân viên
                    System.out.println("Updated cbxNVBH after NhanVien update.");
                });
            }
        });
    }

    public void registerPhieuGiamGiaListener(ViewPhieuGiamGia viewPhieuGiamGia) {
        this.setPhieuGiamGiaUpdateListener(new PhieuGiamGiaUpdateListener() {
            @Override
            public void onPhieuGiamGiaUpdated() {
                viewPhieuGiamGia.refreshTableData(); // Gọi làm mới bảng ngay lập tức
                System.out.println("Thông báo cập nhật từ ViewBanHang đến ViewPhieuGiamGia");
            }
        });
    }

    private void updateNhanVienComboBox() {
        cbxNVBH.removeAllItems(); // Xóa tất cả mục cũ
        cbxNVBH.addItem(""); // Thêm mục trống mặc định

        ArrayList<NhanVien> nhanVienList = nhanVienRepo.getActiveNhanVien(); // Lấy danh sách nhân viên đang làm
        java.util.HashSet<String> uniqueMaNhanVien = new java.util.HashSet<>(); // Đảm bảo không trùng lặp

        for (NhanVien nv : nhanVienList) {
            String maNhanVien = nv.getMaNhanVien();
            if (maNhanVien != null && !maNhanVien.trim().isEmpty() && uniqueMaNhanVien.add(maNhanVien)) {
                cbxNVBH.addItem(maNhanVien); // Thêm mã nhân viên
            }
        }

        cbxNVBH.setSelectedIndex(0); // Đặt mặc định là mục trống
        cbxNVBH.revalidate(); // Cập nhật giao diện
        cbxNVBH.repaint();    // Vẽ lại giao diện
    }
    private KhachHangRepo khachHangRepo = KhachHangRepo.getInstance();

    private SanPhamRepo sanPhamRepo = new SanPhamRepo();

    private SanPhamUpdateListener sanPhamUpdateListener;

    public void setSanPhamUpdateListener(SanPhamUpdateListener listener) {
        this.sanPhamUpdateListener = listener;
    }

    public void updateSanPhamList() {
        if (isSPfilter && filteredSPList != null) {
            filteredSPList = sanPhamRepo.searchSanPham(txtLocSP.getText().trim());
            loadDataDSSP(filteredSPList);
        } else {
            loadDataDSSP(sanPhamRepo.getListSP()); // Chỉ tải sản phẩm Đang bán
        }
    }
    private HoaDonRepo hoaDonRepo = new HoaDonRepo();
    private HoaDonChiTietRepo hoaDonChiTietRepo = new HoaDonChiTietRepo();
    private ArrayList<SanPham> filteredSPList = null;
    private HoaDonUpdateListener hoaDonUpdateListener;

    public void setHoaDonUpdateListener(HoaDonUpdateListener listener) {
        this.hoaDonUpdateListener = listener;
    }

    private void notifyHoaDonUpdated() {
        if (hoaDonUpdateListener != null) {
            hoaDonUpdateListener.onHoaDonUpdated();
        }
    }

    private PhieuGiamGiaUpdateListener updateListener;

    // Đăng ký listener từ bên ngoài
    public void setPhieuGiamGiaUpdateListener(PhieuGiamGiaUpdateListener listener) {
        this.updateListener = listener;
    }

// Phương thức cập nhật combobox phiếu giảm giá
    public void updatePhieuGiamGiaComboBox() {
        cbxPhieuGiamGia.removeAllItems(); // Xóa tất cả các mục cũ
        cbxPhieuGiamGia.addItem(""); // Thêm mục trống mặc định
        ArrayList<PhieuGiamGia> danhSachPhieuGiamGia = phieuGiamGiaRepo.getAll();
        for (PhieuGiamGia phieu : danhSachPhieuGiamGia) {
            if (phieu.isTrangThai()) { // Chỉ thêm các phiếu chưa hết hạn (TrangThai = true)
                cbxPhieuGiamGia.addItem(phieu.getTenPhieuGiamGia());
            }
        }
        cbxPhieuGiamGia.setSelectedIndex(0); // Chọn mục trống làm mặc định
    }

    public ViewBanHang() {
        initComponents();
        loadDataDSSP(sanPhamRepo.getListSP());
        loadDataDSHD(hoaDonRepo.getListHDCHT());
        updatePhieuGiamGiaComboBox();
        updateNhanVienComboBox();
        // Điền dữ liệu vào combobox phiếu giảm giá với tên thay vì mã

        // Điền dữ liệu vào combobox nhân viên bán hàng nhưng không chọn mặc định
//        ArrayList<NhanVien> danhSachNhanVien = nhanVienRepo.getActiveNhanVien();
//        cbxNVBH.addItem(""); // Thêm một mục trống làm mặc định
//        for (NhanVien nv : danhSachNhanVien) {
//            cbxNVBH.addItem(nv.getMaNhanVien());
//        }
//        cbxNVBH.setSelectedIndex(0); // Chọn mục trống làm mặc định
        // Đảm bảo txtCanTra trống khi khởi tạo
        txtCanTra.setText("");

        addTableGioHangCellListener();

        // Thêm sự kiện thay đổi mã nhân viên
        cbxNVBH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNVBHActionPerformed(evt);
            }
        });
    }

// Thêm phương thức xử lý sự kiện thay đổi mã nhân viên
    private void cbxNVBHActionPerformed(java.awt.event.ActionEvent evt) {
        String maHD = txtMaHD.getText();
        if (maHD.isEmpty()) {
            return; // Không làm gì nếu chưa có hóa đơn
        }

        HoaDon hoaDon = hoaDonRepo.getHoaDonByMa(maHD);
        if (hoaDon != null && !hoaDon.getTrangThai()) { // Chỉ cho phép thay đổi nếu hóa đơn chưa thanh toán
            String maNhanVienMoi = (String) cbxNVBH.getSelectedItem();
            if (maNhanVienMoi != null && !maNhanVienMoi.equals(hoaDon.getMaNhanVien())) {
                hoaDon.setMaNhanVien(maNhanVienMoi);
                hoaDonRepo.updateNhanVienHD(maHD, maNhanVienMoi); // Cập nhật mã nhân viên trong DB
                JOptionPane.showMessageDialog(this, "Đã cập nhật mã nhân viên thành: " + maNhanVienMoi);
                loadDataDSHD(hoaDonRepo.getListHDCHT()); // Cập nhật lại danh sách hóa đơn
            }
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
        panelHD = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtLocSP = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDSSP = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        panelTaoHoaDon = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtTenKHHD = new javax.swing.JTextField();
        txtSDTHD = new javax.swing.JTextField();
        lbTong = new javax.swing.JLabel();
        lbCanTra = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        txtTienThua = new javax.swing.JTextField();
        btnThanhToan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        txtTong = new javax.swing.JTextField();
        txtCanTra = new javax.swing.JTextField();
        btnResetHD = new javax.swing.JButton();
        btnTaoHD = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbxPhieuGiamGia = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbxNVBH = new javax.swing.JComboBox<>();
        btnInBill = new javax.swing.JButton();
        rdoChuaTT = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDSHD = new javax.swing.JTable();
        rdoDaTT = new javax.swing.JRadioButton();
        btnXoaSPGH = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(695, 600));
        setPreferredSize(new java.awt.Dimension(750, 650));

        panelHD.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));

        area.setColumns(20);
        area.setRows(5);
        jScrollPane1.setViewportView(area);

        panelHD.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 388, 0, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Tìm kiếm");

        txtLocSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocSPActionPerformed(evt);
            }
        });
        txtLocSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLocSPKeyReleased(evt);
            }
        });

        tblDSSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sản Phẩm", "Sản Phẩm", "Thể loại", "Ngôn ngữ", "Nhà xuất bản", "Tác giả", "Tồn Kho"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSSPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDSSP);
        if (tblDSSP.getColumnModel().getColumnCount() > 0) {
            tblDSSP.getColumnModel().getColumn(0).setResizable(false);
            tblDSSP.getColumnModel().getColumn(1).setResizable(false);
            tblDSSP.getColumnModel().getColumn(2).setResizable(false);
            tblDSSP.getColumnModel().getColumn(3).setResizable(false);
            tblDSSP.getColumnModel().getColumn(4).setResizable(false);
            tblDSSP.getColumnModel().getColumn(5).setResizable(false);
            tblDSSP.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        panelHD.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 423, -1, -1));

        jLabel21.setText("Danh sách sản phẩm");
        panelHD.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 370, -1, -1));

        jLabel22.setText("Giỏ hàng");
        panelHD.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 198, -1, -1));

        panelTaoHoaDon.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel23.setText("Đơn hàng");

        jLabel24.setText("Tên KH:");

        jLabel25.setText("SĐT:");

        txtTenKHHD.setEnabled(false);

        txtSDTHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTHDActionPerformed(evt);
            }
        });

        lbTong.setText("Tổng tiền hàng: ");

        lbCanTra.setText("Thành tiền: ");

        jLabel29.setText("Tiền khách đưa:");

        jLabel30.setText("Tiền thừa:");

        txtTienKhachDua.setEnabled(false);
        txtTienKhachDua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKhachDuaActionPerformed(evt);
            }
        });

        txtTienThua.setEditable(false);
        txtTienThua.setEnabled(false);

        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnHuy.setText("Huỷ");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        jLabel50.setText("Mã HĐ:");

        txtMaHD.setEditable(false);
        txtMaHD.setEnabled(false);

        txtTong.setEditable(false);
        txtTong.setEnabled(false);
        txtTong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongActionPerformed(evt);
            }
        });

        txtCanTra.setEditable(false);
        txtCanTra.setEnabled(false);
        txtCanTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanTraActionPerformed(evt);
            }
        });

        btnResetHD.setText("Reset");
        btnResetHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetHDActionPerformed(evt);
            }
        });

        btnTaoHD.setText("Tạo hoá đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        jLabel1.setText("Phiếu giảm giá: ");

        cbxPhieuGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPhieuGiamGiaActionPerformed(evt);
            }
        });

        jLabel3.setText("NVBH:");

        btnInBill.setText("IN BILL");
        btnInBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTaoHoaDonLayout = new javax.swing.GroupLayout(panelTaoHoaDon);
        panelTaoHoaDon.setLayout(panelTaoHoaDonLayout);
        panelTaoHoaDonLayout.setHorizontalGroup(
            panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaoHoaDonLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnHuy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnThanhToan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTaoHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnResetHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                                    .addComponent(lbCanTra)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                                    .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel29)
                                        .addComponent(jLabel30))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                        .addComponent(txtTienKhachDua))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaoHoaDonLayout.createSequentialGroup()
                                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaoHoaDonLayout.createSequentialGroup()
                                        .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel50)
                                                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel24)
                                                    .addComponent(jLabel25)))
                                            .addComponent(jLabel3)
                                            .addComponent(lbTong))
                                        .addGap(6, 6, 6))
                                    .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(2, 2, 2)))
                                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTong, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSDTHD)
                                    .addComponent(txtTenKHHD)
                                    .addComponent(txtMaHD)
                                    .addComponent(cbxNVBH, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxPhieuGiamGia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel23)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaoHoaDonLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnInBill, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        panelTaoHoaDonLayout.setVerticalGroup(
            panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(21, 21, 21)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtTenKHHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel25))
                    .addGroup(panelTaoHoaDonLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtSDTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxNVBH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTong)
                    .addComponent(txtTong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxPhieuGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCanTra, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCanTra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(12, 12, 12)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan)
                    .addComponent(btnTaoHD))
                .addGap(18, 18, 18)
                .addGroup(panelTaoHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuy)
                    .addComponent(btnResetHD))
                .addGap(18, 18, 18)
                .addComponent(btnInBill, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        panelHD.add(panelTaoHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(706, 10, -1, 590));

        buttonGroup1.add(rdoChuaTT);
        rdoChuaTT.setSelected(true);
        rdoChuaTT.setText("Chưa thanh toán");
        rdoChuaTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChuaTTActionPerformed(evt);
            }
        });
        panelHD.add(rdoChuaTT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 19, -1, -1));

        tblDSHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Nhân Viên", "Khách Hàng", "Ngày Tạo", "Tổng tiền", "Phiếu giảm giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSHDMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblDSHD);

        panelHD.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 46, 665, 112));

        buttonGroup1.add(rdoDaTT);
        rdoDaTT.setText("Đã thanh toán");
        rdoDaTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDaTTActionPerformed(evt);
            }
        });
        panelHD.add(rdoDaTT, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 19, -1, -1));

        btnXoaSPGH.setText("Xoá");
        btnXoaSPGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPGHActionPerformed(evt);
            }
        });
        panelHD.add(btnXoaSPGH, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 191, -1, -1));

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn giá", "Số Lượng", "Tổng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblGioHang);

        panelHD.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 220, 665, 110));

        jLabel20.setText("Danh sách đơn hàng");
        panelHD.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 21, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(panelHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtLocSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocSPActionPerformed
//        if (ValidateLocSP()) {
//            isSPfilter = true;
//            String keyword = txtLocSP.getText().trim();
//            filteredSPList = sanPhamRepo.searchSanPham(keyword); // Lưu danh sách lọc
//            loadDataDSSP(filteredSPList);
//            if (filteredSPList.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào với từ khóa: " + keyword);
//            }
//        }
    }//GEN-LAST:event_txtLocSPActionPerformed

    private void tblDSSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSSPMouseClicked

        String maHD = txtMaHD.getText();

        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc tạo hóa đơn trước khi thêm sản phẩm!");
            return;
        }

        HoaDon hoaDon = hoaDonRepo.getHoaDonByMa(maHD);
        if (hoaDon != null && hoaDon.getTrangThai()) {
            JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm vì hóa đơn đã thanh toán!");
            return;
        }

        int index = tblDSSP.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm từ danh sách!");
            return;
        }

        ArrayList<SanPham> spList = (isSPfilter && filteredSPList != null && !filteredSPList.isEmpty())
                ? filteredSPList
                : sanPhamRepo.getListSP();

        if (spList == null || spList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách sản phẩm rỗng! Vui lòng kiểm tra dữ liệu.");
            return;
        }

        if (index >= spList.size()) {
            JOptionPane.showMessageDialog(this, "Chỉ số sản phẩm không hợp lệ! Dữ liệu bảng và danh sách không đồng bộ.");
            return;
        }

        SanPham selectedSP = spList.get(index);
        if (selectedSP.getSoLuong() <= 0) {
            JOptionPane.showMessageDialog(this, "Không còn hàng tồn!");
        } else {
            Integer soLuongMua = 1;
            String maSP = selectedSP.getMaSanPham();
            int soLuongTon = selectedSP.getSoLuong() - soLuongMua;
            hoaDonChiTietRepo.addGH(maHD, maSP, soLuongMua);
            sanPhamRepo.updateSP(maSP, soLuongTon);
            loadDataHDCT(hoaDonChiTietRepo.getChiTietByHoaDon(maHD));

            if (isSPfilter && filteredSPList != null) {
                filteredSPList = sanPhamRepo.searchSanPham(txtLocSP.getText().trim());
                loadDataDSSP(filteredSPList);
            } else {
                loadDataDSSP(sanPhamRepo.getListSP());
            }
            sumHD(maHD);

            // Thông báo cập nhật số lượng sản phẩm cho ViewSanPham
            if (sanPhamUpdateListener != null) {
                sanPhamUpdateListener.onSanPhamUpdated();
            }
        }

        if (!txtTienKhachDua.getText().isBlank()) {
            if (ValidateTienKhachDua()) {
                String tienCanTra = txtCanTra.getText().replace(",", "").replace(" VND", "");
                double value = Double.parseDouble(txtTienKhachDua.getText()) - Double.parseDouble(tienCanTra);
                txtTienThua.setText(new DecimalFormat("#,###").format(value) + " VND");
            }
        }

    }//GEN-LAST:event_tblDSSPMouseClicked

    private void txtSDTHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTHDActionPerformed

        if (ValidateSDTHD()) {
            String phoneNum = txtSDTHD.getText();
            if (khachHangRepo == null) {
                JOptionPane.showMessageDialog(this, "Lỗi: KhachHangRepo chưa được khởi tạo!");
                return;
            }
            KhachHang kh = khachHangRepo.getKHinfo(phoneNum);
            if (kh.getSdt() != null) {
                txtTenKHHD.setText(kh.getTenKhachHang());
                txtSDTHD.setText(kh.getSdt());
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Khách hàng không tồn tại! Bạn có muốn thêm mới?", "Thêm mới KH", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    String tenKh = null;
                    boolean tenHopLe = false;

                    // Vòng lặp để yêu cầu nhập tên khách hàng cho đến khi hợp lệ
                    while (!tenHopLe) {
                        tenKh = JOptionPane.showInputDialog(this, "Nhập tên KH:", "Thêm mới KH", JOptionPane.INFORMATION_MESSAGE);
                        if (tenKh == null) { // Người dùng nhấn Cancel
                            return; // Thoát nếu nhấn Cancel
                        }
                        tenHopLe = validateTenKH(tenKh); // Kiểm tra tên
                    }

                    // Thêm lựa chọn giới tính
                    Object[] gioiTinhOptions = {"Nam", "Nữ"};
                    int gioiTinhChoice = JOptionPane.showOptionDialog(this, "Chọn giới tính:", "Thêm mới KH",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gioiTinhOptions, gioiTinhOptions[0]);
                    if (gioiTinhChoice == -1) { // Người dùng đóng hộp thoại
                        return;
                    }
                    boolean gioiTinh = gioiTinhChoice == 0; // 0 = Nam, 1 = Nữ

                    KhachHang kh1 = new KhachHang();
                    kh1.setTenKhachHang(tenKh.trim()); // Loại bỏ khoảng trắng thừa ở đầu và cuối
                    kh1.setSdt(phoneNum);
                    kh1.setGioiTinh(gioiTinh); // Thiết lập giới tính

                    if (khachHangRepo.addKH(kh1)) {
                        txtTenKHHD.setText(kh1.getTenKhachHang());
                        JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công! Mã KH: " + kh1.getMaKhachHang());
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại! Vui lòng kiểm tra log.");
                    }
                }
            }
        }

    }//GEN-LAST:event_txtSDTHDActionPerformed

    private void txtTienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachDuaActionPerformed
        if (ValidateTienKhachDua()) {
            String tienCanTra = txtCanTra.getText().replace(",", "").replace(" VND", "");
            double value = Double.parseDouble(txtTienKhachDua.getText()) - Double.parseDouble(tienCanTra);
            txtTienThua.setText(new DecimalFormat("#,###").format(value) + " VND");
        }
    }//GEN-LAST:event_txtTienKhachDuaActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed

        String tenPhieuGiamGia = (String) cbxPhieuGiamGia.getSelectedItem();
        double soTienGiam = 0.0;

        // Lấy số tiền giảm từ phiếu giảm giá
        if (tenPhieuGiamGia != null && !tenPhieuGiamGia.isEmpty()) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepo.getPhieuGiamGiaByTen(tenPhieuGiamGia); // Sử dụng tên
            if (phieuGiamGia != null) {
                soTienGiam = phieuGiamGia.getSoTienGiam();
            }
        }

        // Kiểm tra điều kiện thanh toán
        if (txtMaHD.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panelHD, "Chưa chọn Hoá Đơn!");
        } else if (txtTienKhachDua.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panelHD, "Vui lòng nhập tiền khách đưa!");
        } else {
            double tienKhachDua;
            try {
                tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panelHD, "Tiền khách đưa phải là số hợp lệ!");
                return;
            }

            double canTra;
            try {
                canTra = Double.parseDouble(txtCanTra.getText().replace(",", "").replace(" VND", ""));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panelHD, "Số tiền cần trả không hợp lệ!");
                return;
            }

            if (canTra > tienKhachDua) {
                JOptionPane.showMessageDialog(panelHD, "Tiền khách đưa không đủ!");
            } else {
                String maHD = txtMaHD.getText();
                String tongTienText = txtTong.getText().replaceAll("[^\\d.]", "");
                double tongTien = tongTienText.isEmpty() ? 0.0 : Double.parseDouble(tongTienText);
                thanhToan(maHD, soTienGiam, tongTien);
            }
        }

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed

        if (txtMaHD.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panelHD, "Chưa chọn Hoá Đơn!");
        } else {
            // Hiển thị hộp thoại xác nhận
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn huỷ hoá đơn này?", "Xác nhận huỷ", JOptionPane.YES_NO_OPTION);

            // Nếu người dùng chọn "Yes"
            if (confirm == JOptionPane.YES_OPTION) {
                String maHD = txtMaHD.getText();

                // Lấy danh sách chi tiết hóa đơn
                ArrayList<HoaDonChiTiet> hdctList = hoaDonChiTietRepo.getChiTietByHoaDon(maHD);

                // Cập nhật lại số lượng tồn kho cho từng sản phẩm trong giỏ hàng
                for (HoaDonChiTiet hdct : hdctList) {
                    String maSP = hdct.getMaSanPham();
                    int soLuongMua = hdct.getSoLuong();

                    // Lấy số lượng tồn kho hiện tại của sản phẩm
                    ArrayList<SanPham> sanPhamList = sanPhamRepo.getListSP(maSP);
                    if (sanPhamList != null && !sanPhamList.isEmpty()) {
                        int soLuongTon = sanPhamList.get(0).getSoLuong() + soLuongMua;
                        sanPhamRepo.updateSP(maSP, soLuongTon); // Cập nhật lại số lượng tồn kho
                    }
                }

                // Xóa tất cả chi tiết hóa đơn
                hoaDonChiTietRepo.xoaHDCT(maHD, maHD);

                // Huỷ hóa đơn
                huyHD(maHD);

                // Xóa dữ liệu trong bảng giỏ hàng
                DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
                dtm.setRowCount(0);

                // Xóa dữ liệu trong các trường nhập liệu
                clearHD();

            }
        }


    }//GEN-LAST:event_btnHuyActionPerformed
    private void huyHDCT(ArrayList<HoaDonChiTiet> hdctlist) {
        for (HoaDonChiTiet hdct : hdctlist) {
            Integer soLuongMua = hdct.getSoLuong();
            String maSP = hdct.getMaChiTietHoaDon();
            Integer soLuongTon = sanPhamRepo.getListSP(maSP).get(0).getSoLuong() + soLuongMua;
            sanPhamRepo.updateSP(maSP, soLuongTon);
        }
    }

    private void btnResetHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetHDActionPerformed
        clearHD(); // Reset các trường nhập liệu
        DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
    }//GEN-LAST:event_btnResetHDActionPerformed

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        if (ValidateHD()) {
            addHD();
            txtTienKhachDua.setEnabled(true);
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void cbxPhieuGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPhieuGiamGiaActionPerformed

        if (rdoDaTT.isSelected()) {
            return; // Không chạy logic áp dụng phiếu giảm giá khi ở tab "Đã thanh toán"
        }
        String tenPhieuGiamGia = (String) cbxPhieuGiamGia.getSelectedItem();
        String tongTienText = txtTong.getText().replaceAll("[^\\d.]", "");
        double tongTien = tongTienText.isEmpty() ? 0.0 : Double.parseDouble(tongTienText);

        if (tenPhieuGiamGia != null && !tenPhieuGiamGia.isEmpty()) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepo.getPhieuGiamGiaByTen(tenPhieuGiamGia); // Sử dụng tên
            if (phieuGiamGia == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phiếu giảm giá!");
                return;
            }
            double soTienGiam = phieuGiamGia.getSoTienGiam();
            if (soTienGiam <= 0) {
                JOptionPane.showMessageDialog(this, "Phiếu giảm giá không hợp lệ!");
                return;
            }

            double thanhTien = tongTien - soTienGiam;
            if (thanhTien < 0) {
                thanhTien = 0; // Nếu số tiền giảm lớn hơn tổng tiền, đặt thành tiền về 0
            }
            DecimalFormat df = new DecimalFormat("#,###");
            txtCanTra.setText(df.format(thanhTien) + " VND");
        } else {
            // Nếu chọn giá trị trống, reset thành tiền về tổng tiền ban đầu
            DecimalFormat df = new DecimalFormat("#,###");
            txtCanTra.setText(df.format(tongTien) + " VND");
        }

    }//GEN-LAST:event_cbxPhieuGiamGiaActionPerformed

    private void btnInBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInBillActionPerformed

        // Đảm bảo có hóa đơn hợp lệ được chọn
        String maHD = txtMaHD.getText();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để in bill!");
            return;
        }

        // Lấy thông tin hóa đơn
        HoaDon hoaDon = hoaDonRepo.getHoaDonByMa(maHD);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!");
            return;
        }

        // Lấy chi tiết hóa đơn
        ArrayList<HoaDonChiTiet> hdctList = hoaDonChiTietRepo.getChiTietByHoaDon(maHD);
        if (hdctList == null || hdctList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm trong hóa đơn để in!");
            return;
        }

        // Khai báo columnWidths để sử dụng chung cho bảng sản phẩm
        float[] columnWidths = {0.1f, 0.4f, 0.2f, 0.3f};

        // Tạo tài liệu PDF
        try {
            // Lấy đường dẫn đến thư mục Downloads của người dùng
            String userHome = System.getProperty("user.home");
            String downloadsDir = userHome + File.separator + "Downloads" + File.separator;

            // Tạo thư mục Downloads nếu chưa tồn tại
            File downloadsFolder = new File(downloadsDir);
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdirs();
            }

            // Thiết lập tài liệu và đầu ra tệp lưu vào thư mục Downloads
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(downloadsDir + "HoaDon_" + maHD + ".pdf"));
            document.open();

            // Định dạng ngày với thời gian 00:00
            LocalDateTime ngayTaoWithTime = hoaDon.getNgayTao().atTime(0, 0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 00:00");
            String formattedDate = ngayTaoWithTime.format(dateFormatter);

            int stt = 1;
            DecimalFormat df = new DecimalFormat("#,###"); // Định dạng số tiền
            double total = 0;
            double giamGia = hoaDon.getSoTienGiam(); // Sử dụng trực tiếp từ HoaDon (kiểu double)

            // Cài đặt font
            String fontPath = "/icon/fonts/NotoSans-Regular.ttf"; // Đường dẫn tương đối trong dự án NetBeans
            BaseFont baseFont;
            try {
                URL fontUrl = ViewBanHang.class.getResource(fontPath);
                if (fontUrl == null) {
                    throw new FileNotFoundException("Không tìm thấy file font: " + fontPath);
                }
                String absoluteFontPath = fontUrl.toURI().getPath();
                baseFont = BaseFont.createFont(absoluteFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception e) {
                baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                JOptionPane.showMessageDialog(this, "Không tải được font NotoSans. Sử dụng font mặc định.");
            }
            Font font = new Font(baseFont, 12, Font.NORMAL); // Font mặc định size 12
            Font boldFont = new Font(baseFont, 12, Font.BOLD); // Font in đậm size 12
            Font largeBoldFont = new Font(baseFont, 14, Font.BOLD); // Font lớn hơn cho Tổng tiền và Thành tiền
            Font largeFont = new Font(baseFont, 13, Font.NORMAL); // Font lớn hơn cho Giảm giá

            // Tiêu đề cửa hàng (căn giữa)
            Paragraph storeName = new Paragraph("TIỆM SÁCH LITZONE", new Font(baseFont, 14, Font.BOLD));
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            Paragraph address = new Paragraph("115, Trịnh Văn Bô, Nam Từ Liêm, Hà Nội", font);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            Paragraph phone = new Paragraph("Điện thoại: 0123.456.789", font);
            phone.setAlignment(Element.ALIGN_CENTER);
            document.add(phone);

            Paragraph facebook = new Paragraph("Facebook: Tiệm sách LitZone", font);
            facebook.setAlignment(Element.ALIGN_CENTER);
            document.add(facebook);

            Paragraph bank = new Paragraph("Zalo: 0123.456.789 - LitZone xin chào", font);
            bank.setAlignment(Element.ALIGN_CENTER);
            document.add(bank);

            // Thêm dòng kẻ ngăn cách
            Paragraph line1 = new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------", font);
            line1.setAlignment(Element.ALIGN_CENTER);
            document.add(line1);

            // Thông tin hóa đơn (căn giữa)
            Paragraph invoiceHeader = new Paragraph("PHIẾU BÁN HÀNG", boldFont);
            invoiceHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceHeader);

            Paragraph dateInfo = new Paragraph("Ngày xuất: " + formattedDate, font);
            dateInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(dateInfo);

            // Thông tin nhân viên (căn giữa) - Lấy trực tiếp từ hoaDon
            String maNhanVien = hoaDon.getMaNhanVien() != null ? hoaDon.getMaNhanVien() : "Không có";
            String tenNhanVien = hoaDon.getTenNhanVien() != null ? hoaDon.getTenNhanVien() : "Không có";
            Paragraph employeeInfo = new Paragraph("Nhân viên: " + maNhanVien + " - " + tenNhanVien, font);
            employeeInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(employeeInfo);

            // Thông tin khách hàng (căn giữa)
            String tenKhachHang = hoaDon.getTenKhachHang() != null ? hoaDon.getTenKhachHang() : "Không có";
            String sdt = hoaDon.getSdt() != null ? hoaDon.getSdt() : "Không có";
            Paragraph customerInfo = new Paragraph("Khách hàng: " + tenKhachHang + " - SĐT: " + sdt, font);
            customerInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(customerInfo);

            // Thêm khoảng cách
            document.add(new Paragraph(" ", font));

            // Bảng danh sách sản phẩm
            PdfPTable table = new PdfPTable(4); // 4 cột: STT, Sản phẩm, Số lượng, Thành tiền
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Thiết lập độ rộng cột
            table.setWidths(columnWidths);

            // Tiêu đề cột
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("STT", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sản phẩm", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Số lượng", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Thành tiền (VND)", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            // Dữ liệu sản phẩm
            for (HoaDonChiTiet hdct : hdctList) {
                table.addCell(createCenteredCell(String.valueOf(stt++), font));
                table.addCell(createCenteredCell(hdct.getTenSanPham(), font));
                table.addCell(createCenteredCell(String.valueOf(hdct.getSoLuong()), font));
                String thanhTien = df.format(hdct.getThanhTien()).replace(",", ".");
                table.addCell(createCenteredCell(thanhTien + " VND", font));
                total += hdct.getThanhTien();
            }

            document.add(table);

            // Thêm dòng kẻ ngăn cách
            Paragraph line2 = new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------", font);
            line2.setAlignment(Element.ALIGN_CENTER);
            document.add(line2);

            // Thêm dòng kẻ ngăn cách
            Paragraph line3 = new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------", font);
            line3.setAlignment(Element.ALIGN_CENTER);
            document.add(line3);

            // Tổng tiền (căn phải, size 14, in đậm)
            Paragraph totalLabel = new Paragraph("Tổng tiền hàng: " + df.format(total).replace(",", ".") + " VND", largeBoldFont);
            totalLabel.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalLabel);

            // Giảm giá (nếu có, căn phải, size 13, không in đậm)
            if (giamGia > 0) {
                Paragraph discount = new Paragraph("Giảm giá: " + df.format(giamGia).replace(",", ".") + " VND", largeFont);
                discount.setAlignment(Element.ALIGN_RIGHT);
                document.add(discount);
                total -= giamGia;
            }

            // Thành tiền (căn phải, size 14, in đậm)
            Paragraph finalTotal = new Paragraph("Thành tiền: " + df.format(total).replace(",", ".") + " VND", largeBoldFont);
            finalTotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(finalTotal);

            // Ghi chú (căn giữa)
            Paragraph note = new Paragraph(
                    "\n\n\n\n\n\n\n\n\n\nCảm ơn quý khách đã lựa chọn mua sách tại cửa hàng của chúng tôi!\n"
                    + "LitZone rất vui khi có thể đồng hành cùng quý khách trong hành trình khám phá tri thức\n"
                    + "Hy vọng quý khách sẽ tìm thấy những cuốn sách bổ ích và thú vị\n"
                    + "Chúng tôi mong được tiếp tục phục vụ quý khách trong những lần sau!\n"
                    + "Chúc quý khách đọc sách vui vẻ và có những trải nghiệm tuyệt vời!", font);
            note.setAlignment(Element.ALIGN_CENTER);
            document.add(note);

            document.close();
            JOptionPane.showMessageDialog(this, "In hóa đơn thành công! Tệp đã được lưu vào thư mục Downloads với tên HoaDon_" + maHD + ".pdf");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Phương thức hỗ trợ để tạo cell căn giữa
    private PdfPCell createCenteredCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        return cell;

    }//GEN-LAST:event_btnInBillActionPerformed

    private void rdoChuaTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChuaTTActionPerformed

        loadDataDSHD(hoaDonRepo.getListHDCHT());
        DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        txtTenKHHD.setEnabled(false);
        txtSDTHD.setEnabled(true);
        txtTienKhachDua.setEnabled(false);
        btnThanhToan.setEnabled(true);
        btnHuy.setEnabled(true);
        btnTaoHD.setEnabled(true);
        cbxPhieuGiamGia.setEnabled(true);
        cbxNVBH.setEnabled(true); // Bật cbxNVBH cho phép thay đổi

        if (tblDSHD.getRowCount() > 0) {
            tblDSHD.setRowSelectionInterval(0, 0);
            HoaDon hd = hoaDonRepo.getListHDCHT().get(0);
            fillModelHD(hd);
        } else {
            clearHD();
        }
    }//GEN-LAST:event_rdoChuaTTActionPerformed

    private void tblDSHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSHDMouseClicked
        if (rdoDaTT.isSelected()) {
            int index = tblDSHD.getSelectedRow();
            if (index >= 0) { // Kiểm tra chỉ số hợp lệ
                HoaDon hd = hoaDonRepo.getListHDHT().get(index);
                String maHD = hd.getMaHoaDon();
                loadDataHDCT(hoaDonChiTietRepo.getChiTietByHoaDon(maHD));
                fillModelHD(hd); // Điền dữ liệu bao gồm cbxPhieuGiamGia
            }
        } else if (rdoChuaTT.isSelected()) {
            int index = tblDSHD.getSelectedRow();
            if (index >= 0) { // Kiểm tra chỉ số hợp lệ
                HoaDon hd = hoaDonRepo.getListHDCHT().get(index);
                String maHD = hd.getMaHoaDon();
                loadDataHDCT(hoaDonChiTietRepo.getChiTietByHoaDon(maHD));
                fillModelHD(hd);
                txtTienKhachDua.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblDSHDMouseClicked

    private void rdoDaTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDaTTActionPerformed

        loadDataDSHD(hoaDonRepo.getListHDHT());
        DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);

        txtTenKHHD.setEnabled(false);
        txtSDTHD.setEnabled(false);
        txtTienKhachDua.setEnabled(false);
        btnThanhToan.setEnabled(false);
        btnHuy.setEnabled(false);
        btnTaoHD.setEnabled(false);
        cbxPhieuGiamGia.setEnabled(false);
        cbxNVBH.setEnabled(false); // Khóa cbxNVBH khi hóa đơn đã thanh toán

        if (tblDSHD.getRowCount() > 0) {
            tblDSHD.setRowSelectionInterval(0, 0);
            HoaDon hd = hoaDonRepo.getListHDHT().get(0);
            fillModelHD(hd);
        } else {
            txtMaHD.setText("");
            txtTenKHHD.setText("");
            txtSDTHD.setText("");
            txtTong.setText("");
            txtCanTra.setText("");
            txtTienKhachDua.setText("");
            txtTienThua.setText("");
            cbxNVBH.setSelectedItem(null);
        }

        loadDataDSSP(sanPhamRepo.getListSP());
    }//GEN-LAST:event_rdoDaTTActionPerformed

    private void btnXoaSPGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSPGHActionPerformed

        String maHD = txtMaHD.getText();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã hóa đơn!");
            return;
        }

        // Lấy thông tin hóa đơn từ mã hóa đơn
        HoaDon hoaDon = hoaDonRepo.getHoaDonByMa(maHD);
        if (hoaDon != null && hoaDon.getTrangThai()) {
            JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm vì hóa đơn đã thanh toán!");
            return;
        }

        // Tiến hành xóa sản phẩm trong giỏ hàng
        XoaSPGH();

        // Cập nhật lại danh sách sản phẩm dựa trên trạng thái lọc
        if (isSPfilter && filteredSPList != null) {
            filteredSPList = sanPhamRepo.searchSanPham(txtLocSP.getText().trim());
            loadDataDSSP(filteredSPList);
        } else {
            loadDataDSSP(sanPhamRepo.getListSP());
        }

        // Cập nhật tiền thừa nếu có
        if (!txtTienKhachDua.getText().isBlank()) {
            txtTienKhachDuaActionPerformed(evt);
        }

    }//GEN-LAST:event_btnXoaSPGHActionPerformed

    private void tblGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGioHangMouseClicked

    private void txtTongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongActionPerformed

    private void txtCanTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanTraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanTraActionPerformed

    private void txtLocSPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocSPKeyReleased
        String keyword = txtLocSP.getText().trim();

        if (keyword.isEmpty()) {
            // Nếu từ khóa trống, reset về danh sách sản phẩm ban đầu
            isSPfilter = false;
            filteredSPList = null;
            loadDataDSSP(sanPhamRepo.getListSP());
        } else {
            // Nếu có từ khóa, thực hiện tìm kiếm và cập nhật bảng
            isSPfilter = true;
            filteredSPList = sanPhamRepo.searchSanPham(keyword); // Gọi phương thức tìm kiếm từ repository
            loadDataDSSP(filteredSPList);
        }
    }//GEN-LAST:event_txtLocSPKeyReleased

    public boolean ValidateLocSP() {
        StringBuilder stb = new StringBuilder();
        Validate v = new Validate();

        String keyword = txtLocSP.getText();

        // Kiểm tra xem chuỗi có bắt đầu bằng khoảng trắng hay không
        if (keyword.startsWith(" ")) {
            stb.append("Từ khóa tìm kiếm không được bắt đầu bằng khoảng trắng!\n");
        }

        // Không kiểm tra trống ở đây nữa, để btnLocSPActionPerformed xử lý
        // v.isEmpty(txtLocSP, stb, "Vui lòng nhập từ khóa tìm kiếm!");
        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb.toString());
            return false;
        } else {
            return true;
        }
    }

    private void loadDataDSSP(ArrayList<SanPham> splist) {
        DefaultTableModel dtm = (DefaultTableModel) tblDSSP.getModel();
        dtm.setRowCount(0);
        for (SanPham sp : splist) {
            dtm.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                sp.getTenTheLoai(),
                sp.getTenNgonNgu(),
                sp.getTenNxb(),
                sp.getTenTacGia(),
                sp.getSoLuong()});
        }
    }

    private void loadDataDSHD(ArrayList<HoaDon> hdlist) {
        DefaultTableModel dtm = (DefaultTableModel) tblDSHD.getModel();
        dtm.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for (HoaDon hd : hdlist) {
            String maPhieuGiamGia = hd.getTenPhieuGiamGia() != null ? hd.getTenPhieuGiamGia() : "Không có";
            dtm.addRow(new Object[]{
                hd.getMaHoaDon(), // Mã hóa đơn
                hd.getMaNhanVien(), // Mã nhân viên
                hd.getMaKhachHang(), // Mã khách hàng
                hd.getNgayTao(), // Ngày tạo
                df.format(hd.getTongTien()) + " VND", // Tổng tiền
                maPhieuGiamGia // Phiếu giảm giá
            });
        }
    }

    private void addTableGioHangCellListener() {
        TableCellListener tcl = new TableCellListener(tblGioHang, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                int row = tcl.getRow();
                int oldValue = Integer.parseInt(tcl.getOldValue().toString());
                int newValue = Integer.parseInt(tcl.getNewValue().toString());
                int soLuongDieuChinh = newValue - oldValue;

                // Lấy mã hóa đơn từ trường nhập liệu
                String maHD = txtMaHD.getText();

                // Kiểm tra trạng thái của hóa đơn (nếu hóa đơn đã thanh toán, không cho phép thay đổi)
                HoaDon hoaDon = hoaDonRepo.getHoaDonByMa(maHD);
                if (hoaDon != null && hoaDon.getTrangThai()) {
                    JOptionPane.showMessageDialog(panelHD, "Không thể thay đổi sản phẩm trong giỏ hàng vì hóa đơn đã thanh toán!");
                    return;
                }
                String maSP = tblGioHang.getValueAt(row, 0).toString();
                int soLuongTon = sanPhamRepo.getListSP(maSP).get(0).getSoLuong();
                if (newValue > soLuongTon + oldValue) {
                    JOptionPane.showMessageDialog(panelHD, "Không đủ hàng tồn!");
                    tblGioHang.setValueAt(oldValue, row, tcl.getColumn());
                } else if (newValue < 0) {
                    JOptionPane.showMessageDialog(panelHD, "Không nhập số âm!");
                    tblGioHang.setValueAt(oldValue, row, tcl.getColumn());
                } else if (newValue == 0) {
                    XoaSPGH();
                } else {
                    soLuongTon -= soLuongDieuChinh;
                    sanPhamRepo.updateSP(maSP, soLuongTon);
                    hoaDonChiTietRepo.updateGH(maHD, maSP, newValue);
                    loadDataDSSP(sanPhamRepo.getListSP());
                    loadDataHDCT(hoaDonChiTietRepo.getChiTietByHoaDon(maHD));
                    sumHD(maHD);
                }
            }
        });

    }

    private void XoaSPGH() {
        int index = tblGioHang.getSelectedRow();
        String maHD = txtMaHD.getText();

        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa trong giỏ hàng!");
            return;
        }

        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã hóa đơn!");
            return;
        }

        ArrayList<HoaDonChiTiet> hdctList = hoaDonChiTietRepo.getChiTietByHoaDon(maHD);
        if (hdctList == null || hdctList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong giỏ hàng để xóa!");
            return;
        }

        HoaDonChiTiet hdct = hdctList.get(index);
        if (hdct == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết hóa đơn!");
            return;
        }

        boolean isDeleted = hoaDonChiTietRepo.xoaHDCT(maHD, hdct.getMaSanPham());
        if (isDeleted) {
            xoaHDCT(hdct);
            loadDataHDCT(hoaDonChiTietRepo.getChiTietByHoaDon(maHD));
            loadDataDSSP(sanPhamRepo.getListSP());
            sumHD(maHD);

            // Thông báo cập nhật số lượng sản phẩm cho ViewSanPham
            if (sanPhamUpdateListener != null) {
                sanPhamUpdateListener.onSanPhamUpdated();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm khỏi giỏ hàng!");
        }
    }

    private void sumHD(String maHD) {
        double tongTien = 0;

        // Tính tổng tiền từ giỏ hàng
        for (int i = 0; i < tblGioHang.getRowCount(); i++) {
            double donGia = Double.parseDouble(tblGioHang.getValueAt(i, 2).toString().replace(",", "").replace(" VND", ""));
            int soLuong = Integer.parseInt(tblGioHang.getValueAt(i, 3).toString());
            tongTien += donGia * soLuong;
        }

        // Áp dụng giảm giá nếu có
        String tenPhieuGiamGia = (String) cbxPhieuGiamGia.getSelectedItem();
        if (tenPhieuGiamGia != null && !tenPhieuGiamGia.isEmpty()) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepo.getPhieuGiamGiaByTen(tenPhieuGiamGia); // Sử dụng tên
            if (phieuGiamGia != null) {
                tongTien -= phieuGiamGia.getSoTienGiam();
            }
        }

        if (tongTien < 0) {
            tongTien = 0;
        }

        // Cập nhật tổng tiền vào hóa đơn
        hoaDonRepo.updateHD(tongTien, maHD);
        DecimalFormat df = new DecimalFormat("#,###");
        txtTong.setText(df.format(tongTien) + " VND");
        txtCanTra.setText(df.format(tongTien) + " VND");
    }

    private void xoaHDCT(HoaDonChiTiet hdct) {
        Integer soLuongMua = hdct.getSoLuong();
        String maSP = hdct.getMaSanPham();
        Integer soLuongTon = sanPhamRepo.getListSP(maSP).get(0).getSoLuong() + soLuongMua;
        sanPhamRepo.updateSP(maSP, soLuongTon);
    }

    private void loadDataHDCT(ArrayList<HoaDonChiTiet> hdctlist) {
        DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);

        for (HoaDonChiTiet hdct : hdctlist) {
            dtm.addRow(new Object[]{
                hdct.getMaSanPham(),
                hdct.getTenSanPham(), // Chỉ hiển thị tên sản phẩm
                new DecimalFormat("#,###").format(hdct.getDonGia()) + " VND",
                hdct.getSoLuong(),
                new DecimalFormat("#,###").format(hdct.getThanhTien()) + " VND"
            });
        }
    }

    public void loadDataSP(ArrayList<SanPham> splist) {
        DefaultTableModel dtm = (DefaultTableModel) tblDSSP.getModel();
        dtm.setRowCount(0);
        for (SanPham sp : splist) {
            dtm.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                sp.getTenTheLoai(),
                sp.getTenNgonNgu(),
                sp.getTenNxb(),
                sp.getTenTacGia(),
                sp.getSoLuong()});
        }
    }

    public boolean ValidateTienKhachDua() {
        StringBuilder stb = new StringBuilder();
        Validate v = new Validate();
        v.isNumber(txtTienKhachDua, stb, "Không đúng định dạng tiền", 1);
        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb);
            return false;
        } else {
            return true;
        }
    }

    private void fillModelHD(HoaDon hd) {
        txtMaHD.setText(hd.getMaHoaDon());
        txtTenKHHD.setText(hd.getTenKhachHang());
        txtSDTHD.setText(hd.getSdt());
        txtTong.setText(new DecimalFormat("#,###").format(hd.getTongTien()) + " VND");
        if (hd.getSoTienGiam() > 0) {
            txtCanTra.setText(new DecimalFormat("#,###").format(hd.getThanhTien()) + " VND");
        } else {
            txtCanTra.setText(txtTong.getText());
        }
        // Đặt giá trị cho cbxPhieuGiamGia dựa trên TenPhieuGiamGia
        if (hd.getTenPhieuGiamGia() != null && !hd.getTenPhieuGiamGia().isEmpty()) {
            cbxPhieuGiamGia.setSelectedItem(hd.getTenPhieuGiamGia());
        } else {
            cbxPhieuGiamGia.setSelectedItem(""); // Đặt về trống nếu không có phiếu
        }
        // Đặt giá trị cho cbxNVBH
        if (hd.getMaNhanVien() != null && !hd.getMaNhanVien().isEmpty()) {
            cbxNVBH.setSelectedItem(hd.getMaNhanVien());
        } else {
            cbxNVBH.setSelectedItem(""); // Hoặc giá trị mặc định nếu cần
        }
    }

    private void clearHD() {
        txtMaHD.setText("");
        txtTenKHHD.setText("");
        txtSDTHD.setText("");
        txtTong.setText("");
        txtCanTra.setText("");
        txtTienKhachDua.setText("");
        txtTienThua.setText("");
        txtLocSP.setText("");
        cbxPhieuGiamGia.setSelectedItem(null); // Reset phiếu giảm giá
        cbxNVBH.setSelectedIndex(0); // Reset combobox nhân viên về mục trống đầu tiên
        isSPfilter = false; // Reset trạng thái lọc
        filteredSPList = null; // Reset danh sách lọc
        txtSDTHD.setBackground(WHITE);
        loadDataDSSP(sanPhamRepo.getListSP()); // Tải lại toàn bộ danh sách sản phẩm
    }

    public boolean ValidateSDTHD() {
        StringBuilder stb = new StringBuilder();
        Validate v = new Validate();

        // Kiểm tra xem có khoảng trắng hay không
        if (txtSDTHD.getText().contains(" ")) {
            stb.append("Số điện thoại không được chứa khoảng trắng!\n");
        }

        // Kiểm tra định dạng số điện thoại
        v.isPhone(txtSDTHD, stb, "Không đúng định dạng SĐT");

        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb.toString());
            return false;
        } else {
            return true;
        }
    }

    public boolean ValidateHD() {
        StringBuilder stb = new StringBuilder();
        Validate v = new Validate();
        v.isEmpty(txtSDTHD, stb, "Chưa nhập SĐT khách hàng");
        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb);
            return false;
        } else {
            return true;
        }
    }

//public boolean addHD() {
//     String kq;
//        if (hoaDonRepo.addHD(getModelHD())) {
//            kq = "Tạo thành công";
//        } else {
//            kq = "Tạo thất bại";
//        }
//        JOptionPane.showMessageDialog(this, kq);
//        loadDataDSHD(hoaDonRepo.getListHDCHT());
//        txtMaHD.setText(hoaDonRepo.getListHDCHT().get(hoaDonRepo.getListHDCHT().size() - 1).getMaHoaDon().toString());
//    }
    public boolean addHD() {
        String kq;
        boolean isSuccess = hoaDonRepo.addHD(getModelHD()); // Lưu kết quả thêm hóa đơn

        if (isSuccess) {
            kq = "Tạo thành công";
            // Tải lại dữ liệu danh sách hóa đơn chưa thanh toán
            loadDataDSHD(hoaDonRepo.getListHDCHT());

            // Đặt mã hóa đơn mới nhất vào txtMaHD
            if (!hoaDonRepo.getListHDCHT().isEmpty()) {
                txtMaHD.setText(hoaDonRepo.getListHDCHT().get(hoaDonRepo.getListHDCHT().size() - 1).getMaHoaDon());
                // Bật các trường cần thiết để chỉnh sửa, bao gồm cbxNVBH
                txtSDTHD.setEnabled(true);
                txtTienKhachDua.setEnabled(true);
                cbxNVBH.setEnabled(true); // Cho phép thay đổi mã nhân viên
                cbxPhieuGiamGia.setEnabled(true);
                // Tự động điền thông tin hóa đơn mới tạo vào form
                HoaDon hd = hoaDonRepo.getListHDCHT().get(hoaDonRepo.getListHDCHT().size() - 1);
                fillModelHD(hd);
            } else {
                JOptionPane.showMessageDialog(this, "Không có hóa đơn nào trong danh sách!");
            }
        } else {
            kq = "Tạo thất bại";
        }

        JOptionPane.showMessageDialog(this, kq);
        return isSuccess; // Trả về kết quả thêm hóa đơn
    }

    private HoaDon getModelHD() {
        if (Auth.user == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập! Vui lòng đăng nhập trước khi tạo hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (khachHangRepo == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: KhachHangRepo chưa được khởi tạo!");
            return null;
        }
        String phoneNumber = txtSDTHD.getText();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        KhachHang kh = khachHangRepo.getKHinfo(phoneNumber);
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String maNhanVien = (String) cbxNVBH.getSelectedItem();
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên bán hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        HoaDon hd = new HoaDon();
        hd.setMaNhanVien(maNhanVien);
        hd.setMaKhachHang(kh.getMaKhachHang());
        hd.setNgayTao(LocalDate.now());
        hd.setTrangThai(false);
        hd.setTongTien(0.0);
        hd.setSoTienGiam(0.0);
        return hd;
    }

    private void huyHD(String maHD) {
        String kq;
        if (hoaDonRepo.huyHD(maHD)) {
            kq = "Huỷ thành công";
        } else {
            kq = "Huỷ không thành công";
        }
        JOptionPane.showMessageDialog(this, kq);
        loadDataDSHD(hoaDonRepo.getListHDCHT()); // Cập nhật lại danh sách hóa đơn
    }

//    public boolean ValidateLocSP() {
//        StringBuilder stb = new StringBuilder();
//        Validate v = new Validate();
//
//        String keyword = txtLocSP.getText();
//
//        // Kiểm tra xem chuỗi có bắt đầu bằng khoảng trắng hay không
//        if (keyword.startsWith(" ")) {
//            stb.append("Từ khóa tìm kiếm không được bắt đầu bằng khoảng trắng!\n");
//        }
//
//        // Kiểm tra trống
//        v.isEmpty(txtLocSP, stb, "Vui lòng nhập từ khóa tìm kiếm!");
//
//        if (stb.length() > 0) {
//            JOptionPane.showMessageDialog(this, stb.toString());
//            return false;
//        } else {
//            return true;
//        }
//    }
    private void thanhToan(String maHD, double soTienGiam, double tongTien) {
        String kq;
        String tenPhieuGiamGia = (String) cbxPhieuGiamGia.getSelectedItem();

        if (tenPhieuGiamGia != null && !tenPhieuGiamGia.isEmpty()) {
            if (!hoaDonRepo.updateMaPhieuGiamGia(maHD, tenPhieuGiamGia)) {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật mã phiếu giảm giá!");
                return;
            }

            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepo.getPhieuGiamGiaByTen(tenPhieuGiamGia);
            if (phieuGiamGia != null && phieuGiamGia.getSoLuong() > 0) {
                int soLuongMoi = phieuGiamGia.getSoLuong() - 1;
                phieuGiamGia.setSoLuong(soLuongMoi);
                if (phieuGiamGiaRepo.update(phieuGiamGia)) {
                    updatePhieuGiamGiaComboBox();
                    if (updateListener != null) {
                        updateListener.onPhieuGiamGiaUpdated();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật số lượng phiếu giảm giá!");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Phiếu giảm giá không hợp lệ hoặc đã hết số lượng!");
                return;
            }
        }

        if (hoaDonRepo.thanhtoanHD(maHD, soTienGiam, tongTien)) {
            kq = "Thanh toán thành công";
            cbxNVBH.setEnabled(false);
            loadDataDSHD(hoaDonRepo.getListHDCHT());
            notifyHoaDonUpdated();

            // Thông báo cập nhật số lượng sản phẩm cho ViewSanPham
            if (sanPhamUpdateListener != null) {
                sanPhamUpdateListener.onSanPhamUpdated();
            }
        } else {
            kq = "Thanh toán không thành công";
        }

        JOptionPane.showMessageDialog(this, kq);
        DefaultTableModel dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        clearHD();
    }

    private boolean validateTenKH(String tenKH) {
        StringBuilder stb = new StringBuilder();

        // Kiểm tra trống
        if (tenKH == null || tenKH.trim().isEmpty()) {
            stb.append("Tên khách hàng không được để trống!\n");
        } else {
            // Kiểm tra khoảng trắng ở đầu
            if (tenKH.startsWith(" ")) {
                stb.append("Tên khách hàng không được bắt đầu bằng khoảng trắng!\n");
            }

            // Kiểm tra có số không
            if (tenKH.matches(".*\\d.*")) {
                stb.append("Tên khách hàng không được chứa số!\n");
            }

            // Kiểm tra ký tự đặc biệt (#, @, &, ^, %, v.v.)
            // Chỉ cho phép chữ cái (bao gồm tiếng Việt) và khoảng trắng
            if (!tenKH.matches("[a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ]+")) {
                stb.append("Tên khách hàng không được chứa ký tự đặc biệt như #, @, &, ^, %, v.v.!\n");
            }
        }

        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb.toString());
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnInBill;
    private javax.swing.JButton btnResetHD;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnXoaSPGH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxNVBH;
    private javax.swing.JComboBox<String> cbxPhieuGiamGia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbCanTra;
    private javax.swing.JLabel lbTong;
    private javax.swing.JPanel panelHD;
    private javax.swing.JPanel panelTaoHoaDon;
    private javax.swing.JRadioButton rdoChuaTT;
    private javax.swing.JRadioButton rdoDaTT;
    private javax.swing.JTable tblDSHD;
    private javax.swing.JTable tblDSSP;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTextField txtCanTra;
    private javax.swing.JTextField txtLocSP;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtSDTHD;
    private javax.swing.JTextField txtTenKHHD;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienThua;
    private javax.swing.JTextField txtTong;
    // End of variables declaration//GEN-END:variables
}
