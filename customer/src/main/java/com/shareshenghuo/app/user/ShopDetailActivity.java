package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CouponGridAdapter;
import com.shareshenghuo.app.user.adapter.ShopActivListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.EaseUserManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ActivInfo;
import com.shareshenghuo.app.user.network.bean.CardInfo;
import com.shareshenghuo.app.user.network.bean.CouponInfo;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.network.request.CollectRequest;
import com.shareshenghuo.app.user.network.request.ReceiveCardRequest;
import com.shareshenghuo.app.user.network.request.ReceiveCouponRequest;
import com.shareshenghuo.app.user.network.request.ShopDetailRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.ShopDetailResponse;
import com.shareshenghuo.app.user.network.response.ShopDetailResponse.ShopDetail;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Utility;
import com.shareshenghuo.app.user.widget.dialog.ShareMenuWindow;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 商家详情页
 */
public class ShopDetailActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private static final int REQ_GET_CARD = 0x101;
	
	private CheckBox cbCollect;
	private Button btnReceive;
	private GridView gvCoupon;
	private ListView lvActiv;
	
	private int shopId;
	private ImageView ivintegral,ivintegralz;
	private CityManager cityManager;
	
	private ShopDetail shopDetail;
	
	private ShopInfo shopInfo;
	private ActivInfo activInfo;
	private TextView tvShoptime;

	private Button mBtnBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
		initView();
		initListener();
		initData();
	}
	
	public void initView() {
		initTopBar("商家");
		cbCollect = getView(R.id.cbCollect);
		btnReceive = getView(R.id.btnReceiveCard);
		gvCoupon = getView(R.id.gvCoupon);
		lvActiv = getView(R.id.lvShopActiv);
		ivintegral = getView(R.id.ivintegral);
		ivintegralz = getView(R.id.ivintegralz);
		tvShoptime = getView(R.id.tvShoptimes);
		mBtnBuy = getView(R.id.btn_buy);
//		if (BuildConfig.isApply) {
//			mBtnBuy.setVisibility(View.VISIBLE);
//			mBtnBuy.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
//		}
	}
	
	public void initListener() {
		cbCollect.setOnClickListener(this);
		findViewById(R.id.ivShopLogo).setOnClickListener(this);
		findViewById(R.id.llShopDetailChoose).setOnClickListener(this);
		findViewById(R.id.btnBuy).setOnClickListener(this);
		findViewById(R.id.btnRecovery).setOnClickListener(this);
		btnReceive.setOnClickListener(this);
		getView(R.id.btnMoreComment).setOnClickListener(this);
		findViewById(R.id.llShopCall).setOnClickListener(this);
		findViewById(R.id.llShopNav).setOnClickListener(this);
		findViewById(R.id.llShopMoreCoupon).setOnClickListener(this);
		findViewById(R.id.llShopActiv).setOnClickListener(this);
		findViewById(R.id.llShopDetailIM).setOnClickListener(this);
		findViewById(R.id.btnShare).setOnClickListener(this);
		findViewById(R.id.rlCardDetail).setOnClickListener(this);
		findViewById(R.id.llmap).setOnClickListener(this);
		getView(R.id.btnMoreActiv).setOnClickListener(this);
		findViewById(R.id.llShopDetailIntroduce).setOnClickListener(this);
	}
	
	public void initData() {
		shopId = getIntent().getIntExtra("shopId", 0);
		cityManager = CityManager.getInstance(this);
		
		loadaData();
	}
	
	public void loadaData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopDetailRequest req = new ShopDetailRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.shop_id = shopId+"";
		req.latitude = cityManager.latitude+"";
		req.longitude = cityManager.longitude+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ShopDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ShopDetailResponse bean = new Gson().fromJson(resp.result, ShopDetailResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data != null) {
					shopDetail = bean.data;
					updateShopInfo(bean.data.shop_info);
					updateVipCard(bean.data.car_info);
					updateCouponInfo(bean.data.coupon_info);
					updateActivInfo(bean.data.active_info);
				} else {
					T.showShort(ShopDetailActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void updateShopInfo(ShopInfo data) {
		this.shopInfo = data;
		if(data.shop_open_time!=null){
		tvShoptime.setText(data.shop_open_time.trim()+" - "+data.shop_end_time.trim());
		}
		setImageByURL(R.id.ivShopLogo, Utility.getFirstString(data.big_logo));
		setText(R.id.tvShopName, data.shop_name);
		setText(R.id.tvShopDesc, data.introduction);
		setText(R.id.tvshopNavDistance, data.range+"km");
		setText(R.id.tvShopAddr, data.address);
		setText(R.id.tvShopMobile, data.mobile);
		setText(R.id.tvShopPerFee, data.consumption_per_person);
		setText(R.id.tvShopManJian, "满"+data.lowest_fee+"减"+data.coupon_fee);
		getView(R.id.ivShopHui).setVisibility(data.is_have_active==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopHui).setVisibility(View.GONE);
		getView(R.id.ivShopQuan).setVisibility(data.is_have_coupon==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopQuan).setVisibility(View.GONE);
		getView(R.id.ivShopQing).setVisibility(data.is_muslim==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopQing).setVisibility(View.GONE);
		getView(R.id.ivShopSong).setVisibility(data.is_push==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopSong).setVisibility(View.GONE);
		getView(R.id.ivShopIntegrity).setVisibility(data.is_integrity==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopIntegrity).setVisibility(View.GONE);
		getView(R.id.ivShopAuth).setVisibility(data.is_authentication==1? View.VISIBLE:View.GONE);
		getView(R.id.ivShopAuth).setVisibility(View.GONE);

		cbCollect.setChecked(data.is_collect == 1);

		if(data.is_consumption!=null&&data.is_consumption.equals("1")){
			ivintegralz.setImageResource(R.drawable.zxf_img);
		}
		
		if(data.is_discount_type_three.equals("1")){
//			ivintegral.setVisibility(View.VISIBLE);
			ivintegral.setImageResource(R.drawable.share_c_business_label_100);
			return;
		}
		if(data.is_discount_type_two.equals("1")){
//			ivintegral.setVisibility(View.VISIBLE);
			ivintegral.setImageResource(R.drawable.share_c_business_label_50);
			return;
		}
		if(data.is_discount_type_one.equals("1")){
//			ivintegral.setVisibility(View.VISIBLE);
			ivintegral.setImageResource(R.drawable.share_c_business_label_25);
			return;
		}
		
		
	}
	
	public void updateVipCard(CardInfo data) {
		if(data==null || data.id==0)
			return;
		
		if(TextUtils.isEmpty(data.user_id)) {
			btnReceive.setVisibility(View.VISIBLE);
		} else {
			btnReceive.setVisibility(View.GONE);
			setText(R.id.tvCardPoint, "积分："+data.point);
			setText(R.id.tvCardMoney, "余额："+data.money);
		}
		setText(R.id.tvCardNo, "NO:"+data.card_no);
		setText(R.id.tvCardName, data.shop_name);
	}
	
	public void updateCouponInfo(List<CouponInfo> data) {
		if(data!=null && data.size()>0) {
			getView(R.id.llShopCouponContainer).setVisibility(View.VISIBLE);
			gvCoupon.setAdapter(new CouponGridAdapter(ShopDetailActivity.this, data));
			gvCoupon.setOnItemClickListener(this);
		}
	}
	
	public void updateActivInfo(List<ActivInfo> data) {
		if(data!=null && data.size()>0) {
			activInfo = data.get(0);
			
			getView(R.id.llShopActiv).setVisibility(View.VISIBLE);
			lvActiv.setAdapter(new ShopActivListAdapter(this, data));
			Utility.setListViewHeightBasedOnChildren(lvActiv);
			lvActiv.setOnItemClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.cbCollect:
			collect(cbCollect.isChecked());
			break;
		case R.id.llmap:
			Intent nav = new Intent(this, ShopNavActivity.class);
			nav.putExtra("title", shopDetail.shop_info.shop_name);
			nav.putExtra("addr", shopDetail.shop_info.address);
			nav.putExtra("lat", shopDetail.shop_info.latitude);
			nav.putExtra("lng", shopDetail.shop_info.longitude);
			startActivity(nav);
			break;
			
		case R.id.ivShopLogo:
			Intent photo = new Intent(this, AlbumCatalogActivity.class);
			photo.putExtra("shopId", shopId);
			
			startActivity(photo);
			break;
		
		case R.id.llShopDetailChoose:
			Intent prod = new Intent(this, ProdClassificationActivity.class);
			prod.putExtra("shopId", shopId);
			startActivity(prod);
			break;
			
		case R.id.btnBuy:
			//闪付
//			if(shopInfo == null)
//				return;
//			
//			Intent flash = new Intent(this, ShopFlashPayActivity.class);
//			flash.putExtra("activInfo", activInfo);
//			flash.putExtra("shopInfo", shopInfo);
//			startActivity(flash);
			break;
			
		case R.id.btnRecovery:
			Intent recovery = new Intent(this, EditShopActivity.class);
			recovery.putExtra("shopInfo", shopDetail.shop_info);
			startActivity(new Intent(recovery));
			break;
			
		case R.id.btnReceiveCard:
			if(TextUtils.isEmpty(UserInfoManager.getUserInfo(this).real_name))
				startActivityForResult(new Intent(this, PerfectPersonalInfoActivity.class), REQ_GET_CARD);
			else
				receiveVipCard();
			break;
			
		case R.id.rlCardDetail:
			if(shopDetail==null || shopDetail.car_info==null || TextUtils.isEmpty(shopDetail.car_info.user_id))
				return;
			
			Intent card = new Intent(this, CardDetailActivity.class);
			card.putExtra("cardInfo", shopDetail.car_info);
			startActivity(card);
			break;
			
		case R.id.llShopCall:
			if(shopDetail != null)
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+shopDetail.shop_info.mobile)));
			else
				receiveVipCard();
			break;
			
		case R.id.llShopNav:
//			Intent nav = new Intent(this, ShopNavActivity.class);
//			nav.putExtra("title", shopDetail.shop_info.shop_name);
//			nav.putExtra("addr", shopDetail.shop_info.address);
//			nav.putExtra("lat", shopDetail.shop_info.latitude);
//			nav.putExtra("lng", shopDetail.shop_info.longitude);
//			startActivity(nav);
			break;
			
		case R.id.llShopMoreCoupon:
			Intent coupon = new Intent(this, ShopCouponListActivity.class);
			coupon.putExtra("shopId", shopId);
			startActivity(coupon);
			break;
			
		case R.id.btnMoreComment:
			Intent comment = new Intent(this, CommentListActivity.class);
			comment.putExtra("shopId", shopId);
			startActivity(comment);
			break;
			
		case R.id.btnMoreActiv:
			Intent activ = new Intent(this, ShopActivListActivity.class);
			activ.putExtra("shopId", shopId);
			startActivity(activ);
			break;
			
		case R.id.llShopDetailIM:
			// 进入聊天页面
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, "s"+shopId);
            startActivity(intent);
            EaseUserManager.addContact(this, shopId);
			break;
			
		case R.id.btnShare:
			ShareMenuWindow window = new ShareMenuWindow(this);
			window.url = Api.URL_SHARE_SHOP + "?shop_id=" + shopId;
			window.title = shopInfo.shop_name;
			window.content = shopInfo==null? getResources().getString(R.string.app_name) : shopInfo.introduction;
			window.thumbnail = Api.HOST + shopInfo.logo;
			window.showAtBottom();
			break;
			
		case R.id.llShopDetailIntroduce:
			Intent introduce = new Intent(this, WebLoadActivity.class);
			introduce.putExtra("title", "商家介绍");
			introduce.putExtra("url", Api.URL_SHOP_INTRODUCE+"?shop_id="+shopId);
			startActivity(introduce);
			break;
		}
	}
	
	public void collect(final boolean isChecked) {
		ProgressDialogUtil.showProgressDlg(this, "");
		CollectRequest req = new CollectRequest();
		req.collect_id = shopId+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.collect_type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_COLLECT, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ShopDetailActivity.this, "成功");
				} else {
					T.showShort(ShopDetailActivity.this, bean.result_desc);
					cbCollect.setChecked(!isChecked);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ShopDetailActivity.this);
			}
		});
	}
	
	public void receiveCoupon(int couponId) {
		ProgressDialogUtil.showProgressDlg(this, "领取优惠券");
		ReceiveCouponRequest req = new ReceiveCouponRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.coupon_id = couponId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_RECEIVE_COUPON, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					loadaData();
					T.showShort(ShopDetailActivity.this, "领取成功");
				} else {
					T.showShort(ShopDetailActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ShopDetailActivity.this);
			}
		});
	}
	
	/**
	 * 领取会员卡
	 */
	public void receiveVipCard() {
		ProgressDialogUtil.showProgressDlg(this, "领取会员卡");
		ReceiveCardRequest req = new ReceiveCardRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.shop_id = shopId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_RECEIVE_CARD, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ShopDetailActivity.this, "领取成功");
					loadaData();
				} else {
					T.showShort(ShopDetailActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ShopDetailActivity.this);
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode == RESULT_OK) {
			switch(reqCode) {
			case REQ_GET_CARD:
				receiveVipCard();
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(parent == gvCoupon) {
			CouponInfo item = (CouponInfo) parent.getItemAtPosition(position);
			receiveCoupon(item.id);
		} else if(parent == lvActiv) {
			ActivInfo item = (ActivInfo) parent.getItemAtPosition(position);
			Intent it = new Intent(this, ActivDetailActivity.class);
			it.putExtra("activInfo", item);
			startActivity(it);
		}
	}
}
