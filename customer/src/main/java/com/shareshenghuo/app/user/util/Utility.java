package com.shareshenghuo.app.user.util;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
    public static void setListViewHeightBasedOnChildren(ListView listView) {
         ListAdapter listAdapter = listView.getAdapter();
         if (listAdapter == null) {
              return;
         }
               
         int totalHeight = 0;
         for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
              View listItem = listAdapter.getView(i, null, listView);
              listItem.measure(0, 0);  
              totalHeight += listItem.getMeasuredHeight();  
         }
               
         ViewGroup.LayoutParams params = listView.getLayoutParams();
         params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
         params.height += BitmapTool.dp2px(listView.getContext(), 10);
         listView.setLayoutParams(params);
   }
    
    
    public static void hideSoftInput(Activity context) {
		if(context.getCurrentFocus() != null) {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
    
    public static String getPath(Activity context, Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
	}
    
    public static String replaceString(String str, String c, int begin, int end) {
    	if(TextUtils.isEmpty(str))
    		return null;
    	
    	if(begin>end || begin<0 || end>str.length())
    		return str;
    	
    	StringBuilder sb = new StringBuilder(str.substring(0, begin));
    	int n = end - begin+1;
    	for(int i=0; i<n; i++) {
    		sb.append(c);
    	}
    	sb.append(str.substring(end+1, str.length()));
    	return sb.toString();
    }
    
    /**
     * 按“，”拆分字符串，返回第一个字符串
     */
    public static String getFirstString(String str) {
    	if(TextUtils.isEmpty(str))
    		return null;
    	
    	String[] array = str.split(",");
    	return array[0];
    }
    
    public static void copy(Context context, String str) {
    	ClipboardManager board = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        board.setText(str);
    }
    
    public static void call(Context context, String tel) {
    	context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel)));
    }
    
    public static void openQQChat(Context context, String qq) {
    	String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
    	context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
    
    
	 public static Map<String, String> URLRequest(String URL)
	    {
	    Map<String, String> mapRequest = new HashMap<String, String>();
	   
	      String[] arrSplit=null;
	     
	    String strUrlParam=TruncateUrlPage(URL);
	    if(strUrlParam==null)
	    {
	        return mapRequest;
	    }
	      //每个键值为一组 www.2cto.com
	    arrSplit=strUrlParam.split("[&]");
	    for(String strSplit:arrSplit)
	    {
	          String[] arrSplitEqual=null;         
	          arrSplitEqual= strSplit.split("[=]");
	         
	          //解析出键值
	          if(arrSplitEqual.length>1)
	          {
	              //正确解析
	              mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
	             
	          }
	          else
	          {
	              if(arrSplitEqual[0]!="")
	              {
	              //只有参数没有值，不加入
	              mapRequest.put(arrSplitEqual[0], "");       
	              }
	          }
	    }   
	    return mapRequest;   
	    }
	    /**
	     * 去掉url中的路径，留下请求参数部分
	     * @param strURL url地址
	     * @return url请求参数部分
	     */
	    private static  String TruncateUrlPage(String strURL)
	    {
	    String strAllParam=null;
	      String[] arrSplit=null;
	     
	      strURL=strURL.trim().toLowerCase();
	     
	      arrSplit=strURL.split("[?]");
	      if(strURL.length()>1)
	      {
	          if(arrSplit.length>1)
	          {
	                  if(arrSplit[1]!=null)
	                  {
	                  strAllParam=arrSplit[1];
	                  }
	          }
	      }
	     
	    return strAllParam;   
	    }

}
