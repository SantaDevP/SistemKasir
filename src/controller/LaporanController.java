/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import connection.DBaseConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
/**
 *
 * @author LENOVO
 */
public class LaporanController {
    public void tampilkanLaporan(JDateChooser tglMulai, JDateChooser tglAkhir, JTable tabel, JTextField txtOmset, JTextField txtKeuntungan) {
            java.sql.Connection con = connection.DBaseConnection.connect();
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabel.getModel();

           
            model.setRowCount(0);

            double totalOmset = 0;
            double totalModal = 0;

            try {
                
                String sqlBase = "SELECT t.id_transaksi, c.nama_customer, " 
                               + "p.nama_product, p.id_product, "
                               + "dt.quantity, "                        
                               + "p.harga_beli, "
                               + "p.harga_jual, "
                               + "dt.subtotal, "                        
                               + "(dt.quantity * p.harga_beli) as modal_item "
                               + "FROM detail_transaksi dt "
                               + "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                               + "JOIN product p ON dt.id_product = p.id_product "
                               + "JOIN customer c ON t.id_customer = c.id_customer "; // JOIN KE CUSTOMER

                String sqlWhere = "";

                
                if (tglMulai.getDate() != null && tglAkhir.getDate() != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    String mulai = sdf.format(tglMulai.getDate()) + " 00:00:00";
                    String akhir = sdf.format(tglAkhir.getDate()) + " 23:59:59";

                    sqlWhere = "WHERE t.tgl_transaksi BETWEEN '" + mulai + "' AND '" + akhir + "' ";
                }

                
                String sqlFinal = sqlBase + sqlWhere + "ORDER BY t.tgl_transaksi DESC"; 

                java.sql.Statement st = con.createStatement();
                java.sql.ResultSet rs = st.executeQuery(sqlFinal);

                while (rs.next()) {
                    
                    String idTrx = rs.getString("id_transaksi");
                    String pembeli = rs.getString("nama_customer"); 
                    String namaBrg = rs.getString("nama_product");
                    String idBrg = rs.getString("id_product");
                    int qty = rs.getInt("quantity");
                    double hBeli = rs.getDouble("harga_beli");
                    double hJual = rs.getDouble("harga_jual");
                    double subJual = rs.getDouble("subtotal");
                    double subModal = rs.getDouble("modal_item");

                    totalOmset += subJual;
                    totalModal += subModal;

                    
                    model.addRow(new Object[]{
                        idTrx,      
                        pembeli,    
                        namaBrg, 
                        idBrg, 
                        qty, 
                        (int)hBeli, 
                        (int)hJual, 
                        (int)subModal, 
                        (int)subJual
                    });
                }

                txtOmset.setText("Rp " + (int)totalOmset);
                txtKeuntungan.setText("Rp " + (int)(totalOmset - totalModal));

                if (model.getRowCount() == 0) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Data tidak ditemukan.");
                }

            } catch (Exception e) {
                System.out.println("Error Laporan: " + e.getMessage());
                e.printStackTrace();
            }
    }
}