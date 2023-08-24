package org.example.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Main;

import java.io.IOException;

public class ReservationView {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton newReservationButton;

    @FXML
    private void showReservationFormModal() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../../Forms/reservationForm.fxml"));
            Parent form = loader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add Reservation");

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), form);
            scaleTransition.setFromX(0.5);
            scaleTransition.setFromY(0.5);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);

            Scene scene = new Scene(form);
            modalStage.setScene(scene);

            modalStage.show();
            scaleTransition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}