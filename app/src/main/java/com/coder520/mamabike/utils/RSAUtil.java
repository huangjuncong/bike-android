package com.coder520.mamabike.utils;/**
 * Created by wangjianbin on 2017/7/31.
 */



import javax.crypto.Cipher;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 加密解密工具类
 * @author wangjianbin
 * @create 2017-07-31 15:28
 **/

public class RSAUtil {

    /**
     * 私钥字符串
     */
    private static String PRIVATE_KEY = "";
    /**
     * 公钥字符串
     */
    private static String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkMzmWUaJ9Xm+qsE+PJ79J MPrjxTZirU1QaIVTjKXzw3YskkRQ6Wh7KgewBINR+H0QoGTVW8mhBF1ZDxI7 +aqqFgD3mOB4Ct1GTwt5a8Qf4n/auLhjXlt31h6qkI2HZFwuIO/c9xJ2d9Hs Ozjl+ZT+N13fd0/bwVxWVizRWjgJMQIDAQAB";


    public static final String KEY_ALGORITHM = "RSA/ECB/PKCS1Padding";


    /**
     * 读取密钥字符串
     * @throws Exception
     */

    public static void convert() throws Exception {
        byte[] data = null;

        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/enc_pri");
            int length = is.available();
            data = new byte[length];
            is.read(data);
        } catch (Exception e) {
        }

        String dataStr = new String(data);
        try {
            PRIVATE_KEY = dataStr;
        } catch (Exception e) {
        }

        if (PRIVATE_KEY == null) {
            throw new Exception("Fail LoginTo retrieve key");
        }
    }



    /**
     * 私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        convert();
        byte[] keyBytes = Base64Util.decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        Key publicKey = keyFactory.generatePublic(pkcs8KeySpec);

//        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
////        FileInputStream bais = new FileInputStream(publicFile);
//        InputStream bais = new ByteArrayInputStream(key.getBytes());
//        Certificate cert = certificatefactory.generateCertificate(bais);
//        bais.close();
//        PublicKey puk = cert.getPublicKey();

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }




//    public static void main(String[] args) throws Exception {
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
//        keyPairGen.initialize(1024);
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//        System.out.println(Base64.encode(privateKey.getEncoded()));
//        System.out.println(Base64.encode(publicKey.getEncoded()));
//        convert();
//        byte[] enR = encryptByPublicKey("老王来了。。。".getBytes("utf-8"),PUBLIC_KEY);
//        System.out.println(enR.toString());
//        byte[] result = decryptByPrivateKey(enR,PRIVATE_KEY);
//        System.out.println(new String(result,"utf-8"));



//    }

}
