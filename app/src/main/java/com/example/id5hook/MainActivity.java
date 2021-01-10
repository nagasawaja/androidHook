package com.example.id5hook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

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
            Log.e("benija", "onCreateMainActiveFail:" + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("benija", "apiHookAppOnResume");
        try {
            Process execRes = null;
            execRes = Runtime.getRuntime().exec("pwd");

            Log.d("benija", "onResume: " + execRes.getClass().getSimpleName());
        } catch (IOException e) {
            Log.e("benija", "execResFail");
        }


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
//        Enumeration<NetworkInterface> interfaces = null;
//        try {
//            String address = null;
//            interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface netWork = interfaces.nextElement();
//                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
//                byte[] by = netWork.getHardwareAddress();
//                Log.d("benija", "getDisplayName: " +netWork.getDisplayName());
//                Log.d("benija", "getName: " +netWork.getName());
//                Log.d("benija", "getMTU: " +netWork.getMTU());
//                Log.d("benija", "isVirtual: " +netWork.isVirtual());
//                Log.d("benija", "isUp: " +netWork.isUp());
//                Log.d("benija", "getHardwareAddress: " + by);
//
//                Enumeration<InetAddress> lk = netWork.getInetAddresses();
//                while(lk.hasMoreElements()) {
//                    Log.d("benija", "lk:more");
//                    InetAddress nn = lk.nextElement();
////                    Log.d("benija", "getHostName: " + nn.getHostName());
////                    Log.d("benija", "getHostAddress: " + nn.getHostAddress());
////                    Log.d("benija", "nn:" + nn.toString());
//                }
//
//                if (by == null || by.length == 0) {
//                    continue;
//                }
//                StringBuilder builder = new StringBuilder();
//                for (byte b : by) {
//                    builder.append(String.format("%02X:", b));
//                }
//                if (builder.length() > 0) {
//                    builder.deleteCharAt(builder.length() - 1);
//                }
//                String mac = builder.toString();
//                Log.d("benija", "interfaceName="+netWork.getName()+", mac="+mac);
//                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
//                if (netWork.getName().equals("wlan0")) {
//                    Log.d("benija", " interfaceName ="+netWork.getName()+", mac="+mac);
//                    address = mac;
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }

    }
}