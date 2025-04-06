package org.example;

import enums.TransactionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Transaction;
import models.User;
import services.Database;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardStack {
    private Database db = Database.getInstance();
    private User currentUser;
    private String activeSort = "sortByDateUp";
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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Method sort = DashboardStack.class.getDeclaredMethod(activeSort);
                sort.setAccessible(true);
                sort.invoke(this);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
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

    //    sort by date descending
    public void dateDown() throws IOException {
        dateArrow.getStyleClass().clear();
        dateArrow.getStyleClass().add("arrowDown");
        sortByDateDown();
        activeSort = "sortByDateDown";
    }
    private void sortByDateDown() throws IOException {
        clearVBox();
        List<Transaction> sortedTransactions = currentUser.searchedTransactions(searchField.getText());
        for (int i = 0; i < sortedTransactions.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(sortedTransactions.get(i));
        }
    }
    //    sort by date ascending
    public void dateUp() throws IOException {
        dateArrow.getStyleClass().clear();
        currentUser.getTransactions().reversed();
        sortByDateUp();
        activeSort = "sortByDateUp";
    }
    private void sortByDateUp() throws IOException {
        clearVBox();
        List<Transaction> sortedTransactions = currentUser.searchedTransactions(searchField.getText()).reversed();
        for (int i = 0; i < sortedTransactions.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(sortedTransactions.get(i));
        }
    }
    //    sort by amount descending
    public void priceDown() throws IOException {
        priceArrow.getStyleClass().clear();
        priceArrow.getStyleClass().add("arrowDown");
        sortByPriceDown();
        activeSort = "sortByPriceDown";
    }
    private void sortByPriceDown() throws IOException{
        clearVBox();
        List<Transaction> sortedTransactions = currentUser.sortTransactionsByAmountDescending(searchField.getText());
        for (int i = 0; i < sortedTransactions.size(); i++) {
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
        sortByPriceUp();
        activeSort = "sortByPriceUp";
    }
    private void sortByPriceUp() throws IOException {
        clearVBox();
        List<Transaction> sortedTransactions = currentUser.sortTransactionsByAmountAscending(searchField.getText());
        for (int i = 0; i < sortedTransactions.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transaction.fxml"));
            Pane newContent = loader.load();
            vbox.getChildren().add(newContent);
            TransactionController transactionController = loader.getController();
            transactionController.setUser(currentUser);
            transactionController.setTransactionData(sortedTransactions.get(i));
        }
    }
}
