package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ReservationForm {
    @FXML
    private TextField peopleField;

    @FXML
    private SearchableComboBox<String> guestComboBox;

    @FXML
    private Label guestLabel;

    @FXML
    private Label peopleLabel;
    @FXML
    private JFXButton closeButton;

    public void initialize() {
        //closeButton.setOnAction(event -> Platform.exit());

        guestComboBox.getItems().addAll("Apple", "Banana", "Cherry", "Grapes", "Lemon", "Orange", "Peach", "Pear");
        peopleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                peopleLabel.setStyle("-fx-text-fill: #1291bf;");
            } else {
                peopleLabel.setStyle("-fx-text-fill: #053445;");
            }
        });

        guestComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                guestLabel.setStyle("-fx-text-fill: #1291bf;");
            } else {
                guestLabel.setStyle("-fx-text-fill: #053445;");
            }
        });
        peopleField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    peopleField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
