/*
 * InventoryController.java
 * Versi FIX: Mapping 'Kategori' -> 'id_supplier'
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

public class InventoryController {
    
    // 1. SIMPAN BARANG BARU (Insert)
    public void simpanBarangBaru(String id, String nama, String kategori, int hargaBeli, int hargaJual, int stokAwal) {
        Connection con = DBaseConnection.connect();
        try {
            // Cek ID
            if (cariDetailProduct(id) != null) {
                JOptionPane.showMessageDialog(null, "ID Barang sudah ada! Gunakan ID lain.");
                return;
            }

            // PERUBAHAN DISINI:
            // Kita tidak pakai kolom 'kategori', tapi data kategori dimasukkan ke 'id_supplier'
            String sql = "INSERT INTO product (id_product, nama_product, harga_beli, harga_jual, stok, id_supplier) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setInt(3, hargaBeli);
            ps.setInt(4, hargaJual);
            ps.setInt(5, stokAwal);
            ps.setString(6, kategori); // Data Kategori masuk ke id_supplier
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Barang Baru Berhasil Disimpan!");
            
        } catch (Exception e) {
            System.out.println("Error Simpan: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal Simpan: " + e.getMessage());
        }
    }

    // 2. TAMPILKAN DATA
    public void tampilkanData(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Kategori (Supplier)"); // Judul kolom di tabel
        
        try {
            Statement st = con.createStatement();
            String sql = "SELECT * FROM product"; 
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),    
                    rs.getString("nama_product"), 
                    rs.getInt("harga_beli"),        
                    rs.getInt("harga_jual"),        
                    rs.getInt("stok"),
                    rs.getString("id_supplier") // Ini yang akan tampil sebagai Kategori
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Load Table: " + e.getMessage());
        }
    }

    // 3. CARI DETAIL (PENTING: Ambil id_supplier sebagai Kategori)
    public String[] cariDetailProduct(String idProduct) {
        Connection con = DBaseConnection.connect();
        String[] data = new String[5];
        
        try {
            String sql = "SELECT * FROM product WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                data[0] = rs.getString("nama_product");
                
                // PERUBAHAN DISINI:
                // Ambil dari 'id_supplier' untuk mengisi textfield Kategori
                data[1] = rs.getString("id_supplier"); 
                
                data[2] = rs.getString("harga_beli"); 
                data[3] = rs.getString("harga_jual");       
                data[4] = rs.getString("stok");       
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error Cari Detail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return data;
    }

    // 4. UPDATE STOK & HARGA (Tidak berubah)
    public void updateStokDanHarga(String idProduct, int tambahStok, int hargaBeliBaru, int hargaJualBaru) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE product SET stok = stok + ?, harga_beli = ?, harga_jual = ? WHERE id_product = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, tambahStok);
            ps.setInt(2, hargaBeliBaru);
            ps.setInt(3, hargaJualBaru);
            ps.setString(4, idProduct);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
            
        } catch (Exception e) {
            System.out.println("Error Update: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal Update: " + e.getMessage());
        }
    }
}