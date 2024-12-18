import java.time.LocalDate;

public class Transaksi {
    private String idTransaksi;
    private String idSewa;
    private double jumlah;
    private LocalDate tanggalPembayaran;
    private String metodePembayaran;

    public Transaksi(String idTransaksi, String idSewa, double jumlah, LocalDate tanggalPembayaran, String metodePembayaran) {
        this.idTransaksi = idTransaksi;
        this.idSewa = idSewa;
        this.jumlah = jumlah;
        this.tanggalPembayaran = tanggalPembayaran;
        this.metodePembayaran = metodePembayaran;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIdSewa() {
        return idSewa;
    }

    public void setIdSewa(String idSewa) {
        this.idSewa = idSewa;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public LocalDate getTanggalPembayaran() {
        return tanggalPembayaran;
    }

    public void setTanggalPembayaran(LocalDate tanggalPembayaran) {
        this.tanggalPembayaran = tanggalPembayaran;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
}

