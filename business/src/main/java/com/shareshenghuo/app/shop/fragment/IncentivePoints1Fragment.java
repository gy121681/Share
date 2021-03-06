package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.IncentivePoints1Adapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.network.request.FilialobeRequest;
import com.shareshenghuo.app.shop.network.request.IntegralRquest;
import com.shareshenghuo.app.shop.network.response.FilialobeResponse;
import com.shareshenghuo.app.shop.network.response.IntegralResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

/**
 * @author hang 产业链秀点,激励秀点
 */
public class IncentivePoints1Fragment extends BaseFragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvData;

	private int pageNo = 1;
	private int pageSize = 10;
	private IncentivePoints1Adapter adapter;
	private TextView tv_title, tv_num;
	private RadioButton btn_1, btn_2;

	@Override
	protected int getLayoutId() {
		return R.layout.incentivepoints_fragment;
	}

	@Override
	protected void init(View rootView) {
		tv_title = getView(R.id.tv_title);
		tv_num = getView(R.id.tv_num);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);

		btn_1 = getView(R.id.btn_1);
		btn_1.setChecked(true);
		btn_1.setTextColor(getResources().getColor(R.color.black));
		btn_2 = getView(R.id.btn_2);
		tv_title.setText("累计获得激励秀点");
		tv_num.setText("0");

		loadData();

		btn_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo = 1;
				loadData();
				btn_1.setTextColor(getResources().getColor(R.color.black));
				btn_2.setTextColor(getResources().getColor(R.color.black));
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo = 1;
				btn_2.setTextColor(getResources().getColor(R.color.black));
				btn_1.setTextColor(getResources().getColor(R.color.black));
			}
		});
	}

	public void loadData() {

		FilialobeRequest req = new FilialobeRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id + "";
		req.userType = "2";
		req.operbType = "";
		req.moneyType = "0";
		req.opersType = "01";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.OBEDIENCELIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						lvData.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						lvData.onRefreshComplete();
						FilialobeResponse bean = new Gson().fromJson(
								resp.result, FilialobeResponse.class);
						if (Api.SUCCEED == bean.result_code)
							// if(bean.data!=null){
							updateView(bean.data);
						// }
					}
				});

	}

	// public void loadData() {
	// FavorityListRequest req = new FavorityListRequest();
	// req.user_id = UserInfoManager.getUserId(getActivity())+"";
	// req.collect_type = "1";
	// req.page_no = pageNo+"";
	// req.page_size = pageSize+"";
	// RequestParams params = new RequestParams();
	// try {
	// params.setBodyEntity(new StringEntity(req.toJson()));
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// new HttpUtils().send(HttpMethod.POST, Api.URL_FAVORITY_LIST, params, new
	// RequestCallBack<String>() {
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// lvData.onRefreshComplete();
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> resp) {
	// lvData.onRefreshComplete();
	// FavorityShopResponse bean = new Gson().fromJson(resp.result,
	// FavorityShopResponse.class);
	// if(Api.SUCCEED == bean.result_code)
	// updateView(bean.data);
	// }
	// });
	// }

	public void updateView(List<FilialobeBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new IncentivePoints1Adapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onResume() {
		super.onResume();
		// onPullDownToRefresh(lvData);
	}
}
