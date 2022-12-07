package com.example.javasecurityproject;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class MainActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    public Button loginBtn;
    public TextView registration;
    public CheckBox rememberUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        EditText UPL = (EditText) findViewById(R.id.password);
        UPL.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        loginBtn = findViewById(R.id.loginBtn);
        registration = findViewById(R.id.tvRegister);
        rememberUser = findViewById(R.id.checkRememberUser);



        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputName = username.getText().toString();
                String inputPassword = password.getText().toString();

                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        inputName, Context.MODE_PRIVATE);

                System.out.println("--------------------------------------------------------------------------");

                String text = sharedPref.getString("text", null);
                String salt = sharedPref.getString("salt", null);



                String key = Encryption.generateSecurePassword(inputPassword, salt);

                String plainText = null;
                try {
                    plainText = Encryption.decrypt(text, key);
                    System.out.println(plainText);

                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    intent.putExtra("salt",salt);
                    intent.putExtra("plainText", plainText);
                    intent.putExtra("key", key);
                    intent.putExtra("inputName", inputName);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Wrong login or password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                /*Credentials credentials = new Credentials();

                credentials.setPlainText(plainText);
                credentials.setSalt(salt);*/


            }
        });
    }
}