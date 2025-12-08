/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.product; // Import model

/**
 *
 * @author Santa
 */
public class ProductController {

    // 1. CARI BARANG (UNTUK KASIR)
    public product cariBarang(int id) {
        product p = null;
        try {
            Connection con = DBaseConnection.connect();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM product WHERE id_product=" + id;
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                p = new product();
                p.setId_product(rs.getInt("id_product"));
                p.setNama_product(rs.getString("nama_product"));
                p.setHarga_jual(rs.getDouble("harga_jual"));
                p.setStok(rs.getInt("stok"));
            }
        } catch (Exception e) {
            System.out.println("Error Cari Barang: " + e.getMessage());
        }
        return p;
    }

    // 2. TAMPILKAN DATA (UNTUK FORM ADMIN GUDANG)
    public void tampilkanData(JTable tabel, String cari) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        // Sesuaikan nama kolom dengan desain tabelmu
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Stok");
        model.addColumn("Harga");

        try {
            String sql;
            // Jika kolom pencarian kosong, ambil semua data
            if (cari == null || cari.equals("")) {
                sql = "SELECT * FROM product";
            } else {
                // Jika ada kata kunci, cari berdasarkan ID atau Nama
                sql = "SELECT * FROM product WHERE id_product LIKE '%" + cari + "%' OR nama_product LIKE '%" + cari + "%'";
            }

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("stok"),
                    rs.getDouble("harga_jual") // Ambil sebagai Double (karena uang)
                });
            }
            tabel.setModel(model);

        } catch (Exception e) {
            System.out.println("Error Tampil Data: " + e.getMessage());
        }
    }

    // 3. TAMBAH BARANG BARU
    public void tambahBarang(String id, String nama, int stok, double harga, int idSupplier) {
        Connection con = DBaseConnection.connect();
        try {
            // Cek dulu apakah ID sudah ada?
            if (cekIdAda(id)) {
                JOptionPane.showMessageDialog(null, "ID Barang sudah ada! Gunakan ID lain.");
                return;
            }

            String sql = "INSERT INTO product (id_product, nama_product, stok, harga_jual, harga_beli, id_supplier) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setInt(3, stok);
            ps.setDouble(4, harga);
            ps.setDouble(5, harga * 0.8); // Otomatis set harga beli 80% dari jual (Contoh)
            ps.setInt(6, idSupplier); // Default supplier 1 jika null
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Simpan: " + e.getMessage());
        }
    }

    // 4. EDIT BARANG
    public void editBarang(String id, String nama, int stok, double harga) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE product SET nama_product=?, stok=?, harga_jual=? WHERE id_product=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, stok);
            ps.setDouble(3, harga);
            ps.setString(4, id); // Where id = ...
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit: " + e.getMessage());
        }
    }

    // 5. HAPUS BARANG
    public void hapusBarang(String id) {
        Connection con = DBaseConnection.connect();
        // Konfirmasi dulu sebelum hapus
        int tanya = JOptionPane.showConfirmDialog(null, "Yakin hapus barang ID: " + id + "?");
        
        if (tanya == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM product WHERE id_product=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, id);
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal Hapus: " + e.getMessage());
            }
        }
    }
    
    // --- Method Bantuan: Cek ID Kembar ---
    private boolean cekIdAda(String id) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "SELECT * FROM product WHERE id_product = '" + id + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next(); // True jika data ditemukan
        } catch (Exception e) {
            return false;
        }
    }   
}