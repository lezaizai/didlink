package com.didlink.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class ManagerUtils {

	public static String randomCode() {
		char[] alphNum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
				.toCharArray();

		Random rnd = new Random();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append(alphNum[rnd.nextInt(alphNum.length)]);
		}

		return sb.toString();

	}

	public static String encodePassord(String sPasswd) {
		byte[] encodedBytes = Base64.encodeBase64(sPasswd.getBytes());
		String sEncoded = randomCode() + new String(encodedBytes);

		return sEncoded;
	}

	/*public static String decodePassword(String sPasswd) throws Exception {
		String sRealPassword = sPasswd.substring(10);
		byte[] decoded = Base64.decodeBase64(sRealPassword);

		return new String(decoded, "UTF-8");
	}*/

	public static void main(String[] args) throws Exception {

		System.out.println("\n\nEnter your Password to be Encrypted::");
		String sPasswd = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		sPasswd = br.readLine();

		String sEncodedPasswd = encodePassord(sPasswd);
		System.out.println(sEncodedPasswd);
	}
}
