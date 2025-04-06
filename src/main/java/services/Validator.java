package services;

import Exceptions.NotValidPasswordException;
import Exceptions.NotValidUsernameException;

public class Validator {
    public Validator(){
    }
    public void validatePass(String password){
        if((!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))){
            System.out.println("Not a valid password");
            throw new NotValidPasswordException();
        }
    }
    public void validateUsername(String username) {
        if(!username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")){
            System.out.println("Not a valid username");
            throw new NotValidUsernameException();
        }
    }
}
