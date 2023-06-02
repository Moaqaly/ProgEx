package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

public class LoginController {
    public Button LoginButton;
    public Label LoginMassege;
    public Button CloseButton;

    public void LoginButtonOnAction(ActionEvent e){
        System.out.println("loged in");
    }

    public void CloseButtonOnAction(ActionEvent e){
        Stage stage= (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
    ////////////////
    // restrict the input in a text field to accept only email addresses
    @FXML
    private TextField EmailTextfeld;

    public void initialize2() {
        // Create a UnaryOperator to validate the email input
        UnaryOperator<TextFormatter.Change> emailFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
                return change;
            }
            return null;  // Reject the change if it doesn't match the email pattern
        };

        // Create a TextFormatter with the email filter
        TextFormatter<String> emailTextFormatter = new TextFormatter<>(emailFilter);

        // Set the TextFormatter to the EmailTextField
        EmailTextfeld.setTextFormatter(emailTextFormatter);
    }
}