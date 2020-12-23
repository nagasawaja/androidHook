package com.example.id5hook;

import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import android.telephony.TelephonyManager;
import android.net.wifi.WifiInfo;
import android.provider.Settings;
import android.content.ContentResolver;

import de.robv.android.xposed.XposedBridge;


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
                    param1MethodHookParam.setResult(((ConnectivityManager) param1MethodHookParam.thisObject).getNetworkInfo(0));
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
                if (((NetworkInfo) param1MethodHookParam.thisObject).getType() == 0) {
                    param1MethodHookParam.setResult(Boolean.TRUE);
                    return;
                }
                param1MethodHookParam.setResult(Boolean.FALSE);
            }
        };
    }

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!"com.netease.dwrg".equals(lpparam.packageName)) {
            return;
        }

        Log.d("benija", "begin:" + lpparam.packageName);
        Log.d("benija", "loader:" + lpparam.classLoader.toString());
        final TelephoneHook telephoneHook = new TelephoneHook();
        telephoneHook.fixBuild();
        // 手机的主要信息
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getDeviceId", telephoneHook);
        XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", lpparam.classLoader, "getDeviceId", telephoneHook);
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getLine1Number", telephoneHook); // number
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSimSerialNumber", telephoneHook); // simserial
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSubscriberId", telephoneHook); // imsi
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSimCountryIso", telephoneHook); // simcountryiso
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSimOperator", telephoneHook); // simoperator
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSimOperatorName", telephoneHook); // simoperatorname
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getNetworkCountryIso", telephoneHook); // networkcountryiso
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getNetworkOperator", telephoneHook); // "networkoperator
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getNetworkOperatorName", telephoneHook); // networkoperatorname
        // 设置手机信息 无论手机是否插入了sim卡 都会模拟出SIM卡的信息 APP获得SIM卡消息时返回该手机已有SIM卡
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getNetworkType", telephoneHook);
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getPhoneType", telephoneHook);
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getSimState", telephoneHook);
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "hasIccCard", telephoneHook);
        // WIFI信息
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), lpparam.classLoader, "getMacAddress", telephoneHook); // wifimac
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), lpparam.classLoader, "getSSID", telephoneHook); // ssid
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), lpparam.classLoader, "getBSSID", telephoneHook); // getBSSID
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), lpparam.classLoader, "getHardwareAddress", telephoneHook); // HardwareAddress
        // 蓝牙信息
        XposedHelpers.findAndHookMethod(BluetoothAdapter.class.getName(), lpparam.classLoader, "getAddress", telephoneHook);
        XposedHelpers.findAndHookMethod(BluetoothAdapter.class.getName(), lpparam.classLoader, "getName", telephoneHook);
        // 防止APP使用Runtime.exec方式获取一些特定的系统属性
        XposedHelpers.findAndHookMethod(Runtime.class.getName(), lpparam.classLoader, "exec", telephoneHook);
        // 修改位置信息
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getLastKnownLocation", telephoneHook);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLatitude", telephoneHook);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLongitude", telephoneHook);
        // 修改GSM制式手机的基站信息
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getLac", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getCid", telephoneHook);
        // 修改CDMA制式手机的基站信息
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationLatitude", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationLongitude", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationId", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getSystemId", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getNetworkId", telephoneHook);
        // 此处模拟正常用户的APP列表 其中随机的增加和删除一些常用APP 以达到每个手机的APP有很大的随意性和合理性
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledPackages", telephoneHook);
        // 防止APP的VPN SOCK5 HTTP代理检测
        XposedHelpers.findAndHookMethod(java.net.NetworkInterface.class.getName(), lpparam.classLoader, "getNetworkInterfacesList", telephoneHook);

        XposedHelpers.findAndHookMethod(android.os.Build.class.getName(), lpparam.classLoader, "getRadioVersion", telephoneHook);
        XposedHelpers.findAndHookMethod(android.os.Build.VERSION.class.getName(), lpparam.classLoader, "RELEASE", telephoneHook);
        XposedHelpers.findAndHookMethod(android.os.Build.VERSION.class.getName(), lpparam.classLoader, "SDK", telephoneHook);
        XposedHelpers.findAndHookMethod(android.content.res.Resources.class.getName(), lpparam.classLoader, "getConfiguration", telephoneHook);


//        XposedBridge.hookAllMethods(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "get", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Log.d("benija", "afterHookedMethod:" + param.toString());
//            }
//        });
//        XposedBridge.hookAllMethods(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "get", telephoneHook);

//        XposedHelpers.findAndHookMethod(Activity.class.getName(), lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
//            protected void beforeHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
//                afterHookedMethod(param1MethodHookParam);
//                telephoneHook.fixBuild();
//                Configuration configuration = ((Activity)param1MethodHookParam.thisObject).getResources().getConfiguration();
//                configuration.mcc = 460;
//                configuration.mnc = 2;
//                XModule.this.fixNetwork(lpparam);
//            }
//        });
        // 为了防止某些APP跳过Build类 而直接使用SystemProperties.native_get获得参数
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "native_get", telephoneHook);


        XposedHelpers.findAndHookMethod(Settings.Secure.class.getName(), lpparam.classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                Log.d("benija", "getString");
                telephoneHook.fixString(param1MethodHookParam, (String) param1MethodHookParam.args[1]);
            }
        });

        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                Log.d("benija", "get");
                String str = (String) param1MethodHookParam.args[0];
                telephoneHook.fixString(param1MethodHookParam, str);
            }
        });
    }

}
