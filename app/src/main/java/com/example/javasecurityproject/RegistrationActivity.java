package com.example.javasecurityproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class RegistrationActivity extends AppCompatActivity {

    private EditText registrationName;
    private EditText registrationPassword;
    private Button registerBtn;
    public static String checkString = "ADncaisiD3fN4f4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        registrationName = findViewById(R.id.registrationUsername);
        registrationPassword = findViewById(R.id.registrationPassword);
        registerBtn = findViewById(R.id.registrationBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regUsername = registrationName.getText().toString();
                String regPassword = registrationPassword.getText().toString();
                String salt = Encryption.getSaltvalue(8);


                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        regUsername, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                String text = "Mich Ren";
                text = checkString + " " + text;
                String key = Encryption.generateSecurePassword(regPassword, salt);
                //String defaultValue = getResources().getString(0);

                String secureText = null;
                try {
                    secureText = Encryption.encrypt(text, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                editor.putString("text", secureText);

                String plainText = null;
                try {
                    plainText = Encryption.decrypt(secureText, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //editor.putString("text", plainText);

                try {

                    //editor.putString("text", Encryption.decrypt(sharedPref.getString("text", defaultValue), regPassword));
                    //Encryption.decrypt(text, Encryption.generateSecurePassword(regPassword, salt)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.putString("salt", salt);
                editor.apply();
            }
        });

    }

    private boolean validate(String username, String password) {

        //Password is not less than 8 char
        if (username.isEmpty() || password.length() < 8) {
            Toast.makeText(this, "Password should be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}