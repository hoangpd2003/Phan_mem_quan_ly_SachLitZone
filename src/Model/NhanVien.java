/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

//import java.util.Date;
//import java.util.Vector;
/**
 *
 * @author Hoang
 */
public class NhanVien {

    private int idNhanVien;
    private int idChucVu;
    private String maChucVu;
    private String tenNhanVien;
    private String tenChucVu;
    private String ngaySinh;
    private String soDienThoai;
    private String Email;
    private String queQuan;
    private boolean gioiTinh;
    private String matKhau;
    private String maNhanVien;
    private boolean trangThai;

    public NhanVien(String maNhanVien, int idChucVu, String tenNhanVien, String ngaySinh, String soDienThoai, boolean gioiTinh, boolean trangThai) {
        this.maNhanVien = maNhanVien;
        this.idChucVu = idChucVu;
        this.tenNhanVien = tenNhanVien;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;

        this.trangThai = trangThai;
    }

    public int getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(int idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public int getIdChucVu() {
        return idChucVu;
    }

    public void setIdChucVu(int idChucVu) {
        this.idChucVu = idChucVu;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien() {
    }

    public NhanVien(int idNhanVien, int idChucVu, String maChucVu, String tenNhanVien, String ngaySinh, String soDienThoai, String Email, String queQuan, boolean gioiTinh, String matKhau, String maNhanVien, boolean trangThai) {
        this.idNhanVien = idNhanVien;
        this.idChucVu = idChucVu;
        this.maChucVu = maChucVu;
        this.tenNhanVien = tenNhanVien;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.Email = Email;
        this.queQuan = queQuan;
        this.gioiTinh = gioiTinh;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.trangThai = trangThai;
    }

    public NhanVien(String maNhanVien, String matKhau, int idChucVu, String tenNhanVien, String ngaySinh, String soDienThoai, boolean gioiTinh, boolean trangThai) {
        this.maNhanVien = maNhanVien;
        this.matKhau = matKhau;
        this.idChucVu = idChucVu;
        this.tenNhanVien = tenNhanVien;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
    }

    public Object[] toDataRow() {
        return new Object[]{
            this.getIdNhanVien(),
            this.getMaNhanVien(),
            this.getMatKhau(),
            this.getTenNhanVien(),
            this.isGioiTinh() ? "Nam" : "Nữ",
            this.getNgaySinh(),
            this.getSoDienThoai(),
            this.getTenChucVu(), // Sử dụng getTenChucVu()
            this.isTrangThai() ? "Đang làm" : "Đã nghỉ"
        };
    }

}
