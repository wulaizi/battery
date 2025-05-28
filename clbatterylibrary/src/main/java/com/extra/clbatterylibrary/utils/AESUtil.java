package com.extra.clbatterylibrary.utils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {

    // 从native层获取密钥
    private static String getKey() {
        return SecurityUtil.INSTANCE.getAesKey();
    }

    // 从native层获取IV
    private static String getIv() {
        return SecurityUtil.INSTANCE.getAesIv();
    }

    // 从字符串生成密钥
    private static SecretKey generateKeyFromString() {
        byte[] keyBytes = hexStringToByteArray(getKey());
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }

    // 从字符串生成 IV
    private static IvParameterSpec generateIvFromString() {
        byte[] ivBytes = hexStringToByteArray(getIv());
        return new IvParameterSpec(ivBytes);
    }

    // 加密方法
    public static String encrypt(String data) throws Exception {
        SecretKey key = generateKeyFromString();
        IvParameterSpec iv = generateIvFromString();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encrypted,Base64.NO_WRAP); // 使用 Base64 编码为可打印的字符串
    }

    // 解密方法
    public static String decrypt(String encryptedData) throws Exception {
        SecretKey key = generateKeyFromString();
        IvParameterSpec iv = generateIvFromString();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decodedData = Base64.decode(encryptedData,Base64.NO_WRAP); // 解码 Base64 字符串
        byte[] decrypted = cipher.doFinal(decodedData);
        return new String(decrypted);
    }

    // 辅助函数：将十六进制字符串转换为字节数组
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
