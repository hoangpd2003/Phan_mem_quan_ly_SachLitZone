/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.NXB;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NXBDAO {

    public List<NXB> getAllNXB() {
        List<NXB> list = new ArrayList<>();
        String sql = "SELECT ID, MaNXB, TenNXB, TrangThai FROM NXB"; // Lấy thêm cột TrangThai
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new NXB(
                        rs.getInt("ID"),
                        rs.getString("MaNXB"),
                        rs.getString("TenNXB"),
                        rs.getBoolean("TrangThai") // Lấy trực tiếp từ DB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertNXB(NXB nxb) {
        String sql = "INSERT INTO NXB (MaNXB, TenNXB, trangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.getMaNXB());
            ps.setString(2, nxb.getTenNXB());
            ps.setBoolean(3, nxb.isTrangThai()); // Thêm trạng thái (true = đang bán, false = ngưng bán)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//sửa
    public boolean updateNXB(NXB nxb) {
        String sql = "UPDATE NXB SET TenNXB = ?, trangThai = ? WHERE MaNXB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.getTenNXB());
            ps.setBoolean(2, nxb.isTrangThai()); // Cập nhật trạng thái (1: Đang bán, 0: Ngưng bán)
            ps.setString(3, nxb.getMaNXB());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteNXB(String maNXB) {
        String sql = "UPDATE NXB SET TrangThai = 0 WHERE MaNXB = ?"; // Cập nhật trạng thái thay vì xóa
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Cập nhật thành công
            } else {
                System.out.println("Không tìm thấy NXB để cập nhật trạng thái!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm thể loại
    public List<NXB> searchNXB(String keyword) {
        List<NXB> list = new ArrayList<>();
        String sql = "SELECT * FROM NXB WHERE TenNXB LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new NXB(
                        rs.getInt("ID"),
                        rs.getString("MaNXB"),
                        rs.getString("TenNXB"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Kiểm tra mã NXB đã tồn tại chưa

    public boolean isMaNXBExists(String maNXB) {
        String sql = "SELECT COUNT(*) FROM NXB WHERE MaNXB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
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
