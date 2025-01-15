package com.extra.mlkitlibrary.utils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {

    // 硬编码 128 位的 AES 密钥（以字符串形式提供）
    private static final String HARDCODED_KEY = "9fce8c1c76f1f23b2b5f7647467a9d9b"; // 32 characters = 128 bits (16 bytes)

    // 硬编码 16 字节的 IV（以字符串形式提供）
    private static final String HARDCODED_IV = "1f2a4b7bba9071d9adf88e6c9e74602f"; // 32 characters = 128 bits (16 bytes)

    // 从字符串生成密钥
    private static SecretKey generateKeyFromString() {
        byte[] keyBytes = hexStringToByteArray(HARDCODED_KEY);
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }

    // 从字符串生成 IV
    private static IvParameterSpec generateIvFromString() {
        byte[] ivBytes = hexStringToByteArray(HARDCODED_IV);
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
