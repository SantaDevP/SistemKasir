/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
/**
 *
 * @author LENOVO
 */
public class PelangganController {
    public String cariPelanggan(String idAtauNama) {
        Connection con = DBaseConnection.connect();
        String namaPelanggan = "Umum"; // Default jika tidak ketemu
        
        try {
            String sql = "SELECT nama_pelanggan FROM pelanggan WHERE id_pelanggan = ? OR nama_pelanggan LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idAtauNama);
            ps.setString(2, "%" + idAtauNama + "%");
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                namaPelanggan = rs.getString("nama_pelanggan");
            }
        } catch (Exception e) {
            System.out.println("Error Cari Pelanggan: " + e.getMessage());
        }
        return namaPelanggan;
    }
    
    // Tambah Pelanggan Baru (Jika ada form member)
    public void tambahPelanggan(String nama, String noHp, String alamat) {
        // ... Logika Insert ke tabel pelanggan ...
    }
}
