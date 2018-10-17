package com.coder520.mamabike.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class DeviceInfoUtils {
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前软件的软件版本名称
     * @param context
     * @return
     */
    public static String getCurrentPackageVersionName(Context context) {
        return getPackageVersionName(context, context.getPackageName());
    }

    /**
     * 获取指定报名的软件版本名称
     * @param context
     * @param packageName
     * @return
     */
    public static String getPackageVersionName(Context context, String packageName) {
        PackageInfo info = getPackageInfo(context, packageName, 0);
        if (info != null) {
            return info.versionName;
        }
        return "";
    }

    private static PackageInfo getPackageInfo(Context context, String packageName, int flag) {
        PackageManager mgr = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = mgr.getPackageInfo(packageName, flag);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
}
