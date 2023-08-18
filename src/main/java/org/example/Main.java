package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.Controller.Login;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../login.fxml"));
        Parent root = loader.load();
        Login loginController = loader.getController();
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.getIcons().add(new Image(("Images/hotel.png")));
        stage.setTitle("Hotel management");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
