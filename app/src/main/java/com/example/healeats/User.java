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
    private String role;
    private int age;
    private float height;
    private float weight;


    // Constructors (you can have multiple constructors as needed)
    public User() {
        // Default constructor required for Firestore
    }

    public User(String userId, String username, String password, String email, String number,
                String firstName, String lastName, String pfp, String role, int age, float height, float weight) {
        this.userId = userId;
        this.username = username;
        this.password= password;
        this.email = email;
        this.number = number;
        this.firstname = firstName;
        this.lastname = lastName;
        this.pfp = pfp;
        this.role=role;
        this.age=age;
        this.height=height;
        this.weight=weight;
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
    public String getRole(){ return role; }


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

    public int getAge(){ return age; }

    public float getHeight(){return height;}

    public float getWeight(){return weight;}

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role){ this.role=role; }


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

    public void setAge(int age){this.age=age;}

    public void setHeight(float height){this.height=height;}

    public void setWeight(float weight){this.weight=weight;}



}
