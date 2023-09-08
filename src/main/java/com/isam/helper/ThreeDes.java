package com.isam.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class ThreeDes {
	private static String arickey = "ISAM2013";
	private static String airciv = "CIER2013";

	public static String getEncryptString(String inString) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(arickey.getBytes());

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		IvParameterSpec iv = new IvParameterSpec(airciv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		Base64.Encoder encoder = Base64.getMimeEncoder();

		return new String(encoder.encode(cipher.doFinal(inString.getBytes(StandardCharsets.UTF_8))),
				StandardCharsets.UTF_8);
	}

	public static String getDecryptString(String inString) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(arickey.getBytes());

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		IvParameterSpec iv = new IvParameterSpec(airciv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		Base64.Decoder decoder = Base64.getMimeDecoder();

		return new String(cipher.doFinal(decoder.decode(inString)), StandardCharsets.UTF_8);
	}

	public static String toMD5(String inString) {
		String md5String = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inString.getBytes());
			byte[] digest = md.digest();
			md5String = toHex(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5String;
	}

	private static String toHex(byte[] digest) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < digest.length; ++i) {
			byte b = digest[i];
			int value = (b & 0x7F) + (b < 0 ? 128 : 0);
			buffer.append(value < 16 ? "0" : "");
			buffer.append(Integer.toHexString(value));
		}
		return buffer.toString();
	}

	public static boolean checkMD5(String a, String b) {
		boolean result = false;
		if (a != null || b != null) {
			String a2 = toMD5(a);
			String a1 = toMD5(b);
			if (a1.equals(a2)) {
				result = true;
			}
		}
		return result;
	}
}