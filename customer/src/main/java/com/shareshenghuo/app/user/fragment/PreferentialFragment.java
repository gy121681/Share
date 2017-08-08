package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.ActivTypeActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ActivListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ActivInfo;
import com.shareshenghuo.app.user.network.request.AreaListRequest;
import com.shareshenghuo.app.user.network.request.CityActivListRequest;
import com.shareshenghuo.app.user.network.response.ActivListResponse;
import com.shareshenghuo.app.user.network.response.AreaListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
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

/**
 * @author hang
 *	全城特惠
 */
public class PreferentialFragment extends BaseFragment
	implements OnClickListener, OnRefreshListener2<ListView> {

	private static final int REQ_PICK_TYPE = 0x101;
	
	private PullToRefreshListView lvData;
	
	private CityManager cityManager;
	
	private ActivListAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 15;
	
	private int active_type = -1;		//活动分类（可选
	private int area_id = -1;			//区域（可选
	private int order_type = 0;			//排序 1距离 2好评 3人气 4关注
	private int is_authentication = -1;	//是否认证 （可选）
	private int is_integrity = -1;		//是否诚信商家 （可选
	private int is_muslim = -1;			//是否清真 （可选
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_preferential;
	}

	@Override
	protected void init(View rootView) {
		initView();
		onPullDownToRefresh(lvData);	
	}

	public void initView() {
		lvData = (PullToRefreshListView) rootView.findViewById(R.id.lvActiv);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		rootView.findViewById(R.id.btnCategory).setOnClickListener(this);
		rootView.findViewById(R.id.btnArea).setOnClickListener(this);
		rootView.findViewById(R.id.btnSort).setOnClickListener(this);
		rootView.findViewById(R.id.btnFilter).setOnClickListener(this);
		
		cityManager = CityManager.getInstance(activity);
	}
	
	public void loadData() {
		CityActivListRequest req = new CityActivListRequest();
		req.user_id = UserInfoManager.getUserId(activity)+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.city_id = cityManager.getCityId()+"";
		req.latitude = cityManager.latitude+"";
		req.longitude = cityManager.longitude+"";
		req.order_type = order_type+"";
		if(is_authentication != -1)
			req.is_authentication = is_authentication+"";
		if(is_integrity != -1)
			req.is_integrity = is_integrity+"";
		if(is_muslim != -1)
			req.is_muslim = is_muslim+"";
		if(active_type != -1)
			req.active_type = active_type+"";
		if(area_id != -1)
			req.area_id = area_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CITY_ACTIV_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				if(activity != null)
					T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ActivListResponse bean = new Gson().fromJson(resp.result, ActivListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}
		});
	}
	
	public void updateView(List<ActivInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ActivListAdapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnCategory:
			startActivityForResult(new Intent(activity, ActivTypeActivity.class), REQ_PICK_TYPE);
			break;
			
		case R.id.btnArea:
			showAreaDlg();
			break;
			
		case R.id.btnSort:
			showSortDlg();
			break;
			
		case R.id.btnFilter:
			showFilterDlg();
			break;
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
	
	private int areaIndex = -1;
	
	public void showAreaDlg() {
		ProgressDialogUtil.showProgressDlg(activity, "加载数据");
		AreaListRequest req = new AreaListRequest();
		req.city_id = cityManager.getCityId()+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_AREA_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				if(activity != null)
					T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(activity == null)
					return;
				
				final AreaListResponse bean = new Gson().fromJson(resp.result, AreaListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data==null || bean.data.size()<=0) {
						T.showShort(activity, "暂无可选区域");
						return;
					}
					
					String[] areas = new String[bean.data.size()];
					for(int i=0; i<bean.data.size(); i++)
						areas[i] = bean.data.get(i).area_name;
					
					new AlertDialog.Builder(activity).setTitle("区域选择")
						.setSingleChoiceItems(areas, areaIndex, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int which) {
								areaIndex = which;
							}
						})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if(areaIndex>-1) {
									area_id = bean.data.get(areaIndex).id;
									onPullDownToRefresh(null);
								}
							}
						})
						.setNegativeButton("不限", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								area_id = -1;
								onPullDownToRefresh(null);
							}
						})
						.show();
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}
		});
	}
	
	private int sortIndex = 1;
	
	public void showSortDlg() {
		String[] items = {"按距离", "发布时间", "人气", "关注"};
		new AlertDialog.Builder(activity).setTitle("智能排序")
		.setSingleChoiceItems(items, sortIndex-1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int which) {
				sortIndex = which+1;
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				order_type = sortIndex;
				onPullDownToRefresh(null);
			}
		})
		.setNegativeButton("默认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				order_type = 0;
				onPullDownToRefresh(null);
			}
		})
		.show();
	}
	
	private boolean[] filterChecked = {false, false, false};
	
	public void showFilterDlg() {
		String[] items = {"清真", "诚信商家", "认证商家"};
		new AlertDialog.Builder(activity).setTitle("筛选(勾选为是/未勾选为否)")
		.setMultiChoiceItems(items, filterChecked, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				filterChecked[which] = isChecked;
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				is_muslim = filterChecked[0]? 1 : 0;
				is_integrity = filterChecked[1]? 1 : 0;
				is_authentication = filterChecked[2]? 1 : 0;
				onPullDownToRefresh(null);
			}
		})
		.setNegativeButton("不限", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				is_muslim = -1;
				is_integrity = -1;
				is_authentication = -1;
				onPullDownToRefresh(null);	
			}
		})
		.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK && requestCode==REQ_PICK_TYPE) {
			active_type = data.getIntExtra("typeId", 0);
			onPullDownToRefresh(null);
		}
	}
}
