package com.td.qianhai.epay.oem.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {

	private String TAG = "MyTextWatcher";
	private EditText numberEditText;
	int beforeLen = 0;  
    int afterLen = 0;
	
    
    
	public MyTextWatcher(EditText numberEditText) {
		super();
		this.numberEditText = numberEditText;
		
	}

	 public String removeAllSpace(String str) {  
	       String tmpstr=str.replace(" ","");  
	       return tmpstr;  
	   } 
	
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		 String txt = numberEditText.getText().toString();  
         afterLen = txt.length();  
         if (afterLen > beforeLen) {  
             if (txt.length() == 5 || txt.length() == 10|| txt.length() == 15|| txt.length() == 20||txt.length() == 25) {  
                 numberEditText.setText(new StringBuffer(txt).insert(  
                         txt.length() - 1, " ").toString());  
                 numberEditText.setSelection(numberEditText.getText()  
                         .length());  
             }  
         } else {  
             if (txt.startsWith(" ")) {  
                 numberEditText.setText(new StringBuffer(txt).delete(  
                         afterLen - 1, afterLen).toString());  
                 numberEditText.setSelection(numberEditText.getText()  
                         .length());  
             }  
         }  
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		beforeLen = arg0.length();  
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

}
