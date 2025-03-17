package Exceptions;

public class TypeOrFieldsNotSetException extends Exception {
    public TypeOrFieldsNotSetException() {
        super("Type is not set or fields are blank.");
    }
}