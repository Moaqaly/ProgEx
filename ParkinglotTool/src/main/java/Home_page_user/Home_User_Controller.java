package Home_page_user;

import Home_page_admin.User_selecter_list;
import Login.LoginController;
import Mysql.DatabaseConnection;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Home_User_Controller implements Initializable  {
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
    /////
    @FXML
    private DatePicker Date_Selecter;

    @FXML
    private TableView<User_selecter_list> User_selecter_list;
    @FXML
    private Label labelBook; // just for testing
       @FXML
       private Button Book_now_Button;




    String Logedinuser = LoginController.un;



    @FXML
    private void Book_now_ButtonOnAction(ActionEvent event) throws SQLException {

        String parkingTypeName = "";


        LocalDate date = Date_Selecter.getValue();
        LocalDate currentDate = LocalDate.now();


        int logedin_userid =getUserId(Logedinuser);

        LocalDate previousReservationDate = multiplereservation(getUserId(Logedinuser));


        // Determine the parking type based on the selected button
        if (Parking00.isSelected() || Parking01.isSelected() || Parking02.isSelected() || Parking10.isSelected() || Parking11.isSelected() || Parking12.isSelected()) {
            parkingTypeName = "Standard";
        } else if (Parking20.isSelected() || Parking21.isSelected() || Parking22.isSelected()) {
            parkingTypeName = "Women";
        } else if (Parking30.isSelected() || Parking31.isSelected() || Parking32.isSelected()) {
            parkingTypeName = "Parking lot for disabled";
        }

        // get the user id
        int id = getUserId(Logedinuser);
        System.out.println(" id of the user "+ id);
        // Get the spaceID based on the selected parking button
        int spaceID = 0;
        if (Parking00.isSelected()) {
            spaceID = 1;
        } else if (Parking01.isSelected()) {
            spaceID = 2;
        } else if (Parking02.isSelected()) {
            spaceID = 3;
        } else if (Parking10.isSelected()) {
            spaceID = 4;
        } else if (Parking11.isSelected()) {
            spaceID = 5;
        } else if (Parking12.isSelected()) {
            spaceID = 6;
        } else if (Parking20.isSelected()) {
            spaceID = 7;
        } else if (Parking21.isSelected()) {
            spaceID = 8;
        } else if (Parking22.isSelected()) {
            spaceID = 9;
        } else if (Parking30.isSelected()) {
            spaceID = 10;
        } else if (Parking31.isSelected()) {
            spaceID = 11;
        } else if (Parking32.isSelected()) {
            spaceID = 12;
        }

        boolean isAvailable = availability(spaceID,date );

        if (Logedinuser == null || date == null) {
            showErrorAlert("ERROR", "Please fill all the requested fields.");
        } else if (getDetails(Logedinuser) == 0 && parkingTypeName.equals("Parking lot for disabled")) {
            showErrorAlert("Error!", "This " + Logedinuser + " is not disabled and cannot reserve this parking type.");
        } else if (Objects.equals(getGender(Logedinuser), "male") && parkingTypeName.equals("Women")) {
            showErrorAlert("Error", "This parking can't be reserved for " + Logedinuser + ". It is for women only!");
        } else if (!date.equals(currentDate)) {
            showErrorAlert("ERROR", "The date must be today!");
        }else if (gettyp(Logedinuser) == 2 && previousReservationDate != null && date.equals(previousReservationDate)) {
            showErrorAlert("ERROR", "Only one reservation per day is allowed.");
        }else if (!isAvailable) {
            showErrorAlert("Error","The Parking lot is not available!");
        }
        else {
            // Insert the reservation into the database
            boolean reservationInserted = insertReservation(id, spaceID, date);
            if (reservationInserted) {
                showInfoAlert("Success", "Reservation saved successfully.");
            } else {
                showErrorAlert("Error", "Failed to make the reservation.");
            }
        }
    }
///////////////////

    ////////
    public static LocalDate multiplereservation(int userId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT ReservationDate FROM reservation WHERE UserID = ?");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        LocalDate currentDate = LocalDate.now();
        while (resultSet.next()) {
            Date reservationDate = resultSet.getDate("ReservationDate");
            LocalDate localReservationDate = reservationDate.toLocalDate();
            if (localReservationDate.equals(currentDate)) {
                return localReservationDate;
            }
        }

        // No previous reservation on the current date
        return null;
    }
    ////////
    public static boolean insertReservation(int userID, int spaceID, LocalDate reservationDate) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO reservation (UserID, SpaceID, ReservationDate) VALUES (?, ?, ?)")
        ) {
            statement.setInt(1, userID);
            System.out.println("userID : "+ userID);
            statement.setInt(2, spaceID);
            System.out.println("spaceID : "+ spaceID);
            statement.setDate(3, java.sql.Date.valueOf(reservationDate));


            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // check the availability of the parkinglot
    public static boolean availability(int spaceID, LocalDate reservationDate) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservation WHERE SpaceID = ? AND ReservationDate = ?");
        statement.setInt(1, spaceID);
        statement.setDate(2, Date.valueOf(reservationDate));
        ResultSet resultSet = statement.executeQuery();

        return !resultSet.next(); // If resultSet has no rows, it means the space is available on the given date.
    }

    // Get the user ID
    public static int getUserId(String username) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM users WHERE Username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("ID");
        }

        return -1;
    }

    public static int getDetails(String username)throws SQLException{
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT Details FROM users WHERE Username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("Details");
        }

        return -1;
    }

    public static String getGender(String username)throws SQLException{
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT Gender FROM users WHERE Username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getString("Gender");
        }

        return null;
    }

    public static int gettyp(String username)throws SQLException{
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT UserTypeID FROM users WHERE Username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("UserTypeID");
        }

        return -1;
    }

////////////////////////////////////////////////////////

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    //////////////////////////////////
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
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


            System.out.println("this is the logedin user "+Logedinuser);


        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // No button is selected
                // Handle the deselection or do nothing
            } else {
                // A button is selected
                ToggleButton selectedButton = (ToggleButton) newValue;
                // Perform actions based on the selected button
                if (selectedButton == Parking00) {
                    // Handle the selection of Parking00 button
                } else if (selectedButton == Parking01) {
                    // Handle the selection of Parking01 button
                } else if (selectedButton == Parking02) {
                    // Handle the selection of Parking02 button
                } else if (selectedButton == Parking10) {
                    // Handle the selection of Parking10 button
                } else if (selectedButton == Parking11) {
                    // Handle the selection of Parking11 button
                } else if (selectedButton == Parking12) {
                    // Handle the selection of Parking12 button
                } else if (selectedButton == Parking20) {
                    // Handle the selection of Parking20 button
                } else if (selectedButton == Parking21) {
                    // Handle the selection of Parking21 button
                } else if (selectedButton == Parking22) {
                    // Handle the selection of Parking22 button
                } else if (selectedButton == Parking30) {
                    // Handle the selection of Parking30 button
                } else if (selectedButton == Parking31) {
                    // Handle the selection of Parking31 button
                } else if (selectedButton == Parking32) {
                    // Handle the selection of Parking32 button
                }
            }
        });


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


}

