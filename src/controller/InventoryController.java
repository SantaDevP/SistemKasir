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
    
     
    public void simpanBarangBaru(String id, String nama, String idKategori, int hargaBeli, int hargaJual, int stokAwal) {
        Connection con = DBaseConnection.connect();
        try {
            if (cekApakahBarangAda(id)) {
                JOptionPane.showMessageDialog(null, "ID Barang sudah ada!");
                return;
            }

             
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

    
    public void tampilkanData(JTable tabel) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
         
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Supplier");
        model.addColumn("Tambah Stock"); 
        
        try {
            Statement st = con.createStatement();
             
            String sql = "SELECT p.id_product, p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.nama_supplier " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier";
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_product"),
                    rs.getString("nama_product"),
                    rs.getInt("harga_beli"),
                    rs.getInt("harga_jual"),
                    rs.getInt("stok"),
                    rs.getString("nama_supplier"), // Muncul Huruf (misal: Makanan)
                    ""
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Load Table: " + e.getMessage());
        }
    }

     
    public String[] cariDetailProduct(String idProduct) {
        Connection con = DBaseConnection.connect();
        String[] data = new String[5];
        
        try {
            String sql = "SELECT p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.nama_supplier " +
                         "FROM product p LEFT JOIN supplier s ON p.id_supplier = s.id_supplier " +
                         "WHERE p.id_product = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                data[0] = rs.getString("nama_product");
                data[1] = rs.getString("nama_supplier"); 
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
    public void updateHargaJual(String id, int hargaBaru) {
        Connection con = DBaseConnection.connect();
        try {
            String sql = "UPDATE product SET harga_jual = ? WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, hargaBaru);
            ps.setString(2, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Harga Jual Berhasil Diupdate!");
        } catch (Exception e) {
            System.out.println("Error Update Jual: " + e.getMessage());
        }
    }
    public void updateHargaBeli(String id, int hargaBaru) {
    Connection con = DBaseConnection.connect();
    try {
        String sql = "UPDATE product SET harga_beli = ? WHERE id_product = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, hargaBaru);
        ps.setString(2, id);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(null, "Harga Beli Berhasil Diupdate!");
    } catch (Exception e) {
        System.out.println("Error Update Beli: " + e.getMessage());
    }
}
     
    public void updateStok(String idProduct, int tambahStok, int hargaBeliBaru, int hargaJualBaru) {
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

     
    public void filterTable(JTable tabel, String id) {
        Connection con = DBaseConnection.connect();
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("ID Produk");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Supplier"); 
        model.addColumn("Tambah Stock");
        
        try {
            String sql = "SELECT p.id_product, p.nama_product, p.harga_beli, p.harga_jual, p.stok, s.nama_supplier " +
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
                    rs.getString("nama_supplier"),
                    ""
                });
            }
            tabel.setModel(model);
        } catch (Exception e) {
            System.out.println("Gagal Filter: " + e.getMessage());
        }
    }

     
    public void tambahBarisTabel(JTable tabel, String id) {
        Connection con = DBaseConnection.connect();
         
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
                    rs.getString("Kategori"),
                    ""
                });
            }
        } catch (Exception e) {
            System.out.println("Gagal Tambah Baris: " + e.getMessage());
        }
    }

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
     

    public void simpanDariTabel(JTable tabel) {
            if (tabel.isCellSelected(tabel.getSelectedRow(), tabel.getSelectedColumn())) {
            if (tabel.isEditing()) {
                tabel.getCellEditor().stopCellEditing();
            }
        }

        Connection con = DBaseConnection.connect();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabel.getModel();

        try {
            con.setAutoCommit(false);  

            String sql = "UPDATE product SET stok = stok + ? WHERE id_product = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            boolean adaPerubahan = false;

             
            for (int i = 0; i < model.getRowCount(); i++) {
                String id = model.getValueAt(i, 0).toString();

                 
                Object tambahObj = model.getValueAt(i, 6);
                int tambah = 0;

                if (tambahObj != null && !tambahObj.toString().isEmpty()) {
                    tambah = Integer.parseInt(tambahObj.toString());
                }

                 
                if (tambah > 0) {
                     
                    ps.setInt(1, tambah);
                    ps.setString(2, id);
                    ps.addBatch();
                    adaPerubahan = true;

                     
                    int stokLama = Integer.parseInt(model.getValueAt(i, 4).toString());

                     
                    int stokBaru = stokLama + tambah;

                     
                    model.setValueAt(stokBaru, i, 4);

                     
                    model.setValueAt("0", i, 6);
                }
            }

            if (adaPerubahan) {
                ps.executeBatch();
                con.commit();
                JOptionPane.showMessageDialog(null, "Stok Berhasil Diupdate!");
            } else {
                 
            }

            con.setAutoCommit(true);

        } catch (Exception e) {
            System.out.println("Error Simpan: " + e.getMessage());
            try { con.rollback(); } catch (Exception ex) {}
        }
    }
}