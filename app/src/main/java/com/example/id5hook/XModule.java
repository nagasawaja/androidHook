package com.example.id5hook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.telephony.TelephonyManager;
import android.os.Build;
import android.net.wifi.WifiInfo;
import android.provider.Settings;
import android.content.ContentResolver;
import de.robv.android.xposed.XC_MethodHook;
import android.app.Activity;
import android.os.Bundle;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class XModule implements IXposedHookLoadPackage {
    public void fixNetwork(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        Log.d("benija", "fixNetworkBegin");
        new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                Log.d("benija", "fixNetwork:" + param1MethodHookParam.method.getName());
                if (param1MethodHookParam.thisObject instanceof ConnectivityManager) {
                    param1MethodHookParam.setResult(((ConnectivityManager)param1MethodHookParam.thisObject).getNetworkInfo(0));
                    return;
                }
                if (param1MethodHookParam.thisObject instanceof android.net.wifi.WifiManager) {
                    param1MethodHookParam.setResult(Boolean.FALSE);
                    return;
                }
                if (param1MethodHookParam.thisObject instanceof WifiInfo) {
                    param1MethodHookParam.setResult(Integer.valueOf(0));
                    return;
                }
                if (((NetworkInfo)param1MethodHookParam.thisObject).getType() == 0) {
                    param1MethodHookParam.setResult(Boolean.TRUE);
                    return;
                }
                param1MethodHookParam.setResult(Boolean.FALSE);
            }
        };
    }

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(!"com.netease.dwrg".equals(lpparam.packageName)) {
            return;
        }
        Log.d("benija", "begin:" + lpparam.packageName);
        String str = TelephonyManager.class.getName();
        final TelephoneHook telephoneHook = new TelephoneHook();
        Log.d("benija", "loader:" + lpparam.classLoader.toString());
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getDeviceId", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getLine1Number", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSimSerialNumber", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSubscriberId", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSimCountryIso", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSimOperator", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSimOperatorName", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getNetworkCountryIso", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getNetworkOperator", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getNetworkOperatorName", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getNetworkType", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getPhoneType", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSimState", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(Build.class.getName(), lpparam.classLoader, "getRadioVersion", new Object[] { telephoneHook });

        XposedHelpers.findAndHookMethod(Activity.class.getName(), lpparam.classLoader, "onCreate", new Object[] { Bundle.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                afterHookedMethod(param1MethodHookParam);
                telephoneHook.fixBuild();
                Configuration configuration = ((Activity)param1MethodHookParam.thisObject).getResources().getConfiguration();
                configuration.mcc = 460;
                configuration.mnc = 2;
                XModule.this.fixNetwork(lpparam);
            }
        } });


        str = WifiInfo.class.getName();
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getMacAddress", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getSSID", new Object[] { telephoneHook });
        XposedHelpers.findAndHookMethod(str, lpparam.classLoader, "getBSSID", new Object[] { telephoneHook });



        XposedHelpers.findAndHookMethod(Settings.Secure.class.getName(), lpparam.classLoader, "getString", new Object[] { ContentResolver.class, String.class, new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                telephoneHook.fixString(param1MethodHookParam, (String)param1MethodHookParam.args[1]);
            }
        } });


        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", new Object[] { String.class, String.class, new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                String str = (String)param1MethodHookParam.args[0];
                telephoneHook.fixString(param1MethodHookParam, str);
            }
        } });

    }
}
