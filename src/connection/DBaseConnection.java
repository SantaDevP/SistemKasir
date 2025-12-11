package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Santa
 */
  public class DBaseConnection {
  public static Connection connect() {
        Connection con = null;
        try {
           
            String url = "jdbc:mysql://localhost:3306/clandys";
            String user = "root";
            String password = "";

            
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi berhasil!");

        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return con;
    }    
}
