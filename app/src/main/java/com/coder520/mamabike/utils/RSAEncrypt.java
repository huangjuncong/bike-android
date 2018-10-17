package com.coder520.mamabike.utils;

import android.content.Context;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class RSAEncrypt {
    static String DES = "DES";
    static String RSA = "RSA/ECB/PKCS1Padding";
    static String RSA_SERVER = "RSA";

    static String encode = "UTF-8";//保持平台兼容统一使用utf-8  

    //私钥文件路径
    static String privateFile = "D:\\keys\\private_pkcs8_der.key";
    //公钥文件路径
    static String publicFile = "D:\\keys\\public_key.der";

    //des 加密  
    private static byte [] encryptByteDES(byte[] byteD,String strKey) throws Exception {
        return doEncrypt(byteD, getKey(strKey), DES);
    }
    //des 解密  
    private static byte [] decryptByteDES(byte[] byteD,String strKey) throws Exception {
        return doDecrypt(byteD, getKey(strKey), DES);
    }

    public static SecretKey getKey(String strKey)  throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey sk = keyFactory.generateSecret(desKeySpec);
        return sk;
    }

    //pkcs8_der.key文件为私钥 只能保存在服务端   
    //public_key.der为公钥文件，保存在客户端  
    public static void main(String[] args) throws Exception {

//        String text = "ceshishuuju测试数据ceshishuuju测试数据";
//        String pwd="12345678";
//        //客户端加密
//        HashMap<String, String> data = DESAndRSAEncrypt(text.getBytes(encode),pwd);
//        System.out.println("pwd RSA加密后base64："+data.get("key"));
//        System.out.println("text DES加密后base64："+data.get("data"));
//
//        //服务端解密
//        String textDecrypt = DESAndRSADecrypt(data);
//        System.out.println("未处理原文："+text);
//        System.out.println("解密后数据："+textDecrypt);
//        System.out.println(Base64.encode(RSAEncrypt("123456".getBytes())));
//      generateKeyPair();
//        String data = Base64.encode(RSAEncrypt("123456".getBytes()));
//        System.out.println("yadong..." + data);
        String url = URLDecoder.decode("C5nMrdU2%252B1i%252BDaWq6aNVqnfxRLJZVhyexKu8uxdOMz4CKoyVdc5gi9nIEylUE8e8sZ2hPbQVEBxobyPu0lOq3QCmC80VHQpqmpKlxImYpSmvVvI1SdwQtjNFFbeg1632UgINYojo%252BSSjYibBgzTftfwrnlZVq8r28%252BQWw%252FaR5GY%253D");
        System.out.println("yadong..." + url);
        System.out.println("yadong..." + new String(RSADecrypt(Base64.decode(url))));
    }

    //公钥加密   
    public static byte[] RSAEncrypt(Context context, byte[] plainText) throws Exception{
        //读取公钥  
        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
//        FileInputStream bais = new FileInputStream(publicFile);
        InputStream bais = context.getAssets().open("public_key.der");
        Certificate cert = certificatefactory.generateCertificate(bais);
        bais.close();
        PublicKey puk = cert.getPublicKey();
        return doEncrypt(plainText, puk, RSA);
    }
    //私钥解密  
    public static byte[] RSADecrypt(byte[] encryptData) throws Exception{
        FileInputStream in = new FileInputStream(privateFile);
//        InputStream in = MainApplication.getInstance().getAssets().open("private_pkcs8_der.key");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] tmpbuf = new byte[1024];
        int count = 0;
        while ((count = in.read(tmpbuf)) != -1) {
            bout.write(tmpbuf, 0, count);
        }
        in.close();
        //读取私钥
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_SERVER, new BouncyCastleProvider());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bout.toByteArray());
        PrivateKey prk = keyFactory.generatePrivate(privateKeySpec);
//      System.out.println("私钥base64："+encoder.encode(prk.getPrivateExponent().toByteArray()));  
        return doDecrypt(encryptData, prk, RSA_SERVER);
    }

    /**
     * 执行加密操作 
     * @param data 待操作数据 
     * @param key Key 
     * @param type 算法 RSA or DES 
     * @return
     * @throws Exception
     */
    public static byte[] doEncrypt(byte[] data,Key key,String type) throws Exception{
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 执行解密操作 
     * @param data 待操作数据 
     * @param key Key 
     * @param type 算法 RSA or DES 
     * @return
     * @throws Exception
     */
    public static byte[] doDecrypt(byte[] data,Key key,String type) throws Exception{
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static void generateKeyPair() throws Exception{
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(1024); // 指定密钥的长度，初始化密钥对生成器  
        KeyPair kp = kpg.generateKeyPair(); // 生成密钥对  
        RSAPublicKey puk = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey prk = (RSAPrivateKey) kp.getPrivate();
        BigInteger e = puk.getPublicExponent();
        BigInteger n = puk.getModulus();
        BigInteger d = prk.getPrivateExponent();

    }
}  
