package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.IntegralAdapter;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.IntegralBean;
import com.shareshenghuo.app.user.network.request.IntegralRquest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.response.IntegralResponse;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.Util;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 
* @author wyp
* @version 创建时间：2017-5-31 上午11:36:39
* 类说明：我的积分-产业链积分
 */
public class IntegralFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvData;
	private IntegralAdapter adapter;
	private TextView tv_num, tv_title, tv_title1, tv_num1;
	private int pageNo = 1;
	private int pageSize = 10;

	LinearLayout ll;// 当前积分布局
	RelativeLayout kejilijifen_layout, daijilijifen_layout;// 新消费积分,静态积分
	TextView daijili_text, kejili_text;// 待激励可激励

	private int integralType_num = 1;// 0可1待激励

	@Override
	protected int getLayoutId() {
		return R.layout.integral_activity;
	}

	@Override
	protected void init(View rootView) {
		initView();
		// loadData();
	}

	// private PullToRefreshListView lvData;
	// private IntegralAdapter adapter;
	// private TextView tv_num,tv_title,tv_title1,tv_num1;
	// private int pageNo = 1;
	// private int pageSize = 10;
	// private String integral,totalIntegral;
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.integral_activity);
	//
	// integral = getIntent().getStringExtra("integral");
	// totalIntegral = getIntent().getStringExtra("totalIntegral");
	//
	// }

	public void initView() {
		// initTopBar("公益积分", "秀儿积分");

		kejilijifen_layout = getView(R.id.kejilijifen_layout);// 新消费积分
		daijilijifen_layout = getView(R.id.daijilijifen_layout);// 静态积分

		daijili_text = getView(R.id.daijili_text);// 静态积分
		kejili_text = getView(R.id.kejili_text);

		ll = getView(R.id.ll);
		ll.setVisibility(View.GONE);

		tv_title = getView(R.id.tv_title);
		tv_title1 = getView(R.id.tv_title1);
		tv_title.setText("当前积分  ");
		tv_title1.setText("累计积分  ");
		tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);

		daijilijifen_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				daijilijifen_layout
						.setBackgroundResource(R.drawable.bg_spinner_content);
				kejilijifen_layout.setBackgroundResource(R.drawable.bai);
				integralType_num = 1;
				pageNo = 1;
				pageSize = 10;
				loadData(integralType_num);
			}
		});// 待激励
		kejilijifen_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				daijilijifen_layout.setBackgroundResource(R.drawable.bai);
				kejilijifen_layout
						.setBackgroundResource(R.drawable.bg_spinner_content);
				integralType_num = 0;
				pageNo = 1;
				pageSize = 10;
				loadData(integralType_num);
			}
		});// 可激励

		kejilijifen_layout.performClick();// 触发一下可激励的查询

		getStatisticsData();
	}

	public void getStatisticsData() {

		// ProgressDialogUtil.showProgressDlg(getActivity(), "");---------无
		// NumRequest req = new NumRequest();
		// try {
		// req.userId = UserInfoManager.getUserInfo(getActivity()).id + "";
		//
		// } catch (Exception e) {
		// }
		// req.userType = "1";
		//
		// RequestParams params = new RequestParams();
		// try {
		// params.setBodyEntity(new StringEntity(req.toJson()));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		NumRequest req = new NumRequest();
		try {
			req.userId = UserInfoManager.getUserInfo(getActivity()).id + "";

		} catch (Exception e) {
		}
		req.userType = "1";

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						// T.showNetworkError(IntegralDivisionActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						NumResponse bean = new Gson().fromJson(resp.result,
								NumResponse.class);
						System.out.println("===产业链积分全部的:" + resp.result);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data.integralNew != null) {
								// tv_num.setText(Util.getnum(
								// bean.data.integralNew + "", false));
								// tv_num1.setText(Util.getnum(
								// bean.data.totalIntegralNew + "",
								// false));//累计积分
								long k =  Long.parseLong(bean.data.keepIntegral.toString().trim());
								long i =  Long.parseLong(bean.data.integralNew.toString().trim());
								long ki = k + i;
								String a = Util.getnum(ki + "", false);
								tv_num1.setText(a + "");// 累计积分

								daijili_text.setText(Util.getnum(
										bean.data.keepIntegral + "", false));// 待激励
								kejili_text.setText(Util.getnum(
										bean.data.integralNew + "", false));// 可激励

							}
							// lvData.setAdapter(new DataReportAdapter(activity,
							// bean.data));
						}
					}
				});
	}

	public void loadData(int integralType_) {
		// List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
		// for (int i = 0; i < 10; i++) {
		// ExcitationBean data = new ExcitationBean();
		// data.amount = "交易金额: 200元";
		// data.time = "结算时间: 2016-8-15";
		// data.num = "+30";
		// bean.add(data);
		// }
		// updateView(bean);
		IntegralRquest req = new IntegralRquest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).id + "";
		req.userType = "1";
		req.operbType = "";
		req.opersType = "";
		req.startDate = "";
		req.endDate = "";
		req.integralType = integralType_ + "";// 0可1待
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
			System.out.println("====秀儿积分分:"+req.toJson());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.INTEGRALLISTNEW, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						lvData.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						Log.e("", "" + resp.result);
						System.out.println("=====产业链积分,返回数据:" + resp.result);
						lvData.onRefreshComplete();
						IntegralResponse bean = new Gson().fromJson(
								resp.result, IntegralResponse.class);
						if (Api.SUCCEED == bean.result_code)

							if (bean.data != null) {
								updateView(bean.data);
							}
					}
				});

	}

	public void updateView(List<IntegralBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new IntegralAdapter(activity, data);
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
		loadData(integralType_num);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(integralType_num);
	}

}
