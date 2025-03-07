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
import lombok.ToString;

/**
 *
 * @author thanhnguyen
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhieuGiamGia {

    private int id;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private int soLuong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double soTienGiam;
    private boolean trangThai;
    
    
}
