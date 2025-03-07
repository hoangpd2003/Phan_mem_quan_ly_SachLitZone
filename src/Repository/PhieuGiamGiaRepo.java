/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

/**
 *
 * @author thanhnguyen
 */
import Model.PhieuGiamGia;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Hoang
 */
public class PhieuGiamGiaRepo {

    public ArrayList<PhieuGiamGia> getAll() {
        // Cập nhật trạng thái của các phiếu hết hạn trước khi lấy dữ liệu
        updateExpiredCoupons();

        ArrayList<PhieuGiamGia> list = new ArrayList<>();
        String query = "SELECT ID, MaPhieuGiamGia, TenPhieuGiamGia, SoLuong, NgayBatDau, NgayKetThuc, SoTienGiam, TrangThai "
                + "FROM PhieuGiamGia";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuGiamGia pgg = new PhieuGiamGia();
                pgg.setId(rs.getInt("ID"));
                pgg.setMaPhieuGiamGia(rs.getString("MaPhieuGiamGia"));
                pgg.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia"));
                pgg.setSoLuong(rs.getInt("SoLuong"));
                pgg.setNgayBatDau(rs.getDate("NgayBatDau"));
                pgg.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                pgg.setSoTienGiam(rs.getDouble("SoTienGiam"));
                pgg.setTrangThai(rs.getBoolean("TrangThai")); // Thêm trạng thái
                list.add(pgg);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return list;
    }

    // Lấy một phiếu giảm giá theo ID
    public PhieuGiamGia getById(int id) {
        String query = "SELECT ID, MaPhieuGiamGia, TenPhieuGiamGia, SoLuong, NgayBatDau, NgayKetThuc, SoTienGiam "
                + "FROM PhieuGiamGia WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhieuGiamGia pgg = new PhieuGiamGia();
                    pgg.setId(rs.getInt("ID"));
                    pgg.setMaPhieuGiamGia(rs.getString("MaPhieuGiamGia"));
                    pgg.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia"));
                    pgg.setSoLuong(rs.getInt("SoLuong"));
                    pgg.setNgayBatDau(rs.getDate("NgayBatDau"));
                    pgg.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    pgg.setSoTienGiam(rs.getDouble("SoTienGiam"));
                    return pgg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    // Thêm một phiếu giảm giá mới
    public boolean add(PhieuGiamGia pgg) {
        String query = "INSERT INTO PhieuGiamGia (MaPhieuGiamGia, TenPhieuGiamGia, SoLuong, NgayBatDau, NgayKetThuc, SoTienGiam) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, pgg.getMaPhieuGiamGia());
            ps.setString(2, pgg.getTenPhieuGiamGia());
            ps.setInt(3, pgg.getSoLuong());
            ps.setDate(4, pgg.getNgayBatDau() != null ? new java.sql.Date(pgg.getNgayBatDau().getTime()) : null);
            ps.setDate(5, pgg.getNgayKetThuc() != null ? new java.sql.Date(pgg.getNgayKetThuc().getTime()) : null);
            ps.setDouble(6, pgg.getSoTienGiam());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    public boolean update(PhieuGiamGia pgg) {
        String query = "UPDATE PhieuGiamGia SET MaPhieuGiamGia = ?, TenPhieuGiamGia = ?, SoLuong = ?, "
                + "NgayBatDau = ?, NgayKetThuc = ?, SoTienGiam = ?, TrangThai = ? WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, pgg.getMaPhieuGiamGia());
            ps.setString(2, pgg.getTenPhieuGiamGia());
            ps.setInt(3, pgg.getSoLuong());
            ps.setDate(4, pgg.getNgayBatDau() != null ? new java.sql.Date(pgg.getNgayBatDau().getTime()) : null);
            ps.setDate(5, pgg.getNgayKetThuc() != null ? new java.sql.Date(pgg.getNgayKetThuc().getTime()) : null);
            ps.setDouble(6, pgg.getSoTienGiam());
            ps.setBoolean(7, pgg.isTrangThai()); // Thêm cập nhật trạng thái
            ps.setInt(8, pgg.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    // Xóa một phiếu giảm giá theo ID
    public boolean delete(int id) {
        String query = "DELETE FROM PhieuGiamGia WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    public PhieuGiamGia getPhieuGiamGiaByTen(String tenPhieuGiamGia) {
        String sql = "SELECT * FROM PhieuGiamGia WHERE TenPhieuGiamGia = ? AND TrangThai = 1"; // Chỉ lấy phiếu chưa hết hạn
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenPhieuGiamGia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PhieuGiamGia pgg = new PhieuGiamGia();
                pgg.setId(rs.getInt("ID"));
                pgg.setMaPhieuGiamGia(rs.getString("MaPhieuGiamGia"));
                pgg.setTenPhieuGiamGia(rs.getString("TenPhieuGiamGia"));
                pgg.setSoLuong(rs.getInt("SoLuong"));
                pgg.setNgayBatDau(rs.getDate("NgayBatDau"));
                pgg.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                pgg.setSoTienGiam(rs.getDouble("SoTienGiam"));
                pgg.setTrangThai(rs.getBoolean("TrangThai")); // Lấy trạng thái
                return pgg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTrangThaiPhieuGiamGia(int id) {
        String sql = "UPDATE PhieuGiamGia SET TrangThai=0 WHERE ID= ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updateExpiredCoupons() {
        String sql = "UPDATE PhieuGiamGia SET TrangThai = 0 WHERE NgayKetThuc < ? AND TrangThai = 1";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime()); // Lấy ngày hiện tại
            ps.setDate(1, currentDate);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }
}
