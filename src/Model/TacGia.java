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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 *
 * @author Admin
 */
public class TacGia {
    private int ID;
    private String maTacGia;    // Lưu 'NXB001', 'NXB002'...
    private String tenTacGia;
    private boolean trangThai;
    @Override
    public String toString() {
        return this.tenTacGia;
    }
}
