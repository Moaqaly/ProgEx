package Add_User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class Add_User_Controller implements Initializable {

    @FXML
    private ChoiceBox<String> Choicebox_Role;

    @FXML
    private ChoiceBox<String> Choicebox_Details;

    private String[] Roles = {"Admin","User"};

    private String[] Details = {"Women","Disable","standard"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Choicebox_Role.getItems().addAll(Roles);
        Choicebox_Role.setOnAction(this::getRole);

        Choicebox_Details.getItems().addAll(Details);
        Choicebox_Details.setOnAction(this::getDetails);
    }
    public void getRole(ActionEvent event){ String Role = Choicebox_Role.getValue();}
    public void getDetails(ActionEvent event){String Detail = Choicebox_Details.getValue();}
}
