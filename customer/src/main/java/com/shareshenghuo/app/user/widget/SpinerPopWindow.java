package com.shareshenghuo.app.user.widget;

import java.util.List;

import com.shareshenghuo.app.user.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SpinerPopWindow <T> extends PopupWindow {  
    private LayoutInflater inflater;  
    private ListView mListView;  
    private List<T> list;  
    private MyAdapter  mAdapter;  
      
    public SpinerPopWindow(Context context,List<T> list,OnItemClickListener clickListener) {  
        super(context);  
        inflater=LayoutInflater.from(context);  
        this.list=list;  
        init(clickListener);  
    }  
    
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}
      
    private void init(OnItemClickListener clickListener){  
        View view = inflater.inflate(R.layout.spiner_window_layout, null);  
        setContentView(view);         
        setWidth(LayoutParams.WRAP_CONTENT);  
        setHeight(LayoutParams.WRAP_CONTENT);  
        setFocusable(true);  
            ColorDrawable dw = new ColorDrawable(0x00);  
        setBackgroundDrawable(dw);  
        mListView = (ListView) view.findViewById(R.id.msplistviews);  
        TextView tvs = (TextView) view.findViewById(R.id.tvs);
        mListView.setAdapter(mAdapter=new MyAdapter());  
        mListView.setOnItemClickListener(clickListener);  
        tvs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
    }  
      
    private class MyAdapter extends BaseAdapter{  
        @Override  
        public int getCount() {  
            return list.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            return list.get(position);  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder=null;  
            if(convertView==null){  
                holder=new ViewHolder();  
                convertView=inflater.inflate(R.layout.item_textviews, null);  
                holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);  
                convertView.setTag(holder);  
            }else{  
                holder=(ViewHolder) convertView.getTag();  
            }  
            holder.tvName.setText(getItem(position).toString());  
            return convertView;  
        }  
    }  
      
    private class ViewHolder{  
        private TextView tvName;  
    }  
}  