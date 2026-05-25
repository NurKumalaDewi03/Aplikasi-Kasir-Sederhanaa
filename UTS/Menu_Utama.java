package UTS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXMLLoader; 
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class Menu_Utama {
    @FXML private Button btninput;
    @FXML private Button btnchange;
    @FXML private Button btndelete;
    @FXML private Button btnpreview;
    @FXML private Button btnsearch;
    @FXML private TextField txtmasukan;
    @FXML private ComboBox<String> cbmenu;
    @FXML private Label lblts;
    @FXML private Label lbldate;
    @FXML private TableView<Nota> listnota;
    @FXML private TableColumn<Nota, String> Tanggal_Nota;
    @FXML private TableColumn<Nota, String> No_Nota;
    @FXML private TableColumn<Nota, String> Nama_Pelanggan;
    @FXML private TableColumn<Nota, String> Total_Nota;

    public void initialize() {
        cbmenu.getItems().addAll("Nomor Nota", "Nama Pelanggan");
        cbmenu.getSelectionModel().selectFirst();

        Tanggal_Nota.setCellValueFactory(new PropertyValueFactory<>("Tanggal_Nota"));
        No_Nota.setCellValueFactory(new PropertyValueFactory<>("No_Nota"));
        Nama_Pelanggan.setCellValueFactory(new PropertyValueFactory<>("Nama_Pelanggan"));
        Total_Nota.setCellValueFactory(new PropertyValueFactory<>("Total_Bayar"));

        tampilkanData();
    }
    
    private void tampilkanData() {
        listnota.getItems().clear(); 
        try{
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            
            // Kembali ke query sederhana!
            String query = "SELECT * FROM Nota";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                Nota nota = new Nota(
                    rs.getString("Tanggal_Nota"),
                    rs.getString("No_Nota"),
                    rs.getString("Nama_Pelanggan"),
                    rs.getString("Total_Bayar") // Mengambil dari kolom Total_Bayar di database
                );
                listnota.getItems().add(nota);
            }
        } catch (Exception e){
            System.out.println("Gagal menampilkan : " + e.getMessage());
        }
    }

    @FXML
    private void bukainput(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Input.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btninput.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    @FXML
    private void bukachange(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inputChange.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnchange.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
   @FXML
    private void delete(ActionEvent event){
        Nota pilihan = listnota.getSelectionModel().getSelectedItem();

        if (pilihan != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus");
            alert.setHeaderText("Apakah Anda yakin ingin menghapus nota ini?");
            alert.setContentText("No Nota: " + pilihan.getNo_Nota());
            
            Optional<ButtonType> jawaban = alert.showAndWait();
            
            if(jawaban.isPresent() && jawaban.get() == ButtonType.OK){
                try{
                    Statement statement = Koneksi.getKoneksi().createStatement();
                    
                    // 1. JURUS UTAMA: Hapus data anaknya (rincian barang) terlebih dahulu!
                    String queryDetail = "DELETE FROM Nota_Detail WHERE No_Nota = '" + pilihan.getNo_Nota() + "'";
                    statement.executeUpdate(queryDetail);
                    
                    // 2. Setelah anaknya bersih, baru hapus data induknya (Nota)!
                    String queryNota = "DELETE FROM Nota WHERE No_Nota = '" + pilihan.getNo_Nota() + "'";
                    statement.executeUpdate(queryNota);
                    
                    // Hapus dari tampilan tabel layar
                    listnota.getItems().remove(pilihan);
                    
                    Alert sukses = new Alert(Alert.AlertType.INFORMATION);
                    sukses.setTitle("Berhasil");
                    sukses.setContentText("Nota beserta rincian barangnya berhasil dihapus bersih!");
                    sukses.showAndWait();
                    
                } catch (Exception e){
                    Alert gagal = new Alert(Alert.AlertType.ERROR);
                    gagal.setContentText("Gagal hapus: " + e.getMessage());
                    gagal.showAndWait();
                }
            }
        } else {
            Alert peringatan = new Alert(Alert.AlertType.WARNING);
            peringatan.setTitle("Peringatan");
            peringatan.setHeaderText(null);
            peringatan.setContentText("Silakan pilih nota di tabel terlebih dahulu!");
            peringatan.showAndWait();
        }
    }

    @FXML
    private void search(ActionEvent event){
        String keyword = txtmasukan.getText();
        String kategori = cbmenu.getValue();
        if(keyword.isEmpty() || keyword.trim().isEmpty()){
            tampilkanData();
            return;
        }
        if(kategori == null){
            System.out.println("Pilih Kategori");
            return;
        }
        String kolomDb = "";
        switch (kategori) {
            case "Nomor Nota":
                kolomDb = "No_Nota";
                break;
            case "Nama Pelanggan":
                kolomDb = "Nama_Pelanggan";
                break;
        }
        listnota.getItems().clear();
        try {
            Connection conn = Koneksi.getKoneksi(); 
            Statement stmt = conn.createStatement();    
            
            // Kembali ke query sederhana untuk pencarian
            String query = "SELECT * FROM Nota WHERE " + kolomDb + " LIKE '%" + keyword + "%'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Nota nota = new Nota(
                    rs.getString("Tanggal_Nota"),
                    rs.getString("No_Nota"),
                    rs.getString("Nama_Pelanggan"),
                    rs.getString("Total_Bayar") // Mengambil dari kolom Total_Bayar
                );
                listnota.getItems().add(nota);  
            }
        } catch (Exception e) {
            System.out.println("Gagal mencari: " + e.getMessage());
        }
    }

    @FXML
    private void preview(ActionEvent event) {
        Nota pilihan = listnota.getSelectionModel().getSelectedItem();

        if (pilihan != null) {
            try {
                // 1. OTOMATISASI: Cek file dengan P besar, jika tidak ketemu cari yang p kecil
                java.net.URL fxmlLocation = getClass().getResource("Preview.fxml");
                if (fxmlLocation == null) {
                    fxmlLocation = getClass().getResource("preview.fxml");
                }
                
                // Jika dua-duanya tidak ketemu, lempar pesan error
                if (fxmlLocation == null) {
                    throw new RuntimeException("File Preview.fxml atau preview.fxml tidak ditemukan di folder package!");
                }

                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();
                
                Preview kontrol = loader.getController();
                if (kontrol == null) {
                    throw new RuntimeException("Controller gagal dimuat! Periksa atribut fx:controller di file FXML Anda.");
                }
                
                kontrol.setNoNota(pilihan.getNo_Nota());
                
                Stage stage = (Stage) btnpreview.getScene().getWindow();
                stage.setScene(new Scene(root));
                
            } catch (Exception e) { // MENGGUNAKAN EXCEPTION (Mampu menangkap SEBUTAN ERROR APAPUN)
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Pencarian File");
                alert.setHeaderText("Sistem Mendeteksi Masalah:");
                alert.setContentText(e.toString());
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert peringatan = new Alert(Alert.AlertType.WARNING);
            peringatan.setTitle("Peringatan");
            peringatan.setHeaderText(null);
            peringatan.setContentText("Silakan pilih nota di tabel yang ingin di-preview!");
            peringatan.showAndWait();
        }
    }
}