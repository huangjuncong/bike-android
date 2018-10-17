package com.coder520.mamabike.utils;

/**
 * Created by yadong on 2017/8/7.
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES static function for different key and iv
 * mode: AES/CBC/PKCS5Padding
 * text input encoding: utf-8
 * text output encoding: base64
 *
 */
public class Aes {
    private static final byte[] key = "af70ae89-15d6-4a".getBytes();
    private static final byte[] ivk = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private Aes() {
    }

//	public static final String bytesPrint(byte[] in) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < in.length; i++) {
//			sb.append(in[i]).append(",");
//		}
//		String out = sb.toString();
//		System.out.println(out);
//		return out;
//	}

    public static final byte[] encBytes(byte[] srcBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivk);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(srcBytes);
        return encrypted;
    }

    public static final String encText(String sSrc) throws Exception {
        byte[] srcBytes = sSrc.getBytes("utf-8");
        byte[] encrypted = encBytes(srcBytes);
        return Base64.encode(encrypted);
    }

    public static final byte[] decBytes(byte[] srcBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivk);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(srcBytes);
        return encrypted;
    }

    public static final String decText(String sSrc)
            throws Exception {
        byte[] srcBytes = Base64.decode(sSrc);
        byte[] decrypted = decBytes(srcBytes);
        return new String(decrypted, "utf-8");
    }

    public static void main(String[] args) {
        String s = "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";

        try {
//            String enc = encText(s);
//            System.out.println(enc);
            String dec = decText(s);
            System.out.println(dec);

            // If there is only one key and one iv, use AesInstance for better performance,but generally the iv need change everyTime

            // AesInstance ai = AesInstance.getInstance(key, ivk);
            // enc = ai.encText(s);
            // System.out.println(enc);
            // dec = ai.decText(enc);
            // System.out.println(dec);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
