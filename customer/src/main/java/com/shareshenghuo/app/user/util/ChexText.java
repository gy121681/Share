package com.shareshenghuo.app.user.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChexText {
	
	/**
	* 判定输入汉字
	* 
	* @param c
	* @return
	*/
	public static boolean isChinese(char c) {
	Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	

	if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	return true;
	}
	if(ub.equals("。")||ub.equals("！")||ub.equals("？")||ub.equals("?")||ub.equals(".")||ub.equals(",")||ub.equals("!")||ub.equals("`")){
		return false;
	}
	return false;
	}
	 
	/**
	* 检测String是否全是中文
	* 
	* @param name
	* @return
	*/
	public static boolean checkNameChese(String name) {
	boolean res = true;
	char[] cTemp = name.toCharArray();
	
    for (int i = 0; i < cTemp.length; i++) {
		if((cTemp[i]+"").equals("。")||(cTemp[i]+"").equals("！")||(cTemp[i]+"").equals("？")||(cTemp[i]+"").equals("?")||(cTemp[i]+"").equals(".")||(cTemp[i]+"").equals(",")||(cTemp[i]+"").equals("!")||(cTemp[i]+"").equals("`")){
			res = true;
			break;
		}
	}
	for (int i = 0; i < name.length(); i++) {
	if (!isChinese(cTemp[i])) {
	res = false;
	break;
	}
	}
	return res;
	}
	
//	public static boolean checkNameChese(String name) {
//		
//	    char[] cTemp = name.toCharArray();
//	    for (int i = 0; i < cTemp.length; i++) {
//			if((cTemp[i]+"").equals("。")||(cTemp[i]+"").equals("！")||(cTemp[i]+"").equals("？")||(cTemp[i]+"").equals("?")||(cTemp[i]+"").equals(".")||(cTemp[i]+"").equals(",")||(cTemp[i]+"").equals("!")||(cTemp[i]+"").equals("`")){
//				return false;
//			}
//		}
//		
//		 Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
//		 Matcher m =p.matcher(name);
//	    if(m.matches()){
//	    	return true;
//	    }else{
//	    	return false;
//	    }
//	}

	public static boolean judgetext(String text) {
		// TODO Auto-generated method stub
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？，。、；‘’,./;'☺�1234567890]"; 
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(text);                 
        if( m.find()){
        	return true;
//            Toast.makeText(CommodityInfoActivity.this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show();
        }
		return m.find();
	}
	
}
