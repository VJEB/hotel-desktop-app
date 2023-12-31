package org.example.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.textfield.CustomTextField;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.Guest;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class GuestsView {

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

    @FXML
    private ImageView searchIconContainer;

    private static final ObservableList<Guest> guestsObservableList = FXCollections.observableArrayList();
    private static FilteredList<Guest> guestsFilteredList;

    private static Guest guestToBeUpdated;
    @FXML
    private void showGuestFormModal() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/guestForm.fxml"));
            Parent form = loader.load();

            Stage modalStage = FormHelper.createModalStage();
            FormHelper.setupModalAnimation(modalStage, form);

            GuestForm guestFormController = loader.getController();
            guestFormController.setStage(modalStage);

            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        initializeColumns();
        setupButtonCellFactory();
        loadGuestData();
        setupSearchFieldListeners();
        setupGuestsObservableListListener();
    }
    private void initializeColumns() {
        nationalIdColumn.setCellValueFactory(new PropertyValueFactory<>("national_id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
    }
    private void setupButtonCellFactory() {
        actionsColumn.setCellFactory(column -> new ButtonCell());
    }

    private class ButtonCell extends TableCell<Guest, Void> {
        private final JFXButton updateButton = new JFXButton();
        private final JFXButton deleteButton = new JFXButton();

        public ButtonCell() {
            setupUpdateButton();
            setupDeleteButton();
        }

        private void setupUpdateButton() {
            // Set up update button properties, styles, and actions
            updateButton.getStyleClass().add("edit-button");
            ImageView pencilImageContainer = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/pencil.png"))));
            pencilImageContainer.setFitWidth(15);
            pencilImageContainer.setFitHeight(15);
            updateButton.setGraphic(pencilImageContainer);
            updateButton.setOnAction(event -> handleUpdateButtonClick());
        }

        private void setupDeleteButton() {
            // Set up delete button properties, styles, and actions
            deleteButton.getStyleClass().add("delete-button");
            ImageView binImageContainer = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/trash-bin.png"))));
            binImageContainer.setFitWidth(15);
            binImageContainer.setFitHeight(15);
            deleteButton.setGraphic(binImageContainer);
            deleteButton.setOnAction(event -> handleDeleteButtonClick());
        }

        private void handleUpdateButtonClick() {
            Guest guest = getTableRow().getItem();
            guestToBeUpdated = guest;

            if (guest != null) {
                openGuestFormModal(guest);
            }
        }

        private void handleDeleteButtonClick() {
            Guest guest = getTableRow().getItem();
            if (guest != null) {
                showDeleteConfirmationAlert(guest);
            }
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
    }

    private void openGuestFormModal(Guest guest) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/guestForm.fxml"));
            Parent form = loader.load();
            Stage modalStage = FormHelper.createModalStage();

            FormHelper.setupModalAnimation(modalStage, form);

            GuestForm guestFormController = loader.getController();
            guestFormController.setGuestToUpdate(guest);
            guestFormController.setStage(modalStage);

            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationAlert(Guest guest) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the guest: " + guest.getFirstName() + " " + guest.getLastName() + "?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(Main.class.getResource("../../application.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ImageView warningImageContainer = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/warning.png"))));
        warningImageContainer.setFitWidth(38);
        warningImageContainer.setFitHeight(38);
        dialogPane.setGraphic(warningImageContainer);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handleGuestDeletion(guest);
        }
    }

    private void handleGuestDeletion(Guest guest) {
        guestsObservableList.remove(guest);
        GuestDAO.removeGuestFromCachedGuests(guest);
        if (new GuestDAO().deleteGuest(guest.getNational_id())) {
            System.out.println("Guest deleted successfully!");
        } else {
            System.out.println("There was an error deleting the guest " + guest.getFirstName() + " with national id of: " + guest.getNational_id());
        }
    }

    private void loadGuestData() {
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
    }

    private void setupSearchFieldListeners() {
        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                searchIconContainer.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/search-focused.png"))));
            } else {
                searchIconContainer.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("../../Icons/search.png"))));
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Guest> temporaryFilteredTable = updateFilter(newValue);
            if (Objects.equals(newValue, "")){
                System.out.println("Empty searchField");
                filteredTableView.setItems(guestsObservableList);
                filteredTableView.refresh();
            } else {
                filteredTableView.setItems(FXCollections.observableArrayList(temporaryFilteredTable));
            }
        });
    }

    private void setupGuestsObservableListListener() {
        guestsObservableList.addListener((ListChangeListener<Guest>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    handleGuestAdded(change);
                }
                if(change.wasRemoved()) {
                    handleGuestRemoved(change);
                }
                searchField.setText("");
            }
        });
    }
    private void handleGuestAdded(ListChangeListener.Change<? extends Guest> change) {
        Guest addedGuest = change.getAddedSubList().get(0);
        String addedGuestNationalId = addedGuest.getNational_id();
        int addedItems = change.getAddedSize();
        if (addedItems == 1) {
            if (guestToBeUpdated != null && Objects.equals(addedGuestNationalId, guestToBeUpdated.getNational_id())) {
                FormHelper.showSnackbar(borderPane, "Guest updated!");
            } else {
                FormHelper.showSnackbar(borderPane, "Guest added!");
            }
            filteredTableView.refresh();
        }
    }

    private void handleGuestRemoved(ListChangeListener.Change<? extends Guest> change) {
        Guest removedGuest = change.getRemoved().get(0);
        String removedGuestNationalId = removedGuest.getNational_id();
        int removedItems = change.getRemovedSize();
        if (removedItems == 1) {
            if (guestToBeUpdated != null && !Objects.equals(removedGuestNationalId, guestToBeUpdated.getNational_id())) {
                FormHelper.showSnackbar(borderPane, "Guest removed!");
            }
            filteredTableView.refresh();
        }
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
