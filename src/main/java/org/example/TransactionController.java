package org.example;

import enums.TransactionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Transaction;
import models.User;

public class TransactionController {
    @FXML
    Label transactionType;
    @FXML
    Label transactionDateTime;
    @FXML
    Label transactionAmount;

    User currentUser;
    public void initialize() {
    }
    public void setUser(User user) {
        currentUser = user;
    }
    public String getPrettyTransactionType(Transaction transaction){
        switch (transaction.getType()){
            case WITHDRAWAL -> {
                return "Withdrawal";
            }
            case TRANSFER -> {
                return "Transfer";
            }
            case ADDTOBALANCE -> {
                return "Add Balance";
            }
        } return null;
    }
    public void setTransactionData(Transaction transaction) {
        transactionAmount.setText(transaction.getAmount().toString() + "€");
        transactionType.setText(getPrettyTransactionType(transaction));
        transactionDateTime.setText(transaction.getDate().toLocalDate() + " at " + transaction.getDate().toLocalTime());
    }
}
