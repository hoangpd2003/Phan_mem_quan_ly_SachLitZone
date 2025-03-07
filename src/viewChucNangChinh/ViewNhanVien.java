/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewChucNangChinh;

import Model.NhanVien;
import Repository.NhanVienRepo;
import Utils.Validate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hoang
 */
public class ViewNhanVien extends javax.swing.JPanel {

    private NhanVienRepo repo = new NhanVienRepo();
    private NhanVienRepo repository;
    private DefaultTableModel mol = new DefaultTableModel();
    private int index = -1;
    private int i;

    private NhanVienUpdateListener updateListener;

    /**
     * Creates new form ViewNhanVien
     */
    public ViewNhanVien() {

        initComponents();
        this.fillTable(repo.getAll());
        this.showData(index);
        System.out.println("ViewNhanVien instance created: " + this.hashCode());
    }

    // Phương thức để đăng ký listener từ bên ngoài
    public void setNhanVienUpdateListener(NhanVienUpdateListener listener) {
        this.updateListener = listener;
    }

//void fillTable(ArrayList<NhanVien> list) {
//        mol = (DefaultTableModel) tbl_Sach.getModel();
//        mol.setRowCount(0);
//        for (NhanVien x : list) {
//            mol.addRow(x.toDataRow());
//        }
//    }
    void fillTable(ArrayList<NhanVien> list) {
        mol = (DefaultTableModel) tbl_Sach.getModel();
        mol.setRowCount(0);
        for (NhanVien nv : list) {
            Object[] row = nv.toDataRow();
            row[7] = nv.getTenChucVu(); // Lấy trực tiếp TenChucVu từ đối tượng NhanVien
            mol.addRow(row);
        }
    }

    private void reset() {
        txt_ns.setDate(null);
        txt_ma.setText(null);
        txt_sdt.setText(null);
        txt_ten.setText(null);
//        txt_ngaySinh.setText(null);
        txt_MatKhau.setText(null);
        txt_timKiem.setText(null);

        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
        buttonGroup3.clearSelection();

    }

    private void showData(int index) {
//        if (index < 0 || index >= tbl_Sach.getRowCount()) {
//            return;
//        }
//        txt_ma.setText(tbl_Sach.getValueAt(index, 1).toString());
//        txt_ten.setText(tbl_Sach.getValueAt(index, 2).toString());
////        txt_ngaySinh.setText(tbl_Sach.getValueAt(index, 4).toString());
//        txt_sdt.setText(tbl_Sach.getValueAt(index, 5).toString());
////        cbbChucVu.setSelectedItem(tbl_Sach.getValueAt(index, 6).toString());
//        if (tbl_Sach.getValueAt(index, 6).toString().equalsIgnoreCase("1")) {
//            rdo_admin.setSelected(true);
//        } else {
//            rdo_nv.setSelected(true);
//        }
//
//        if (tbl_Sach.getValueAt(index, 3).toString().equalsIgnoreCase("nam")) {
//            rdoNam.setSelected(true);
//        } else {
//            rdoNu.setSelected(true);
//        };
//////        cbbTrangThai.setSelectedItem(tbl_Sach.getValueAt(index, 7).toString());
//        if (tbl_Sach.getValueAt(index, 7).toString().equals("Đang làm")) {
//            rdo_lam.setSelected(true);
//        } else {
//            rdo_nghi.setSelected(true);
//
//        };
////      txt_ns.setText(tbl_Sach.getValueAt(index, 4).toString());
//        try {
//            index = tbl_Sach.getSelectedRow();
//            Date a = new SimpleDateFormat("yyyy-MM-dd").parse((String) tbl_Sach.getValueAt(index, 4).toString());
//            txt_ns.setDate(a);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        if (index < 0 || index >= tbl_Sach.getRowCount()) {
            return;
        }
        txt_ma.setText(tbl_Sach.getValueAt(index, 1).toString());
        txt_MatKhau.setText(tbl_Sach.getValueAt(index, 2).toString());
        txt_ten.setText(tbl_Sach.getValueAt(index, 3).toString());
//        txt_ngaySinh.setText(tbl_Sach.getValueAt(index, 4).toString());
        txt_sdt.setText(tbl_Sach.getValueAt(index, 6).toString());
//        cbbChucVu.setSelectedItem(tbl_Sach.getValueAt(index, 6).toString());
        if (tbl_Sach.getValueAt(index, 7).toString().equalsIgnoreCase("1")) {
            rdo_admin.setSelected(true);
        } else {
            rdo_nv.setSelected(true);
        }

        if (tbl_Sach.getValueAt(index, 4).toString().equalsIgnoreCase("nam")) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        };
////        cbbTrangThai.setSelectedItem(tbl_Sach.getValueAt(index, 7).toString());
        if (tbl_Sach.getValueAt(index, 8).toString().equals("Đang làm")) {
            rdo_lam.setSelected(true);
        } else {
            rdo_nghi.setSelected(true);

        };
//      txt_ns.setText(tbl_Sach.getValueAt(index, 4).toString());
        try {
            index = tbl_Sach.getSelectedRow();
            Date a = new SimpleDateFormat("yyyy-MM-dd").parse((String) tbl_Sach.getValueAt(index, 5).toString());
            txt_ns.setDate(a);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public NhanVien readForm() {
        String ma = txt_ma.getText().trim();
        String matKhau = txt_MatKhau.getText().trim();
        String ten = txt_ten.getText().trim();
        String sdt = txt_sdt.getText().trim();
        boolean gioiTinh = rdoNam.isSelected();
        boolean trangThai = rdo_lam.isSelected();

        Date selectedDate = txt_ns.getDate();
        String ngaySinh = (selectedDate != null) ? dateFormat.format(selectedDate) : "";

        // Kiểm tra các trường bắt buộc
        if (ma.isBlank() || matKhau.isBlank() || ten.isBlank() || ngaySinh.isBlank() || sdt.isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int idChucVu = rdo_admin.isSelected() ? 1 : 2;

        return new NhanVien(ma, matKhau, idChucVu, ten, ngaySinh, sdt, gioiTinh, trangThai);
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_ma = new javax.swing.JTextField();
        txt_ten = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txt_sdt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        rdo_lam = new javax.swing.JRadioButton();
        rdo_nghi = new javax.swing.JRadioButton();
        rdo_admin = new javax.swing.JRadioButton();
        rdo_nv = new javax.swing.JRadioButton();
        txt_ns = new com.toedter.calendar.JDateChooser();
        txt_MatKhau = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_clean = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Sach = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1059, 510));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Quản lý nhân viên");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("Mã nhân viên:");

        jLabel3.setText("Tên nhân viên:");

        jLabel4.setText("Chức vụ:");

        jLabel5.setText("Giới tính:");

        jLabel7.setText("Ngày Sinh:");

        jLabel9.setText("Số điện thoại:");

        txt_ma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_maActionPerformed(evt);
            }
        });

        txt_ten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tenActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoNam);
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");

        txt_sdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sdtActionPerformed(evt);
            }
        });

        jLabel12.setText("Trạng thái:");

        buttonGroup2.add(rdo_lam);
        rdo_lam.setText("Đang làm");

        buttonGroup2.add(rdo_nghi);
        rdo_nghi.setText("Đã nghỉ");

        buttonGroup3.add(rdo_admin);
        rdo_admin.setText("Quản Lý");
        rdo_admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_adminActionPerformed(evt);
            }
        });

        buttonGroup3.add(rdo_nv);
        rdo_nv.setText("Nhân Viên");

        txt_ns.setDateFormatString("yyyy-MM-dd");

        txt_MatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MatKhauActionPerformed(evt);
            }
        });

        jLabel8.setText("Mật khẩu:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel2))
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ten)
                            .addComponent(txt_ma)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu)
                            .addComponent(txt_ns, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel4)
                    .addComponent(jLabel12)
                    .addComponent(jLabel8))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rdo_admin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdo_nv))
                            .addComponent(txt_MatKhau))
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdo_nghi, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdo_lam))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_ma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(rdo_admin)
                    .addComponent(rdo_nv))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(23, 23, 23)
                        .addComponent(rdoNu)
                        .addGap(22, 22, 22))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_MatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(txt_ns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rdoNam)
                                    .addComponent(jLabel5))
                                .addGap(49, 49, 49))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(rdo_lam)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdo_nghi)
                                .addGap(33, 33, 33))))))
        );

        btn_clean.setText("Làm mới");
        btn_clean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cleanActionPerformed(evt);
            }
        });

        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btn_xoa.setText("Xoá");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_clean, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_them, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_sua)
                    .addComponent(btn_xoa))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_clean, btn_sua, btn_them, btn_xoa});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_sua)
                    .addComponent(btn_them))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clean)
                    .addComponent(btn_xoa))
                .addGap(26, 26, 26))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_clean, btn_sua, btn_them, btn_xoa});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        txt_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timKiemActionPerformed(evt);
            }
        });
        txt_timKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_timKiemKeyReleased(evt);
            }
        });

        tbl_Sach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mã nhân viên", "Mật khẩu", "Tên nhân viên", "Giới tính", "Ngày Sinh", "Số điện thoại", "Chức vụ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Sach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_SachMouseClicked(evt);
            }
        });
        tbl_Sach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbl_SachKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_Sach);
        if (tbl_Sach.getColumnModel().getColumnCount() > 0) {
            tbl_Sach.getColumnModel().getColumn(0).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(1).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(2).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(3).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(4).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(5).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(6).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(7).setResizable(false);
            tbl_Sach.getColumnModel().getColumn(8).setResizable(false);
        }

        jLabel6.setText("Tìm Kiếm:");
        jLabel6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jLabel6KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 884, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(267, 267, 267))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_maActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_maActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_maActionPerformed

    private void txt_tenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tenActionPerformed

    private void txt_sdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_sdtActionPerformed

    private void rdo_adminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_adminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_adminActionPerformed

    private void btn_cleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cleanActionPerformed
        // TODO add your handling code here:
        lamMoiForm();
    }//GEN-LAST:event_btn_cleanActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed

        int index = tbl_Sach.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!kiemTra1()) {
            return;
        }

        try {
            NhanVien nv = reaform2();
            int result = repo.updateNV(nv);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                this.fillTable(repo.getAll());
                this.reset();
                if (updateListener != null) {
                    updateListener.onNhanVienUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại do lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
//        index = tbl_Sach.getSelectedRow();
//        if (index == -1) {
//            JOptionPane.showMessageDialog(this, "Bạn phải chọn 1 dòng",
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String id = tbl_Sach.getValueAt(index, 0).toString();
//        repo.deleteNV(id);
//        JOptionPane.showMessageDialog(this, "Xoá thành công");
//        this.fillTable(
//                repo.getAll());

        int selectedRow = tbl_Sach.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần cập nhật trạng thái!");
            return;
        }

        // Lấy ID của nhân viên từ bảng
        String idNhanVien = tbl_Sach.getValueAt(selectedRow, 0).toString(); // Lấy giá trị từ cột ID (cột 0)

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đổi trạng thái nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = repo.updateTrangThaiNhanVien(idNhanVien); // Cập nhật trạng thái nhân viên

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                fillTable(repo.getAll()); // Load lại bảng trong ViewNhanVien
                lamMoiForm(); // Làm mới form

                // Gọi listener để thông báo cho các view khác
                if (updateListener != null) {
                    updateListener.onNhanVienUpdated();
                    System.out.println("Called onNhanVienUpdated after status change.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại!");
            }
        }

    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed

        if (!kiemTra1()) {
            return;
        }

        NhanVien nv = this.readForm();
        if (nv == null) {
            return;
        }

        try {
            int result = repo.addNV(nv);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                this.fillTable(repo.getAll());
                this.reset();
                if (updateListener != null) {
                    updateListener.onNhanVienUpdated();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại do lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_themActionPerformed

    private void txt_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timKiemActionPerformed
        // TODO add your handling code here:
        //        String keyword = txt_timKiem.getText().trim();
        //
        //    if (keyword.isEmpty()) {
        //        return; // Không làm gì nếu ô tìm kiếm trống
        //    }
        //
        //    List<NhanVien> list = new NhanVienRepo().timKiem(keyword);
        //    DefaultTableModel model = (DefaultTableModel) tbl_Sach.getModel();
        //    model.setRowCount(0); // Xóa dữ liệu cũ
        //
        //    for (NhanVien nv : list) {
        //        model.addRow(new Object[]{
        //            nv.getIdNhanVien(),
        //            nv.getMaNhanVien(),
        //            nv.getTenNhanVien(),
        //            nv.isGioiTinh() ? "Nam" : "Nữ",
        //            nv.getNgaySinh(),
        //            nv.getSoDienThoai(),
        //            nv.getIdChucVu() == 1 ? "Quản lý" : "Nhân viên",
        //            nv.isTrangThai() ? "Đang làm" : "Nghỉ"

        //        });
        //    }
    }//GEN-LAST:event_txt_timKiemActionPerformed

    private void txt_timKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_timKiemKeyReleased
        this.fillTable(repo.search(txt_timKiem.getText()));

        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timKiemKeyReleased

    private void tbl_SachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_SachMouseClicked
        index = tbl_Sach.getSelectedRow();
        this.showData(index);
    }//GEN-LAST:event_tbl_SachMouseClicked

    private void txt_MatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MatKhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MatKhauActionPerformed

    private void tbl_SachKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_SachKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_SachKeyReleased

    private void jLabel6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel6KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6KeyReleased
    private void lamMoiForm() {
        // Đặt lại các trường nhập liệu về giá trị mặc định
        txt_ma.setText("");
        txt_MatKhau.setText("");
        txt_ten.setText("");
        txt_sdt.setText("");
        txt_ns.setDate(null); // Hoặc có thể đặt một ngày mặc định nếu cần

        // Đặt lại các nút radio về trạng thái chưa được chọn
        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
        buttonGroup3.clearSelection();

        // Xóa lựa chọn trên bảng (nếu có)
        tbl_Sach.clearSelection();

    }

    public NhanVien reaform2() {
        NhanVien nv = new NhanVien();
        Date selectedDate = txt_ns.getDate();
        String ngaySinh = dateFormat.format(selectedDate);
        nv.setIdNhanVien(Integer.parseInt(tbl_Sach.getValueAt(index, 0).toString().trim()));
        nv.setMaNhanVien(txt_ma.getText().trim());
        nv.setMatKhau(txt_MatKhau.getText().trim());
        nv.setTenNhanVien(txt_ten.getText().trim());
        nv.setNgaySinh(ngaySinh);
        nv.setSoDienThoai(txt_sdt.getText().trim());

        if (rdo_admin.isSelected()) {
            nv.setIdChucVu(1);

        } else {
            nv.setIdChucVu(2);
        }

        nv.setGioiTinh(rdoNam.isSelected());
        nv.setTrangThai(rdo_lam.isSelected());

        return nv;
    }
//
//    public Boolean check() {
//        String ma = txt_ma.getText().trim();
//        String ten = txt_ten.getText().trim();
//        String ngaySinh = txt_ngaySinh.getText().trim();
//        String sdt = txt_sdt.getText().trim();
//        if (ma.isBlank()) {
//            JOptionPane.showMessageDialog(this, "Trống mã nhân viên ", "Thông Báo", 3);
//        }
//        return true;
//    }

    public Boolean kiemTra1() {
        StringBuilder stb = new StringBuilder();
        NhanVienRepo repo = new NhanVienRepo();
        Validate v = new Validate();

        // Lấy ID của nhân viên hiện tại (nếu đang sửa)
        int excludeId = (index >= 0 && tbl_Sach.getSelectedRow() >= 0)
                ? Integer.parseInt(tbl_Sach.getValueAt(index, 0).toString())
                : -1;

        // 1. Kiểm tra mã nhân viên
        String ma = txt_ma.getText().trim();
        if (ma.isEmpty()) {
            stb.append("Mã nhân viên bị trống\n");
            txt_ma.requestFocus();
        } else if (ma.contains(" ")) {
            stb.append("Mã nhân viên không được chứa khoảng trắng\n");
            txt_ma.requestFocus();
        } else if (!ma.matches("^[a-zA-Z0-9]+$")) {
            stb.append("Mã nhân viên chứa ký tự đặc biệt\n");
            txt_ma.requestFocus();
        } else if (repo.isMaNhanVienExists(ma, excludeId)) {
            stb.append("Mã nhân viên đã tồn tại trong hệ thống\n");
            txt_ma.requestFocus();
        }

        // 2. Kiểm tra mật khẩu
        String matKhau = txt_MatKhau.getText().trim();
        if (matKhau.isEmpty()) {
            stb.append("Mật khẩu bị trống\n");
            txt_MatKhau.requestFocus();
        } else if (matKhau.contains(" ")) {
            stb.append("Mật khẩu không được chứa khoảng trắng\n");
            txt_MatKhau.requestFocus();
        }

        // 3. Kiểm tra tên nhân viên
        String ten = txt_ten.getText().trim();
        if (ten.isEmpty()) {
            stb.append("Tên nhân viên bị trống\n");
            txt_ten.requestFocus();
        } else if (!ten.matches("^[a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ]+$")) {
            stb.append("Tên nhân viên chứa ký tự đặc biệt hoặc số\n");
            txt_ten.requestFocus();
        }

        // 4. Kiểm tra số điện thoại
        String sdt = txt_sdt.getText().trim();
        if (sdt.isEmpty()) {
            stb.append("Số điện thoại bị trống\n");
            txt_sdt.requestFocus();
        } else if (sdt.contains(" ")) {
            stb.append("Số điện thoại không được chứa khoảng trắng\n");
            txt_sdt.requestFocus();
        } else if (!v.isPhone(txt_sdt, new StringBuilder(""), "")) {
            stb.append("Số điện thoại không đúng định dạng\n");
            txt_sdt.requestFocus();
        } else if (repo.isPhoneNumberExists(sdt, excludeId)) {
            stb.append("Số điện thoại đã tồn tại trong hệ thống\n");
            txt_sdt.requestFocus();
        }

        // 5. Kiểm tra ngày sinh
        Date selectedDate = txt_ns.getDate();
        if (selectedDate == null) {
            stb.append("Ngày sinh chưa được chọn\n");
        }// 6. Kiểm tra giới tính
        if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            stb.append("Giới tính chưa được chọn\n");
        }

        // 7. Kiểm tra trạng thái
        if (!rdo_lam.isSelected() && !rdo_nghi.isSelected()) {
            stb.append("Trạng thái chưa được chọn\n");
        }

        // 8. Kiểm tra chức vụ
        if (!rdo_admin.isSelected() && !rdo_nv.isSelected()) {
            stb.append("Chức vụ chưa được chọn\n");
        }

        // Hiển thị thông báo lỗi nếu có
        if (stb.length() > 0) {
            JOptionPane.showMessageDialog(this, stb.toString(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clean;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdo_admin;
    private javax.swing.JRadioButton rdo_lam;
    private javax.swing.JRadioButton rdo_nghi;
    private javax.swing.JRadioButton rdo_nv;
    private javax.swing.JTable tbl_Sach;
    private javax.swing.JTextField txt_MatKhau;
    private javax.swing.JTextField txt_ma;
    private com.toedter.calendar.JDateChooser txt_ns;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_ten;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
