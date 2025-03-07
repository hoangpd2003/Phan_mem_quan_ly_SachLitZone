package Utils;

import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.regex.Pattern;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author khoad
 */
public class Validate {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern STRING_PATTERN = Pattern.compile("^[\\p{L} ]+$");

    public boolean isEmpty(JTextField txt, StringBuilder stb, String msg) {
        if (txt.getText().trim().isEmpty()) {
            txt.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isEmpty(JTextArea txt, StringBuilder stb, String msg) {
        if (txt.getText().trim().isEmpty()) {
            txt.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isNumber(JTextField txt, StringBuilder stb, String msg, int type) {
        if (!isEmpty(txt, stb, msg)) {
            return false;
        } else {
            String input = txt.getText().trim();
            if (type == 0 && !INTEGER_PATTERN.matcher(input).matches()) {
                txt.setBackground(Color.YELLOW);
                stb.append(msg).append("\n");
                return false;
            } else if (type == 1 && !DOUBLE_PATTERN.matcher(input).matches()) {
                txt.setBackground(Color.YELLOW);
                stb.append(msg).append("\n");
                return false;
            }
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean numberLimit(JTextField txt, StringBuilder stb, String msg, int type, int min) {
        if (!isNumber(txt, stb, msg, type)) {
            return false;
        } else {
            double number = Double.parseDouble(txt.getText().trim());
            if (number < min) {
                txt.setBackground(Color.YELLOW);
                stb.append(msg).append("\n");
                return false;
            }
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isString(JTextField txt, StringBuilder stb, String msg) {
        if (!isEmpty(txt, stb, msg)) {
            return false;
        }
        String input = txt.getText().trim();
        if (INTEGER_PATTERN.matcher(input).matches() || DOUBLE_PATTERN.matcher(input).matches() || !STRING_PATTERN.matcher(input).matches()) {
            txt.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isPhone(JTextField txt, StringBuilder stb, String msg) {
        if (!isEmpty(txt, stb, msg)) {
            return false;
        }
        String input = txt.getText().trim();
        if (!Pattern.matches("^0\\d{9}$", input)) {
            txt.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isEmptyUS(JTextField txt, StringBuilder stb, String msg) {
        if (txt.getText().trim().isBlank()) {
            txt.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        txt.setBackground(Color.WHITE);
        return true;
    }

    public boolean isEmptyPW(JPasswordField pw, StringBuilder stb, String msg) {
        if (pw.getText().trim().isBlank()) {
            pw.setBackground(Color.YELLOW);
            stb.append(msg).append("\n");
            return false;
        }
        pw.setBackground(Color.WHITE);
        return true;
    }
    
   

}
