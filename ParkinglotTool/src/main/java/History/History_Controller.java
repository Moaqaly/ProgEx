package History;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import Add_User.tableUser;
import HistoryAdmin.HistoryDataAdmin;
import Login.LoginController;
import Mysql.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import static Home_page_admin.Home_Admin_Controller.showErrorAlert;
import static Home_page_admin.Home_Admin_Controller.showInfoAlert;

public class History_Controller implements Initializable {
	@FXML
	private Button Back_Button;
	
	@FXML
    private TableView<HistoryData> ReservationHistory;
    
    @FXML
    private TableColumn<HistoryData, String> ParkingSpaceColumn; 
    
    @FXML
    private TableColumn<HistoryData, String> DateColumn;
    
    @FXML
    private TableView<HistoryData> ReservationToday;
    
    @FXML
    private TableColumn<HistoryData, String> TParkingSpaceColumn; 
    
    @FXML
    private TableColumn<HistoryData, String> TDateColumn;
    
 

    
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
    
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ParkingSpaceColumn.setCellValueFactory(new PropertyValueFactory<HistoryData, String>("ParkingSpace"));
		DateColumn.setCellValueFactory(new PropertyValueFactory<HistoryData, String>("Date"));
		TParkingSpaceColumn.setCellValueFactory(new PropertyValueFactory<HistoryData, String>("ParkingSpace"));
		TDateColumn.setCellValueFactory(new PropertyValueFactory<HistoryData, String>("Date"));
	    load();
	}
	ObservableList<HistoryData> DataList = FXCollections.observableArrayList();
	ObservableList<HistoryData> TDataList = FXCollections.observableArrayList();
	private void load() {

		String space = "leer";
	    String date = "leer";
	    LocalDate ld = LocalDate.now();
	    String localdate = ld.toString();
	    System.out.println(localdate);
		Connection con = DatabaseConnection.getConnection(); // Use your existing method to get a database connection
		try(Statement stm = con.createStatement()) {
            String s = "SELECT * FROM reservation WHERE UserID = '" + getUserId(LoginController.un) + "'";
            ResultSet rs = stm.executeQuery(s);
            while (rs.next()) {
            	date = rs.getString("ReservationDate");
            	space = rs.getString("SpaceID");
            	HistoryData data = new HistoryData(space, date);
        		DataList.add(data);
        		if(date.equals(localdate)) TDataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
		
		
		ReservationHistory.setItems(DataList);
		ReservationToday.setItems(TDataList);
		
	}
	//////////////////////
	@FXML
	void Cancel_Booking(ActionEvent event) throws SQLException {
		HistoryData selectedUser = ReservationHistory.getSelectionModel().getSelectedItem();
		HistoryData selectedUser1 = ReservationToday.getSelectionModel().getSelectedItem();

		if (!(selectedUser ==null)){
			String date = selectedUser.getDate();
			int spaceID = Integer.parseInt(selectedUser.getParkingSpace());
			cancelReservation(LocalDate.parse(date),spaceID);
		}else if (!(selectedUser1 ==null)) {
			String date = selectedUser1.getDate();
			int spaceID = Integer.parseInt(selectedUser1.getParkingSpace());
			cancelReservation(LocalDate.parse(date),spaceID);
		}else {
			showErrorAlert("Error","Please Select from the List!");
		}

		ReservationToday.getItems().clear();
		ReservationHistory.getItems().clear();

		TDataList.clear();

		DataList.clear();


		load();

	}

	//////////
	public static boolean cancelReservation(LocalDate reservationDate, int spaceID) {
		try (Connection connection = DatabaseConnection.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "DELETE FROM reservation WHERE  ReservationDate = ? AND SpaceID = ?")
		) {

			statement.setDate(1, java.sql.Date.valueOf(reservationDate));
			statement.setInt(2, spaceID);

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

	///////////////
	
	
	
	
	@FXML
	private void handleBackButtonHistory() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_User.fxml"));
        Scene scene = new Scene(root);

        // Get the current stage (window)
        Stage currentStage = (Stage) Back_Button.getScene().getWindow();

        // Set the login page scene and show the stage
        currentStage.setScene(scene);
        currentStage.show();
	}
	
}
