package controller;

import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.transaksi; // Pastikan model ini ada
import model.detailTransaksi; // Pastikan model ini ada

public class TransaksiController {

    // 1. METHOD GENERATE NOMOR TRANSAKSI OTOMATIS
    public String getNoTransaksiOtomatis() {
        String idBaru = "TRX-001"; // Default jika database kosong
        Connection con = DBaseConnection.connect();
        
        try {
            Statement st = con.createStatement();
            // Ambil id terakhir, urutkan dari terbesar
            String sql = "SELECT id_transaksi FROM transaksi ORDER BY id_transaksi DESC LIMIT 1";
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                // Misal data terakhir: TRX-005
                String idTerakhir = rs.getString("id_transaksi");
                
                // Ambil angkanya saja (hapus "TRX-" di 4 karakter awal) -> "005"
                // Ubah jadi integer -> 5
                int angka = Integer.parseInt(idTerakhir.substring(4));
                
                // Tambah 1 -> 6
                angka++;
                
                // Format ulang jadi String dengan 3 digit (006) -> TRX-006
                idBaru = "TRX-" + String.format("%03d", angka);
            }
        } catch (Exception e) {
            System.out.println("Error Auto Number: " + e.getMessage());
        }
        return idBaru;
    }

    // 2. METHOD SIMPAN TRANSAKSI
    public boolean simpanTransaksi(transaksi t, ArrayList<detailTransaksi> listDetail) {
        Connection con = DBaseConnection.connect();
        try {
            // Matikan auto-commit agar data tersimpan sekaligus (header + detail)
            con.setAutoCommit(false); 
            
            // A. SIMPAN HEADER TRANSAKSI
            // Sesuaikan dengan kolom di clandys.sql: id_transaksi, tgl_transaksi, id_pegawai, id_customer, total_belanja
            String sqlTrans = "INSERT INTO transaksi (id_transaksi, tgl_transaksi, id_pegawai, id_customer, total_belanja) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psTrans = con.prepareStatement(sqlTrans);
            
            // Tanggal harus format yyyy-MM-dd HH:mm:ss untuk MySQL DATETIME
            // Asumsi t.getTgl_transaksi() masih String dari JDateChooser yang sudah diformat di View, 
            // ATAU kita format ulang disini biar aman:
            String tglDB = t.getTgl_transaksi(); 
            
            psTrans.setString(1, t.getId_transaksi());
            psTrans.setString(2, tglDB);
            psTrans.setInt(3, t.getId_pegawai());
            psTrans.setInt(4, t.getId_customer());
            psTrans.setDouble(5, t.getTotal_belanja());
            psTrans.executeUpdate();
            
            // B. SIMPAN DETAIL & UPDATE STOK
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_product, quantity, subtotal) VALUES (?, ?, ?, ?)";
            String sqlStok = "UPDATE product SET stok = stok - ? WHERE id_product = ?";
            
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);
            PreparedStatement psStok = con.prepareStatement(sqlStok);
            
            for (detailTransaksi d : listDetail) {
                // Masukkan Detail
                psDetail.setString(1, t.getId_transaksi());
                psDetail.setInt(2, d.getId_product());
                psDetail.setInt(3, d.getQuantity());
                psDetail.setDouble(4, d.getSubtotal());
                psDetail.executeUpdate(); // Eksekusi per baris barang
                
                // Kurangi Stok Barang
                psStok.setInt(1, d.getQuantity());
                psStok.setInt(2, d.getId_product());
                psStok.executeUpdate();
            }
            
            // Jika sampai sini lancar, simpan permanen
            con.commit();
            con.setAutoCommit(true);
            return true;
            
        } catch (Exception e) {
            try {
                con.rollback(); // Batalkan semua jika ada error di tengah
                System.out.println("Rollback dilakukan.");
            } catch (SQLException ex) {}
            
            System.out.println("Error Simpan Transaksi: " + e.getMessage());
            return false;
        }
    }
}