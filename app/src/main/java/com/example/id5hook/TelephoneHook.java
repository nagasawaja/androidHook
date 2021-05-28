package com.example.id5hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class TelephoneHook extends XC_MethodHook{
    public static Map paramsMap = new HashMap<String, String>();
    private  static final Map<String, String> rootDict;
    static
    {
        rootDict = new HashMap<String, String>();
        rootDict.put("/sbin/su","");
        rootDict.put("/system/bin/su","");
        rootDict.put("/system/xbin/su","");
        rootDict.put("/data/local/xbin/su","");
        rootDict.put("/data/local/bin/su","");
        rootDict.put("/system/sd/xbin/su","");
        rootDict.put("/system/bin/failsafe/su","");
        rootDict.put("/data/local/su","");
    }

    private String getValue(String p) {
        String returnValue = "";
        if (paramsMap.containsKey("getDeviceId")) {
            returnValue = (String) paramsMap.get(p);
            Log.d("benija", "getValue1");
        } else {
            String jsonString = FileUtil.readString("/mnt/sdcard/benija.json", "utf-8");
            Gson gson = new Gson();
            paramsMap = gson.fromJson(jsonString, paramsMap.getClass());
            returnValue = (String) paramsMap.get(p);
            Log.d("benija", "getValue2");
        }

        if (returnValue == null || returnValue == "") {
            Log.d("benija", "getValue:" + p);
            returnValue = "";
        }
        return returnValue;
    }

    private String getValueAndroid5(String p) {
        XSharedPreferences  v  = new XSharedPreferences("com.example.id5hook","config");
        return v.getString(p, "");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        String str = paramMethodHookParam.method.getName();
        if (str.equals("getNetworkType")) {
            Log.d("benija", "fixStrings;" + "getNetworkType----" + TelephonyManager.NETWORK_TYPE_HSPAP);
            paramMethodHookParam.setResult(TelephonyManager.NETWORK_TYPE_HSPAP); // set 3g
            return;
        }
        if (str.equals("getPhoneType")) {
            Log.d("benija", "fixStrings;" + "getPhoneType----" + TelephonyManager.PHONE_TYPE_CDMA);
            paramMethodHookParam.setResult(TelephonyManager.PHONE_TYPE_CDMA);
            return;
        }
        if (str.equals("getSimState")) {
            Log.d("benija", "fixStrings;" + "getSimState----" + TelephonyManager.SIM_STATE_READY);
            paramMethodHookParam.setResult(TelephonyManager.SIM_STATE_READY);
            return;
        }
        if (str.equals("hasIccCard")) {
            Log.d("benija", "fixStrings;" + "hasIccCard----" + true);
            paramMethodHookParam.setResult(true);
            return;
        }
        if(str.equals("exists")) {
            // 设备是否root
            if(rootDict.containsKey(paramMethodHookParam.thisObject.toString())) {
                // contain key
                paramMethodHookParam.setResult(Boolean.FALSE);
                Log.d("benija", "newFileCheck1:" + paramMethodHookParam.thisObject.toString());
            }
            Log.d("benija", "newFileCheck2:" + paramMethodHookParam.thisObject.toString());
        }
    }

    public void fixBuild(final XC_LoadPackage.LoadPackageParam lpparam) {
        Log.d("benija", "fixBuildBegin");
        fixBuildV("MANUFACTURER", lpparam);
        fixBuildV("BRAND", lpparam);
        fixBuildV("HARDWARE", lpparam);
        fixBuildV("BOARD", lpparam);
        fixBuildV("SERIAL", lpparam);
        fixBuildV("DEVICE", lpparam);
        fixBuildV("ID", lpparam);
        fixBuildV("PRODUCT", lpparam);
        fixBuildV("DISPLAY", lpparam);
        fixBuildV("FINGERPRINT", lpparam);
        fixBuildV("MODEL", lpparam);
        Log.d("benija", "fixBuildFinish");
//        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "BOARD");
//        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "DEVICE");
//        XposedHelpers.setStaticObjectField(Build.class, "ID", "ID");
//        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", "PRODUCT");
//        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", "DISPLAY");
//        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", "FINGERPRINT");
    }

    public void fixBuildV(String paramString, final XC_LoadPackage.LoadPackageParam lpparam) {
        String str = getValue(paramString);
        if (str.length() > 0) {
            XposedHelpers.setStaticObjectField(android.os.Build.class, paramString, str);
            Log.d("benija", "fixStrings;" + paramString + "----" + str);
        } else {
            Log.e("benija", "fixStringFail;" + paramString + "----" + str);
        }
    }

    public void fixString(XC_MethodHook.MethodHookParam paramMethodHookParam, String paramString) {
        String str = getValue(paramString);
        if (str.length() > 0) {
            paramMethodHookParam.setResult(str);
            Log.d("benija", "fixStrings;" + paramString + "----" + str);
        } else {
            Log.e("benija", "fixStringFail;" + paramString + "----" + str);
        }
    }

    public void fixString2(XC_MethodHook.MethodHookParam paramMethodHookParam, String paramString) {
        String str = getValue(paramString);
        if (str.length() > 0) {
            paramMethodHookParam.setResult(str);
            Log.d("benija", "fffString;" + paramString + "----" + str);
        } else {
            Log.e("benija", "fffStringFail;" + paramString + "----" + str);
        }
    }
}
