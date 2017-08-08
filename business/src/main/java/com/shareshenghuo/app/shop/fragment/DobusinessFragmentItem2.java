package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.widget.MyOnclicListener;
import com.example.widget.SelectBirthday;
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
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.TodaydataActivity;
import com.shareshenghuo.app.shop.adapter.BusinessAdapter;
import com.shareshenghuo.app.shop.adapter.HistoryrecordAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.BusinessBeans;
import com.shareshenghuo.app.shop.network.bean.HistoryDataBeans;
import com.shareshenghuo.app.shop.network.request.BusinesstodayRequest;
import com.shareshenghuo.app.shop.network.response.BusinesstodayResponse;
import com.shareshenghuo.app.shop.network.response.HistoryDataResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.widget.MyTabView1;

public class DobusinessFragmentItem2 extends BaseFragment implements  OnRefreshListener2<ListView>{
	
	
	private int pageNo = 1;
	private int pageSize = 10;
	private int pageNo1 = 1;
	private int pageSize1 = 10;
	private RadioButton btn_1,btn_2;
	private BusinessAdapter adapter;
	private PullToRefreshListView lvData;
	private TextView tv_totalbusiness,tv_totalbusiness1;
	private int pages = 0;
	
	
	private MyTabView1 tabView;
	private TextView tv_title,tv1,tv2,tv3,tv4;
	private TextView tv_choosedate;
	private   List<View> viewList;
	private ViewPagerAdapter pageradapter;
	private ViewPager pagers;
	
	
	private HistoryrecordAdapter adapter1;
	private PullToRefreshListView lvData1;
	private TextView tv_time;
	private SelectBirthday birth;
	private String initstartdate = "";
	private List<HistoryDataBeans> mlist = new ArrayList<HistoryDataBeans>();
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.dobusiness_fragment_item2;
	}
	
	@Override
	protected void init(View rootView) {
		
		initview();
	}
	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.dobusiness_fragment_item1);
//		
//		initview();
//	}

	private void initview() {
		// TODO Auto-generated method stub
		pagers = getView(R.id.viewpager);
		tabView = getView(R.id.tabFavorites);
		tv_choosedate = getView(R.id.tv_choosedate);
		tv_choosedate.setVisibility(View.INVISIBLE);

		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
		tv3 = getView(R.id.tv3);
		tv4 = getView(R.id.tv4);
		LayoutInflater inflater=getActivity().getLayoutInflater();  
		View view1 = inflater.inflate(R.layout.data_list_fragment1, null);  
        View view2 = inflater.inflate(R.layout.data_list_fragment1,null);  
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        pageradapter = new ViewPagerAdapter(viewList);
        pagers.setAdapter(pageradapter);
        
		tv_totalbusiness = (TextView) view1.findViewById(R.id.tv_totalbusiness);
		tv_totalbusiness.setText("今日营业额:");
		
		tv_totalbusiness1 = (TextView) view2.findViewById(R.id.tv_totalbusiness);
		tv_totalbusiness1.setText("历史营业额:");
		
		
		lvData =(PullToRefreshListView) view1.findViewById(R.id.lvdata);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		
		
		initstartdate = DateUtil.gettosevenday1();
		pageNo1 = 1;
		lvData1 = (PullToRefreshListView) view2.findViewById(R.id.lvdata);
		lvData1.setMode(Mode.BOTH);
		lvData1.setOnRefreshListener(this);
		tv_time = (TextView) view2.findViewById(R.id.tv_time);
		tv_time.setText(DateUtil.gettosevenday1()+"至今");
		loadData();
		loadData1();
		tv_choosedate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setdate();
			}
		});
		
		lvData1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(),TodaydataActivity.class);
//				Log.e("", ""+mlist.get(arg2-1).payDate);
//				Log.e("", ""+mlist.get(arg2).payDate);
//				Log.e("", ""+mlist.get((int)arg3).payDate);
				it.putExtra("dates", mlist.get((int)arg3).payDate+"");
				startActivity(it);
			}
		});
        tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv1.setTextColor(getResources().getColor(R.color.text_orange));
				tv3.setTextColor(getResources().getColor(R.color.text_black));
				tv2.setVisibility(View.VISIBLE);
				tv4.setVisibility(View.GONE);
				tv_choosedate.setVisibility(View.INVISIBLE);
				pagers.setCurrentItem(0);
			}
		});
        tv3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv1.setTextColor(getResources().getColor(R.color.text_black));
				tv3.setTextColor(getResources().getColor(R.color.text_orange));
				tv4.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.GONE);
				tv_choosedate.setVisibility(View.VISIBLE);
				pagers.setCurrentItem(1);
			}
		});
        
        pagers.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0){
					pages = arg0;
					tv1.setTextColor(getResources().getColor(R.color.text_orange));
					tv3.setTextColor(getResources().getColor(R.color.text_black));
					tv2.setVisibility(View.VISIBLE);
					tv4.setVisibility(View.GONE);
					tv_choosedate.setVisibility(View.INVISIBLE);
				}else{
					pages = arg0;
					
					tv1.setTextColor(getResources().getColor(R.color.text_black));
					tv3.setTextColor(getResources().getColor(R.color.text_orange));
					tv4.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.GONE);
					tv_choosedate.setVisibility(View.VISIBLE);
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
//		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		map.put("今日营业额",null);
////		map.put("激励积分", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("历史营业额",null);
////		map.put("共享积分", null);
//		titles.add(map);
//		
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		fragments.add(new TodaydataFragment1());
//		fragments.add(new HistoryDataFragment2());
////		fragments.add(new IncentivePoints2Fragment());
////		
//		tabView.createView(titles, fragments, getActivity().getSupportFragmentManager());
//		
//		tabView.setPageChangeListener(new PageChangeListener() {
//			
//			@Override
//			public void onPageChanged(int index, String tabTitle) {
//				// TODO Auto-generated method stub
//				if(index==1){
//					tv_choosedate.setVisibility(View.VISIBLE);
//				}else{
//					tv_choosedate.setVisibility(View.GONE);
//				}
//			}
//		});
//		
//		tv_choosedate.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				HistoryDataFragment2.context.setdate();
//			}
//		});
//		
//	}
	
	
//	PagerAdapter pagerAdapter = new PagerAdapter() {  
//		
//		
//        @Override  
//        public boolean isViewFromObject(View arg0, Object arg1) {  
//            // TODO Auto-generated method stub  
//            return arg0 == arg1;  
//        }  
//          
//        @Override  
//        public int getCount() {  
//            // TODO Auto-generated method stub  
//            return viewList.size();  
//        }  
//          
//        @Override  
//        public void destroyItem(ViewGroup container, int position,  
//                Object object) {  
//            // TODO Auto-generated method stub  
//            container.removeView(viewList.get(position));  
//        }  
//          
//        @Override  
//        public Object instantiateItem(ViewGroup container, int position) {  
//            // TODO Auto-generated method stub  
//            container.addView(viewList.get(position));  
//              
//              
//            return viewList.get(position);  
//        }  
//    	};  
//    	
//    	pagers.setAdapter(pagerAdapter);  
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
	
	
	
	public void loadData() {
//		List<BusinessBean> bean = new ArrayList<BusinessBean>();
//		for (int i = 0; i < 10; i++) {
//			BusinessBean data = new BusinessBean();
//			data.amont = "+20000";
//			data.time = "16:22:45";
//			data.oder = "12223665488";
//			data.name  = "哈哈哈";
//			bean.add(data);
//		}
//		updateView(bean);
		
		BusinesstodayRequest req = new BusinesstodayRequest();
		req.shopId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.payDate = DateUtil.getCDate();
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FINDSHOPPAYCLSINFLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
//				T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				BusinesstodayResponse bean = new Gson().fromJson(resp.result, BusinesstodayResponse.class);
				if(Api.SUCCEED == bean.result_code){
					updateView(bean.data.list);
				tv_totalbusiness.setText("今日营业额:"+Util.getnum(bean.data.dayShopSumMoney, true));
				}
			}
		});
		
	}
	
	
	public void updateView(List<BusinessBeans> data) {
		
		if(data!=null&&data.size()>0){
		if(pageNo==1 || adapter==null) {
			adapter = new BusinessAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if(pages ==0){
			loadData();
		}else{
			loadData1();
		}
	}
	
	public void loadData1() {
		BusinesstodayRequest req = new BusinesstodayRequest();
		req.shopId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.payDate = "";
		req.startDate = initstartdate.replace("-", "");
		req.endDate = DateUtil.getCDate();
		req.pageNo = pageNo1+"";
		req.pageSize = pageSize+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.FINDDAYPAYLIST);
		new HttpUtils().send(HttpMethod.POST, Api.FINDDAYPAYLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData1.onRefreshComplete();
//				T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData1.onRefreshComplete();
				HistoryDataResponse bean = new Gson().fromJson(resp.result, HistoryDataResponse.class);
				if(Api.SUCCEED == bean.result_code){
				updateView1(bean.data.list);
				
				tv_totalbusiness1.setText("历史营业额: "+Util.getnum(bean.data.shopSumMoney, true));
				}
			}
		});

	}


	public void updateView1(List<HistoryDataBeans> data) {
		
		
//		if(data!=null&&data.size()>0){
		if (pageNo1 == 1 || adapter1 == null) {
			adapter1 = new HistoryrecordAdapter(getActivity(), data);
			lvData1.setAdapter(adapter1);
			mlist.clear();
			mlist.addAll(data);
		}
		if (pageNo1 > 1) {
			adapter1.getmData().addAll(data);
			adapter1.notifyDataSetChanged();
			mlist.addAll(data);
		}
		pageNo1++;
//		}else{
//			
//		}
	}
	
	public void setdate(){
//		if(getActivity()==null){
//			return;
//		}
		birth = new SelectBirthday(getActivity(),initstartdate,new MyOnclicListener() {

			public void MyOnclicListener(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.submit:
					initstartdate = birth.getdata();
					tv_time.setText(initstartdate+"至今");
//					Toast.makeText(getActivity().getApplicationContext(), initstartdate, Toast.LENGTH_SHORT).show();
					birth.dismiss();
					
					onPullDownToRefresh(lvData1);
//					pageNo = 1;
//					loadData();
					break;
				case R.id.cancel:
					birth.dismiss();
					break;
				default:
					break;
				}
			}
		});
		birth.showAtLocation(getActivity().findViewById(R.id.root),
				Gravity.BOTTOM, 0, 0);
	}
      
}
