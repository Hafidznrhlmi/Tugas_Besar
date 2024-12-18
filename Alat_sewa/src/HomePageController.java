import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class HomePageController {
    @FXML
    private Button Administrator;
    @FXML 
    private Button Stock;
    @FXML
    private Button Sewa;
    @FXML
    private Button  Pengembalian;
    @FXML
    private Button Transaksi;
    @FXML
    void administrator(ActionEvent event) throws IOException {
        Parent pageAdmin = FXMLLoader.load(getClass().getResource("Admin.FXML"));
        Scene sceneAdmin = new Scene(pageAdmin);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(sceneAdmin);
        stage.show();
    }

    @FXML
    void stock(ActionEvent event) throws IOException {
        Parent pageStock = FXMLLoader.load(getClass().getResource("StockBarang.FXML"));
        Scene sceneStock = new Scene(pageStock);

        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
   
        stage.setScene(sceneStock);
        stage.show();
     }
    @FXML
    void sewa(ActionEvent event) throws IOException {
        Parent pageSewa = FXMLLoader.load(getClass().getResource("Penyewaan.FXML"));
        Scene sceneSewa = new Scene(pageSewa);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(sceneSewa);
        stage.show();
    } 
    @FXML
    void pengembalian(ActionEvent event) throws IOException {
        Parent pagekembali = FXMLLoader.load(getClass().getResource("Pengembalian.FXML"));
        Scene scenekembali = new Scene(pagekembali);

        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scenekembali);
        stage.show();
    }
    @FXML
    void Transaksi(ActionEvent event) throws IOException{
        Parent pageTran = FXMLLoader.load(getClass().getResource("Transaksi.FXML"));
        Scene sceneTran = new Scene(pageTran);

        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(sceneTran);
        stage.show();
    }   
}
