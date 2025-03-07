/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.TheLoai;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TheLoaiDAO {

    public List<TheLoai> getAllTheLoai() {
        List<TheLoai> list = new ArrayList<>();
        String sql = "SELECT ID, MaTheLoai, TenTheLoai, TrangThai FROM TheLoai"; // Lấy thêm cột TrangThai
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TheLoai(
                        rs.getInt("ID"),
                        rs.getString("MaTheLoai"),
                        rs.getString("TenTheLoai"),
                        rs.getBoolean("TrangThai") // Lấy trực tiếp từ DB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertTheLoai(TheLoai tl) {
        String sql = "INSERT INTO TheLoai (MaTheLoai, TenTheLoai, trangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.getMaTheLoai());
            ps.setString(2, tl.getTenTheLoai());
            ps.setBoolean(3, tl.isTrangThai()); // Thêm trạng thái (true = đang bán, false = ngưng bán)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//sửa
    public boolean updateTheLoai(TheLoai tl) {
        String sql = "UPDATE TheLoai SET TenTheLoai = ?, trangThai = ? WHERE MaTheLoai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.getTenTheLoai());
            ps.setBoolean(2, tl.isTrangThai()); // Cập nhật trạng thái (1: Đang bán, 0: Ngưng bán)
            ps.setString(3, tl.getMaTheLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTheLoai(String maTheLoai) {
        String sql = "UPDATE TheLoai SET TrangThai = 0 WHERE MaTheLoai = ?"; // Cập nhật trạng thái thay vì xóa
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTheLoai);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Cập nhật thành công
            } else {
                System.out.println("Không tìm thấy thể loại để cập nhật trạng thái!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm thể loại
    public List<TheLoai> searchTheLoai(String keyword) {
        List<TheLoai> list = new ArrayList<>();
        String sql = "SELECT * FROM TheLoai WHERE TenTheLoai LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TheLoai(
                        rs.getInt("ID"),
                        rs.getString("MaTheLoai"),
                        rs.getString("TenTheLoai"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Kiểm tra mã thể loại đã tồn tại chưa

    public boolean isMaTheLoaiExists(String maTheLoai) {
        String sql = "SELECT COUNT(*) FROM TheLoai WHERE MaTheLoai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTheLoai);
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
