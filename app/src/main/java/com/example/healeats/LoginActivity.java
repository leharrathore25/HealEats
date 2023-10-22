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
                                String role = loggedInUser.getRole();

                                // Check if the entered password matches the stored password
                                if (loggedInUser.getPassword().equals(password)) {
                                    // Successful login, set the user in the Singleton
                                    UserDataSingleton.getInstance().setUser(loggedInUser);

                                    Intent intent;
                                    if (role.equals("admin")) {
                                        intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                                    }

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
