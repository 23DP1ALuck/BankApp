package Exceptions;

public class NotValidUsernameException extends RuntimeException {
    public NotValidUsernameException() {
        super("Username must be 8-20 characters,\nno _ or . at start/end, no double _ or .");
    }
}
