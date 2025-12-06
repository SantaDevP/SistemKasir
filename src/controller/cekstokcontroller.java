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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LENOVO
 */
public class cekstokcontroller {
    public void tampilkanData(JTable tabelBarang) {
        Connection con = DBaseConnection.connect();
        
        // 1. Buat Model Tabel (Judul Kolom)
        // Sesuaikan judul kolom ini dengan tampilan yang kamu mau
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Stok");
        model.addColumn("Harga Satuan");
        
        try {
            Statement st = con.createStatement();
            
            // 2. Query Ambil Data (SESUAIKAN NAMA TABEL DI DATABASE, misal: 'barang')
            String sql = "SELECT * FROM barang"; 
            ResultSet rs = st.executeQuery(sql);
            
            // 3. Looping data dari database untuk dimasukkan ke model
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("id_barang"),   // Sesuaikan nama kolom di database
                    rs.getString("nama_barang"), // Sesuaikan nama kolom di database
                    rs.getInt("stok"),           // Sesuaikan nama kolom di database
                    rs.getInt("harga")           // Sesuaikan nama kolom di database
                });
            }
            
            // 4. Pasang model yang sudah berisi data ke JTable di Form
            tabelBarang.setModel(model);
            
        } catch (Exception e) {
            System.out.println("Error Tampil Data: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal mengambil data stok!");
        }
    }
    
    // Method Tambahan: Untuk Mencari Barang (Fitur Search)
    public void cariBarang(JTable tabelBarang, String kataKunci) {
        Connection con = DBaseConnection.connect();
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Stok");
        model.addColumn("Harga Satuan");
        
        try {
            Statement st = con.createStatement();
            
            // Query Search menggunakan LIKE
            String sql = "SELECT * FROM barang WHERE nama_barang LIKE '%" + kataKunci + "%'"; 
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("stok"),
                    rs.getInt("harga")
                });
            }
            
            tabelBarang.setModel(model);
            
        } catch (Exception e) {
            System.out.println("Error Cari Data: " + e.getMessage());
        }
    }
}
