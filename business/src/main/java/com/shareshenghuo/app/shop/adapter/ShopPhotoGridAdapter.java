package com.shareshenghuo.app.shop.adapter;

import net.tsz.afinal.FinalBitmap;

import com.lidroid.xutils.BitmapUtils;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.ShopCategoryListActivity;
import com.shareshenghuo.app.shop.ShopPhotoAcitivty;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.network.bean.PhotoBean;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.photo.GalleryActivity;
import com.shareshenghuo.app.shop.photo.ImageItem;
import com.shareshenghuo.app.shop.photo.PublicWay;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ShopPhotoGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private boolean showurl = false;
	private TwoButtonDialog downloadDialog;
	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public ShopPhotoGridAdapter(Context context) {
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shopphotot_grida,
					null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
			holder.img_del = (ImageView) convertView
					.findViewById(R.id.img_del);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.img_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDialog("确定删除?","否","是",position);
			}
		});
//		if(Bimp.tempSelectBitmap.size()<PublicWay.num-1){
//			Bimp.tempSelectBitmap.add(new ImageItem());
//		}
		
//		holder.image.setImageResource(R.drawable.defult_bg);
		if (Bimp.tempSelectBitmap.get(position).imagePath==null&&Bimp.tempSelectBitmap.get(position).url==null) {
			holder.image.setImageResource(R.drawable.share_b_public_add_to_photo_small);
			holder.img_del.setVisibility(View.GONE);
			}else{
			holder.img_del.setVisibility(View.VISIBLE);
			}
		
//		if (position !=Bimp.tempSelectBitmap.size()) {
			if(Bimp.tempSelectBitmap.get(position).getBitmap()!=null){
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

					if(Bimp.tempSelectBitmap.get(position).url!=null){
						try {
							
//							  new BitmapUtils(context).display(holder.image,  Api.HOSTERMA+Bimp.tempSelectBitmap.get(position).url);
							ImageLoadManager.getInstance(context.getApplicationContext()).displayImage(Bimp.tempSelectBitmap.get(position).url, holder.image);
						} catch (Exception e) {
							// TODO: handle exception
						}
					
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
		public ImageView image,img_del;
	}
	
	
	private void initDialog(String content,String left,String right, final int position) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(context, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							((ShopPhotoAcitivty) context).istag = true;
							Bimp.tempSelectBitmap.remove(position);
							Bimp.max--;
							notifyDataSetChanged();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
			
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
