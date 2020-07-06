package com.example.id5hook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Random;

import external.org.apache.commons.lang3.StringUtils;


public class GenParams {
    public String data;

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
}
