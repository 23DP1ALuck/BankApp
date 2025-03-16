package org.example;

import Exceptions.IncorrectPassException;
import Exceptions.NoSuchUserException;
import Exceptions.FieldsAreBlankException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.User;
import services.Database;

public class Login {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label userMessage;
    @FXML
    private Label switchToRegistration;
    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        // button action listeners
        switchToRegistration.setOnMouseClicked(this::switchToRegistration);
        loginButton.setOnAction(this::loginButtonHandler);
    }

    // switch to registration scene
    public void switchToRegistration(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }

    // action on login button
    private void loginButtonHandler(ActionEvent event){
        try {
            User user = signIn();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            root = loader.load();

            Dashboard dashboardController = loader.getController();
            dashboardController.setNameAndLastName(user.getName(), user.getSurname());
            dashboardController.setHelloUsername(user.getUsername());
            dashboardController.setAccountNumber(user.getAccountNumber());
            dashboardController.setStackPane(user);

            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.setResizable(false);
            stage.centerOnScreen();
            scene = new Scene(root);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (FieldsAreBlankException | NoSuchUserException | IncorrectPassException |  IOException e){
            userMessage.setText(e.getMessage());
            userMessage.setStyle("-fx-background-color: rgba(255, 65, 65, 0.1); -fx-text-fill: #FF4141; -fx-background-radius: 12.5; -fx-border-color: rgba(255, 65, 65, 0.2); -fx-border-radius: 12.5");
        }
    }

    Database database = Database.getInstance();

    // sign in func
    private User signIn() throws FieldsAreBlankException, NoSuchUserException {
        userMessage.setText("");
        checkIfBlankInLogin();
        String login = this.usernameField.getText();
        String password = this.passwordField.getText();
        return database.checkUser(login, password);
    }

    // blank field check
    private void checkIfBlankInLogin() throws FieldsAreBlankException {
        if (this.usernameField.getText().isBlank() || this.passwordField.getText().isBlank()) {
            throw new FieldsAreBlankException();
        }
    }
}
