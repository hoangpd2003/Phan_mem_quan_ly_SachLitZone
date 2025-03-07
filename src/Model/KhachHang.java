/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Hoang
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KhachHang {

    private int id;
    private String maKhachHang;
    private String tenKhachHang;
    private String sdt;
    private String email;
    private boolean gioiTinh;
    private Date ngaySinh;
}
