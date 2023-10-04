package com.example.healeats;

public class UserDataSingleton {

    private static UserDataSingleton instance;
    private User user;

    private UserDataSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
