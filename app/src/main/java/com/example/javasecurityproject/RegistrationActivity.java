package com.example.javasecurityproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText registrationName;
    private EditText registrationPassword;
    private Button registerBtn;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        registrationName = findViewById(R.id.registrationUsername);
        registrationPassword = findViewById(R.id.registrationPassword);
        registerBtn = findViewById(R.id.registrationBtn);

        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferences.edit();

        if (sharedPreferences != null) {

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if (preferencesMap.size() != 0) {
                credentials.loadCredentials(preferencesMap);
            }
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regUsername = registrationName.getText().toString();
                String regPassword = registrationPassword.getText().toString();

                if (validate(regUsername, regPassword)) {

                    if (credentials.checkUsername(regUsername)) {
                        Toast.makeText(RegistrationActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    } else {

                        credentials.addCredentials(regUsername, regPassword);

                        /* Store the credentials*/
                        sharedPreferenceEditor.putString(regUsername, regPassword);

                        sharedPreferenceEditor.putString("LastSavedUsername", regUsername);
                        sharedPreferenceEditor.putString("LastSavedPassword", regPassword);

                        //Commits the changes and adds them to the file
                        sharedPreferenceEditor.apply();

                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        Toast.makeText(RegistrationActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }
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