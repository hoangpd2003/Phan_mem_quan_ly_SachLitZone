/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Hoang
 */
public class SanPham {

    private int id;
    private String maSanPham;
    private String tenSanPham;
    private double giaBan;
    private String hinhAnh;
    private int soLuong;
    private String tenTheLoai;
    private String tenNgonNgu;
    private String tenNxb;
    private String tenTacGia;
    private boolean trangThai;

    public SanPham() {
    }

    public SanPham(int id, String maSanPham, String tenSanPham, double giaBan, String hinhAnh, int soLuong, String tenTheLoai, String tenNgonNgu, String tenNxb, String tenTacGia, boolean trangThai) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.tenTheLoai = tenTheLoai;
        this.tenNgonNgu = tenNgonNgu;
        this.tenNxb = tenNxb;
        this.tenTacGia = tenTacGia;
        this.trangThai = trangThai;
    }

    public SanPham(String maSanPham, String tenSanPham, double giaBan, String hinhAnh, int soLuong, String tenTheLoai, String tenNgonNgu, String tenNxb, String tenTacGia, boolean trangThai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.tenTheLoai = tenTheLoai;
        this.tenNgonNgu = tenNgonNgu;
        this.tenNxb = tenNxb;
        this.tenTacGia = tenTacGia;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public String getTenNgonNgu() {
        return tenNgonNgu;
    }

    public String getTenNxb() {
        return tenNxb;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public void setTenNgonNgu(String tenNgonNgu) {
        this.tenNgonNgu = tenNgonNgu;
    }

    public void setTenNxb(String tenNxb) {
        this.tenNxb = tenNxb;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

}
