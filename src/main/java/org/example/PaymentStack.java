package org.example;

import models.User;
import services.Database;

public class PaymentStack {
    private Database db = Database.getInstance();
    private User currentUser;

    public void setUser(User user) {
        currentUser = user;
    }
}
