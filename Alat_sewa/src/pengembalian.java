import java.time.LocalDate;

public class pengembalian {
    private String idPengembalian;
    private LocalDate tanggalPengembalian;
    private String idPenyewaan;
    private String kondisiAlat;

    public pengembalian(String idPengembalian, LocalDate tanggalPengembalian, String idPenyewaan, String kondisiAlat) {
        this.idPengembalian = idPengembalian;
        this.tanggalPengembalian = tanggalPengembalian;
        this.idPenyewaan = idPenyewaan;
        this.kondisiAlat = kondisiAlat;
    }

    public String getIdPengembalian() {
        return idPengembalian;
    }

    public void setIdPengembalian(String idPengembalian) {
        this.idPengembalian = idPengembalian;
    }

    public LocalDate getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(LocalDate tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }

    public String getIdPenyewaan() {
        return idPenyewaan;
    }

    public void setIdPenyewaan(String idPenyewaan) {
        this.idPenyewaan = idPenyewaan;
    }

    public String getKondisiAlat() {
        return kondisiAlat;
    }

    public void setKondisiAlat(String kondisiAlat) {
        this.kondisiAlat = kondisiAlat;
    }
}
