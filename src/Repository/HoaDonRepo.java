package Repository;

import Model.SanPham;
import Model.HoaDon;
import Utils.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Hoang
 */
public class HoaDonRepo {

    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> list = new ArrayList<>();
        String sql = """
                SELECT 
                    HD.MaHoaDon, 
                    NV.MaNhanVien, 
                    KH.MaKhachHang, 
                    PGG.MaPhieuGiamGia, 
                    PGG.TenPhieuGiamGia,  
                    HD.NgayTao, 
                    HD.TongTien, 
                    PGG.SoTienGiam, 
                    (HD.TongTien - COALESCE(PGG.SoTienGiam, 0)) AS ThanhTien 
                FROM HoaDon HD 
                JOIN NhanVien NV ON HD.IDNhanVien = NV.ID 
                JOIN KhachHang KH ON HD.IDKhachHang = KH.ID 
                LEFT JOIN PhieuGiamGia PGG ON HD.IDPhieuGiamGia = PGG.ID
                """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("MaHoaDon"));
                hd.setMaNhanVien(rs.getString("MaNhanVien"));
                hd.setMaKhachHang(rs.getString("MaKhachHang"));
                hd.setMaPhieuGiamGia(rs.getString("MaPhieuGiamGia")); // Lấy mã phiếu giảm giá
                hd.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia")); // Lấy tên phiếu giảm giá
                Date ngayTao = rs.getDate("NgayTao");
                if (ngayTao != null) {
                    hd.setNgayTao(ngayTao.toLocalDate()); // Chuyển sang LocalDate
                } else {
                    hd.setNgayTao(null);
                }
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setSoTienGiam(rs.getDouble("SoTienGiam"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách hóa đơn chưa thanh toán
    public ArrayList<HoaDon> getListHDCHT() {
        String sql = """
             SELECT hd.MaHoaDon, nv.MaNhanVien, kh.MaKhachHang, kh.TenKhachHang, hd.NgayTao, hd.TongTien, pgg.SoTienGiam, kh.SDT, (hd.TongTien - COALESCE(pgg.SoTienGiam, 0)) AS ThanhTien, pgg.TenPhieuGiamGia
             FROM HoaDon hd
             JOIN NhanVien nv ON nv.ID = hd.IDNhanVien
             JOIN KhachHang kh ON kh.ID = hd.IDKhachHang
             LEFT JOIN PhieuGiamGia pgg ON pgg.ID = hd.IDPhieuGiamGia
             WHERE hd.TrangThai = 0  
             """;
        ArrayList<HoaDon> hdlist = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("MaHoaDon"));
                hd.setMaNhanVien(rs.getString("MaNhanVien"));
                hd.setMaKhachHang(rs.getString("MaKhachHang"));
                hd.setTenKhachHang(rs.getString("TenKhachHang"));
                Date ngayTao = rs.getDate("NgayTao");
                if (ngayTao != null) {
                    hd.setNgayTao(ngayTao.toLocalDate());
                } else {
                    hd.setNgayTao(null);
                }
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setSoTienGiam(rs.getDouble("SoTienGiam"));
                hd.setSdt(rs.getString("SDT"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia"));
                hdlist.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hdlist;
    }

    public ArrayList<HoaDon> getListHDHT() {
        String sql = """
         SELECT hd.MaHoaDon, nv.MaNhanVien, kh.MaKhachHang, kh.TenKhachHang, hd.NgayTao, hd.TongTien, pgg.SoTienGiam, kh.SDT, (hd.TongTien - COALESCE(pgg.SoTienGiam, 0)) AS ThanhTien, pgg.TenPhieuGiamGia
         FROM HoaDon hd
         JOIN NhanVien nv ON nv.ID = hd.IDNhanVien
         JOIN KhachHang kh ON kh.ID = hd.IDKhachHang
         LEFT JOIN PhieuGiamGia pgg ON pgg.ID = hd.IDPhieuGiamGia
         WHERE hd.TrangThai = 1
         ORDER BY hd.NgayTao DESC
         """;
        ArrayList<HoaDon> hdlist = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString(1));
                hd.setMaNhanVien(rs.getString(2));
                hd.setMaKhachHang(rs.getString(3));
                hd.setTenKhachHang(rs.getString(4));
                Date ngayTao = rs.getDate(5);
                if (ngayTao != null) {
                    hd.setNgayTao(ngayTao.toLocalDate());
                } else {
                    hd.setNgayTao(null);
                }
                hd.setTongTien(rs.getDouble(6));
                hd.setSoTienGiam(rs.getDouble(7));
                hd.setSdt(rs.getString(8));
                hd.setThanhTien(rs.getDouble(9));
                hd.setTenPhieuGiamGia(rs.getString(10));
                hdlist.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hdlist;
    }

    public boolean updateHD(double tongTien, String maHD) {
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE ID = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tongTien);
            ps.setString(2, maHD);
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addHD(HoaDon hd) {
        String sql = """
         INSERT INTO HoaDon (IDNhanVien, IDKhachHang, IDPhieuGiamGia, TrangThai, NgayTao, TongTien, SoTienGiam, ThanhTien)
         VALUES ((SELECT ID FROM NhanVien WHERE MaNhanVien = ?), 
                 (SELECT ID FROM KhachHang WHERE MaKhachHang = ?), 
                 (SELECT ID FROM PhieuGiamGia WHERE TenPhieuGiamGia = ?), 
                 ?, ?, ?, ?, ?)
         """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaNhanVien());
            ps.setString(2, hd.getMaKhachHang());
            ps.setString(3, hd.getTenPhieuGiamGia());
            ps.setBoolean(4, hd.getTrangThai());
            ps.setDate(5, Date.valueOf(hd.getNgayTao()));
            ps.setDouble(6, hd.getTongTien());
            ps.setDouble(7, hd.getSoTienGiam());
            ps.setDouble(8, hd.getThanhTien());
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean huyHD(String maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?)";
        String sql2 = "DELETE FROM HoaDon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); PreparedStatement ps2 = conn.prepareStatement(sql2)) {
            conn.setAutoCommit(false);
            ps.setString(1, maHoaDon);
            ps2.setString(1, maHoaDon);
            int kq = ps.executeUpdate();
            int kq2 = ps2.executeUpdate();
            if (kq >= 0 && kq2 > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean thanhtoanHD(String maHD, double soTienGiam, double tongTien) {
        String sql = """
            UPDATE HoaDon 
            SET TrangThai = ?, 
                SoTienGiam = ?, 
                TongTien = ?, 
                ThanhTien = ? - COALESCE(?, 0)
            WHERE MaHoaDon = ?
            """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setDouble(2, soTienGiam);
            ps.setDouble(3, tongTien);
            ps.setDouble(4, tongTien);
            ps.setDouble(5, soTienGiam);
            ps.setString(6, maHD);
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public HoaDon getHoaDonByMa(String maHD) {
        String sql = """
    SELECT 
        HD.ID, 
        HD.MaHoaDon, 
        NV.MaNhanVien, 
        NV.TenNhanVien,  -- Thêm cột TenNhanVien từ bảng NhanVien
        KH.MaKhachHang, 
        KH.TenKhachHang, 
        KH.SDT, 
        PGG.TenPhieuGiamGia, 
        HD.TrangThai, 
        HD.NgayTao, 
        HD.TongTien, 
        HD.SoTienGiam, 
        HD.ThanhTien
    FROM HoaDon HD
    JOIN NhanVien NV ON HD.IDNhanVien = NV.ID
    JOIN KhachHang KH ON HD.IDKhachHang = KH.ID
    LEFT JOIN PhieuGiamGia PGG ON HD.IDPhieuGiamGia = PGG.ID
    WHERE HD.MaHoaDon = ?
    """;
        HoaDon hoaDon = null;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(rs.getString("MaHoaDon"));
                    hoaDon.setMaNhanVien(rs.getString("MaNhanVien"));
                    hoaDon.setTenNhanVien(rs.getString("TenNhanVien")); // Điền TenNhanVien
                    hoaDon.setMaKhachHang(rs.getString("MaKhachHang"));
                    hoaDon.setTenKhachHang(rs.getString("TenKhachHang"));
                    hoaDon.setSdt(rs.getString("SDT"));
                    hoaDon.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia"));
                    hoaDon.setTrangThai(rs.getBoolean("TrangThai"));
                    Date ngayTao = rs.getDate("NgayTao");
                    if (ngayTao != null) {
                        hoaDon.setNgayTao(ngayTao.toLocalDate());
                    } else {
                        hoaDon.setNgayTao(null);
                    }
                    hoaDon.setTongTien(rs.getDouble("TongTien"));
                    hoaDon.setSoTienGiam(rs.getDouble("SoTienGiam"));
                    hoaDon.setThanhTien(rs.getDouble("ThanhTien"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hoaDon;
    }

    public boolean updateMaPhieuGiamGia(String maHD, String maPhieuGiamGia) {
        String sql = "UPDATE HoaDon SET IDPhieuGiamGia = (SELECT ID FROM PhieuGiamGia WHERE TenPhieuGiamGia = ?) WHERE MaHoaDon = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuGiamGia);
            ps.setString(2, maHD);
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNhanVienHD(String maHD, String maNhanVien) {
        String sql = "UPDATE HoaDon SET IDNhanVien = (SELECT ID FROM NhanVien WHERE MaNhanVien = ?) WHERE MaHoaDon = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ps.setString(2, maHD);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
