package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.easemob.easeui.EaseConstant;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.EaseUserManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.ProdDetailRequest;
import com.shareshenghuo.app.user.network.response.ProdDetailResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.ProdFormatWindow;
import com.shareshenghuo.app.user.widget.proddetail.ProdDetailBottomView;
import com.shareshenghuo.app.user.widget.proddetail.ProdDetailTopView;
import com.shareshenghuo.app.user.widget.snapscrollview.McoySnapPageLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ProdDetailActivity extends BaseTopActivity implements OnClickListener {
	
	private McoySnapPageLayout mcoySnapPageLayout;
	private ProdDetailTopView topView;
	private ProdDetailBottomView bottomView;
	
	private ProdInfo prodInfo;
	
	private boolean noCart;	//true 隐藏加入购物车

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prod_detail);
		
		prodInfo = (ProdInfo) getIntent().getSerializableExtra("prodInfo");
		noCart = getIntent().getBooleanExtra("noCart", false);
		
		if(noCart)
			findViewById(R.id.btnAddToCart).setVisibility(View.GONE);
		
		initListener();
		loadData();
	}
	
	
	public void initListener() {
		findViewById(R.id.btnCusSvr).setOnClickListener(this);
		findViewById(R.id.btnCart).setOnClickListener(this);
		findViewById(R.id.btnAddToCart).setOnClickListener(this);
		findViewById(R.id.btnBuyItNow).setOnClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ProdDetailRequest req = new ProdDetailRequest();
		req.product_id = prodInfo.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ProdDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdDetailResponse bean = new Gson().fromJson(resp.result, ProdDetailResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data != null) {
					mcoySnapPageLayout = (McoySnapPageLayout) findViewById(R.id.flipLayout);
					topView = new ProdDetailTopView(ProdDetailActivity.this, 
							getLayoutInflater().inflate(R.layout.view_prod_detail_top, null), 
							bean.data);
					bottomView = new ProdDetailBottomView(ProdDetailActivity.this, 
							getLayoutInflater().inflate(R.layout.view_prod_detail_bottom, null), 
							Api.URL_PROD_DETAIL_WAP+"?product_id="+bean.data.id);
					mcoySnapPageLayout.setSnapPages(topView, bottomView);
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(topView != null)
			topView.startScroll();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(topView != null)
			topView.stopScroll();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnCusSvr:
			if(UserInfoManager.toLogin(this))
				return;
			
			// 进入聊天页面
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, "s"+prodInfo.shop_id);
            startActivity(intent);
            EaseUserManager.addContact(this, prodInfo.shop_id);
			break;
			
		case R.id.btnCart:
			startActivity(new Intent(this, CartActivity.class));
			break;
			
		case R.id.btnAddToCart:
		case R.id.btnBuyItNow:
			if(UserInfoManager.toLogin(this))
				return;
			new ProdFormatWindow(this, prodInfo, noCart).showAtBottom();
			break;
		}
	}
}
