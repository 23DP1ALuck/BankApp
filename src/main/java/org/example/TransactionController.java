package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import models.Transaction;
import models.User;

public class TransactionController {
    @FXML
    private Label transactionType, transactionDateTime, transactionAmount;

    User currentUser;

    public void setUser(User user) {
        currentUser = user;
    }

    private String getPrettyTransactionType(Transaction transaction){
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
            case BONUS -> {
                return "First top up bonus";
            }
        } return null;
    }

    public void setTransactionData(Transaction transaction) {
        transactionAmount.setText(transaction.getAmount().toString() + "€");
        transactionType.setText(getPrettyTransactionType(transaction));
        transactionDateTime.setText(transaction.getDate().toLocalDate() + " at " + transaction.getDate().toLocalTime());
    }
}
