package com.example.javasecurityproject;

public class Credentials {
    private String plainText;
    private String salt;

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPlainText() {
        return plainText;
    }

    public String getSalt() {
        return salt;
    }
}
