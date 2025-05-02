package models;

import services.Database;

import java.util.*;

public class AccountNumber {
    private static Set<String> accountNumbers = Database.getInstance().getAllAccNumbers();
    // unique account number for every user
    public static String accountNumberGenerator(){
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("LV29HABA");
        int digitCounter = 0;
        while(digitCounter < 13){
            accountNumber.append(String.valueOf(random.nextInt(10)));
            digitCounter++;
        }
        if(accountNumbers.contains(accountNumber.toString())){
            return accountNumberGenerator();
        }
        accountNumbers.add(accountNumber.toString());
        return accountNumber.toString();
    }
}
