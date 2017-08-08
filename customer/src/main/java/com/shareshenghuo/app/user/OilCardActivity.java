//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//
//import org.apache.http.entity.StringEntity;
//
//import UserInfoManager;
//import OilInfoBean;
//import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
//import com.shareshenghuo.app.user.network.request.AddOilCardRequest;
//import AddOilCardResponse;
//import com.shareshenghuo.app.user.network.response.OilInfoResponse;
//import Api;
//import ChexText;
//import com.shareshenghuo.app.user.util.DateUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.ViewUtil;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//public class OilCardActivity extends BaseTopActivity implements OnClickListener{
//
//	private TextView tv_notice,tvs,tv_name;
//	private ImageView img;
//	private RelativeLayout agt_layout;
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.oil_card_activity);
//		initView();
//		initdata();
//	}
//	
//	public void initView() {
//		initTopBar("我的油卡");
//		tvs = getView(R.id.tvs);
//		img = getView(R.id.img);
//		tv_name = getView(R.id.tv_name);
//		tv_notice = getView(R.id.tv_notice);
//		agt_layout = getView(R.id.agt_layout);
//		findViewById(R.id.img1).setOnClickListener(this);
//		findViewById(R.id.img2).setOnClickListener(this);
//		findViewById(R.id.img3).setOnClickListener(this);
//		findViewById(R.id.img4).setOnClickListener(this);
//		findViewById(R.id.relayout).setOnClickListener(this);
//		
//	}
//
//	
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.img1:
//			startActivity(new Intent(OilCardActivity.this,OilIntegralActivity.class));
//			break;
//		case R.id.img2:
//			startActivity(new Intent(OilCardActivity.this,OilRechargeActivity.class));
//			break;
//		case R.id.img3:
//			startActivity(new Intent(OilCardActivity.this,BindOilCardActivity.class));
//			break;
//		case R.id.img4:
//			startActivity(new Intent(OilCardActivity.this,OilReceivQrActivity.class));
//			break;
//		case R.id.relayout:
//			Intent introduce = new Intent(this, WebLoadActivity.class);
//			introduce.putExtra("title", "规则");
//			introduce.putExtra("url", Api.OILCARDRULE);
//			startActivity(introduce);
//			break;
//		default:
//			break;
//		}
//		
//	}
//	
//	private void initdata() {
//		// TODO Auto-generated method stub
//		
//		AddOilCardRequest req = new AddOilCardRequest();
//		req.userId = UserInfoManager.getUserInfo(this).id+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.OILCARDINTEGRALLOGCONTROLLER, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				OilInfoResponse bean = new Gson().fromJson(resp.result, OilInfoResponse.class);
//				Log.e("", ""+resp.result);
//				if(Api.SUCCEED == bean.result_code){
//					
//					upview(bean.data);
////					finish();
////					if(bean.data.RSPCOD.equals("000000")){
////						T.showShort(getApplicationContext(), bean.data.RSPMSG);
////						finish();
////					}else{
////						T.showShort(getApplicationContext(), bean.data.RSPMSG);
////					}
//				}else{
//					T.showShort(getApplicationContext(), bean.result_desc);
//				}
//			}
//
//		});
//	}
//	private void upview(OilInfoBean data) {
//		// TODO Auto-generated method stub
//		
//		if(data.is_agent!=null){
//			if(data.is_agent.equals("1")){
//				agt_layout.setVisibility(View.VISIBLE);
//			}else{
//				agt_layout.setVisibility(View.GONE);
////				tvs.setVisibility(View.GONE);
////				img.setVisibility(View.GONE);
//			}
//		}
//		if(data.user_name!=null){
//			tv_name.setText(data.user_name);
//		}else{
//			tv_name.setText("未认证");
//		}
//		
//		if(data.activities!=null&&data.activities.size()>0){
//			tv_notice.setText(data.activities.get(0).activity+"  "+DateUtil.getTime(data.activities.get(0).start_time,2)+" - "+DateUtil.getTime(data.activities.get(0).end_time,2));
//		}
//	}
//	
//}
