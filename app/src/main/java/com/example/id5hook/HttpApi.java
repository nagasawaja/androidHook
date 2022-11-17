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
        Log.d("benija", "handleServerFrom:" + session.getRemoteIpAddress());
        SetDeviceInfo();
        return newFixedLengthResponse("suc");
    }

    public void SetDeviceInfo() {
        // init param map
        Map<String, String> paramsMap = new HashMap<String, String>();
        // init networkOperator
        String NetworkOperator = GenParams.getNetworkOperator();

        // 唯一的设备ID 如果是GSM网络，返回IMEI(国际移动设备识别码)(手机IMEI码由15-17位数字组成)；如果是CDMA网络，返回MEID 有两种算法.另外一种在appEnv代码中，和硬件绑定的,暂时用gsm网络
        paramsMap.put("getDeviceId", GenParams.getDeviceId()); // suc
        // 手机号 对于GSM网络来说即MSISDN
        paramsMap.put("getLine1Number", GenParams.randomTelephonyGetLine1Number()); // suc
        // AndroidId 是一串64位的编码（十六进制的字符串），是随机生成的设备的第一个引导，其记录着一个固定值，通过它可以知道设备的寿命（在设备恢复出厂设置或刷机后，该值可能会改变）。和系统绑定的
        paramsMap.put("android_id", GenParams.getAndroid()); // suc
        // SIM卡的序列号(20位长度) 需要权限：READ_PHONE_STATE
        paramsMap.put("getSimSerialNumber", GenParams.getSimSerialNumber()); // suc
        // 唯一的用户ID 例如：IMSI(国际移动用户识别码)
        paramsMap.put("getSubscriberId", GenParams.getSubscriberId()); // suc
        // SIM卡的国家码
        paramsMap.put("getSimCountryIso", "ISO 3166-2:CN"); // suc
        // android 手机的mac地址
        paramsMap.put("getMacAddress", GenParams.getMac()); // suc
        // wifi路由器的mac地址
        paramsMap.put("getBSSID", GenParams.getMac()); // suc
        // wifi的名字
        paramsMap.put("getSSID", "\"" + GenParams.randomWifiInfoSSID() + "\""); // suc
        // 是否低内存设备 返回值是一个字符串，不能改成布尔值
        paramsMap.put("ro.config.low_ram", "false"); // suc
        // 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字
        paramsMap.put("getSimOperator", NetworkOperator.replace("-", "")); // suc

        // android os build
        /* 参考
        12-16 19:55:50.987 2437-2437/com.netease.dwrg I/Xposed: fix MODEL to xiaomi 8
        12-16 19:55:50.989 2437-2437/com.netease.dwrg I/Xposed: fix MANUFACTURER to xiaomi
        12-16 19:55:50.990 2437-2437/com.netease.dwrg I/Xposed: fix HARDWARE to android_x86
        12-16 19:55:50.991 2437-2437/com.netease.dwrg I/Xposed: fix BRAND to xiaomi
         */
        // 参考 https://blog.csdn.net/ioiol/article/details/45535601/
        // 参考2 https://blog.csdn.net/Small_Lee/article/details/50550048
        String MANUFACTURER = GenParams.getMANUFACTURER();
        String SERIAL = MANUFACTURER +  GenParams.randomString(4, false, false, true);
        String DISPLAY = "QKQ1." + GenParams.randomString(6, false, false, true)  + ".002";
        String HARDWARE = MANUFACTURER +  GenParams.randomString(4, false, false, true);
        paramsMap.put("MODEL", GenParams.getChinese() + GenParams.getChinese() + GenParams.getChinese() + "的" + MANUFACTURER); // suc
        paramsMap.put("MANUFACTURER", MANUFACTURER); // suc
        paramsMap.put("HARDWARE", HARDWARE); // suc
        paramsMap.put("BRAND", MANUFACTURER); // suc
        paramsMap.put("ro.build.id", GenParams.randomString(6, false, true, true)); // suc
        paramsMap.put("ro.build.version.release", "13.1"); // suc
        paramsMap.put("SERIAL", SERIAL); // suc
        paramsMap.put("BOARD", MANUFACTURER + GenParams.randomString(6, true, false, true)); // notSure
        paramsMap.put("DEVICE", GenParams.getChinese() + GenParams.getChinese() + GenParams.getChinese() + "的" + MANUFACTURER); // notSure
        paramsMap.put("ID", GenParams.randomBuildSerial()); // notSure
        paramsMap.put("PRODUCT", GenParams.getChinese() + GenParams.getChinese() + GenParams.getChinese() + "的" + MANUFACTURER); // notSure
        paramsMap.put("DISPLAY", DISPLAY); // notSure
        paramsMap.put("FINGERPRINT", "Xiaomi/cepheus/cepheus:10/"+DISPLAY+"/20."+GenParams.randomString(2, false, false, true)+"."+GenParams.randomString(1, false, false, true)+":user/release-keys"); // notSure
        paramsMap.put("gsm.version.baseband", GenParams.getMODEM()); // notSure
        paramsMap.put("gsm.version.ril-impl", MANUFACTURER + " RIL V3.0"); // notSure
        // 按照字母次序的current registered operator(当前已注册的用户)的名字,  注意：仅当用户已在网络注册时有效 ,在CDMA网络中结果也许不可靠
        paramsMap.put("getNetworkOperatorName", MANUFACTURER); // notSure
        // MCC+MNC(mobile country code + mobile network code)注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠。
        paramsMap.put("getNetworkOperator", NetworkOperator); // notSure

        paramsMap.put("ro.product.cpu.abilist64", "arm64-v8a,armeabi"); // notSure
        paramsMap.put("ro.product.cpu.abi", "arm64-v8a"); // notSure
        paramsMap.put("ro.product.cpu.abi2", "arm64"); // notSure
        paramsMap.put("ro.product.cpu.abilist", "arm64-v8a,armeabi-v7a,armeabi"); // notSure
        paramsMap.put("ro.product.cpu.abilist32", "armeabi-v7a,armeabi"); // notSure

        paramsMap.put("getNetworkCountryIso", GenParams.getNetworkOperatorName.get(NetworkOperator));


        
        Gson gson = new Gson();
        String jsonString = gson.toJson(paramsMap);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("/mnt/sdcard/benija.json"));
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.d("benija", "SetDeviceInfoToFileException:" + e.getMessage());
        }
        Log.d("benija", "SetDeviceInfoToFileSuc");

    }
}