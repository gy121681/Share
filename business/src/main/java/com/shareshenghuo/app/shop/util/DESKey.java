package com.shareshenghuo.app.shop.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;

public class 

DESKey{
//	public static class desencryptiontest {
		
	    private  byte[] desKey;
	    private static String key = "12345678";
//	    public static void main(String[] args) {
//	        String text = "测试asdY^&*NN!__s some plaintext!";
//	        System.out.println("加密前的明文:" + text);
//
//	        String cryperText = "";
//	        try {
//	            cryperText = toHexString(encrypt(text));
//	            System.out.println("加密前的明文:" + cryperText);
//	        } catch (Exception e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
//
//	        try {
//	            System.out.println("解密后的明文:" + decrypt(cryperText));
//	        } catch (Exception e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
//	        try {
//	            System.in.read();
//	        } catch (IOException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
//	    }



	    public String decrypt(String message) throws Exception {

	        byte[] bytesrc = convertHexString(message);
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

	        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

	        byte[] retByte = cipher.doFinal(bytesrc);
	        return new String(retByte);
	    }

	    public static byte[] encrypt(String message) throws Exception {
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

	        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

	        return cipher.doFinal(message.getBytes("UTF-8"));
	    }

	    public byte[] convertHexString(String ss) {
	        byte digest[] = new byte[ss.length() / 2];
	        for (int i = 0; i < digest.length; i++) {
	            String byteString = ss.substring(2 * i, 2 * i + 2);
	            int byteValue = Integer.parseInt(byteString, 16);
	            digest[i] = (byte) byteValue;
	        }

	        return digest;
	    }

	    public  String toHexString(byte b[]) {
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < b.length; i++) {
	            String plainText = Integer.toHexString(0xff & b[i]);
	            if (plainText.length() < 2)
	                plainText = "0" + plainText;
	            hexString.append(plainText);
	        }

	        return hexString.toString();
	    }
	    
	    
	    
	    public  String encrypts(String input, String key){  
	        byte[] crypted = null;  
	        try{  
	            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");  
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
	            cipher.init(Cipher.ENCRYPT_MODE, skey);  
	            crypted = cipher.doFinal(input.getBytes());  
	        }catch(Exception e){  
	        System.out.println(e.toString());  
	    }  
	    return new String(Base64Encoder.encode(crypted));  
	} 
	    
		public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };


		@SuppressLint("NewApi") 
		public static String AES_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException {
			
			byte[] textBytes = str.getBytes("UTF-8");
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			     SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			     Cipher cipher = null;
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
			
			return Base64.encodeToString(cipher.doFinal(textBytes), 0);
		}

		@SuppressLint("NewApi") 
		public static String AES_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			
			byte[] textBytes =Base64.decode(str,0);
			//byte[] textBytes = str.getBytes("UTF-8");
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			return new String(cipher.doFinal(textBytes), "UTF-8");
		}

	}
//}

