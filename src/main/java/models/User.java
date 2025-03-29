package models;

import Exceptions.CannotSendMoneyToYourself;
import Exceptions.IncorrectAccountNumber;
import Exceptions.NotPositiveAmountException;
import Exceptions.CannotWithdrawException;
import enums.TransactionType;
import services.Database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private BigDecimal balance;
    private String accountNumber;
    private List<Transaction> transactions;
    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.balance = new BigDecimal(1000).setScale(2);
        this.accountNumber = AccountNumber.accountNumberGenerator();
        this.transactions = new ArrayList<>();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public BigDecimal getBalance() { return balance; }

    public List<Transaction> getTransactions() { return transactions; }

    public void performTransaction(Transaction transaction) throws NotPositiveAmountException {
        if (transaction.amount.compareTo(BigDecimal.ZERO) > 0) {
            switch (transaction.type) {
                case ADDTOBALANCE -> {
                    setBalance(this.balance.add(transaction.amount));
                    System.out.println("balance topped by " + transaction.amount);
                }
                case WITHDRAWAL -> {
                    if (this.balance.compareTo(transaction.amount) >= 0) {
                        transaction.amount = transaction.amount.negate();
                        setBalance(this.balance.add(transaction.amount));
                        System.out.println("from balance withdrawn " + transaction.amount);
                    } else throw new CannotWithdrawException("withdraw");
                }
            }
        } else throw new NotPositiveAmountException();
        transactions.add(transaction);
    }

    public void performTransaction(Transaction transaction, User recipient) throws NotPositiveAmountException, CannotSendMoneyToYourself {
        if(this.getAccountNumber().equals(recipient.getAccountNumber())){
            throw new CannotSendMoneyToYourself();
        }
        if (transaction.amount.compareTo(BigDecimal.ZERO) > 0){
            if(this.balance.compareTo(transaction.amount) >= 0) {
                try{
                    recipient.setBalance(recipient.getBalance().add(transaction.amount));
                }catch (NullPointerException e){
                    throw new IncorrectAccountNumber();
                }
                Transaction recipientTransaction =  new Transaction(transaction.amount, transaction.type);
                recipient.transactions.add(recipientTransaction);

                transaction.amount = transaction.amount.negate();
                setBalance(this.balance.add(transaction.amount));
                transactions.add(transaction);
            } else {
                throw new CannotWithdrawException("transfer");
            }
        } else throw new NotPositiveAmountException();
    }
// bubble sort by amount
    public List<Transaction> sortTransactionsByAmountAscending(String search) {
//        make copy of transactions
//        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        List<Transaction> sortedTransactions = searchedTransactions(search);
        for (int i = 0; i < sortedTransactions.size() - 1; i++) {
            for(int j = 0; j < sortedTransactions.size() - 1; j++) {
                if(sortedTransactions.get(j).amount.compareTo(sortedTransactions.get(j + 1).amount) > 0) {
                    Transaction swapTransaction = sortedTransactions.get(j);
                    sortedTransactions.set(j, sortedTransactions.get(j+1));
                    sortedTransactions.set(j+1, swapTransaction);
                }
            }
        }
        System.out.println(sortedTransactions);
        return sortedTransactions;
    }
//    reversed sort by amount
    public List<Transaction> sortTransactionsByAmountDescending(String search) {
        return sortTransactionsByAmountAscending(search).reversed();
    }

    public List<Transaction> searchedTransactions(String search) {
        if (search == null) {
            return new ArrayList<>(transactions);
        } else {
            List<Transaction> searched = new ArrayList<>();
            for (Transaction transaction : transactions) {
                if (transaction.getAmount().toString().contains(search) || transaction.date().contains(search) || transaction.getType().toString().contains(search.toUpperCase())) {
                    searched.add(transaction);
                }
            }
            return searched;
        }
    }
}
