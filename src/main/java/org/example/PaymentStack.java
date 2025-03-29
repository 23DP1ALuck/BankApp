package org.example;

import Exceptions.*;
import enums.TransactionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.Transaction;
import models.User;
import services.Database;

import javax.xml.stream.EventFilter;
import java.io.IOException;
import java.math.BigDecimal;

public class PaymentStack {
    private Database db = Database.getInstance();
    private User currentUser;

    @FXML
    private TextField accNumField;
    @FXML
    private TextField amountField;
    @FXML
    private RadioButton addMoneyButton, withdrawButton, transferButton;
    @FXML
    private Button completeButton;
    @FXML
    private ToggleGroup type;
    @FXML
    private Label userMessage;

    @FXML
    private void initialize() {
        // button action listeners
        completeButton.setOnAction(this::completeTransaction);
        addMoneyButton.setOnAction(this::radioChoice);
        withdrawButton.setOnAction(this::radioChoice);
        transferButton.setOnAction(this::radioChoice);

        // blocks to type unwanted symbols
        amountField.addEventFilter(KeyEvent.ANY, event ->{
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                return; // Allows backspace, left, right arrows
            }
            String c = event.getCharacter(); // Receives input
//            allows only numbers and dot. block if only single dot in input
            if(!c.matches("[0-9.]") || (c.equals(".") && amountField.getText().contains("."))){
                event.consume();
            }
//            split to two different parts. allows to type only two digits after dot
            if(amountField.getText().split("\\.").length > 1 && amountField.getText().split("\\.")[1].length() >= 2){
                event.consume();
            }
        });

        // hides userMessage if something is typed
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        accNumField.textProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setVisible(false);
        });
        // hides userMessage by default
        userMessage.setVisible(false);
        completeButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        completeButton.fire();
                    }
                });
            }
        });
    }

    public void setUser(User user) {
        currentUser = user;
        accNumField.setDisable(true);
    }

    public void radioChoice(ActionEvent event) {
        userMessage.setVisible(false);
        accNumField.clear();
        accNumField.setDisable(!type.getSelectedToggle().equals(transferButton));
    }

    public Transaction createTransaction() throws TypeOrFieldsNotSetException {
        TransactionType type;
        if (addMoneyButton.isSelected()) {
            type = TransactionType.ADDTOBALANCE;
        }
        else if (withdrawButton.isSelected()) {
            type = TransactionType.WITHDRAWAL;
        }
        else if (transferButton.isSelected()) {
            type = TransactionType.TRANSFER;
        }
        else type = null;

        if (type != null && !amountField.getText().isBlank() && (accNumField.isDisable() || (!accNumField.isDisable() && !accNumField.getText().isBlank())))  {
            BigDecimal amount = new BigDecimal(amountField.getText()).setScale(2);
            return new Transaction(amount, type);
        } else throw new TypeOrFieldsNotSetException();
    }

    public void completeTransaction(ActionEvent event) {
        userMessage.getStyleClass().clear();
        try {
            Transaction transaction = createTransaction();
            if(addMoneyButton.isSelected() || withdrawButton.isSelected()) {
                currentUser.performTransaction(transaction);
                db.saveToJson(currentUser);
                //                completed transaction label
                amountField.clear();
                userMessage.setText("Transaction completed.");
                userMessage.getStyleClass().add("completed");
                userMessage.setVisible(true);
            } else if (transferButton.isSelected()) {
                User recipient = db.getUserFromAccNumber(accNumField.getText());
                currentUser.performTransaction(transaction, db.getUserFromAccNumber(accNumField.getText()));
                db.saveToJson(currentUser);
                db.saveToJson(recipient);
//                clear when button pressed
                amountField.clear();
                accNumField.clear();
                //                completed transaction label
                userMessage.setText("Transaction completed.");
                userMessage.getStyleClass().add("completed");
                userMessage.setVisible(true);
            }
        } catch (NotPositiveAmountException | CannotWithdrawException | TypeOrFieldsNotSetException |
                 IncorrectAccountNumber |  CannotSendMoneyToYourself e) {
            userMessage.getStyleClass().add("error");
            userMessage.setText(e.getMessage());
            userMessage.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
