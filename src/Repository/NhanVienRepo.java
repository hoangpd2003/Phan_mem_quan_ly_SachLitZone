/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.NhanVien;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Hoang
 */
public class NhanVienRepo {

    public boolean dangNhap(String maNhanVien, String matKhau) {
        String sql = "SELECT * FROM NhanVien WHERE MaNhanVien = ? AND MatKhau = ? AND TrangThai = 1";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> listNV = new ArrayList<>();
        // SQL JOIN để lấy TenChucVu
        String sql = """
                 SELECT nv.[ID], nv.[MaNhanVien], nv.[MatKhau], nv.[TenNhanVien], nv.[GioiTinh],
                        nv.[NgaySinh], nv.[SDT], nv.[IdChucVu], nv.[TrangThai], cv.TenChucVu
                 FROM [dbo].[NhanVien] nv
                 INNER JOIN ChucVu cv ON nv.IdChucVu = cv.ID
                     where TrangThai =1
                 """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt("ID"));
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setTenNhanVien(rs.getString("TenNhanVien"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setNgaySinh(rs.getString("NgaySinh"));
                nv.setSoDienThoai(rs.getString("SDT"));
                nv.setIdChucVu(rs.getInt("IdChucVu"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setTenChucVu(rs.getString("TenChucVu")); // **Lấy TenChucVu từ ResultSet**

                listNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNV;
    }

//    public int deleteNV(String id) {
//        String sql = "DELETE FROM [dbo].[NhanVien] WHERE ID = ?";
//        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setString(1, id);
//            return ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    public int addNV(NhanVien nv) {
        String sql = "INSERT INTO [dbo].[NhanVien] ([MaNhanVien], [MatKhau], [TenNhanVien], [GioiTinh], [NgaySinh], [SDT], [IDChucVu], [TrangThai]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getMatKhau());
            ps.setString(3, nv.getTenNhanVien());
            ps.setBoolean(4, nv.isGioiTinh());
            ps.setString(5, nv.getNgaySinh());
            ps.setString(6, nv.getSoDienThoai());
            ps.setInt(7, nv.getIdChucVu());
            ps.setBoolean(8, nv.isTrangThai());

            return ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Mã lỗi SQL Server cho vi phạm ràng buộc UNIQUE
                throw new RuntimeException("Mã nhân viên đã tồn tại trong hệ thống!");
            }
            e.printStackTrace();
            return 0;
        }
    }

    public int updateNV(NhanVien nv) {
        String sql = "UPDATE NhanVien SET MaNhanVien = ?, MatKhau = ?, TenNhanVien = ?, SDT = ?, GioiTinh = ?, NgaySinh = ?, IDChucVu = ?, TrangThai = ? WHERE ID = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getMatKhau());
            ps.setString(3, nv.getTenNhanVien());
            ps.setString(4, nv.getSoDienThoai());
            ps.setBoolean(5, nv.isGioiTinh());
            ps.setString(6, nv.getNgaySinh());
            ps.setInt(7, nv.getIdChucVu());
            ps.setBoolean(8, nv.isTrangThai());
            ps.setInt(9, nv.getIdNhanVien());

            return ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Mã lỗi SQL Server cho vi phạm ràng buộc UNIQUE
                throw new RuntimeException("Mã nhân viên đã tồn tại trong hệ thống!");
            }
            e.printStackTrace();
            return 0;
        }
    }

    public NhanVien getNVLogin(String username, String pass) {
        String sql = """
                     SELECT nv.ID, nv.MaNhanVien, cv.MaChucVu, nv.TenNhanVien, nv.NgaySinh, nv.SDT, nv.GioiTinh, nv.TrangThai FROM NhanVien nv
                     JOIN ChucVu cv ON cv.id = nv.IDChucVu
                     WHERE MaNhanVien = ? AND MatKhau = ?
                     """;
        NhanVien nv = new NhanVien();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt(1));
                nv.setMaNhanVien(rs.getString(2));
                nv.setMaChucVu(rs.getString(3));
                nv.setTenNhanVien(rs.getString(4));
                nv.setNgaySinh(rs.getString(5));
                nv.setSoDienThoai(rs.getString(6));
                nv.setGioiTinh(rs.getBoolean(7));
                nv.setTrangThai(rs.getBoolean(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nv;
    }

    public NhanVien getNhanVienByMa(String maNhanVien) {
        String sql = "SELECT ID, MaNhanVien, MatKhau, TenNhanVien, GioiTinh, NgaySinh, SDT, IDChucVu, TrangThai FROM NhanVien WHERE MaNhanVien = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt("ID"));
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setTenNhanVien(rs.getString("TenNhanVien"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setNgaySinh(rs.getString("NgaySinh"));
                nv.setSoDienThoai(rs.getString("SDT"));
                nv.setIdChucVu(rs.getInt("IDChucVu"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql = null;

//    public ArrayList<Model.NhanVien> search(String search) {
//        ArrayList<Model.NhanVien> list = new ArrayList<>();
//        String sql = """
//                 SELECT ID, MaNhanVien, MatKhau, TenNhanVien, GioiTinh, NgaySinh, SDT, IDChucVu, TrangThai 
//                 FROM NhanVien
//                 WHERE MaNhanVien LIKE ? 
//                    OR TenNhanVien LIKE ? 
//                    OR SDT LIKE ?
//                 """;
//        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, search + "%");  // Tìm kiếm theo Mã Nhân Viên (bắt đầu bằng)
//            ps.setString(2, "%" + search + "%");  // Tìm kiếm theo Tên Nhân Viên (chứa)
//            ps.setString(3, "%" + search + "%");  // Tìm kiếm theo Số Điện Thoại (chứa)
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Model.NhanVien nv = new Model.NhanVien();
//                    nv.setIdNhanVien(rs.getInt("ID"));
//                    nv.setMaNhanVien(rs.getString("MaNhanVien"));
//                    nv.setMatKhau(rs.getString("MatKhau"));
//                    nv.setTenNhanVien(rs.getString("TenNhanVien"));
//                    nv.setGioiTinh(rs.getBoolean("GioiTinh"));
//                    nv.setNgaySinh(rs.getDate("NgaySinh").toString());  // Chuyển Date thành String
//                    nv.setSoDienThoai(rs.getString("SDT"));
//                    nv.setIdChucVu(rs.getInt("IDChucVu"));
//                    nv.setTrangThai(rs.getBoolean("TrangThai"));
//                    list.add(nv);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();  // In lỗi chi tiết
//        }
//        return list;
//    }
    public ArrayList<Model.NhanVien> search(String search) {
        ArrayList<Model.NhanVien> list = new ArrayList<>();
        // SQL JOIN để lấy TenChucVu trong search query
        String sql = """
                     SELECT nv.[ID], nv.[MaNhanVien], nv.[MatKhau], nv.[TenNhanVien], nv.[GioiTinh],
                            nv.[NgaySinh], nv.[SDT], nv.[IdChucVu], nv.[TrangThai], cv.TenChucVu
                     FROM NhanVien nv
                     INNER JOIN ChucVu cv ON nv.IdChucVu = cv.ID
                     WHERE nv.MaNhanVien LIKE ?
                        OR nv.TenNhanVien LIKE ?
                        OR nv.SDT LIKE ?
                     """;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, search + "%");  // Tìm kiếm theo Mã Nhân Viên (bắt đầu bằng)
            ps.setString(2, "%" + search + "%");  // Tìm kiếm theo Tên Nhân Viên (chứa)
            ps.setString(3, "%" + search + "%");  // Tìm kiếm theo Số Điện Thoại (chứa)

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Model.NhanVien nv = new Model.NhanVien();
                    nv.setIdNhanVien(rs.getInt("ID"));
                    nv.setMaNhanVien(rs.getString("MaNhanVien"));
                    nv.setMatKhau(rs.getString("MatKhau"));
                    nv.setTenNhanVien(rs.getString("TenNhanVien"));
                    nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                    nv.setNgaySinh(rs.getString("NgaySinh"));
                    nv.setSoDienThoai(rs.getString("SDT"));
                    nv.setIdChucVu(rs.getInt("IdChucVu"));
                    nv.setTrangThai(rs.getBoolean("TrangThai"));
                    nv.setTenChucVu(rs.getString("TenChucVu")); // **Lấy TenChucVu từ ResultSet**
                    list.add(nv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi chi tiết
        }
        return list;
    }

    public boolean updateTrangThaiNhanVien(String idNhanVien) {
        String sql = "UPDATE NhanVien SET TrangThai = 0 WHERE ID = ?"; // 

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idNhanVien)); // 

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<NhanVien> getActiveNhanVien() {
        ArrayList<NhanVien> listNV = new ArrayList<>();
        String sql = "SELECT [ID], [MaNhanVien], [MatKhau], [TenNhanVien], [GioiTinh], [NgaySinh], [SDT], [IdChucVu], [TrangThai] "
                + "FROM [dbo].[NhanVien] WHERE TrangThai = 1";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt("ID"));
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setTenNhanVien(rs.getString("TenNhanVien"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setNgaySinh(rs.getString("NgaySinh"));
                nv.setSoDienThoai(rs.getString("SDT"));
                nv.setIdChucVu(rs.getInt("IdChucVu"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));

                listNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNV;
    }

    // Check trùng SDT nhân viên
    public boolean isPhoneNumberExists(String soDienThoai, int excludeId) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE SDT = ? AND ID != ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);
            ps.setInt(2, excludeId); // Loại trừ ID của nhân viên hiện tại khi cập nhật
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu số điện thoại đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra mã nhân viên đã tồn tại
    public boolean isMaNhanVienExists(String maNhanVien, int excludeId) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaNhanVien = ? AND ID != ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ps.setInt(2, excludeId); // Loại trừ ID của nhân viên hiện tại khi sửa
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
