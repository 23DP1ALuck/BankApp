package models;

import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Transaction {
//    public int id;
    public BigDecimal amount;
    TransactionType type;
//    LocalDate date;

//    public Transaction(int id, int amount, TransactionType type) {
//        this.id = id;
//        this.amount = amount;
//        this.type = type;
//    }
    public Transaction(BigDecimal amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
//        this.date = LocalDate.now();
    }
    public TransactionType getType() {
        return type;
    }
}
