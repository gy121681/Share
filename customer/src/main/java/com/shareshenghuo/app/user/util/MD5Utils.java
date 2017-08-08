package com.shareshenghuo.app.user.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  

protected static MessageDigest messagedigest = null;  

	static {  
	    try {  
	        messagedigest = MessageDigest.getInstance("MD5");  
	    } catch (NoSuchAlgorithmException nsaex) {  
	        System.err.println(MD5Utils.class.getName()  
	                + "初始化失败，MessageDigest不支持MD5Util。");  
	        nsaex.printStackTrace();  
	    }  
	}  

	/** 
	 * 功能：得到一个字符串的MD5值。 
	 *  
	 * @date 2014年06月24日 
	 * @param str 
	 *            字符串 
	 * @return String 
	 */  
	public static String getMD5String(String str) {  
	    return getMD5String(str.getBytes());  
	}  


	private static String getMD5String(byte[] bytes) {  
	    messagedigest.update(bytes);  
	    return bufferToHex(messagedigest.digest());  
	}  
	
	
	private static String bufferToHex(byte bytes[]) {  
	    return bufferToHex(bytes, 0, bytes.length);  
	}  
	
	private static String bufferToHex(byte bytes[], int m, int n) {  
	    StringBuffer stringbuffer = new StringBuffer(2 * n);  
	    int k = m + n;  
	    for (int l = m; l < k; l++) {  
	        appendHexPair(bytes[l], stringbuffer);  
	    }  
	    return stringbuffer.toString();  
	}  
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];  
        char c1 = hexDigits[bt & 0xf];  
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    } 
	public static void main(String[] args) {
	String a=	MD5Utils.getMD5String("huangj");
	System.out.println(a);
	}
}
