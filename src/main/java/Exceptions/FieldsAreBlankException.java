package Exceptions;

public class FieldsAreBlankException extends Exception {
    public FieldsAreBlankException() {
        super("Username or Password fields are blank");
    }
}
