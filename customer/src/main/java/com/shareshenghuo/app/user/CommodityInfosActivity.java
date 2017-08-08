package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.Banner;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.ProdDetailRequest;
import com.shareshenghuo.app.user.network.response.ProdDetailResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.AdGallery;
import com.shareshenghuo.app.user.widget.AdGallery.OnAdItemClickListener;
import com.shareshenghuo.app.user.widget.dialog.ShareMenuWindow;
import com.shareshenghuo.app.user.widget.AdGalleryHelper;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommodityInfosActivity extends BaseTopActivity implements OnAdItemClickListener{
	
	private String id  = "";
	private RelativeLayout adContainer;
	private AdGalleryHelper adGalleryHelper;
	private AdGallery adGallery;
	private List<Banner> list;
	private ProdInfo prodInfo;
	private TextView name;
	private TextView pic;
	private TextView modo;
	private TextView content,arrow_left;
	private Button btnShare;
	private ProdInfo datas;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.commodity_infos_activity);
		prodInfo = (ProdInfo) getIntent().getSerializableExtra("prodInfo");
		
		initview();
		initdata();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ProdDetailRequest req = new ProdDetailRequest();
		req.id = prodInfo.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETGOODSDETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CommodityInfosActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdDetailResponse bean = new Gson().fromJson(resp.result, ProdDetailResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code && bean.data != null) {
					upview(bean.data);
					datas = bean.data;
				}
			}
		});
	}

	private void initview() {
		// TODO Auto-generated method stub
//		initTopBar("商品详情");
		arrow_left = getView(R.id.arrow_left);
		btnShare = getView(R.id.btnShare);
		arrow_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(list!=null&&list.size()>0){
					ShareMenuWindow window = new ShareMenuWindow(CommodityInfosActivity.this);
					window.ico =list.get(0).banner_imgurl;
					window.url = "";
					window.title = getResources().getString(R.string.app_name);
					window.content = datas.name;
					window.showAtBottom();
				}
			}
		});
		adContainer = getView(R.id.ad_container);
		name = getView(R.id.name);
		pic = getView(R.id.pic);
		modo = getView(R.id.modo);
		content = getView(R.id.content);
		
		
	}
	
	private void upview(ProdInfo data) {
		// TODO Auto-generated method stub
		
		DecimalFormat df = new DecimalFormat("###.00"); 
		String price = "";
		try {
			price = df.format(Double.parseDouble(data.price));
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		name.setText(data.name);
		pic.setText("￥"+price);
		modo.setText(data.model);
		content.setText(data.description);
		
		list = new ArrayList<Banner>();
		if(!TextUtils.isEmpty(data.photo)){
			String[] shop_photos = data.photo.split(",");
			for (int i = 0; i < shop_photos.length; i++) {
				Banner b = new Banner();
				b.banner_imgurl = shop_photos[i];
				list.add(b);
			}
		}
		if(list!=null&&list.size()>0){
			realizeFunc(list);
		}
	}

	
	public void realizeFunc(List<Banner> data){
		if(adGalleryHelper!=null){
			adGalleryHelper = null;
			adContainer.removeAllViews();
			adGallery = null;
		}
			try {
				adGalleryHelper = new AdGalleryHelper(CommodityInfosActivity.this, data, 5000,false);
				adContainer.addView(adGalleryHelper.getLayout());
				adGallery = adGalleryHelper.getAdGallery();
				adGallery.setAdOnItemClickListener(this);
			} catch (Exception e) {
				// TODO: handle exception
			}
	}

	@Override
	public void setItemClick(int position) {
		// TODO Auto-generated method stub
		ArrayList<String> arr = new ArrayList<String>();
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				arr.add(list.get(i).banner_imgurl);
			}
			
			Intent it = new Intent(CommodityInfosActivity.this, ImagePagerActivity.class);
			it.putExtra("title", "浏览");
			it.putExtra("position", 0);
			it.putStringArrayListExtra("urls", arr);
			startActivity(it);	
		}

	}
}
