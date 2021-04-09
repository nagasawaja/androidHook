package com.example.id5hook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Random;

import external.org.apache.commons.lang3.StringUtils;


public class GenParams {
    public String data;
    public static String[]MODEL = {"ZHONGXING", "GELI", "XIAOMI", "MEIZU", "HUAWEI", "GOOGLE", "SONY", "OPPO", "SAMSUNG", "LG", "ZTE", "VIVO", "HONOR", "ONEPLUS", "HTC", "NUBIA", "HISENSE", "联想", "乐视", "锤子", "一加"};
    public static String[]MODEM = {"21C20B088S000C000,21C20B088S000C000","G973USQS4ETJ2","21C20B388S000C000,21C20B388S000C000","G970USQS4ETJ2","21C60B269S007C000,21C60B269S007C000","8974-AAAAANAZQ-10270086-81","M8940_41.00.01.163R SURF_NA_CA_CUST","21C20B526S000C000,21C20B526S000C000","S727VLUDS4ARF2","N975FXXS6DTK8,N975FXXS6DTK8","J530FXXS7CTF1,J530FXXS7CTF1","MPSS.AT.3.1-00777-SDM660_GEN_PACK-1.290939.2.294155.1","ztesh6735_65t_m_lwg_dsds","MPSS.JO.3.1.c5-00003-8937_GENNS_PACK-1_V048","M_V3_P10,M_V3_P10","A217FXXU4BTL1,A217FXXU4BTL1","MOLY.LR12A.R3.MP.V101.3,MOLY.LR12A.R3.MP.V101.3","G973USQS4ETJ2","N975FXXS6DTK8,N975FXXS6DTK8", "21C20B369S007C000,21C20B369S007C000"};
    public static String[]NetworkOperator = {"460-00", "460-01", "460-02", "460-03"};
    public static HashMap<String, String> getNetworkOperatorName = new HashMap<String, String>(){{
        put("460-00", "中国移动");
        put("460-01", "中国联通");
        put("460-02", "中国移动");
        put("460-03", "中国联通");
    }};
    private static Random random = new Random();


    public static String getDeviceId() {
        Random random = new Random();
        String str = "";
        for (int i = 0;; i++) {
            if (i >= 4) {
                String str1 = "";
                for (i = 0;; i++) {
                    if (i >= 2) {
                        str = String.valueOf("") + "35" + str + str1;
                        for (i = 0;; i++) {
                            if (i >= 6) {
                                i = 0;
                                int n = str.length() - 1;
                                while (true) {
                                    if (n < 0) {
                                        if (i % 10 == 0)
                                            return String.valueOf(str) + "0";
                                    } else {
                                        int i1 = (str.charAt(n) - 48) * 2;
                                        int i2 = i1 / 10;
                                        i = i + i2 + i1 % 10 + str.charAt(--n) - 48;
                                        n--;
                                        continue;
                                    }
                                    return String.valueOf(str) + (10 - i % 10);
                                }
                            }
                            int m = Math.abs(random.nextInt());
                            str = String.valueOf(str) + (m % 10);
                        }
                    }
                    int k = Math.abs(random.nextInt());
                    str1 = String.valueOf(str1) + (k % 10);
                }
            }
            int j = Math.abs(random.nextInt());
            str = String.valueOf(str) + (j % 10);
        }
    }

    public static String getAndroid() {
        return genRandStr(16).toLowerCase();
    }

    public static String getSimSerialNumber() {
        return genRandNum(20);
    }

    public static String getSubscriberId() {
        Random random = new Random();
        int i = Math.abs(random.nextInt());
        String[] array = new String[3];
        array[0] = "46000";
        array[1] = "46002";
        array[2] = "46007";
        String str = "" + array[i % 3];
        for (i = 0;; i++) {
            if (i >= 10)
                return str;
            int j = Math.abs(random.nextInt());
            str = str + (j % 10);
        }
    }

    // 获取mac地址
    public static String getMac() {
        StringBuilder returnValue = new StringBuilder();
        Random random = new Random();
        String[] mac = {
                String.format("%02x", 0x52),
                String.format("%02x", 0x54),
                String.format("%02x", 0x00),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };

        for (String s : mac) {//进行遍历
            returnValue.append(s).append(":");
        }
        return returnValue.substring(0, (returnValue.length()-1));
    }

    public static String genRandStr(int paramInt) {
        String str = "";
        Random random = new Random();
        int i = 0;
        while (true) {
            char c;
            if (i >= paramInt)
                return str;
            switch (random.nextInt(3)) {
                case 0:
                    c = (char)(random.nextInt(10) + 48);
                    str = String.valueOf(str) + c;
                    i++;
                    break;
                case 1:
                    c = (char)(random.nextInt(10) + 97);
                    str = String.valueOf(str) + c;
                    i++;
                    break;
                case 2:
                    c = (char)(random.nextInt(10) + 65);
                    str = String.valueOf(str) + c;
                    i++;
                    break;
            }
        }
    }

    public static String genRandNum(int paramInt) {
        String str = "";
        Random random = new Random();
        for (int i = 0;; i++) {
            if (i >= paramInt)
                return str;
            str = String.valueOf(str) + String.valueOf(random.nextInt(10));
        }
    }

    // 获取手机model
    public static String getMODEL() {
        String str = "";
        for (int i = 0;i<5;i++){
            str = str+ (char)(Math.random()*26+'A');
        }
        str = str + "'";
        for (int i = 0;i<6;i++){
            str = str+ (char)(Math.random()*26+'A');
        }
        return str;
    }

    public static String getMANUFACTURER() {
        int index = (int) (Math.random() * MODEL.length);
        return MODEL[index];
    }

    public static String getMODEM() {
        int index = (int) (Math.random() * MODEM.length);
        return MODEM[index];
    }

    public static String getImpl() {
        int index = (int) (Math.random() * MODEM.length);
        return MODEM[index];
    }

    public static String randomTelephonyGetLine1Number() {
        String[] telFirst    = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        String   line1Number = "";

        boolean isUserArea = randomInt(0, 100) < 30;
        if (isUserArea) line1Number += "+86";

        return line1Number + telFirst[randomInt(0, telFirst.length - 1)] + randomString(8, false, false, true);
    }

    public static String getNetworkOperator() {
        int index = (int) (Math.random() * NetworkOperator.length);
        return NetworkOperator[index];
    }

    public static String randomBuildSerial() {
        return randomString(randomInt(10, 20), true, false, true);
    }

    public static String randomWifiInfoSSID() {
        String[] strings = new String[]{"大垃圾", "蹭网", "斯蒂芬", "T阿斯蒂芬", "放大", "吧等123", "过滤法的"};
        return strings[randomInt(0, strings.length - 1)] + randomString(randomInt(5, 8), false, true, true);
    }

    public static String randomString(int length, boolean lowEnglish, boolean upperEnglish, boolean number) {
        String baseString = "";
        if (lowEnglish) baseString += "abcdefghijklmnopqrstuvwxyz";
        if (upperEnglish) baseString += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (number) baseString += "0123456789";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(baseString.charAt(random.nextInt(baseString.length())));
        }
        return sb.toString();
    }

    public static int randomInt(int min, int max) {
        if (min == max) return min;
        return random.nextInt(max) + min;
    }

}
