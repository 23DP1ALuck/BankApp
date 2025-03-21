package services;

import Exceptions.IncorrectAccountNumber;
import Exceptions.IncorrectPassException;
import Exceptions.NoSuchUserException;
import Exceptions.UserExistsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.TransactionType;
import models.Transaction;
import models.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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

    public User checkUser(String login, String password) throws NoSuchUserException{
        jsonLoader();
        for (User user : users) {
            if(user.getUsername().equals(login)){
                if(user.getPassword().equals(password)){
                    return user;
                } else{
                    throw new IncorrectPassException();
                }
            }
        }
        throw new NoSuchUserException();
    }

//    Function, which loads users from json
    public void jsonLoader() {
        try (FileReader fr = new FileReader(filePath)) {
            User[] readedUsers = gson.fromJson(fr, User[].class);
            if (readedUsers != null) {
//                add to users already created users, which were saved to json
                users = new ArrayList<>(List.of(readedUsers));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveToJson(User TransactonMaker) throws IOException {
        jsonLoader();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(TransactonMaker.getUsername())){
                users.set(i, TransactonMaker);
                break;
            }
        }
        try(FileWriter fw = new FileWriter(filePath)) {
            gson.toJson(users, fw);
        }
    }
    public BigDecimal moneyIn(User user) {
        BigDecimal totalIn = BigDecimal.ZERO;
        List<Transaction> transactions = user.getTransactions();
        for (Transaction transaction : transactions) {
            if ((transaction.getType() == TransactionType.TRANSFER || transaction.getType() ==
                    TransactionType.ADDTOBALANCE) && transaction.amount.compareTo(BigDecimal.ZERO) > 0) {
                totalIn = totalIn.add(transaction.amount);
            }
        }
        return totalIn;
    }
    public BigDecimal moneyOut(User user) {
        BigDecimal totalOut = BigDecimal.ZERO;
        List<Transaction> transactions = user.getTransactions();
        for (Transaction transaction : transactions) {
            if ((transaction.getType() == TransactionType.TRANSFER || transaction.getType() ==
                    TransactionType.WITHDRAWAL) && transaction.amount.compareTo(BigDecimal.ZERO) < 0) {
                totalOut = totalOut.add(transaction.amount);
            }
        }
        return totalOut;
    }
    public User getUserFromAccNumber(String accNumber){
        for (User user : users) {
            if(user.getAccountNumber().equals(accNumber)){
                return user;
            }
        }
        return null;
    }
}
