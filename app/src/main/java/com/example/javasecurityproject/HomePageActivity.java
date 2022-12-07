package com.example.javasecurityproject;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class HomePageActivity extends AppCompatActivity {
    private EditText message;
    private ImageFilterButton sentBtn;
    private EditText editField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        message = findViewById(R.id.message);
        editField = findViewById(R.id.editField);
        sentBtn = findViewById(R.id.sentBtn);

        Intent intent = getIntent();
        String salt = intent.getStringExtra("salt");
        String plainText = intent.getStringExtra("plainText");
        String key = intent.getStringExtra("key");
        String inputName = intent.getStringExtra("inputName");

        message.setText(plainText);

        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sentText = editField.getText().toString();

                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        inputName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                String secureText = null;
                try {
                    secureText = Encryption.encrypt(sentText, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.putString("text", secureText);
                editor.apply();
                //plainText = secureText;
                message.setText(sentText);
            }
        });

    }

}
