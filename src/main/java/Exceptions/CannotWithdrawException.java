package Exceptions;

public class CannotWithdrawException extends RuntimeException {
    public CannotWithdrawException(String message) {
        super("Cannot "+message+" (not enough money).");
    }
}
