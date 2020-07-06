package com.example.id5hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import android.os.Build;
import android.util.Log;

public class TelephoneHook extends XC_MethodHook{

    private String getValue(String p) {
        XSharedPreferences  v  = new XSharedPreferences("com.example.id5hook","config");
        String returnValue = v.getString(p, "");
        if (returnValue == "") {
            Log.d("benija", "getValue:" + p);
        }
        return returnValue;
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        String str = paramMethodHookParam.method.getName();
        if (str.equals("getNetworkType")) {
            paramMethodHookParam.setResult(Integer.valueOf(4));
            return;
        }
        if (str.equals("getPhoneType")) {
            paramMethodHookParam.setResult(Integer.valueOf(2));
            return;
        }
        if (str.equals("getSimState")) {
            paramMethodHookParam.setResult(Integer.valueOf(5));
            return;
        }
        fixString(paramMethodHookParam, str);
    }

    public void fixBuild() {
        fixBuildV("MODEL");
        fixBuildV("MANUFACTURER");
        fixBuildV("HARDWARE");
        fixBuildV("BRAND");
        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "BOARD");
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "DEVICE");
        XposedHelpers.setStaticObjectField(Build.class, "ID", "ID");
        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", "PRODUCT");
        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", "DISPLAY");
        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", "FINGERPRINT");
    }

    public void fixBuildV(String paramString) {
        String str = getValue(paramString);
        if (str != null && str.length() > 0) {
            XposedHelpers.setStaticObjectField(Build.class, paramString, str);
            Log.d("benija", "fixBuildV;" + paramString + "----" + str);
        }
    }



    public void fixString(XC_MethodHook.MethodHookParam paramMethodHookParam, String paramString) {
        String str = getValue(paramString);
        if (str != null && str.length() > 0) {
            paramMethodHookParam.setResult(str);
            Log.d("benija", "fixString;" + paramString + "----" + str);
        }
    }
}
