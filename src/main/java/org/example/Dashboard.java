package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import models.User;
import services.Database;

import java.io.IOException;

public class Dashboard {
    @FXML
    StackPane stackPaneContainer;
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
    public void setStackPane(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardStack.fxml"));
        AnchorPane newContent = loader.load();

        DashboardStack dashboardStackContainer = loader.getController();
        dashboardStackContainer.setUser(user);

        stackPaneContainer.getChildren().setAll(newContent);

    }
}
