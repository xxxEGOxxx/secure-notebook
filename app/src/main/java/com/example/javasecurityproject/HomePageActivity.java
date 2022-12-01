package com.example.javasecurityproject;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HomePageActivity extends AppCompatActivity {

    private TextView sortedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sortedText = findViewById(R.id.tvWelcome);
    }
}
