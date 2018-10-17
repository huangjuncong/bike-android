package com.coder520.mamabike.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 *
 * IMSI是一个唯一的数字， 标识了GSM和UMTS 网络里的唯一一个用户. 它存储在手机的SIM卡里，它会通过手机发送到网络上. IMSI 与 SIM唯一对应。从技术层面而言，手机的SIM卡上并不会存储手机号码信息，只会存储IMSI（International Mobile Subscriber Identification Number）。手机号码（MSISDN）都是登记在HLR（Home Location Register）中的，在HLR中会把IMSI和MSISDN关联在一起。
 * IMEI也是一串唯一的数字， 标识了GSM 和 UMTS网络里的唯一一个手机.它通常被打印在手机里电池下面的那一面，拨 *#06# 也能看到它. IMEI 与 设备唯一对应.
 */

public class IMSIUtils {
    public static final int CHIP_PLATDORM_MTK = 0x1000;
    public static final int CHIP_PLATDORM_QCOM = 0x1001;
    public static final int CHIP_PLATDORM_KIRIN = 0x1002;

    private static boolean isQualcommDoubleSim;
    private static boolean isMtkDoubleSim;

    /**
     * 获取SIM卡IMSI号码
     * @param context
     * @return
     */
    public static String getIMSI1(Context context) {
        String IMSI = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        IMSI = manager.getSubscriberId();
        return IMSI;
    }

    public static String getIMSI2(Context context, int slotId) {
        String IMSI = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class clazz = manager.getClass();
        Method getImsi= null;

        if(slotId >= 0 && slotId <= 1) {
            try {
                getImsi = clazz.getDeclaredMethod("getSubscriberId",int.class);
                IMSI = (String) getImsi.invoke(manager, slotId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
        }
        return IMSI;
    }

    /**
     * 判断手机硬件平台
     * @return
     */
    public static int getPlatform() {
        int platform = 0;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            String condition = (String) (get.invoke(c, "ro.hardware", "" ));
            if(condition.contains("mt")) {
                platform = CHIP_PLATDORM_MTK;
            } else if(condition.contains("hi")) {
                platform = CHIP_PLATDORM_KIRIN;
            } else if(condition.contains("qc")) {
                platform = CHIP_PLATDORM_QCOM;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return platform;
        }
    }

    /**
     * 获取MTK手机的IMSI号码
     * @return
     */
    public static String getIMSI_MTK() {
        String value = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String)(get.invoke(c, "gsm.sim.operator.imsi", "" ));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }

    /**
     * 获取设备入网IMEI号码
     * @param context
     * @return
     */
    public static String getIMEI1(Context context) {
        String SimSerialNumber = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        SimSerialNumber = manager.getSimSerialNumber();
        return SimSerialNumber;
    }

    /**
     * 获取设备入网IMEI号码
     * @param slotId 只能取值 0 或 1
     */
    public static String getIMEI2(Context context, int slotId) {
        String imei = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class clazz = manager.getClass();
        Method getImei= null;

        if(slotId >= 0 && slotId <= 1) {
            try {
                getImei = clazz.getDeclaredMethod("getImei",int.class);
                imei = (String) getImei.invoke(manager, slotId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
        }
        return imei;
    }

    /**
     * 获取小米手机的IMEI号码
     * @return
     */
    public static String getIMEI_XIAOMI(int slotId) {
        String value = null;
        if(slotId >= 0 && slotId <= 1) {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class, String.class);
                value = (String) (get.invoke(c, "ro.ril.miui.imei" + slotId, ""));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return value;
            }
        } else {
            return value;
        }
    }

    /**
     * 获取设备编码
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        deviceId = manager.getDeviceId();
        return deviceId;
    }

    /**
     * 获取手机号码
     * @param context
     * @return
     */
    public static String getLine1Number(Context context) {
        String line1Number = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        line1Number = manager.getLine1Number();
        return line1Number;
    }

    private static void initMtkDoubleSim(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int simId_1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int simId_2 = (Integer) fields2.get(null);

            Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);
            String imsi_1 = (String) m.invoke(tm, simId_1);
            String imsi_2 = (String) m.invoke(tm, simId_2);

            Method m1 = TelephonyManager.class.getDeclaredMethod(
                    "getDeviceIdGemini", int.class);
            String imei_1 = (String) m1.invoke(tm, simId_1);
            String imei_2 = (String) m1.invoke(tm, simId_2);

            Method mx = TelephonyManager.class.getDeclaredMethod(
                    "getPhoneTypeGemini", int.class);
            int phoneType_1 = (Integer) mx.invoke(tm, simId_1);
            int phoneType_2 = (Integer) mx.invoke(tm, simId_2);

        } catch (Exception e) {
            isMtkDoubleSim = false;
            return;
        }
        isMtkDoubleSim = true;
    }

    public static void initQualcommDoubleSim(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> cx = Class.forName("android.telephony.MSimTelephonyManager");
            Object obj =context.getSystemService("phone_msim");
            int simId_1 = 0;
            int simId_2 = 1;

            Method mx = cx.getMethod("getDataState");
            int stateimei_2 = tm.getDataState();
            Method mde = cx.getMethod("getDefault");
            Method md = cx.getMethod("getDeviceId", int.class);
            Method ms = cx.getMethod("getSubscriberId", int.class);
            Method mp = cx.getMethod("getPhoneType");

            String imei_1 = (String) md.invoke(obj, simId_1);
            String imei_2 = (String) md.invoke(obj, simId_2);

            String imsi_1 = (String) ms.invoke(obj, simId_1);
            String imsi_2 = (String) ms.invoke(obj, simId_2);

        } catch (Exception e) {
            isQualcommDoubleSim = false;
            return;
        }
        isQualcommDoubleSim = true;
    }
}
