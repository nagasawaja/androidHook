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
        // android os build
        /* 参考
        12-16 19:55:50.987 2437-2437/com.netease.dwrg I/Xposed: fix MODEL to xiaomi 8
        12-16 19:55:50.989 2437-2437/com.netease.dwrg I/Xposed: fix MANUFACTURER to xiaomi
        12-16 19:55:50.990 2437-2437/com.netease.dwrg I/Xposed: fix HARDWARE to android_x86
        12-16 19:55:50.991 2437-2437/com.netease.dwrg I/Xposed: fix BRAND to xiaomi
         */
        String MANUFACTURER = GenParams.getMANUFACTURER();
        PreferencesUtil.getInstance().saveParam("MODEL", GenParams.getMODEL());
        PreferencesUtil.getInstance().saveParam("MANUFACTURER", MANUFACTURER);
        PreferencesUtil.getInstance().saveParam("HARDWARE", "android_x64");
        PreferencesUtil.getInstance().saveParam("BRAND", MANUFACTURER);
        PreferencesUtil.getInstance().saveParam("SERIAL", GenParams.randomBuildSerial());
        PreferencesUtil.getInstance().saveParam("BOARD", "BOARD");
        PreferencesUtil.getInstance().saveParam("DEVICE", "DEVICE");
        PreferencesUtil.getInstance().saveParam("ID", "ID");
        PreferencesUtil.getInstance().saveParam("PRODUCT", "PRODUCT");
        PreferencesUtil.getInstance().saveParam("DISPLAY", "DISPLAY");
        PreferencesUtil.getInstance().saveParam("FINGERPRINT", "FINGERPRINT");

        PreferencesUtil.getInstance().saveParam("gsm.version.baseband", GenParams.getMODEM());
        PreferencesUtil.getInstance().saveParam("gsm.version.ril-impl", MANUFACTURER + " RIL V3.0");
        PreferencesUtil.getInstance().saveParam("getNetworkOperatorName", MANUFACTURER + "");
        String NetworkOperator = GenParams.getNetworkOperator();
        PreferencesUtil.getInstance().saveParam("getNetworkOperator", NetworkOperator);
        PreferencesUtil.getInstance().saveParam("getSimOperator", NetworkOperator.replace("-", ""));
        PreferencesUtil.getInstance().saveParam("ro.product.cpu.abilist64", "arm64-v8a");
        PreferencesUtil.getInstance().saveParam("ro.product.cpu.abi", "arm64-v8a");
        PreferencesUtil.getInstance().saveParam("ro.product.cpu.abilist", "arm64-v8a,armeabi-v7a,armeabi");
        PreferencesUtil.getInstance().saveParam("ro.product.cpu.abilist32", "armeabi-v7a,armeabi");
        PreferencesUtil.getInstance().saveParam("getSimCountryIso", "ISO 3166-2:CN");
        PreferencesUtil.getInstance().saveParam("getNetworkCountryIso", GenParams.getNetworkOperatorName.get(NetworkOperator));
    }
}