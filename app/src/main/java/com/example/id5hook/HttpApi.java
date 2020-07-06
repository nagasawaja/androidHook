package com.example.id5hook;

import fi.iki.elonen.NanoHTTPD;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

public class HttpApi extends NanoHTTPD {
    private static int port = 9090;
    public HttpApi(Context context) throws IOException {
        super(port);
        SetDeviceInfo();
        Log.d("benija", "ListenServerAt:127.0.0.1:" + port);
        start();
    }

    /*
    解析的主入口函数，所有请求从这里进，也从这里出
    */
    @Override
    public Response serve(IHTTPSession session) {
        SetDeviceInfo();
        return newFixedLengthResponse("suc");
    }

    public void SetDeviceInfo() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("getDeviceId", GenParams.getDeviceId());
        paramsMap.put("android_id", GenParams.getAndroid());
        paramsMap.put("getSimSerialNumber", GenParams.getSimSerialNumber());
        paramsMap.put("getSubscriberId", GenParams.getSubscriberId());
        paramsMap.put("getMacAddress", GenParams.getMac());
        paramsMap.put("getBSSID", GenParams.getMac());
        paramsMap.put("ro.config.low_ram", "false");
        Gson gson = new Gson();
        String jsonString = gson.toJson(paramsMap);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("/mnt/sdcard/benija.json"));
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.d("benija", "fileExc222e" + e.getMessage());
        }
        Log.d("benija", "filefinsi2222h");

    }
}