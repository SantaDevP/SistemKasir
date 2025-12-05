/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Santa
 */
public class transaksi {
    private String id_transaksi;
    private String tgl_transaksi;
    private int id_pegawai;
    private int id_customer;
    private double total_belanja;

    public transaksi() {
    }

    public transaksi(String id_transaksi, String tgl_transaksi, int id_pegawai, int id_customer, double total_belanja) {
        this.id_transaksi = id_transaksi;
        this.tgl_transaksi = tgl_transaksi;
        this.id_pegawai = id_pegawai;
        this.id_customer = id_customer;
        this.total_belanja = total_belanja;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public double getTotal_belanja() {
        return total_belanja;
    }

    public void setTotal_belanja(double total_belanja) {
        this.total_belanja = total_belanja;
    }
    
}
