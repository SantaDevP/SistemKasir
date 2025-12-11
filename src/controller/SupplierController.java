/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LENOVO
 */
public class SupplierController {
    public void tampilSupplier(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Supplier");
        model.addColumn("Nama Supplier");
        model.addColumn("No Telp");
        model.addColumn("Alamat");
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM supplier");
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_supplier"),
                    rs.getString("nama_supplier"),
                    rs.getString("no_telp"),
                    rs.getString("alamat")
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Error Supplier: " + e.getMessage());
        }
    }

    
    public void tambahSupplier(String nama, String telp, String alamat) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "INSERT INTO supplier (nama_supplier, no_telp, alamat) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, telp);
            ps.setString(3, alamat);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Supplier Berhasil Ditambah");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Tambah: " + e.getMessage());
        }
    }
}