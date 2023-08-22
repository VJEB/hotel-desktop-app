package org.example.Controller;

import com.jfoenix.controls.JFXSnackbar;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Main;
import org.example.Model.DAO.UserDAO;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button exitButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonClick() {
        loginButton.setDisable(true);
        loginButton.setText("Logging in...");
        enableLoginButtonWithDelay();
        String usernameValue = usernameField.getText();
        String passwordValue = passwordField.getText();
        try {
            if (userDAO.verifyUser(usernameValue, passwordValue)) {
                showSnackbar("VERIFIED!");
                showHomeScene();
            } else {
                showSnackbar("PASSWORD DOES NOT MATCH");
            }
        } catch (RuntimeException e) {
            showSnackbar(e.getMessage());
            e.printStackTrace();
        }
    }
    private void showSnackbar(String errorMessage) {
        JFXSnackbar snackbar = new JFXSnackbar(borderPane);
        Label snackbarLabel = new Label(errorMessage);
        snackbar.setPrefWidth(500);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbarLabel, Duration.millis(3000)));
    }
    private void enableLoginButtonWithDelay() {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            loginButton.setDisable(false);
            loginButton.setText("SIGN IN");
        });
        delay.play();
    }

    public void showHomeScene() {
        try {
            BorderPane newBorderPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../userHome.fxml")));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(newBorderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitButton.setOnAction(event -> Platform.exit());

        usernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                usernameLabel.setStyle("-fx-text-fill: #1291bf;");
            } else {
                usernameLabel.setStyle("-fx-text-fill: #053445;");
            }
        });

        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordLabel.setStyle("-fx-text-fill: #1291bf;");
            } else {
                passwordLabel.setStyle("-fx-text-fill: #053445;");
            }
        });
    }
}