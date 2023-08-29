package org.example.Controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Main;
import org.example.Model.DAO.UserDAO;
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
                showHomeScene();
            } else {
                FormHelper.showSnackbar(borderPane,"PASSWORD DOES NOT MATCH");
            }
        } catch (RuntimeException e) {
            FormHelper.showSnackbar(borderPane, e.getMessage());
            e.printStackTrace();
        }
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
            Scene newScene = new Scene(newBorderPane);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setScene(newScene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitButton.setOnAction(event -> Platform.exit());

        FormHelper.styleLabelOnElementFocus(usernameField, usernameLabel, "#1291bf", "#053445");

        FormHelper.styleLabelOnElementFocus(passwordField, passwordLabel, "#1291bf", "#053445");
    }
}