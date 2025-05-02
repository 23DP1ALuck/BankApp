package services;

import Exceptions.*;
import enums.TransactionType;
import models.Transaction;
import models.User;
import org.junit.Test;

import javax.xml.crypto.Data;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void addUserToDatabaseUserExistsThrowsError() throws UserExistsException, IOException {
        Database db = Database.getInstance();
        Exception exception = assertThrows(UserExistsException.class, () -> {
            db.addUserToDatabase("t0mmyqwe", "TestPass12", "TestName", "Test");
        });
        String expectedMessage = "User already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkUserSuccess() throws NoSuchUserException {
        Database db = Database.getInstance();
        User result = db.checkUser("t0mmyqwe", "Qwerty21");

        assertNotNull("Method should return user if exists, not null", result);
        assertEquals("t0mmyqwe", result.getUsername());
    }
    @Test
    public void checkUserNoSuchUserThrowsError(){
        Database db = Database.getInstance();
        Exception exception = assertThrows(NoSuchUserException.class, () -> {
            db.checkUser("Satalana", "Qwerty21");
        });
        String expectedMessage = "No such user!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void checkUserIncorrectPassThrowsError() {
        Database db = Database.getInstance();
        Exception exception = assertThrows(IncorrectPassException.class, () -> {
            db.checkUser("t0mmyqwe", "testPass");
        });
        String expectedMessage = "Incorrect Password";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void moneyIn() throws NotPositiveAmountException, CannotSendMoneyToYourself {
        Database db = Database.getInstance();
        User user1 = new User("testUser1", "TestPass12", "TestName", "Test");
        User user2 = new User("testUser2", "TestPass12", "TestName2", "Test");
        Transaction transaction1 = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("100.00"), TransactionType.TRANSFER);

        user1.performTransaction(transaction1);
//        add money to other user to send it to first
        user2.performTransaction(transaction1);
        user2.performTransaction(transaction2, user1);

        assertEquals(new BigDecimal("1200.00"),db.moneyIn(user1));
    }

    @Test
    public void moneyOut() throws NotPositiveAmountException, CannotSendMoneyToYourself {
        Database db = Database.getInstance();
        User user1 = new User("testUser1", "TestPass12", "TestName", "Test");
        User user2 = new User("testUser2", "TestPass12", "TestName2", "Test");
        Transaction transaction1 = new Transaction(new BigDecimal("1000.00"), TransactionType.ADDTOBALANCE);
        Transaction transaction2 = new Transaction(new BigDecimal("200.00"), TransactionType.TRANSFER);
        Transaction transaction3 = new Transaction(new BigDecimal("100.00"), TransactionType.WITHDRAWAL);

        user1.performTransaction(transaction1);
        user1.performTransaction(transaction2, user2);
        user1.performTransaction(transaction3);

        assertEquals(new BigDecimal("-300.00"),db.moneyOut(user1));
    }

    @Test
    public void getUserFromAccNumber() {
        Database db = Database.getInstance();
        User result = db.getUserFromAccNumber("LV29HABA2763326627601");
        assertNotNull("Should return user if account number was matched ", result);
        assertEquals("t0mmyqwe", result.getUsername());
    }
}