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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PengembalianController {
    @FXML
    private Button buttonSubmit;

    @FXML
    private Button buttonKembai;

    @FXML
    private Button buttonHapus;

    @FXML
    private TextField idPengembalianField;

    @FXML
    private TextField tanggalPengembalianField;

    @FXML
    private TextField idPenyewaanField;

    @FXML
    private TextField kondisiAlatField;

    @FXML
    private TableView<pengembalian> tableView;

    @FXML
    private TableColumn<pengembalian, String> idPengembalianColumn;

    @FXML
    private TableColumn<pengembalian, LocalDate> tanggalPengembalianColumn;

    @FXML
    private TableColumn<pengembalian, String> idPenyewaanColumn;

    @FXML
    private TableColumn<pengembalian, String> kondisiColumn;

    private ObservableList<pengembalian> pengembalianList;

    @FXML
    public void initialize() {
        // untuk tabel 
        idPengembalianColumn.setCellValueFactory(new PropertyValueFactory<>("idPengembalian"));
        tanggalPengembalianColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));
        idPenyewaanColumn.setCellValueFactory(new PropertyValueFactory<>("idPenyewaan"));
        kondisiColumn.setCellValueFactory(new PropertyValueFactory<>("kondisiAlat"));

        // Inisialisasi daftar pengembalian
        pengembalianList = FXCollections.observableArrayList();
        tableView.setItems(pengembalianList);
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        try {
            // submit dari field
            String idPengembalian = idPengembalianField.getText();
            String tanggalPengembalianInput = tanggalPengembalianField.getText();
            String idPenyewaan = idPenyewaanField.getText();
            String kondisiAlat = kondisiAlatField.getText();

            // Validasi input kosong
            if (idPengembalian.isEmpty() || tanggalPengembalianInput.isEmpty() ||
                    idPenyewaan.isEmpty() || kondisiAlat.isEmpty()) {
                throw new IllegalArgumentException("Semua bidang harus diisi.");
            }

            // Parsing tanggal
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate tanggalPengembalian = LocalDate.parse(tanggalPengembalianInput, formatter);

            // Tambahkan data ke tabel
            pengembalian pengembalian = new pengembalian(idPengembalian, tanggalPengembalian, idPenyewaan, kondisiAlat);
            pengembalianList.add(pengembalian);

            // Bersihkan input field
            idPengembalianField.clear();
            tanggalPengembalianField.clear();
            idPenyewaanField.clear();
            kondisiAlatField.clear();
        } catch (IllegalArgumentException | DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Walawee Error", "Pastikan semua input valid.\nTanggal harus dalam format yyyy-MM-dd.");
        }
    }

    @FXML
    void handleKembali(ActionEvent event) throws IOException {
        // balik ke halaman sebelumnya
        Parent previousPage = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Scene previousScene = new Scene(previousPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }

    @FXML
    void handleHapus(ActionEvent event) {
        // Ambil data yang dipilih dari tabel
        pengembalian selectedPengembalian = tableView.getSelectionModel().getSelectedItem();
        if (selectedPengembalian != null) {
            pengembalianList.remove(selectedPengembalian);
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih data yang akan dihapus.");
        }
    }
    void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
