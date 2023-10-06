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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText edt1, edt2;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt1 = findViewById(R.id.editTextText);
        edt2 = findViewById(R.id.editTextText2);
        db = FirebaseFirestore.getInstance();
        TextView textViewSignUp = findViewById(R.id.textView2);
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        textViewSignUp.setHighlightColor(Color.TRANSPARENT);  // to remove highlight on click   Spannable spannable = new SpannableString(textViewSignUp.getText());
        Spannable spannable = new SpannableString(textViewSignUp.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Navigate to the sign-up activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };
        String spantext = textViewSignUp.getText().toString();
        int startIndex = spantext.indexOf("Sign up");
        int endIndex = startIndex + 7;
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignUp.setText(spannable);

    }

    public void loginUser(View view) {
        final String username = edt1.getText().toString();
        final String password = edt2.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
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
        db.collection("users")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User loggedInUser = document.toObject(User.class);

                                // Check if the entered password matches the stored password
                                if (loggedInUser.getPassword().equals(password)) {
                                    // Successful login, set the user in the Singleton
                                    UserDataSingleton.getInstance().setUser(loggedInUser);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error getting document.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






}
