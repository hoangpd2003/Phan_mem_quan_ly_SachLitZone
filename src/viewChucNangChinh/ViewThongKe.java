/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package viewChucNangChinh;

import Model.HoaDon;
import Model.HoaDonChiTiet;
import Model.SanPham;
import Repository.HoaDonChiTietRepo;
import Repository.HoaDonRepo;
import Repository.SanPhamRepo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author Hoang
 */
public class ViewThongKe extends javax.swing.JPanel {

    private HoaDonRepo hoaDonRepo;
    private HoaDonChiTietRepo hoaDonChiTietRepo;
    private SanPhamRepo sanPhamRepo;

    public ViewThongKe() {
        initComponents();
        hoaDonRepo = new HoaDonRepo();
        hoaDonChiTietRepo = new HoaDonChiTietRepo();
        sanPhamRepo = new SanPhamRepo();

        // Thiết lập kích thước mặc định cho các panel chứa biểu đồ
        jpnDoanhThu.setLayout(new BorderLayout());
        jpnSanPham.setLayout(new BorderLayout());
        jpnDoanhThu.setPreferredSize(new Dimension(800, 300));
        jpnSanPham.setPreferredSize(new Dimension(800, 300));

        // Gắn sự kiện cho nút lọc
        btnFilterDoanhThu.addActionListener(evt -> btnFilterDoanhThuActionPerformed(evt));
        btnFilterSanPham.addActionListener(evt -> btnFilterSanPhamActionPerformed(evt));

        // Thiết lập ngày mặc định cho JDateChooser
        Date defaultStartDate = java.sql.Date.valueOf(LocalDate.of(2023, 10, 1));
        Date defaultEndDate = java.sql.Date.valueOf(LocalDate.now());
        txtTGTuDoanhThu.setDate(defaultStartDate);
        txtTGDenDoanhThu.setDate(defaultEndDate);
        txtTGTuSanPham.setDate(defaultStartDate);
        txtTGDenSanPham.setDate(defaultEndDate);

        // Tải dữ liệu và biểu đồ mặc định từ 2023-10-01 đến ngày hiện tại
        loadData(LocalDate.of(2023, 10, 1), LocalDate.now());
        loadCharts(LocalDate.of(2023, 10, 1), LocalDate.now());
    }

    private void loadData(LocalDate fromDate, LocalDate toDate) {
        int sanPhamDaBan = 0;
        double tongDoanhThu = 0;
        int tongKhachHang = 0;

        ArrayList<HoaDon> hoaDons = hoaDonRepo.getAllHoaDon();
        HashSet<String> uniqueKhachHang = new HashSet<>();

        System.out.println("Filtering data from " + fromDate + " to " + toDate);
        boolean hasData = false;
        for (HoaDon hd : hoaDons) {
            LocalDate ngayTao = hd.getNgayTao();
            if (ngayTao != null && !ngayTao.isBefore(fromDate) && !ngayTao.isAfter(toDate)) {
                System.out.println("Processing HoaDon: MaHoaDon=" + hd.getMaHoaDon() + ", NgayTao=" + ngayTao);
                hasData = true;
                ArrayList<HoaDonChiTiet> chiTiets = hoaDonChiTietRepo.getChiTietByHoaDon(hd.getMaHoaDon());
                for (HoaDonChiTiet ct : chiTiets) {
                    sanPhamDaBan += ct.getSoLuong();
                }
                tongDoanhThu += hd.getThanhTien();
                uniqueKhachHang.add(hd.getMaKhachHang());
            }
        }

        tongKhachHang = uniqueKhachHang.size();

        // Cập nhật giao diện với giá tiền VND nguyên gốc
        txtSanPhamDaBan.setText(String.valueOf(sanPhamDaBan));
        txtTongDoanhThu.setText(String.format("%.2f", tongDoanhThu) + " VNĐ");
        txtTongKhachHang.setText(String.valueOf(tongKhachHang));

        if (!hasData) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian từ " + fromDate + " đến " + toDate);
            txtSanPhamDaBan.setText("0");
            txtTongDoanhThu.setText("0.00 VNĐ");
            txtTongKhachHang.setText("0");
        }
    }

    private void loadCharts(LocalDate fromDate, LocalDate toDate) {
        // Xóa biểu đồ cũ
        jpnDoanhThu.removeAll();
        jpnSanPham.removeAll();

        // Tạo và thêm biểu đồ doanh thu
        JFreeChart revenueChart = createRevenueChart(fromDate, toDate);
        ChartPanel revenueChartPanel = new ChartPanel(revenueChart);
        revenueChartPanel.setPreferredSize(new Dimension(800, 300));
        jpnDoanhThu.add(revenueChartPanel, BorderLayout.CENTER);

        // Tạo và thêm biểu đồ top sản phẩm
        JFreeChart topProductsChart = createTopProductsChart(fromDate, toDate);
        ChartPanel topProductsChartPanel = new ChartPanel(topProductsChart);
        topProductsChartPanel.setPreferredSize(new Dimension(800, 300));
        jpnSanPham.add(topProductsChartPanel, BorderLayout.CENTER);

        // Làm mới giao diện
        jpnDoanhThu.revalidate();
        jpnDoanhThu.repaint();
        jpnSanPham.revalidate();
        jpnSanPham.repaint();
    }

    private JFreeChart createRevenueChart(LocalDate fromDate, LocalDate toDate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<HoaDon> hoaDons = hoaDonRepo.getAllHoaDon();
        Map<LocalDate, Double> dailyRevenue = new HashMap<>();

        for (HoaDon hd : hoaDons) {
            LocalDate ngayTao = hd.getNgayTao();
            if (ngayTao != null && !ngayTao.isBefore(fromDate) && !ngayTao.isAfter(toDate)) {
                dailyRevenue.merge(ngayTao, hd.getThanhTien(), Double::sum);
            }
        }

        if (dailyRevenue.isEmpty()) {
            dataset.addValue(0, "Doanh thu", "Không có dữ liệu");
        } else {
            dailyRevenue.forEach((date, revenue)
                    -> dataset.addValue(revenue, "Doanh thu", date.toString())
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu từ " + fromDate + " đến " + toDate,
                "Ngày", "Doanh thu (VNĐ)", dataset, PlotOrientation.VERTICAL, true, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        return chart;
    }

    private JFreeChart createTopProductsChart(LocalDate fromDate, LocalDate toDate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<HoaDon> hoaDons = hoaDonRepo.getAllHoaDon();
        Map<String, Double> productRevenue = new HashMap<>();

        for (HoaDon hd : hoaDons) {
            LocalDate ngayTao = hd.getNgayTao();
            if (ngayTao != null && !ngayTao.isBefore(fromDate) && !ngayTao.isAfter(toDate)) {
                ArrayList<HoaDonChiTiet> chiTiets = hoaDonChiTietRepo.getChiTietByHoaDon(hd.getMaHoaDon());
                for (HoaDonChiTiet ct : chiTiets) {
                    productRevenue.merge(ct.getTenSanPham(), ct.getThanhTien(), Double::sum);
                }
            }
        }

        if (productRevenue.isEmpty()) {
            dataset.addValue(0, "Doanh thu", "Không có dữ liệu");
        } else {
            productRevenue.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(5)
                    .forEach(entry -> dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey()));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top 5 sản phẩm bán chạy từ " + fromDate + " đến " + toDate,
                "Sản phẩm", "Doanh thu (VNĐ)", dataset, PlotOrientation.HORIZONTAL, true, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        return chart;
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
        txtSanPhamDaBan = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTongKhachHang = new javax.swing.JTextField();
        jpnDoanhThu = new javax.swing.JPanel();
        jpnSanPham = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTGTuSanPham = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        txtTGDenSanPham = new com.toedter.calendar.JDateChooser();
        btnFilterSanPham = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTGTuDoanhThu = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtTGDenDoanhThu = new com.toedter.calendar.JDateChooser();
        btnFilterDoanhThu = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("SẢN PHẨM ĐÃ BÁN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(txtSanPhamDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSanPhamDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("TỔNG DOANH THU");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(90, 90, 90))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("TỔNG KHÁCH HÀNG");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(90, 90, 90))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtTongKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTongKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jpnDoanhThu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnDoanhThuLayout = new javax.swing.GroupLayout(jpnDoanhThu);
        jpnDoanhThu.setLayout(jpnDoanhThuLayout);
        jpnDoanhThuLayout.setHorizontalGroup(
            jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 948, Short.MAX_VALUE)
        );
        jpnDoanhThuLayout.setVerticalGroup(
            jpnDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );

        jpnSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnSanPhamLayout = new javax.swing.GroupLayout(jpnSanPham);
        jpnSanPham.setLayout(jpnSanPhamLayout);
        jpnSanPhamLayout.setHorizontalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 948, Short.MAX_VALUE)
        );
        jpnSanPhamLayout.setVerticalGroup(
            jpnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel7.setText("BIỂU ĐỒ TOP 5 SẢN PHẨM BÁN CHẠY NHẤT");

        jLabel8.setText("Thời gian từ");

        jLabel9.setText("đến");

        btnFilterSanPham.setText("Lọc");
        btnFilterSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addGap(73, 73, 73)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTGTuSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTGDenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnFilterSanPham)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFilterSanPham)
                    .addComponent(txtTGDenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtTGTuSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("BIỂU ĐỒ DOANH THU");

        jLabel5.setText("Thời gian từ");

        txtTGTuDoanhThu.setDateFormatString("yyyy-MM-dd");

        jLabel6.setText("đến");

        txtTGDenDoanhThu.setDateFormatString("yyyy-MM-dd");

        btnFilterDoanhThu.setText("Lọc");
        btnFilterDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterDoanhThuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(171, 171, 171)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTGTuDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTGDenDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFilterDoanhThu)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFilterDoanhThu)
                    .addComponent(txtTGDenDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTGTuDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jpnDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jpnSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterDoanhThuActionPerformed
        LocalDate fromDate = txtTGTuDoanhThu.getDate() != null ?
            txtTGTuDoanhThu.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() :
            LocalDate.of(2023, 10, 1);
        LocalDate toDate = txtTGDenDoanhThu.getDate() != null ?
            txtTGDenDoanhThu.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() :
            LocalDate.now();

        System.out.println("Selected fromDate: " + fromDate + ", toDate: " + toDate);
        if (fromDate.isAfter(toDate)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
            return;
        }

        loadData(fromDate, toDate);
        loadCharts(fromDate, toDate);
    }//GEN-LAST:event_btnFilterDoanhThuActionPerformed

    private void btnFilterSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterSanPhamActionPerformed
        LocalDate fromDate = txtTGTuSanPham.getDate() != null ?
            txtTGTuSanPham.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() :
            LocalDate.of(2023, 10, 1);
        LocalDate toDate = txtTGDenSanPham.getDate() != null ?
            txtTGDenSanPham.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() :
            LocalDate.now();

        System.out.println("Selected fromDate: " + fromDate + ", toDate: " + toDate);
        if (fromDate.isAfter(toDate)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
            return;
        }

        loadCharts(fromDate, toDate);
    }//GEN-LAST:event_btnFilterSanPhamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFilterDoanhThu;
    private javax.swing.JButton btnFilterSanPham;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jpnDoanhThu;
    private javax.swing.JPanel jpnSanPham;
    private javax.swing.JTextField txtSanPhamDaBan;
    private com.toedter.calendar.JDateChooser txtTGDenDoanhThu;
    private com.toedter.calendar.JDateChooser txtTGDenSanPham;
    private com.toedter.calendar.JDateChooser txtTGTuDoanhThu;
    private com.toedter.calendar.JDateChooser txtTGTuSanPham;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTongKhachHang;
    // End of variables declaration//GEN-END:variables
}
