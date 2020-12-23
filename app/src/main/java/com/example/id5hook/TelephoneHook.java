package com.example.id5hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

public class TelephoneHook extends XC_MethodHook{
    public String uuidd = UUID.randomUUID().toString();

    private String getValue(String p) {
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
//            paramMethodHookParam.setResult(2);
            return;
        }
        if (str.equals("getSimState")) {
            Log.d("benija", "fixStrings;" + "getSimState----" + TelephonyManager.SIM_STATE_READY);
            paramMethodHookParam.setResult(TelephonyManager.SIM_STATE_READY);
//            paramMethodHookParam.setResult(5);
            return;
        }
        if (str.equals("hasIccCard")) {
            Log.d("benija", "fixStrings;" + "hasIccCard----" + true);
            paramMethodHookParam.setResult(true);
//            paramMethodHookParam.setResult(5);
            return;
        }
        fixString(paramMethodHookParam, str);
    }

    public void fixBuild() {
        Log.d("benija", "fixBuild");
        fixBuildV("MODEL");
        fixBuildV("MANUFACTURER");
        fixBuildV("BRAND");
        fixBuildV("HARDWARE");
        fixBuildV("BOARD");
        fixBuildV("SERIAL");
        fixBuildV("DEVICE");
        fixBuildV("ID");
        fixBuildV("PRODUCT");
        fixBuildV("DISPLAY");
        fixBuildV("FINGERPRINT");

//        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "BOARD");
//        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "DEVICE");
//        XposedHelpers.setStaticObjectField(Build.class, "ID", "ID");
//        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", "PRODUCT");
//        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", "DISPLAY");
//        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", "FINGERPRINT");
    }

    public void fixBuildV(String paramString) {
        String str = getValue(paramString);
        if (str != null && str.length() > 0) {
            XposedHelpers.setStaticObjectField(Build.class, paramString, str);
            Log.d("benija", "fixBuildV;" + paramString + "----" + str);
        } else {
            Log.e("benija", "fixBuildVFail;" + paramString + "----" + str);
        }
    }

    public void fixString(XC_MethodHook.MethodHookParam paramMethodHookParam, String paramString) {
        String str = getValue(paramString);
        if (str != null && str.length() > 0) {
            paramMethodHookParam.setResult(str);
            Log.d("benija", "fixStrings;" + paramString + "----" + str);
        } else {
            Log.e("benija", "fixStringFail;" + paramString + "----" + str);
        }
    }
}
