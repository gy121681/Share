package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.ExcitationAdapter;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;

/**
 * @author hang
 * 收藏的商家
 */
public class Excitation1Fragment extends BaseFragment implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private ExcitationAdapter adapter;
	private TextView tv_title,tv_num;

	@Override
	protected int getLayoutId() {
		return R.layout.excitation_fragment;
	}

	@Override
	protected void init(View rootView) {
		tv_title = getView(R.id.tv_title);
		tv_num = getView(R.id.tv_num);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		tv_title.setText("累计获得25%激励秀心数");
		
		loadData();
	}
	public void loadData() {
		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
		for (int i = 0; i < 10; i++) {
			ExcitationBean data = new ExcitationBean();
			data.amount = "交易金额: 2.00 元";
			data.time = "结算时间: 2016-8-15";
			data.num = "+3";
			bean.add(data);
		}
		updateView(bean);
		
	}
//	public void loadData() {
//		FavorityListRequest req = new FavorityListRequest();
//		req.user_id = UserInfoManager.getUserId(getActivity())+"";
//		req.collect_type = "1";
//		req.page_no = pageNo+"";
//		req.page_size = pageSize+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_FAVORITY_LIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				lvData.onRefreshComplete();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				lvData.onRefreshComplete();
//				FavorityShopResponse bean = new Gson().fromJson(resp.result, FavorityShopResponse.class);
//				if(Api.SUCCEED == bean.result_code)
//					updateView(bean.data);
//			}
//		});
//	}
	
	public void updateView(List<ExcitationBean> data) {
//		if(pageNo==1 || adapter==null) {
//			adapter = new ExcitationAdapter(getActivity(), data);
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
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
}
