/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package absensi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author ASUS
 */
public class Koneksi {
    public static Connection mysqlKonek;

    public static Connection koneksiDB() throws SQLException {
        if (mysqlKonek == null) {
            try {
                String DB = "jdbc:mysql://localhost:3307/restoran";
                String user = "root";
                String pass = "";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                mysqlKonek = DriverManager.getConnection(DB, user, pass);
            } catch (SQLException e) {
                // Handle the exception appropriately, e.g., log the error or re-throw it
                e.printStackTrace();
                throw e;
            }
        }
        return mysqlKonek;
    }
}
