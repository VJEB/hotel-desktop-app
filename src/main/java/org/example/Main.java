package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.Controller.Login;
import org.example.Model.HibernateUtil;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../login.fxml"));
        Parent root = loader.load();
        Login loginController = loader.getController();//This is exactly how you assign a controller
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.getIcons().add(new Image(("Images/hotel.png")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

