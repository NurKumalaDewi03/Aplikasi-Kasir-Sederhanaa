package UTS;
public class Nota {
    private String Tanggal_Nota;
    private String No_Nota;
    private String Nama_Pelanggan;
    private String Total_Bayar;
    public Nota(String tanggal_Nota, String no_Nota, String nama_Pelanggan, String total_Bayar) {
        Tanggal_Nota = tanggal_Nota;
        No_Nota = no_Nota;
        Nama_Pelanggan = nama_Pelanggan;
        Total_Bayar = total_Bayar;
    }
    public String getTanggal_Nota() {
        return Tanggal_Nota;
    }
    public void setTanggal_Nota(String tanggal_Nota) {
        Tanggal_Nota = tanggal_Nota;
    }
    public String getNo_Nota() {
        return No_Nota;
    }
    public void setNo_Nota(String no_Nota) {
        No_Nota = no_Nota;
    }
    public String getNama_Pelanggan() {
        return Nama_Pelanggan;
    }
    public void setNama_Pelanggan(String nama_Pelanggan) {
        Nama_Pelanggan = nama_Pelanggan;
    }
    public String getTotal_Bayar() {
        return Total_Bayar;
    }
    public void setTotal_Bayar(String total_Bayar) {
        Total_Bayar = total_Bayar;
    }
}
