package com.shareshenghuo.app.shop.widget;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.ShopCategoryListActivity;
import com.shareshenghuo.app.shop.adapter.ViewHolder;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.ShopCategoryResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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
public class DragListAdapter extends BaseAdapter {
	private static final String TAG = "DragListAdapter";
	private List<ShopCategoryBean> arrayTitles;
//	private ArrayList<Integer> arrayDrawables;
	private Context context;
	public boolean isHidden;
	private View convertViews = null;
	private TwoButtonDialog downloadDialog;
	private boolean tag = false;
	private MyEditDialog1 doubleWarnDialog1;
	private int endpositiontag = -1;

	public DragListAdapter(Context context, List<ShopCategoryBean> datas) {
		this.context = context;
		this.arrayTitles = datas;
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
		convertView = LayoutInflater.from(context).inflate(R.layout.drag_list_item,null);

		TextView textView = (TextView) convertView.findViewById(R.id.drag_list_item_text);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.drag_list_item_image);
		RelativeLayout re = (RelativeLayout) convertView.findViewById(R.id.re_edit);
		ImageView edit_image = (ImageView) convertView.findViewById(R.id.edit_image);
		ImageView del_image = (ImageView) convertView.findViewById(R.id.del_image);
		TextView tv_num = (TextView) convertView.findViewById(R.id.tv_num);
		
		view = convertView;
		del_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDialog(position,view);
			}
		});
		edit_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showeditdailog(position,arrayTitles.get(position).type_name);
			}
		});
		
		
//		imageView.setImageResource(arrayDrawables.get(position));
		tv_num.setText("共"+arrayTitles.get(position).goods_num+"件商品");
		textView.setText(arrayTitles.get(position).type_name);
		
		if(tag){
			if(!arrayTitles.get(position).id.equals("0")){
				re.setVisibility(View.VISIBLE);
			}else{
				re.setVisibility(View.INVISIBLE);
			}
//			tv_num.setVisibility(View.INVISIBLE);
		
		}else{
//			tv_num.setVisibility(View.VISIBLE);
			re.setVisibility(View.INVISIBLE);
		}
		if (isChanged){
		    if (position == invisilePosition){
		    	if(!ShowItem){
//		    		reall
//		    		convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
//		    		convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
//		    		convertView.findViewById(R.id.tv_num).setVisibility(View.INVISIBLE);
//		    		convertView.findViewById(R.id.re_edit).setVisibility(View.INVISIBLE);
//		    		convertView.findViewById(R.id.check_del).setVisibility(View.INVISIBLE);
			        convertView.setVisibility(View.INVISIBLE);
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
	private int endposition = -1;
	private boolean isChanged = true;
	private boolean ShowItem = false;
	
	public void exchange(int startPosition, int endPosition) {
		System.out.println(startPosition + "--" + endPosition);
//		holdPosition = endPosition;
		Object startObject = getItem(startPosition);
//		System.out.println(startPosition + "========" + endPosition);
//		Log.d("ON","startPostion ==== " + startPosition );
//		Log.d("ON","endPosition ==== " + endPosition );
		if(startPosition < endPosition){
			arrayTitles.add(endPosition + 1, (ShopCategoryBean) startObject);
			arrayTitles.remove(startPosition);
		}else{
			arrayTitles.add(endPosition,(ShopCategoryBean)startObject);
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
			mCopyList.add(endPosition + 1, (ShopCategoryBean) startObject);
			mCopyList.remove(startPosition);
		}else{
			mCopyList.add(endPosition,(ShopCategoryBean)startObject);
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
		Log.i(TAG,"start" + start);
		String title = arrayTitles.get(start).type_name;
		arrayTitles.remove(start);// 删除该项
		arrayTitles.add(start, (ShopCategoryBean)obj);// 添加删除项
	}
	
	private ArrayList<ShopCategoryBean> mCopyList = new ArrayList<ShopCategoryBean>();
	
	public void copyList(){
		mCopyList.clear();
		for (ShopCategoryBean str : arrayTitles) {
			mCopyList.add(str);
		}
	}
	
	public void pastList(){
		StringBuffer buff = new StringBuffer();

		arrayTitles.clear();

		for (ShopCategoryBean str : mCopyList) {
			arrayTitles.add(str);

		}
		for (int i = 0; i < arrayTitles.size(); i++) {
			arrayTitles.get(i).tag = i+"";
			buff.append(arrayTitles.get(i).id+","+arrayTitles.get(i).tag+";");
			
		}
		if(endpositiontag==-1||endpositiontag==endposition){
		}else{
			setGoodsTypeSort(buff.toString());
		}
		

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
	
	private void initDialog(final int position,final View view) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(context, R.style.CustomDialog,
				"确定删除该分类?", "删除后该分类下的商品不会被删除", "否", "是",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							deletegoodstype(position,view);
							
//							notifyDataSetChanged();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
	}
	
	public void deletegoodstype(final int position,final View view) {
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.id = arrayTitles.get(position).id;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.DELETEGOODSTYPE, params, new RequestCallBack<String>() {
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
//							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
							deleteCell(view, position);
							((ShopCategoryListActivity) context).initd(false);
							((ShopCategoryListActivity) context).pageNo = 1;
							
						}else{
							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}
						
					} else {
						T.showShort(context.getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	
	
	private void setGoodsTypeSort(final String name) {
		// TODO Auto-generated method stub
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.sorts = name;
		Log.e("", " = ==   "+name);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SETGOODSTYPESORT, params, new RequestCallBack<String>() {
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
//							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}else{
							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}
					
					} else {
						T.showShort(context.getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	private void setshoptypename(final String name,final int position) {
		// TODO Auto-generated method stub
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.shopId = UserInfoManager.getUserInfo(context).shop_id+"";
		req.id = arrayTitles.get(position).id;
		req.name = name;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATEGOODSTYPE, params, new RequestCallBack<String>() {
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
//							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
				        	arrayTitles.get(position).type_name = name;
				        	 notifyDataSetChanged();  
						}else{
							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
						}
					
					} else {
						T.showShort(context.getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	private void deleteCell(final View v, final int index) {  
	    AnimationListener al = new AnimationListener() {  
	        @Override  
	        public void onAnimationEnd(Animation arg0) {  
	        	arrayTitles.remove(index);  
	  
	            notifyDataSetChanged();  
	        }  
	        @Override public void onAnimationRepeat(Animation animation) {}  
	        @Override public void onAnimationStart(Animation animation) {}  
	    };  
	  
	    collapse(v, al);  
	}  
	  
	private void collapse(final View v, AnimationListener al) {  
	    final int initialHeight = v.getMeasuredHeight();  
	  
	    Animation anim = new Animation() {  
	        @Override  
	        protected void applyTransformation(float interpolatedTime, Transformation t) {  
	            if (interpolatedTime == 1) {  
	                v.setVisibility(View.GONE);  
	            }  
	            else {  
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
	                v.requestLayout();  
	            }  
	        }  
	  
	        @Override  
	        public boolean willChangeBounds() {  
	            return true;  
	        }  
	    };  
	  
	    if (al!=null) {  
	        anim.setAnimationListener(al);  
	    }  
	    anim.setDuration(300);  
	    v.startAnimation(anim);  
	}  
	
	
	public void showeditdailog(final int position,String typename){
		doubleWarnDialog1 = new MyEditDialog1(context,
				R.style.CustomDialog, "编辑分类名称", "", "确认", "取消", typename,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(context,"不能为空",
										Toast.LENGTH_SHORT).show();
								return;
							}
							break;
						default:
							break;
						}
					}
				},
		
				new onMyaddTextListener() {
					
					@Override
					public void refreshActivity(String paypwd) {
						if (paypwd == null || paypwd.equals("")) {
							Toast.makeText(context.getApplicationContext(),"不能为空",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"请输入支付密码！");
							return;
						}
						InputMethodManager m=(InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						
						if(judgetext(paypwd)){
							T.showShort(context.getApplicationContext(), "非法字符");
							return;
						}
						setshoptypename(paypwd,position);
						doubleWarnDialog1.dismiss();
//						Toast.makeText(context.getApplicationContext(), paypwd, Toast.LENGTH_SHORT).show();
					}
				});

		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
		
	}
	
	private boolean judgetext(String text) {
		// TODO Auto-generated method stub
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？，。、；‘’,./;']"; 
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(text);                 
        if( m.find()){
        	return true;
//            Toast.makeText(CommodityInfoActivity.this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show();
        }
		return m.find();
	}
}
