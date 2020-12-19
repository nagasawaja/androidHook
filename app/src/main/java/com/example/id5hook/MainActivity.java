package com.example.id5hook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import de.robv.android.xposed.XposedBridge;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private HttpApi httpapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init SharedPreferences
        PreferencesUtil.getInstance().init(this);
        try {
            httpapi = new HttpApi(this);
            Log.d("benija", "onCreateMainActiveSuc");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("benija", "onCreateMainActiveFail" + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("benija", "apiHookAppOnResume");
//        String url = "http://www.beni.pub/ipp";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .build();
//        final Call call = okHttpClient.newCall(request);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response response = call.execute();
//                    Log.d("benija", "run: " + response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}