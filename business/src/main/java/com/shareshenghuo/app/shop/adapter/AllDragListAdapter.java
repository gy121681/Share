package com.shareshenghuo.app.shop.adapter;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.ViewHolder;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/***
 * 自定义适配器
 * 
 * @author zhangjia
 * 
 */
public class AllDragListAdapter extends BaseAdapter {
//	private static final String TAG = "DragListAdapter";
	private List<CommodityManageBean>  arrayTitles;
//	private ArrayList<Integer> arrayDrawables;
	private Context context;
	public boolean isHidden;
	private View convertViews = null;
	private TwoButtonDialog downloadDialog;
	private boolean tag = false;
	private MyEditDialog1 doubleWarnDialog1;
	private int endposition = -1;
	private int endpositiontag = -1;
	private String id  = "";

	public AllDragListAdapter(Context context, List<CommodityManageBean> data, String id) {
		this.context = context;
		this.arrayTitles = data;
		this.id = id;
//		this.arrayDrawables = arrayDrawables;
	}

	public void showDropItem(boolean showItem){
		this.ShowItem = showItem;		
	}
	
	public void setInvisiblePosition(int position){
		invisilePosition = position;
	}
	
	public void showEdit(boolean tag){
		this.tag = tag;
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position,  View convertView, ViewGroup parent) {
		/***
		 * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
		 * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
		 */
		 final View view; 
		convertView = LayoutInflater.from(context).inflate(R.layout.commodity_manage_item,null);
		TextView shop_imfo = (TextView) convertView.findViewById(R.id.shop_imfo);
		TextView shop_balance = (TextView) convertView.findViewById(R.id.shop_balance);
		TextView shop_name = (TextView) convertView.findViewById(R.id.shop_name);
		ImageView img_arr = (ImageView) convertView.findViewById(R.id.img_arr);
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		ImageView shop_img = (ImageView) convertView.findViewById(R.id.shop_img);
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.drag_list_item_image);
//		RelativeLayout re = (RelativeLayout) convertView.findViewById(R.id.re_edit);
		view = convertView;
		
		
//		imageView.setImageResource(arrayDrawables.get(position));
//		textView.setText(arrayTitles.get(position));
//		holder.setText(R.id.shop_name, item.name);
//		holder.setText(R.id.shop_imfo, item.description);
//		holder.setText(R.id.shop_balance, "价格: "+item.price);
//		holder.setImageByURL(R.id.shop_img, item.photo);
		
		CommodityManageBean item = arrayTitles.get(position);
		DecimalFormat df = new DecimalFormat("###.00"); 
		String price = "";
		try {
			price = df.format(Double.parseDouble(item.goods_price));
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		
		if(price.substring(0, 1).equals(".")){
			price = "0"+price;
		}
		if(!id.equals("0")){
			img_arr.setVisibility(View.GONE);
		}
		shop_imfo.setText(item.goods_description);
		shop_name.setText(item.goods_name);
		shop_balance.setText( "价格: "+price+"元");
		tv_time.setText(item.goods_create_time);
		ImageLoadManager.getInstance(context).displayImage(Api.HOSTERMA+item.goods_photo, shop_img);
		
		if(tag){
//			img_arr.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}else{
//			img_arr.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.GONE);
		}
		if (isChanged){
		    if (position == invisilePosition){
		    	if(!ShowItem){
//		    		convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
		    		convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
//		    		convertView.findViewById(R.id.check_del).setVisibility(View.INVISIBLE);
//			        convertView.setVisibility(View.INVISIBLE);
		    	}
		    }
		    if(lastFlag != -1){
		    	if(lastFlag == 1){
				    if(position > invisilePosition){
				    	Animation animation;
				    	animation = getFromSelfAnimation(0, -height);
				    	convertView.startAnimation(animation);
				    }
		    	}else if(lastFlag == 0){
		    		if(position < invisilePosition){
				    	Animation animation;
				    	animation = getFromSelfAnimation(0, height);
				    	convertView.startAnimation(animation);
				    }
		    	}
		    }
		    
//		    if(lastFlag != -1){
//		    	if(lastFlag == 1){
//		    		if(position < invisilePosition){
//		    			if(position == invisilePosition - 1){
//		    				convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
//				    		convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
//				    		convertView.findViewById(R.id.check_del).setVisibility(View.INVISIBLE);
//		    			}
////					    Animation animation;
////					    if(isSameDragDirection){
////					    	animation = getToSelfAnimation(0, height);
////					    }else{
////					    	animation = getFromSelfAnimation(0, -height);
////					    }
////					    convertView.startAnimation(animation);
//		    		}
//		    	}else{
//		    		
//		    	}
//		    }
		}
		return convertView;
	}

	/***
	 * 动态修改ListVIiw的方位.
	 * 
	 * @param start
	 *            点击移动的position
	 * @param down
	 *            松开时候的position
	 */
	private int invisilePosition = -1;
	private boolean isChanged = true;
	private boolean ShowItem = false;
	
	public void exchange(int startPosition, int endPosition) {
//		System.out.println(startPosition + "--" + endPosition);
//		holdPosition = endPosition;
		Object startObject = getItem(startPosition);
//		System.out.println(startPosition + "========" + endPosition);
//		Log.d("ON","startPostion ==== " + startPosition );
//		Log.d("ON","endPosition ==== " + endPosition );
		if(startPosition < endPosition){
			arrayTitles.add(endPosition + 1, (CommodityManageBean) startObject);
			arrayTitles.remove(startPosition);
		}else{
			arrayTitles.add(endPosition,(CommodityManageBean)startObject);
			arrayTitles.remove(startPosition + 1);
		}
		isChanged = true;
//		notifyDataSetChanged();
	}
	
	public void exchangeCopy(int startPosition, int endPosition) {
//		System.out.println(startPosition + "--" + endPosition);
//		holdPosition = endPosition;
		Object startObject = getCopyItem(startPosition);
//		System.out.println(startPosition + "========" + endPosition);
//		Log.d("ON","startPostion ==== " + startPosition );
//		Log.d("ON","endPosition ==== " + endPosition );
		if(startPosition < endPosition){
			mCopyList.add(endPosition + 1, (CommodityManageBean) startObject);
			mCopyList.remove(startPosition);
		}else{
			mCopyList.add(endPosition,(CommodityManageBean)startObject);
			mCopyList.remove(startPosition + 1);
		}
		isChanged = true;
//		notifyDataSetChanged();
		endpositiontag = endposition;
		endposition = endPosition;
	}
	
	
	public Object getCopyItem(int position) {
		return mCopyList.get(position);
	}
	
	@Override
	public int getCount() {
		return arrayTitles.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayTitles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addDragItem(int start, Object obj){
//		Log.i(TAG,"start" + start);
		String title = arrayTitles.get(start).name;
		arrayTitles.remove(start);// 删除该项
		arrayTitles.add(start, (CommodityManageBean)obj);// 添加删除项
	}
	
	private ArrayList<CommodityManageBean> mCopyList = new ArrayList<CommodityManageBean>();
	
	public void copyList(){
		mCopyList.clear();
		for (CommodityManageBean str : arrayTitles) {
			mCopyList.add(str);
		}
	}
	
	public void pastList(){
		StringBuffer buff = new StringBuffer();
		arrayTitles.clear();
		for (CommodityManageBean str : mCopyList) {
			arrayTitles.add(str);
		}
		
		for (int i = 0; i < arrayTitles.size(); i++) {
			arrayTitles.get(i).tag = i+"";
			buff.append(arrayTitles.get(i).goods_id+","+arrayTitles.get(i).tag+";");
			
		}
		if(endpositiontag==-1||endpositiontag==endposition){
		}else{
			setGoodsTypeSort(buff.toString());
		}
//		setGoodsTypeSort(buff.toString());
	}
	
	private boolean isSameDragDirection = true;
	private int lastFlag = -1;
	private int height;
	private int dragPosition = -1;
	
	public void setIsSameDragDirection(boolean value){
		isSameDragDirection = value;
	}
	
	public void setLastFlag(int flag){
		lastFlag = flag;
	}
	
	public void setHeight(int value){
		height = value;
	}
	
	public void setCurrentDragPosition(int position){
		dragPosition = position;
	}
	
	public Animation getFromSelfAnimation(int x,int y){
		TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x, 
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
		go.setInterpolator(new AccelerateDecelerateInterpolator());
		go.setFillAfter(true);
		go.setDuration(100);	
		go.setInterpolator(new AccelerateInterpolator());
		return go;
	}
	
	public Animation getToSelfAnimation(int x,int y){
		TranslateAnimation go = new TranslateAnimation(
				 Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0, 
				 Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
		go.setInterpolator(new AccelerateDecelerateInterpolator());
		go.setFillAfter(true);
		go.setDuration(100);	
		go.setInterpolator(new AccelerateInterpolator());
		return go;
	}
	
	
//	private void deleteCell(final View v, final int index) {  
//	    AnimationListener al = new AnimationListener() {  
//	        @Override  
//	        public void onAnimationEnd(Animation arg0) {  
//	        	arrayTitles.remove(index);  
//	  
//	            notifyDataSetChanged();  
//	        }  
//	        @Override public void onAnimationRepeat(Animation animation) {}  
//	        @Override public void onAnimationStart(Animation animation) {}  
//	    };  
//	  
//	    collapse(v, al);  
//	}  
//	  
//	private void collapse(final View v, AnimationListener al) {  
//	    final int initialHeight = v.getMeasuredHeight();  
//	  
//	    Animation anim = new Animation() {  
//	        @Override  
//	        protected void applyTransformation(float interpolatedTime, Transformation t) {  
//	            if (interpolatedTime == 1) {  
//	                v.setVisibility(View.GONE);  
//	            }  
//	            else {  
//	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
//	                v.requestLayout();  
//	            }  
//	        }  
//	  
//	        @Override  
//	        public boolean willChangeBounds() {  
//	            return true;  
//	        }  
//	    };  
//	  
//	    if (al!=null) {  
//	        anim.setAnimationListener(al);  
//	    }  
//	    anim.setDuration(300);  
//	    v.startAnimation(anim);  
//	}  
	
	private void setGoodsTypeSort(final String name) {
		// TODO Auto-generated method stub
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.sorts = name;
		Log.e("", ""+name);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SETGOODSSORT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(context.getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
							endpositiontag = -1;
							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}else{
							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}
					
					} else {
						T.showShort(context.getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
}
