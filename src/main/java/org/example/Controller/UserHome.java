package org.example.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Main;

import java.io.IOException;
import java.util.Objects;

public class UserHome {

    @FXML
    private BorderPane borderPane;

    public void showReservationsView() {
        try {
            BorderPane reservationsPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../userHomeViews/reservationsView.fxml")));
            FormHelper.setupViewTransition(reservationsPane, borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showInvoicesView() {
        try {
            BorderPane invoicesPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../userHomeViews/invoicesView.fxml")));
            FormHelper.setupViewTransition(invoicesPane, borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showGuestView() {
        try {
            BorderPane guestsPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../userHomeViews/guestsView.fxml")));
            FormHelper.setupViewTransition(guestsPane, borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showRoomsView() {
        try {
            BorderPane roomsPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../userHomeViews/roomsView.fxml")));
            FormHelper.setupViewTransition(roomsPane, borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLoginScene() {
        try {
            BorderPane newBorderPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("../../login.fxml")));
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
    public void initialize() {
        showReservationsView();
    }
}
