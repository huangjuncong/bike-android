package com.coder520.mamabike.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class FileUitls {
    /**
     * 读取图片文件内容，并转为Base64编码
     *
     * @return 图片内容Base64编码的字符串
     */
    public static String getImageBase64Content(InputStream inputStream, long size)
            throws Exception {
        byte[] bytes = null;
        try {
            long length = size;
            bytes = new byte[(int) length];

            int offset = 0, numRead = 0;
            while (offset < bytes.length
                    && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        return MD5Util.base64encodeByte(bytes);
    }

    /**
     * 读取图片文件内容，并转为Base64编码
     *
     * @param filePath 文件路径
     * @return 图片内容Base64编码的字符串
     */
    public static String getImageBase64Content1(String filePath)
            throws Exception {
        File imgFile = new File(filePath);
        InputStream is = new FileInputStream(imgFile);
        byte[] bytes = null;
        try {
            long length = imgFile.length();
            bytes = new byte[(int) length];

            int offset = 0, numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return MD5Util.base64encodeByte(bytes);
    }

    //调用者保证创建文件
    public static void writeFile(InputStream inputStream, File file) {

        FileOutputStream fos = null;
        try {

            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);
            int len = 0;
            while((len = inputStream.read(buffer)) != -1){
                fos.write(buffer, 0, len);
                fos.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(null != fos){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
