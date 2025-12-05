package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Santa
 */
public class supplier {
    private int id_supplier;
    private String nama_supplier;
    private String alamat;
    private String telp;

    public supplier(int id_supplier, String nama_supplier, String alamat, String telp) {
        this.id_supplier = id_supplier;
        this.nama_supplier = nama_supplier;
        this.alamat = alamat;
        this.telp = telp;
    }

    public supplier() {
        
    }

    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(int id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getNama_supplier() {
        return nama_supplier;
    }

    public void setNama_supplier(String nama_supplier) {
        this.nama_supplier = nama_supplier;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
    
    
}
