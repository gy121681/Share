package com.shareshenghuo.app.shop.fragment.myfhq;

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

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.myfhq.Myfhq_xf_Adapter;
import com.shareshenghuo.app.shop.fragment.BaseFragment;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.myfhq.FilialInvestmentBean;
import com.shareshenghuo.app.shop.network.request.FractionRequest;
import com.shareshenghuo.app.shop.network.response.myfhq.FilialInvestmentResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.Util;
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
 * 
 * @author wyp
 * @version 创建时间：2017-5-22 下午7:07:01 类说明：我的分红权_秀点
 */
public class MyFhq_xfFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {
	private TextView zs_xx_and_xf_btn;

	private PullToRefreshListView lvData;

	private int pageNo = 1;
	private int pageSize = 10;
	private Myfhq_xf_Adapter adapter;

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
		zs_xx_and_xf_btn.setTag("投资总秀点");

		tv_fhqbl = getView(R.id.tv_fhqbl);
		tv_fhqbl.setTag("分红权比例：");

		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
	}

	public void loadData() {

		// AjaxParams params=new AjaxParams();
		// params.put("userType", value)

		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id + "";
		req.type = "";
		req.userType = "2";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";
		req.investmentType = "2";// 1,秀点投资流水

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
						System.out.println("=====秀点返回:" + resp.result);
						try {
							JSONObject j = new JSONObject(resp.result);
							JSONObject j2 = j.getJSONObject("data");
							System.out.println("=====j2:" + j2.toString());
							JSONArray jsonArray = j2
									.getJSONArray("userMoneyInvestmentList");
							System.out.println("=====秀点返回jsonArray:"
									+ jsonArray.toString());

							Gson gson = new Gson();
							String result = jsonArray.toString();

							List<FilialInvestmentBean> list = new ArrayList<FilialInvestmentBean>();
							list = gson
									.fromJson(
											result,
											new TypeToken<List<FilialInvestmentBean>>() {
											}.getType());

							System.out.println("=====秀点返回daxiao1:"
									+ list.size());

							FilialInvestmentResponse bean = new FilialInvestmentResponse();
							bean.data = list;
							System.out.println("=====秀点返回daxiao2:"
									+ bean.data.size());
							tv_fhqbl.setText(tv_fhqbl.getTag().toString()
									+ j2.getString("userMoneyExtremeRatio")
									+ "/万");
							String userMoneyInvestmentTotal_long = Util.getfotmatnum(
									j2.getString("userMoneyInvestmentTotal"),
									true, 1);
							zs_xx_and_xf_btn
									.setText(zs_xx_and_xf_btn.getTag()
											.toString()
											+ userMoneyInvestmentTotal_long);
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

	public void updateView(List<FilialInvestmentBean> data) {
		if (pageNo == 1 || adapter == null) {
			System.out.println("===秀点" + data.size());
			adapter = new Myfhq_xf_Adapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
}