package UTS;

public class Nota_Detail {
    private String id_detail;
    private String id_Nota;
    private String id_Barang;
    private String No_Nota;
    private String Nama_Barang;
    private String Harga_Satuan;
    private String Jumlah_beli;
    private String Total;
    public Nota_Detail(String id_detail, String id_Nota, String id_Barang, String no_Nota, String nama_Barang,
            String harga_Satuan, String jumlah_beli, String total) {
        this.id_detail = id_detail;
        this.id_Nota = id_Nota;
        this.id_Barang = id_Barang;
        No_Nota = no_Nota;
        Nama_Barang = nama_Barang;
        Harga_Satuan = harga_Satuan;
        Jumlah_beli = jumlah_beli;
        Total = total;
    }
    public String getId_detail() {
        return id_detail;
    }
    public void setId_detail(String id_detail) {
        this.id_detail = id_detail;
    }
    public String getId_Nota() {
        return id_Nota;
    }
    public void setId_Nota(String id_Nota) {
        this.id_Nota = id_Nota;
    }
    public String getId_Barang() {
        return id_Barang;
    }
    public void setId_Barang(String id_Barang) {
        this.id_Barang = id_Barang;
    }
    public String getNo_Nota() {
        return No_Nota;
    }
    public void setNo_Nota(String no_Nota) {
        No_Nota = no_Nota;
    }
    public String getNama_Barang() {
        return Nama_Barang;
    }
    public void setNama_Barang(String nama_Barang) {
        Nama_Barang = nama_Barang;
    }
    public String getHarga_Satuan() {
        return Harga_Satuan;
    }
    public void setHarga_Satuan(String harga_Satuan) {
        Harga_Satuan = harga_Satuan;
    }
    public String getJumlah_beli() {
        return Jumlah_beli;
    }
    public void setJumlah_beli(String jumlah_beli) {
        Jumlah_beli = jumlah_beli;
    }
    public String getTotal() {
        return Total;
    }
    public void setTotal(String total) {
        Total = total;
    }
    
}
