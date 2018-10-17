package com.coder520.mamabike.utils;


//D:\workCode\android\app\src\main\java>javah -cp . -jni -d ../jni com.zltd.cloan.utils.ImportantUtils
public class ImportantUtils {
    static {
        System.loadLibrary("important");
    }
    public native String getCertificatePassowrd();
    public native String sign(long time, String sessionId, String content);
    public native String getResPub();
    public native String getAesPub();
    public native String getDbPassword();
}
