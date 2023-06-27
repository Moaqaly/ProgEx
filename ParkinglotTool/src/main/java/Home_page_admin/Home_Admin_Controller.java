package Home_page_admin;

import Add_User.tableUser;
import Mysql.DatabaseConnection;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Home_Admin_Controller implements Initializable  {
    @FXML
    private Button Add_User_Button;
    @FXML
    private ToggleButton  Parking00;
    @FXML
    private ToggleButton  Parking01;
    @FXML
    private ToggleButton  Parking02;
    @FXML
    private ToggleButton  Parking10;
    @FXML
    private ToggleButton  Parking11;
    @FXML
    private ToggleButton  Parking12;
    @FXML
    private ToggleButton  Parking20;
    @FXML
    private ToggleButton  Parking21;
    @FXML
    private ToggleButton  Parking22;
    @FXML
    private ToggleButton  Parking30;
    @FXML
    private ToggleButton  Parking31;
    @FXML
    private ToggleButton  Parking32;

    /////////////////////////
    @FXML
    private TableColumn<User_selecter_list, String> Column_Details;

    @FXML
    private TableColumn<User_selecter_list, String> Column_User;
    @FXML
    private TableColumn<User_selecter_list, String> Column_Gender;
    @FXML
    private TableView<User_selecter_list> User_selecter_list;
    /////////

       @FXML
       private Button Book_now_Button;

        @FXML
        void Book_now_ButtonOnAction(ActionEvent event) {
             // Create a TranslateTransition to animate the button's position
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), Book_now_Button);
                translateTransition.setFromX(0);
                translateTransition.setToX(-30);

                // Create a FillTransition to animate the button's background color
                FillTransition fillTransition = new FillTransition(Duration.seconds(1), Book_now_Button.getShape());
                fillTransition.setFromValue(Color.web("#6225E6"));
                fillTransition.setToValue(Color.WHITE);

                // Set the cycle count and auto-reverse for the fill transition
                fillTransition.setCycleCount(2);
                fillTransition.setAutoReverse(true);

                // Set an event handler to perform actions after the animations finish
                fillTransition.setOnFinished(e -> {
                        // Add your logic for Step 3 here

                        // Perform the necessary actions
                });

                // Play both animations
                translateTransition.play();
                fillTransition.play();
            //////////////////////


            System.out.println("Book Now button clicked!");

        }

        @FXML
        private void handleAddUser(ActionEvent event) throws IOException{
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Add_User.fxml"));
            Scene scene = new Scene(root);
            Stage crrentStage = (Stage) Add_User_Button.getScene().getWindow();
            crrentStage.setScene(scene);
            crrentStage.show();
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

///////////////////////////     initialize /////////////////////////////
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Column_User.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("name_A"));
        Column_Details.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("details_A"));

        Column_Gender.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("Gender_A"));
        loadUserData();
        /////////

        //parkinglot Button
        ToggleGroup toggleGroup = new ToggleGroup();
        Parking00.setToggleGroup(toggleGroup);
        Parking01.setToggleGroup(toggleGroup);
        Parking02.setToggleGroup(toggleGroup);
        Parking10.setToggleGroup(toggleGroup);
        Parking11.setToggleGroup(toggleGroup);
        Parking12.setToggleGroup(toggleGroup);
        Parking20.setToggleGroup(toggleGroup);
        Parking21.setToggleGroup(toggleGroup);
        Parking22.setToggleGroup(toggleGroup);
        Parking30.setToggleGroup(toggleGroup);
        Parking31.setToggleGroup(toggleGroup);
        Parking32.setToggleGroup(toggleGroup);
        ///////

        User_selecter_list selectedUser = User_selecter_list.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            boolean isDisabled = selectedUser.getDetails_A();
            String gender = selectedUser.getGender_A();

            // Enable or disable parking buttons based on user's characteristics
            Parking00.setDisable(false);
            Parking01.setDisable(false);
            Parking02.setDisable(false);
            Parking10.setDisable(false);
            Parking11.setDisable(false);
            Parking12.setDisable(false);
            Parking20.setDisable(false);
            Parking21.setDisable(false);
            Parking22.setDisable(false);
            Parking30.setDisable(false);
            Parking31.setDisable(false);
            Parking32.setDisable(false);

            if (isDisabled) {
                // User has a disability
                // All parking buttons are available
            } else {
                if (gender.equalsIgnoreCase("female")) {
                    // User is a woman
                    // Disable women-only and disabled parking buttons
                    Parking20.setDisable(true);
                    Parking30.setDisable(true);
                } else if (gender.equalsIgnoreCase("male")) {
                    // User is a man
                    // Disable men-only, women-only, and disabled parking buttons
                    Parking20.setDisable(true);
                    Parking21.setDisable(true);
                    Parking22.setDisable(true);
                    Parking30.setDisable(true);
                    Parking31.setDisable(true);
                    Parking32.setDisable(true);
                }
            }
        } else {
            // No user selected, enable all parking buttons
            Parking00.setDisable(false);
            Parking01.setDisable(false);
            Parking02.setDisable(false);
            Parking10.setDisable(false);
            Parking11.setDisable(false);
            Parking12.setDisable(false);
            Parking20.setDisable(false);
            Parking21.setDisable(false);
            Parking22.setDisable(false);
            Parking30.setDisable(false);
            Parking31.setDisable(false);
            Parking32.setDisable(false);
        }

        ////////////////////////


        ///////////
        Date_Selecter.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Disable Sundays and Saturdays
                if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #63676e;"); // Optional: Highlight disabled dates
                }

                // Disable past dates
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #b70f2e;"); // Optional: Highlight disabled dates
                }
            }
        });
    }
    //// get Data From Database
    private void loadUserData() {
        ObservableList<User_selecter_list> userList = FXCollections.observableArrayList();

        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = connectionNow.getConnection();
            String query = "SELECT Name, Gender, Details FROM users";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String gender = resultSet.getString("Gender");
                boolean details = resultSet.getBoolean("Details");


                User_selecter_list user = new User_selecter_list(name, details, gender);
                userList.add(user);
            }

            resultSet.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User_selecter_list.setItems(userList);
    }



}
