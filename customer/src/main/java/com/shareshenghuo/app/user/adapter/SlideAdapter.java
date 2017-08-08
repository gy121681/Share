package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.MyOilBean;
import com.shareshenghuo.app.user.network.request.MyOilRequest;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

public class SlideAdapter extends SlideBaseAdapter {
	private  List<MyOilBean>   mData;
	private Context mcontext;
	private TwoButtonDialog downloadDialog;
	
	public SlideAdapter(Context context, List<MyOilBean> data) {
		super(context);
		this.mcontext = context;
		mData = data;
	}

	
	@Override
	public SlideMode getSlideModeInPosition(int position) {
//		if (position == 1) {
//			return SlideMode.LEFT;
//		}
//		if (position == 2) {
//			return SlideMode.NONE;
//		}
		return super.getSlideModeInPosition(position);
	}

	@Override
	public int getFrontViewId(int position) {
		return R.layout.row_front_view;
	}

	@Override
	public int getLeftBackViewId(int position) {
//		if (position % 2 == 0) {
//			return R.layout.row_left_back_view;
//		}
		return R.layout.row_left_back_view;
	}

	@Override
	public int getRightBackViewId(int position) {
		return R.layout.row_right_back_view;
	}
//
//	@Override
//	public int getItemViewType(int position) {
//		if (position % 2 == 0) {
//			return 0;
//		}
//		return 1;
//	}

//	@Override
//	public int getViewTypeCount() {
//		return 1;
//	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		 final View view; 
		
		if (convertView == null) {
			convertView =createConvertView(position);
//			convertView =LayoutInflater.from(mcontext).inflate(
//					R.layout.row_right_back_view, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.edit = (Button) convertView.findViewById(R.id.edit);
			holder.card = (TextView) convertView.findViewById(R.id.card);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			
			holder.delete = (Button) convertView.findViewById(R.id.delete);
			holder.detail = (Button) convertView.findViewById(R.id.detail);
			holder.lin_content = (LinearLayout) convertView.findViewById(R.id.lin_content);
			holder.img_logo = (ImageView) convertView.findViewById(R.id.img_logo);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		view = convertView;
		final MyOilBean text = mData.get(position);
		holder.img_logo.setVisibility(View.GONE);
		if(text.channel_name!=null&&text.channel_name.equals("中国石油")){
			holder.img_logo.setVisibility(View.VISIBLE);
			holder.lin_content.setBackgroundResource(R.drawable.card_bg_org);
			holder.img_logo.setImageResource(R.drawable.sy_logo_sm);
		}else if(text.channel_name!=null&&text.channel_name.equals("中国石化")){
			holder.img_logo.setVisibility(View.VISIBLE);
			holder.lin_content.setBackgroundResource(R.drawable.card_bg_blue);
			holder.img_logo.setImageResource(R.drawable.sh_logo_sm);
		}else if(text.channel_name!=null&&text.channel_name.equals("中海油")){
			holder.img_logo.setVisibility(View.VISIBLE);
			holder.lin_content.setBackgroundResource(R.drawable.card_bg_gray);
			holder.img_logo.setImageResource(R.drawable.zhonghaiyou_big);
		}else{
			holder.img_logo.setVisibility(View.GONE);
		}
		holder.tv_name.setText(text.account_name);
//		holder.title.setText(text.channel_name);
		if(text.card_no!=null){
			String scard  = text.card_no.substring(0,4);
			String card  = text.card_no.substring(text.card_no.length()-4);
			holder.card.setText(scard+" **** **** "+card);
		}
		
//		holder.title.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(mContext, "Click title:" + position, Toast.LENGTH_SHORT).show();
//			}
//		});

		if (holder.edit != null) {
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(mContext, "Click edit:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}

		if (holder.delete != null) {
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					initDialog(position,view,text.card_no);
				
//					mData.remove(position);
//					notifyDataSetChanged();
//					Toast.makeText(mContext, "Click delete:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}

		if (holder.detail != null) {
			holder.detail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "Click detail:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		TextView title,card,tv_name;
		Button edit;
		Button delete;
		Button detail;
		LinearLayout lin_content;
		ImageView img_logo;
	}
	
	private void initDialog(final int position,final View view,final String cardNo) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(mcontext, R.style.CustomDialog,
				"", "确定删除", "否", "是",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							deletegoodstype(position,view,cardNo);
							
//							notifyDataSetChanged();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
	}
	
	
	public void deletegoodstype(final int position,final View view,String cardNo) {
		MyOilRequest req = new MyOilRequest();
		req.cardNo = cardNo;
		req.userId = UserInfoManager.getUserInfo(mcontext).id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.USEROILCARDUNBUNDLING, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(mcontext.getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						
						if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
//							T.showShort(context.getApplicationContext(), bean.data.RSPMSG);
							deleteCell(view, position);
							
						}else{
							T.showShort(mcontext.getApplicationContext(), bean.data.RSPMSG);
						}
						
					} else {
						T.showShort(mcontext.getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	
	public  void deleteCell(final View v, final int index) {  
	    AnimationListener al = new AnimationListener() {  
	        @Override  
	        public void onAnimationEnd(Animation arg0) {  
	        	mData.remove(index);  
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
//	            if (interpolatedTime == 1) {  
//	                v.setVisibility(View.GONE);  
//	            }  
//	            else {  
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
	                v.requestLayout();  
//	            }  
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


	public List<MyOilBean> getmData() {
		// TODO Auto-generated method stub
		return mData;
	}  
	
	public int getcardchannelName(String card){
		int a = 0;
		if(card.substring(0,1).equals("9")){
			a = 1;
		}else if(card.substring(0,6).equals("100011")){
			a = 2;
		}else {
			a = 3;
		}
		return a;
		
	}

}