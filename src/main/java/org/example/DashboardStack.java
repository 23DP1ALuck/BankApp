package org.example;

import enums.TransactionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Transaction;
import models.User;
import services.Database;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    VBox vbox;
    FXMLLoader loader;
    @FXML
    private void initialize() {
        sortByDate.setOnAction(event -> {
            try {
                dateButton(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sortByPrice.setOnAction(event -> {
            try {
                priceButton(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        vbox.setSpacing(10);

    }

    public void setUser(User user) throws IOException {
        currentUser = user;
        setBalanceValue();
        changeToDate();
        setMoneyIn();
        setMoneyOut();
    }

    public void setBalanceValue(){
        balance.setText(currentUser.getBalance() + "€");
    }
    public void setMoneyIn() { moneyIn.setText(db.moneyIn(currentUser).toString() + "€");}
    public void setMoneyOut() { moneyOut.setText(db.moneyOut(currentUser).toString() + "€");}

    public void dateButton(ActionEvent event) throws IOException { changeToDate(); }
    public void priceButton(ActionEvent event) throws IOException { changeToPrice(); }

    public void changeToDate() throws IOException {
        if (sortByDate.getStyleClass().getFirst().equals("activeButton")) {
            if (dateArrow.getStyleClass().getFirst().equals("arrowDown")) {
                dateUp();
            } else {
                dateDown();
            }
        } else {
            dateUp();
        }

        sortByDate.getStyleClass().clear();
        sortByDate.getStyleClass().add("activeButton");
        dateArrow.getStyleClass().add("activeArrow");

        sortByPrice.getStyleClass().clear();
        sortByPrice.getStyleClass().add("inactiveButton");

        priceArrow.getStyleClass().add("inactiveArrow");
    }
    public void changeToPrice() throws IOException {
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
        dateArrow.getStyleClass().add("inactiveArrow");
    }
//    clear vbox when new sort method called
    public void clearVBox(){
        vbox.getChildren().clear();
    }
    //    sort by date descending button handle
    public void dateDown() throws IOException {
        dateArrow.getStyleClass().clear();
        dateArrow.getStyleClass().add("arrowDown");
        clearVBox();
        for (int i = 0; i < currentUser.getTransactions().size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(currentUser.getTransactions().get(i));
        }
    }
//    sort by date ascending
    public void dateUp() throws IOException {
        dateArrow.getStyleClass().clear();
        currentUser.getTransactions().reversed();
        List<Transaction> transactions = new ArrayList<>(currentUser.getTransactions()).reversed();
        clearVBox();
        for (int i = 0; i < transactions.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(transactions.get(i));
        }
    }
    //    sort by amount descending
    public void priceDown() throws IOException {
        priceArrow.getStyleClass().clear();
        priceArrow.getStyleClass().add("arrowDown");
        List<Transaction> sortedTransactions = currentUser.sortTransactionsByAmountDescending();
        clearVBox();
        for (int i = 0; i < currentUser.sortTransactionsByAmountDescending().size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(sortedTransactions.get(i));
        }
    }
    //    sort by amount ascending
    public void priceUp() throws IOException {
        priceArrow.getStyleClass().clear();
        List<Transaction> sortedTransactions = currentUser.sortTransactionsByAmountAscending();
        clearVBox();
        for (int i = 0; i < currentUser.sortTransactionsByAmountDescending().size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(sortedTransactions.get(i));
        }
    }
}
