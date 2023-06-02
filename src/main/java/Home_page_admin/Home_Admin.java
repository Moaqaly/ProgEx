package Home_page_admin;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class Home_Admin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Home_Admin.class.getResource("/com/example/parkinglottool/Home_Admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1016, 937);
        stage.setTitle("Home_page_admin");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/com/example/parkinglottool/Style.css").toExternalForm());



        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}
