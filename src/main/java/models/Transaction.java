package models;

import enums.TransactionType;

import java.time.LocalDate;


public class Transaction {
//    public int id;
    public int amount;
    TransactionType type;
//    LocalDate date;

//    public Transaction(int id, int amount, TransactionType type) {
//        this.id = id;
//        this.amount = amount;
//        this.type = type;
//    }
    public Transaction(int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
//        this.date = LocalDate.now();
    }
}
