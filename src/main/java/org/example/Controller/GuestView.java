package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.textfield.CustomTextField;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.Guest;
import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuestView {

    @FXML
    private FilteredTableView<Guest> filteredTableView;

    @FXML
    private TableColumn<Guest, String> nationalIdColumn;
    @FXML
    private TableColumn<Guest, String> firstNameColumn;
    @FXML
    private TableColumn<Guest, String> lastNameColumn;
    @FXML
    private TableColumn<Guest, LocalDate> dateOfBirthColumn;
    @FXML
    private TableColumn<Guest, String> phoneNumberColumn;
    @FXML
    private TableColumn<Guest, String> emailColumn;
    @FXML
    private TableColumn<Guest, String> nationalityColumn;
    @FXML
    private TableColumn<Guest, Void> actionsColumn;

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton newGuestButton;

    @FXML
    private CustomTextField searchField;

    private static ObservableList<Guest> guestsObservableList = FXCollections.observableArrayList();
    private static FilteredList<Guest> guestsFilteredList;

    @FXML
    private void showGuestFormModal() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/guestForm.fxml"));
            Parent form = loader.load();
            Image hotelIcon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Images/hotel.png")));

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.getIcons().add(hotelIcon);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), form);
            scaleTransition.setFromX(0.5);
            scaleTransition.setFromY(0.5);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);

            Scene scene = new Scene(form);
            modalStage.setScene(scene);

            modalStage.show();
            scaleTransition.play();

            GuestForm guestFormController = loader.getController();
            guestFormController.setStage(modalStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        nationalIdColumn.setCellValueFactory(new PropertyValueFactory<>("national_id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));

        // Set up your custom cell value factory for the "Actions" column
        //actionsColumn.setCellFactory(...); // You need to define how the buttons should be displayed
        List<Guest> cachedGuests = GuestDAO.getCachedGuests();
        if (cachedGuests == null) {
            List<Guest> newGuestsList = new GuestDAO().selectGuestsFromDB();
            guestsObservableList.setAll(newGuestsList);
            guestsFilteredList = new FilteredList<>(guestsObservableList);
            GuestDAO.setCachedGuests(newGuestsList);
        } else {
            guestsObservableList.setAll(cachedGuests);
            guestsFilteredList = new FilteredList<>(guestsObservableList);
        }

        filteredTableView.setItems(guestsObservableList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            guestsFilteredList.setPredicate(guest -> {
                filteredTableView.setItems(guestsFilteredList);
                System.out.println(newValue);
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return guest.getFirstName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        guestsObservableList.addListener((ListChangeListener<Guest>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    guestsFilteredList.addAll(change.getAddedSubList());
                    int addedItems = change.getAddedSize();
                    if (addedItems == 1) {
                        FormHelper.showSnackbar(borderPane, "Guest added!");
                    }
                }
            }
        });
    }
    public static void addGuestToTable(Guest guest) {
        guestsObservableList.add(guest);
        GuestDAO.addGuestToCachedGuests(guest);
    }
}
