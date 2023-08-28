package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.Guest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationForm {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeForm() {
        stage.close();
    }

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

    private void setGuestComboBox() {
        List<Guest> cachedGuests = GuestDAO.getCachedGuests();
        if (cachedGuests == null) {
            List<Guest> newGuestsList = new GuestDAO().selectGuestsFromDB();
            GuestDAO.setCachedGuests(newGuestsList);
            for (Guest guest : newGuestsList) {
                guestComboBox.getItems().add(guest.getFirstName() + "\n" + guest.getNational_id());
            }
        } else {
            for (Guest guest : cachedGuests) {
                guestComboBox.getItems().add(guest.getFirstName() + "\n" + guest.getNational_id());
            }
        }
    }
    public void initialize() {
        //closeButton.setOnAction(event -> Platform.exit());

        setGuestComboBox();
        FormHelper.styleLabelOnElementFocus_(guestComboBox, guestLabel,"#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(peopleField, peopleLabel,"#1291bf", "#053445");

        FormHelper.applyRegexFilter(peopleField, "\\d*");
    }
}
