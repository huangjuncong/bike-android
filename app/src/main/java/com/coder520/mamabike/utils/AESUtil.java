package com.coder520.mamabike.utils;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by JackWangon[www.coder520.com] 2017/7/31.
 */

public class AESUtil {


    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";


    /**
     * AES对称加密
     * @param data
     * @param key key需要16位
     * @return
     */
    public static String encrypt(String data , String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"),KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE , spec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * AES对称解密 key需要16位
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE , spec , new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    public static void main(String[] args) throws Exception {
        String okey = "123456789abcdefg";
        //移动端随机key  AES加密数据
       String enr= encrypt("{'mobile':'18980840843','code':'6666','platform':'android'}",okey);
       System.out.println(enr);
       //移动端RSA加密AES的key 和加密的数据一起传到服务器
//       byte[] keyrsa = RSAUtil.encryptByPublicKey(okey.getBytes("UTF-8"),"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkMzmWUaJ9Xm+qsE+PJ79J MPrjxTZirU1QaIVTjKXzw3YskkRQ6Wh7KgewBINR+H0QoGTVW8mhBF1ZDxI7 +aqqFgD3mOB4Ct1GTwt5a8Qf4n/auLhjXlt31h6qkI2HZFwuIO/c9xJ2d9Hs Ozjl+ZT+N13fd0/bwVxWVizRWjgJMQIDAQAB");
//       System.out.println(Base64Util.encode(keyrsa));
//       String base = Base64Util.encode(keyrsa);

//       //服务端RSA解密AES的key
       byte[] keybyte= RSAUtil.decryptByPrivateKey(Base64Util.decode("HXPQS2eYSoERX54M28ImvnwSez5s6jpSABYrTZJYDPBJMM0U0sUf6hrGZ4V/ 6rZF3UCjM5zXdclOxM+fwkjQ46CObEHY7gcRqEVqW0wXlArokIIPncUtYSKN rmjVkqdiogopufhqNkK805BBZiENe10ePAraTmJTmxM3Nu39m1U="));
       String keyR=new String(keybyte,"UTF-8");
        System.out.println(keyR);
       String  result = decrypt(enr,keyR);
        System.out.println(result);
    }
}
