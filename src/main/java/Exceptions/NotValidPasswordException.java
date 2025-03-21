package Exceptions;

public class NotValidPasswordException extends RuntimeException {
    public NotValidPasswordException() {
        super("Password must be at least 8 characters long\n and include at least one letter and one number");
    }
}
