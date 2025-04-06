package Exceptions;

public class CurrentPasswordIncorrect extends RuntimeException {
    public CurrentPasswordIncorrect() {
        super("Current password is incorrect.");
    }
}
