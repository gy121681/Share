package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
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
import com.shareshenghuo.app.shop.adapter.IncentivePoints1Adapter;
import com.shareshenghuo.app.shop.adapter.IncentivePointsAdapter;
import com.shareshenghuo.app.shop.fragment.BaseFragment1;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.network.request.FilialobeRequest;
import com.shareshenghuo.app.shop.network.response.FilialobeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.widget.MyTabView;

/**
 * @author hang
 * 秀点
 */
public class IncentivePointsActivityFm1 extends BaseFragment1 implements OnRefreshListener2<ListView>{
	
	private MyTabView tabView;
	private TextView tv_title,tv1,tv2,tv3,tv4;
	private   List<View> viewList;
	private ViewPagerAdapter pageradapter;
	private ViewPager pagers;
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private IncentivePoints1Adapter adapter;
	private int  pages = 0;
	
	private PullToRefreshListView lvData1;

	private int pageNo1 = 1;
	private int pageSize1 = 10;
	private IncentivePointsAdapter adapter1;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_incentivepoints;
	}
	
	@Override
	protected void init(View rootView) {
		initView();
	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_incentivepoints);
//		initView();
//	}
	
	public void initView() {
//		initTopBar("可兑换秀点");
		
		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
		tv3 = getView(R.id.tv3);
		tv4 = getView(R.id.tv4);
		pagers = getView(R.id.viewpager);
		LayoutInflater inflater=getActivity().getLayoutInflater();  
		View view1 = inflater.inflate(R.layout.incentivepoints_fragment, null);  
        View view2 = inflater.inflate(R.layout.incentivepoints_fragment,null);  
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        pageradapter = new ViewPagerAdapter(viewList);
        pagers.setAdapter(pageradapter);
        
		lvData = (PullToRefreshListView) view1.findViewById(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		
		lvData1 = (PullToRefreshListView) view2.findViewById(R.id.lvShop);
		lvData1.setMode(Mode.BOTH);
		lvData1.setOnRefreshListener(this);
        
		loadData();
		loadData1();
		  tv1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tv1.setTextColor(getResources().getColor(R.color.black));
					tv3.setTextColor(getResources().getColor(R.color.black));
					tv2.setVisibility(View.VISIBLE);
					tv4.setVisibility(View.GONE);
					pagers.setCurrentItem(0);
				}
			});
	        tv3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tv1.setTextColor(getResources().getColor(R.color.black));
					tv3.setTextColor(getResources().getColor(R.color.black));
					tv4.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.GONE);
					pagers.setCurrentItem(1);
				}
			});
	        
	        pagers.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					if(arg0==0){
						pages = arg0;
						tv1.setTextColor(getResources().getColor(R.color.black));
						tv3.setTextColor(getResources().getColor(R.color.black));
						tv2.setVisibility(View.VISIBLE);
						tv4.setVisibility(View.GONE);
					}else{
						pages = arg0;
						
						tv1.setTextColor(getResources().getColor(R.color.black));
						tv3.setTextColor(getResources().getColor(R.color.black));
						tv4.setVisibility(View.VISIBLE);
						tv2.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		
//		tabView =getView(R.id.tabFavorites);
//		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		map.put("激励秀点", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("推荐秀点", null);
//		titles.add(map);
////		map = new HashMap<String, Integer>();
////		map.put("获赠秀点", null);
////		titles.add(map);
//		
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		fragments.add(new IncentivePoints1Fragment());
//		fragments.add(new IncentivePointsFragment());
////		fragments.add(new IncentivePoints2Fragment());
//		
//		tabView.createView(titles, fragments, getActivity().getSupportFragmentManager());
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		if(pages ==0){
			pageNo = 1;
			loadData();
		}else{
			pageNo1 = 1;
			loadData1();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if(pages ==0){
			loadData();
		}else{
			loadData1();
		}
	}
	
	

	public void loadData() {
		
		FilialobeRequest req = new FilialobeRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.userType = "2";
		req.operbType = "";
		req.moneyType = "0";
		req.opersType = "01";
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
		new HttpUtils().send(HttpMethod.POST, Api.OBEDIENCELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FilialobeResponse bean = new Gson().fromJson(resp.result, FilialobeResponse.class);
				if(Api.SUCCEED == bean.result_code)
//					if(bean.data!=null){
						updateView(bean.data);
//					}
			}
		});
		
		
	}
	
	public void updateView(List<FilialobeBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new IncentivePoints1Adapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	
	
public void loadData1() {
		
		FilialobeRequest req = new FilialobeRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.userType = "2";
		req.operbType = "";
		req.opersType = "02";
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
		new HttpUtils().send(HttpMethod.POST, Api.OBEDIENCELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData1.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData1.onRefreshComplete();
				FilialobeResponse bean = new Gson().fromJson(resp.result, FilialobeResponse.class);
				if(Api.SUCCEED == bean.result_code){
						updateView1(bean.data);
				}
			}
		});

	}

	public void updateView1(List<FilialobeBean> data) {
		if(data!=null){
		if (pageNo1 == 1 || adapter1 == null) {
			if(getActivity()!=null&&data.size()>0){
				adapter1 = new IncentivePointsAdapter(activity, data);
				lvData1.setAdapter(adapter1);
			}
		}
		if (pageNo1 > 1) {
			adapter1.getmData().addAll(data);
			adapter1.notifyDataSetChanged();
		}
		pageNo1++;
		}
	}
	
	
	public class ViewPagerAdapter extends PagerAdapter{  
		  
	    List<View> viewLists;  
	      
	    public ViewPagerAdapter(List<View> lists)  
	    {  
	        viewLists = lists;  
	    }  
	  
	    @Override  
	    public int getCount() {  //获得size  
	        // TODO Auto-generated method stub  
	        return viewLists.size();  
	    }  
	  
	    @Override  
	    public boolean isViewFromObject(View arg0, Object arg1) {                           
	        // TODO Auto-generated method stub  
	        return arg0 == arg1;  
	    }  
	      
	    @Override  
	    public void destroyItem(View view, int position, Object object) //销毁Item  
	    {  
	        ((ViewPager) view).removeView(viewLists.get(position));  
	    }  
	      
	    @Override  
	    public Object instantiateItem(View view, int position)//实例化Item  
	    {  
	        ((ViewPager) view).addView(viewLists.get(position), 0);  
	          
	        return viewLists.get(position);  
	    }  
	      
	}  

}
