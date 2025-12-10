/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import connection.DBaseConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
/**
 *
 * @author Santa
 */
public class EndDayController {
    public void tampilkanEndDay(Date tanggalDipilih, JTable tabel, JTextField txtKeuntungan, JTextField txtTotalItem) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        model.setRowCount(0); // Bersihkan tabel

        // Format tanggal agar cocok dengan database (yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tglStr = sdf.format(tanggalDipilih);

        double totalUntung = 0;
        int totalBarang = 0;

        try {
            // QUERY 1: ISI TABEL (Rincian Barang Terjual Hari Ini)
            // Kita Group By product agar barang yang sama dijumlahkan qty-nya
            String sqlTabel = "SELECT p.id_product, p.nama_product, "
                            + "SUM(dt.quantity) as qty_total, "
                            + "p.harga_jual, "
                            + "SUM(dt.subtotal) as subtotal_jual, "
                            + "SUM(dt.quantity * p.harga_beli) as subtotal_beli " // Modal total item ini
                            + "FROM detail_transaksi dt "
                            + "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                            + "JOIN product p ON dt.id_product = p.id_product "
                            + "WHERE DATE(t.tgl_transaksi) = '" + tglStr + "' "
                            + "GROUP BY p.id_product";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlTabel);

            while (rs.next()) {
                String id = rs.getString("id_product");
                String nama = rs.getString("nama_product");
                int qty = rs.getInt("qty_total");
                int harga = rs.getInt("harga_jual");
                int subJual = rs.getInt("subtotal_jual"); // Omset item ini
                int subBeli = rs.getInt("subtotal_beli"); // Modal item ini
                
                // Hitung keuntungan per item (Jual - Beli)
                int untungItem = subJual - subBeli;

                // Masukkan ke Tabel
                model.addRow(new Object[]{
                    id, nama, qty, harga, subJual
                });

                // Akumulasi ke Total Besar
                totalBarang += qty;
                totalUntung += untungItem;
            }

            // TAMPILKAN TOTAL KE TEXTFIELD
            txtTotalItem.setText(String.valueOf(totalBarang));
            txtKeuntungan.setText("Rp " + (int)totalUntung);

        } catch (Exception e) {
            System.out.println("Error End Day: " + e.getMessage());
        }
    }
}
