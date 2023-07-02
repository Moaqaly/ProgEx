package HistoryAdmin;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoryAdmin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HistoryAdmin.class.getResource("/com/example/parkinglottool/HistoryAdmin.fxml"));
        BorderPane root = fxmlLoader.load();

        Scene scene = new Scene(root, 1016, 937);
        stage.setTitle("History");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/com/example/parkinglottool/Style.css").toExternalForm());

        new SlideInLeft(root).play();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

