package com.example.id5hook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import de.robv.android.xposed.XposedBridge;

public class MainActivity extends AppCompatActivity {
    private HttpApi httpapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init SharedPreferences

//        PreferencesUtil.getInstance().init(this);
        try {
            httpapi = new HttpApi(this);
//            XposedBridge.log("onResumeHttp");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("onResume", "WebServer start failed" + e.getMessage());
        }
    }
}