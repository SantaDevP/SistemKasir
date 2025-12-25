package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Santa
 */
public class product {
    private String id_product;
    private String nama_product;
    private double harga_jual;
    private double harga_beli;
    private int stok;
    private String supplier;

    // Constructor kosong
    public product() {}

    // Constructor dengan isi
    public product(String id, String nama, double harga, int stok) {
        this.id_product = id;
        this.nama_product = nama;
        this.harga_jual = harga;
        this.stok = stok;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getNama_product() {
        return nama_product;
    }

    public void setNama_product(String nama_product) {
        this.nama_product = nama_product;
    }

    public double getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(double harga_jual) {
        this.harga_jual = harga_jual;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    
    public String getId_supplier() {
        return supplier;
    }

    public void setId_supplier(String id_supplier) {
        this.supplier = supplier;
    }
}
