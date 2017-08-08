package com.shareshenghuo.app.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.entity.StringEntity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.IncentivePointsActivityFm1.ViewPagerAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.QRcodeBean;
import com.shareshenghuo.app.shop.network.request.QrcodeRequest;
import com.shareshenghuo.app.shop.network.response.QrcodeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.widget.MyTabView;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class QrCodeFm2 extends BaseFragment{
	private MyTabView tabView;
	private TextView tv_title,tv1,tv2,tv3,tv4,tv5,tv6;
	private   List<View> viewList;
	private ViewPagerAdapter pageradapter;
	private TwoButtonDialog downloadDialog;
	private ViewPager pagers;
	private int pages;

	private ViewGroup mLayoutType1;
	private ViewGroup mLayoutType2;
	private ViewGroup mLayoutType3;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.qr_code_fm;
	}

	@Override
	protected void init(View rootView) {
		initView();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		QrcodeRequest req = new QrcodeRequest();
		req.shopId = UserInfoManager.getUserInfo(activity).shop_id + "";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GENERCODELIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// T.showNetworkError(getApplicationContext());
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						QrcodeResponse bean = new Gson().fromJson(resp.result,
								QrcodeResponse.class);
						Log.e("", "" + resp.result);
						if (Api.SUCCEED == bean.result_code) {
							if(bean.data.size()>0){
								updateView(bean.data);
							}
						}
					}

				});
	}

	private void updateView(List<QRcodeBean> data) {
		// TODO Auto-generated method stub
		
		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
		tv3 = getView(R.id.tv3);
		tv4 = getView(R.id.tv4);
		tv5 = getView(R.id.tv5);
		tv6 = getView(R.id.tv6);
		pagers = getView(R.id.viewpager);
		LayoutInflater inflater=getActivity().getLayoutInflater();  
		View view1 = inflater.inflate(R.layout.qrcode_layout1, null);  
        View view2 = inflater.inflate(R.layout.qrcode_layout1,null);  
        View view3 = inflater.inflate(R.layout.qrcode_layout1,null);  
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        viewList.add(view3);  
        pageradapter = new ViewPagerAdapter(viewList);
        pagers.setAdapter(pageradapter);
		
		final ImageView ivQR =(ImageView) view1.findViewById(R.id.ivQR);
		final ImageView ivQR1 =(ImageView) view2.findViewById(R.id.ivQR);
		final ImageView ivQR2 =(ImageView) view3.findViewById(R.id.ivQR);
		
		TextView tv_save = (TextView) view1.findViewById(R.id.tv_save);
		TextView tv_save1 = (TextView) view2.findViewById(R.id.tv_save);
		TextView tv_save2 = (TextView) view3.findViewById(R.id.tv_save);
		tv_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				savebitmap(ivQR);
				
			}
		});
		tv_save1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				savebitmap(ivQR1);
			}
		});
		tv_save2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				savebitmap(ivQR2);
				
			}
		});
		
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pagers.setCurrentItem(0);
			}
		});
		tv3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pagers.setCurrentItem(1);
			}
		});
		tv5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pagers.setCurrentItem(2);
			}
		});
		
		
		List<Map<String, Integer>> titles = new ArrayList<Map<String, Integer>>();
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (data.get(i).discountType.equals("3")) {
				
				FinalBitmap.create(this.getActivity()).display(ivQR,
						data.get(i).returnUrl,
						ivQR.getWidth(),
						ivQR.getHeight(), null, null);
//                mLayoutType1.setVisibility(View.VISIBLE);
//				Bundle bundle1 = new Bundle();
//				bundle1.putString("url", data.get(i).returnUrl);
//				QrcodeFm fm = new QrcodeFm();
//				fm.setArguments(bundle1);
//				fragments.add(fm);
//				map.put("100%积分", null);
//				titles.add(map);
			} else if (data.get(i).discountType.equals("2")) {
				FinalBitmap.create(this.getActivity()).display(ivQR1,
						data.get(i).returnUrl,
						ivQR.getWidth(),
						ivQR.getHeight(), null, null);
//                mLayoutType2.setVisibility(View.VISIBLE);
//				Bundle bundle1 = new Bundle();
//				bundle1.putString("url", data.get(i).returnUrl);
//				QrcodeFm fm = new QrcodeFm();
//				fm.setArguments(bundle1);
//				fragments.add(fm);
//				map.put("50%积分", null);
				titles.add(map);
			} else if (data.get(i).discountType.equals("1")) {
				FinalBitmap.create(this.getActivity()).display(ivQR2,
						data.get(i).returnUrl,
						ivQR.getWidth(),
						ivQR.getHeight(), null, null);
//                mLayoutType3.setVisibility(View.VISIBLE);
//				Bundle bundle1 = new Bundle();
//				bundle1.putString("url", data.get(i).returnUrl);
//				QrcodeFm fm = new QrcodeFm();
//				fm.setArguments(bundle1);
//				fragments.add(fm);
//				map.put("25%积分", null);
//				titles.add(map);
			}
		}
//		tabView.createView(titles, fragments, activity.getSupportFragmentManager());
		
		
		 pagers.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					if(arg0==0){
						pages = arg0;
						tv1.setTextColor(getResources().getColor(R.color.text_orange));
						tv3.setTextColor(getResources().getColor(R.color.text_black_light));
						tv5.setTextColor(getResources().getColor(R.color.text_black_light));
						tv2.setVisibility(View.VISIBLE);
						tv4.setVisibility(View.GONE);
						tv6.setVisibility(View.GONE);
					}else if(arg0==1){
						pages = arg0;
						tv1.setTextColor(getResources().getColor(R.color.text_black_light));
						tv3.setTextColor(getResources().getColor(R.color.text_orange));
						tv5.setTextColor(getResources().getColor(R.color.text_black_light));
						tv2.setVisibility(View.GONE);
						tv4.setVisibility(View.VISIBLE);
						tv6.setVisibility(View.GONE);
					}else if(arg0==2){
						pages = arg0;
						tv1.setTextColor(getResources().getColor(R.color.text_black_light));
						tv3.setTextColor(getResources().getColor(R.color.text_black_light));
						tv5.setTextColor(getResources().getColor(R.color.text_orange));
						tv2.setVisibility(View.GONE);
						tv4.setVisibility(View.GONE);
						tv6.setVisibility(View.VISIBLE);
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

	}
	
	
	public void savebitmap(ImageView img){
		
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		
		Bitmap bitmap = null;
		
		 bitmap = convertViewToBitmap(img);
		
		FileOutputStream m_fileOutPutStream = null;
		String filepath = Environment.getExternalStorageDirectory() +"/share/"//+File.separator
				+ getStringDateMerge()+"mypayqrcode.png";
		try {
			m_fileOutPutStream = new FileOutputStream(filepath);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		obmp = BitmapFactory.decodeFile(filepath, newOpts);
		bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
		
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.setphotopath(activity, filepath);
//		MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", "description");
		
		downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
				"提示", "保存成功,可在相册查看", "确定", "",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		
	}
	public  Bitmap convertViewToBitmap(View view)  
	{  
	    view.buildDrawingCache();  
	    Bitmap bitmap = view.getDrawingCache();  
	  
	    return bitmap;  
	}
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public void initView() {
		tabView = getView(R.id.tabFavorites);

		mLayoutType1 = getView(R.id.layout_type_1);
		mLayoutType2 = getView(R.id.layout_type_2);
		mLayoutType3 = getView(R.id.layout_type_3);
		initdata();

		// Map<String,Integer> map = new HashMap<String, Integer>();
		// map.put("100%积分", null);
		// titles.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("25%积分", null);
		// titles.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("5%积分", null);
		// titles.add(map);

		// fragments.add(new Qrcode1Fm());
		// fragments.add(new Qrcode2Fm());

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
