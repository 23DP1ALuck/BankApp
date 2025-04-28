package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import models.User;

import java.io.IOException;

public class Dashboard {
    private User currentUser;
    private Stage stage;
    private Scene scene;

    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private Label accountNumber, username, nameAndLastName;
    @FXML
    private Pane navDashboardContainer, navPaymentContainer, navSettingsContainer;
    @FXML
    private Button logOutButton;

    @FXML
    private void initialize() {
        // button action listeners
        navDashboardContainer.setOnMouseClicked(this::switchToDashboard);
        navPaymentContainer.setOnMouseClicked(this::switchToPayment);
        navSettingsContainer.setOnMouseClicked(this::switchToSettings);
        logOutButton.setOnAction(this::logOut);
        accountNumber.setOnMouseClicked(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(accountNumber.getText());
            clipboard.setContent(content);
        });

        // hover for logout button
        logOutButton.setOnMouseEntered(e -> logOutButton.setStyle("-fx-background-color:  rgba(255,255,255,0.25);-fx-background-radius: 15;"));
        logOutButton.setOnMouseExited(e -> logOutButton.setStyle("-fx-background-color:  rgba(255,255,255,0.2);-fx-background-radius: 15;"));
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
    public void setHelloUsername(String usersname) {
        username.setText("@"+usersname);
    }
    public void setNameAndLastName(String name, String surname) {
        nameAndLastName.setText(name + " " + surname);
    }
    public void setAccountNumber(String accountNum) {
        accountNumber.setText(accountNum);
    }

    // loads chosen stackPane
    public void loadStackPane(String stackName) throws IOException {
        String fxml;
        switch (stackName) {
            case "dashboard" -> fxml = "/dashboardStack.fxml";
            case "payment" -> fxml = "/paymentStack.fxml";
            case "settings" -> fxml = "/settingsStack.fxml";
            default -> {
                return;
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        AnchorPane newContent = loader.load();

        if (stackName.equals("dashboard")) {
            navDashboardContainer.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 12.5;");
            navPaymentContainer.setStyle("");
            navSettingsContainer.setStyle("");
            DashboardStack dashboardStackController = loader.getController();
            dashboardStackController.setUser(currentUser);
        } else if (stackName.equals("settings")) {
            navSettingsContainer.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 12.5;");
            navPaymentContainer.setStyle("");
            navDashboardContainer.setStyle("");
            SettingsStack settingsStackController = loader.getController();
            settingsStackController.setUser(currentUser);
            settingsStackController.setController(this);
        } else {
            navPaymentContainer.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 12.5;");
            navDashboardContainer.setStyle("");
            navSettingsContainer.setStyle("");
            PaymentStack paymentStackController = loader.getController();
            paymentStackController.setUser(currentUser);
        }

        stackPaneContainer.getChildren().setAll(newContent);
    }

    // shows dashboard stackPane
    private void switchToDashboard(MouseEvent event) {
        try {
            loadStackPane("dashboard");
        } catch (IOException e) {}

    }
    // shows payment stackPane
    private void switchToPayment(MouseEvent event) {
        try {
            loadStackPane("payment");
        } catch (IOException e) {}
    }
    // shows settings stackPane
    private void switchToSettings(MouseEvent event) {
        try {
            loadStackPane("settings");
        } catch (IOException e) {}
    }

    public void logOut(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.centerOnScreen();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }
}
