/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
/**
 *
 * @author Admin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NXB {

    private int id;
    private String maNXB;    // Lưu 'NXB001', 'NXB002'...
    private String tenNXB;
    private boolean trangThai;

    @Override
    public String toString() {
        return this.tenNXB;
    }
}
