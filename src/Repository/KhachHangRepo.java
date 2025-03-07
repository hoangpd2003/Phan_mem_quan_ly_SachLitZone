package Repository;

import Model.KhachHang;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KhachHangRepo {

    private static KhachHangRepo instance;

    public interface KhachHangListener {

        void onKhachHangAdded(KhachHang kh);
    }
    private Set<KhachHangListener> listeners = new HashSet<>();

    private KhachHangRepo() {
    }

    public static KhachHangRepo getInstance() {
        if (instance == null) {
            instance = new KhachHangRepo();
        }
        return instance;
    }

    public void addListener(KhachHangListener listener) {
        listeners.add(listener);
    }

    public void removeListener(KhachHangListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(KhachHang kh) {
        for (KhachHangListener listener : listeners) {
            listener.onKhachHangAdded(kh);
        }
    }

    public ArrayList<KhachHang> getAll() {
        ArrayList<KhachHang> listKH = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listKH.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listKH;
    }

    // Kiểm tra số điện thoại đã tồn tại chưa
    public boolean isPhoneNumberExists(String sdt) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE SDT = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (TenKhachHang, SDT, GioiTinh, Email, NgaySinh) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSdt());
            ps.setBoolean(3, kh.isGioiTinh());
            ps.setString(4, kh.getEmail().isEmpty() ? null : kh.getEmail()); // Email không bắt buộc
            ps.setDate(5, kh.getNgaySinh() != null ? new java.sql.Date(kh.getNgaySinh().getTime()) : null); // Ngày sinh không bắt buộc
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                String generatedMaKH = getLastInsertedMaKhachHang();
                kh.setMaKhachHang(generatedMaKH);
                notifyListeners(kh);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKhachHang = ?, SDT = ?, Email = ?, GioiTinh = ?, NgaySinh = ? WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getEmail().isEmpty() ? null : kh.getEmail());
            ps.setBoolean(4, kh.isGioiTinh());
            ps.setDate(5, kh.getNgaySinh() != null ? new java.sql.Date(kh.getNgaySinh().getTime()) : null);
            ps.setInt(6, kh.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(int id) {
        String sql = "DELETE FROM KhachHang WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<KhachHang> timKiemKhachHang(String tuKhoa) {
        ArrayList<KhachHang> result = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE TenKhachHang LIKE ? OR SDT LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + tuKhoa.trim() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<KhachHang> layDanhSachKhachHang() {
        return getAll();
    }

    public ArrayList<KhachHang> getListKH() {
        return getAll();
    }

    public KhachHang getKHinfo(String phoneNum) {
        String sql = "SELECT * FROM KhachHang WHERE SDT = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phoneNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new KhachHang();
    }

    private String getLastInsertedMaKhachHang() {
        String sql = "SELECT TOP 1 MaKhachHang FROM KhachHang ORDER BY ID DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaKhachHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setId(rs.getInt("ID"));
        kh.setMaKhachHang(rs.getString("MaKhachHang"));
        kh.setTenKhachHang(rs.getString("TenKhachHang"));
        kh.setSdt(rs.getString("SDT"));
        kh.setEmail(rs.getString("Email"));
        kh.setGioiTinh(rs.getBoolean("GioiTinh"));
        kh.setNgaySinh(rs.getDate("NgaySinh"));
        return kh;
    }

    public boolean addKH(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (TenKhachHang, SDT, GioiTinh) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSdt());
            ps.setBoolean(3, kh.isGioiTinh());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                String generatedMaKH = getLastInsertedMaKhachHang();
                kh.setMaKhachHang(generatedMaKH);
                notifyListeners(kh);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getIdByMaKhachHang(String maKhachHang) {
        String sql = "SELECT ID FROM KhachHang WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}
