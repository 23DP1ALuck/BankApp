package Exceptions;

public class IncorrectPassException extends RuntimeException {
    public IncorrectPassException() {
        super("Incorrect Password");
    }
}
