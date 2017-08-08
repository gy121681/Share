package com.shareshenghuo.app.user;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.city.CityPicker;
import com.shareshenghuo.app.user.city.onChoiceCytyChilListener;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.BankListBean;
import com.shareshenghuo.app.user.network.request.AutRequset;
import com.shareshenghuo.app.user.network.request.CityRequest;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.BanklListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.IdCard;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RealnameActivity extends BaseTopActivity implements OnClickListener {

//	private WheelView mViewProvince;
//	private WheelView mViewCity;
//	private WheelView mViewDistrict;
//	private Button mBtnConfirm;
	//所有省
	protected String[] mProvinceDatas;
	//key - 省 value - 市
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	//key - 市 values - 区
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	//key - 区 values - 邮编
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 
	//当前省的名称
	protected String mCurrentProviceName;
	//当前市的名称
	protected String mCurrentCityName;
	// 当前区的名称
	protected String mCurrentDistrictName ="";
	//当前区的邮政编码
	protected String mCurrentZipCode ="";
	
	private EditText eibankcardnum,eidcard,edname;
	
	private LinearLayout lin_cascade;

	private boolean addrtag =  false;
	
	private TextView edcome,eibankcard,tv_finishs,tv_conmmit;
	
	private String[] c;
	private int[] d;
	private String[] b;
	private CityPicker citypicker;
	private String bankProvinceid,bankCityid,bankareid;
	private BanklListResponse bean;
	private String banknum = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realname);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		edcome = getView(R.id.edcome);
		citypicker= getView(R.id.citypicker);
		eibankcardnum = getView(R.id.eibankcardnum);
		eibankcard = getView(R.id.eibankcard);
		eidcard = getView(R.id.eidcard);
		edname = getView(R.id.edname);
		lin_cascade = getView(R.id.lin_cascade);
		tv_conmmit = getView(R.id.tv_comit);
		tv_finishs = getView(R.id.tv_finishs);
		edcome.setOnClickListener(this);
		eibankcard.setOnClickListener(this);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				authentication();
			}

		});
		
		if(bean==null){
			initbank();
		}
		
		citypicker.getcity(new onChoiceCytyChilListener() {
			
			@Override
			public void onClick(String i, String v, String a, String ni, String vi,
					String ai) {
				// TODO Auto-generated method stub
				bankProvinceid = i;
				bankCityid = v;
				bankareid = a;
				try {
					edcome.setText(ni+vi+ai);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		tv_finishs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lin_cascade.setVisibility(View.INVISIBLE);
				
			}
		});
		
		tv_conmmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lin_cascade.setVisibility(View.INVISIBLE);
				citypicker.setfirstdata();
			}
		});
		
	}
	
	private void authentication() {
		
		
		if(ViewUtil.checkEditEmpty(edname, "请填写真实姓名")){
			return;
		}
			
		if(ViewUtil.checkEditEmpty(eidcard, "请填身份证号")){
			return;
		}
			
		if(new IdCard().isValidatedAllIdcard(eidcard.getText().toString())){
			
		}else{
			T.showShort(RealnameActivity.this, "身份证有误");
//			eidcard.setError("");
			return;
		}
		if(banknum.equals("")){
			eibankcard.setError("");
			return;
		}
		if(ViewUtil.checkEditEmpty(eibankcardnum, "请填写银行卡号"))
			return;
		
		if(eibankcardnum.getText().toString().length()<16){
			T.showShort(RealnameActivity.this, "银行卡至少16位");
			return;
		}
		
		if(bankareid==null||bankareid.equals("")){
			edcome.setError("");
			return;
		}
		

		
//		if(!TextUtils.isEmpty(edname.getText())){
//			
//		}
		
		
		
		// TODO Auto-generated method stub
		AutRequset req = new AutRequset();
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		req.realName = edname.getText().toString();
		req.personNo = eidcard.getText().toString();
		req.bankName = banknum;
		req.cardNo =	eibankcardnum.getText().toString();
		req.provinceCode = bankProvinceid;
		req.cityCode = bankCityid;
		req.areaCode = bankareid;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.FICATION);
		new HttpUtils().send(HttpMethod.POST, Api.FICATION, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				Log.e("", " - - - - - - "+resp.result);
				if(Api.SUCCEED == bean.result_code){
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
						finish();
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}
			}
		});
	}

//	private void initaddressdata() {
//		// TODO Auto-generated method stub
//		mViewProvince = (WheelView) findViewById(R.id.id_province);
//		mViewCity = (WheelView) findViewById(R.id.id_city);
//		mViewDistrict = (WheelView) findViewById(R.id.id_district);
//		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
//		setUpListener();
//		setUpData();
//	}
//	private void setUpListener() {
//    	// 添加change事件
//    	mViewProvince.addChangingListener(this);
//    	// 添加change事件
//    	mViewCity.addChangingListener(this);
//    	// 添加change事件
//    	mViewDistrict.addChangingListener(this);
//    	// 添加onclick事件
//    	mBtnConfirm.setOnClickListener(this);
//    }
//	
//	private void setUpData() {
//		initProvinceDatas();
//		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(RealnameActivity.this, mProvinceDatas));
//		// 设置可见条目数量
//		mViewProvince.setVisibleItems(7);
//		mViewCity.setVisibleItems(7);
//		mViewDistrict.setVisibleItems(7);
//		updateCities();
//		updateAreas();
//	}
//	
//	@Override
//	public void onChanged(WheelView wheel, int oldValue, int newValue) {
//		// TODO Auto-generated method stub
//		if (wheel == mViewProvince) {
//			updateCities();
//		} else if (wheel == mViewCity) {
//			updateAreas();
//		} else if (wheel == mViewDistrict) {
//			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
//		}
//	}
//
//	/**
//	 * 根据当前的市，更新区WheelView的信息
//	 */
//	private void updateAreas() {
//		int pCurrent = mViewCity.getCurrentItem();
//		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
//		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//		mCurrentDistrictName = areas[0];
//		
//		if (areas == null) {
//			areas = new String[] { "" };
//		}
//		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//		mViewDistrict.setCurrentItem(0);
//		showSelectedResult();
////		updateDistrict();
//	}
//
//	/**
//	 * 根据当前的省，更新市WheelView的信息
//	 */
//	private void updateCities() {
//		int pCurrent = mViewProvince.getCurrentItem();
//		mCurrentProviceName = mProvinceDatas[pCurrent];
//		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//		if (cities == null) {
//			cities = new String[] { "" };
//		}
//		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
//		mViewCity.setCurrentItem(0);
//		updateAreas();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edcome:
//			closeKeybord(this);
			if(lin_cascade.getVisibility()==View.VISIBLE){
				lin_cascade.setVisibility(View.INVISIBLE);
			}else{
				lin_cascade.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.eibankcard:
			initdialog();
			break;
		default:
			break;
		}
	}

	private void initdialog() {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this);
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(bean.data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				banknum = bean.data.get(arg2).bank_name;
				eibankcard.setText(bean.data.get(arg2).bank_name);
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(lv);
		dialog.show();
	}

//	private void showSelectedResult() {
//		edcome.setText(mCurrentProviceName+"-"+mCurrentCityName+"-"+mCurrentDistrictName);
////		Toast.makeText(RealnameActivity.this, "当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
////				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();
//	}
//	protected void initProvinceDatas()
//	{
//		List<ProvinceModel> provinceList = null;
//    	AssetManager asset = getAssets();
//        try {
//            InputStream input = asset.open("province_data.xml");
//            // 创建一个解析xml的工厂对象
//			SAXParserFactory spf = SAXParserFactory.newInstance();
//			// 解析xml
//			SAXParser parser = spf.newSAXParser();
//			XmlParserHandler handler = new XmlParserHandler();
//			parser.parse(input, handler);
//			input.close();
//			// 获取解析出来的数据
//			provinceList = handler.getDataList();
//			//*/ 初始化默认选中的省、市、区
//			if (provinceList!= null && !provinceList.isEmpty()) {
//				mCurrentProviceName = provinceList.get(0).getName();
//				List<CityModel> cityList = provinceList.get(0).getCityList();
//				if (cityList!= null && !cityList.isEmpty()) {
//					mCurrentCityName = cityList.get(0).getName();
//					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
//					mCurrentDistrictName = districtList.get(0).getName();
//					mCurrentZipCode = districtList.get(0).getZipcode();
//				}
//			}
//			//*/
//			mProvinceDatas = new String[provinceList.size()];
//        	for (int i=0; i< provinceList.size(); i++) {
//        		// 遍历所有省的数据
//        		mProvinceDatas[i] = provinceList.get(i).getName();
//        		List<CityModel> cityList = provinceList.get(i).getCityList();
//        		String[] cityNames = new String[cityList.size()];
//        		for (int j=0; j< cityList.size(); j++) {
//        			// 遍历省下面的所有市的数据
//        			cityNames[j] = cityList.get(j).getName();
//        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
//        			String[] distrinctNameArray = new String[districtList.size()];
//        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
//        			for (int k=0; k<districtList.size(); k++) {
//        				// 遍历市下面所有区/县的数据
//        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
//        				// 区/县对于的邮编，保存到mZipcodeDatasMap
//        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
//        				distrinctArray[k] = districtModel;
//        				distrinctNameArray[k] = districtModel.getName();
//        			}
//        			// 市-区/县的数据，保存到mDistrictDatasMap
//        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
//        		}
//        		// 省-市的数据，保存到mCitisDatasMap
//        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
//        	}
//        } catch (Throwable e) {  
//            e.printStackTrace();  
//        } finally {
//        	
//        } 
//	}
	
	class ProvinceAdapter extends BaseAdapter{
		public List<BankListBean>  adapter_list;
		public ProvinceAdapter(List<BankListBean> b){
			adapter_list = b;
		}
		
		@Override
		public int getCount() {
			return adapter_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			TextView tv = new TextView(RealnameActivity.this);
			tv.setTextColor(getResources().getColor(R.color.white));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).bank_name);
			return tv;
		}
		
	}
	
	private void initbank(){
		
		CityRequest req = new CityRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", " - - - - - - "+Api.BANKLIST);
		new HttpUtils().send(HttpMethod.POST, Api.BANKLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				 bean = new Gson().fromJson(resp.result, BanklListResponse.class);
				if(Api.SUCCEED == bean.result_code){
				}
			}
		});
		
		
		
		
		b = new String[]{"农业银行","北京银行","中国银行","建设银行","光大银行","兴业银行","中信银行","招商银行","民生银行","广发银行","华夏银行",
				"工商银行","邮政储蓄","平安银行","浦发银行","包商银行","上海银行"};
		c = new String[]{"ABCCREDIT","BCCBCREDIT","BOCCREDIT","CCBCREDIT","EVERBRIGHTCREDIT","CIBCREDIT","ECITICCREDIT",
				"CMBCHINACREDIT","CMBCCREDIT","GDBCREDIT","HXBCREDIT","ICBCCREDIT","PSBCCREDIT",
				"PINGANCREDIT","SPDBCREDIT","BSBCREDIT","BOSHCREDIT"};
//		d = new int[]{R.drawable.ps_abc,R.drawable.ps_bjb,R.drawable.ps_boc,R.drawable.ps_ccb,R.drawable.ps_cebb,
//				R.drawable.ps_cib,R.drawable.ps_citic,R.drawable.ps_cmb,R.drawable.ps_cmbc,
//				R.drawable.ps_gdb,R.drawable.ps_hxb,R.drawable.ps_icbc,R.drawable.ps_psbc,
//				R.drawable.ps_spa,R.drawable.ps_spdb,R.drawable.ps_bsb,R.drawable.ps_sh};
	}
	
	
	
}
