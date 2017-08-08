package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shareshenghuo.app.user.CardDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ClubCardAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CardInfo;
import com.shareshenghuo.app.user.network.request.MyCardsRequest;
import com.shareshenghuo.app.user.network.response.MyCardsResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 会员卡列表
 */
public class CardListFragment extends Fragment implements OnItemClickListener {
	
	private ListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 200;
	public String keyword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.view_listview, container, false);
		initView(root);
		return root;
	}

	public void initView(View root) {
		lvData = (ListView) root.findViewById(R.id.lvData);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(getActivity(), "加载数据");
		MyCardsRequest req = new MyCardsRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.user_id = UserInfoManager.getUserId(getActivity())+"";
		if(!TextUtils.isEmpty(keyword))
			req.search_name = keyword;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_MY_CARDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				MyCardsResponse bean = new Gson().fromJson(resp.result, MyCardsResponse.class);
				if(Api.SUCCEED == bean.result_code && getActivity()!=null)
					lvData.setAdapter(new ClubCardAdapter(getActivity(), bean.data));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		CardInfo item = (CardInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(getActivity(), CardDetailActivity.class);
		it.putExtra("cardInfo", item);
		startActivity(it);
	}
}
