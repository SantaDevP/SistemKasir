package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Santa
 */
public class pegawai {
    private String id_pegawai;
    private String nama_pegawai;
    private String username;
    private String password;
    
    public pegawai(){
        
    }
    public pegawai(String id, String nama, String username, String pass){
        this.id_pegawai = id;
        this.nama_pegawai = nama;
        this.password = pass;
        this.username = username;
    }
    
    public String getId_pegawai(){
        return id_pegawai;
    }
    public void setId_pegawai(String id_pegawai){
        this.id_pegawai = id_pegawai;
    }
    public String getNama_pegawai(){
        return nama_pegawai;
    }
    public void setNama_pegawai(String nama_pegawai){
        this.nama_pegawai = nama_pegawai;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
