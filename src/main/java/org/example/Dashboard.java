package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Dashboard {
    @FXML
    Label hello;

    public void setHelloUsername(String username) {
        hello.setText("Hello, " + username + " !");
    }
}
