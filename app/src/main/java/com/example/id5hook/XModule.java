package com.example.id5hook;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.location.LocationManager;
import android.util.DisplayMetrics;
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
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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
        if(!"com.netease.dwrg".equals(lpparam.packageName) && !"com.netease.pes".equals(lpparam.packageName) && !"com.android.settings".equals(lpparam.packageName) && !"com.axieinfinity.origin".equals(lpparam.packageName) ) {
            Log.d("benija", "fuckOther:" + lpparam.packageName);
            return;
        }

        Log.d("benija", "begin:" + lpparam.packageName);
        Log.d("benija", "loader:" + lpparam.classLoader.toString());
        final TelephoneHook telephoneHook = new TelephoneHook();
        // 手机的主要信息
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getDeviceId", telephoneHook);
//        XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", lpparam.classLoader, "getDeviceId", telephoneHook);
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
        XposedHelpers.findAndHookMethod(Runtime.class.getName(), lpparam.classLoader, "exec", String[].class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws  Throwable {
                String[] sArray = (String[]) param1MethodHookParam.args[0];
                if (sArray[0].equals("/system/xbin/which")) {
                     param1MethodHookParam.args = null;
                }
                Log.d("benija", "execStringArray:" + Arrays.toString(sArray));
            }
        });

        // 修改位置信息 todo，暂时没用到不处理
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getLastKnownLocation", String.class,
                new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
                        Log.d("benija", "getLastKnownLocation:" + Arrays.toString(param1MethodHookParam.args));
                    }
                });
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLatitude", telephoneHook);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLongitude", telephoneHook);
        // 修改GSM制式手机的基站信息
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getLac", telephoneHook);
        XposedHelpers.findAndHookMethod(android.telephony.gsm.GsmCellLocation.class.getName(), lpparam.classLoader, "getCid", telephoneHook);

        // 修改ANDROID_ID
        XposedHelpers.findAndHookMethod(Settings.Secure.class.getName(), lpparam.classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                Log.d("benija", "getString:" + param1MethodHookParam.args[1]);
                telephoneHook.fixString2(param1MethodHookParam, (String) param1MethodHookParam.args[1]);
            }
        });

        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                String str = (String) param1MethodHookParam.args[0];
                if(param1MethodHookParam.args[0].equals("init.svc.qemud") || param1MethodHookParam.args[0].equals("init.svc.qemu-props") || param1MethodHookParam.args[0].equals("qemu.hw.mainkeys")
                        || param1MethodHookParam.args[0].equals("qemu.sf.fake_camera") || param1MethodHookParam.args[0].equals("qemu.sf.lcd_density") || param1MethodHookParam.args[0].equals("ro.kernel.android.qemud")
                        || param1MethodHookParam.args[0].equals("ro.kernel.qemu.gles")
                ) {
                    param1MethodHookParam.setResult(null);
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.bootloader")) {
                    param1MethodHookParam.setResult("MSM8953_DAISY1.0_20181010204655");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.bootmode")) {
                    param1MethodHookParam.setResult("auto");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.kernel.qemu")) {
                    param1MethodHookParam.setResult("0");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.board.platform")) {
                    param1MethodHookParam.setResult("msm8953");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.product.device")) {
                    param1MethodHookParam.setResult("gauguininpro");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.hardware")) {
                    param1MethodHookParam.setResult("Qualcomm Technologies, Inc SM7250");
                    return;
                }
                if (param1MethodHookParam.args[0].equals("ro.product.name")) {
                    param1MethodHookParam.setResult(GenParams.getProductName());
                    Log.d("benija", "set----" + param1MethodHookParam.args[0] + "----" + param1MethodHookParam.getResult());
                    return;
                }
                Log.d("benija", "SystemPropertiesGetAfterHook----" + str + "----" + param1MethodHookParam.getResult());
            }
        });

        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "native_get", String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                if (param1MethodHookParam.args.length >0) {
                    Log.d("benija", "native_get----" + param1MethodHookParam.args[0].toString() + "----" + param1MethodHookParam.getResult().toString());
                }
            }
        });

        // 下面这个get是native_get的上一层函数，native_get是获取更底层的,用get也可以，不过hook native_get更加合适
        // XposedBridge.hookAllMethods(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "get", telephoneHook);
        // 为了防止某些APP跳过Build类 而直接使用SystemProperties.native_get获得参数
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                String str = (String) param1MethodHookParam.args[0];
                Log.d("benija", "SystemPropertiesGetAfterHook2----" + str + "----" + param1MethodHookParam.getResult());
                telephoneHook.fixString2(param1MethodHookParam, str);
            }
            // 设定一些静态参数
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                String methodName = param.method.getName();
                if (methodName.startsWith("get")) {
                    // 设定一些静态参数
                    // 不能动位置，之前试过调整去加载app后运行，但会数据异常，应该是加载顺序问题。后面再研究
                    telephoneHook.fixBuild(lpparam);
                }
            }

        });

        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstallerPackageName", String.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                String sss = (String) param1MethodHookParam.args[0];
                if (sss.equals("com.android.flysilkworm") || sss.equals("com.android.coreservice") || sss.equals("com.cyanogenmod.filemanager")) {
                     param1MethodHookParam.setResult(null);
                     param1MethodHookParam.setThrowable(new Exception("Unknown package: " + sss));
                }
                Log.d("benija", "getInstalledPackages----" + sss + "----" + param1MethodHookParam.getResult());
            }
        });

        XposedHelpers.findAndHookMethod(File.class.getName(), lpparam.classLoader, "exists", telephoneHook); // is root
        XposedHelpers.findAndHookConstructor(FileReader.class, String.class, new XC_MethodHook() {
            @SuppressLint("SdCardPath")
            protected void beforeHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                String  readResultStr = (String) param1MethodHookParam.args[0];
                if (readResultStr.equals("/proc/cpuinfo")) {
                    param1MethodHookParam.args[0] = "/mnt/sdcard/cpuinfo_copy";
                    return;
                }
                if (readResultStr.equals("/proc/meminfo")) {
                    param1MethodHookParam.args[0] = "/mnt/sdcard/meminfo_copy";
                    return;
                }
                Log.d("benija", "FileReader----" + param1MethodHookParam.args[0].toString());

            }
        });
        XposedHelpers.findAndHookMethod(BufferedReader.class,  "readLine", new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
                        if (param1MethodHookParam.getResult()!= null) {
                            Log.d("benija", "BufferedReaderHardware----" + param1MethodHookParam.getResult().toString());
                        }
                    }
        });

        Log.d("benija", "init xposed finish");
        // 貌似是在app创建前hook，但不知道为什么hook失败
//        XposedHelpers.findAndHookMethod(Activity.class.getName(), lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
//            protected void afterHookedMethod(MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "a33333333");
//                Configuration configuration = ((Activity)param1MethodHookParam.thisObject).getResources().getConfiguration();
//                configuration.mcc = 460;
//                configuration.mnc = 2;
//                configuration.screenHeightDp = 180;
//                configuration.screenWidthDp = 320;
//                param1MethodHookParam.setResult(configuration);
//            }
//        });

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
//                // Log.d("benija", "getConfiguration:" + param1MethodHookParam.args[1]);
//                Configuration configuration = ((Activity)param1MethodHookParam.thisObject).getResources().getConfiguration();
////                Configuration configuration = ((Activity)param1MethodHookParam.getResult()).getResources().getConfiguration();
//                configuration.mcc = 460;
//                configuration.mnc = 2;
//                configuration.screenHeightDp = 180;
//                configuration.screenWidthDp = 320;
//                param1MethodHookParam.setResult(configuration);
//                Log.d("benija", "getConfiguration:");
//            }
//        });
        // 暂时没发现用到 todo
//        XposedHelpers.findAndHookMethod(java.net.NetworkInterface.class.getName(),lpparam.classLoader, "getHardwareAddress", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1MethodHookParam) throws Throwable {
//                Log.d("benija", "getHardwareAddress:" + param1MethodHookParam.toString());
//            }
//        });


    }
}
