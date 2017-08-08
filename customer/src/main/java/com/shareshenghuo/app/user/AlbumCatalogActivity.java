package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.PhotoInfo;
import com.shareshenghuo.app.user.network.request.ShopPhotoRequest;
import com.shareshenghuo.app.user.network.response.ShopPhotoResponse;
import com.shareshenghuo.app.user.network.response.ShopPhotoResponse.ShopPhoto;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.TransferTempDataUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 商家照片目录
 */
public class AlbumCatalogActivity extends BaseTopActivity implements OnClickListener{
	
	private ImageView[] ivCover;
	
	private ShopPhoto data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_catalog);
		initTopBar("商家照片");
		initView();
		loadData();
	}
	
	public void initView() {
		int count = 4;
		ivCover = new ImageView[count];
		ivCover[0] = getView(R.id.ivPhotoDianpu);
		ivCover[1] = getView(R.id.ivPhotoDiannei);
		ivCover[2] = getView(R.id.ivPhotoFuwu);
		ivCover[3] = getView(R.id.ivPhotoOther);
		
		int width = (BitmapTool.getScreenWidthPX(this)-BitmapTool.dp2px(this, 6)*3) / 2;
		for(int i=0; i<count; i++)
			ivCover[i].setLayoutParams(new LayoutParams(width, width));
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopPhotoRequest req = new ShopPhotoRequest();
		req.shop_id = getIntent().getIntExtra("shopId", 0)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_PHOTO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AlbumCatalogActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ShopPhotoResponse bean = new Gson().fromJson(resp.result, ShopPhotoResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				}
			}
		});
	}
	
	public void updateView(ShopPhoto data) {
		this.data = data;
		
		setText(R.id.tvPhotoDianpuCount, data.mendian_count+"");
		setText(R.id.tvPhotoDianneiCount, data.diannei_count+"");
		setText(R.id.tvPhotoFuwuCount, data.fuwu_count+"");
		setText(R.id.tvPhotoOhterCount, data.qita_count+"");
		
		if(data.mendian_count > 0)
			setImageByURL(R.id.ivPhotoDianpu, data.mendian_photo.get(0).shop_photo);
		if(data.diannei_count > 0)
			setImageByURL(R.id.ivPhotoDiannei, data.diannei_photo.get(0).shop_photo);
		if(data.fuwu_count > 0)
			setImageByURL(R.id.ivPhotoFuwu, data.fuwu_photo.get(0).shop_photo);
		if(data.qita_count > 0)
			setImageByURL(R.id.ivPhotoOther, data.qita_photo.get(0).shop_photo);
		
		findViewById(R.id.llPhotoDianpu).setOnClickListener(this);
		findViewById(R.id.llPhotoDiannei).setOnClickListener(this);
		findViewById(R.id.llPhotoFuwu).setOnClickListener(this);
		findViewById(R.id.llPhotoOther).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llPhotoDianpu:
			if(data != null)
				openAlbum("店铺照片", data.mendian_photo);
			break;
			
		case R.id.llPhotoDiannei:
			if(data != null)
				openAlbum("店内照片", data.diannei_photo);
			break;
			
		case R.id.llPhotoFuwu:
			if(data != null)
				openAlbum("服务照片", data.fuwu_photo);
			break;
			
		case R.id.llPhotoOther:
			if(data != null) 
				openAlbum("其他照片", data.qita_photo);
			break;
		}
	}
	
	public void openAlbum(String title, List<PhotoInfo> photos) {
		if(photos==null || photos.size()<=0)
			return;
		
		TransferTempDataUtil.getInstance().setData(photos);
		Intent it = new Intent(this, ImagePagerActivity.class);
		it.putExtra("title", title);
		startActivity(it);
	}
}
