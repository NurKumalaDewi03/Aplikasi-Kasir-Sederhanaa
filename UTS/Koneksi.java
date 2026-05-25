package UTS;
//memasukkan library untuk menghubungkan mysql dengan java
import java.sql.Connection;
import java.sql.DriverManager;



public class Koneksi {
    //cara untuk menghubungkan antara mysql dengan java.
    public static Connection getKoneksi(){
        //alamat url untuk menghubungkan dengan java, dengan mamsukkan nama database yang telah kita buat
        String url = "jdbc:mysql://localhost:3306/uts";
        String user = "root";
        String password = "jalan1234";
        Connection koneksi = null;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection(url,user,password);
        } catch (Exception e){
            System.out.println("Gagal: " + e.getMessage());
            
        } 
        return koneksi;
    }
    public static void main(String[] args) {
        System.out.println("mencoba koneksi");
        Connection testKoneksi = getKoneksi();

        if (testKoneksi != null){
            System.out.println("Terjadi Koneksi");
        }else {
            System.out.println("Gagal Terhubung");
        }
    }
}


