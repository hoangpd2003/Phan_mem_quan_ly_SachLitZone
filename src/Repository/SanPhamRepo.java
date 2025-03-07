/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.SanPham;
import Utils.DBConnection;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Hoang
 */
public class SanPhamRepo {

    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> list = new ArrayList<>();
        String sql = """
        SELECT 
            sp.ID, 
            sp.MaSanPham, 
            sp.TenSanPham, 
            sp.GiaBan, 
            sp.SoLuong, 
            sp.HinhAnh, 
            sp.TrangThai,
            nn.TenNgonNgu, 
            tl.TenTheLoai, 
            tg.TenTacGia, 
            nxb.TenNXB
        FROM SanPham sp
        LEFT JOIN NgonNgu nn ON sp.IDNgonNgu = nn.ID
        LEFT JOIN TheLoai tl ON sp.IDTheLoai = tl.ID
        LEFT JOIN TacGia tg ON sp.IDTacGia = tg.ID
        LEFT JOIN NXB nxb ON sp.IDNXB = nxb.ID
        """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt("ID"));
                sp.setMaSanPham(rs.getString("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setGiaBan(rs.getDouble("GiaBan"));
                sp.setSoLuong(rs.getInt("SoLuong"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setTenNxb(rs.getString("TenNXB"));
                sp.setTenTheLoai(rs.getString("TenTheLoai"));
                sp.setTenNgonNgu(rs.getString("TenNgonNgu"));
                sp.setTenTacGia(rs.getString("TenTacGia"));
                sp.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm
    public boolean addSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (MaSanPham, TenSanPham, GiaBan, HinhAnh, SoLuong, IDTheLoai, IDNgonNgu, IDNXB, IDTacGia, TrangThai) VALUES (?, ?, ?, ?, ?, (SELECT ID FROM TheLoai WHERE TenTheLoai = ?), (SELECT ID FROM NgonNgu WHERE TenNgonNgu = ?), (SELECT ID FROM NXB WHERE TenNXB = ?), (SELECT ID FROM TacGia WHERE TenTacGia = ?), ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sp.getMaSanPham());
            stmt.setString(2, sp.getTenSanPham());
            stmt.setDouble(3, sp.getGiaBan());
            stmt.setString(4, sp.getHinhAnh());
            stmt.setInt(5, sp.getSoLuong());
            stmt.setString(6, sp.getTenTheLoai());
            stmt.setString(7, sp.getTenNgonNgu());
            stmt.setString(8, sp.getTenNxb());
            stmt.setString(9, sp.getTenTacGia());
            stmt.setBoolean(10, sp.isTrangThai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật sản phẩm
    public boolean updateSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET TenSanPham=?, GiaBan=?, HinhAnh=?, SoLuong=?, IDTheLoai=(SELECT ID FROM TheLoai WHERE TenTheLoai=?), IDNgonNgu=(SELECT ID FROM NgonNgu WHERE TenNgonNgu=?), IDNXB=(SELECT ID FROM NXB WHERE TenNXB=?), IDTacGia=(SELECT ID FROM TacGia WHERE TenTacGia=?), TrangThai=? WHERE MaSanPham=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sp.getTenSanPham());
            stmt.setDouble(2, sp.getGiaBan());
            stmt.setString(3, sp.getHinhAnh());
            stmt.setInt(4, sp.getSoLuong());
            stmt.setString(5, sp.getTenTheLoai());
            stmt.setString(6, sp.getTenNgonNgu());
            stmt.setString(7, sp.getTenNxb());
            stmt.setString(8, sp.getTenTacGia());
            stmt.setBoolean(9, sp.isTrangThai());
            stmt.setString(10, sp.getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Xóa sản mềm sản phẩm
    public boolean updateTrangThaiSanPham(String maSanPham) {
        String sql = "UPDATE SanPham SET TrangThai = 0 WHERE MaSanPham = ?"; // 0 là trạng thái Ngưng bán

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSanPham);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    public ArrayList<SanPham> timKiemSanPham(String tuKhoa) {
        ArrayList<SanPham> dsSanPham = new ArrayList<>();
        String sql = """
            SELECT DISTINCT sp.ID, sp.MaSanPham, sp.TenSanPham, sp.GiaBan, sp.SoLuong, sp.HinhAnh, sp.TrangThai,
                            nxb.TenNXB, tl.TenTheLoai, tg.TenTacGia, nn.TenNgonNgu
            FROM SanPham sp
            JOIN NXB nxb ON sp.IDNXB = nxb.ID
            JOIN TheLoai tl ON sp.IDTheLoai = tl.ID
            JOIN TacGia tg ON sp.IDTacGia = tg.ID
            JOIN NgonNgu nn ON sp.IDNgonNgu = nn.ID
            WHERE sp.MaSanPham LIKE ? 
               OR sp.TenSanPham LIKE ? 
               OR nxb.TenNXB LIKE ?
               OR tl.TenTheLoai LIKE ?
               OR tg.TenTacGia LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, "%" + tuKhoa + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getInt("ID"),
                            rs.getString("MaSanPham"),
                            rs.getString("TenSanPham"),
                            rs.getDouble("GiaBan"),
                            rs.getString("HinhAnh"),
                            rs.getInt("SoLuong"),
                            rs.getString("TenTheLoai"),
                            rs.getString("TenNgonNgu"),
                            rs.getString("TenNXB"),
                            rs.getString("TenTacGia"),
                            rs.getBoolean("TrangThai")
                    );
                    dsSanPham.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSanPham;
    }

    public List<String> getAllTheLoai() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT TenTheLoai FROM TheLoai WHERE TrangThai = 1"; // Lọc thể loại có trạng thái true (1)

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("TenTheLoai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllNgonNgu() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT TenNgonNgu FROM NgonNgu where TrangThai = 1";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("TenNgonNgu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllNXB() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT TenNXB FROM NXB where TrangThai = 1";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("TenNXB"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllTacGia() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT TenTacGia FROM TacGia where TrangThai = 1";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("TenTacGia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean kiemTraTonTai(String maSanPham) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE MaSanPham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<SanPham> getListSP() {
        String sql = """
                     select sp.ID, sp.MaSanPham, sp.TenSanPham, sp.GiaBan, sp.HinhAnh, sp.SoLuong, tl.TenTheLoai, nn.TenNgonNgu, nxb.TenNXB, tg.TenTacGia from SanPham sp 
                     JOIN TheLoai tl ON tl.ID = sp.IDTheLoai
                     JOIN NgonNgu nn ON nn.ID = sp.IDNgonNgu
                     JOIN NXB nxb ON nxb.ID = sp.IDNXB
                     JOIN TacGia tg ON tg.ID = sp.IDTacGia
                     WHERE sp.TrangThai = 1
                     """;
        ArrayList<SanPham> splist = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt(1));
                sp.setMaSanPham(rs.getString(2));
                sp.setTenSanPham(rs.getString(3));
                sp.setGiaBan(rs.getDouble(4));
                sp.setHinhAnh(rs.getString(5));
                sp.setSoLuong(rs.getInt(6));
                sp.setTenTheLoai(rs.getString(7));
                sp.setTenNgonNgu(rs.getString(8));
                sp.setTenNxb(rs.getString(9));
                sp.setTenTacGia(rs.getString(10));
                splist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return splist;
    }

    public ArrayList<SanPham> getListSP(String maSP) {
        String sql = """
                 SELECT sp.ID, sp.MaSanPham, sp.TenSanPham, sp.GiaBan, sp.HinhAnh, sp.SoLuong, 
                        tl.TenTheLoai, nn.TenNgonNgu, nxb.TenNXB, tg.TenTacGia 
                 FROM SanPham sp 
                 JOIN TheLoai tl ON tl.ID = sp.IDTheLoai
                 JOIN NgonNgu nn ON nn.ID = sp.IDNgonNgu
                 JOIN NXB nxb ON nxb.ID = sp.IDNXB
                 JOIN TacGia tg ON tg.ID = sp.IDTacGia
                 WHERE sp.TrangThai = 1 AND sp.MaSanPham = ?
                 """;
        ArrayList<SanPham> splist = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);  // Truy vấn theo MaSanPham
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt(1));
                sp.setMaSanPham(rs.getString(2));
                sp.setTenSanPham(rs.getString(3));
                sp.setGiaBan(rs.getDouble(4));
                sp.setHinhAnh(rs.getString(5));
                sp.setSoLuong(rs.getInt(6));
                sp.setTenTheLoai(rs.getString(7));
                sp.setTenNgonNgu(rs.getString(8));
                sp.setTenNxb(rs.getString(9));
                sp.setTenTacGia(rs.getString(10));
                splist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return splist;
    }

    public void updateSP(String maSP, int soLuong) {
        String sql = "UPDATE SanPham SET SoLuong = ? WHERE MaSanPham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setString(2, maSP);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateSP(SanPham sp) {
        String query = "UPDATE SanPham SET SoLuong = ? WHERE MaSanPham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, sp.getSoLuong());
            ps.setString(2, sp.getMaSanPham());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    public SanPham getSanPhamByMa(String maSanPham) {
        // Truy vấn cơ sở dữ liệu để lấy sản phẩm theo mã
        // Ví dụ:
        String query = "SELECT * FROM SanPham WHERE MaSanPham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maSanPham);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSanPham(rs.getString("MaSanPham"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setSoLuong(rs.getInt("SoLuong"));
                    // Các trường khác...
                    return sp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public ArrayList<SanPham> searchSanPham(String keyword) {
        String sqlBase = """
        SELECT sp.ID, sp.MaSanPham, sp.TenSanPham, sp.GiaBan, sp.HinhAnh, sp.SoLuong, 
               tl.TenTheLoai, nn.TenNgonNgu, nxb.TenNXB, tg.TenTacGia 
        FROM SanPham sp 
        LEFT JOIN TheLoai tl ON tl.ID = sp.IDTheLoai
        LEFT JOIN NgonNgu nn ON nn.ID = sp.IDNgonNgu
        LEFT JOIN NXB nxb ON nxb.ID = sp.IDNXB
        LEFT JOIN TacGia tg ON tg.ID = sp.IDTacGia
        WHERE sp.TrangThai = 1 
    """;

        StringBuilder sql = new StringBuilder(sqlBase);
        ArrayList<Object> params = new ArrayList<>();
        boolean hasCondition = false;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchPattern = "%" + keyword.trim() + "%";
            String numericKeyword = keyword.trim().replaceAll("[^0-9]", ""); // Trích xuất số từ từ khóa

            // Tìm kiếm trên các trường văn bản
            sql.append(" AND (sp.MaSanPham LIKE ? OR sp.TenSanPham LIKE ? OR tl.TenTheLoai LIKE ? "
                    + "OR nn.TenNgonNgu LIKE ? OR nxb.TenNXB LIKE ? OR tg.TenTacGia LIKE ?)");
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            hasCondition = true;

            // Nếu từ khóa chứa số, thêm điều kiện tìm kiếm số lượng tồn kho
            if (!numericKeyword.isEmpty()) {
                try {
                    int soLuong = Integer.parseInt(numericKeyword);
                    sql.append(" OR sp.SoLuong = ?");
                    params.add(soLuong);
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không parse được số
                    System.out.println("Không thể parse số lượng từ khóa: " + numericKeyword);
                }
            }
        }

        ArrayList<SanPham> splist = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Thiết lập các tham số
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt("ID"));
                sp.setMaSanPham(rs.getString("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setGiaBan(rs.getDouble("GiaBan"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setSoLuong(rs.getInt("SoLuong"));
                sp.setTenTheLoai(rs.getString("TenTheLoai"));
                sp.setTenNgonNgu(rs.getString("TenNgonNgu"));
                sp.setTenNxb(rs.getString("TenNXB"));
                sp.setTenTacGia(rs.getString("TenTacGia"));
                splist.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm sản phẩm: " + e.getMessage());
        }
        return splist;
    }
}
