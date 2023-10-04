package com.example.healeats;

public class User {

    private String userId;
    private String username;
    private String password;
    private String email;
    private String number;
    private String firstname;
    private String lastname;
    private String pfp;

    // Constructors (you can have multiple constructors as needed)
    public User() {
        // Default constructor required for Firestore
    }

    public User(String userId, String username, String password, String email, String number,
                String firstName, String lastName, String pfp) {
        this.userId = userId;
        this.username = username;
        this.password= password;
        this.email = email;
        this.number = number;
        this.firstname = firstName;
        this.lastname = lastName;
        this.pfp = pfp;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getPfp() {
        return pfp;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

}
