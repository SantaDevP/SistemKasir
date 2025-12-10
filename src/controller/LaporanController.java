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
        
        // 1. Validasi: Pastikan tanggal sudah dipilih
        if (tglMulai.getDate() == null || tglAkhir.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Harap pilih rentang tanggal mulai dan akhir!");
            return;
        }
        
        // 2. Persiapan Koneksi & Tabel
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        model.setRowCount(0); // Bersihkan tabel lama
        
        // 3. Format Tanggal untuk Query MySQL (yyyy-MM-dd)
        // Kita tambah jam 00:00:00 dan 23:59:59 agar mencakup seluruh hari itu
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String mulai = sdf.format(tglMulai.getDate()) + " 00:00:00";
        String akhir = sdf.format(tglAkhir.getDate()) + " 23:59:59";

        // Variabel penampung total
        double totalOmset = 0;
        double totalModal = 0;

        try {
            // 4. QUERY SQL (LOGIKA UTAMA)
            // Menggabungkan data penjualan dengan data modal barang
            String sql = "SELECT p.nama_product, p.id_product, "
                       + "SUM(dt.quantity) as qty_terjual, "
                       + "p.harga_beli, "
                       + "p.harga_jual, "
                       + "SUM(dt.subtotal) as total_jual, " // Omzet per item (Harga Jual x Qty)
                       + "SUM(dt.quantity * p.harga_beli) as total_modal " // Modal per item (Harga Beli x Qty)
                       + "FROM detail_transaksi dt "
                       + "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                       + "JOIN product p ON dt.id_product = p.id_product "
                       + "WHERE t.tgl_transaksi BETWEEN '" + mulai + "' AND '" + akhir + "' "
                       + "GROUP BY p.id_product"; // Dikelompokkan biar barang yang sama jadi satu baris

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // 5. Masukkan Data ke Tabel GUI
            while (rs.next()) {
                String nama = rs.getString("nama_product");
                String id = rs.getString("id_product");
                int qty = rs.getInt("qty_terjual");
                double hBeli = rs.getDouble("harga_beli");
                double hJual = rs.getDouble("harga_jual");
                double subModal = rs.getDouble("total_modal");
                double subJual = rs.getDouble("total_jual");
                
                // Tambahkan ke Total Besar
                totalOmset += subJual;
                totalModal += subModal;

                // Masukkan baris ke JTable
                model.addRow(new Object[]{
                    nama,       // Kolom Nama Barang
                    id,         // Kolom ID Barang
                    qty,        // Kolom Jumlah
                    (int)hBeli, // Kolom Harga Beli
                    (int)hJual, // Kolom Harga Jual
                    (int)subModal, // Kolom SubTotal Beli
                    (int)subJual   // Kolom SubTotal Jual
                });
            }
            
            // 6. Hitung & Tampilkan Keuntungan Bersih
            double keuntungan = totalOmset - totalModal;
            
            txtOmset.setText("Rp " + (int)totalOmset);
            txtKeuntungan.setText("Rp " + (int)keuntungan);
            
            // Cek jika tidak ada data
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Tidak ada transaksi pada periode ini.");
                txtOmset.setText("Rp 0");
                txtKeuntungan.setText("Rp 0");
            }

        } catch (Exception e) {
            System.out.println("Error Laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}