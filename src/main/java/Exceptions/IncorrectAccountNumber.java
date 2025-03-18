package Exceptions;

public class IncorrectAccountNumber extends RuntimeException {
    public IncorrectAccountNumber() {
        super("Incorrect account number");
    }
}
