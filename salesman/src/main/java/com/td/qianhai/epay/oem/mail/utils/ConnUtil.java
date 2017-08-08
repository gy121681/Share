package com.td.qianhai.epay.oem.mail.utils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class ConnUtil {

	private static Store store = null;
	 private static long lastClickTime;
	
	public static String format(String s){ 
		  String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""); 
		  return str; 
		 }

	public static Store login(String host, String user, String password) {
		// 连接服务器
		Session session = Session.getDefaultInstance(System.getProperties(),
				null);
		try {
			store = session.getStore("pop3");
			store.connect(host, user, password);
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
		return store;
	}

	public static String getPOP3Host(String user) {
		if (user.contains("163")) {
			return "pop.163.com";
		} else {
			return null;
		}
	}

	public static String getSMTPHost(String user) {
		if (user.contains("163")) {
			return "smtp.163.com";
		} else {
			return null;
		}
	}
	
	
   
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();   
        if ( time - lastClickTime < 500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}
