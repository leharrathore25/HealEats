package com.example.healeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, firstNameEditText, lastNameEditText,
            phoneNumberEditText, passwordEditText, confirmPasswordEditText, ageEditText, heightEditText, weightEditText;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        db = FirebaseFirestore.getInstance();

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.editTextText3);
        emailEditText = findViewById(R.id.editTextText6);
        firstNameEditText = findViewById(R.id.editTextText4);
        lastNameEditText = findViewById(R.id.editTextText5);
        phoneNumberEditText = findViewById(R.id.editTextPhone);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword2);
        ageEditText=findViewById(R.id.editTextTextAge);
        heightEditText=findViewById(R.id.editTextTextHeight);
        weightEditText=findViewById(R.id.editTextTextWeight);

        TextView textViewSignIn = findViewById(R.id.textView4);
        textViewSignIn.setMovementMethod(LinkMovementMethod.getInstance());
        textViewSignIn.setHighlightColor(Color.TRANSPARENT);  // to remove highlight on click   Spannable spannable = new SpannableString(textViewSignUp.getText());
        Spannable spannable = new SpannableString(textViewSignIn.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Navigate to the sign-up activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        String spantext=textViewSignIn.getText().toString();
        int startIndex = spantext.indexOf("Sign in");
        int endIndex = startIndex + 7;
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignIn.setText(spannable);

    }

    public void RegisterUser(View view) {
        final String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String firstname = firstNameEditText.getText().toString();
        String lastname = lastNameEditText.getText().toString();
        String number = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String ageString = ageEditText.getText().toString();
        int age = Integer.parseInt(ageString);
        String heightString = heightEditText.getText().toString();
        float height = Float.parseFloat(heightString);
        String weightString = weightEditText.getText().toString();
        float weight = Float.parseFloat(weightString);
        String role="customer";
        String disease_id="";
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.startsWith(username + " ")) {
         Toast.makeText(this, "There cannot be a space after Username", Toast.LENGTH_LONG).show();
        }
        if (username.length() < 5) {
           Toast.makeText(this, "Minimum Length of UserName should be 5", Toast.LENGTH_LONG).show();
           return;
       }
        if (username.contains(" ")) {
            Toast.makeText(this, "Username cannot contain space", Toast.LENGTH_LONG).show();
            return;
        }
        if (username.length() > 20) {
            Toast.makeText(this, "Username cannot be more than 20 characters", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.contains(" ")) {
            Toast.makeText(this, "Password cannot contain space", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() > 20) {
            Toast.makeText(this, "Password cannot be more than 20 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Password cannot be less than 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean containsLowercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                containsLowercase = true;
                break;
            }
        }
        if (!containsLowercase) {
            Toast.makeText(this, "Password needs atleast one Lowercase Character", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean containsUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
                break;
            }
        }
        if (!containsUppercase) {
            Toast.makeText(this, "Password needs atleast one Uppercase Character", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }
        if (!containsDigit) {
            Toast.makeText(this, "Password needs atleast one Digit", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean containsSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                containsSpecialCharacter = true;
                break;
            }
        }
        if (!containsSpecialCharacter) {
            Toast.makeText(this, "Password needs atleast one Special Character", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equalsIgnoreCase(username)) {
            Toast.makeText(this, "Password cannot be the same as username", Toast.LENGTH_SHORT).show();
            return;
       }


        int atSymbolIndex = email.indexOf('@');
        if (atSymbolIndex == -1 || atSymbolIndex == 0) {
            Toast.makeText(this, " Missing '@' symbol or starts with '@' ", Toast.LENGTH_SHORT).show();
            return;
        }

        int dotIndex = email.indexOf('.', atSymbolIndex);
        if (dotIndex == -1 || dotIndex == atSymbolIndex + 1 || dotIndex == email.length() - 1) {
            Toast.makeText(this, " Missing dot after '@' or starts or ends with dot ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstname.startsWith(firstname + " ")) {
            Toast.makeText(this, "There cannot be a space after Firstname", Toast.LENGTH_LONG).show();
        }

        if (firstname.contains(" ")) {
            Toast.makeText(this, "Firstname cannot contain space", Toast.LENGTH_LONG).show();
            return;
        }
        if (firstname.length() > 20) {
            Toast.makeText(this, "Firstname cannot be more than 20 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (lastname.startsWith(firstname + " ")) {
            Toast.makeText(this, "There cannot be a space after Lastname", Toast.LENGTH_LONG).show();
        }

        if (lastname.contains(" ")) {
            Toast.makeText(this, "Lastname cannot contain space", Toast.LENGTH_LONG).show();
            return;
        }
        if (lastname.length() > 20) {
            Toast.makeText(this, "Lastname cannot be more than 20 characters", Toast.LENGTH_LONG).show();
            return;
        }

        String numericPhoneNumber = number.replaceAll("[^0-9]", "");

        // Check if the cleaned phone number has exactly 10 digits
        if (numericPhoneNumber.length() != 10) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
            return;
        }

        if (age <= 0 && age >= 100) {
            Toast.makeText(this, "Age should be between 0-100", Toast.LENGTH_LONG).show();
            return;
        }

        if (height <= 50 && height >= 300) {
            Toast.makeText(this, "Invalid Height", Toast.LENGTH_LONG).show();
            return;
        }

        if (weight <= 10 && weight >= 500) {
            Toast.makeText(this, "Invalid Weight", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> user = new HashMap<>();

        // Add the user data to the Firestore database
        db.collection("users")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Username already exists, display an error message
                                Toast.makeText(SignupActivity.this, "Username already exists, choose a different username", Toast.LENGTH_SHORT).show();
                            } else {
                                // Username is available, proceed with registration
                                // Create a user object with the provided information
                                Map<String, Object> user = new HashMap<>();
                                user.put("username", username);
                                user.put("password", password);
                                user.put("email", email);
                                user.put("firstname", firstname);
                                user.put("lastname", lastname);
                                user.put("number", number);
                                user.put("pfp","-");
                                user.put("role",role);
                                user.put("age",age);
                                user.put("height",height);
                                user.put("weight",weight);
                                user.put("disease_id",disease_id);


                                // Add the user data to the Firestore database
                                db.collection("users")
                                        .document(username)
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                    finish();  // Finish the activity after successful registration
                                                } else {
                                                    Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Error checking username availability", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}