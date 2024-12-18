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

public class TransaksiController extends controllertetap {
    @FXML
    private TextField idTransaksiField;
    @FXML
    private ComboBox<String> idSewaField;
    @FXML
    private TextField jumlahField;
    @FXML
    private TextField metodePembayaranField;
    @FXML
    private Button submitButton;
    @FXML
    private Button kembaliButton;
    @FXML
    private Button hapusButton;
    @FXML
    private TableView<Transaksi> transaksiTable;
    @FXML
    private TableColumn<Transaksi, String> idTransaksiColumn;
    @FXML
    private TableColumn<Transaksi, String> idSewaColumn;
    @FXML
    private TableColumn<Transaksi, Double> jumlahColumn;
    @FXML
    private TableColumn<Transaksi, LocalDate> tanggalPembayaranColumn;
    @FXML
    private TableColumn<Transaksi, String> metodePembayaranColumn;
    private ObservableList<Transaksi> transaksiList;
    private ObservableList<String> idSewaList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Kolom tabel
        idTransaksiColumn.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        idSewaColumn.setCellValueFactory(new PropertyValueFactory<>("idSewa"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        tanggalPembayaranColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPembayaran"));
        metodePembayaranColumn.setCellValueFactory(new PropertyValueFactory<>("metodePembayaran"));

        // List tabel
        transaksiList = FXCollections.observableArrayList();
        transaksiTable.setItems(transaksiList);

        // Muat data ID Sewa ke ComboBox
        loadIdSewaToComboBox();
    }

    private void loadIdSewaToComboBox() {
        idSewaList.clear();
        String query = "SELECT `ID Sewa` FROM penyewaan";

        try (Connection conn = databasecon.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idSewa = rs.getString("ID Sewa");
                idSewaList.add(idSewa);
            }

            idSewaField.setItems(idSewaList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat ID Sewa dari database.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSubmit(ActionEvent event) {
    try {
        // Ambil data dari input pengguna
        String idTransaksi = idTransaksiField.getText();
        String idSewa = idSewaField.getValue();
        String jumlahInput = jumlahField.getText();
        String metodePembayaran = metodePembayaranField.getText();

        if (idTransaksi.isEmpty() || idSewa == null || jumlahInput.isEmpty() || metodePembayaran.isEmpty()) {
            throw new IllegalArgumentException("Semua field harus diisi!");
        }

        double jumlah = Double.parseDouble(jumlahInput);
        LocalDate tanggalPembayaran = LocalDate.now();

        // Masukkan data ke database
        String query = "INSERT INTO transaksi (`ID Transaksi`, `ID Sewa`, `Jumlah`, `Tanggal Pembayaran`, `Metode Pembayaran`) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databasecon.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idTransaksi);
            stmt.setString(2, idSewa);
            stmt.setDouble(3, jumlah);
            stmt.setDate(4, java.sql.Date.valueOf(tanggalPembayaran));
            stmt.setString(5, metodePembayaran);
            stmt.executeUpdate(); 
            // Menjalankan perintah SQL
            // Jika berhasil, tambahkan ke TableView
            Transaksi transaksi = new Transaksi(idTransaksi, idSewa, jumlah, tanggalPembayaran, metodePembayaran);
            transaksiList.add(transaksi);

            // Bersihkan input form
            idTransaksiField.clear();
            jumlahField.clear();
            metodePembayaranField.clear();

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi berhasil ditambahkan.");
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Kesalahan", "Jumlah harus berupa angka!");
    } catch (IllegalArgumentException e) {
        showAlert(Alert.AlertType.ERROR, "Kesalahan", e.getMessage());
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Kesalahan", "Gagal menambahkan transaksi ke database.");
        e.printStackTrace();
    }
}

@FXML
void handleHapus(ActionEvent event) {
    Transaksi selectedTransaksi = transaksiTable.getSelectionModel().getSelectedItem();
    if (selectedTransaksi != null) {
        // Konfirmasi sebelum menghapus
        Alert pesanAlert = new Alert(Alert.AlertType.CONFIRMATION);
        pesanAlert.setTitle("Konfirmasi Penghapusan");
        pesanAlert.setContentText("Apakah Anda yakin ingin menghapus transaksi ini?");
        pesanAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String query = "DELETE FROM transaksi WHERE `ID Transaksi` = ?";
                try (Connection conn = databasecon.getConnection();
                     PreparedStatement statement = conn.prepareStatement(query)) {

                    statement.setString(1, selectedTransaksi.getIdTransaksi());
                    statement.executeUpdate(); 
                    transaksiList.remove(selectedTransaksi);
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi berhasil dihapus.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus transaksi dari database.");
                }
            }
        });
    } else {
        showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih transaksi yang akan dihapus!");
    }
}
    @FXML
    void handleKembali(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("HomePage.FXML"));
        Scene previousScene = new Scene(previousPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }
}
