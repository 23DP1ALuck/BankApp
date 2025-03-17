package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.User;
import services.Database;

public class DashboardStack {
    private Database db = Database.getInstance();
    private User currentUser;
    @FXML
    Label balance;
    public void setUser(User user) {
        currentUser = user;
        setBalanceValue();
    }

    public void setBalanceValue(){
        balance.setText(currentUser.getBalance() + "€");
    }
}
