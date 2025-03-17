package Exceptions;

public class NotPositiveAmountException extends Exception {
    public NotPositiveAmountException() { super("Amount must be positive."); }
}
