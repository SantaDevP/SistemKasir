/*
 * InventoryController.java
 * Controller untuk Form Inventory (Tabel 'product')
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

    // 1. TAMPILKAN TABEL PRODUCT
    public void tampilkanData(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        // Header Tabel di GUI
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("ID Supplier");
        
        try {
            Statement st = con.createStatement();
            // GANTI 'barang' JADI 'product'
            String sql = "SELECT * FROM product"; 
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),    // Ganti id_barang jadi id_product
                    rs.getString("nama_product"),  // Pastikan di DB namanya nama_product
                    rs.getInt("harga_beli"),       
                    rs.getInt("harga_jual"),       
                    rs.getInt("stok"),
                    rs.getString("id_supplier") 
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Load Table: " + e.getMessage());
        }
    }

    // 2. CARI DETAIL PRODUCT (Untuk mengisi form saat tombol Cari diklik)
    // Return Array: [0]Nama, [1]Kategori, [2]HargaBeli, [3]HargaJual, [4]StokLama
    public String[] cariDetailProduct(String idProduct) {
        Connection con = DBaseConnection.connect();
        String[] data = new String[5];
        
        try {
            // Query ke tabel product
            String sql = "SELECT * FROM product WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                data[0] = rs.getString("nama_product");
                data[1] = rs.getString("kategori");   
                data[2] = rs.getString("harga_beli"); 
                data[3] = rs.getString("harga_jual");      
                data[4] = rs.getString("stok");       // INI STOK LAMA
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error Cari Detail: " + e.getMessage());
            return null;
        }
        return data;
    }

    // 3. UPDATE STOK PRODUCT
    public void updateStokProduct(String idProduct, int tambahanStok) {
        Connection con = DBaseConnection.connect();
        try {
            // Update tabel product
            String sql = "UPDATE product SET stok = stok + ? WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, tambahanStok);
            ps.setString(2, idProduct);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Stok Product Berhasil Ditambahkan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Update Stok: " + e.getMessage());
        }
    }
}