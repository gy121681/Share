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
import com.shareshenghuo.app.shop.adapter.IncentivePointsAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.network.request.FilialobeRequest;
import com.shareshenghuo.app.shop.network.response.FilialobeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

/**
 */
public class IncentivePointsFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvData;

	private ListView views;

	private int pageNo = 1;
	private int pageSize = 10;
	private IncentivePointsAdapter adapter;
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
		btn_2 = getView(R.id.btn_2);
		btn_1.setChecked(true);
		btn_1.setTextColor(getResources().getColor(R.color.black));
		

		tv_title.setText("累计获得推荐秀点");

		loadData();

		btn_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo =1;
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
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.userType = "2";
		req.operbType = "";
		req.opersType = "02";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		Log.e("", " - - - - "+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", " - - - -  "+Api.OBEDIENCELIST);
		new HttpUtils().send(HttpMethod.POST, Api.OBEDIENCELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FilialobeResponse bean = new Gson().fromJson(resp.result, FilialobeResponse.class);
				if(Api.SUCCEED == bean.result_code){
						updateView(bean.data);
				}
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
		if(data!=null){
		if (pageNo == 1 || adapter == null) {
			if(getActivity()!=null&&data.size()>0){
				adapter = new IncentivePointsAdapter(activity, data);
				lvData.setAdapter(adapter);
			}
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		}
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
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
////		if(resultCode==activity.RESULT_OK && requestCode==OrderListAdapter.REQ_ORDER_OPERATE)
//			onPullDownToRefresh(lvData);
//	}
//	@Override
//	public void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		Log.e("", "  = = = = = = =  ");
//	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		onPullDownToRefresh(lvData);
	}
}
