public class sewa extends PenyewaanController{
    private String idSewa;
    private String idCustomer;
    private String tanggalSewa;
    private String tanggalPengembalian;
    private String waktu;
    private String status;

    public sewa(String idSewa, String idCustomer, String tanggalSewa, String tanggalPengembalian, String waktu, String status) {
        this.idSewa = idSewa;
        this.idCustomer = idCustomer;
        this.tanggalSewa = tanggalSewa;
        this.tanggalPengembalian = tanggalPengembalian;
        this.waktu = waktu;
        this.status = status;
    }

    // Getter dan Setter
    public String getIdSewa() { 
        return idSewa;
    }
    public void setIdSewa(String idSewa) { 
        this.idSewa = idSewa; 
    }

    public String getIdCustomer() { 
        return idCustomer; 
    }
    public void setIdCustomer(String idCustomer) { 
        this.idCustomer = idCustomer; 
    }

    public String getTanggalSewa() { 
        return tanggalSewa;
    }
    public void setTanggalSewa(String tanggalSewa) {
         this.tanggalSewa = tanggalSewa; 
        }

    public String getTanggalPengembalian() {
         return tanggalPengembalian; 
        }
    public void setTanggalPengembalian(String tanggalPengembalian) { 
        this.tanggalPengembalian = tanggalPengembalian; 
    }

    public String getWaktu() {
         return waktu; 
        }
    public void setWaktu(String waktu) {
         this.waktu = waktu; 
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

