package UTS;

    import javafx.event.ActionEvent;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.scene.control.DatePicker;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.control.Button;
    import javafx.scene.control.ButtonType;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.TextField;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.stage.Stage;
    import javafx.scene.control.TableView;
    import javafx.scene.control.TableColumn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
    import java.io.IOException;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

public class Input {
    @FXML private DatePicker txttglinput;
    @FXML private TextField txtNo_Nota;
    @FXML private ComboBox<String> cbNama_Pelanggan;
    @FXML private TextField txtHp_Pelanggan;
    @FXML private TextField txtJumlahBelanja;
    @FXML private TextField txtdiskon;
    @FXML private TextField txtsubtotal;
    @FXML private TextField txtgrandtotal;
    @FXML private Button btntomoAdd;
    @FXML private Button btntombolDelete;
    @FXML private TableView<Nota_Detail> TABEL_INPUT;

    @FXML private TableColumn<Nota_Detail, String> id_barang;
    @FXML private TableColumn<Nota_Detail, String> Nama_barang;
    @FXML private TableColumn<Nota_Detail, String> Jumlah_beli;
    @FXML private TableColumn<Nota_Detail, String> Harga_Satuan;
    @FXML private TableColumn<Nota_Detail, String> Total;

    private ObservableList<Nota_Detail> keranjang = FXCollections.observableArrayList();
    private HashMap<String, String> dataPelanggan = new HashMap<>();

    @FXML
    public void initialize() {
        // A. Set Tanggal Otomatis hari ini
        txttglinput.setPromptText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txttglinput.setEditable(false);
        
        // B. Set Nomor Nota Sementara (Nanti malam kita buatkan query otomatisnya)
        txtNo_Nota.setText("N-001");
        
        // C. Sambungkan kolom tabel
       id_barang.setCellValueFactory(new PropertyValueFactory<>("id_Barang"));
        Nama_barang.setCellValueFactory(new PropertyValueFactory<>("Nama_Barang"));
        Jumlah_beli.setCellValueFactory(new PropertyValueFactory<>("Jumlah_beli"));
        Harga_Satuan.setCellValueFactory(new PropertyValueFactory<>("Harga_Satuan"));
        Total.setCellValueFactory(new PropertyValueFactory<>("Total"));
        
        TABEL_INPUT.setItems(keranjang);
        Hitungauto();

        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Nama_Pelanggan, No_HP FROM Pelanggan");
            
            while (rs.next()) {
                String nama = rs.getString("Nama_Pelanggan");
                String noHp = rs.getString("No_HP");
                
                // 1. Masukkan nama ke dalam pilihan ComboBox
                cbNama_Pelanggan.getItems().add(nama);
                // 2. Simpan pasangan Nama dan No HP ke dalam memori HashMap
                dataPelanggan.put(nama, noHp);
            }
        } catch (Exception e) {
            System.out.println("Gagal memuat data pelanggan: " + e.getMessage());
        }

        // E. AKSI COMBOBOX: Saat nama dipilih, No HP otomatis terisi
        cbNama_Pelanggan.setOnAction(event -> {
            String namaTerpilih = cbNama_Pelanggan.getValue();
            if (namaTerpilih != null && dataPelanggan.containsKey(namaTerpilih)) {
                // Ambil No HP dari memori berdasarkan nama, lalu tampilkan
                txtHp_Pelanggan.setText(dataPelanggan.get(namaTerpilih));
            }
        });

        
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            // Menyedot kembali barang yang barusan di-save
            ResultSet rs = stmt.executeQuery("SELECT * FROM Nota_Detail WHERE No_Nota = 'N-001'");
            
            keranjang.clear();
            while (rs.next()) {
                Nota_Detail detail = new Nota_Detail(
                    rs.getString("id_detail"), rs.getString("id_Nota"),
                    rs.getString("id_Barang"), rs.getString("No_Nota"),
                    rs.getString("Nama_Barang"), rs.getString("Harga_Satuan"),
                    rs.getString("Jumlah_beli"), rs.getString("Total")
                );
                keranjang.add(detail);
            }
            TABEL_INPUT.setItems(keranjang);
            
            // Panggil fungsi hitung agar Subtotal & Grand Total langsung terupdate!
            Hitungauto(); 
            setNomorNotaOtomatis(); // Panggil fungsi untuk set nomor nota otomatis saat layar input dibuka

        } catch (Exception e) {
            System.out.println("Gagal memuat keranjang: " + e.getMessage());
        }
    }
    private void setNomorNotaOtomatis() {
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            
            // Mencari nomor nota paling besar (terakhir) di tabel Nota
            ResultSet rs = stmt.executeQuery("SELECT MAX(No_Nota) AS LastNota FROM Nota");
            
            if (rs.next() && rs.getString("LastNota") != null) {
                String lastNota = rs.getString("LastNota"); // Contoh dapetnya: N-005
                
                // Memotong huruf "N-" dan mengambil angkanya saja, lalu ditambah 1
                int nomorUrut = Integer.parseInt(lastNota.substring(3));
                nomorUrut++; 
                
                // Merakitnya kembali menjadi format N-XXX (contoh: N-006)
                String nomorBaru = String.format("TS-%03d", nomorUrut);
                txtNo_Nota.setText(nomorBaru);
            } else {
                // Jika tabel Nota masih kosong melompong (transaksi pertama kali)
                txtNo_Nota.setText("TS-001");
            }
        } catch (Exception e) {
            System.out.println("Gagal membuat nomor nota otomatis: " + e.getMessage());
            txtNo_Nota.setText("TS-001"); // Nilai aman jika terjadi error
        }
    }
    private void Hitungauto(){
        double subtotal = 0;
        int jumlah_beli = 0;

        for (Nota_Detail barang : keranjang) {
            subtotal += Double.parseDouble(barang.getTotal());
            jumlah_beli += Integer.parseInt(barang.getJumlah_beli());
        }
        txtsubtotal.setText(String.valueOf(subtotal));
        txtJumlahBelanja.setText(String.valueOf(jumlah_beli));

        double diskon = 0;
        if(subtotal <= 50000){
            diskon = 0;
        }else if(subtotal >50000 && subtotal <= 100000){
            diskon = 0.05 * subtotal;
        }else {
            diskon = 0.1 * subtotal;
        }
        txtdiskon.setText(String.valueOf(diskon));
        double grandtotal = subtotal - diskon;
        txtgrandtotal.setText(String.valueOf(grandtotal));
    }
    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FormInput.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btntomoAdd.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    @FXML
    private void delete(ActionEvent event){
        Nota_Detail pilihan = TABEL_INPUT.getSelectionModel().getSelectedItem();

        if (pilihan != null){
            //menampilakn dialog peringatan untuk penggunan
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus");
            alert.setHeaderText("Apakah Anda yakin ingin menghapus?");
            alert.setContentText("id barang: " + pilihan.getId_Barang());
            //menunggu jawaban pengguna apakah ok atau tidak
            Optional<ButtonType> jawaban = alert.showAndWait();
            //logika jika pengguna menekan OK
            if(jawaban.isPresent() && jawaban.get() == ButtonType.OK){
            try{
                //perintah untuk menghapus data pada tabel nota di dalam mysql/database
                String query = "DELETE FROM Nota_Detail WHERE id_Barang = '" + pilihan.getId_Barang() + "'";
                Statement statement = Koneksi.getKoneksi().createStatement();
                statement.executeUpdate(query);
                //menghilangkan baris nota di layar menu utama
                TABEL_INPUT.getItems().remove(pilihan);
                System.out.println("Nota Berhasil Dihapus");
            } catch (Exception e){
                System.out.println("Gagal" + e.getMessage());
            }
        }
    }
}
@FXML
    private void Save(ActionEvent event) {
        try {
            // 1. Format Tanggal
            String tanggalLayar = txttglinput.getPromptText(); 
            String[] pisah = tanggalLayar.split("/");
            String tanggalMySQL = pisah[2] + "-" + pisah[1] + "-" + pisah[0]; 

            // 2. Tarik Data Teks
            String noNota = txtNo_Nota.getText(); // Ini nilainya N-002, N-003, dst.
            String pelanggan = cbNama_Pelanggan.getValue();
            
            // 3. Tarik Data Angka (Sesuai Mapping)
            String subtotalStr = txtsubtotal.getText();
            if (subtotalStr == null || subtotalStr.isEmpty()) subtotalStr = "0";
            
            String diskonStr = txtdiskon.getText();
            if (diskonStr == null || diskonStr.isEmpty()) diskonStr = "0";
            
            String grandtotalStr = txtgrandtotal.getText();
            if (grandtotalStr == null || grandtotalStr.isEmpty()) grandtotalStr = "0";

            java.sql.Connection conn = Koneksi.getKoneksi();
            java.sql.Statement statement = conn.createStatement();

            // 4. Cari id_pelanggan
            String idPelanggan = "P001"; // Nilai default jika error
            java.sql.ResultSet rs = statement.executeQuery("SELECT id_pelanggan FROM Pelanggan WHERE Nama_Pelanggan = '" + pelanggan + "'");
            if (rs.next()) {
                idPelanggan = rs.getString("id_pelanggan");
            }

            // 5. JURUS PENYELAMAT: Update No_Nota di Nota_Detail dari 'N-001' menjadi nomor baru (misal N-002)
            // Agar rincian barang ikut pindah dan sinkron saat di-preview!
            String queryUpdateDetail = "UPDATE Nota_Detail SET No_Nota = '" + noNota + "' WHERE No_Nota = 'N-001'";
            statement.executeUpdate(queryUpdateDetail);

            // 6. Simpan data induk ke tabel Nota
            String querySelesai = "INSERT INTO Nota (Tanggal_Nota, No_Nota, Nama_Pelanggan, id_pelanggan, Total_Harga, Diskon, Total_Bayar) " +
                                  "VALUES ('" + tanggalMySQL + "', '" + noNota + "', '" + pelanggan + "', '" + idPelanggan + "', '" + subtotalStr + "', '" + diskonStr + "', '" + grandtotalStr + "')";
            
            statement.executeUpdate(querySelesai);

            Alert sukses = new Alert(Alert.AlertType.INFORMATION);
            sukses.setTitle("Berhasil");
            sukses.setContentText("Transaksi Selesai! Seluruh data rincian berhasil disinkronkan ke tabel Nota.");
            sukses.showAndWait();

            // 7. KEMBALI KE MENU UTAMA 
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Menu_Utama.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) txtNo_Nota.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));

        } catch (Exception e) {
            Alert gagal = new Alert(Alert.AlertType.ERROR);
            gagal.setContentText("Error MySQL: " + e.getMessage());
            gagal.showAndWait();
        }
    }
}