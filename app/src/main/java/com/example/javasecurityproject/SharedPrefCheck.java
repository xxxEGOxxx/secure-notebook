package com.example.javasecurityproject;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.File;

public class SharedPrefCheck {

    public static boolean isSharedPrefExist(String fileName){
        @SuppressLint("SdCardPath") File f = new File(
                "/data/data/com.example.javasecurityproject/shared_prefs/" + fileName + ".xml");
        if (f.exists()){
            Log.d("TAG", "SharedPreferences " + fileName + ".xml : exist");
            return true;
        }
        else{
            Log.d("TAG", "Setup default preferences");
            return false;
        }

    }

}
