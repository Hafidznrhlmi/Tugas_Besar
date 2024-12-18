import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController extends controllertetap {
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private TextField textFieldUsername;
    @FXML
    void LoginFunction(ActionEvent event) throws IOException {
        String inputUsername = textFieldUsername.getText();
        String inputPassword = textFieldPassword.getText();
        String query = "SELECT * FROM administrator WHERE `Username` = ? AND `Password` = ?";
        
        try (Connection conn = databasecon.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, inputUsername);
            pstmt.setString(2, inputPassword);
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Jika username dan password cocok
                Parent homePageParent = FXMLLoader.load(getClass().getResource("homepage.FXML"));
                Scene homePageScene = new Scene(homePageParent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(homePageScene);
                stage.show();
            } else {
                // Jika username atau password salah
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau Password salah.");
            }
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menghubungkan ke database.");
            e.printStackTrace();
        }
    }
}
