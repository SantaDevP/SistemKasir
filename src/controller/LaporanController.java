/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LENOVO
 */
public class LaporanController {
    public void filterLaporan(JTable tabel, JLabel lblOmzet, String tglMulai, String tglSampai) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        // Judul Kolom Laporan
        model.addColumn("ID Transaksi");
        model.addColumn("Tanggal");
        model.addColumn("Nama Kasir"); // Kita akan JOIN dengan tabel pegawai
        model.addColumn("Total Belanja");

        try {
            // Query JOIN: Mengambil nama pegawai dari tabel pegawai berdasarkan ID di tabel transaksi
            String sql = "SELECT t.id_transaksi, t.tgl_transaksi, p.nama_pegawai, t.total_harga " +
                         "FROM transaksi t " +
                         "JOIN pegawai p ON t.id_pegawai = p.id_pegawai " +
                         "WHERE t.tgl_transaksi BETWEEN ? AND ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tglMulai); // Format: yyyy-MM-dd
            ps.setString(2, tglSampai);
            
            ResultSet rs = ps.executeQuery();
            
            int totalOmzet = 0;

            while (rs.next()) {
                // Masukkan baris ke tabel
                model.addRow(new Object[]{
                    rs.getString("id_transaksi"),
                    rs.getString("tgl_transaksi"),
                    rs.getString("nama_pegawai"),
                    rs.getInt("total_harga")
                });
                
                // Sekalian hitung omzet biar hemat query
                totalOmzet += rs.getInt("total_harga");
            }
            
            tabel.setModel(model);
            lblOmzet.setText("Rp " + totalOmzet); // Tampilkan total pendapatan

        } catch (Exception e) {
            System.out.println("Error Laporan: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal memuat laporan!");
        }
    }
    
    // 2. CETAK LAPORAN (Opsional - Jika ingin fitur print sederhana)
    public void cetakLaporan(JTable tabel) {
        try {
            // Fitur bawaan Java Swing untuk print tabel
            tabel.print(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Mencetak");
        }
    }
}
