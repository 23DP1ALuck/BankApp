package models;

import enums.TransactionType;

public class Transaction {
    public int id;
    public int amount;
    TransactionType type;
    public Transaction(int id, int amount, TransactionType type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }
}
