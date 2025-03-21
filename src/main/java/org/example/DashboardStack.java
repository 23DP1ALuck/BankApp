package org.example;

import enums.TransactionType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Transaction;
import models.User;
import services.Database;

import java.math.BigDecimal;

public class DashboardStack {
    private Database db = Database.getInstance();
    private User currentUser;
    @FXML
    Label balance;
    @FXML
    Label moneyIn;
    @FXML
    Label moneyOut;
    public void setUser(User user) {
        currentUser = user;
        setBalanceValue();
        setMoneyIn();
        setMoneyOut();
    }

    public void setBalanceValue(){
        balance.setText(currentUser.getBalance() + "€");
    }
    public void setMoneyIn() { moneyIn.setText(db.moneyIn(currentUser).toString() + "€");}
    public void setMoneyOut() { moneyOut.setText(db.moneyOut(currentUser).toString() + "€");}
}
