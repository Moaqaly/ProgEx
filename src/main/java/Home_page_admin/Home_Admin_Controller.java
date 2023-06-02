package Home_page_admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

public class Home_Admin_Controller {

        @FXML
        private Button Book_now_Button;

        @FXML
        private void Book_now_ButtonOnAction(ActionEvent e) {
                System.out.println("booked !");
        }
        @FXML
        private Button Logout_Button;
        @FXML
        private void handleLogout(ActionEvent event) throws IOException {
                // Load the LoginPage.fxml file
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/LoginPage.fxml"));
                Scene scene = new Scene(root);

                // Get the current stage (window)
                Stage currentStage = (Stage) Logout_Button.getScene().getWindow();

                // Set the login page scene and show the stage
                currentStage.setScene(scene);
                currentStage.show();
        }


        // to make SUNDAY and SATURDAY not selectable
        @FXML
        private DatePicker Date_Selecter;
        @FXML
        private void initialize() {
                Date_Selecter.setDayCellFactory(picker -> new DateCell() {
                        @Override
                        public void updateItem(LocalDate date, boolean empty) {
                                super.updateItem(date, empty);

                                // Disable Sundays and Saturdays
                                if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                                        setDisable(true);
                                }
                        }
                });
        }


        }









