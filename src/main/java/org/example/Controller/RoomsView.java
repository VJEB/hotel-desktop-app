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
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.Main;
import org.example.Model.DAO.GuestDAO;
import org.example.Model.DAO.RoomDAO;
import org.example.Model.Guest;
import org.example.Model.Room;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
public class RoomsView {
    @FXML
    private FilteredTableView<Room> filteredTableView;

    @FXML
    private TableColumn<Room, String> roomNumberColumn;
    @FXML
    private TableColumn<Room, String> roomTypeColumn;
    @FXML
    private TableColumn<Room, String> priceColumn;
    @FXML
    private TableColumn<Room, String> bedsColumn;
    @FXML
    private TableColumn<Room, String> capacityColumn;
    @FXML
    private TableColumn<Room, Void> actionsColumn;
    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton newRoomButton;
    @FXML
    private CustomTextField searchField;

    @FXML
    private ImageView searchIconContainer;

    private static ObservableList<Room> roomsObservableList = FXCollections.observableArrayList();
    private static FilteredList<Room> roomsFilteredList;

    private static Room roomToBeUpdated;
    @FXML
    private void showRoomFormModal() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/roomForm.fxml"));
            Parent form = loader.load();

            Stage modalStage = FormHelper.createModalStage();
            FormHelper.setupModalAnimation(modalStage, form);

            RoomForm roomFormController = loader.getController();
            roomFormController.setStage(modalStage);

            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        initializeColumns();
        setupButtonCellFactory();
        loadRoomData();
        setupSearchFieldListeners();
        setupRoomsObservableListListener();
    }
    private void initializeColumns() {
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        bedsColumn.setCellValueFactory(new PropertyValueFactory<>("beds"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    }
    private void setupButtonCellFactory() {
        actionsColumn.setCellFactory(column -> new RoomsView.ButtonCell());
    }

    private class ButtonCell extends TableCell<Room, Void> {
        private final JFXButton updateButton = new JFXButton();
        private final JFXButton deleteButton = new JFXButton();

        public ButtonCell() {
            setupUpdateButton();
            setupDeleteButton();
        }

        private void setupUpdateButton() {
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
            Room room = getTableRow().getItem();
            roomToBeUpdated = room;

            if (room != null) {
                openRoomFormModal(room);
            }
        }

        private void handleDeleteButtonClick() {
            Room room = getTableRow().getItem();
            if (room != null) {
                showDeleteConfirmationAlert(room);
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
    private void openRoomFormModal(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/roomForm.fxml"));
            Parent form = loader.load();
            Stage modalStage = FormHelper.createModalStage();

            FormHelper.setupModalAnimation(modalStage, form);

            RoomForm roomFormController = loader.getController();
            roomFormController.setRoomToUpdate(room);
            roomFormController.setStage(modalStage);

            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showDeleteConfirmationAlert(Room room) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the room: " + room.getRoomNumber() + "?");

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
            handleRoomDeletion(room);
        }
    }
    private void handleRoomDeletion(Room room) {
        roomsObservableList.remove(room);
        RoomDAO.removeRoomFromCachedRooms(room);
        if (new RoomDAO().deleteRoom(room.getRoomNumber())) {
            System.out.println("Room deleted successfully!");
        } else {
            System.out.println("There was an error deleting the room " + room.getRoomNumber());
        }
    }
    private void loadRoomData() {
        List<Room> cachedRooms = RoomDAO.getCachedRooms();
        if (cachedRooms == null) {
            List<Room> newRoomsList = new RoomDAO().selectRoomsFromDB();
            roomsObservableList.setAll(newRoomsList);
            roomsFilteredList = new FilteredList<>(roomsObservableList);
            RoomDAO.setCachedRooms(newRoomsList);
        } else {
            roomsObservableList.setAll(cachedRooms);
            roomsFilteredList = new FilteredList<>(roomsObservableList);
        }
        filteredTableView.setItems(roomsObservableList);
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
            ArrayList<Room> temporaryFilteredTable = updateFilter(newValue);
            if (Objects.equals(newValue, "")){
                System.out.println("Empty searchField");
                filteredTableView.setItems(roomsObservableList);
                filteredTableView.refresh();
            } else {
                filteredTableView.setItems(FXCollections.observableArrayList(temporaryFilteredTable));
            }
        });
    }

    private void setupRoomsObservableListListener() {
        roomsObservableList.addListener((ListChangeListener<Room>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    handleRoomAdded(change);
                }
                if(change.wasRemoved()) {
                    handleRoomRemoved(change);
                }
                searchField.setText("");
            }
        });
    }
    private void handleRoomAdded(ListChangeListener.Change<? extends Room> change) {
        Room addedRoom = change.getAddedSubList().get(0);
        String addedRoomRoomNumber = addedRoom.getRoomNumber();
        int addedItems = change.getAddedSize();
        if (addedItems == 1) {
            if (roomToBeUpdated != null && Objects.equals(addedRoomRoomNumber, roomToBeUpdated.getRoomNumber())) {
                FormHelper.showSnackbar(borderPane, "Room updated!");
            } else {
                FormHelper.showSnackbar(borderPane, "Room added!");
            }
            filteredTableView.refresh();
        }
    }

    private void handleRoomRemoved(ListChangeListener.Change<? extends Room> change) {
        Room removedGuest = change.getRemoved().get(0);
        String removedGuestNationalId = removedGuest.getRoomNumber();
        int removedItems = change.getRemovedSize();
        if (removedItems == 1) {
            if (roomToBeUpdated != null && !Objects.equals(removedGuestNationalId, roomToBeUpdated.getRoomNumber())) {
                FormHelper.showSnackbar(borderPane, "Room removed!");
            }
            filteredTableView.refresh();
        }
    }


    public static void addRoomToTable(Room room) {
        roomsObservableList.add(room);
        RoomDAO.addRoomToCachedRooms(room);
    }
    public static void updateRoomFromTable(Room room) {
        roomsObservableList.remove(roomToBeUpdated);
        roomsObservableList.add(room);
        RoomDAO.updateRoomFromCachedRooms(roomToBeUpdated, room);
    }
    private ArrayList<Room> updateFilter(String searchText) {
        Platform.runLater(() -> {
            roomsFilteredList.setPredicate(room -> {
                String lowerCaseSearchText = searchText.toLowerCase();

                return room.getRoomNumber().toLowerCase().contains(lowerCaseSearchText) ||
                        room.getRoomType().toString().toLowerCase().contains(lowerCaseSearchText) ||
                        room.getPrice().toString().contains(lowerCaseSearchText) ||
                        String.valueOf(room.getBeds()).contains(lowerCaseSearchText) ||
                        String.valueOf(room.getCapacity()).contains(lowerCaseSearchText);
            });
        });
        return new ArrayList<>(roomsFilteredList);
    }
}
