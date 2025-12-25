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

/**
 *
 * @author Santa
 */
public class ProductController {

    // 1. Method ID Otomatis (Sudah Benar)
    public String getIdOtomatis() {
        return new TransaksiController().getAutoID("product", "id_product", "BRG-");
    }

    // 2. [PERBAIKAN] Method Cari Barang
    public model.product cariBarang(String id) {
        model.product p = null;
        // Pakai ? biar aman untuk String
        String sql = "SELECT * FROM product WHERE id_product = ?"; 
        
        try {
            java.sql.Connection con = connection.DBaseConnection.connect();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id); // Set String
            
            java.sql.ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                p = new model.product();
                p.setId_product(rs.getString("id_product"));
                p.setNama_product(rs.getString("nama_product"));
                p.setHarga_jual(rs.getDouble("harga_jual"));
                p.setStok(rs.getInt("stok"));
                // Tambahan: Ambil ID Supplier juga (biar lengkap)
                p.setId_supplier(rs.getString("id_supplier")); 
            }
        } catch (Exception e) {
            System.out.println("Error Cari Barang: " + e.getMessage());
        }
        return p;
    }
        
    public void tampilkanData(JTable tabel, String cari) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
   
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Stok");
        model.addColumn("Harga");

        try {
            String sql;
      
            if (cari == null || cari.equals("")) {
                sql = "SELECT * FROM product";
            } else {
                sql = "SELECT * FROM product WHERE id_product LIKE '%" + cari + "%' OR nama_product LIKE '%" + cari + "%'";
            }

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("stok"),
                    rs.getDouble("harga_jual")
                });
            }
            tabel.setModel(model);

        } catch (Exception e) {
            System.out.println("Error Tampil Data: " + e.getMessage());
        }
    }

    // 3. [PERBAIKAN] Parameter idSupplier jadi String!
    public void tambahBarang(String id, String nama, int stok, double harga, String idSupplier) {
        Connection con = DBaseConnection.connect();
        try {
      
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
            ps.setDouble(5, harga * 0.8);  // Harga beli otomatis 80%
            
            // [PERBAIKAN] Ubah setInt jadi setString
            ps.setString(6, idSupplier);  
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Simpan: " + e.getMessage());
        }
    }

    
    public void editBarang(String id, String nama, int stok, double harga) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE product SET nama_product=?, stok=?, harga_jual=? WHERE id_product=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, stok);
            ps.setDouble(3, harga);
            ps.setString(4, id);  
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit: " + e.getMessage());
        }
    }

     
    public void hapusBarang(String id) {
        Connection con = DBaseConnection.connect();
         
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
    
     
    private boolean cekIdAda(String id) {
        Connection con = DBaseConnection.connect();
        try {
            // Ini sudah benar pakai kutip satu manual ('id')
            String sql = "SELECT * FROM product WHERE id_product = '" + id + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next();  
        } catch (Exception e) {
            return false;
        }
    }   
}