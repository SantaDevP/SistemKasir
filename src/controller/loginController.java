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
            // Query mencocokkan username dan password
            String sql = "SELECT * FROM pegawai WHERE username='" + user + "' AND password='" + pass + "'";
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                // 1. JIKA LOGIN SUKSES
                // Ambil data dari database
                int id = rs.getInt("id_pegawai");
                String nama = rs.getString("nama_pegawai");
                
                // Simpan ke "Dompet" UserSession
                userSession.setUserLogin(id, nama);
                
                // Tampilkan pesan
                JOptionPane.showMessageDialog(frame, "Login Berhasil! Selamat bekerja, " + nama);
                
                // Buka Form Kasir (Menu Utama)
                new FormKasir().setVisible(true);
                
                // Tutup Form Login
                frame.dispose();
                
            } else {
                // 2. JIKA LOGIN GAGAL
                JOptionPane.showMessageDialog(frame, "Maaf, Username atau Password Salah!");
            }
            
        } catch (Exception e) {
            System.out.println("Error Login: " + e.getMessage());
        }
    }
}
