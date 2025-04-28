package org.example;

import Exceptions.CurrentPasswordIncorrect;
import Exceptions.NotValidPasswordException;
import Exceptions.NotValidUsernameException;
import Exceptions.UserExistsException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import models.User;
import services.Database;
import services.Validator;

import java.io.IOException;

public class SettingsStack {
    private Database db = Database.getInstance();
    private User currentUser;
    private Dashboard dashboardController;
    private String currPass;
    private String newPass;

    @FXML
    private Label userMessage;
    @FXML
    private TextField usernameField, nameField, surnameField;
    @FXML
    private PasswordField currPassField, newPassField;
    @FXML
    private Button showCurrPassButton, showNewPassButton, applyButton, deleteAccountButton;
    @FXML
    private ImageView showCurrPassImage, showNewPassImage;

    @FXML
    private void initialize() {
        // button action listeners
        applyButton.setOnAction(this::applyChanges);

        ChangeListener<String> changeListener = (obs, oldVal, newVal) -> {
            updateApplyButtonState();
            userMessage.setVisible(false);
        };
        usernameField.textProperty().addListener(changeListener);
        nameField.textProperty().addListener(changeListener);
        surnameField.textProperty().addListener(changeListener);
        currPassField.textProperty().addListener(changeListener);
        newPassField.textProperty().addListener(changeListener);

        userMessage.setVisible(false);

        showCurrPassButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Image hide = new Image(getClass().getResourceAsStream("/images/hide.png"));
            showCurrPassImage.setImage(hide);
            currPass = currPassField.getText();
            currPassField.setPromptText(currPass);
            currPassField.clear();
        });
        showCurrPassButton.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            Image show = new Image(getClass().getResourceAsStream("/images/show.png"));
            showCurrPassImage.setImage(show);
            currPassField.setPromptText("Current password");
            currPassField.setText(currPass);
        });
        showNewPassButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Image hide = new Image(getClass().getResourceAsStream("/images/hide.png"));
            showNewPassImage.setImage(hide);
            newPass = newPassField.getText();
            newPassField.setPromptText(newPass);
            newPassField.clear();
        });
        showNewPassButton.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            Image show = new Image(getClass().getResourceAsStream("/images/show.png"));
            showNewPassImage.setImage(show);
            newPassField.setPromptText("New password");
            newPassField.setText(newPass);
        });

        deleteAccountButton.setOnAction(event -> {
            try {
                deleteAccount(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        applyButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
            newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    applyButton.fire();
                }
            });
        });
    }

    public void setUser(User user) {
        currentUser = user;

        usernameField.setText(currentUser.getUsername());
        nameField.setText(currentUser.getName());
        surnameField.setText(currentUser.getSurname());

        updateApplyButtonState();
    }

    public void setController(Dashboard dashboard) {
        dashboardController = dashboard;
    }

    // button is disabled if account credentials are the same as before
    private void updateApplyButtonState() {
        applyButton.setDisable(usernameField.getText().equals(currentUser.getUsername())
                && nameField.getText().equals(currentUser.getName())
                && surnameField.getText().equals(currentUser.getSurname())
                && newPassField.getText().isBlank());
    }

    Validator validator = new Validator();

    private void applyChanges(ActionEvent event) {
        userMessage.getStyleClass().clear();
        try {
            if (!newPassField.getText().isBlank()) {
                setNewPassword();
            }
            if (!usernameField.getText().equals(currentUser.getUsername())) {
                validator.validateUsername(usernameField.getText());
                currentUser.setUsername(usernameField.getText());
            }
            currentUser.setName(nameField.getText());
            currentUser.setSurname(surnameField.getText());

            db.saveToJson(currentUser);

            updateApplyButtonState();
            currPassField.clear();
            newPassField.clear();
            dashboardController.setHelloUsername(usernameField.getText());
            dashboardController.setNameAndLastName(nameField.getText(), surnameField.getText());

            userMessage.setText("Account info changed.");
            userMessage.getStyleClass().add("completed");
            userMessage.setVisible(true);
        } catch (CurrentPasswordIncorrect | IOException | NotValidPasswordException | NotValidUsernameException | UserExistsException e) {
            userMessage.getStyleClass().add("error");
            userMessage.setText(e.getMessage());
            userMessage.setVisible(true);
        }
    }

    private void setNewPassword() throws CurrentPasswordIncorrect {
        if (currPassField.getText().equals(currentUser.getPassword())) {
            validator.validatePass(newPassField.getText());
            currentUser.setPassword(newPassField.getText());
        } else throw new CurrentPasswordIncorrect();
    }

    // delete account button with confirmation window
    private void deleteAccount(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        alert.setTitle("Delete account");
        alert.setHeaderText("Do you want to delete your account?");
        alert.setContentText("You can't undo this if you proceed");

        if (alert.showAndWait().get() == ButtonType.OK) {
            dashboardController.logOut(event);
            db.deleteUser(currentUser);
        }
    }
}
