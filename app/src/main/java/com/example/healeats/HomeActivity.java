package com.example.healeats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text=findViewById(R.id.textView);
        User user = UserDataSingleton.getInstance().getUser();
        text.setText("Hello "+ user.getFirstName());


    }
}