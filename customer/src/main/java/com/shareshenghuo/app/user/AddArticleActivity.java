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
import com.shareshenghuo.app.user.network.request.AddArticleRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

/**
 * @author hang
 *	发布文章
 */
public class AddArticleActivity extends BaseTopActivity {
	
	private EditText edTitle;
	private EditText edContent;
	private GridView gvPhoto;
	
	private AddPhotoGridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_article);
		initView();
	}
	
	public void initView() {
		initTopBar("发布文章");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("发送");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
		
		edTitle = getView(R.id.edArticleTitle);
		edContent = getView(R.id.edArticleContent);
		gvPhoto = getView(R.id.gvArticlePhoto);
		
		List<PhotoBean> data = new ArrayList<PhotoBean>();
		data.add(new PhotoBean());
		adapter = new AddPhotoGridAdapter(this, data, gvPhoto);
		gvPhoto.setAdapter(adapter);
	}
	
	public void submit() {
//		if(ViewUtil.checkEditEmpty(edTitle, "请填写标题"))
//			return;
		if(ViewUtil.checkEditEmpty(edContent, "请填写内容"))
			return;
		
		ProgressDialogUtil.showProgressDlg(this, "发送中");
		AddArticleRequest req = new AddArticleRequest();
		req.city_id = CityManager.getInstance(this).cityInfo.id+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.title = edTitle.getText().toString();
		req.content = edContent.getText().toString();
		req.photo = adapter.getPhotoUrls();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_ARTICLE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AddArticleActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(AddArticleActivity.this, "发布成功");
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(AddArticleActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.onActivityResult(requestCode, resultCode, data);
	}
}
