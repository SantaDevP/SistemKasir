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
public class TransaksiController {
    public void kurangiStok(String idBarang, int jumlahBeli) {
        Connection con = DBaseConnection.connect();
        try {
            // Logika: Stok Baru = Stok Lama - Jumlah Beli
            String sql = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, jumlahBeli);
            ps.setString(2, idBarang);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Gagal Kurangi Stok: " + e.getMessage());
        }
    }

    // 2. Method untuk Simpan Transaksi Utama
    public void simpanTransaksi(String idTrans, String tanggal, int total, int idPegawai) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "INSERT INTO transaksi (id_transaksi, tgl_transaksi, total_harga, id_pegawai) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idTrans);
            ps.setString(2, tanggal);
            ps.setInt(3, total);
            ps.setInt(4, idPegawai);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Simpan Transaksi");
        }
    }
}
