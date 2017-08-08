package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
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
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.AddPhotoGridAdapter;
import com.shareshenghuo.app.user.entity.PhotoBean;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.network.request.CategoryRequest;
import com.shareshenghuo.app.user.network.request.EditShopRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.CategoryResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.shareshenghuo.app.user.widget.dialog.ShopTypeWindow;
import com.shareshenghuo.app.user.widget.dialog.ShopTypeWindow.PickTypeCallback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author hang
 * 纠错商家信息
 */
public class EditShopActivity extends BaseTopActivity implements OnClickListener, PickTypeCallback {
	
	private static final int REQ_SET_ADDR = 0x101;
	
	private EditText edName;
	private EditText edMobile;
	private EditText edDesc;
	private LinearLayout llPickType;
	
	private ShopInfo shopInfo; // 为null新增  非null修改
	
	private AddPhotoGridAdapter adapter;
	
	private ShopTypeWindow shopTypeWindow;
	
	private int parentTypeId = -1;
	private int childTypeId = -1;
	private String addr;
	private double lat = -1, lng = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_shop);
		init();
	}
	
	public void init() {
		shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
		
		if(shopInfo != null)
			initTopBar("纠错商家信息");
		else
			initTopBar("新增商家");
		
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(this);
		
		llPickType = getView(R.id.llShopPickType);
		edName = getView(R.id.edShopName);
		edMobile = getView(R.id.edShopMobile);
		edDesc = getView(R.id.edShopDesc);
		
		if(shopInfo != null) {
			edName.setText(shopInfo.shop_name);
			edMobile.setText(shopInfo.mobile);
			setText(R.id.tvShopType, shopInfo.shop_child_type_name);
			setText(R.id.tvShopAddr, shopInfo.address);
			edDesc.setText(shopInfo.introduction);
			
			parentTypeId = shopInfo.shop_type_id;
			childTypeId = shopInfo.shop_child_type_id;
			addr = shopInfo.address;
			lat = shopInfo.latitude;
			lng = shopInfo.longitude;
		}
		
		MyGridView gvPhoto = getView(R.id.gvShopPhoto);
		List<PhotoBean> data = new ArrayList<PhotoBean>();
		data.add(new PhotoBean());
		adapter = new AddPhotoGridAdapter(this, data, gvPhoto);
		gvPhoto.setAdapter(adapter);
		
		llPickType.setOnClickListener(this);
		getView(R.id.llShopPickAddr).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
			if(ViewUtil.checkEditEmpty(edName, "请输入名称"))
				return;
			if(ViewUtil.checkEditEmpty(edMobile, "请输入联系电话"))
				return;
			if(TextUtils.isEmpty(addr)) {
				T.showShort(this, "请选择地址");
				return;
			}
			
			submit();
			break;
			
		case R.id.llShopPickType:
			if(shopTypeWindow == null)
				getShopTypeList();
			else
				shopTypeWindow.showAsDropDown(llPickType);
			break;
			
		case R.id.llShopPickAddr:
			startActivityForResult(new Intent(this, PoiMapActivity.class), REQ_SET_ADDR);
			break;
		}
	}
	
	public void submit() {
		ProgressDialogUtil.showProgressDlg(this, "提交审核");
		EditShopRequest req = new EditShopRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.city_id = CityManager.getInstance(this).cityInfo.id+"";
		req.shop_type_id = parentTypeId+"";
		req.shop_child_type_id = childTypeId+"";
		if(shopInfo != null)
			req.shop_id = shopInfo.id+"";
		req.shop_name = edName.getText().toString();
		req.mobile = edMobile.getText().toString();
		req.introduction = edDesc.getText().toString();
		req.address = addr;
		req.latitude = lat+"";
		req.longitude = lng+"";
		req.shop_photo = adapter.getPhotoUrls();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String api = "";
		if(shopInfo != null)
			api = Api.URL_ADD_SHOP_WRONG;
		else
			api = Api.URL_FIND_SHOP;
		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(EditShopActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showLong(EditShopActivity.this, "提交成功，请等待审核");
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}
	
	public void getShopTypeList() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		CategoryRequest req = new CategoryRequest();
		req.parent_id = "0";
		req.city_id = CityManager.getInstance(this).getCityId()+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CATEGORY_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CategoryResponse bean = new Gson().fromJson(resp.result, CategoryResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					shopTypeWindow = new ShopTypeWindow(EditShopActivity.this, bean.data,0);
					shopTypeWindow.setPickTypeCallback(EditShopActivity.this);
					shopTypeWindow.showAsDropDown(llPickType);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQ_SET_ADDR:
				addr = data.getStringExtra("addr");
				lat = data.getDoubleExtra("lat", lat);
				lng = data.getDoubleExtra("lng", lng);
				setText(R.id.tvShopAddr, addr);
				break;
			}
		}
	}

	/* 
	 * 商户类型选择回调
	 */
	@Override
	public void onPickedType(CategoryInfo parentInfo, CategoryInfo info,int po,int po1) {
		if(parentInfo!=null&&info!=null){
			setText(R.id.tvShopType, parentInfo.type_name+" "+info.type_name);
			parentTypeId = info.parent_id;
			childTypeId = info.id;
		}
	}
}
