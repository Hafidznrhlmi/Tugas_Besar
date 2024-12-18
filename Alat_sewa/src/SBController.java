import javafx.application.Platform;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class SBController extends controllertetap {

    @FXML
    private TextField idBarangField;
    @FXML
    private TextField merkBarangField;
    @FXML
    private TextField namaAlatField;
    @FXML
    private TextField hargaBarangField;
    @FXML
    private TextField stockField;
    @FXML
    private Button submitButton;
    @FXML
    private Button kembaliButton;
    @FXML
    private Button hapusButton;
    @FXML
    private TableView<barang> tableView;
    @FXML
    private TableColumn<barang, String> idBarangColumn;
    @FXML
    private TableColumn<barang, String> merkBarangColumn;
    @FXML
    private TableColumn<barang, String> namaAlatColumn;
    @FXML
    private TableColumn<barang, Double> hargaBarangColumn;
    @FXML
    private TableColumn<barang, Integer> stockColumn;
    @FXML
    private TableColumn<barang, String> waktuColumn;
    // List to store table data
    private ObservableList<barang> itemList;
    @FXML
    public void initialize() {
        // tabel colom
        idBarangColumn.setCellValueFactory(new PropertyValueFactory<barang, String>("idBarang"));
        merkBarangColumn.setCellValueFactory(new PropertyValueFactory<barang, String>("merkBarang"));
        namaAlatColumn.setCellValueFactory(new PropertyValueFactory<barang, String >("namaAlat"));
        hargaBarangColumn.setCellValueFactory(new PropertyValueFactory<barang, Double>("hargaBarang"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<barang, Integer>("stock"));
        waktuColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));

        // Inisialisasi observable list
        itemList = FXCollections.observableArrayList();
        tableView.setItems(itemList);
        loadyangbaru();

    }

    @FXML
    public void loadyangbaru() {
    // Meghapus data sekarang
    itemList.clear();
    String query = "SELECT * FROM Stock";

    try (Connection conn = databasecon.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        // Loop melalui hasil query dan tambahkan ke itemList
        while (rs.next()) {
            String idBarang = rs.getString("ID Barang");
            String merkBarang = rs.getString("Merk Barang");
            String namaAlat = rs.getString("Nama Alat");
            double hargaBarang = rs.getDouble("Harga Barang/hari");
            int stock = rs.getInt("Stock");
            String waktu = rs.getString("Waktu");
            // Membuat objek barang dan menambahkannya ke itemList
            barang item = new barang(idBarang, merkBarang, namaAlat, hargaBarang, stock, waktu);
            itemList.add(item);
        }

        // Setelah data dimuat, refresh table view
        tableView.refresh();

    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat mengambil data dari database.");
        e.printStackTrace();
    }
}

   @FXML
    void handleSubmit(ActionEvent event) {
    try {
        // Membaca input dari text field
        String idBarang = idBarangField.getText();
        String merkBarang = merkBarangField.getText();
        String namaAlat = namaAlatField.getText();
        double hargaBarang = Double.parseDouble(hargaBarangField.getText());
        int stock = Integer.parseInt(stockField.getText());
        // Validasi input, jika data di field kosong akan menampilkan notifikasi
        if (idBarang.isEmpty() || merkBarang.isEmpty() || namaAlat.isEmpty()) {
            throw new IllegalArgumentException("Semua Field harus diisi");
        }
        // Format Waktu
        String waktu = java.time.LocalDateTime.now().toString();
        // Membuat item baru
        barang newItem = new barang(idBarang, merkBarang, namaAlat, hargaBarang, stock, waktu);
        // Insert into database
        String query = "INSERT INTO stock (`ID Barang`, `Merk Barang`, `Nama Alat`, `Harga Barang/hari`, `Stock`, `Waktu`) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databasecon.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for the query
            stmt.setString(1, idBarang);
            stmt.setString(2, merkBarang);
            stmt.setString(3, namaAlat);
            stmt.setDouble(4, hargaBarang);
            stmt.setInt(5, stock);
            stmt.setString(6, waktu);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                //pemberitahuan data sudah berhasil di input
                itemList.add(newItem);
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil ditambahkan.");

                // Clear input field
                idBarangField.clear();
                merkBarangField.clear();
                namaAlatField.clear();
                hargaBarangField.clear();
                stockField.clear(); 
                //merefresh data untuk memastikan data muncul ditabel
                Platform.runLater(() -> tableView.refresh());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan data.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghubungkan ke database.");
            e.printStackTrace();
        }

    } catch (NumberFormatException e) {
        System.err.println("Format Nomor tidak valid: " + e.getMessage());
        showAlert(Alert.AlertType.ERROR, "Input Error", "Format harga atau stock tidak valid.");
    } catch (IllegalArgumentException e) {
        System.err.println("Input Error: " + e.getMessage());
        showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
    }
}
    @FXML
    void handleKembali(ActionEvent event) throws IOException {
        // load ke halaman Homepage
        Parent previousPage = FXMLLoader.load(getClass().getResource("HomePage.FXML"));
        Scene previousScene = new Scene(previousPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }
    @FXML
    void handleHapus(ActionEvent event) {
        // Ambil item yang dipilih di TableView
        barang barangterpilih = tableView.getSelectionModel().getSelectedItem();
        // Pastikan ada item yang dipilih
        if (barangterpilih != null) {
            // Tampilkan konfirmasi penghapusan
            Alert peringakatankonf = new Alert(Alert.AlertType.CONFIRMATION);
            peringakatankonf.setTitle("Konfirmasi Penghapusan");
            peringakatankonf.setContentText("Apakah Anda yakin ingin menghapus data ini?");
            peringakatankonf.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String query = "DELETE FROM Stock WHERE `ID Barang` = ?";
                        try (Connection conn = databasecon.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(query)) {

                            stmt.setString(1, barangterpilih.getIdBarang());
                            int rowsAffected = stmt.executeUpdate();
                            if (rowsAffected > 0) {
                                itemList.remove(barangterpilih);  // Hapus item dari TableView
                                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Gagal", "Data gagal dihapus dari database.");
                            }
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghapus data.");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang ingin dihapus.");
        }
    }
}    
