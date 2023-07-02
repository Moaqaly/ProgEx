package Home_page_admin;

import Login.LoginController;
import Mysql.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class Home_Admin_Controller implements Initializable  {


    @FXML
    private Button Cancel_Booking;

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
    /////
    @FXML
    private DatePicker Date_Selecter;

    /////////////////////////
    @FXML
    private TableColumn<User_selecter_list, String> Column_Details;

    @FXML
    private TableColumn<User_selecter_list, String> Column_User;
    @FXML
    private TableColumn<Home_page_admin.User_selecter_list, Integer> Column_Typ;
    @FXML
    private TableColumn<User_selecter_list, String> Column_Gender;
    @FXML
    private TableView<User_selecter_list> User_selecter_list;
    /////////

    @FXML
    private Button Book_now_Button;

    //////////////////
    @FXML
    private TableView<info_List> info_List;
    @FXML
    private TableColumn<info_List, Integer> Parkinglot_Typ;

    @FXML
    private TableColumn<info_List, Integer> Parkinglot_nummber;


///////////////
@FXML
private Label User_lable;
    /////////////////////
@FXML
void Cancel_Booking(ActionEvent event) throws SQLException {
    User_selecter_list selectedUser = User_selecter_list.getSelectionModel().getSelectedItem();
    LocalDate date = Date_Selecter.getValue();
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

    // Get the user ID
    String username = selectedUser.getName_A(); // Assuming the User_selecter_list returns an object with a getUsername() method
    int userID = getUserId(username);

    cancelReservation(userID, date, spaceID);


}

//////////
public static boolean cancelReservation(int userID, LocalDate reservationDate, int spaceID) {
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "DELETE FROM reservation WHERE UserID = ? AND ReservationDate = ? AND SpaceID = ?")
    ) {
        statement.setInt(1, userID);
        statement.setDate(2, java.sql.Date.valueOf(reservationDate));
        statement.setInt(3, spaceID);

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            showInfoAlert("Cancellation Successful", "Booking has been canceled.");
            return true;
        } else {
            showErrorAlert("Cancellation Failed", "Unable to cancel the booking.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showErrorAlert("Error", "An error occurred while canceling the booking.");
    }
    return false;
}


////////

    @FXML
    private void Book_now_ButtonOnAction(ActionEvent event) throws SQLException {
        String parkingTypeName = "";

        User_selecter_list selectedUser = User_selecter_list.getSelectionModel().getSelectedItem();
        LocalDate date = Date_Selecter.getValue();
        LocalDate currentDate = LocalDate.now();




        LocalDate previousReservationDate = multiplereservation(getUserId(selectedUser.getName_A()));


        // Determine the parking type based on the selected button
        if (Parking00.isSelected() || Parking01.isSelected() || Parking02.isSelected() || Parking10.isSelected() || Parking11.isSelected() || Parking12.isSelected()) {
            parkingTypeName = "Standard";
        } else if (Parking20.isSelected() || Parking21.isSelected() || Parking22.isSelected()) {
            parkingTypeName = "Women";
        } else if (Parking30.isSelected() || Parking31.isSelected() || Parking32.isSelected()) {
            parkingTypeName = "Parking lot for disabled";
        }

        // get the user id
        int id = getUserId(selectedUser.getName_A());
        int userID = getUserId(selectedUser.getName_A());

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

        if (selectedUser == null || date == null) {
            showErrorAlert("ERROR", "Please fill all the requested fields.");
        } else if (!selectedUser.getDetails_A() && parkingTypeName.equals("Parking lot for disabled")) {
            showErrorAlert("Error!", "This " + selectedUser.getName_A() + " is not disabled and cannot reserve this parking type.");
        } else if (Objects.equals(selectedUser.getGender_A(), "male") && parkingTypeName.equals("Women")) {
            showErrorAlert("Error", "This parking can't be reserved for " + selectedUser.getName_A() + ". It is for women only!");
        } else if (!date.isEqual(currentDate) && !date.isEqual(currentDate.plusDays(1))) {
            showErrorAlert("ERROR", "The date must be today or tomorrow!");
        }else if (selectedUser.getTyp().equals("User") && previousReservationDate != null && (date.equals(previousReservationDate) || date.isEqual(currentDate.plusDays(1))) ) {
            showErrorAlert("ERROR", "Only one reservation per day is allowed.");
        }else if (!isAvailable) {
            showErrorAlert("Error","The Parking lot is not available!");
        } else if(hasReservationOnDate(userID, date)&& selectedUser.getTyp()=="User") {
            showErrorAlert("Error", "You have already made a reservation for the selected date.");

        }else {
            // Insert the reservation into the database
            boolean reservationInserted = insertReservation(id, spaceID, date);
            if (reservationInserted) {
                showInfoAlert("Success", "Reservation saved successfully.");
            } else {
                showErrorAlert("Error", "Failed to make the reservation.");
            }
        }
    }
///////////////
public static boolean hasReservationOnDate(int userId, LocalDate date) throws SQLException {
    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement statement = connection.prepareStatement("SELECT ReservationId FROM reservation WHERE UserID = ? AND ReservationDate = ?");
    statement.setInt(1, userId);
    statement.setDate(2, java.sql.Date.valueOf(date));
    ResultSet resultSet = statement.executeQuery();

    return resultSet.next(); // Returns true if a reservation is found for the given user and date
}
    ///////////////
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

        return null; // No previous reservation found on the same day
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
    public static int getUserId(String name) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT ID FROM users WHERE Name = '" + name + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("ID");
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


    //////////////////////////////////////////////
      ////////////////////////////////////////////////////
        @FXML
        private void handleAddUser(ActionEvent event) throws IOException{
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Add_User.fxml"));
            Scene scene = new Scene(root);
            Stage crrentStage = (Stage) Add_User_Button.getScene().getWindow();
            crrentStage.setScene(scene);
            crrentStage.show();
        }
    ///////////////////////////////////////
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
    int spaceID = 0;
    @FXML
    void Parking00(ActionEvent event) {
        if (Parking00.isSelected()) {
            spaceID = 1;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);

    }

    @FXML
    void Parking01(ActionEvent event) {
        if (Parking01.isSelected()) {
            spaceID = 2;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking02(ActionEvent event) {
        if (Parking02.isSelected()) {
            spaceID = 3;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking10(ActionEvent event) {
        if (Parking10.isSelected()) {
            spaceID = 4;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking11(ActionEvent event) {
        if (Parking11.isSelected()) {
            spaceID = 5;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking12(ActionEvent event) {
        if (Parking12.isSelected()) {
            spaceID = 6;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking20(ActionEvent event) {
        if (Parking20.isSelected()) {
            spaceID = 7;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking21(ActionEvent event) {
        if (Parking21.isSelected()) {
            spaceID = 8;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking22(ActionEvent event) {
        if (Parking22.isSelected()) {
            spaceID = 9;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking30(ActionEvent event) {
        if (Parking30.isSelected()) {
            spaceID = 10;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking31(ActionEvent event) {
        if (Parking31.isSelected()) {
            spaceID = 11;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

    @FXML
    void Parking32(ActionEvent event) {
        if (Parking32.isSelected()) {
            spaceID = 12;
        }
        Parkinglot_nummber.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_nummber"));
        Parkinglot_Typ.setCellValueFactory(new PropertyValueFactory<info_List,Integer>("Parkinglot_Typ"));
        loadParkingData(spaceID);
    }

        // to make SUNDAY and SATURDAY not selectable


///////////////////////////     initialize /////////////////////////////
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Column_User.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("name_A"));
        Column_Details.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("details_A"));
        Column_Gender.setCellValueFactory(new PropertyValueFactory<User_selecter_list,String>("Gender_A"));
        Column_Typ.setCellValueFactory(new PropertyValueFactory<User_selecter_list,Integer>("typ"));
        loadUserData();
        /////////
        String username = LoginController.un;
        User_lable.setText(username);
        /////////////
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
            String query = "SELECT Name, Gender, Details,UserTypeID FROM users";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String gender = resultSet.getString("Gender");
                boolean details = resultSet.getBoolean("Details");
                int typ = resultSet.getInt("UserTypeID");
                String userTypeID=null;
                if (typ == 1){
                    userTypeID = "Admin";
                }else if (typ == 2){userTypeID = "User";}

                User_selecter_list user = new User_selecter_list(name, details, gender,userTypeID);
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
    /////////////////

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



}
