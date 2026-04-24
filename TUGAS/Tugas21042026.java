package TUGAS;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;



public class Tugas21042026 extends Application{
    @Override
    //agar window keluar saat Run
    public void start(Stage stage){
    //membuat judul dari aplikasinya
        Label Title = new Label("APLIKASI PERHITUNGAN TAHUN KABISAT");
        //untuk menentukan jenis font dan ukurna font
        Title.setFont(new Font("Times New Roman", 16));
        //membuat kotak untuk tempat tahun di input oleh user
        TextField Year = new TextField();
        //menentukan ukran dan kotak tahunnya
        Year.setMaxWidth(110);
        //agar di dalam kotak tahun muncul perintah input
        Year.setPromptText("Input Tahun");
        //membuat tombol untuk cek tahun yang dimasukkan
        Button Cek = new Button("CEK");
       //kotak unutk menampung tahun dan tombol cek
        HBox Kotak = new HBox(Year, Cek);
        Kotak.setSpacing(4);
        //menentukan poisis text 
        Kotak.setAlignment(Pos .CENTER);
        //masukkan semua komponen yang telah di buat : judul, tahun, dan tombol cekknya kedalam satu box
        VBox root = new VBox(Title, Kotak);
        //menentukan jarak antara tiap komponen (Year, button, dan judulnya)
        root.setSpacing(16);
        //menentukan ukuran window aplikasinya
        Scene scene = new Scene(root, 500, 300);
        //jdul dan windownya
        stage.setTitle("Tugas 21 April 2026");
        //agar kursor tidak langsung berada di dalam jotak input tahun.
        root.requestFocus();
        stage.setScene(scene);
        stage.show();

        //membuat agar tombol cek berfungsi   
        Label Cetak = new Label("");
        Cek.setOnAction(e -> {
            //merubah inputan year yang sebelumnya bertipe string menjadi integert
             int tahun = Integer.parseInt(Year.getText());
        //membuat logika agar tahun yang di input dapat dikenali apakah termasuk tahun kabisat atau bukan
        if (tahun % 400 == 0){
            Cetak.setText("Tahun " + tahun + " adalah tahun kabisat");
        }
          else if (tahun % 100 == 0){
            Cetak.setText("Tahun " + tahun + " bukan merupakan tahun kabisat");
          }
            else if (tahun % 4 == 0){
               Cetak.setText("Tahun " + tahun + " adalah tahun kabisat");
            } 
                else {
                    Cetak.setText("Tahun " + tahun + " bukan merupakan tahun kabisat");
                } 
                Cetak.setFont(new Font("Times New Roman", 14));
                //untuk menentukan jarak dari tepi window aplikasi
                Cetak.setPadding(new Insets(10));
                //agar hasil dari logika dapat keluar di window aplikasi
                root.getChildren().add(Cetak);
        });       
    }
    public static void main(String[] args) {
        launch(args);
    }
}