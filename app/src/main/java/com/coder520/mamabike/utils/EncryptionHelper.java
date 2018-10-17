package com.coder520.mamabike.utils;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionHelper {

    /**
     * pem格式文件类
     */
    public static class PemFile {

        private PemObject pemObject;

        public PemFile(InputStream inputStream) throws IOException {
            PemReader pemReader = new PemReader(new InputStreamReader(inputStream));
            try {
                this.pemObject = pemReader.readPemObject();
            } finally {
                pemReader.close();
            }
        }

        public PemFile(String filename) throws FileNotFoundException, IOException {
            PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(
                    PemFile.class.getClassLoader().getResource(filename).getFile())));
            try {
                this.pemObject = pemReader.readPemObject();
            } finally {
                pemReader.close();
            }
        }

        public PemObject getPemObject() {
            return pemObject;
        }
    }

    /**
     * MD5工具类
     */
    public static class MD5Helper {

        public static class Md5EncodingException extends Exception {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public Md5EncodingException() {
            }

            public Md5EncodingException(String msg) {
                super(msg);
            }
        }

        /**
         * 产生MD5, 格式为32字节的16进制编码
         *
         * @return 公钥类
         * @throws Md5EncodingException
         */
        public static String md5(String message) throws Md5EncodingException {
            try {
                // 产生 MD5 Hash(16字节)
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(message.getBytes(Charset.forName("UTF-8")));
                byte messageDigest[] = digest.digest();

                // 16进制编码(32字节)
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++)
                    hexString.append(Integer.toHexString((messageDigest[i] & 0xFF) | 0x100).substring(1, 3));

                return hexString.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Md5EncodingException("无法生成MD5字符串");
            }
        }
    }

    /**
     * RSA工具类
     *
     * @author xylin
     */
    public static class RSAHelper {

        /**
         * 从pem文件读取密钥
         *
         * @param factory
         * @param filename
         * @return
         * @throws InvalidKeySpecException
         * @throws FileNotFoundException
         * @throws IOException
         */
        public static PrivateKey generatePrivateKey(KeyFactory factory, String filename)
                throws InvalidKeySpecException, FileNotFoundException, IOException {
            PemFile pemFile = new PemFile(filename);
            byte[] content = pemFile.getPemObject().getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return factory.generatePrivate(privKeySpec);
        }

        public static class PublicKeyException extends Exception {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public PublicKeyException() {
            }

            public PublicKeyException(String msg) {
                super(msg);
            }
        }

        /**
         * 从pem文件读取公钥
         *
         * @param filename , 公钥文件路径
         * @return 公钥类
         * @throws PublicKeyException
         */
        public static PublicKey loadPublicKey(String filename) throws PublicKeyException {
            try {
                Security.addProvider(new BouncyCastleProvider());
                KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
                PemFile pemFile = new PemFile(filename);
                byte[] content = pemFile.getPemObject().getContent();
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
                return factory.generatePublic(pubKeySpec);
            } catch (Exception e) {
                e.printStackTrace();
                throw new PublicKeyException("载入公钥错误");
            }
        }

        /**
         * 从pem文件读取公钥
         *
         * @return 公钥类
         * @throws PublicKeyException
         */
        public static PublicKey loadPublicKey(InputStream inputStream) throws PublicKeyException {
            try {
                Security.addProvider(new BouncyCastleProvider());
                KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
                PemFile pemFile = new PemFile(inputStream);
                byte[] content = pemFile.getPemObject().getContent();
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
                return factory.generatePublic(pubKeySpec);
            } catch (Exception e) {
                e.printStackTrace();
                throw new PublicKeyException("载入公钥错误");
            }
        }

        /**
         * 从pem文件读取公钥
         *
         * @param factory
         * @param filename
         * @return
         * @throws InvalidKeySpecException
         * @throws FileNotFoundException
         * @throws IOException
         */
        public static PublicKey generatePublicKey(KeyFactory factory, String filename) throws InvalidKeySpecException,
                FileNotFoundException, IOException {
            PemFile pemFile = new PemFile(filename);
            byte[] content = pemFile.getPemObject().getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return factory.generatePublic(pubKeySpec);
        }

        /**
         * 使用公钥加密
         *
         * @param message
         * @param publicKey
         * @return
         * @throws Exception
         */
        public static byte[] encrypt(byte[] message, PublicKey publicKey) throws
                NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                BadPaddingException, IllegalBlockSizeException {

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(message);

            return enBytes;
        }

        /**
         * 使用密钥解密
         *
         * @param result
         * @param privateKey
         * @return
         * @throws Exception
         */
        public static byte[] decrypt(final byte[] result, PrivateKey privateKey) throws Exception {

            Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher1.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher1.doFinal(result);
            return decryptedBytes;
        }
    }

}
