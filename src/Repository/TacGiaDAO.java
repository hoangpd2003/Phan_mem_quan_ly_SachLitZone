/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.TacGia;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TacGiaDAO {

    public List<TacGia> getAllTacGia() {
        List<TacGia> list = new ArrayList<>();
        String sql = "SELECT ID, MaTacGia, TenTacGia, TrangThai FROM TacGia"; // Lấy thêm cột TrangThai
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TacGia(
                        rs.getInt("ID"),
                        rs.getString("MaTacGia"),
                        rs.getString("TenTacGia"),
                        rs.getBoolean("TrangThai") // Lấy trực tiếp từ DB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertTacGia(TacGia tg) {
        String sql = "INSERT INTO TacGia (MaTacGia, TenTacGia, trangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tg.getMaTacGia());
            ps.setString(2, tg.getTenTacGia());
            ps.setBoolean(3, tg.isTrangThai()); // Thêm trạng thái (true = đang bán, false = ngưng bán)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//sửa
    public boolean updateTacGia(TacGia tl) {
        String sql = "UPDATE TacGia SET TenTacGia = ?, trangThai = ? WHERE MaTacGia = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.getTenTacGia());
            ps.setBoolean(2, tl.isTrangThai()); // Cập nhật trạng thái (1: Đang bán, 0: Ngưng bán)
            ps.setString(3, tl.getMaTacGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTacGia(String maTacGia) {
        String sql = "UPDATE TacGia SET TrangThai = 0 WHERE MaTacGia = ?"; // Cập nhật trạng thái thay vì xóa
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTacGia);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Cập nhật thành công
            } else {
                System.out.println("Không tìm thấy Tác giả để cập nhật trạng thái!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm thể loại
    public List<TacGia> searchTacGia(String keyword) {
        List<TacGia> list = new ArrayList<>();
        String sql = "SELECT * FROM TacGia WHERE TenTacGia LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TacGia(
                        rs.getInt("ID"),
                        rs.getString("MaTacGia"),
                        rs.getString("TenTacGia"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Kiểm tra mã thể loại đã tồn tại chưa

    public boolean isMaTacGiaExists(String maTacGia) {
        String sql = "SELECT COUNT(*) FROM TacGia WHERE MaTacGia = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTacGia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0 thì mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
