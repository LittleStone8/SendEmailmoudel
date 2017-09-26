package org.ofbiz.base.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * 封装Base64的工具类
 * 
 */
public class UrlBase64Coder {
	public final static String ENCODING = "UTF-8";

	// 加密
	public static String encoded(String data) throws UnsupportedEncodingException {
		byte[] b = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
		return new String(b, ENCODING);
	}


	// 解密
	public static String decode(String data) throws UnsupportedEncodingException {
		byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));
		return new String(b, ENCODING);
	}


	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "java的base64位加解密操作";
		// 加密该字符串
        System.out.println(Base64.encodeBase64String(str.getBytes())) ;
		String encodedString = UrlBase64Coder.encoded(str);
		System.out.println(encodedString);
		// 解密该字符串
		String decodedString = UrlBase64Coder.decode(encodedString);
		System.out.println(decodedString);
	}
}