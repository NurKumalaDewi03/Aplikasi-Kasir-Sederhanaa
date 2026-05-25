package UTS;

public class Pelanggan {
    private String id_pelanggan;
    private String Nama_Pelanggan;
    private String No_HP;
    public Pelanggan(String id_pelanggan, String nama_Pelanggan, String no_HP) {
        this.id_pelanggan = id_pelanggan;
        Nama_Pelanggan = nama_Pelanggan;
        No_HP = no_HP;
    }
    public String getId_pelanggan() {
        return id_pelanggan;
    }
    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }
    public String getNama_Pelanggan() {
        return Nama_Pelanggan;
    }
    public void setNama_Pelanggan(String nama_Pelanggan) {
        Nama_Pelanggan = nama_Pelanggan;
    }
    public String getNo_HP() {
        return No_HP;
    }
    public void setNo_HP(String no_HP) {
        No_HP = no_HP;
    }
}
