package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.User;
import services.Database;
import java.io.IOException;

public class Dashboard {
    private User currentUser;

    @FXML
    StackPane stackPaneContainer;
    @FXML
    Label accountNumber;
    @FXML
    Label username;
    @FXML
    Label nameAndLastName;
    @FXML
    Pane navDashboardContainer, navPaymentContainer;

    @FXML
    private void initialize() {
        // button action listeners
        navDashboardContainer.setOnMouseClicked(this::switchToDashboard);
        navPaymentContainer.setOnMouseClicked(this::switchToPayment);
    }

    public void setUser(User user) { this.currentUser = user; }
    public void setHelloUsername(String usersname) {
        username.setText("@"+usersname);
    }
    public void setNameAndLastName(String name, String surname) { nameAndLastName.setText(name + " " + surname); }
    public void setAccountNumber(String accountNum) {
        accountNumber.setText(accountNum);
    }

    // loads chosen stackPane
    public void loadStackPane(String stackName) throws IOException {
        String fxml;
        if (stackName.equals("dashboard")) {
            fxml = "/dashboardStack.fxml";
        } else if (stackName.equals("payment")) {
            fxml = "/paymentStack.fxml";
        } else return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        AnchorPane newContent = loader.load();

        if (stackName.equals("dashboard")) {
            DashboardStack dashboardStackController = loader.getController();
            dashboardStackController.setUser(currentUser);
        } else {
            PaymentStack paymentStackController = loader.getController();
            paymentStackController.setUser(currentUser);
        }

        stackPaneContainer.getChildren().setAll(newContent);
    }

    // shows dashboard stackPane
    public void switchToDashboard(MouseEvent event) {
        try {
            loadStackPane("dashboard");
        } catch (IOException e) {}

    }
    // shows payment stackPane
    public void switchToPayment(MouseEvent event) {
        try {
            loadStackPane("payment");
        } catch (IOException e) {}
    }
}
