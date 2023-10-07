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

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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