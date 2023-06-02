package Add_User;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Add_user extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Add_user.class.getResource("/com/example/parkinglottool/Add_User.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 965, 779);
            stage.setTitle("Add_user");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }

