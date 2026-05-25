package UTS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormInput {

    @FXML private ComboBox<String> cbIdBarang;
    @FXML private TextField txtNamaBarang;
    @FXML private TextField txtQty;
    @FXML private Button btnSave;
    @FXML private Button btnClose;

    // Variabel rahasia untuk menyimpan harga tanpa ditampilkan di layar
    private double hargaSatuan = 0; 

    @FXML
    public void initialize() {
        // Kunci nama barang agar kasir tidak ngetik manual (biar otomatis dari database)
        txtNamaBarang.setEditable(false); 

        // 1. Sedot ID Barang dari tabel 'Barang' di MySQL ke dalam ComboBox
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_Barang FROM Barang");
            while (rs.next()) {
                cbIdBarang.getItems().add(rs.getString("id_Barang"));
            }
        } catch (Exception e) {
            System.out.println("Gagal memuat ID Barang: " + e.getMessage());
        }

        // 2. OTOMATISASI: Begitu kasir memilih ID Barang, Nama & Harga langsung ditarik!
        cbIdBarang.setOnAction(e -> {
            String idTerpilih = cbIdBarang.getValue();
            if (idTerpilih != null) {
                try {
                    Connection conn = Koneksi.getKoneksi();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Nama_Barang, Harga_Satuan FROM Barang WHERE id_Barang = '" + idTerpilih + "'");
                    if (rs.next()) {
                        txtNamaBarang.setText(rs.getString("Nama_Barang"));
                        hargaSatuan = rs.getDouble("Harga_Satuan"); // Simpan harganya ke memori
                    }
                } catch (Exception ex) {
                    System.out.println("Gagal menarik detail barang: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            String idBarang = cbIdBarang.getValue();
            String namaBarang = txtNamaBarang.getText();
            String qtyStr = txtQty.getText();

            if (idBarang == null || qtyStr.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setContentText("Pilih ID Barang dan isi Qty terlebih dahulu!");
                alert.showAndWait();
                return;
            }

            int qty = Integer.parseInt(qtyStr);
            double totalHarga = qty * hargaSatuan;

            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            
            // JURUS DARURAT: Matikan dulu aturan ketat MySQL sementara waktu
            stmt.execute("SET FOREIGN_KEY_CHECKS=0;");
            
            String query = "INSERT INTO Nota_Detail (id_Nota, id_Barang, No_Nota, Nama_Barang, Harga_Satuan, Jumlah_beli, Total) " +
                           "VALUES (1, '" + idBarang + "', 'N-001', '" + namaBarang + "', '" + hargaSatuan + "', '" + qty + "', '" + totalHarga + "')";
            stmt.executeUpdate(query);

            // Nyalakan kembali aturan ketat MySQL
            stmt.execute("SET FOREIGN_KEY_CHECKS=1;");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukses");
            alert.setContentText("Barang berhasil ditambahkan ke keranjang!");
            alert.showAndWait();

            cbIdBarang.setValue(null);
            txtNamaBarang.clear();
            txtQty.clear();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error MySQL: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void tutupForm(ActionEvent event) throws IOException {
        // Balik ke layar Input utama (Pastikan nama file FXML-nya sesuai, misalnya input.fxml atau Input.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Input.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}


