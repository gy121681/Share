package com.shareshenghuo.app.shop.adapter;

import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.lidroid.xutils.BitmapUtils;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.Bimp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter{ 
    private Context context; 
    public List<HashMap<String, Object>> datalist;
    public MyGridAdapter(Context context,List<HashMap<String, Object>> datalist){ 
        this.context = context; 
        this.datalist = datalist;
    } 
    public int getCount() { 
        return datalist.size(); 
    } 

    public Object getItem(int item) { 
        return item; 
    } 

    public long getItemId(int id) { 
        return id; 
    } 
     
    //创建View方法 
    public View getView(int position, View convertView, ViewGroup parent) { 
        ViewHolder viewHolder = null;  
        if (convertView == null)  
        {  
            convertView = LayoutInflater.from(context).inflate(R.layout.data_list_item, parent,  
                    false);  
            viewHolder = new ViewHolder();  
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.texts);  
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);  
            viewHolder.num = (TextView) convertView.findViewById(R.id.nums);  
            convertView.setTag(viewHolder);  
        } else  
        {  
            viewHolder = (ViewHolder) convertView.getTag();  
        }  
        HashMap<String , Object> map = datalist.get(position);
        viewHolder.mTextView.setText(map.get("name").toString());
        viewHolder.num.setText(map.get("num").toString());
//		FinalBitmap.create(context).display(viewHolder.image,
//				Api.HOSTERMA+map.get("img").toString(),
//				viewHolder.image.getWidth(),
//				viewHolder.image.getHeight(), null, null);
//        new BitmapUtils(context).configDefaultLoadingImage(R.drawable.defult_bg);
//        new BitmapUtils(context).display(viewHolder.image,  Api.HOSTERMA+map.get("img").toString());
        ImageLoadManager.getInstance(context.getApplicationContext()).displayImage(map.get("img").toString(),  viewHolder.image);
        return convertView;  
    }  
    
    private final class ViewHolder  
    {  
        TextView mTextView,num;  
        ImageView image;
    }  
} 
