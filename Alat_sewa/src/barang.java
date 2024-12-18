public class barang  {
        private String idBarang;
        private String merkBarang;
        private String namaAlat;
        private double hargaBarang;
        private int stock;
        private String waktu;

        public barang(String idBarang, String merkBarang, String namaAlat, double hargaBarang, int stock, String waktu) {
            this.idBarang = idBarang;
            this.merkBarang = merkBarang;
            this.namaAlat = namaAlat;
            this.hargaBarang = hargaBarang;
            this.stock = stock;
            this.waktu = waktu;
        }

        public String getIdBarang() {
            return idBarang;
        }

        public String getMerkBarang() {
            return merkBarang;
        }

        public String getNamaAlat() {
            return namaAlat;
        }

        public double getHargaBarang() {
            return hargaBarang;
        }

        public int getStock() {
            return stock;
        }

        public String getWaktu() {
            return waktu;
        }
}

