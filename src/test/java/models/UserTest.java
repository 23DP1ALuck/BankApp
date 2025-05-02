package models;

import Exceptions.CannotWithdrawException;
import Exceptions.NotPositiveAmountException;
import enums.TransactionType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {

//    performTransaction() methods tests

    @Test
    public void testWithdrawalMoneyAmountBiggerThanBalanceThrowsError() {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("1000.00"));
        Transaction transactionAmountLessThanBalance = new Transaction(new BigDecimal("1005.00"), TransactionType.WITHDRAWAL);

        Exception exception = assertThrows(CannotWithdrawException.class, () -> {
            user.performTransaction(transactionAmountLessThanBalance);
        });
        String expectedMessage = "Cannot withdraw (not HUJ money).";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testWithdrawalMoneyAmountLessThanZeroThrowsError() {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("1000.00"));
        Transaction transactionAmountLessThanZero = new Transaction(new BigDecimal("-1000.00"), TransactionType.WITHDRAWAL);

        Exception exception = assertThrows(NotPositiveAmountException.class, () -> {
            user.performTransaction(transactionAmountLessThanZero);
        });
        String expectedMessage = "Amount must be positive.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testWithdrawalSuccessful() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("1000.00"));
        Transaction transaction = new Transaction(new BigDecimal("999.00"), TransactionType.WITHDRAWAL);
        user.performTransaction(transaction);

        assertEquals(new BigDecimal("1.00"), user.getBalance());
    }
    @Test
    public void testAddToBalanceSuccessful() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("1000.00"));
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("999.00"), TransactionType.ADDTOBALANCE);
        user.performTransaction(transaction);

        assertEquals(new BigDecimal("1999.00"), user.getBalance());
    }
    @Test
    public void testAddToBalanceAmountNotPositiveThrowError() {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("1000.00"));
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("-1000.00"), TransactionType.ADDTOBALANCE);
        Exception exception = assertThrows(NotPositiveAmountException.class, () -> {
            user.performTransaction(transaction);
        });
        String expectedMessage = "Amount must be positive.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testFirstTopUpSuccessfulAndCheckIfTransactionWithTypeBonusAdded() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("0.00"));

        Transaction transaction = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
//        should give +10% of amount if first top up is below 100 EUR
//        assertEquals(user.getBalance(), new BigDecimal("1100.00"));
        List<Transaction> transactions = user.getTransactions();
        assertEquals("Second transaction's type should be BONUS", transactions.get(1).getType(), TransactionType.BONUS);
    }
    @Test
    public void testFirstTopUpLessThanHundredEuro() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setBalance(new BigDecimal("0.00"));
        Transaction transaction = new Transaction(new BigDecimal("50.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
        assertEquals(user.getBalance(), new BigDecimal("50.00"));
    }


    @Test
    public void sortTransactionsByAmountAscendingDefault() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("700.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction3 = new Transaction(new BigDecimal("900.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction4 = new Transaction(new BigDecimal("100.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction5 = new Transaction(new BigDecimal("2000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction6 = new Transaction(new BigDecimal("30000.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
        user.performTransaction(transaction2);
        user.performTransaction(transaction3);
        user.performTransaction(transaction4);
        user.performTransaction(transaction5);
        user.performTransaction(transaction6);

        List<Transaction> sortedTransactions = user.sortTransactionsByAmountAscending("");

        assertEquals(sortedTransactions.get(0).getAmount(), new BigDecimal("100.00"));
        assertEquals(sortedTransactions.get(1).getAmount(), new BigDecimal("700.00"));
        assertEquals(sortedTransactions.get(2).getAmount(), new BigDecimal("900.00"));
        assertEquals(sortedTransactions.get(3).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(4).getAmount(), new BigDecimal("2000.00"));
        assertEquals(sortedTransactions.get(5).getAmount(), new BigDecimal("30000.00"));
    }
    @Test
    public void sortTransactionsByAmountAscendingWithSimilar() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction3 = new Transaction(new BigDecimal("900.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction4 = new Transaction(new BigDecimal("20000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction5 = new Transaction(new BigDecimal("20000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction6 = new Transaction(new BigDecimal("11000.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
        user.performTransaction(transaction2);
        user.performTransaction(transaction3);
        user.performTransaction(transaction4);
        user.performTransaction(transaction5);
        user.performTransaction(transaction6);

        List<Transaction> sortedTransactions = user.sortTransactionsByAmountAscending("");

        assertEquals(sortedTransactions.get(0).getAmount(), new BigDecimal("900.00"));
        assertEquals(sortedTransactions.get(1).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(2).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(3).getAmount(), new BigDecimal("11000.00"));
        assertEquals(sortedTransactions.get(4).getAmount(), new BigDecimal("20000.00"));
        assertEquals(sortedTransactions.get(5).getAmount(), new BigDecimal("20000.00"));
    }


    @Test
    public void sortTransactionsByAmountDescendingDefault() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("700.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction3 = new Transaction(new BigDecimal("900.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction4 = new Transaction(new BigDecimal("100.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction5 = new Transaction(new BigDecimal("2000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction6 = new Transaction(new BigDecimal("30000.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
        user.performTransaction(transaction2);
        user.performTransaction(transaction3);
        user.performTransaction(transaction4);
        user.performTransaction(transaction5);
        user.performTransaction(transaction6);

        List<Transaction> sortedTransactions = user.sortTransactionsByAmountDescending("");

        assertEquals(sortedTransactions.get(0).getAmount(), new BigDecimal("30000.00"));
        assertEquals(sortedTransactions.get(1).getAmount(), new BigDecimal("2000.00"));
        assertEquals(sortedTransactions.get(2).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(3).getAmount(), new BigDecimal("900.00"));
        assertEquals(sortedTransactions.get(4).getAmount(), new BigDecimal("700.00"));
        assertEquals(sortedTransactions.get(5).getAmount(), new BigDecimal("100.00"));
    }
    @Test
    public void sortTransactionsByAmountDescendingWithSimilar() throws NotPositiveAmountException {
        User user = new User("Test", "Test", "Test", "Test");
        user.setFirstTopUp(false);
        Transaction transaction = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction3 = new Transaction(new BigDecimal("900.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction4 = new Transaction(new BigDecimal("20000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction5 = new Transaction(new BigDecimal("20000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction6 = new Transaction(new BigDecimal("11000.00"), TransactionType.ADDTOBALANCE);

        user.performTransaction(transaction);
        user.performTransaction(transaction2);
        user.performTransaction(transaction3);
        user.performTransaction(transaction4);
        user.performTransaction(transaction5);
        user.performTransaction(transaction6);

        List<Transaction> sortedTransactions = user.sortTransactionsByAmountDescending("");

        assertEquals(sortedTransactions.get(0).getAmount(), new BigDecimal("20000.00"));
        assertEquals(sortedTransactions.get(1).getAmount(), new BigDecimal("20000.00"));
        assertEquals(sortedTransactions.get(2).getAmount(), new BigDecimal("11000.00"));
        assertEquals(sortedTransactions.get(3).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(4).getAmount(), new BigDecimal("1000.00"));
        assertEquals(sortedTransactions.get(5).getAmount(), new BigDecimal("900.00"));
    }
    

//
//    @Test
//    public void searchedTransactions() {
//    }
}