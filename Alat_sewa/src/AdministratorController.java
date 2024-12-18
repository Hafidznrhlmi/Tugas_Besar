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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdministratorController extends controllertetap {
    @FXML
    private TextField idAdminTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TableView<Admin> adminTableView;

    @FXML
    private TableColumn<Admin, String> idAdminColumn;

    @FXML
    private TableColumn<Admin, String> usernameColumn;

    @FXML
    private TableColumn<Admin, String> passwordColumn;

    @FXML
    private TableColumn<Admin, String> emailColumn;

    @FXML
    private Button submitButton;

    @FXML
    private Button kembaliButton;

    @FXML
    private Button hapusButton;

    @FXML
    private Button updButton;

    private ObservableList<Admin> adminList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map table columns to Admin properties
        idAdminColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("idAdmin"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("email"));
        // Bind the adminList to the table
        adminTableView.setItems(adminList);
        // Add listener for row selection
        adminTableView.setOnMouseClicked(this::pilihrow);
        loadAdminData();
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        String query = "INSERT INTO administrator (`ID Admin`, `Username`, `Password`, `email`) VALUES (?, ?, ?, ?)";
        try (Connection conn = databasecon.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, idAdminTextField.getText());
            pstmt.setString(2, usernameTextField.getText());
            pstmt.setString(3, passwordTextField.getText());
            pstmt.setString(4, emailTextField.getText());
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data berhasil ditambahkan.");
            clearTextFields();
            loadAdminData();
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menambahkan data.");
                        e.printStackTrace();
                    }
                }
   
    private void loadAdminData() {
        adminList.clear(); // Bersihkan list terlebih dahulu
    
        String query = "SELECT `ID Admin`, `Username`, `Password`, `email` FROM administrator";
    
        try (Connection conn = databasecon.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                java.sql.ResultSet resultSet = pstmt.executeQuery()) {
    
            while (resultSet.next()) {
                String idAdmin = resultSet.getString("ID Admin");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String email = resultSet.getString("email");
    
                // Tambahkan data ke ObservableList
                adminList.add(new Admin(idAdmin, username, password, email));
            }
    
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data.");
            e.printStackTrace();
        }
    }
                
    @FXML
    void handleKembali(ActionEvent event) throws IOException {
        // kembali ke halaman homepage jika klik button kembali
        Parent previousPage = FXMLLoader.load(getClass().getResource("HomePage.FXML"));
        Scene previousScene = new Scene(previousPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();
    }
    @FXML
    void handleHapus(ActionEvent event) {
    Admin pilihAdmin = adminTableView.getSelectionModel().getSelectedItem();
    if (pilihAdmin != null) {
        // Konfirmasi sebelum menghapus
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin menghapus data ini?");
        confirmAlert.setContentText("Data akan dihapus secara permanen.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Query SQL untuk menghapus data berdasarkan ID Admin
                String query = "DELETE FROM administrator WHERE `ID Admin` = ?";
                
                try (Connection conn = databasecon.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    // Set parameter query
                    pstmt.setString(1, pilihAdmin.getIdAdmin());

                    int affectedRows = pstmt.executeUpdate(); // Eksekusi query

                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Data berhasil dihapus.");

                        // Hapus data dari ObservableList dan refresh TableView
                        adminList.remove(pilihAdmin);
                        adminTableView.refresh();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Delete Error", "Gagal menghapus data.");
                    }

                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus data dari database.");
                    e.printStackTrace();
                }
            }
        });
    } else {
        // Jika tidak ada data yang dipilih
        showAlert(Alert.AlertType.WARNING, "No Selection", "Pilih data yang ingin dihapus.");
    }
}

    @FXML
    void pilihrow(MouseEvent event) {
        // Admin yang sudah terpilih
        Admin pilihAdmin = adminTableView.getSelectionModel().getSelectedItem();
        if (pilihAdmin != null) {
            idAdminTextField.setText(pilihAdmin.getIdAdmin());
            usernameTextField.setText(pilihAdmin.getUsername());
            passwordTextField.setText(pilihAdmin.getPassword());
            emailTextField.setText(pilihAdmin.getEmail());
        }
    }
    @FXML
    void handleUpdate(ActionEvent event) {
    Admin pilihAdmin = adminTableView.getSelectionModel().getSelectedItem();
    if (pilihAdmin != null) {
        // Ambil data dari TextField
        String idAdmin = idAdminTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        String email = emailTextField.getText().trim();
        // Validasi input
        if (idAdmin.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Field masih kosong. Isi semua data terlebih dahulu.");
            return;
        }
        // Query SQL untuk UPDATE data
        String query = "UPDATE administrator SET `Username` = ?, `Password` = ?, `email` = ? WHERE `ID Admin` = ?";
        
        try (Connection conn = databasecon.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set parameter query
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, idAdmin);
            int affectedRows = pstmt.executeUpdate(); // Eksekusi query
            
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data berhasil diperbarui.");
                // Perbarui data di ObservableList
                pilihAdmin.setUsername(username);
                pilihAdmin.setPassword(password);
                pilihAdmin.setEmail(email);
                adminTableView.refresh(); // Refresh TableView
                
                clearTextFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Gagal memperbarui data.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memperbarui data di database.");
            e.printStackTrace();
        }
    } else {
        // Jika tidak ada admin yang dipilih, tampilkan alert
        showAlert(Alert.AlertType.WARNING, "No Selection", "Pilih Admin untuk diupdate.");
    }
}
    private void clearTextFields() {
        idAdminTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
        emailTextField.clear();
    }
}
