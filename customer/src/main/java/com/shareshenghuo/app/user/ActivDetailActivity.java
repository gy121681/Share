package com.shareshenghuo.app.user;

import android.os.Bundle;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ActivInfo;

/**
 * @author hang
 * 活动详情页
 */
public class ActivDetailActivity extends BaseTopActivity  {
	
	private ActivInfo activInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activ_detail);
//		initView();
	}
	
//	public void initView() {
//		activInfo = (ActivInfo) getIntent().getSerializableExtra("activInfo");
//		
//		initTopBar("详情");
//		btnTopRight3.setBackgroundResource(R.drawable.cb_collect);
//		btnTopRight3.setVisibility(View.VISIBLE);
//		btnTopRight3.setOnClickListener(this);
//		btnTopRight3.setChecked(activInfo.is_collect == 1);
//		
//		btnTopRight2.setBackgroundResource(R.drawable.icon_89);
//		btnTopRight2.setVisibility(View.VISIBLE);
//		btnTopRight2.setOnClickListener(this);
//		
//		String url = Api.URL_ACTIV_DETAIL + "?active_id=" + activInfo.id;
//		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//		t.replace(R.id.rlContent, WebLoadFragment.getInstance(url));
//		t.commit();
//		
//		getView(R.id.llActivDetailNav).setOnClickListener(this);
//		getView(R.id.llActivDetailShop).setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.btnTopRight3:
//			collect(btnTopRight3.isChecked());
//			break;
//		
//		case R.id.llActivDetailNav:
//			Intent nav = new Intent(this, ShopNavActivity.class);
//			nav.putExtra("title", activInfo.active_title);
//			nav.putExtra("addr", activInfo.address);
//			nav.putExtra("lat", activInfo.latitude);
//			nav.putExtra("lng", activInfo.longitude);
//			startActivity(nav);
//			break;
//			
//		case R.id.llActivDetailShop:
//			Intent shop = new Intent(this, ShopDetailActivity.class);
//			shop.putExtra("shopId", activInfo.shop_id);
//			startActivity(shop);
//			break;
//			
//		case R.id.btnTopRight2:
//			ShareMenuWindow window = new ShareMenuWindow(this);
//			window.url = Api.URL_ACTIV_DETAIL + "?active_id=" + activInfo.id;
//			window.title = activInfo.active_title;
//			window.content = activInfo.address;
//			window.thumbnail = Api.HOST + activInfo.thum_photo;
//			window.showAtBottom();
//			break;
//		}
//	}
//	
//	public void collect(final boolean isChecked) {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		CollectRequest req = new CollectRequest();
//		req.collect_id = activInfo.id+"";
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.collect_type = "2";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_COLLECT, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(ActivDetailActivity.this, "成功");
//					btnTopRight3.setChecked(isChecked);
//				} else {
//					T.showShort(ActivDetailActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(ActivDetailActivity.this);
//			}
//		});
//	}
}
