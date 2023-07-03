package HistoryAdmin;

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
import Login.LoginController;
import Mysql.DatabaseConnection;
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


public class HistoryControllerAdmin implements Initializable {
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
		ObservableList<HistoryDataAdmin> DataList = FXCollections.observableArrayList();
		ObservableList<HistoryDataAdmin> DataList2 = FXCollections.observableArrayList();
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
            	System.out.println("Username: " + username);
            	HistoryDataAdmin data = new HistoryDataAdmin(space, date, username);
        		DataList.add(data);
        		if(user==getUserId(LoginController.un)) {
        			DataList2.add(data);
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
	private void handleBackButtonHistory() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_Admin.fxml"));
        Scene scene = new Scene(root);

        // Get the current stage (window)
        Stage currentStage = (Stage) Back_Button.getScene().getWindow();

        // Set the login page scene and show the stage
        currentStage.setScene(scene);
        currentStage.show();
	}
	
	
	
}
