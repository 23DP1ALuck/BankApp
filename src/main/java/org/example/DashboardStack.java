package org.example;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;
import java.util.List;

public class DashboardStack {
    private Database db = Database.getInstance();
    private User currentUser;
    private String activeSort = "sortByDateUp";
    @FXML
    private Label balance, moneyIn, moneyOut;
    @FXML
    private TextField searchField;
    @FXML
    private Button sortByDate, sortByPrice;
    @FXML
    private ImageView priceArrow, dateArrow;
    @FXML
    private VBox vbox;

    @FXML
    private void initialize() {
        // button action listeners
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

    private void setBalanceValue() {
        balance.setText(currentUser.getBalance() + "€");
    }
    private void setMoneyIn() {
        moneyIn.setText(db.moneyIn(currentUser).toString() + "€");
    }
    private void setMoneyOut() {
        moneyOut.setText(db.moneyOut(currentUser).toString() + "€");
    }

    // methods with ActionEvent to assign buttons to corresponding methods
    private void dateButton(ActionEvent event) throws IOException { changeToDate(); }
    private void priceButton(ActionEvent event) throws IOException { changeToPrice(); }

    private void changeToDate() throws IOException {
        if (sortByDate.getStyleClass().getFirst().equals("activeButton")) {
            if (activeSort.equals("sortByDateDown")) {
                dateUp();
            } else {
                dateDown();
            }
        } else {
            dateUp();

            sortByDate.getStyleClass().clear();
            sortByDate.getStyleClass().add("activeButton");
            dateArrow.getStyleClass().add("activeArrow");

            sortByPrice.getStyleClass().clear();
            sortByPrice.getStyleClass().add("inactiveButton");
            priceArrow.getStyleClass().clear();
            priceArrow.getStyleClass().add("inactiveArrow");
        }

    }
    private void changeToPrice() throws IOException {
        if (sortByPrice.getStyleClass().getFirst().equals("activeButton")) {
            if (activeSort.equals("sortByPriceDown")) {
                priceUp();
            } else {
                priceDown();
            }
        } else {
            priceUp();

            sortByPrice.getStyleClass().clear();
            sortByPrice.getStyleClass().add("activeButton");
            priceArrow.getStyleClass().add("activeArrow");

            sortByDate.getStyleClass().clear();
            sortByDate.getStyleClass().add("inactiveButton");
            dateArrow.getStyleClass().clear();
            dateArrow.getStyleClass().add("inactiveArrow");
        }

    }

    // clear vbox when new sort method called
    private void clearVBox(){
        vbox.getChildren().clear();
    }

    // sort by date descending
    private void dateDown() throws IOException {
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
    private void dateUp() throws IOException {
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
    private void priceDown() throws IOException {
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
    private void priceUp() throws IOException {
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
