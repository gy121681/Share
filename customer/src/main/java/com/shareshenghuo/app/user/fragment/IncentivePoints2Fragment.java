package com.shareshenghuo.app.user.fragment;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.IncentivePoints2Adapter;
import com.shareshenghuo.app.user.network.bean.ExcitationBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author hang
 * 收藏的商家
 */
public class IncentivePoints2Fragment extends BaseFragment implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private IncentivePoints2Adapter adapter;
	private TextView tv_title,tv_num;
	private RadioButton btn_1,btn_2;
	private RadioGroup radio;
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
		btn_1.setTextColor(getResources().getColor(R.color.white));
		btn_2 = getView(R.id.btn_2);
		radio = getView(R.id.radio);
		radio.setVisibility(View.GONE);
		tv_title.setText("累计获得获赠秀点");
		tv_num.setText("300");
		
		loadData();
		
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo =1;
				loadData();
				btn_1.setTextColor(getResources().getColor(R.color.white));
				btn_2.setTextColor(getResources().getColor(R.color.black));
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo =1;
				loadData1();
				btn_2.setTextColor(getResources().getColor(R.color.white));
				btn_1.setTextColor(getResources().getColor(R.color.black));
			}
		});
	}
	public void loadData() {
		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
		for (int i = 0; i < 10; i++) {
			ExcitationBean data = new ExcitationBean();
			data.amount = "100";
			data.time = "2016-8-15";
			data.num = " +2.00";
			data.name  = "哈哈哈";
			bean.add(data);
		}
		updateView(bean);
		
	}
	
	public void loadData1() {
		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
		for (int i = 0; i < 10; i++) {
			ExcitationBean data = new ExcitationBean();
			data.amount = "1.00";
			data.time = "2016-8-15";
			data.num = " +34.00";
			data.name  = "嘿嘿嘿";
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
		if(pageNo==1 || adapter==null) {
			adapter = new IncentivePoints2Adapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
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
		// TODO Auto-generated method stub
		super.onResume();
		onPullDownToRefresh(lvData);
		Log.e("", " - - - - - - - - - - ");
	}
}
