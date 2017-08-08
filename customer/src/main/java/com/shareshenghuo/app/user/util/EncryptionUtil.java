package com.shareshenghuo.app.user.util;

import com.shareshenghuo.app.user.util.DESKey;

import java.net.URLEncoder;
import java.util.List;


public class EncryptionUtil {
	
	public static String getEncryptionstring(List<String> Obj){
		
		StringBuffer objs = new StringBuffer();
		for (int i = 0; i < Obj.size(); i++) {
			if(i==Obj.size()-1){
				objs.append(Obj.get(i));
			}else{
				objs.append(Obj.get(i)+"&");
			}
			
		}
		String aa = null;
		try {
			aa = DESKey.AES_Encode(objs.toString(),
					"f15f1ede25a2471998ee06edba7d2e29");
			aa = URLEncoder.encode(aa);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return aa;
	} 
}
