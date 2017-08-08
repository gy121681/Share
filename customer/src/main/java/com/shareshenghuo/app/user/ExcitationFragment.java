package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ExcitationAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FractionBean;
import com.shareshenghuo.app.user.network.request.FractionRequest;
import com.shareshenghuo.app.user.network.response.FractionResponse;
import com.shareshenghuo.app.user.networkapi.Api;
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
 * 收藏的商家
 */
public class ExcitationFragment extends BaseTopActivity implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private ExcitationAdapter adapter;
	private TextView tv_title,tv_num,tv_title1,tv_num1;
	private String filialPiety,totalFilialPiety;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excitation_fragment);
        
        filialPiety = getIntent().getStringExtra("filialPiety");
        totalFilialPiety = getIntent().getStringExtra("totalFilialPiety");
        initView();
        
        loadData();
    }

	protected void initView() {
		initTopBar("我的秀心");
    	tv_title = getView(R.id.tv_title);
    	tv_title1 = getView(R.id.tv_title1);
    
		tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
//		tv_num.setText(filialPiety);
//		tv_num1.setText(totalFilialPiety);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		tv_num.setText("累计秀心数  "+totalFilialPiety);
		tv_title.setText("当前秀心数  "+filialPiety);
	}
	
	
	public void loadData() {
		
		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		req.type = "";
		req.userType = "1";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FRACTIONLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FractionResponse bean = new Gson().fromJson(resp.result, FractionResponse.class);
				if(Api.SUCCEED == bean.result_code)
					Log.e("", " - - - -  "+resp.result.toString());
					updateView(bean.data);
			}
		});
		
	}
	
	public void updateView(List<FractionBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ExcitationAdapter(this, data);
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
}
