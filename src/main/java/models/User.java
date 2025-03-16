package models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private double balance;
    private String accountNumber;

    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.balance = 1000.0;
        this.accountNumber = AccountNumber.accountNumberGenerator();
        List<Transaction> transactions = new ArrayList<>();
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

    public void setBalance(double balance) { this.balance = balance; }

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

    public Double getBalance() { return balance; }
}
