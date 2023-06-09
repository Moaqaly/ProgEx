package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/com/example/parkinglottool/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 947, 801);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false); // Disable window resizing
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}