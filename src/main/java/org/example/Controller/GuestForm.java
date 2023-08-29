package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.Guest;
import org.example.Model.GuestNationalities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GuestForm {

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
    private Label nationalIdLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label nationalityLabel;

    @FXML
    private TextField nationalIdTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private DatePicker dateOfBirthDatePicker;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private SearchableComboBox<GuestNationalities> nationalitySearchableComboBox;
    @FXML
    private JFXButton saveGuestButton;

    private Guest guestToUpdate;

    public void setGuestToUpdate(Guest guest) {
        this.guestToUpdate = guest;
        populateFormWithGuestData();
        title.setText("Edit guest");
    }

    private void populateFormWithGuestData() {
        nationalIdTextField.setText(guestToUpdate.getNational_id());
        nationalIdTextField.setDisable(true);
        firstNameTextField.setText(guestToUpdate.getFirstName());
        lastNameTextField.setText(guestToUpdate.getLastName());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth = guestToUpdate.getDateOfBirth();
        String formattedDate = dateOfBirth.format(dateFormatter);
        dateOfBirthDatePicker.getEditor().setText(formattedDate);
        dateOfBirthDatePicker.commitValue();

        phoneNumberTextField.setText(guestToUpdate.getPhoneNumber());
        emailTextField.setText(guestToUpdate.getEmail());
        nationalitySearchableComboBox.getSelectionModel().select(guestToUpdate.getNationality());
    }
    @FXML
    private void handleSaveGuestButtonClick(){
        saveGuestButton.setDisable(false);
        saveGuestButton.setText("Saving guest...");
        enableGuestButtonWithDelay();
        String nationalId = nationalIdTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate dateOfBirth = dateOfBirthDatePicker.getValue();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        GuestNationalities nationality = nationalitySearchableComboBox.getSelectionModel().getSelectedItem();

        if (nationalId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                dateOfBirth == null || phoneNumber.isEmpty() || email.isEmpty() ||
                nationality == null) {
            FormHelper.showSnackbar(vBoxContainer,"Please fill in all fields.");
        } else {
            Guest guest = new Guest(nationalId, firstName, lastName, dateOfBirth, phoneNumber, email, nationality);
            if (guestToUpdate != null) {
                new GuestDAO().updateGuest(guest);
                GuestsView.updateGuestFromTable(guest);
                System.out.println("Guest updated!");
            } else {
                new GuestDAO().storeGuest(guest);
                GuestsView.addGuestToTable(guest);
            }
            closeForm();
        }
    }
    private void enableGuestButtonWithDelay() {
        PauseTransition delay = new PauseTransition(Duration.millis(3000));
        delay.setOnFinished(event -> {
            saveGuestButton.setDisable(false);
            saveGuestButton.setText("Save guest");
        });
        delay.play();
    }

    public void initialize() {
        nationalitySearchableComboBox.getItems().addAll(GuestNationalities.values());

        FormHelper.styleLabelOnElementFocus(nationalIdTextField, nationalIdLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(firstNameTextField, firstNameLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(lastNameTextField, lastNameLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(dateOfBirthDatePicker, dateOfBirthLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(phoneNumberTextField, phoneNumberLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(emailTextField, emailLabel, "#1291bf", "#053445");
        FormHelper.styleLabelOnElementFocus(nationalitySearchableComboBox, nationalityLabel, "#1291bf", "#053445");

        //FORM VALIDATIONS
        FormHelper.applyRegexFilter(nationalIdTextField, "[a-zA-Z0-9-]*");
        FormHelper.applyRegexFilter(firstNameTextField, "[a-zA-Z ñÑ]*");
        FormHelper.applyRegexFilter(lastNameTextField, "[a-zA-Z ñÑ]*");
        FormHelper.applyRegexFilter(phoneNumberTextField, "[0-9]*");
        FormHelper.applyRegexFilter(emailTextField, "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    }
}
