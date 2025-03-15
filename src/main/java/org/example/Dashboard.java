package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.Database;

public class Dashboard {
    @FXML
    Label accountNumber;
    @FXML
    Label username;
    @FXML
    Label nameAndLastName;
    public void setHelloUsername(String usersname) {
        username.setText("@"+usersname);
    }
    public void setNameAndLastName(String name) {
        nameAndLastName.setText(name);
    }
    public void setAccountNumber(String name) {
        this.accountNumber.setText(name);
    }
}
