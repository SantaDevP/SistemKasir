/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
/**
 *
 * @author LENOVO
 */
public class StokMasukController {
    public void tambahStokBarang(String idBarang, int jumlahMasuk, String idSupplier) {
        Connection con = DBaseConnection.connect();
        try {
            // 1. UPDATE STOK DI TABEL BARANG (Bertambah)
            String sqlUpdate = "UPDATE barang SET stok = stok + ? WHERE id_barang = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, jumlahMasuk);
            psUpdate.setString(2, idBarang);
            psUpdate.executeUpdate();
            
            // 2. CATAT RIWAYAT (Opsional: Jika ada tabel 'stok_masuk')
            // Ini bagus agar Owner tau kapan barang masuk
            String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String sqlRiwayat = "INSERT INTO stok_masuk (tgl_masuk, id_barang, jumlah, id_supplier) VALUES (?, ?, ?, ?)";
            PreparedStatement psRiwayat = con.prepareStatement(sqlRiwayat);
            psRiwayat.setString(1, tanggal);
            psRiwayat.setString(2, idBarang);
            psRiwayat.setInt(3, jumlahMasuk);
            psRiwayat.setString(4, idSupplier);
            psRiwayat.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Stok Berhasil Ditambahkan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Tambah Stok: " + e.getMessage());
        }
    }
}
