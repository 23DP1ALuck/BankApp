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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private String password;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label userMessage, switchToRegistration;
    @FXML
    private Button loginButton, showPassButton;
    @FXML
    private ImageView showImage;

    @FXML
    private void initialize() {
        // button action listeners
        switchToRegistration.setOnMouseClicked(this::switchToRegistration);
        loginButton.setOnAction(this::loginButtonHandler);

        // hides userMessage if something is typed
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        // hides userMessage by default
        userMessage.setVisible(false);
        loginButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if(newScene != null){
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        loginButton.fire();
                    }
                });
            }
        });

        showPassButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Image hide = new Image(getClass().getResourceAsStream("/images/hide.png"));
            showImage.setImage(hide);
            password = passwordField.getText();
            passwordField.setPromptText(password);
            passwordField.clear();
        });
        showPassButton.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            Image show = new Image(getClass().getResourceAsStream("/images/show.png"));
            showImage.setImage(show);
            passwordField.setPromptText("Password");
            passwordField.setText(password);
        });
    }

    // switch to registration scene
    private void switchToRegistration(MouseEvent event) {
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
            dashboardController.setUser(user);
            dashboardController.loadStackPane("dashboard");

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
            userMessage.setVisible(true);
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
