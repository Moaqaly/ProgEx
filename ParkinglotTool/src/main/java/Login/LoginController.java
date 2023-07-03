package Login;

import Home_page_admin.info_List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import Mysql.DatabaseConnection;

public class LoginController {


    public Button LoginButton;
    public Label LoginMassege;
    public Button CloseButton;

    @FXML
    public  TextField UsernameTextfeld;

    @FXML
    private TextField PasswordTextfeld;

    @FXML
    private Label error_label;

    //////////

/////////////



/////////
   public static String un = "leer";
    public void LoginButtonOnAction(ActionEvent e) throws IOException {
        if(UsernameTextfeld.getText().isEmpty() || PasswordTextfeld.getText().isEmpty()) {error_label.setText("Please fill out all fields");}
        else if (UsernameTextfeld.getText().equals("Admin") && PasswordTextfeld.getText().equals("Admin")) {
            System.out.println("Admin angemeldet");
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_Admin.fxml"));
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage currentStage = (Stage) LoginButton.getScene().getWindow();

            // Set the login page scene and show the stage
            currentStage.setScene(scene);
            currentStage.show();

        } else if (UsernameTextfeld.getText().equals("User") && PasswordTextfeld.getText().equals("User")) {
            System.out.println("User angemeldet");
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_User.fxml"));
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage currentStage = (Stage) LoginButton.getScene().getWindow();

            // Set the login page scene and show the stage
            currentStage.setScene(scene);
            currentStage.show();
        } else {
            System.out.println("loged in");
            DatabaseConnection db= new DatabaseConnection();
            Connection con= db.getConnection();
            try (Statement stm = con.createStatement()){
                String sql = "SELECT * FROM Users WHERE Username = '" + UsernameTextfeld.getText() + "'";
                ResultSet rs = stm.executeQuery(sql);
               // String un = "leer";
                String ps = "leer";
                String role = "3";
                while(rs.next()) {
                    un = rs.getString("Username");
                    ps = rs.getString("Password");
                    role = rs.getString("isAdmin");
                }
                if(un.equals("leer")) {
                    error_label.setText("Username does not exist");
                }
                boolean b = BCrypt.checkpw(PasswordTextfeld.getText(), ps);
                if(b) {
                    System.out.println("Passwort korrekt");
                    int irole = Integer.parseInt(role);
                    System.out.println(irole);
                    if(irole==1) {
                        System.out.println("Admin angemeldet");
                        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_Admin.fxml"));
                        Scene scene = new Scene(root);

                        // Get the current stage (window)
                        Stage currentStage = (Stage) LoginButton.getScene().getWindow();

                        // Set the login page scene and show the stage
                        currentStage.setScene(scene);
                        currentStage.show();
                    }
                    else {
                        System.out.println("User angemeldet");
                        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkinglottool/Home_User.fxml"));
                        Scene scene = new Scene(root);

                        // Get the current stage (window)
                        Stage currentStage = (Stage) LoginButton.getScene().getWindow();

                        // Set the login page scene and show the stage
                        currentStage.setScene(scene);
                        currentStage.show();
                    }


                }
                else error_label.setText("Wrong password");

                System.out.println(un);
                System.out.println(ps);

            }
            catch(Exception a) {
                a.printStackTrace();

            }
        }
    }
    
    public String getuser(){return un;}
    
    public void CloseButtonOnAction(ActionEvent e){

        Stage stage= (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

}