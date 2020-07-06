package com.example.id5hook;

import fi.iki.elonen.NanoHTTPD;

import android.content.Context;
import android.util.Log;
import java.io.IOException;


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
        Log.d("benija", "handleServerFrom:" + session.getRemoteIpAddress());
        SetDeviceInfo();
        return newFixedLengthResponse("suc");
    }

    public void SetDeviceInfo() {
        PreferencesUtil.getInstance().saveParam("getDeviceId", GenParams.getDeviceId());
        PreferencesUtil.getInstance().saveParam("android_id", GenParams.getAndroid());
        PreferencesUtil.getInstance().saveParam("getSimSerialNumber", GenParams.getSimSerialNumber());
        PreferencesUtil.getInstance().saveParam("getSubscriberId", GenParams.getSubscriberId());
        PreferencesUtil.getInstance().saveParam("getMacAddress", GenParams.getMac());
        PreferencesUtil.getInstance().saveParam("getBSSID", GenParams.getMac());
        PreferencesUtil.getInstance().saveParam("ro.config.low_ram", "false");
    }
}