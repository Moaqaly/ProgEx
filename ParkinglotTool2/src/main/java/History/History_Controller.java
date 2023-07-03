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
	
	private void load() {
		ObservableList<HistoryData> DataList = FXCollections.observableArrayList();
		ObservableList<HistoryData> TDataList = FXCollections.observableArrayList();
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
