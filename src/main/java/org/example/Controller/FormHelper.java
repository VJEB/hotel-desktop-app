package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.example.Main;
import org.example.Model.GuestNationalities;
import org.example.Model.RoomTypes;

import java.util.Objects;

public class FormHelper {
    public static Stage createModalStage() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);

        Image hotelIcon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Images/hotel.png")));
        modalStage.getIcons().add(hotelIcon);

        return modalStage;
    }

    public static void setupModalAnimation(Stage modalStage, Parent form) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), form);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        Scene scene = new Scene(form);
        modalStage.setScene(scene);

        scaleTransition.setOnFinished(event -> modalStage.show());
        scaleTransition.play();
    }

    public static void setupViewTransition(BorderPane viewBorderPane, BorderPane userHomeBorderPane){
        viewBorderPane.setTranslateX(userHomeBorderPane.getWidth());
        userHomeBorderPane.setCenter(viewBorderPane);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), viewBorderPane);
        transition.setToX(0);
        transition.play();
    }
    //STYLING
    public static void styleLabelOnElementFocus(TextField textField, Label label, String focusedColor, String unfocusedColor) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
    }
    public static void styleLabelOnElementFocus(SearchableComboBox<GuestNationalities> searchableComboBox, Label label, String focusedColor, String unfocusedColor) {
        searchableComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
    }
    public static void styleLabelOnElementFocusR(SearchableComboBox<RoomTypes> searchableComboBox, Label label, String focusedColor, String unfocusedColor) {
        searchableComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
    }
    public static void styleLabelOnElementFocus_(SearchableComboBox<String> searchableComboBox, Label label, String focusedColor, String unfocusedColor) {
        searchableComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
    }

    public static void styleLabelOnElementFocus(DatePicker datePicker, Label label, String focusedColor, String unfocusedColor) {
        datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
    }
    public static void showSnackbar(VBox vBoxContainer, String errorMessage) {
        JFXSnackbar snackbar = new JFXSnackbar(vBoxContainer);
        Label snackbarLabel = new Label(errorMessage);
        snackbarLabel.setStyle("-fx-padding: 5px;");
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbarLabel, Duration.millis(3000)));
    }
    public static void showSnackbar(BorderPane borderPaneContainer, String errorMessage) {
        JFXSnackbar snackbar = new JFXSnackbar(borderPaneContainer);
        Label snackbarLabel = new Label(errorMessage);
        snackbarLabel.setStyle("-fx-padding: 5px;");
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbarLabel, Duration.millis(3000)));
    }

    //VALIDATIONS
    public static void applyRegexFilter(TextField textField, String regexPattern) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regexPattern)) {
                textField.setText(newValue.replaceAll("[^" + regexPattern + "]", ""));
            }
        });
    }
}
