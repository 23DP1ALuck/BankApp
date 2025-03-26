package models;

import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction {
//    public int id;
    public BigDecimal amount;
    TransactionType type;
    LocalDateTime date;

//    public Transaction(int id, int amount, TransactionType type) {
//        this.id = id;
//        this.amount = amount;
//        this.type = type;
//    }
    public Transaction(BigDecimal amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public BigDecimal getAmount() { return amount; }

    public LocalDateTime getDate() { return date; }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Amount : " + getAmount() + "Type: " + getType() + "Date: " + getDate();
    }
}
