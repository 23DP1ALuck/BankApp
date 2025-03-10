package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/registration.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("/images/VAbank.png"));
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/RobotoDefault.ttf"), 12);
        System.out.println(font != null ? "Font loaded: " + font.getName() : "Font not found!");
        stage.getIcons().add(icon);
        stage.setTitle("VA Bank");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }
}
