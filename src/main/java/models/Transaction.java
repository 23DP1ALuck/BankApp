package models;

import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Transaction {
    public BigDecimal amount;
    public TransactionType type;
    public LocalDateTime date;

    public Transaction(BigDecimal amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    // another getter for searching
    public String date() { return String.valueOf(date.getYear()) + date.getMonthValue() + date.getDayOfMonth() + date.getHour() + date.getMinute(); }

    @Override
    public String toString() {
        return "Amount : " + getAmount() + "Type: " + getType() + "Date: " + getDate();
    }
}
