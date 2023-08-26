package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.example.Model.GuestNationalities;

public class FormHelper {

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
    public static void styleLabelOnElementFocus(DatePicker datePicker, Label label, String focusedColor, String unfocusedColor) {
        datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setStyle("-fx-text-fill: " + focusedColor + ";");
            } else {
                label.setStyle("-fx-text-fill: " + unfocusedColor + ";");
            }
        });
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
