/*
 * InventoryController.java
 * Versi SUPER LENGKAP (Final Edition)
 * Mencakup: Join Table, Auto-Create Kategori, Update Stok, Filter & Multi-Item.
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
    public void simpanBarangBaru(String id, String nama, String idKategori, int hargaBeli, int hargaJual, int stokAwal) {
        Connection con = DBaseConnection.connect();
        try {
            if (cekApakahBarangAda(id)) {
                JOptionPane.showMessageDialog(null, "ID Barang sudah ada!");
                return;
            }

            // Query Insert ke tabel product
            // Kita simpan ID Kategori (Angka) ke kolom id_supplier
            String sql = "INSERT INTO product (id_product, nama_product, id_supplier, harga_beli, harga_jual, stok) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, idKategori); 
            ps.setInt(4, hargaBeli);
            ps.setInt(5, hargaJual);
            ps.setInt(6, stokAwal);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Barang Baru Berhasil Disimpan!");
            
        } catch (Exception e) {
            System.out.println("Error Simpan: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal Simpan: " + e.getMessage());
        }
    }

    // 2. TAMPILKAN SEMUA DATA (Reset Tabel)
    public void tampilkanData(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        // Header Tabel
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Kategori"); // Muncul Teks (bukan angka)
        
        try {
            Statement st = con.createStatement();
            // JOIN: Ambil nama kategori dari tabel supplier
            String sql = "SELECT p.id_product, p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.Kategori " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier";
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("harga_beli"),
                    rs.getInt("harga_jual"),
                    rs.getInt("stok"),
                    rs.getString("Kategori") // Muncul Huruf (misal: Makanan)
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Load Table: " + e.getMessage());
        }
    }

    // 3. CARI DETAIL (Untuk Mengisi Form)
    public String[] cariDetailProduct(String idProduct) {
        Connection con = DBaseConnection.connect();
        String[] data = new String[5];
        
        try {
            String sql = "SELECT p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.Kategori " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier " +
                         "WHERE p.id_product = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                data[0] = rs.getString("nama_product");
                data[1] = rs.getString("Kategori"); 
                data[2] = rs.getString("harga_beli"); 
                data[3] = rs.getString("harga_jual");       
                data[4] = rs.getString("stok");       
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error Cari: " + e.getMessage());
            return null;
        }
        return data;
    }

    // 4. UPDATE STOK DAN HARGA
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
            JOptionPane.showMessageDialog(null, "Stok Berhasil Ditambah!");
            
        } catch (Exception e) {
            System.out.println("Error Update: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal Update: " + e.getMessage());
        }
    }

    // 5. FILTER TABEL (Hanya Tampilkan 1 Barang, Tabel Direset Dulu)
    public void filterTable(JTable tabel, String id) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Kategori"); 
        
        try {
            String sql = "SELECT p.id_product, p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.Kategori " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier " +
                         "WHERE p.id_product = ?";
                         
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("harga_beli"),
                    rs.getInt("harga_jual"),
                    rs.getInt("stok"),
                    rs.getString("Kategori")
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Filter: " + e.getMessage());
        }
    }

    // 6. TAMBAH BARIS KE TABEL (Tanpa Reset, Menumpuk ke Bawah)
    public void tambahBarisTabel(JTable tabel, String id) {
        Connection con = DBaseConnection.connect();
        // Ambil model yang SUDAH ADA (Jangan bikin baru)
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        
        try {
            String sql = "SELECT p.id_product, p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.Kategori " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier " +
                         "WHERE p.id_product = ?";
                         
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("harga_beli"),
                    rs.getInt("harga_jual"),
                    rs.getInt("stok"),
                    rs.getString("Kategori")
                });
            }
        } catch (Exception e) {
            System.out.println("Gagal Tambah Baris: " + e.getMessage());
        }
    }

    // 7. CEK ATAU BUAT KATEGORI BARU (Helper)
    // Kalau user ketik "Minuman", sistem cari ID-nya. Kalau gak ada, dibuatkan baru.
    public String cekAtauBuatKategori(String namaKategori) {
        Connection con = DBaseConnection.connect();
        try {
            // A. CEK SUDAH ADA?
            String sqlCek = "SELECT id_supplier FROM supplier WHERE Kategori LIKE ?";
            PreparedStatement psCek = con.prepareStatement(sqlCek);
            psCek.setString(1, namaKategori);
            ResultSet rs = psCek.executeQuery();
            
            if (rs.next()) {
                return rs.getString("id_supplier"); // Ada, kembalikan ID
            } else {
                // B. BELUM ADA, BUAT BARU
                Statement st = con.createStatement();
                ResultSet rsMax = st.executeQuery("SELECT MAX(id_supplier) FROM supplier");
                int idBaru = 1;
                if (rsMax.next()) {
                    idBaru = rsMax.getInt(1) + 1;
                }
                
                String sqlInsert = "INSERT INTO supplier (id_supplier, nama_supplier, Kategori, alamat, no_telp, email_suplier) VALUES (?, ?, ?, '-', '-', '-')";
                PreparedStatement psInsert = con.prepareStatement(sqlInsert);
                psInsert.setInt(1, idBaru);
                psInsert.setString(2, "Supplier " + namaKategori);
                psInsert.setString(3, namaKategori);
                psInsert.executeUpdate();
                
                return String.valueOf(idBaru);
            }
        } catch (Exception e) {
            System.out.println("Error Kategori: " + e.getMessage());
            return "0";
        }
    }

    // 8. CEK APAKAH BARANG SUDAH ADA (Boolean)
    // Dipakai untuk Validasi Tombol Tambah (Hybrid)
    public boolean cekApakahBarangAda(String id) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "SELECT id_product FROM product WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // True jika ada
        } catch (Exception e) {
            System.out.println("Error Cek Barang: " + e.getMessage());
            return false;
        }
    }
}