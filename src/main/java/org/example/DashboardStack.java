package org.example;

import enums.TransactionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    @FXML
    TextField searchField;
    @FXML
    Button sortByDate;
    @FXML
    Button sortByPrice;
    @FXML
    ImageView priceArrow;
    @FXML
    ImageView dateArrow;

    @FXML
    private void initialize() {
        sortByDate.setOnAction(this::dateButton);
        sortByPrice.setOnAction(this::priceButton);
        changeToDate();
    }

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

    public void dateButton(ActionEvent event) { changeToDate(); }
    public void priceButton(ActionEvent event) { changeToPrice(); }

    public void changeToDate() {
        if (sortByDate.getStyleClass().getFirst().equals("activeButton")) {
            if (dateArrow.getStyleClass().getFirst().equals("arrowDown")) {
                dateUp();
            } else {
                dateDown();
            }
        } else {
            dateDown();
        }

        sortByDate.getStyleClass().clear();
        sortByDate.getStyleClass().add("activeButton");
        dateArrow.getStyleClass().add("activeArrow");

        sortByPrice.getStyleClass().clear();
        sortByPrice.getStyleClass().add("inactiveButton");
        priceUp();
        priceArrow.getStyleClass().add("inactiveArrow");
    }
    public void changeToPrice() {
        if (sortByPrice.getStyleClass().getFirst().equals("activeButton")) {
            if (priceArrow.getStyleClass().getFirst().equals("arrowDown")) {
                priceUp();
            } else {
                priceDown();
            }
        } else {
            priceUp();
        }

        sortByPrice.getStyleClass().clear();
        sortByPrice.getStyleClass().add("activeButton");
        priceArrow.getStyleClass().add("activeArrow");

        sortByDate.getStyleClass().clear();
        sortByDate.getStyleClass().add("inactiveButton");
        dateDown();
        dateArrow.getStyleClass().add("inactiveArrow");
    }

    public void dateDown() {
        dateArrow.getStyleClass().clear();
        dateArrow.getStyleClass().add("arrowDown");
    }
    public void dateUp() {
        dateArrow.getStyleClass().clear();
    }
    public void priceDown() {
        priceArrow.getStyleClass().clear();
        priceArrow.getStyleClass().add("arrowDown");
    }
    public void priceUp() {
        priceArrow.getStyleClass().clear();
    }
}
