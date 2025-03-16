package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    @FXML
    Pane navDashboardContainer;
    public void setHelloUsername(String usersname) {
        username.setText("@"+usersname);
    }
    public void setNameAndLastName(String name, String surname) { nameAndLastName.setText(name + " " + surname); }
    public void setAccountNumber(String accountNum) {
        accountNumber.setText(accountNum);
    }
    public void setStackPane(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardStack.fxml"));
        AnchorPane newContent = loader.load();

        DashboardStack dashboardStackContainer = loader.getController();
        dashboardStackContainer.setUser(user);

        stackPaneContainer.getChildren().setAll(newContent);
        navDashboardContainer.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 12.5");
    }

}
