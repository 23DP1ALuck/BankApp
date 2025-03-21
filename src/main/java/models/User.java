package models;

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
        if (transaction.amount.compareTo(BigDecimal.ZERO) >= 0) {
            switch (transaction.type) {
                case ADDTOBALANCE -> {
                    setBalance(this.balance.add(transaction.amount));
                    System.out.println("balance topped by " + transaction.amount);
                }
                case WITHDRAWAL -> {
                    if (this.balance.compareTo(transaction.amount) > 0) {
                        transaction.amount = transaction.amount.negate();
                        setBalance(this.balance.add(transaction.amount));
                        System.out.println("from balance withdrawn " + transaction.amount);
                    } else throw new CannotWithdrawException("withdrawal");
                }
            }
        } else throw new NotPositiveAmountException();
        transactions.add(transaction);
    }

    public void performTransaction(Transaction transaction, User recipient) {
        if (transaction.amount.compareTo(BigDecimal.ZERO) > 0){
            if(this.balance.compareTo(transaction.amount) >= 0) {
                transaction.amount = transaction.amount.negate();
                setBalance(this.balance.add(transaction.amount));
                try{
                    recipient.setBalance(recipient.getBalance().add(transaction.amount.negate()));
                }catch (NullPointerException e){
                    throw new IncorrectAccountNumber();
                }
                transactions.add(transaction);
                Transaction recipientTransaction =  new Transaction(transaction.amount.negate(), transaction.type);
                recipient.transactions.add(recipientTransaction);
            } else {
                throw new CannotWithdrawException("transfer.");
            }
        }
    }
}
