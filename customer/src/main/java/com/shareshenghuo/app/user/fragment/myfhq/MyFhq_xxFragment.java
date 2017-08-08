package com.shareshenghuo.app.user.fragment.myfhq;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.myfhq.Myfhq_xx_Adapter;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.myfhq.FilialInvestmentBean;
import com.shareshenghuo.app.user.network.request.FractionRequest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.network.response.myfhq.FilialInvestmentResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

/**
 * 秀心分红权
 */
public class MyFhq_xxFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {
	private TextView zs_xx_and_xf_btn;// 投资总秀心
	private PullToRefreshListView lvData;

	private int pageNo = 1;
	private int pageSize = 10;
	private Myfhq_xx_Adapter adapter;

	private TextView tv_fhqbl;// 分红权比例

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
	protected int getLayoutId() {
		return R.layout.myfhq_fragment;
	}

	@Override
	protected void init(View rootView) {
		initView();
		loadData();
	}

	protected void initView() {
		zs_xx_and_xf_btn = getView(R.id.zs_xx_and_xf_btn);
		zs_xx_and_xf_btn.setTag("投资总秀心：");

		tv_fhqbl = getView(R.id.tv_fhqbl);
		tv_fhqbl.setTag("分红权比例：");

		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		// getStatisticsData();
	}

	public void loadData() {

		// AjaxParams params=new AjaxParams();
		// params.put("userType", value)

		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).id + "";
		req.type = "";
		req.userType = "1";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";
		req.investmentType = "1";// 1,秀心投资流水

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETXX_XF_LIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						lvData.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						lvData.onRefreshComplete();
						System.out.println("=====秀心返回:" + resp.result);
						try {
							JSONObject j = new JSONObject(resp.result);
							JSONObject j2 = j.getJSONObject("data");
							System.out.println("=====j2:" + j2.toString());
							JSONArray jsonArray = j2
									.getJSONArray("filialInvestmentList");
							System.out.println("=====秀心返回jsonArray:"
									+ jsonArray.toString());

							Gson gson = new Gson();
							String result = jsonArray.toString();

							List<FilialInvestmentBean> list = new ArrayList<FilialInvestmentBean>();
							list = gson
									.fromJson(
											result,
											new TypeToken<List<FilialInvestmentBean>>() {
											}.getType());

							System.out.println("=====秀心返回daxiao1:"
									+ list.size());

							FilialInvestmentResponse bean = new FilialInvestmentResponse();
							bean.data = list;
							System.out.println("=====秀心返回daxiao2:"
									+ bean.data.size());
							tv_fhqbl.setText(tv_fhqbl.getTag().toString()
									+ j2.getString("filialExtremeRatio")+"/万");
							zs_xx_and_xf_btn.setText(zs_xx_and_xf_btn.getTag()
									.toString()
									+ j2.getString("filialInvestmentTotal"));
							if (Api.SUCCEED == bean.result_code)
								if (bean.data != null) {
									updateView(bean.data);
								}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getStatisticsData() {

		// ProgressDialogUtil.showProgressDlg(getActivity(), "");
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
						// T.showNetworkError(IntegralDivisionActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						NumResponse bean = new Gson().fromJson(resp.result,
								NumResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							// tv_num.setText("累计秀心数  "+bean.data.totalFilialPiety+"");
							zs_xx_and_xf_btn.setText("当前秀心数  "
									+ bean.data.filialPiety + "");
							// lvData.setAdapter(new DataReportAdapter(activity,
							// bean.data));
						}
					}
				});
	}

	public void updateView(List<FilialInvestmentBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new Myfhq_xx_Adapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
}
