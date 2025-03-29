package Exceptions;

public class CannotSendMoneyToYourself extends Exception {
    public CannotSendMoneyToYourself() {
        super("Cannot send money to yourself.");
    }
}
