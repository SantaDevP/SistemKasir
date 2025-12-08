/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Santa
 */
public class detailTransaksi {
    private int id_detail;
    private String id_transaksi;
    private int id_product;
    private int quantity;
    private int subtotal;

    public detailTransaksi(int id_detail, String id_transaksi, int id_product, int quantity, int subtotal) {
        this.id_detail = id_detail;
        this.id_transaksi = id_transaksi;
        this.id_product = id_product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    
    public detailTransaksi(){
        
    }

    public int getId_detail() {
        return id_detail;
    }

    public void setId_detail(int id_detail) {
        this.id_detail = id_detail;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
    
}
