package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.DAO.RoomDAO;
import org.example.Model.Guest;
import org.example.Model.GuestNationalities;
import org.example.Model.Room;
import org.example.Model.RoomTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RoomForm {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeForm() {
        stage.close();
    }
    @FXML
    private VBox vBoxContainer;

    @FXML
    private Label title;

    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label bedsLabel;
    @FXML
    private Label capacityLabel;

    @FXML
    private TextField roomNumberTextField;
    @FXML
    private SearchableComboBox<RoomTypes> roomTypeSearchableComboBox;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField bedsTextField;
    @FXML
    private TextField capacityTextField;

    @FXML
    private JFXButton saveRoomButton;
    private Room roomToUpdate;

    public void setRoomToUpdate(Room room) {
        this.roomToUpdate = room;
        populateFormWithRoomData();
        title.setText("Edit room");
    }
    private void populateFormWithRoomData() {
        roomNumberTextField.setText(roomToUpdate.getRoomNumber());
        roomNumberTextField.setDisable(true);
        roomTypeSearchableComboBox.getSelectionModel().select(roomToUpdate.getRoomType());
        priceTextField.setText(String.valueOf(roomToUpdate.getPrice()));
        bedsTextField.setText(String.valueOf(roomToUpdate.getBeds()));
        capacityTextField.setText(String.valueOf(roomToUpdate.getCapacity()));
    }
    @FXML
    private void handleSaveGuestButtonClick(){
        saveRoomButton.setDisable(false);
        saveRoomButton.setText("Saving guest...");
        enableRoomButtonWithDelay();
        String roomNumber = roomNumberTextField.getText();
        RoomTypes roomType = roomTypeSearchableComboBox.getSelectionModel().getSelectedItem();
        Double price = Double.parseDouble(priceTextField.getText());
        int beds = Integer.parseInt(bedsTextField.getText());
        int capacity = Integer.parseInt(capacityTextField.getText());

        if (roomNumber.isEmpty() || roomType == null || priceTextField.getText().isEmpty() || bedsTextField.getText().isEmpty() || capacityTextField.getText().isEmpty()) {
            FormHelper.showSnackbar(vBoxContainer,"Please fill in all fields.");
        } else {
            Room room = new Room(roomNumber, roomType, price, beds, capacity);
            if (roomToUpdate != null) {
                new RoomDAO().updateRoom(room);
                RoomsView.updateRoomFromTable(room);
                System.out.println("Room updated!");
            } else {
                new RoomDAO().storeRoom(room);
                RoomsView.addRoomToTable(room);
            }
            closeForm();
        }
    }
    private void enableRoomButtonWithDelay() {
        PauseTransition delay = new PauseTransition(Duration.millis(3000));
        delay.setOnFinished(event -> {
            saveRoomButton.setDisable(false);
            saveRoomButton.setText("Save guest");
        });
        delay.play();
    }
    public void initialize() {
        roomTypeSearchableComboBox.getItems().addAll(RoomTypes.values());

        FormHelper.styleLabelOnElementFocus(roomNumberTextField, roomNumberLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocusR(roomTypeSearchableComboBox, roomTypeLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(priceTextField, priceLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(bedsTextField, bedsLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(capacityTextField, capacityLabel, "#1291bf", "#053445");

        //FORM VALIDATIONS
        FormHelper.applyRegexFilter(roomNumberTextField, "[a-zA-Z0-9-]*");
        FormHelper.applyRegexFilter(priceTextField, "\\d*\\.\\d+");
        FormHelper.applyRegexFilter(bedsTextField, "\\d");
        FormHelper.applyRegexFilter(capacityTextField, "\\d");
    }
}
