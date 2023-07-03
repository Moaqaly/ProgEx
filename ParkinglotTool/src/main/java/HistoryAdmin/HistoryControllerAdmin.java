package HistoryAdmin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import Home_page_admin.User_selecter_list;
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


public class HistoryControllerAdmin implements Initializable {

	ObservableList<HistoryDataAdmin> DataList = FXCollections.observableArrayList();
	ObservableList<HistoryDataAdmin> DataList2 = FXCollections.observableArrayList();
	ObservableList<HistoryDataAdmin> TDataList = FXCollections.observableArrayList();
	ObservableList<HistoryDataAdmin> TDataList2 = FXCollections.observableArrayList();


	@FXML
	private Button Back_Button;

	@FXML
	private TableView<HistoryDataAdmin> ReservationHistory;

	@FXML
	private TableColumn<HistoryDataAdmin, String> ParkingSpaceColumn;

	@FXML
	private TableColumn<HistoryDataAdmin, String> DateColumn;

	@FXML
	private TableColumn<HistoryDataAdmin, String> UsernameColumn;

	@FXML
	private TableView<HistoryDataAdmin> ReservationFuture;

	@FXML
	private TableColumn<HistoryDataAdmin, String> FDateColumn;

	@FXML
	private TableColumn<HistoryDataAdmin, String> FParkingSpaceColumn;


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

		ParkingSpaceColumn.setCellValueFactory(new PropertyValueFactory<HistoryDataAdmin, String>("ParkingSpace"));
		DateColumn.setCellValueFactory(new PropertyValueFactory<HistoryDataAdmin, String>("Date"));
		UsernameColumn.setCellValueFactory(new PropertyValueFactory<HistoryDataAdmin, String>("Username"));
		FParkingSpaceColumn.setCellValueFactory(new PropertyValueFactory<HistoryDataAdmin, String>("ParkingSpace"));
		FDateColumn.setCellValueFactory(new PropertyValueFactory<HistoryDataAdmin, String>("Date"));
		load();
	}

	public void load() {


		LocalDate ld = LocalDate.now();
		String localdate = ld.toString();
		String space = "leer";
		String date = "leer";
		int user;
		String username = "leer";
		Connection con = DatabaseConnection.getConnection(); // Use your existing method to get a database connection
		LocalDate currentDate = LocalDate.now();
		System.out.println("CurrentDate:" +currentDate);
		try(Statement stm = con.createStatement()) {
			String s = "SELECT * FROM reservation ";
			ResultSet rs = stm.executeQuery(s);
			while (rs.next()) {
				date = rs.getString("ReservationDate");
				space = rs.getString("SpaceID");
				user = rs.getInt("UserID");
				try(Statement stm2 = con.createStatement()){
					String h ="SELECT Username FROM users WHERE ID = " + user;
					ResultSet rs2 = stm2.executeQuery(h);
					while(rs2.next()) {
						username = rs2.getString("Username");
					}
					HistoryDataAdmin data = new HistoryDataAdmin(space, date, username);
					DataList.add(data);
					if(date.equals(localdate)) TDataList.add(data);
					if(user==getUserId(LoginController.un)) {
						DataList2.add(data);
						if(date.equals(localdate)) TDataList2.add(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		if(!DataList.isEmpty()) {
			ReservationHistory.setItems(DataList);
			ReservationFuture.setItems(DataList2);

		}


	}


	@FXML
	private void handletoday(){
		ReservationFuture.getItems().clear();
		ReservationHistory.getItems().clear();

		TDataList.clear();
		TDataList2.clear();
		DataList.clear();
		DataList2.clear();

		LocalDate ld = LocalDate.now();
		String localdate = ld.toString();
		String space = "leer";
		String date = "leer";
		int user;
		String username = "leer";
		Connection con = DatabaseConnection.getConnection(); // Use your existing method to get a database connection
		LocalDate currentDate = LocalDate.now();
		System.out.println("CurrentDate:" +currentDate);
		try(Statement stm = con.createStatement()) {
			String s = "SELECT * FROM reservation ";
			ResultSet rs = stm.executeQuery(s);
			while (rs.next()) {
				date = rs.getString("ReservationDate");
				space = rs.getString("SpaceID");
				user = rs.getInt("UserID");
				try(Statement stm2 = con.createStatement()){
					String h ="SELECT Username FROM users WHERE ID = " + user;
					ResultSet rs2 = stm2.executeQuery(h);
					while(rs2.next()) {
						username = rs2.getString("Username");
					}
					HistoryDataAdmin data = new HistoryDataAdmin(space, date, username);
					DataList.add(data);
					if(date.equals(localdate)) TDataList.add(data);
					if(user==getUserId(LoginController.un)) {
						DataList2.add(data);
						if(date.equals(localdate)) TDataList2.add(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		ReservationHistory.setItems(TDataList);
		ReservationFuture.setItems(TDataList2);

	}

	@FXML
	private void handleall() {
		ReservationFuture.getItems().clear();
		ReservationHistory.getItems().clear();

		TDataList.clear();
		TDataList2.clear();
		DataList.clear();
		DataList2.clear();

		LocalDate ld = LocalDate.now();
		String localdate = ld.toString();
		String space = "leer";
		String date = "leer";
		int user;
		String username = "leer";
		Connection con = DatabaseConnection.getConnection(); // Use your existing method to get a database connection
		LocalDate currentDate = LocalDate.now();
		System.out.println("CurrentDate:" +currentDate);
		try(Statement stm = con.createStatement()) {
			String s = "SELECT * FROM reservation ";
			ResultSet rs = stm.executeQuery(s);
			while (rs.next()) {
				date = rs.getString("ReservationDate");
				space = rs.getString("SpaceID");
				user = rs.getInt("UserID");
				try(Statement stm2 = con.createStatement()){
					String h ="SELECT Username FROM users WHERE ID = " + user;
					ResultSet rs2 = stm2.executeQuery(h);
					while(rs2.next()) {
						username = rs2.getString("Username");
					}
					HistoryDataAdmin data = new HistoryDataAdmin(space, date, username);
					DataList.add(data);
					if(date.equals(localdate)) TDataList.add(data);
					if(user==getUserId(LoginController.un)) {
						DataList2.add(data);
						if(date.equals(localdate)) TDataList2.add(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		ReservationHistory.setItems(DataList);
		ReservationFuture.setItems(DataList2);
	}



	@FXML
	private void handleBackButtonHistory() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_Admin.fxml"));
		Scene scene = new Scene(root);

		// Get the current stage (window)
		Stage currentStage = (Stage) Back_Button.getScene().getWindow();

		// Set the login page scene and show the stage
		currentStage.setScene(scene);
		currentStage.show();
	}

	@FXML
	private void handlefile() {
		try {
			writeToTextFile("reservations.txt", TDataList);


		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeToTextFile(String filename, ObservableList<HistoryDataAdmin> students)
			throws IOException {
		LocalDate ld = LocalDate.now();
		String localdate = ld.toString();
		FileWriter writer = new FileWriter(filename);
		writer.write("Reservations for " + localdate + ":\n");
		for (HistoryDataAdmin data : students) {
			writer.write(data.getUsername() + ", " + data.getParkingSpace() + "\n");
		}
		writer.close();
	}


	@FXML
	void Cancel_Booking(ActionEvent event) throws SQLException {
		HistoryDataAdmin selectedUser = ReservationHistory.getSelectionModel().getSelectedItem();
		HistoryDataAdmin selectedUser1 = ReservationFuture.getSelectionModel().getSelectedItem();




		if(!(selectedUser==null)){
			String username = selectedUser.getUsername();
			int userid =getUserId(username);
			String date = selectedUser.getDate();
			int spaceID = Integer.parseInt(selectedUser.getParkingSpace());
			cancelReservation(userid, LocalDate.parse(date),spaceID);
		} else if (!(selectedUser1==null)) {
			String date = selectedUser1.getDate();
			int spaceID = Integer.parseInt(selectedUser1.getParkingSpace());
			cancelReservation1(LocalDate.parse(date),spaceID);
		}else {
			showErrorAlert("Error","Please Select from the List!");
		}

		ReservationFuture.getItems().clear();
		ReservationHistory.getItems().clear();

		TDataList.clear();
		TDataList2.clear();
		DataList.clear();
		DataList2.clear();

		load();

	}

	//////////
	public static boolean cancelReservation(int userid, LocalDate reservationDate, int spaceID) {
		try (Connection connection = DatabaseConnection.getConnection();
			 PreparedStatement statement = connection.prepareStatement(
					 "DELETE FROM reservation WHERE UserID = ? AND ReservationDate = ? AND SpaceID = ?")
		) {
			statement.setInt(1, userid);
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

	public static boolean cancelReservation1(LocalDate reservationDate, int spaceID) {
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


}