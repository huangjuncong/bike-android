package com.coder520.mamabike.utils;

import android.util.Base64;

import java.security.MessageDigest;


public class MD5Util {

    public static String base64encode(String s) {
        try {
            String encodeStr = Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);
            return encodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    public static String base64encodeByte(byte b[]) {
        try {
            String encodeStr = Base64.encodeToString(b, Base64.NO_WRAP);
            return encodeStr;
        } catch (Exception e) {
            return new String(b);
        }
    }

    public static String base64decode(String s) {
        try {
            String decodeStr = new String(Base64.decode(s, Base64.NO_WRAP));
            return decodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    public static byte[] md5encode(byte[] input) {
        byte[] digestedValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            digestedValue = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digestedValue;
    }

    public static void main(String[] args) {
        try {
            String md5Base64 = encrypt16("123456");

            System.out.println(md5Base64);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 先对数据进行md5再base64
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String getMd5Base64(String str) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] b = md5.digest(str.getBytes("utf-8"));
        String newstr = Base64.encodeToString(b, Base64.DEFAULT);// 加密后的字符串
        return newstr;
    }

    /**
     * 进行md532位的加密
     *
     * @param value
     * @return
     */
    @SuppressWarnings("unused")
    public static String getMd5Define(String value) {
        String md5 = "";
        try {
            MessageDigest mdInst = MessageDigest.getInstance("md5");
            byte[] md = mdInst.digest(value.getBytes("utf-8"));// value为认证的原文
            for (int i = 0; i < md.length; i++) {
                int a = 0xf & md[i] >> 4;
                int b = md[i] & 0xf;
                md5 += ("" + "0123456789ABCDEF".charAt(0xf & md[i] >> 4) + "0123456789ABCDEF".charAt(md[i] & 0xf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String getMd5Define(String value, int length) {
        return getMd5Define(value).substring(0, 16);
    }

    /**
     * @Description:Md5加密-32位小写
     * @author:王浩
     */
    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * @Description:加密-16位小写
     * @author:王浩
     */
    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }

}
