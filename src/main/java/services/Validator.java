package services;

import Exceptions.NotValidPasswordException;
import Exceptions.NotValidUsernameException;
import Exceptions.UserExistsException;
import models.User;

public class Validator {
    Database db = Database.getInstance();

    public Validator(){
    }

    public void validatePass(String password){
        if((!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))){
            System.out.println("Not a valid password");
            throw new NotValidPasswordException();
        }
    }

    public void validateUsername(String username) throws UserExistsException {
        if(!username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")){
            System.out.println("Not a valid username");
            throw new NotValidUsernameException();
        }
        db.checkUsername(username);
    }
}
