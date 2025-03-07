/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.HoaDon;
import Model.HoaDonChiTiet;
import Model.SanPham;
import Utils.DBConnection;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author Hoang
 */
public class HoaDonChiTietRepo {

    public ArrayList<HoaDonChiTiet> getChiTietByHoaDon(String maHD) {
        ArrayList<HoaDonChiTiet> list = new ArrayList<>();
        String sql = """
                     SELECT CTHD.MaChiTietHoaDon, SP.MaSanPham, SP.TenSanPham, CTHD.SoLuong, SP.GiaBan, (CTHD.SoLuong * SP.GiaBan) AS ThanhTien 
                     FROM ChiTietHoaDon CTHD 
                     JOIN SanPham SP ON CTHD.IDSanPham = SP.ID 
                     WHERE CTHD.IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?)
                     """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDonChiTiet cthd = new HoaDonChiTiet();
                cthd.setMaChiTietHoaDon(rs.getString("MaChiTietHoaDon"));
                cthd.setMaHoaDon(maHD);
                cthd.setMaSanPham(rs.getString("MaSanPham"));
                cthd.setTenSanPham(rs.getString("TenSanPham"));
                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setDonGia(rs.getDouble("GiaBan"));
                cthd.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(cthd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaHDCT(String MaHD, String MaSP) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?) "
                + "AND IDSanPham = (SELECT ID FROM SanPham WHERE MaSanPham = ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, MaHD);
            ps.setString(2, MaSP);

            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateGH(String maHD, String maSP, int soLuongMua) {
        String sql = "UPDATE ChiTietHoaDon "
                + "SET SoLuong = ? "
                + "WHERE IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?) "
                + "AND IDSanPham = (SELECT ID FROM SanPham WHERE MaSanPham = ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMua);
            ps.setString(2, maHD);
            ps.setString(3, maSP);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGH(String maHD, String maSP, int soLuongMua) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?) AND IDSanPham = (SELECT ID FROM SanPham WHERE MaSanPham = ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maSP);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int soLuongCu = rs.getInt("SoLuong"); // Lấy số lượng cũ
                int soLuongMoi = soLuongCu + soLuongMua; // Cộng dồn số lượng mới
                String sql1 = "UPDATE ChiTietHoaDon SET SoLuong = ? WHERE IDHoaDon = (SELECT ID FROM HoaDon WHERE MaHoaDon = ?) AND IDSanPham = (SELECT ID FROM SanPham WHERE MaSanPham = ?)";
                try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {
                    ps1.setInt(1, soLuongMoi);
                    ps1.setString(2, maHD);
                    ps1.setString(3, maSP);
                    ps1.executeUpdate();
                }
            } else {
                String sql2 = "INSERT INTO ChiTietHoaDon (IDHoaDon, IDSanPham, SoLuong) VALUES ((SELECT ID FROM HoaDon WHERE MaHoaDon = ?), (SELECT ID FROM SanPham WHERE MaSanPham = ?), ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, maHD);
                    ps2.setString(2, maSP);
                    ps2.setInt(3, soLuongMua);
                    ps2.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean xoaHDCT(String MaHD) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE IDHoaDon IN (SELECT ID FROM HoaDon WHERE MaHoaDon = ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, MaHD);

            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean huyHD(String maHD) {
//    String sql = "DELETE FROM HoaDon WHERE MaHoaDon = ?";
//    try (Connection conn = DBConnection.getConnection();
//         PreparedStatement ps = conn.prepareStatement(sql)) {
//        ps.setString(1, maHD);
//        int result = ps.executeUpdate();
//        return result > 0;
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return false;
//}
}
