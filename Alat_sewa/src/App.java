import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage primStage) throws Exception{
        Parent root =  FXMLLoader.load(getClass().getResource("TESTER.fxml"));
        Scene scene = new Scene(root);

        primStage.setTitle("Login Page Administrator");
        primStage.setScene(scene);
        primStage.show();

        
    }
    public static void main(String[] args) {
        launch(args);
    }
}