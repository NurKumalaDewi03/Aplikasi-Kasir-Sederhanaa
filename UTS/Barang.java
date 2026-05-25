package UTS;
public class Barang {
    private String id_barang;
    private String Nama_Barang;
    private String Stok;
    private String Harga_Satuan;
    public Barang(String id_barang, String nama_Barang, String stok, String harga_Satuan) {
        this.id_barang = id_barang;
        Nama_Barang = nama_Barang;
        Stok = stok;
        Harga_Satuan = harga_Satuan;
    }
    public String getId_barang() {
        return id_barang;
    }
    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }
    public String getNama_Barang() {
        return Nama_Barang;
    }
    public void setNama_Barang(String nama_Barang) {
        Nama_Barang = nama_Barang;
    }
    public String getStok() {
        return Stok;
    }
    public void setStok(String stok) {
        Stok = stok;
    }
    public String getHarga_Satuan() {
        return Harga_Satuan;
    }
    public void setHarga_Satuan(String harga_Satuan) {
        Harga_Satuan = harga_Satuan;
    }
}
