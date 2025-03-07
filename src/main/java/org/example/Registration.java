package org.example;

import Exceptions.IncorrectPassException;
import Exceptions.NoSuchUserException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.Database;
import Exceptions.FieldsAreBlankException;
import Exceptions.UserExistsException;

import java.io.IOException;

public class Registration {
    @FXML
    private AnchorPane registrationPage;
    @FXML
    private AnchorPane loginPage;
    @FXML
    private Button signInButton;
    @FXML
    private PasswordField passField;
    @FXML
    private TextField username;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private Button signUpButton;
    @FXML
    private Label alreadyExists;
    @FXML
    private TextField usernameInLoginPage;
    @FXML
    private PasswordField passFieldInLoginPage;
    @FXML
    private Button signInInLoginPage;
    @FXML
    private Button signUpInLoginPage;
    @FXML
    private Label error;

    @FXML
    private void initialize() {
//        button action listeners
        signUpButton.setOnAction(event -> {signUpButtonHandler();});
        signInButton.setOnAction(event -> {signInButtonHandler();});
        signUpInLoginPage.setOnAction(event -> {signUpInLoginPageHandler();});
        signInInLoginPage.setOnAction(event -> {signInInLoginPageHandler();});
    }
// sign up func
    private void signUpButtonHandler(){
        try {
            if(signUp()){
                registrationPage.setVisible(false);
                loginPage.setVisible(true);
            }
        }catch (UserExistsException | FieldsAreBlankException | IOException e){
            alreadyExists.setText(e.getMessage());
        }
    }
// change from login to register
    private void signInButtonHandler(){
        loginPage.setVisible(true);
        registrationPage.setVisible(false);
    }
// change from register to login
    private void signUpInLoginPageHandler(){
        registrationPage.setVisible(true);
        loginPage.setVisible(false);
    }
// sign in func
    private void signInInLoginPageHandler(){
        try{
            signIn();
        } catch (FieldsAreBlankException | NoSuchUserException | IncorrectPassException e){
            error.setText(e.getMessage());
        }
    }
    Database database = Database.getInstance();
    private boolean signUp() throws UserExistsException, FieldsAreBlankException, IOException {
        alreadyExists.setText("");
        checkIfBlank();
        String login = this.username.getText();
        String password = this.passField.getText();
        String name = this.name.getText();
        String surname = this.surname.getText();
        database.addUserToDatabase(login, password, name, surname);
//        just reminder with login details
        System.out.printf("login: %s, password: %s\n", login, password);
        return true;
    }
    private void signIn() throws FieldsAreBlankException, NoSuchUserException {
        error.setText("");
        checkIfBlankInLogin();
        String login = this.usernameInLoginPage.getText();
        String password = this.passFieldInLoginPage.getText();
        if(database.checkUser(login, password)){
            goToDashboard();
        }
    }
    private void checkIfBlank() throws FieldsAreBlankException {
        if(this.username.getText().isBlank() || this.passField.getText().isBlank()){
            throw new FieldsAreBlankException();
        }
    }
    private void checkIfBlankInLogin() throws FieldsAreBlankException {
        if(this.usernameInLoginPage.getText().isBlank() || this.passFieldInLoginPage.getText().isBlank()){
            throw new FieldsAreBlankException();
        }
    }

    private void goToDashboard(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            AnchorPane dashboardPage = loader.load();

            Dashboard dashboardController = loader.getController();
            dashboardController.setHelloUsername(this.usernameInLoginPage.getText());

            Scene dashboardScene = new Scene(dashboardPage);
            Stage stage = (Stage) registrationPage.getScene().getWindow();

            stage.setScene(dashboardScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
