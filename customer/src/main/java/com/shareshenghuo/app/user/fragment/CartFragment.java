package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CartListAdapter;
import com.shareshenghuo.app.user.adapter.CartListAdapter.Mode;
import com.shareshenghuo.app.user.adapter.CartListAdapter.OnProdChangeCallback;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CartInfo;
import com.shareshenghuo.app.user.network.bean.OrderProdInfo;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.CartListRequest;
import com.shareshenghuo.app.user.network.response.CartListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
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
 * 购物车
 */
public class CartFragment extends BaseFragment
	implements OnClickListener, OnProdChangeCallback {
	
	private static final int REQ_SETTLE = 0x101;
	
	private View emptyView;
	private ListView lvData;
	private Button btnEdit;
	private Button btnSettle;
	private LinearLayout llAmount;
	private TextView tvAmount;
	
	private CartListAdapter adapter;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_cart;
	}

	@Override
	protected void init(View rootView) {
		emptyView = getView(R.id.viewEmpty);
		lvData = getView(R.id.lvData);
		btnEdit = getView(R.id.btnEdit);
		btnSettle = getView(R.id.btnSettle);
		llAmount = getView(R.id.llCartAmount);
		tvAmount = getView(R.id.tvCartAmount);
		
		adapter = new CartListAdapter(activity, new ArrayList<CartInfo>(), tvAmount);
		adapter.setOnProdChangeCallback(this);
		
		btnEdit.setOnClickListener(this);
		btnSettle.setOnClickListener(this);
		
		loadData();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
	}

	public void loadData() {
		ProgressDialogUtil.showProgressDlg(activity, "");
		CartListRequest req = new CartListRequest();
		req.user_id = UserInfoManager.getUserId(activity)+"";
		req.city_id = CityManager.getInstance(activity).getCityId()+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CART_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CartListResponse bean = new Gson().fromJson(resp.result, CartListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					tvAmount.setText("0.0");
					adapter.setmData(bean.data);
					lvData.setAdapter(adapter);
					if(bean.data.size()>0) {
						emptyView.setVisibility(View.GONE);
					} else {
						emptyView.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnEdit:
			if(adapter.getMode() == Mode.MODE_EDIT) {
				//当前处于编辑态，更新购物车
				adapter.commitModify();
			} else {
				//当前处于普通态，切换成编辑态
				adapter.setMode(Mode.MODE_EDIT);
				btnEdit.setText("完成");
				btnSettle.setText("删除");
				llAmount.setVisibility(View.GONE);
			}
			break;
			
		case R.id.btnSettle:
			if(adapter.getMode() == Mode.MODE_EDIT)
				delete();
			else
				settle();
			break;
		}
	}
	
	public void delete() {
		new AlertDialog.Builder(activity).setMessage("确定要删除吗？")
		.setNegativeButton("取消", null)
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				adapter.delete();
			}
		})
		.show();
	}
	
	private String[] items = {"外卖订单", "到店消费"};
	
	public void settle() {
		final List<ProdInfo> list = adapter.getCheckedProds();
		if(list.size() <= 0) {
			T.showShort(activity, "请选择商品");
			return;
		}
		
		new AlertDialog.Builder(activity).setTitle("请选择消费类型")
			.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int which) {
					List<OrderProdInfo> data = new ArrayList<OrderProdInfo>();
					for(ProdInfo item : list) {
						OrderProdInfo info = new OrderProdInfo();
						info.product_id = item.product_id;
						info.product_name = item.product_name;
						info.product_count = item.shop_car_count;
						info.per_price = item.default_new_price;
						info.thum_photo = item.thum_photo;
						info.product_format_id = item.format_id+"";
						info.child_format_id = item.child_format_id+"";
						data.add(info);
					}
					TransferTempDataUtil.getInstance().setData(data);
					
//					Intent it = new Intent(activity, SubmitOrderActivity.class);
//					it.putExtra("orderType", which+1);
//					it.putExtra("shopId", list.get(0).shop_id);
//					startActivityForResult(it, REQ_SETTLE);
				}
			})
			.show();
	}
	
	@Override
	public void onChanged(int operate) {
		if(operate == 1) {
			// 编辑完成
			adapter.setMode(Mode.MODE_NORMAL);
			btnEdit.setText("编辑");
			btnSettle.setText("结算");
			llAmount.setVisibility(View.VISIBLE);
			tvAmount.setText("0.0");
		} else 
			// 删除
			loadData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK && requestCode==REQ_SETTLE) {
			// 订单提交成功，清除购物车商品
			adapter.delete();
		}
	}
}
