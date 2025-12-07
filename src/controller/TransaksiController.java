/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransaksiController {
    
    // 1. FUNGSI MENCARI BARANG (Saat Kasir Scan/Ketik ID)
    // Mengembalikan array string: [Nama, Harga, Stok]
    public String[] cariBarang(String idBarang) {
        String[] data = new String[3]; // Nampung Nama, Harga, Stok
        Connection con = DBaseConnection.connect();
        
        try {
            String sql = "SELECT * FROM barang WHERE id_barang = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idBarang);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                data[0] = rs.getString("nama_barang");
                data[1] = rs.getString("harga");
                data[2] = rs.getString("stok");
            } else {
                return null; // Barang tidak ditemukan
            }
            
        } catch (Exception e) {
            System.out.println("Error Cari Barang: " + e.getMessage());
        }
        return data;
    }

    // 2. FUNGSI UTAMA: PROSES PEMBAYARAN
    public void simpanTransaksi(String idTrans, int totalBayar, int idPegawai, JTable tabelKeranjang) {
        Connection con = DBaseConnection.connect();
        
        try {
            // PENTING: Matikan auto-save agar bisa dibatalkan jika ada error di tengah jalan
            con.setAutoCommit(false); 
            
            // A. Simpan ke Tabel TRANSAKSI (Header Struk)
            String tanggalSekarang = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String sqlTrans = "INSERT INTO transaksi (id_transaksi, tgl_transaksi, total_harga, id_pegawai) VALUES (?, ?, ?, ?)";
            PreparedStatement psTrans = con.prepareStatement(sqlTrans);
            psTrans.setString(1, idTrans);
            psTrans.setString(2, tanggalSekarang);
            psTrans.setInt(3, totalBayar);
            psTrans.setInt(4, idPegawai);
            psTrans.executeUpdate();
            
            // B. Simpan Detail Barang & Kurangi Stok (Looping baris tabel)
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";
            String sqlUpdateStok = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";
            
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);
            PreparedStatement psStok = con.prepareStatement(sqlUpdateStok);
            
            DefaultTableModel model = (DefaultTableModel) tabelKeranjang.getModel();
            
            // Loop semua baris di keranjang belanja
            for (int i = 0; i < model.getRowCount(); i++) {
                String idBarang = model.getValueAt(i, 0).toString(); // Kolom 0: ID Barang
                int harga = Integer.parseInt(model.getValueAt(i, 2).toString()); // Kolom 2: Harga
                int qty = Integer.parseInt(model.getValueAt(i, 3).toString());   // Kolom 3: Jumlah
                int subtotal = harga * qty;
                
                // 1. Masukkan ke detail_transaksi
                psDetail.setString(1, idTrans);
                psDetail.setString(2, idBarang);
                psDetail.setInt(3, qty);
                psDetail.setInt(4, subtotal);
                psDetail.executeUpdate();
                
                // 2. Kurangi Stok di tabel barang
                psStok.setInt(1, qty);
                psStok.setString(2, idBarang);
                psStok.executeUpdate();
            }
            
            // Jika semua lancar, SIMPAN PERMANEN
            con.commit();
            con.setAutoCommit(true); // Kembalikan ke normal
            JOptionPane.showMessageDialog(null, "Transaksi Berhasil Disimpan!");
            
            // Kosongkan tabel keranjang setelah sukses
            model.setRowCount(0);
            
        } catch (Exception e) {
            try {
                con.rollback(); // BATALKAN SEMUA jika ada error
                System.out.println("Transaksi Dibatalkan: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Gagal Transaksi! Stok dikembalikan.");
            } catch (SQLException ex) {
                System.out.println("Rollback gagal");
            }
        }
    }
    
}