package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    // You can define event handlers and logic here
    @FXML
    private void handleLoginButtonClick() {
        String usernameValue = usernameField.getText();
        String passwordValue = passwordField.getText();

        usernameField.setText(usernameValue + "0");
    }
}