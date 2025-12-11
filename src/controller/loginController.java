/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import model.userSession;
import view.FormLogin;
import view.FormKasir;
/**
 *
 * @author Santa
 */


public class loginController {
    public void cekLogin(String user, String pass, FormLogin frame) {
        Connection con = DBaseConnection.connect();
        
        try {
            Statement st = con.createStatement();
             
            String sql = "SELECT * FROM pegawai WHERE username='" + user + "' AND password='" + pass + "'";
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                 
                int id = rs.getInt("id_pegawai");
                String nama = rs.getString("nama_pegawai");
                userSession.setUserLogin(id, nama);
                
                JOptionPane.showMessageDialog(frame, "Login Berhasil! Selamat bekerja, " + nama);
                
                new FormKasir().setVisible(true);
            
                frame.dispose();
                
            } else {
                 
                JOptionPane.showMessageDialog(frame, "Maaf, Username atau Password Salah!");
            }
            
        } catch (Exception e) {
            System.out.println("Error Login: " + e.getMessage());
        }
    }
}
