package Home_page_user;

import Home_page_admin.User_selecter_list;
import Home_page_admin.info_List;
import Login.LoginController;
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
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private Button Book_now_Button;
    ////////////////////
    @FXML
    private TableView<Home_page_admin.info_List> info_List;
    @FXML
    private TableColumn<info_List, Integer> Parkinglot_Typ;

    @FXML
    private TableColumn<info_List, Integer> Parkinglot_nummber;

    ////////////////
    String Logedinuser = LoginController.un;
    /////////////////
    @FXML
    private Label User_lable;
    String usernamelable = LoginController.un;

    ///////////
    @FXML
    void Date_Selecter(ActionEvent event) {
        LocalDate selectedDate = Date_Selecter.getValue();

        if (selectedDate != null) {
            // Enable all buttons initially
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

            try {
                // Fetch the reserved parking lots for the selected date
                List<Integer> reservedParkingLots = getReservedParkingLots(selectedDate);

                // Disable the buttons for the reserved parking lots
                for (Integer spaceID : reservedParkingLots) {
                    disableParkingButton(spaceID);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Error", "Failed to fetch reservation data.");
            }
        }
    }

    public List<Integer> getReservedParkingLots(LocalDate date) throws SQLException {
        List<Integer> reservedParkingLots = new ArrayList<>();

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT SpaceID FROM reservation WHERE ReservationDate = ?");
        statement.setDate(1, java.sql.Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int spaceID = resultSet.getInt("SpaceID");
            reservedParkingLots.add(spaceID);
        }

        return reservedParkingLots;
    }

    public void disableParkingButton(int spaceID) {
        switch (spaceID) {
            case 1:
                Parking00.setDisable(true);
                break;
            case 2:
                Parking01.setDisable(true);
                break;
            case 3:
                Parking02.setDisable(true);
                break;
            case 4:
                Parking10.setDisable(true);
                break;
            case 5:
                Parking11.setDisable(true);
                break;
            case 6:
                Parking12.setDisable(true);
                break;
            case 7:
                Parking20.setDisable(true);
                break;
            case 8:
                Parking21.setDisable(true);
                break;
            case 9:
                Parking22.setDisable(true);
                break;
            case 10:
                Parking30.setDisable(true);
                break;
            case 11:
                Parking31.setDisable(true);
                break;
            case 12:
                Parking32.setDisable(true);
                break;
            default:
                break;
        }
    }
    /////////////////////

    @FXML
    private void HandleHistory() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/History.fxml"));
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage currentStage = (Stage) Logout_Button.getScene().getWindow();

            // Set the login page scene and show the stage
            currentStage.setScene(scene);
            currentStage.show();
        }
        catch(Exception e) {
            showErrorAlert("ERROR"," !!");
        }
    }



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
        } else if (getusertyp(Logedinuser)==2 && previousReservationDate != null && date.equals(previousReservationDate) ) {
            showErrorAlert("ERROR", "Only one reservation per day is allowed.");
        }
        else if (!isAvailable) {
            showErrorAlert("Error","The Parking lot is not available!");
        } else if(hasReservationOnDate(id, date)&& getusertyp(Logedinuser)==2) {
            showErrorAlert("Error", "You have already made a reservation for the selected date.");

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
public static boolean hasReservationOnDate(int userId, LocalDate date) throws SQLException {
    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement statement = connection.prepareStatement("SELECT ReservationId FROM reservation WHERE UserID = ? AND ReservationDate = ?");
    statement.setInt(1, userId);
    statement.setDate(2, java.sql.Date.valueOf(date));
    ResultSet resultSet = statement.executeQuery();

    return resultSet.next(); // Returns true if a reservation is found for the given user and date
}
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
    public static int getusertyp(String username) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT UserTypeID FROM users WHERE Username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("UserTypeID");
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

    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private static void showInfoAlert(String title, String message) {
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
        User_lable.setText(usernamelable);
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

    private void loadParkingData(int spaceid) {
        ObservableList<info_List> parkingList = FXCollections.observableArrayList();
        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = connectionNow.getConnection();
            String query = "SELECT SpaceId, ParkingsTypeID FROM parkingslot WHERE SpaceId = '" + spaceid + "'";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int SpaceId = resultSet.getInt("SpaceId");
                int typ = resultSet.getInt("ParkingsTypeID");
                String parkingType="";
                if (typ == 1){parkingType = "Standard";}
                else if (typ == 2){parkingType = "Women";}
                else if (typ == 3) {parkingType="Special needs";}

                info_List user = new info_List(SpaceId,parkingType);
                parkingList.add(user);
            }

            resultSet.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        info_List.setItems(parkingList);

    }

    int spaceID = 0;
    @FXML
    void Parking00user(ActionEvent event) {
        if (Parking00.isSelected()) {
            spaceID = 1;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);

    }
    @FXML
    void Parking01user(ActionEvent event) {
        if (Parking01.isSelected()) {
            spaceID = 2;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking02user(ActionEvent event) {
        if (Parking02.isSelected()) {
            spaceID = 3;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking10user(ActionEvent event) {
        if (Parking10.isSelected()) {
            spaceID = 4;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking11user(ActionEvent event) {
        if (Parking11.isSelected()) {
            spaceID = 5;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking12user(ActionEvent event) {
        if (Parking12.isSelected()) {
            spaceID = 6;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking20user(ActionEvent event) {
        if (Parking20.isSelected()) {
            spaceID = 7;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking21user(ActionEvent event) {
        if (Parking21.isSelected()) {
            spaceID = 8;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking22user(ActionEvent event) {
        if (Parking22.isSelected()) {
            spaceID = 9;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking30user(ActionEvent event) {
        if (Parking30.isSelected()) {
            spaceID = 10;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking31user(ActionEvent event) {
        if (Parking31.isSelected()) {
            spaceID = 11;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking32user(ActionEvent event) {
        if (Parking32.isSelected()) {
            spaceID = 12;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }


}

