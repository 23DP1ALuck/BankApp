package org.example;
import Exceptions.CurrentPasswordIncorrect;
import Exceptions.NotValidPasswordException;
import Exceptions.NotValidUsernameException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.User;
import services.Database;
import services.Validator;

import java.io.IOException;

public class SettingsStack {
    private Database db = Database.getInstance();
    private User currentUser;
    private String username;
    private String name;
    private String surname;
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
            newPassField.setPromptText("Current password");
            newPassField.setText(newPass);
        });

        deleteAccountButton.setOnAction(event -> {
            try {
                deleteAccount(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setUser(User user) {
        currentUser = user;

        username = currentUser.getUsername();
        usernameField.setText(username);
        name = currentUser.getName();
        nameField.setText(name);
        surname = currentUser.getSurname();
        surnameField.setText(surname);

        updateApplyButtonState();
    }

    public void setController(Dashboard dashboard) {
        dashboardController = dashboard;
    }

    private void updateApplyButtonState() {
        applyButton.setDisable(usernameField.getText().equals(username)
                && nameField.getText().equals(name)
                && surnameField.getText().equals(surname)
                && newPassField.getText().isBlank());
    }
    Validator validator = new Validator();
    private void applyChanges(ActionEvent event) {
        userMessage.getStyleClass().clear();
        try {
            if (!newPassField.getText().isBlank()) {
                setNewPassword();
            }
            validator.validateUsername(usernameField.getText());
            currentUser.setUsername(usernameField.getText());
            currentUser.setName(nameField.getText());
            currentUser.setSurname(surnameField.getText());
            db.saveToJson(currentUser);
            dashboardController.setHelloUsername(usernameField.getText());
            dashboardController.setNameAndLastName(nameField.getText(), surnameField.getText());
            userMessage.setText("Account info changed.");
            userMessage.getStyleClass().add("completed");
            userMessage.setVisible(true);
        } catch (CurrentPasswordIncorrect | IOException | NotValidPasswordException | NotValidUsernameException e) {
            userMessage.getStyleClass().add("error");
            userMessage.setText(e.getMessage());
            userMessage.setVisible(true);
        }
    }

    private void setNewPassword() throws CurrentPasswordIncorrect{
        if (currPassField.getText().equals(currentUser.getPassword())) {
            validator.validatePass(newPassField.getText());
            currentUser.setPassword(newPassField.getText());
        } else throw new CurrentPasswordIncorrect();
    }

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
