package services;

import Exceptions.IncorrectPassException;
import Exceptions.NoSuchUserException;
import Exceptions.UserExistsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;
    private static Database instance;

    public Database(){
        users = new ArrayList<User>();
        jsonLoader();
    }
//    Singleton, to make single instance of db
    public static Database getInstance(){
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    String filePath = "src/main/java/data/users.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public void addUserToDatabase(String login, String password, String name, String surname) throws UserExistsException, IOException{
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(login)){
                throw new UserExistsException();
            }
        }
        users.add(new User(login, password,  name, surname));
        try(FileWriter fw = new FileWriter(filePath)) {
            gson.toJson(users, fw);
        }
    }

    public boolean checkUser(String login, String password) throws NoSuchUserException{
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(login)){
                if(user.getPassword().equals(password)){
                    return true;
                } else{
                    throw new IncorrectPassException();
                }
            }
        }
        throw new NoSuchUserException();
    }
    public String getFirstNameAndLastName(String username){
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(username)){
                return user.getName() + " " + user.getSurname();
            }
        }
        return null;
    }
    public String getAccountNumber(String username){
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(username)){
                return user.getAccountNumber(username);
            }
        }
        return null;
    }
    public User getUserByUsername(String username){
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
//    Function, which loads users from json
    public void jsonLoader() {
        try(FileReader fr = new FileReader(filePath)){
            User[] readedUsers = gson.fromJson(fr, User[].class);
            if(readedUsers != null){
//                add to users already created users, which were saved to json
                users = new ArrayList<>(List.of(readedUsers));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
