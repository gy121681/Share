package com.shareshenghuo.app.shop.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;


public class NetCommunicate {
	
	public static String[] DOWNMAINKEY= {"TMKKEY","TMKKEYCHECKVALUE","RSPCOD","RSPMSG"};
	public static String[] DOWNWORKKEY= {"PINKEY","PINCHKVALUE","MACKEY","MAKCHKVALUE","ENCRYPTKEY","ENCCHKVALUE","RSPCOD","RSPMSG"};
	public static String[] HANDPAY= {"LOGNO","TXNDAT","TXNTIM","CSEQNO","ISSNAM","SREFNO","RSPCOD","RSPMSG"};
	
	public static HashMap<String,Object> parseredXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				map = new HashMap<String, Object>();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.put("RSPCOD", parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.put("RSPMSG", parser.nextText().toString());
				}else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
						         map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
}
