package Exceptions;

public class CannotTopUpException extends Exception {
    public CannotTopUpException() { super("Cannot top up by this amount."); }
}
