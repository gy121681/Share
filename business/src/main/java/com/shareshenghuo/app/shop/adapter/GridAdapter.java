package com.shareshenghuo.app.shop.adapter;

import net.tsz.afinal.FinalBitmap;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.network.bean.PhotoBean;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.photo.ImageItem;
import com.shareshenghuo.app.shop.photo.PublicWay;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class GridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private boolean showurl = false;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public GridAdapter(Context context) {
//		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void update() {
//		if(Bimp.tempSelectBitmap.size()>=PublicWay.num){
//			Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size()-1);
//		}

		notifyDataSetChanged();
//		loading();
	}

	public int getCount() {
//		if(Bimp.tempSelectBitmap.size() == PublicWay.num){
//			return PublicWay.num;
//		}
		return (Bimp.tempSelectBitmap.size());
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida,
					null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		if(Bimp.tempSelectBitmap.size()<PublicWay.num-1){
//			Bimp.tempSelectBitmap.add(new ImageItem());
//		}
		
		Log.e("", " = =    "+Bimp.tempSelectBitmap.size());
//		holder.image.setImageResource(R.drawable.defult_bg);
		if (Bimp.tempSelectBitmap.get(position).getBitmap()==null&&Bimp.tempSelectBitmap.get(position).url==null) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.share_b_public_add_to_photo_small));
			}
//		if (position !=Bimp.tempSelectBitmap.size()) {
			if(Bimp.tempSelectBitmap.get(position).getBitmap()!=null){
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

					if(Bimp.tempSelectBitmap.get(position).url!=null){
//						FinalBitmap.create(context).display(holder.image,
//						Api.HOSTERMA+Bimp.tempSelectBitmap.get(position).url,
//						holder.image.getWidth(),
//						holder.image.getHeight(), null, null);
						ImageLoadManager.getInstance(context).displayImage(Bimp.tempSelectBitmap.get(position).url, holder.image);
//						holder.image.setImageByURL(R.id.ivPhoto, item.url);
					}


//		} 
//		else {
//			holder.image.setImageBitmap(BitmapFactory.decodeResource(
//					context.getResources(), R.drawable.icon_addpic_unfocused));
			if (Bimp.tempSelectBitmap.get(position).getBitmap()==null&&Bimp.tempSelectBitmap.get(position).url==null&&position == PublicWay.num-1) {
				holder.image.setVisibility(View.GONE);
//				notifyDataSetChanged();
			}else{
				holder.image.setVisibility(View.VISIBLE);
			}
//		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				 if(Bimp.tempSelectBitmap.size()<PublicWay.num){
//					 Bimp.tempSelectBitmap.add(new ImageItem());
//				 }
				notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}
}
