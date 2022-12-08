package com.example.javasecurityproject;

import static android.app.PendingIntent.getActivity;
import androidx.biometric.BiometricPrompt;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class MainActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    public Button loginBtn;
    public TextView registration;
    public CheckBox rememberUser;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

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



                if(SharedPrefCheck.isSharedPrefExist(inputName))
                {
                    String text = sharedPref.getString("text", null);
                    String salt = sharedPref.getString("salt", null);

                    String key = Encryption.generateSecurePassword(inputPassword, salt);

                    String plainText = null;
                    try {
                        plainText = Encryption.decrypt(text, key);
                        System.out.println(plainText);

                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        intent.putExtra("salt", salt);
                        intent.putExtra("plainText", plainText);
                        intent.putExtra("key", key);
                        intent.putExtra("inputName", inputName);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Wrong login or password", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(context, "Wrong login or password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        TextView biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }
}