/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.NgonNgu;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NgonNguDAO {

    public List<NgonNgu> getAllNgonNgu() {
        List<NgonNgu> list = new ArrayList<>();
        String sql = "SELECT ID, MaNgonNgu, TenNgonNgu, TrangThai FROM NgonNgu";

        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new NgonNgu(
                        rs.getInt("ID"),
                        rs.getString("MaNgonNgu"),
                        rs.getString("TenNgonNgu"),
                        rs.getBoolean("TrangThai") // Lấy trực tiếp từ DB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertNgonNgu(NgonNgu nn) {
        String sql = "INSERT INTO NgonNgu (MaNgonNgu, TenNgonNgu, trangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nn.getMaNgonNgu());
            ps.setString(2, nn.getTenNgonNgu());
            ps.setBoolean(3, nn.isTrangThai()); // Thêm trạng thái (true = đang bán, false = ngưng bán)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //sửa
    public boolean updateNgonNgu(NgonNgu nn) {
        String sql = "UPDATE NgonNgu SET TenNgonNgu = ?, trangThai = ? WHERE MaNgonNgu = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nn.getTenNgonNgu());
            ps.setBoolean(2, nn.isTrangThai()); // Cập nhật trạng thái (1: Đang bán, 0: Ngưng bán)
            ps.setString(3, nn.getMaNgonNgu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteNgonNgu(String maNgonNgu) {
        String sql = "UPDATE NgonNgu SET TrangThai = 0 WHERE MaNgonNgu = ?"; // Cập nhật trạng thái thay vì xóa
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNgonNgu);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Cập nhật thành công
            } else {
                System.out.println("Không tìm thấy Ngôn ngữ để cập nhật trạng thái!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm thể loại
    public List<NgonNgu> searchNgonNgu(String keyword) {
        List<NgonNgu> list = new ArrayList<>();
        String sql = "SELECT * FROM NgonNgu WHERE TenNgonNgu LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new NgonNgu(
                        rs.getInt("ID"),
                        rs.getString("MaNgonNgu"),
                        rs.getString("TenNgonNgu"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Kiểm tra mã thể loại đã tồn tại chưa

    public boolean isMaNgonNguExists(String maNgonNgu) {
        String sql = "SELECT COUNT(*) FROM NgonNgu WHERE MaNgonNgu = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNgonNgu);
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
