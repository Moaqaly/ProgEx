package Home_page_admin;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Home_Admin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Home_Admin.class.getResource("/com/example/parkinglottool/Home_Admin.fxml"));
        BorderPane root = fxmlLoader.load();

        Scene scene = new Scene(root, 1010,953 );
        stage.setTitle("Home_page_admin");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/com/example/parkinglottool/Style.css").toExternalForm());

        new SlideInLeft(root).play();
        stage.setResizable(false); // Disable window resizing
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
