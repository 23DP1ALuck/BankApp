package Exceptions;

public class CannotWithdrawException extends RuntimeException {
    public CannotWithdrawException() {
        super("Cannot withdraw.");
    }
}
