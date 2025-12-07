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
public class PegawaiController {
    public void tampilPegawai(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("ID");
        model.addColumn("Nama Lengkap");
        model.addColumn("Username");
        model.addColumn("Password"); // Hati-hati menampilkan password di aplikasi asli
        
        try {
            Statement st = con.createStatement();
            String sql = "SELECT * FROM pegawai";
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_pegawai"),
                    rs.getString("nama_pegawai"),
                    rs.getString("username"),
                    rs.getString("password")
                });
            }
            tabel.setModel(model);
            
        } catch (Exception e) {
            System.out.println("Error Tampil Pegawai: " + e.getMessage());
        }
    }
    
    // 2. TAMBAH PEGAWAI BARU
    public void tambahPegawai(String nama, String user, String pass) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "INSERT INTO pegawai (nama_pegawai, username, password) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, user);
            ps.setString(3, pass);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pegawai Baru Berhasil Didaftarkan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Tambah: " + e.getMessage());
        }
    }
    
    // 3. EDIT DATA PEGAWAI
    public void editPegawai(int id, String nama, String user, String pass) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE pegawai SET nama_pegawai=?, username=?, password=? WHERE id_pegawai=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, user);
            ps.setString(3, pass);
            ps.setInt(4, id);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Pegawai Diupdate!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit: " + e.getMessage());
        }
    }
    
    // 4. HAPUS PEGAWAI
    public void hapusPegawai(int id) {
        Connection con = DBaseConnection.connect();
        int tanya = JOptionPane.showConfirmDialog(null, "Hapus pegawai ID: " + id + "?");
        
        if (tanya == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM pegawai WHERE id_pegawai=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Pegawai Dihapus!");
            } catch (Exception e) {
                // Biasanya error jika pegawai ini sudah pernah melakukan transaksi (Foreign Key)
                JOptionPane.showMessageDialog(null, "Gagal! Pegawai ini memiliki riwayat transaksi.");
            }
        }
    }
}
