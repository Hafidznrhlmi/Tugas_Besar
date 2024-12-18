import javafx.scene.control.Alert;

public class controllertetap {
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null); 
        alert.showAndWait();
    }
}
