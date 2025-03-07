/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewChucNangChinh;

import Repository.PhieuGiamGiaRepo;
import Model.PhieuGiamGia;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Hoang
 */
public class ViewPhieuGiamGia extends javax.swing.JPanel {

    public void refreshTableData() {
        SwingUtilities.invokeLater(() -> {
            loadTableData(); // Làm mới bảng
            System.out.println("Đã làm mới bảng ViewPhieuGiamGia");
        });
    }
    private PhieuGiamGiaRepo repository;
    private DefaultTableModel tableModel;
    private PhieuGiamGiaUpdateListener updateListener;

    /**
     * Creates new form ViewPhieuGiamGia
     */
    public ViewPhieuGiamGia(PhieuGiamGiaUpdateListener listener) {
        this.updateListener = listener;
        initComponents();
        repository = new PhieuGiamGiaRepo();
        tableModel = (DefaultTableModel) tblPhieuGiamGia.getModel();
        setupSearchListener();
        repository.updateExpiredCoupons();
        loadTableData();

    }

    private void setupSearchListener() {
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnSearchActionPerformed(null);
                }
            }
        });
    }

    // Constructor không tham số (nếu cần giữ lại)
    public ViewPhieuGiamGia() {
        this(null); // Gọi constructor có tham số với listener null
    }

    // Phương thức để đăng ký listener từ bên ngoài (tuỳ chọn)
    public void setPhieuGiamGiaUpdateListener(PhieuGiamGiaUpdateListener listener) {
        this.updateListener = listener;
    }

    private void loadTableData() {
        List<PhieuGiamGia> list = repository.getAll(); // Lấy tất cả phiếu giảm giá
        loadTableData(list); // Gọi phương thức có tham số
    }

    // Kiểm tra tên phiếu hỗ trợ tiếng Việt, chữ số và khoảng trắng
    private boolean isValidName(String input) {
        String regex = "^[A-Za-z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ][-A-Za-z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ/\\s]*$";
        return input != null && !input.isEmpty() && input.matches(regex);
    }

    private boolean isValidCode(String input) {
        String regex = "^[A-Za-z0-9]+$";
        return input != null && !input.isEmpty() && input.matches(regex);
    }

    private boolean isValidDateRange(Date startDate, Date endDate) {
        return startDate != null && endDate != null && startDate.before(endDate);
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtSoTienGiam.setText("");
        txtSoLuong.setText("");
        txt_ngaybatdau.setDate(null);
        txt_ngayketthuc.setDate(null);
        txtSearch.setText("");
        buttonGroup1.clearSelection();
    }

    private boolean isMaPhieuExists(String maPhieu, int excludeId) {
        List<PhieuGiamGia> list = repository.getAll();
        for (PhieuGiamGia pgg : list) {
            if (pgg.getMaPhieuGiamGia().equals(maPhieu) && pgg.getId() != excludeId) {
                return true; // Mã đã tồn tại
            }
        }
        return false; // Mã chưa tồn tại
    }

    private void loadTableData(List<PhieuGiamGia> list) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < list.size(); i++) {
            PhieuGiamGia pgg = list.get(i);
            Object[] row = {
                i + 1, // STT
                pgg.getMaPhieuGiamGia(),
                pgg.getTenPhieuGiamGia(),
                pgg.getSoLuong(), // Số lượng
                pgg.getSoTienGiam(), // Giữ nguyên kiểu Double
                (pgg.getNgayBatDau() != null ? sdf.format(pgg.getNgayBatDau()) : ""), // Ngày bắt đầu
                (pgg.getNgayKetThuc() != null ? sdf.format(pgg.getNgayKetThuc()) : ""), // Ngày kết thúc
                pgg.isTrangThai() ? "Chưa hết hạn" : "Đã hết hạn" // Hiển thị trạng thái
            };
            tableModel.addRow(row);
        }
    }

    private int getIdByMa(String maPhieu) {
        List<PhieuGiamGia> list = repository.getAll();
        for (PhieuGiamGia pgg : list) {
            if (pgg.getMaPhieuGiamGia().equals(maPhieu)) {
                return pgg.getId();
            }
        }
        return -1; // Không tìm thấy
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSoTienGiam = new javax.swing.JTextField();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPhieuGiamGia = new javax.swing.JTable();
        txt_ngaybatdau = new com.toedter.calendar.JDateChooser();
        txt_ngayketthuc = new com.toedter.calendar.JDateChooser();
        btnClear = new javax.swing.JButton();
        txtTrangThai = new javax.swing.JLabel();
        rdoChuaHetHan = new javax.swing.JRadioButton();
        rdoDaHetHan = new javax.swing.JRadioButton();
        btnXuatPhieuGiamGia = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 700));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Ngày bắt đầu : ");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 86, -1, -1));

        jLabel6.setText("Ngày kết thúc : ");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 132, -1, -1));

        jLabel9.setText("Số lượng :");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 187, -1, -1));

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });
        add(txtSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 184, 154, -1));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("QUẢN LÍ PHIẾU GIẢM GIÁ");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 6, -1, -1));

        jLabel2.setText("Mã : ");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 86, -1, -1));
        add(txtMa, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 83, 150, -1));

        jLabel3.setText("Tên phiếu giảm giá : ");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 132, -1, -1));

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });
        add(txtTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 129, 150, -1));

        jLabel4.setText("Số tiền giảm");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 187, -1, -1));
        add(txtSoTienGiam, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 184, 150, -1));
        add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 302, 152, -1));

        jLabel7.setText("Mã giảm giá ");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 305, 74, -1));

        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 302, -1, -1));

        btnAdd.setText("Thêm ");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 80, -1, -1));

        btnSua.setText("Sửa");
        btnSua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuaMouseClicked(evt);
            }
        });
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        btnXoa.setText("Xoá");
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXoaMouseClicked(evt);
            }
        });
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 160, -1, -1));

        tblPhieuGiamGia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã giảm giá ", "Tên giảm giá", "Số lượng", "Số tiền giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhieuGiamGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhieuGiamGiaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPhieuGiamGia);
        if (tblPhieuGiamGia.getColumnModel().getColumnCount() > 0) {
            tblPhieuGiamGia.getColumnModel().getColumn(0).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(1).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(2).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(3).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(4).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(5).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(6).setResizable(false);
            tblPhieuGiamGia.getColumnModel().getColumn(7).setResizable(false);
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 387, 779, 225));

        txt_ngaybatdau.setDateFormatString("yyyy-MM-dd");
        add(txt_ngaybatdau, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 83, 154, -1));

        txt_ngayketthuc.setDateFormatString("yyyy-MM-dd");
        add(txt_ngayketthuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 123, 154, -1));

        btnClear.setText("Làm mới");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClearMouseEntered(evt);
            }
        });
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, -1, -1));

        txtTrangThai.setText("Trạng thái ");
        add(txtTrangThai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        buttonGroup1.add(rdoChuaHetHan);
        rdoChuaHetHan.setText("Chưa hết hạn");
        rdoChuaHetHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChuaHetHanActionPerformed(evt);
            }
        });
        add(rdoChuaHetHan, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, -1, -1));

        buttonGroup1.add(rdoDaHetHan);
        rdoDaHetHan.setText("Đã hết hạn");
        add(rdoDaHetHan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, -1, -1));

        btnXuatPhieuGiamGia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXuatPhieuGiamGia.setText("In phiếu giảm giá");
        btnXuatPhieuGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatPhieuGiamGiaActionPerformed(evt);
            }
        });
        add(btnXuatPhieuGiamGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, -1, 80));
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadTableData(); // Nếu không nhập gì, hiển thị toàn bộ dữ liệu
            return;
        }

        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<PhieuGiamGia> list = repository.getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int stt = 1;

        for (PhieuGiamGia pgg : list) {
            boolean match = pgg.getMaPhieuGiamGia().toLowerCase().contains(searchText)
                    || pgg.getTenPhieuGiamGia().toLowerCase().contains(searchText)
                    || String.valueOf(pgg.getSoLuong()).contains(searchText)
                    //                    || pgg.getSoTienGiam().toString().contains(searchText)
                    || String.valueOf(pgg.getSoTienGiam()).contains(searchText)
                    || (pgg.getNgayBatDau() != null && sdf.format(pgg.getNgayBatDau()).toLowerCase().contains(searchText))
                    || (pgg.getNgayKetThuc() != null && sdf.format(pgg.getNgayKetThuc()).toLowerCase().contains(searchText));

            if (match) {
                Object[] row = {
                    stt++, // STT
                    pgg.getMaPhieuGiamGia(),
                    pgg.getTenPhieuGiamGia(),
                    pgg.getSoLuong(),
                    pgg.getSoTienGiam(),
                    pgg.getNgayBatDau() != null ? sdf.format(pgg.getNgayBatDau()) : "",
                    pgg.getNgayKetThuc() != null ? sdf.format(pgg.getNgayKetThuc()) : ""
                };
                tableModel.addRow(row);
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả!");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        try {
            String maPhieu = txtMa.getText().trim();
            String tenPhieu = txtTen.getText().trim();

            if (maPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã phiếu không được để trống!");
                return;
            }
            if (!isValidCode(maPhieu)) {
                JOptionPane.showMessageDialog(this, "Mã phiếu chỉ được chứa chữ cái và số!");
                return;
            }

            if (tenPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên phiếu không được để trống!");
                return;
            }
            if (!isValidName(tenPhieu)) {
                JOptionPane.showMessageDialog(this, "Tên phiếu không được bắt đầu bằng khoảng trắng và chỉ được chứa chữ cái, số, dấu - và /!");
                return;
            }

            if (isMaPhieuExists(maPhieu, -1)) {
                JOptionPane.showMessageDialog(this, "Mã phiếu đã tồn tại!");
                return;
            }

            double soTienGiam = txtSoTienGiam.getText().isEmpty() ? 0 : Double.parseDouble(txtSoTienGiam.getText());
            int soLuong = Integer.parseInt(txtSoLuong.getText());
            Date ngayBatDau = txt_ngaybatdau.getDate();
            Date ngayKetThuc = txt_ngayketthuc.getDate();

            if (soTienGiam < 0) {
                JOptionPane.showMessageDialog(this, "Số tiền giảm không được âm!");
                return;
            }
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
                return;
            }

            if (!isValidDateRange(ngayBatDau, ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
                return;
            }

            PhieuGiamGia pgg = new PhieuGiamGia();
            pgg.setMaPhieuGiamGia(maPhieu);
            pgg.setTenPhieuGiamGia(tenPhieu);
            pgg.setSoTienGiam(soTienGiam);
            pgg.setSoLuong(soLuong);
            pgg.setNgayBatDau(ngayBatDau);
            pgg.setNgayKetThuc(ngayKetThuc);

            if (repository.add(pgg)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadTableData();
                clearForm();
                if (updateListener != null) {
                    updateListener.onPhieuGiamGiaUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng kiểm tra định dạng dữ liệu nhập vào!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnSuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseClicked
        int selectedRow = tblPhieuGiamGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu để sửa!");
            return;
        }

        try {
            String maPhieu = txtMa.getText().trim();
            String tenPhieu = txtTen.getText().trim();

            if (maPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã phiếu không được để trống!");
                return;
            }
            if (!isValidCode(maPhieu)) {
                JOptionPane.showMessageDialog(this, "Mã phiếu chỉ được chứa chữ cái và số!");
                return;
            }

            if (tenPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên phiếu không được để trống!");
                return;
            }
            if (!isValidName(tenPhieu)) {
                JOptionPane.showMessageDialog(this, "Tên phiếu không được bắt đầu bằng khoảng trắng và chỉ được chứa chữ cái, số, dấu - và /!");
                return;
            }

            String oldMaPhieu = (String) tableModel.getValueAt(selectedRow, 1);
            int id = getIdByMa(oldMaPhieu);

            if (!maPhieu.equals(oldMaPhieu) && isMaPhieuExists(maPhieu, id)) {
                JOptionPane.showMessageDialog(this, "Mã phiếu đã tồn tại!");
                return;
            }

            double soTienGiam = txtSoTienGiam.getText().isEmpty() ? 0 : Double.parseDouble(txtSoTienGiam.getText());
            int soLuong = Integer.parseInt(txtSoLuong.getText());
            Date ngayBatDau = txt_ngaybatdau.getDate();
            Date ngayKetThuc = txt_ngayketthuc.getDate();

            if (soTienGiam < 0) {
                JOptionPane.showMessageDialog(this, "Số tiền giảm không được âm!");
                return;
            }
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng không được âm!");
                return;
            }

            if (!isValidDateRange(ngayBatDau, ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
                return;
            }

            // Lấy trạng thái từ radio buttons
            boolean trangThai = rdoChuaHetHan.isSelected(); // true cho "Chưa hết hạn", false cho "Đã hết hạn"

            PhieuGiamGia pgg = repository.getById(id);
            pgg.setMaPhieuGiamGia(maPhieu);
            pgg.setTenPhieuGiamGia(tenPhieu);
            pgg.setSoTienGiam(soTienGiam);
            pgg.setSoLuong(soLuong);
            pgg.setNgayBatDau(ngayBatDau);
            pgg.setNgayKetThuc(ngayKetThuc);
            pgg.setTrangThai(trangThai); // Cập nhật trạng thái

            if (repository.update(pgg)) {
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng kiểm tra định dạng dữ liệu nhập vào!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaMouseClicked

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseClicked
        int selectedRow = tblPhieuGiamGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu để cập nhật trạng thái!");
            return;
        }

        // Lấy mã phiếu từ bảng
        String maPhieu = (String) tableModel.getValueAt(selectedRow, 1);
        int id = getIdByMa(maPhieu);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đổi trạng thái phiếu giảm giá này thành đã hết hạn?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (repository.updateTrangThaiPhieuGiamGia(id)) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                loadTableData(); // Tải lại bảng, chỉ hiển thị các phiếu còn hiệu lực
                clearForm(); // Làm mới form

                // Gọi listener để thông báo cho các view khác (nếu có)
                if (updateListener != null) {
                    updateListener.onPhieuGiamGiaUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại!");
            }
        }
    }//GEN-LAST:event_btnXoaMouseClicked

    private void tblPhieuGiamGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhieuGiamGiaMouseClicked
        int selectedRow = tblPhieuGiamGia.getSelectedRow();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (selectedRow >= 0) {
            txtMa.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtTen.setText((String) tableModel.getValueAt(selectedRow, 2));
            txtSoLuong.setText(String.valueOf(repository.getById(getIdByMa(txtMa.getText())).getSoLuong()));
            txtSoTienGiam.setText(tableModel.getValueAt(selectedRow, 4).toString());
            try {
                Date ngayBatDau = sdf.parse((String) tableModel.getValueAt(selectedRow, 5));
                txt_ngaybatdau.setDate(ngayBatDau);
                Date ngayKetThuc = sdf.parse((String) tableModel.getValueAt(selectedRow, 6));
                txt_ngayketthuc.setDate(ngayKetThuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Hiển thị trạng thái lên radio buttons
            String trangThai = (String) tableModel.getValueAt(selectedRow, 7);
            if (trangThai.equals("Chưa hết hạn")) {
                rdoChuaHetHan.setSelected(true); // Chọn "Chưa hết hạn"
            } else {
                rdoDaHetHan.setSelected(true); // Chọn "Đã hết hạn"
            }
        }
    }//GEN-LAST:event_tblPhieuGiamGiaMouseClicked

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        clearForm();
        loadTableData();
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

    }//GEN-LAST:event_btnClearActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void rdoChuaHetHanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChuaHetHanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoChuaHetHanActionPerformed

    private void btnClearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearMouseEntered

    private void btnXuatPhieuGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatPhieuGiamGiaActionPerformed
        int selectedRow = tblPhieuGiamGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu giảm giá để in!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có muốn in phiếu giảm giá này không?", 
            "Xác nhận in", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maPhieu = (String) tableModel.getValueAt(selectedRow, 1);
                PhieuGiamGia pgg = repository.getPhieuGiamGiaByTen((String) tableModel.getValueAt(selectedRow, 2));
                if (pgg == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phiếu giảm giá!");
                    return;
                }

                // Tạo file PDF
                Document document = new Document();
                String fileName = "PhieuGiamGia_" + maPhieu + "_" + System.currentTimeMillis() + ".pdf";
                String downloadsPath = System.getProperty("user.home") + "/Downloads/" + fileName;

                PdfWriter.getInstance(document, new FileOutputStream(downloadsPath));
                document.open();

                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Paragraph title = new Paragraph("PHIẾU GIẢM GIÁ", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);

                document.add(new Paragraph("Ngày in: " + sdf.format(new Date()), normalFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Mã phiếu: " + pgg.getMaPhieuGiamGia(), normalFont));
                document.add(new Paragraph("Tên phiếu: " + pgg.getTenPhieuGiamGia(), normalFont));
                document.add(new Paragraph("Số lượng: " + pgg.getSoLuong(), normalFont));
                document.add(new Paragraph("Số tiền giảm: " + String.format("%.2f", pgg.getSoTienGiam()) + " VNĐ", normalFont));
                document.add(new Paragraph("Ngày bắt đầu: " + (pgg.getNgayBatDau() != null ? sdf.format(pgg.getNgayBatDau()) : ""), normalFont));
                document.add(new Paragraph("Ngày kết thúc: " + (pgg.getNgayKetThuc() != null ? sdf.format(pgg.getNgayKetThuc()) : ""), normalFont));
                document.add(new Paragraph("Trạng thái: " + (pgg.isTrangThai() ? "Chưa hết hạn" : "Đã hết hạn"), normalFont));

                document.close();
                JOptionPane.showMessageDialog(this, "In phiếu giảm giá thành công: " + downloadsPath);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi in phiếu giảm giá: " + e.getMessage());
            }
        
            }
    }//GEN-LAST:event_btnXuatPhieuGiamGiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatPhieuGiamGia;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoChuaHetHan;
    private javax.swing.JRadioButton rdoDaHetHan;
    private javax.swing.JTable tblPhieuGiamGia;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtSoTienGiam;
    private javax.swing.JTextField txtTen;
    private javax.swing.JLabel txtTrangThai;
    private com.toedter.calendar.JDateChooser txt_ngaybatdau;
    private com.toedter.calendar.JDateChooser txt_ngayketthuc;
    // End of variables declaration//GEN-END:variables
}
