module com.example.parkinglottool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens Login;
    opens Home_page_admin;
    opens Add_User;

}