/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.ihangjing.ZJYXForAndroid;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA������
 * 
 */
public class Rsa {

//	private static final String ALGORITHM = "RSA";

//	private static PublicKey getPublicKeyFromX509(String algorithm,
//			String bysKey) throws NoSuchAlgorithmException, Exception {
//		byte[] decodedKey = Base64.decode(bysKey);
//		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
//
//		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
//		return keyFactory.generatePublic(x509);
//	}

//	public static String encrypt(String content, String key) {
//		try {
//			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
//
//			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
//
//			byte plaintext[] = content.getBytes("UTF-8");
//			byte[] output = cipher.doFinal(plaintext);
//
//			String s = new String(Base64.encode(output));
//
//			return s;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			
			byte [] temp = Base64.decode(privateKey);
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(temp);
			
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
