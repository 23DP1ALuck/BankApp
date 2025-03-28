package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.User;
import services.Database;
import java.io.IOException;

public class Dashboard {
    private User currentUser;
    private Stage stage;
    private Scene scene;
    private Parent root;

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
    Button logOutButton;

    @FXML
    private void initialize() {
        // button action listeners
        navDashboardContainer.setOnMouseClicked(this::switchToDashboard);
        navPaymentContainer.setOnMouseClicked(this::switchToPayment);
        logOutButton.setOnMouseClicked(this::logOut);

//        hover for logout button
        logOutButton.setOnMouseEntered(e -> logOutButton.setStyle("-fx-background-color:  rgba(255,255,255,0.25);-fx-background-radius: 15;"));
        logOutButton.setOnMouseExited(e -> logOutButton.setStyle("-fx-background-color:  rgba(255,255,255,0.2);-fx-background-radius: 15;"));
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
            navDashboardContainer.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 12.5;");
            navPaymentContainer.setStyle("");
            DashboardStack dashboardStackController = loader.getController();
            dashboardStackController.setUser(currentUser);
        } else {
            navPaymentContainer.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 12.5;");
            navDashboardContainer.setStyle("");
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
    public void logOut(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setWidth(603);
            stage.setHeight(550);
            stage.centerOnScreen();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }
}
