/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Hoang
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HoaDon {

    private int id;
    private String maHoaDon;
    private String maNhanVien;
    private String maKhachHang;
    private String tenNhanVien;
    private String tenKhachHang;
    private String tenPhieuGiamGia;
    private LocalDate ngayTao;
    private boolean trangThai;
    private double tongTien;
    private double soTienGiam;
    private double thanhTien;
    private String sdt;
    private String maPhieuGiamGia;

    public boolean getTrangThai() {
        return this.trangThai;
    }

}
