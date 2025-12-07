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
 * @author LENOVO
 */
public class BarangController {
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
                sql = "SELECT * FROM barang";
            } else {
                // Jika ada kata kunci, cari berdasarkan ID atau Nama
                sql = "SELECT * FROM barang WHERE id_barang LIKE '%" + cari + "%' OR nama_barang LIKE '%" + cari + "%'";
            }

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("stok"),
                    rs.getInt("harga")
                });
            }
            tabel.setModel(model);

        } catch (Exception e) {
            System.out.println("Error Tampil Data: " + e.getMessage());
        }
    }

    // 2. TAMBAH BARANG BARU
    public void tambahBarang(String id, String nama, int stok, int harga) {
        Connection con = DBaseConnection.connect();
        try {
            // Cek dulu apakah ID sudah ada?
            if (cekIdAda(id)) {
                JOptionPane.showMessageDialog(null, "ID Barang sudah ada! Gunakan ID lain.");
                return;
            }

            String sql = "INSERT INTO barang (id_barang, nama_barang, stok, harga) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setInt(3, stok);
            ps.setInt(4, harga);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Simpan: " + e.getMessage());
        }
    }

    // 3. EDIT BARANG
    public void editBarang(String id, String nama, int stok, int harga) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE barang SET nama_barang=?, stok=?, harga=? WHERE id_barang=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, stok);
            ps.setInt(3, harga);
            ps.setString(4, id); // Where id = ...
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit: " + e.getMessage());
        }
    }

    // 4. HAPUS BARANG
    public void hapusBarang(String id) {
        Connection con = DBaseConnection.connect();
        // Konfirmasi dulu sebelum hapus
        int tanya = JOptionPane.showConfirmDialog(null, "Yakin hapus barang ID: " + id + "?");
        
        if (tanya == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM barang WHERE id_barang=?";
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
            String sql = "SELECT * FROM barang WHERE id_barang = '" + id + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next(); // True jika data ditemukan
        } catch (Exception e) {
            return false;
        }
    }  
}
