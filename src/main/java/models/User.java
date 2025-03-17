package models;

import Exceptions.NotPositiveAmountException;
import Exceptions.CannotWithdrawException;

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
        this.balance = BigDecimal.valueOf(1000.0);
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

    public void performTransaction(Transaction transaction) throws NotPositiveAmountException {
        if (transaction.amount.compareTo(BigDecimal.ZERO) > 0) {
            switch (transaction.type) {
                case ADDTOBALANCE -> {
                    setBalance(this.balance.add(transaction.amount));
                    System.out.println("balance topped by " + transaction.amount);
                }
                case WITHDRAWAL -> {
                    if (this.balance.compareTo(transaction.amount) > 0) {
                        setBalance(this.balance.subtract(transaction.amount));
                        System.out.println("from balance withdrawn " + transaction.amount);
                    } else throw new CannotWithdrawException();
                }
//            case TRANSFER -> {
//            }
            }
        } else throw new NotPositiveAmountException();
        transactions.add(transaction);
    }
}
