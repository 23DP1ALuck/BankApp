package Exceptions;

public class UserExistsException extends Exception{
    public UserExistsException(){
        super("This username is already taken.");
    }
}
