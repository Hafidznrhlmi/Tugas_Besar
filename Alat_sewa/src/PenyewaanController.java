import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class PenyewaanController extends controllertetap{
    @FXML 
    private Button buttonSubmit;

    @FXML
    private Button buttonKembali;

    @FXML
    private Button buttonHapus;
    @FXML
    private Button buttonPengembalian;

    @FXML
    private TextField idSewaField;

    @FXML
    private TextField idCustomerField;

    @FXML
    private TextField tanggalSewaField;

    @FXML
    private TextField tanggalPengembalianField;

    @FXML
    private ComboBox<String> idBarangField;

    @FXML
    private TableView<sewa> tableView;

    @FXML
    private TableColumn<sewa, String> idSewaColumn;

    @FXML
    private TableColumn<sewa, String> idCustomerColumn;

    @FXML
    private TableColumn<sewa, LocalDate> tanggalSewaColumn;

    @FXML
    private TableColumn<sewa, LocalDate> tanggalPengembalianColumn;

    @FXML
    private TableColumn<sewa, String> waktuColumn;

    @FXML
    private TableColumn<sewa, String> idBarangColumn;

    private ObservableList<sewa> sewaList;

    @FXML
    public void initialize() {
        idSewaColumn.setCellValueFactory(new PropertyValueFactory<>("idSewa"));
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("idCustomer"));
        tanggalSewaColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalSewa"));
        tanggalPengembalianColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));
        waktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        idBarangColumn.setCellValueFactory(new PropertyValueFactory<>("idBarang"));

        sewaList = FXCollections.observableArrayList();
        tableView.setItems(sewaList);
        ambildatabase();
        muatbarang();

    }
    @FXML
    void muatbarang() {
        ObservableList<String> barangList = FXCollections.observableArrayList();
    
        // Query untuk mengambil ID Barang dari database
        String query = "SELECT `ID Barang` FROM Stock";  // Asumsi nama tabel barang adalah 'barang' dan kolomnya adalah 'ID Barang'
        
        try (Connection conn = databasecon.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {
        
            while (rs.next()) {
                String idBarang = rs.getString("ID Barang");
                barangList.add(idBarang);
            }
        
            // Set data yang didapat ke ComboBox
            idBarangField.setItems(barangList);
        
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data barang.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        try {
            // Ambil data dari TextField
            String idSewa = idSewaField.getText();
            String idCustomer = idCustomerField.getText();
            LocalDate tanggalSewa = LocalDate.parse(tanggalSewaField.getText());
            LocalDate tanggalPengembalian = LocalDate.parse(tanggalPengembalianField.getText());
            String waktu = LocalDateTime.now().toString();
            String idBarang = idBarangField.getValue(); 


            if (idSewa.isEmpty() || idCustomer.isEmpty()) {
                throw new IllegalArgumentException("Semua bidang harus diisi!");
            }

            // untuk Insert data
            String query = "INSERT INTO penyewaan (`ID Sewa`, `ID Customer`, `Tanggal Sewa`, `Tanggal Pengembalian`, `Waktu`, `ID Barang`) VALUES (?, ?, ?, ?, ?, ?)";

            // Koneksi dan eksekusi query
            try (Connection conn = databasecon.getConnection();
                 PreparedStatement ewe = conn.prepareStatement(query)) {

                ewe.setString(1, idSewa);
                ewe.setString(2, idCustomer);
                ewe.setString(3, tanggalSewa.toString());
                ewe.setString(4, tanggalPengembalian.toString());
                ewe.setString(5, waktu);
                ewe.setString(6, idBarang);

                int Row = ewe.executeUpdate(); // Eksekusi query

                if (Row > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Data berhasil ditambahkan.");

                    // Setelah berhasil memasukkan data ke DB, tambahkan data ke TableView
                    sewa newSewa = new sewa(idSewa, idCustomer, tanggalSewa.toString(), tanggalPengembalian.toString(), waktu, idBarang);
                    sewaList.add(newSewa);

                    // Clear fields
                    idSewaField.clear();
                    idCustomerField.clear();
                    tanggalSewaField.clear();
                    tanggalPengembalianField.clear();
                    
                } else {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Gagal memasukkan data ke database.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghubungkan ke database.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
        }
    }

    @FXML
    void handleHapus(ActionEvent event) {
        // Ambil item yang dipilih di TableView
        sewa selectedSewa = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedSewa != null) {
            try {
                // Koneksi ke database
                String query = "DELETE FROM penyewaan WHERE `ID Sewa` = ?";
                try (Connection conn = databasecon.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
    
                    // Set parameter untuk query
                    stmt.setString(1, selectedSewa.getIdSewa());
                    
                    // Eksekusi query
                    int rowsAffected = stmt.executeUpdate();
    
                    if (rowsAffected > 0) {
                        // Hapus data dari TableView jika berhasil dihapus dari database
                        sewaList.remove(selectedSewa);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Data berhasil dihapus.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Data tidak ditemukan atau gagal dihapus.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghapus data dari database.");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang akan dihapus.");
        }
    }

    @FXML
    void handlePengembalian(ActionEvent event) {
        // Ambil item yang dipilih di TableView
        sewa selectedSewa = tableView.getSelectionModel().getSelectedItem();
    
        // Periksa apakah ada item yang dipilih
        if (selectedSewa == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang akan dikembalikan.");
            return; // Menghentikan eksekusi jika tidak ada item yang dipilih
        }
    
        // Konfirmasi pengembalian
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Pengembalian");
        confirmationAlert.setContentText("Apakah Anda yakin ingin mengembalikan barang ini?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Koneksi ke database
                    String query = "DELETE FROM penyewaan WHERE `ID Sewa` = ?";
                    try (Connection conn = databasecon.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        // Set parameter untuk query
                        stmt.setString(1, selectedSewa.getIdSewa());
                        // Eksekusi query untuk menghapus data
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            // Hapus data dari TableView jika berhasil dihapus dari database
                            sewaList.remove(selectedSewa);
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Barang sudah dikembalikan.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Data tidak ditemukan atau gagal dihapus.");
                        }
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghapus data dari database.");
                        e.printStackTrace(); // Tambahkan print stack trace untuk debugging
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void handleKembali(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("HomePage.FXML"));
        Scene previousScene = new Scene(previousPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }

    private void ambildatabase() {
        sewaList.clear(); // Bersihkan daftar sebelum memuat ulang
        String query = "SELECT `ID Sewa`, `ID Customer`, `Tanggal Sewa`, `Tanggal Pengembalian`, `Waktu`, `ID Barang` FROM penyewaan";
    
        try (Connection conn = databasecon.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String idSewa = rs.getString("ID Sewa");
                String idCustomer = rs.getString("ID Customer");
                String tanggalSewa = rs.getString("Tanggal Sewa");
                String tanggalPengembalian = rs.getString("Tanggal Pengembalian");
                String waktu = rs.getString("Waktu");
                String idBarang = rs.getString("ID Barang");
                // Buat objek sewa dan tambahkan ke list
                sewa dataSewa = new sewa(idSewa, idCustomer, tanggalSewa, tanggalPengembalian, waktu, idBarang);
                sewaList.add(dataSewa);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data dari database.");
            e.printStackTrace();
        }
    }
}
