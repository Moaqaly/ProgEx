package Add_User;

import Mysql.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class Add_User_Controller implements Initializable {



    @FXML
    private Button Search_button;

    @FXML
    private TextField Search_text;

    // Column in the Table users
    @FXML
    private TableView<tableUser> User_List;

    @FXML
    private TableColumn<tableUser, Enum> Column_Disability;

    @FXML
    private TableColumn<tableUser, String> Column_Email;

    @FXML
    private TableColumn<tableUser, String> Column_Gender;

    @FXML
    private TableColumn<tableUser, Integer> Column_ID;

    @FXML
    private TableColumn<tableUser, String> Column_Name;

    @FXML
    private TableColumn<tableUser, Integer> Column_Role;

    @FXML
    private TableColumn<tableUser, String> Column_Username;


    //////////// the buttons in the Add_user page
    @FXML
    private Button Back_Button_add_User;

    // textfield and password field
    @FXML
    private TextField Email_Field;

    @FXML
    private TextField Name_Field;
    @FXML
    private TextField User_Name_Field;

    @FXML
    private PasswordField Password_Field;


    @FXML
    private PasswordField rePassword_Field;



    /////////////////////////////////
    @FXML
    private Button register_Button;
    @FXML
    private Button edit_User_Button;

    @FXML
    private Button remove_User_Button;

    @FXML
    private Button refresh_Button;

    // choice box
    @FXML
    private ChoiceBox<String> Choicebox_Gender;

    @FXML
    private ChoiceBox<String> Choicebox_Role;

    @FXML
    private ToggleButton Disability_Button;
    ///////////////////////////////////////////////////////////////////////

//////////////////////////////////////
private ObservableList<tableUser> originalUserList; // Declare a variable to store the original user list

    @FXML
    void handle_Search(ActionEvent event) {
        String searchText = Search_text.getText(); // Get the search text from the text field

        List<tableUser> searchResults = new ArrayList<>(); // Create a list to store the search results

        // Retrieve the data from the User_List TableView
        ObservableList<tableUser> userList = User_List.getItems();

        if (originalUserList == null) {
            // Store the original user list if it hasn't been saved before
            originalUserList = FXCollections.observableArrayList(userList);
        }

        // Iterate over the user list and check if any user matches the search text
        for (tableUser user : userList) {
            if (user.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                    user.getUsername().toLowerCase().contains(searchText.toLowerCase()) || user.getGender().contains(searchText.toLowerCase())
                    || Integer.toString(user.getId()).contains(searchText.toLowerCase()) ) {
                searchResults.add(user); // Add the matching user to the search results list
            }
        }

        // Create a new ObservableList with the search results
        ObservableList<tableUser> searchResultsList = FXCollections.observableArrayList(searchResults);

        // Update the data in the User_List TableView
        User_List.setItems(searchResultsList);
    }

    @FXML
    void handle_Refresh_Button(ActionEvent event) {
        loadUserData();
    }






    /////////////////////////////////////////////////
    // remove a user
    @FXML
    void handle_Remove_Button(ActionEvent event) {
        // Get the selected user from the table
        tableUser selectedUser = User_List.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Display a confirmation dialog to confirm the deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete this user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed the deletion, remove the user from the database
                boolean success = removeUserFromDatabase(selectedUser.getId());

                if (success) {
                    // User removed successfully, remove the user from the table
                    User_List.getItems().remove(selectedUser);

                    showInfoAlert( "Success", "User removed successfully.");
                } else {
                    showErrorAlert( "Error", "Failed to remove the user. It is not possible to delete a user with an active reservation!");
                }
            }
        } else {
            showErrorAlert( "Warning", "No user selected.");
        }
    }

    private boolean removeUserFromDatabase(int userId) {
        try {
            Connection connection = DatabaseConnection.getConnection(); // Use your existing method to get a database connection
            String sql = "DELETE FROM users WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            connection.close();
            return rowsAffected > 0; // Return true if at least one row was affected (user deleted)
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurred
        }
    }
    /////////////////////////////////////////////////////
    ////////////////////////// Edit user ///////////////////////////////////////////////
    @FXML
    void handle_Edit_User_Button(ActionEvent event) {
        // Get the selected user from the table
        tableUser selectedUser = User_List.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Set the user information in the text fields, choice boxes, and toggle button
            Name_Field.setText(selectedUser.getName());
            Email_Field.setText(selectedUser.getEmail());
            User_Name_Field.setText(selectedUser.getUsername());
            Password_Field.setText(""); // Clear the password field for security reasons
            rePassword_Field.setText(""); // Clear the re-enter password field for security reasons
            Choicebox_Gender.setValue(selectedUser.getGender());
            Choicebox_Role.setValue(selectedUser.getUserTypeID());
            Disability_Button.setSelected(selectedUser.isDetails());

            // Change the register button text to "Save"
            register_Button.setText("Save");
        } else {
            showErrorAlert("Warning", "No user selected.");
        }


    }
    /////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    ////////////////////////// Registering Process ///////////////////////////////////////////


    @FXML
    void handle_Register_Button(ActionEvent event) {
        tableUser selectedUser = User_List.getSelectionModel().getSelectedItem();

        String buttonText = register_Button.getText();
        String name = Name_Field.getText().trim();
        String email = Email_Field.getText().trim();
        String username = User_Name_Field.getText().trim();
        String password = Password_Field.getText().trim();
        String rePassword = rePassword_Field.getText().trim();
        String gender = Choicebox_Gender.getValue();
        String role = Choicebox_Role.getValue();
        boolean isDisabled = Disability_Button.isSelected();
        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if (buttonText.equals("Save")) {
            // Update the user information in the database
            String query = "UPDATE users SET Name = ?, Email = ?, Username = ?, Password = ?, Gender = ?, Details = ?, UserTypeID = ? WHERE id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set the values for the prepared statement
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, username);

                statement.setString(4, hashedPassword);
                statement.setString(5, gender);
                boolean disable;
                int Role = 0;
                if (Disability_Button.isSelected()) {
                    disable = true;
                } else {
                    disable = false;
                }
                if (Choicebox_Role.getValue() == "Admin") {
                    Role = 1;
                } else if (Choicebox_Role.getValue() == "User") {
                    Role = 2;
                }
                statement.setBoolean(6, disable);
                statement.setString(7, String.valueOf(Role));
                statement.setInt(8, selectedUser.getId());

                // Execute the update query
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    showInfoAlert("Success", "User information updated.");
                    clearFields();
                } else {
                    showErrorAlert("Error", "Failed to update user information.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Error", "Database error occurred. Please try again.");
            }

            register_Button.setText("Register"); // Change the button text back to "Register"



    } else {
            // Registration process



            // Check if all required fields are filled
            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || rePassword.isEmpty() || gender == null || role == null) {
                showErrorAlert("Error", "Please fill in all the required fields.");
                return;
            }

            // Check if the password and re-entered password match
            if (!password.equals(rePassword)) {
                showErrorAlert("Error", "The passwords do not match. Please re-enter the password.");
                return;
            }

            // Check if the email format is valid
            if (!isValidEmail(email)) {
                showErrorAlert("Error", "Please enter a valid email address.");
                return;
            }

            // Check if the username already exists
            if (isUsernameExists(username)) {
                showErrorAlert("Error", "The username is already registered. Please choose a different username.");
                return;
            }

            // Check if the user already exists with the same info
            if (isUserExists(name, email, username)) {
                showErrorAlert("Error", "There is already a user with the same information. Please enter different information.");
                return;
            }

            // Add the user to the database
            if (addUserToDatabase(name, email, username, password, gender, isDisabled, role)) {
                showInfoAlert("Success", "User successfully registered.");
                clearFields();
            } else {
                showErrorAlert("Error", "Failed to register user. Please try again.");
            }
        }
    }

    ///////////
    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        // You can use regular expressions or any other validation method
        // Return true if the email is valid, false otherwise
        // For example:
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    }

    private boolean isUsernameExists(String username) {
        // Implement your logic to check if the username already exists in the database
        // Perform the necessary database query and return true if the username exists, false otherwise
        // For example:
        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            boolean exists = resultSet.next();
            resultSet.close();
            statement.close();
            connectionDB.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUserExists(String name, String email, String username) {
        // Implement your logic to check if a user with the same info already exists in the database
        // Perform the necessary database query and return true if a user exists with the same info, false otherwise
        // For example:
        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE name = '" + name + "' AND email = '" + email + "' AND username = '" + username + "'";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            boolean exists = resultSet.next();
            resultSet.close();
            statement.close();
            connectionDB.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean addUserToDatabase(String name, String email, String username, String password, String gender, boolean isDisabled, String role) {

        // Implement your logic to add the user to the database
        // Perform the necessary database query to insert the user data
        // Return true if the user is successfully added, false otherwise
        // For example:
        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = connectionNow.getConnection();

            // Check if a user with the same password already exists
            if (isUserExistsWithPassword(password)) {
                showErrorAlert("Error", "A user with the same password already exists.");
                connectionDB.close();
                return false;
            }

            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // insert the data in the database
            String query = "INSERT INTO users (Name, Email, Username, Password, Gender, Details, isAdmin, UserTypeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connectionDB.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setString(5, gender);
            preparedStatement.setInt(6, isDisabled ? 1 : 0);
            preparedStatement.setInt(7, role.equals("Admin") ? 1 : 0);

            int Role1 = 0;
            if (Choicebox_Role.getValue() == "Admin") { Role1= 1;}else if(Choicebox_Role.getValue() == "User") {Role1=2;}

            preparedStatement.setInt(8, Role1);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connectionDB.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isUserExistsWithPassword(String password) {

        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = connectionNow.getConnection();

            String query = "SELECT * FROM users WHERE Password = ?";
            PreparedStatement preparedStatement = connectionDB.prepareStatement(query);
            preparedStatement.setString(1, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = resultSet.next();

            resultSet.close();
            preparedStatement.close();
            connectionDB.close();

            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    private void clearFields() {
        Name_Field.clear();
        Email_Field.clear();
        User_Name_Field.clear();
        Password_Field.clear();
        rePassword_Field.clear();
        Choicebox_Gender.getSelectionModel().clearSelection();
        Choicebox_Role.getSelectionModel().clearSelection();
        Disability_Button.setSelected(false);
    }






    ////////////////////////////////////////////////////////////////
    // choice box
    private String[] genders = {"Male", "Female"};
    private String[] roles = {"User", "Admin"};

    // Toggle button Disable

    @FXML
    void handle_disability_Button(ActionEvent event) {
        boolean isDisabled = Disability_Button.isSelected();

        // Perform actions based on the toggle state
        if (isDisabled) {
            // Person is disabled
            System.out.println("Person is disabled");
        } else {
            // Person is not disabled
            System.out.println("Person is not disabled");
        }
    }



    ////////////////////////// initialize ////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ChoiceBoxs
        Choicebox_Gender.getItems().addAll(genders);
        Choicebox_Role.getItems().addAll(roles);

        // Initialize the table columns
        Column_ID.setCellValueFactory(new PropertyValueFactory<tableUser, Integer>("id"));
        Column_Name.setCellValueFactory(new PropertyValueFactory<tableUser, String>("name"));
        Column_Email.setCellValueFactory(new PropertyValueFactory<tableUser, String>("email"));
        Column_Username.setCellValueFactory(new PropertyValueFactory<tableUser, String>("username"));
        Column_Gender.setCellValueFactory(new PropertyValueFactory<tableUser, String>("gender"));
        Column_Disability.setCellValueFactory(new PropertyValueFactory<tableUser, Enum>("details"));
        Column_Role.setCellValueFactory(new PropertyValueFactory<tableUser, Integer>("userTypeID"));

        // Populate the table with data from the database
        loadUserData();
    }


    //// get Data From Database
    private void loadUserData() {
        ObservableList<tableUser> userList = FXCollections.observableArrayList();

        try {
            DatabaseConnection connectionNow = new DatabaseConnection();
            Connection connectionDB = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users";
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                boolean details = resultSet.getBoolean("details");
                int userTypeID = resultSet.getInt("userTypeID");
                String typ=null;
                if (userTypeID == 1){
                    typ = "Admin";
                }else if (userTypeID == 2){typ = "User";}

                tableUser user = new tableUser(id, name, email, username, gender, details, typ);
                userList.add(user);
            }

            resultSet.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User_List.setItems(userList);
    }




    @FXML
    private void handleBackButtonAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_Admin.fxml"));
        Scene scene = new Scene(root);
        Stage crrentStage = (Stage) Back_Button_add_User.getScene().getWindow();
        crrentStage.setScene(scene);
        crrentStage.show();
    }

}
