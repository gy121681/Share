package com.td.qianhai.epay.oem.mail.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NumUtil {
	
	 public static String getfotmatnum(String str ,boolean b, int i){
	    	
			DecimalFormat df = new DecimalFormat("###,###.00"); 
			String price = "";
			try {
				if(Double.parseDouble(str)/100>1){
					if( i==1){
						price = df.format(Double.parseDouble(str)/100);
					}else{
						price = df.format(Double.parseDouble(str));
					}
				}else{
					price = Double.parseDouble(str)/100+"";
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				price = "0.00";
			}
	    	if(b){
	    		return price;
	    	}
			return price+"元";
	    }
	 
	 public static String getTime(long time,int code) {
			SimpleDateFormat format = null;
			switch (code) {
			case 0:
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				break;
			case 1:
				format = new SimpleDateFormat("MM-dd HH:mm");
			case 2:
				format = new SimpleDateFormat("yyyy-MM-dd");
				break;
			default:
				break;
			}
			return format.format(new Date(time));
		}
	 
	 
		public static String getStrTime(String str) {
			
			
			if(str.length()==8){
				str = str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(str.length()-2);
			}
			   return str;
		}
		
		public static String getStrTime1(String str) {
			
			
			if(str.length()==8){
				str = str.substring(0,4)+"."+str.substring(4,6)+"."+str.substring(str.length()-2);
			}
			   return str;
		}
}
