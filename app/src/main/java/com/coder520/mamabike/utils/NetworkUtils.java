package com.coder520.mamabike.utils;

import android.text.TextUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by yadong on 2017/5/17.
 */

public class NetworkUtils {
    public static final char[] toDigit = ("0123456789ABCDEF").toCharArray();

    public static boolean isSameUrl(String url, String url2) {
        if (TextUtils.equals(url, url2)) {
            return true;
        }
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url2)) {
            url = cleanUrl(url);
            url2 = cleanUrl(url2);
            return TextUtils.equals(url, url2);
        }
        return false;
    }

    private static String cleanUrl(String url) {
        url = url.replaceAll("http://", "");
        url = url.replaceAll("https://", "");
        if (url.indexOf("?") != -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    /**
     * 生成Signature
     *
     * @param accessKey          , access Key
     * @param bodyString         , HTTP 请求内容
     * @param userDefinedContent , 客户自定义域，要求小于41字节
     * @return 生成的加密后的Signature
     */
    public static String generateSignature(PublicKey publicKey, String accessKey, String bodyString,
                                           String userDefinedContent) throws Exception {
        // 生成unix时间戳
        int unixTime = (int) (System.currentTimeMillis() / 1000L);
        byte[] unixTimeArray = ByteBuffer.allocate(4).putInt(unixTime).array();

        // 生成随机数
        SecureRandom sr = new SecureRandom();
        byte[] rndBytes = new byte[8];
        sr.nextBytes(rndBytes);

        // 拼接Signature
        byte[] signatureStr = CommonUtils.mergeArray(accessKey.getBytes(Charset.forName("UTF-8")),
                EncryptionHelper.MD5Helper.md5(bodyString).getBytes(Charset.forName("UTF-8")));
        signatureStr = CommonUtils.mergeArray(signatureStr, unixTimeArray);
        signatureStr = CommonUtils.mergeArray(signatureStr, rndBytes);
        signatureStr = CommonUtils.mergeArray(signatureStr,
                userDefinedContent.getBytes(Charset.forName("UTF-8")));

        // RSA加密
        String result = hexEncode(EncryptionHelper.RSAHelper.encrypt(signatureStr, publicKey));

        return result;
    }

    /**
     * 16进制编码
     *
     * @param bytes , 输入字符数组
     * @return 经过16进制编码后的数组
     */
    public static String hexEncode(byte[] bytes) {
        char[] chars = new char[2 * bytes.length];
        int j = 0;
        for (int i = 0; i < bytes.length; ++i) {
            byte bits = bytes[i];
            chars[j++] = toDigit[((bits >>> 4) & 0xF)];
            chars[j++] = toDigit[(bits & 0xF)];
        }
        return new String(chars);
    }

    /**
     * 向URL 中添加查询字段
     *
     * @param url
     * @param appendQuery
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public static URL appendUri(URL url, String appendQuery) throws
            URISyntaxException, MalformedURLException {
        URI oldUri = url.toURI();

        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }

        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
        return newUri.toURL();
    }

    /**
     * 签名
     *
     * @param oriMap
     * @param jessionId
     * @return
     */
    public static String sign(Map<String, Object> oriMap, String jessionId) {
        if (oriMap == null) {
            oriMap = new HashMap<>(1);
        }
        oriMap.put("jssessionId", jessionId);
        return sign(oriMap);
    }

    public static String sign(Map<String, Object> oriMap) {
        Map<String, Object> sortedMap = sortMapByValue(oriMap);
        return generateSignFromMap(sortedMap);
    }

    public static String sign(String sessionId, Object o) {
        Map<String, Object> data = new HashMap<>(2);
        data.put("content", o.toString());
        data.put("jssessionId", sessionId);
        return sign(data);
    }

    public static String generateSignFromMap(Map<String, Object> sortedMap) {
        Set<String> keySet = sortedMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = sortedMap.get(key).toString();
            sb.append(value);
        }
        return MD5Util.encrypt16(sb.toString()).toUpperCase();
    }

    static class ValueComparator implements Comparator<Map.Entry<String, Object>> {
        @Override
        public int compare(Map.Entry<String, Object> me1, Map.Entry<String, Object> me2) {
            Object value1 = me2.getValue();
            Object value2 = me2.getValue();
            String str1;
            String str2;
            if (!(value1 instanceof String)) {
                str1 = value1.toString();
            } else {
                str1 = (String) value1;
            }
            if (!(value2 instanceof String)) {
                str2 = value1.toString();
            } else {
                str2 = (String) value1;
            }
            return str1.compareTo(str2);
        }
    }

    public static Map<String, Object> sortMapByValue(Map<String, Object> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, Object>> entryList = new ArrayList<>(oriMap.entrySet());
        Collections.sort(entryList, new ValueComparator());

        Iterator<Map.Entry<String, Object>> iter = entryList.iterator();
        Map.Entry<String, Object> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
