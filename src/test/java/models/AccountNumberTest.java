package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountNumberTest {
    @Test
    public void accountNumberGeneratorCorrectFormatTest() {
//        Test generate 5 acc numbers and check number's format
        for (int i = 0; i < 5 ; i++){
            String testAccNumber = AccountNumber.accountNumberGenerator();
            assertTrue("Acc number should starts with LV29HABA",testAccNumber.startsWith("LV29HABA"));
            assertTrue("Should contain 13 digits after LV29HABA",testAccNumber.matches("^LV29HABA\\d{13}$"));
        }
    }
}