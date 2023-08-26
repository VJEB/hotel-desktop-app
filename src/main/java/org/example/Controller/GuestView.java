package org.example.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import java.util.*;

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

    private static Guest guestToBeUpdated;
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

        actionsColumn.setCellFactory(column -> new TableCell<Guest, Void>() {
            private final JFXButton updateButton = new JFXButton();
            private final JFXButton deleteButton = new JFXButton();

            {
                ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/pencil.png"))));
                editIcon.setFitWidth(15);
                editIcon.setFitHeight(15);
                updateButton.getStyleClass().add("edit-button");
                updateButton.setGraphic(editIcon);

                ImageView binIcon = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/trash-bin.png"))));
                binIcon.setFitWidth(15);
                binIcon.setFitHeight(15);
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setGraphic(binIcon);

                updateButton.setOnAction(event -> {
                    Guest guest = getTableRow().getItem();
                    guestToBeUpdated = guest;

                    if (guest != null) {
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
                            guestFormController.setGuestToUpdate(guest);
                            guestFormController.setStage(modalStage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                deleteButton.setOnAction(event -> {
                    Guest guest = getTableRow().getItem();
                    if (guest != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Deletion");
                        alert.setHeaderText("Are you sure you want to delete the guest: " + guest.getFirstName() + " " + guest.getLastName() + "?");

                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                Objects.requireNonNull(Main.class.getResource("../../application.css")).toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        ImageView warningIon = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/warning.png"))));
                        warningIon.setFitHeight(48);
                        warningIon.setFitWidth(48);
                        dialogPane.setGraphic(warningIon);

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            guestsObservableList.remove(guest);
                            GuestDAO.removeGuestFromCachedGuests(guest);
                            if (new GuestDAO().deleteGuest(guest.getNational_id())) {
                                System.out.println("Guest deleted successfully!");
                            } else {
                                System.out.println("There was an error deleting the guest " + guest.getFirstName() + " with national id of: " + guest.getNational_id());
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonContainer = new HBox(updateButton, deleteButton);
                    buttonContainer.setAlignment(Pos.CENTER);
                    buttonContainer.setSpacing(10);
                    setGraphic(buttonContainer);
                }
            }
        });

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
            ArrayList<Guest> temporaryFilteredTable = updateFilter(newValue);
            if (Objects.equals(newValue, "")){
                System.out.println("Empty searchField");
                filteredTableView.setItems(guestsObservableList);
            } else {
                filteredTableView.setItems(FXCollections.observableArrayList(temporaryFilteredTable));
            }
        });

        guestsObservableList.addListener((ListChangeListener<Guest>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Guest addedGuest = change.getAddedSubList().get(0);
                    String addedGuestNationalId = addedGuest.getNational_id();
                    int addedItems = change.getAddedSize();
                    if (addedItems == 1) {
                        if (Objects.equals(addedGuestNationalId, guestToBeUpdated.getNational_id())) {
                            FormHelper.showSnackbar(borderPane, "Guest updated!");
                        } else {
                            FormHelper.showSnackbar(borderPane, "Guest added!");
                        }
                    }
                }
                if(change.wasRemoved()) {
                    Guest removedGuest = change.getRemoved().get(0);
                    String removedGuestNationalId = removedGuest.getNational_id();
                    int removedItems = change.getRemovedSize();
                    if (removedItems == 1) {
                        if (!Objects.equals(removedGuestNationalId, guestToBeUpdated.getNational_id())) {
                            FormHelper.showSnackbar(borderPane, "Guest removed!");
                        }
                    }
                }
                searchField.setText("");
            }
        });
    }
    public static void addGuestToTable(Guest guest) {
        guestsObservableList.add(guest);
        GuestDAO.addGuestToCachedGuests(guest);
    }
    public static void updateGuestFromTable(Guest guest) {
        guestsObservableList.remove(guestToBeUpdated);
        guestsObservableList.add(guest);
        GuestDAO.updateGuestFromCachedGuests(guestToBeUpdated, guest);
    }
    private ArrayList<Guest> updateFilter(String searchText) {
        Platform.runLater(() -> {
            guestsFilteredList.setPredicate(guest -> {
                String lowerCaseSearchText = searchText.toLowerCase();

                return guest.getNational_id().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getFirstName().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getLastName().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getDateOfBirth().toString().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getPhoneNumber().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getEmail().toLowerCase().contains(lowerCaseSearchText) ||
                        guest.getNationality().toString().toLowerCase().contains(lowerCaseSearchText);
            });
        });
        return new ArrayList<>(guestsFilteredList);
    }
}
