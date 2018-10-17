package com.coder520.mamabike.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 安全的转换string为int
     * @param value string值
     * @param defaultInt string值转换失败后的返回值
     * @return
     */
    public static float string2float(String value, float defaultInt) {
        if (TextUtils.isEmpty(value)) {
            return 1f;
        }
        float out = defaultInt;
        try {
            out = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            Log.e(TAG, "string 2 int error... value is " + value);
        }
        return out;
    }

    /**
     * 合并两个byte数组
     * @param a
     * @param b
     * @return
     */
    public static byte[] mergeArray(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static Map<String, String> urlQueryToMap(String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        return keyValueToString("&", "=", query);
    }

    /**
     * 将手机号码进行处理,隐藏除去头3位和后四位的所有数字, 只能处理长度超过7位的号码
     *
     * @param phoneNumber
     * @return
     */
    public static String securePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() <= 7) {
            return phoneNumber;
        }
        int length = phoneNumber.length();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i <= 2 || i > length - 5) {
                out.append(phoneNumber.charAt(i));
            } else {
                out.append("*");
            }
        }
        return out.toString();
    }

    /**
     * 去除字符串中的 大括号和引号
     *
     * @param input
     * @return
     */
    public static String normalizeString(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        return input.replaceAll("\"", "")
                .replaceAll("\\{", "")
                .replace("}", "")
                .replaceAll("\"", "")
                .replaceAll("\\[", "")
                .replaceAll("]", "");
    }

    /**
     * 将键值对字符串转换成Map
     *
     * @param itemSplit     每一项的分割符号
     * @param keyValueSplit 键，值的分割符号
     * @param keyValueStr   待分割的字符串
     * @return
     */
    public static Map<String, String> keyValueToString(String itemSplit, String keyValueSplit,
                                                       String keyValueStr) {
        if (TextUtils.isEmpty(keyValueStr)) {
            return new HashMap<>(0);
        }
        String[] splitedItem = keyValueStr.split(itemSplit);
        Map<String, String> out = new HashMap<>(splitedItem.length);
        for (String item : splitedItem) {
            splitItem(keyValueSplit, item, out);
        }
        return out;
    }

    /**
     * 将键值对字符串分割并放入Map
     *
     * @param keyValueSplit 键，值 分割符号
     * @param itemStr       待分割的字符串
     * @param out           最终输出
     */
    public static void splitItem(String keyValueSplit, String itemStr,
                                 Map<String, String> out) {
        if (TextUtils.isEmpty(itemStr)) {
            return;
        }
        int split = itemStr.indexOf(keyValueSplit);
        if (itemStr.indexOf(keyValueSplit) == -1) {
            return;
        }
        out.put(itemStr.substring(0, split), itemStr.substring(split + 1));
    }


}
