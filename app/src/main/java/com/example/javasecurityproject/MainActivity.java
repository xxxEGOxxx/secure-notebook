package com.example.javasecurityproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginBtn;
    private TextView registration;
    private CheckBox rememberUser;

    boolean isValid = false;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //key = fun(password + salt...)
        //encrypt(key)
        //Check if file end with "MichRen"
        ////////////////////////////////////////////////////////////////
        /*Context context = getApplicationContext();

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = null;
        try {
            mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        String fileToRead = "my_sensitive_data.txt";

        EncryptedFile encryptedFile = null;
        try {
            assert mainKeyAlias != null;
            encryptedFile = new EncryptedFile.Builder(
                    new File("This PC\\POCO F3\\Internal shared storage\\Download", fileToRead),
                    context,
                    mainKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            assert encryptedFile != null;
            inputStream = encryptedFile.openFileInput();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            assert inputStream != null;
            int nextByte = inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        ////////////////////////////////////////////////////////////////
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        EditText UPL = (EditText) findViewById(R.id.password);
        UPL.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        loginBtn = findViewById(R.id.loginBtn);
        registration = findViewById(R.id.tvRegister);
        rememberUser = findViewById(R.id.checkRememberUser);



        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // check whether file sharedPreferences exists and takes cred from there
        if (sharedPreferences != null) {

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if (preferencesMap.size() != 0) {
                credentials.loadCredentials(preferencesMap);
            }

            String savedUsername = sharedPreferences.getString("LastSavedUsername", "");
            String savedPassword = sharedPreferences.getString("LastSavedPassword", "");

            if (sharedPreferences.getBoolean("RememberMeCheckbox", false)) {
                username.setText(savedUsername);
                password.setText(savedPassword);
                rememberUser.setChecked(true);
            }
        }

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputName = username.getText().toString();
                String inputPassword = password.getText().toString();
                //System.out.println(md5(String.valueOf(password)));
                //System.out.println(new String(key(), StandardCharsets.UTF_8));
////////////////////////////////////////////////
                /* Plain text Password. */


                /* generates the Salt value. It can be stored in a database. */
                String saltvalue = PassBasedEnc.getSaltvalue(30);

                /* generates an encrypted password. It can be stored in a database.*/
                String encryptedpassword = PassBasedEnc.generateSecurePassword(String.valueOf(password), saltvalue);

                /* Print out plain text password, encrypted password and salt value. */
                System.out.println("Plain text password = " + String.valueOf(password));
                System.out.println("Secure password = " + encryptedpassword);
                System.out.println("Salt value = " + saltvalue);

                /* verify the original password and encrypted password */
                boolean status = PassBasedEnc.verifyUserPassword(String.valueOf(password),encryptedpassword,saltvalue);
                if(status)
                    System.out.println("Password Matched!!");
                else
                    System.out.println("Password Mismatched");
//////////////////////////////////////////////////////////


                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Short", Toast.LENGTH_LONG).show();
                } else {
                    isValid = validate(inputName, inputPassword);

                    if (!isValid) {

                        Toast.makeText(MainActivity.this, "Wrong login or password", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();

                        sharedPreferencesEditor.putString("LastSavedUsername", inputName);
                        sharedPreferencesEditor.putString("LastSavedPassword", inputPassword);

                        sharedPreferencesEditor.putBoolean("RememberMeCheckbox", rememberUser.isChecked());

                        sharedPreferencesEditor.apply();
                        // new activity

                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validate(String name, String password) {
        return credentials.verifyCredentials(name, password);
    }

    /*public byte[] key() {
        byte[] plaintext = "Hej Mr.Ren".getBytes(StandardCharsets.UTF_8);

        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert keygen != null;
        keygen.init(256);
        SecretKey key = keygen.generateKey();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            assert cipher != null;
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] ciphertext = new byte[0];
        try {
            ciphertext = cipher.doFinal(plaintext);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        byte[] iv = cipher.getIV();

        System.out.println(new String(plaintext, StandardCharsets.UTF_8));
        return ciphertext;
    }*/

    /*public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }*/

    static class PassBasedEnc
    {
        /* Declaration of variables */
        private static final Random random = new SecureRandom();
        private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        private static final int iterations = 10000;
        private static final int keylength = 256;

        /* Method to generate the salt value. */
        public static String getSaltvalue(int length)
        {
            StringBuilder finalval = new StringBuilder(length);

            for (int i = 0; i < length; i++)
            {
                finalval.append(characters.charAt(random.nextInt(characters.length())));
            }

            return new String(finalval);
        }

        /* Method to generate the hash value */
        public static byte[] hash(char[] password, byte[] salt)
        {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
            Arrays.fill(password, Character.MIN_VALUE);
            try
            {
                SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                return skf.generateSecret(spec).getEncoded();
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException e)
            {
                throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
            }
            finally
            {
                spec.clearPassword();
            }
        }

        /* Method to encrypt the password using the original password and salt value. */
        public static String generateSecurePassword(String password, String salt)
        {
            String finalval = null;

            byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                finalval = Base64.getEncoder().encodeToString(securePassword);
            }

            return finalval;
        }

        /* Method to verify if both password matches or not */
        public static boolean verifyUserPassword(String providedPassword,
                                                 String securedPassword, String salt)
        {
            boolean finalval = false;

            /* Generate New secure password with the same salt */
            String newSecurePassword = generateSecurePassword(providedPassword, salt);

            /* Check if two passwords are equal */
            finalval = newSecurePassword.equalsIgnoreCase(securedPassword);

            return finalval;
        }
    }
}