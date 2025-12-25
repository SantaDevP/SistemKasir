/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Santa
 */
public class userSession {
    private static String id_pegawai;
    private static String nama_pegawai;

    public userSession() {
    }
    
    public static void setUserLogin(String id, String nama){
        id_pegawai = id;
        nama_pegawai = nama;
    }
    public static String getId_pegawai(){
        return id_pegawai;
    }
    public static String getNama_pegawai(){
        return nama_pegawai;
    }
}
