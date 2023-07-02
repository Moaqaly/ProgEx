module com.example.parkinglottool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires AnimateFX;
    requires java.sql;
    //requires BCrypt;

    opens Home_page_user;
    opens Login;
    opens Home_page_admin;
    opens Add_User;
    opens History;
    opens HistoryAdmin;

}