package controller;

import connection.DBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.transaksi;  
import model.detailTransaksi;  

public class TransaksiController {

     
    public String getNoTransaksiOtomatis() {
        String idBaru = "TRX-001";  
        Connection con = DBaseConnection.connect();
        
        try {
            Statement st = con.createStatement();
             
            String sql = "SELECT id_transaksi FROM transaksi ORDER BY id_transaksi DESC LIMIT 1";
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                 
                String idTerakhir = rs.getString("id_transaksi");
             
                int angka = Integer.parseInt(idTerakhir.substring(4));
      
                angka++;
 
                idBaru = "TRX-" + String.format("%03d", angka);
            }
        } catch (Exception e) {
            System.out.println("Error Auto Number: " + e.getMessage());
        }
        return idBaru;
    }

 
    public boolean simpanTransaksi(transaksi t, ArrayList<detailTransaksi> listDetail) {
        Connection con = DBaseConnection.connect();
        try {
  
            con.setAutoCommit(false); 
 
            String sqlTrans = "INSERT INTO transaksi (id_transaksi, tgl_transaksi, id_pegawai, id_customer, total_belanja) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psTrans = con.prepareStatement(sqlTrans);
  
            String tglDB = t.getTgl_transaksi(); 
            
            psTrans.setString(1, t.getId_transaksi());
            psTrans.setString(2, tglDB);
            psTrans.setInt(3, t.getId_pegawai());
            psTrans.setInt(4, t.getId_customer());
            psTrans.setDouble(5, t.getTotal_belanja());
            psTrans.executeUpdate();
  
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_product, quantity, subtotal) VALUES (?, ?, ?, ?)";
            String sqlStok = "UPDATE product SET stok = stok - ? WHERE id_product = ?";
            
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);
            PreparedStatement psStok = con.prepareStatement(sqlStok);
            
            for (detailTransaksi d : listDetail) {
 
                psDetail.setString(1, t.getId_transaksi());
                psDetail.setInt(2, d.getId_product());
                psDetail.setInt(3, d.getQuantity());
                psDetail.setDouble(4, d.getSubtotal());
                psDetail.executeUpdate();  
   
                psStok.setInt(1, d.getQuantity());
                psStok.setInt(2, d.getId_product());
                psStok.executeUpdate();
            }
         
            con.commit();
            con.setAutoCommit(true);
            return true;
            
        } catch (Exception e) {
            try {
                con.rollback();  
                System.out.println("Rollback dilakukan.");
            } catch (SQLException ex) {}
            
            System.out.println("Error Simpan Transaksi: " + e.getMessage());
            return false;
        }
    }
    
}