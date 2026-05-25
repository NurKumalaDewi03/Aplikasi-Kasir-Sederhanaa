package UTS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Preview {

    @FXML private TextField txtTanggal;
    @FXML private TextField txtNo_Nota;
    @FXML private TextField txtNama_Pelanggan; 
    @FXML private TextField txtHp_Pelanggan;
    @FXML private TextField txtJumlahBelanja;

    @FXML private TableView<Nota_Detail> TABEL_PREVIEW;
    @FXML private TableColumn<Nota_Detail, String> id_barang;
    @FXML private TableColumn<Nota_Detail, String> Nama_barang;
    @FXML private TableColumn<Nota_Detail, String> Jumlah_beli;
    @FXML private TableColumn<Nota_Detail, String> Harga_Satuan;
    @FXML private TableColumn<Nota_Detail, String> Total;

    @FXML private TextField txtsubstotal;
    @FXML private TextField txtdiskon;
    @FXML private TextField txtgrandtotal;
    
    @FXML private Button btnClose; 

    private ObservableList<Nota_Detail> listBarangPreview = FXCollections.observableArrayList();

    public void setNoNota(String noNotaTerpilih) {
        txtNo_Nota.setText(noNotaTerpilih);

        // Kunci form agar read-only
        txtTanggal.setEditable(false);
        txtNo_Nota.setEditable(false);
        txtNama_Pelanggan.setEditable(false);
        txtHp_Pelanggan.setEditable(false);
        txtJumlahBelanja.setEditable(false);
        txtsubstotal.setEditable(false);
        txtdiskon.setEditable(false);
        txtgrandtotal.setEditable(false);

        // Kunci kolom ke class model Nota_Detail
        id_barang.setCellValueFactory(new PropertyValueFactory<>("id_Barang"));
        Nama_barang.setCellValueFactory(new PropertyValueFactory<>("Nama_Barang"));
        Jumlah_beli.setCellValueFactory(new PropertyValueFactory<>("Jumlah_beli"));
        Harga_Satuan.setCellValueFactory(new PropertyValueFactory<>("Harga_Satuan"));
        Total.setCellValueFactory(new PropertyValueFactory<>("Total"));

        Database(noNotaTerpilih);
    }

    private void Database(String noNota) {
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            String namaPelanggan = "";

            // 1. AMBIL DATA DARI TABEL NOTA DENGAN AMAN
            String queryNota = "SELECT * FROM Nota WHERE No_Nota = '" + noNota + "'";
            ResultSet rsNota = stmt.executeQuery(queryNota);
            if (rsNota.next()) {
                txtTanggal.setText(rsNota.getString("Tanggal_Nota"));
                namaPelanggan = rsNota.getString("Nama_Pelanggan");
                txtNama_Pelanggan.setText(namaPelanggan);
                
                // Gunakan try-catch per baris agar tidak saling menggagalkan
                try { txtgrandtotal.setText(rsNota.getString("Total_Bayar")); } catch (Exception e) {}
                try { txtsubstotal.setText(rsNota.getString("Total_Harga")); } catch (Exception e) {} 
                try { txtdiskon.setText(rsNota.getString("Diskon")); } catch (Exception e) {}
            }

            // 2. AMBIL NO HP DARI TABEL PELANGGAN
            try {
                // Asumsi nama kolomnya No_HP atau No_Telepon
                String queryHP = "SELECT No_HP FROM Pelanggan WHERE Nama_Pelanggan = '" + namaPelanggan + "'";
                ResultSet rsHP = conn.createStatement().executeQuery(queryHP);
                if (rsHP.next()) {
                    txtHp_Pelanggan.setText(rsHP.getString(1)); // Ambil kolom pertama
                } else {
                    txtHp_Pelanggan.setText("-");
                }
            } catch(Exception e) {
                 txtHp_Pelanggan.setText("-"); 
            }

            // 3. MASUKKAN DATA KE TABEL & HITUNG QTY
            listBarangPreview.clear();
            String queryDetail = "SELECT * FROM Nota_Detail WHERE No_Nota = '" + noNota + "'";
            ResultSet rsDetail = conn.createStatement().executeQuery(queryDetail);
            
            int totalBarang = 0;
            
            while (rsDetail.next()) {
                Nota_Detail detail = new Nota_Detail(
                    rsDetail.getString("id_detail"), rsDetail.getString("id_Nota"),
                    rsDetail.getString("id_Barang"), rsDetail.getString("No_Nota"),
                    rsDetail.getString("Nama_Barang"), rsDetail.getString("Harga_Satuan"),
                    rsDetail.getString("Jumlah_beli"), rsDetail.getString("Total")
                );
                listBarangPreview.add(detail);
                
                // Tambahkan Qty barang
                try {
                    totalBarang += Integer.parseInt(rsDetail.getString("Jumlah_beli"));
                } catch (Exception e) {}
            }
            TABEL_PREVIEW.setItems(listBarangPreview);
            txtJumlahBelanja.setText(String.valueOf(totalBarang));

        } catch (Exception e) {
            System.out.println("Gagal memuat rincian preview: " + e.getMessage());
        }
    }

    @FXML
    private void Close(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu_Utama.fxml")); 
        Parent root = loader.load();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}