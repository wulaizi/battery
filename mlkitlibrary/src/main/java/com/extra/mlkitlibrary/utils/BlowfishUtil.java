package com.extra.mlkitlibrary.utils;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BlowfishUtil {

	// 加密
	public static String encrypt(String ivStr,byte[] data) throws CryptoException {
        byte[] key = "qwe8iba2".getBytes(StandardCharsets.UTF_8); // 64位密钥
        byte[] iv = ivStr.getBytes(StandardCharsets.UTF_8); // 初始向量，8字节
		BlockCipher engine = new BlowfishEngine(); // 使用 Blowfish 加密算法
		CBCBlockCipher cipher = new CBCBlockCipher(engine); // CBC模式
		cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv)); // 初始化为加密模式
		// 填充数据并执行加密
		return Hex.toHexString(processCipher(cipher, data)); // 执行加密
	}

	// 解密
	public static String decrypt(String ivStr,byte[] data) throws CryptoException {
        byte[] key = "qwe8iba2".getBytes(StandardCharsets.UTF_8); // 64位密钥
        byte[] iv = ivStr.getBytes(StandardCharsets.UTF_8); // 初始向量，8字节
		BlockCipher engine = new BlowfishEngine();
		CBCBlockCipher cipher = new CBCBlockCipher(engine);
		cipher.init(false, new ParametersWithIV(new KeyParameter(key), iv)); // 初始化为解密模式
		// 解密并自动去除填充
		return new String(processCipher(cipher, data),StandardCharsets.UTF_8); // 执行解密
	}

	// 执行加密或解密
	private static byte[] processCipher(CBCBlockCipher cipher, byte[] data) throws CryptoException {
		int blockSize = cipher.getBlockSize(); // 获取块大小，通常为 8 字节（Blowfish 的块大小）
		int len = data.length;
		int paddedLength = (len + blockSize - 1) / blockSize * blockSize; // 填充数据长度，确保是块大小的倍数

		byte[] paddedData = Arrays.copyOf(data, paddedLength); // 填充数据
		byte[] output = new byte[paddedLength]; // 输出数据

		// 按块处理数据
		for (int i = 0; i < paddedLength; i += blockSize) {
			byte[] block = Arrays.copyOfRange(paddedData, i, i + blockSize); // 按块获取数据
			byte[] resultBlock = new byte[blockSize];

			cipher.processBlock(block, 0, resultBlock, 0); // 处理块

			System.arraycopy(resultBlock, 0, output, i, blockSize); // 写入输出数组
		}

		return output;
	}

}
