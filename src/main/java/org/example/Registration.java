package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.Database;
import Exceptions.FieldsAreBlankException;
import Exceptions.UserExistsException;
import java.io.IOException;

public class Registration {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField surnameField;
    @FXML
    private Label userMessage;
    @FXML
    private Label switchToLogin;
    @FXML
    private Button signUpButton;

    @FXML
    private void initialize() {
        // button action listeners
        switchToLogin.setOnMouseClicked(this::switchToLogin);
        signUpButton.setOnAction(this::signUpButtonHandler);

        // hides userMessage if something is typed
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        // hides userMessage by default
        userMessage.setVisible(false);
    }

    // action on sign up button
    private void signUpButtonHandler(ActionEvent event){
        try {
            if (signUp()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                root = loader.load();

                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (UserExistsException | FieldsAreBlankException | IOException e){
            userMessage.setText(e.getMessage());
            userMessage.setVisible(true);
        }
    }

    // switch to log in scene
    private void switchToLogin(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }

    Database database = Database.getInstance();

    // sign up func
    private boolean signUp() throws UserExistsException, FieldsAreBlankException, IOException {
        userMessage.setText("");
        checkIfBlank();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String name = this.nameField.getText();
        String surname = this.surnameField.getText();
        database.addUserToDatabase(username, password, name, surname);
        // just reminder with login details
        System.out.printf("login: %s, password: %s\n", username, password);
        return true;
    }

    // blank field check
    private void checkIfBlank() throws FieldsAreBlankException {
        if(this.usernameField.getText().isBlank() || this.passwordField.getText().isBlank()){
            throw new FieldsAreBlankException();
        }
    }
}
