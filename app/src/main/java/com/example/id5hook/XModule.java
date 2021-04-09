package com.example.id5hook;

import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.location.LocationManager;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

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
        if(!"com.netease.dwrg".equals(lpparam.packageName) && !"com.netease.pes".equals(lpparam.packageName)) {
            Log.d("benija", "fuckOther:" + lpparam.packageName);
            return;
        }
        Log.d("benija", "begin:" + lpparam.packageName);
        Log.d("benija", "loader:" + lpparam.classLoader.toString());
        final TelephoneHook telephoneHook = new TelephoneHook();
        // 设定一些静态参数
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
        // 暂时没发现用到 todo
//        XposedHelpers.findAndHookMethod(java.net.NetworkInterface.class.getName(),lpparam.classLoader, "getHardwareAddress", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getHardwareAddress:" + param1MethodHookParam.toString());
//            }
//        });
        // 修改CDMA制式手机的基站信息
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationLatitude", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationLongitude", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getBaseStationId", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getSystemId", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.cdma.CdmaCellLocation.class.getName(), lpparam.classLoader, "getNetworkId", telephoneHook);

        // 蓝牙信息
        XposedHelpers.findAndHookMethod(BluetoothAdapter.class.getName(), lpparam.classLoader, "getAddress", telephoneHook);
        XposedHelpers.findAndHookMethod(BluetoothAdapter.class.getName(), lpparam.classLoader, "getName", telephoneHook);
        // 防止APP使用Runtime.exec方式获取一些特定的系统属性
        XposedHelpers.findAndHookMethod(Runtime.class.getName(), lpparam.classLoader, "exec", String.class, String[].class, File.class, new XC_MethodHook() {
            /*
            id5：exec:[ls -l /system/bin/su, null, null] hook
             */
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                Log.d("benija", "exec:" + param1MethodHookParam.args[0]);
                Process execRes = (Process) param1MethodHookParam.getResult();
                Log.d("benija", "afterHookedMethod: " + execRes);
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(execRes.getInputStream()));
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    Log.d("benija", "execRes:" + s);
                }
                param1MethodHookParam.setResult(execRes);
            }
        });
        // 修改位置信息 todo，暂时没用到不处理
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getLastKnownLocation", String.class,
                new XC_MethodHook()
                {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                        Log.d("benija", "getLastKnownLocation:" + Arrays.toString(param1MethodHookParam.args));
                    }
                });
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLatitude", telephoneHook);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLongitude", telephoneHook);
        // 修改GSM制式手机的基站信息
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getLac", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getCid", telephoneHook);

        // 此处模拟正常用户的APP列表 其中随机的增加和删除一些常用APP 以达到每个手机的APP有很大的随意性和合理性 TODO
//        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getInstalledPackages:" + param1MethodHookParam.toString());
//            }
//        });
//        // 防止APP的VPN SOCK5 HTTP代理检测 TODO
//        XposedHelpers.findAndHookMethod(java.net.NetworkInterface.class.getName(), lpparam.classLoader, "getNetworkInterfacesList", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getNetworkInterfacesList:" + param1MethodHookParam.toString());
//            }
//        });
//        // 防止APP的VPN SOCK5 HTTP代理检测 TODO
//        XposedHelpers.findAndHookMethod(java.net.NetworkInterface.class.getName(), lpparam.classLoader, "getNetworkInterfaces", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getNetworkInterfacesList2:" + param1MethodHookParam);
//            }
//        });
//        XposedHelpers.findAndHookMethod(android.os.Build.class.getName(), lpparam.classLoader, "getRadioVersion", telephoneHook);
//        XposedHelpers.findAndHookMethod(android.os.Build.VERSION.class.getName(), lpparam.classLoader, "RELEASE", telephoneHook); // TODO
//        XposedHelpers.findAndHookMethod(android.os.Build.VERSION.class.getName(), lpparam.classLoader, "SDK", telephoneHook); // TODO
        // TODO mcc mnc
//        XposedHelpers.findAndHookMethod(android.content.res.Resources.class.getName(), lpparam.classLoader, "getConfiguration", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getConfiguration:" + param1MethodHookParam.args[1]);
//                telephoneHook.fixString2(param1MethodHookParam, (String) param1MethodHookParam.args[1]);
//            }
//        });
        // 修改ANDROID_ID TODO
        XposedHelpers.findAndHookMethod(Settings.Secure.class.getName(), lpparam.classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getString:" + param1MethodHookParam.args[1]);
                telephoneHook.fixString2(param1MethodHookParam, (String) param1MethodHookParam.args[1]);
            }
        });

        // 下面这个get是native_get的上一层函数，native_get是获取更底层的,用get也可以，不过hook native_get更加合适
        // XposedBridge.hookAllMethods(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "get", telephoneHook);
        // 为了防止某些APP跳过Build类 而直接使用SystemProperties.native_get获得参数
        // todo
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                String str = (String) param1MethodHookParam.args[0];
//                Log.d("benija", "native_get:" + str);
                telephoneHook.fixString2(param1MethodHookParam, str);
            }
        });

        // 貌似是在app创建前hook，但不知道为什么hook失败
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

    }
}
