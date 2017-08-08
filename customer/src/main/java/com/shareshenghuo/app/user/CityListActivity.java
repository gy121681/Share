package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.Indexablewidget.CharacterParser;
import com.shareshenghuo.app.user.Indexablewidget.ContactsSortAdapter;
import com.shareshenghuo.app.user.Indexablewidget.PinyinComparator;
import com.shareshenghuo.app.user.Indexablewidget.SideBar;
import com.shareshenghuo.app.user.Indexablewidget.SideBar.OnTouchingLetterChangedListener;
import com.shareshenghuo.app.user.Indexablewidget.SortModel;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.HotCityAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CityInfo;
import com.shareshenghuo.app.user.network.response.CityListResponse;
import com.shareshenghuo.app.user.network.response.CityListResponse.CityList;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 城市选择列表
 */
public class CityListActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {

	public static final int FROM_SERVICE_CITY = 0x102;
	public static final int FROM_ADDR_MANAGE = 0x103;
	
	private static final int REQ_SEARCH_CITY = 0x101;
	
	private TextView tvLoc,dialog;
	private MyGridView gvHot;
	private MyGridView gvlishi;
	private ListView lvAll;
	
	private CityManager cityManager;
	
	private int from;
	private SideBar sideBar;
	private ContactsSortAdapter adapter;
	private List<SortModel> mAllContactsList;
	private List<CityInfo> cityinfo;
	public static final  int num = 4;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	
	private final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
	private final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600 };
    // 存放国标一级汉字不同读音的起始区位码对应读音
	private final char[] firstLetter = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X',
            'Y', 'Z' };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_list);
		initView();
		loadData();
		History();
	}
	

	private void History() {
		String user = UserInfoManager.getHISTORY(CityListActivity.this);
		 List<SortModel> hot_city_list = new ArrayList<SortModel>(); 
		if(user!=null&&user.length()>0){
			String [] history = user.split(";");
			for (int i = 0; i < history.length; i++) {
				 String [] city = history[i].split(",");
				 SortModel info = new SortModel(city[0], city[1]+"", "","","","");
				 hot_city_list.add(info);
			}
			gvlishi.setAdapter(new HotCityAdapter(CityListActivity.this, hot_city_list));
		}
		Log.e("", "user = = = =   "+user);
	}


	public void initView() {
		initTopBar("城市");
		gvlishi = getView(R.id.gvlishi);
		tvLoc = getView(R.id.tvLocCity);
		gvHot = getView(R.id.gvHotCity);
		lvAll = getView(R.id.listviews);
		from = getIntent().getIntExtra("from", FROM_SERVICE_CITY);
		
		cityManager = CityManager.getInstance(this);
		if(cityManager.locCityInfo!=null) {
			tvLoc.setText(cityManager.locCityInfo.is_default==1? cityManager.locCity+" 未开通" : cityManager.locCity);
		}

		findViewById(R.id.ivBack).setOnClickListener(this);
		findViewById(R.id.btnSearchCity).setOnClickListener(this);
		tvLoc.setOnClickListener(this);
		
		gvHot.setOnItemClickListener(this);
		lvAll.setOnItemClickListener(this);
		gvlishi.setOnItemClickListener(this);
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		/** 给ListView设置adapter **/
		characterParser = CharacterParser.getInstance();
		mAllContactsList = new ArrayList<SortModel>();
		pinyinComparator = new PinyinComparator();
		Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
		adapter = new ContactsSortAdapter(this, mAllContactsList);
		lvAll.setAdapter(adapter);
		
		//设置右侧[A-Z]快速导航栏触摸监听
				sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						//该字母首次出现的位置
//						Utility.setListViewHeightBasedOnChildren(lvAll);
						int position = adapter.getPositionForSection(s.charAt(0));
						if (position != -1) {
							lvAll.setSelection(position);
						}
					}
				});
	}
	
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		new HttpUtils().send(HttpMethod.GET, Api.URL_CITY_LIST, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CityListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CityListResponse bean = new Gson().fromJson(resp.result, CityListResponse.class);
				if(Api.SUCCEED==bean.result_code && bean.data!=null) {
					 List<SortModel> hot_city_list = new ArrayList<SortModel>(); 
					for (int i = 0; i <bean.data.hot_city_list.size(); i++) {
						SortModel info = new SortModel(bean.data.hot_city_list.get(i).city_name, bean.data.hot_city_list.get(i).id+"", "","","","");
						hot_city_list.add(info);
					}
//					gvHot.setAdapter(new HotCityAdapter(CityListActivity.this, bean.data.hot_city_list));
					gvHot.setAdapter(new HotCityAdapter(CityListActivity.this, hot_city_list));
//					lvAll.setAdapter(new CityListAdapter(CityListActivity.this, bean.data.city_list));
//					Utility.setListViewHeightBasedOnChildren(lvAll);
					cityinfo = bean.data.city_list;
					initlist(bean.data);
				
				}
			}

		});
	}
	
	private void initlist(CityList data) {
		// TODO Auto-generated method stub
		for (int i = 0; i < data.city_list.size(); i++) {
			CityInfo info = data.city_list.get(i);
			SortModel sortModel = new SortModel(info.city_name, info.id+"", "","","","");
			//优先使用系统sortkey取,取不到再使用工具取
			String 	sortLetters = getSortLetter1(info.city_name);
			sortModel.sortLetters = sortLetters;
			mAllContactsList.add(sortModel);

		}

		Collections.sort(mAllContactsList, pinyinComparator);
		adapter.updateListView(mAllContactsList);
//		Utility.setListViewHeightBasedOnChildren(lvAll);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivBack:
			finish();
			break;
			
		case R.id.btnSearchCity:
			startActivityForResult(new Intent(this, SearchCityActivity.class), REQ_SEARCH_CITY);
			break;
			
		case R.id.tvLocCity:
			if(cityManager.locCityInfo!=null && cityManager.locCityInfo.is_default==0) {
				pickCity(cityManager.locCityInfo);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
//		pickCity(cityinfo.get((int)arg3));
			pickCity( (SortModel) parent.getAdapter().getItem(position));
	}
	private void pickCity(SortModel item) {
		// TODO Auto-generated method stub
		StringBuffer buff = new StringBuffer();
		String user = UserInfoManager.getHISTORY(CityListActivity.this);
		boolean nums = false;
		if(!user.contains(item.name)){
			buff.append(item.name+","+item.number+";");
			nums = true;
		}
		
		if(user!=null&&user.length()>0){
			String[] name = user.split(";");
			
			if(name.length<num){
				for (int i = 0; i < name.length; i++) {
					String [] city = name[i].split(",");
					buff.append(city[0]+","+city[1]+";");
				}
			}else if(name.length==num){
				for (int i = 0; i < name.length-1; i++) {
					String [] city = name[i].split(",");
					buff.append(city[0]+","+city[1]+";");
				}
			}
			
		}
		if(nums){
			UserInfoManager.setHISTORY(CityListActivity.this, buff.toString());
		}
	
		CityInfo info = new CityInfo();
		info.city_name = item.name;
		info.id = Integer.parseInt(item.number);
		cityManager.cityInfo = info;
		setResult(RESULT_OK);
		finish();
		buff = null;
		
		
	}

	public void pickCity(CityInfo cityInfo) {
		if(from == FROM_SERVICE_CITY) {
			cityManager.cityInfo = cityInfo;
			setResult(RESULT_OK);
		} else if(from == FROM_ADDR_MANAGE) {
			Intent data = new Intent();
			data.putExtra("cityId", cityInfo.id);
			data.putExtra("cityName", cityInfo.city_name);
			setResult(RESULT_OK, data);
		}
		finish();
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode==RESULT_OK && reqCode==REQ_SEARCH_CITY) {
			pickCity((CityInfo) data.getSerializableExtra("cityInfo"));
		}
	}
	
	
	

//	private void setlist() {
//		// TODO Auto-generated method stub
//	
//		UserInfo userInfo = UserInfoManager.getUserInfo(this);
////		UserInfoManager.saveUserInfo(this, userInfo);
//		List<CityInfo> list = new ArrayList<CityInfo>();
//		CityInfo info = new CityInfo();
//		info.id = 0;
//		info.city_name = "深圳";
//		list.add(info);
//		userInfo.serachcity  = new ArrayList<CityInfo>();
//		userInfo.serachcity .addAll(list);
//		UserInfoManager.saveUserInfo(this, userInfo);
//		UserInfo userInfos = UserInfoManager.getUserInfo(this);
//		
//		gvlishi.setAdapter(new HotCityAdapter(CityListActivity.this,userInfos.serachcity));
////		if(userInfo.serachcity.size()>0){
////			
////			
////		}
//	}
	
	private String getSortLetter1(String name) {
		String letter = "#";
		if (name == null) {
			return letter;
		}
		String sortString = name.substring(0, 1);
		if(sortString.equals("泸")){
			return "L";
		}else if(sortString.equals("亳")){
			return "B";
		}else if(sortString.equals("衢")){
			return "Q"; 
		}else if(sortString.equals("漯")){
			return "L";
		}else if(sortString.equals("濮")){
			return "P";
		}else if(sortString.equals("儋")){
			return "D";
		}
//		String pinyin = characterParser.getSelling(name);
//		String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);
		return getSpells(sortString);
	}
	
	/**
	 * 名字转拼音,取首字母
	 * @param name
	 * @return
	 */
	private String getSortLetter(String name) {
		Log.e("", " - - - -  "+name);
		String letter = "#";
		if (name == null) {
			return letter;
		}
		//汉字转换成拼音
		String pinyin = characterParser.getSelling(name);
		String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

//		// 正则表达式，判断首字母是否是英文字母
//		if (sortString.matches("[A-Z]")) {
//			letter = sortString.toUpperCase(Locale.CHINESE);
//		}
		return getPYIndexStr(sortString,true);
	}

	/**
	 * 取sort_key的首字母
	 * @param sortKey
	 * @return
	 */
	private String getSortLetterBySortKey(String sortKey) {
		
		if (sortKey == null || "".equals(sortKey.trim())) {
			return null;
		}
		String letter = "#";
		//汉字转换成拼音
		String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
//		// 正则表达式，判断首字母是否是英文字母
//		if (sortString.matches("[A-Z]")||sortString.matches("[a-z]")) {
//			Log.e("", "pinyin1 = = "+sortString);
//			letter = sortString.toUpperCase(Locale.CHINESE);
//		}
		return getPYIndexStr(sortString,true);
	}

	/**
	 * 模糊查询
	 * @param str
	 * @return
	 */
	private List<SortModel> search(String str) {
		List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
		//if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
		if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
			String simpleStr = str.replaceAll("\\-|\\s", "");
			for (SortModel contact : mAllContactsList) {
				if (contact.number != null && contact.name != null) {
					if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		}else {
			for (SortModel contact : mAllContactsList) {
				if (contact.number != null && contact.name != null) {
					//姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
					if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "").contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		}
		return filterList;
	}
	
	/*
	* 返回首字母

	* @param strChinese

	* @param bUpCase

	* @return

	*/

	   public static String getPYIndexStr(String strChinese, boolean bUpCase){

	       try{

	           StringBuffer buffer = new StringBuffer();

	           byte b[] = strChinese.getBytes("GBK");//把中文转化成byte数组

	           for(int i = 0; i < b.length; i++){

	               if((b[i] & 255) > 128){

	                   int char1 = b[i++] & 255;

	                   char1 <<= 8;//左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方

	                   int chart = char1 + (b[i] & 255);

	                   buffer.append(getPYIndexChar((char)chart, bUpCase));

	                   continue;

	               }

	               char c = (char)b[i];

	               if(!Character.isJavaIdentifierPart(c))//确定指定字符是否可以是 Java 标识符中首字符以外的部分。

	                   c = 'A';

	               buffer.append(c);

	           }

	           return buffer.toString();

	       }catch(Exception e){

	           System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519").append(e.getMessage()).toString());

	       }

	       return null;

	   }
	   
	   /**

	    * 得到首字母

	    * @param strChinese

	    * @param bUpCase

	    * @return

	    */

	   private static char getPYIndexChar(char strChinese, boolean bUpCase){

	       int charGBK = strChinese;

	       char result;

	       if(charGBK >= 45217 && charGBK <= 45252)

	           result = 'A';

	       else

	       if(charGBK >= 45253 && charGBK <= 45760)

	           result = 'B';

	       else

	       if(charGBK >= 45761 && charGBK <= 46317)

	           result = 'C';

	       else

	       if(charGBK >= 46318 && charGBK <= 46825)

	           result = 'D';

	       else

	       if(charGBK >= 46826 && charGBK <= 47009)

	           result = 'E';

	       else

	       if(charGBK >= 47010 && charGBK <= 47296)

	           result = 'F';

	       else

	       if(charGBK >= 47297 && charGBK <= 47613)

	           result = 'G';

	       else

	       if(charGBK >= 47614 && charGBK <= 48118)

	           result = 'H';

	       else

	       if(charGBK >= 48119 && charGBK <= 49061)

	           result = 'J';

	       else

	       if(charGBK >= 49062 && charGBK <= 49323)

	           result = 'K';

	       else

	       if(charGBK >= 49324 && charGBK <= 49895)

	           result = 'L';

	       else

	       if(charGBK >= 49896 && charGBK <= 50370)

	           result = 'M';

	       else

	       if(charGBK >= 50371 && charGBK <= 50613)

	           result = 'N';

	       else

	       if(charGBK >= 50614 && charGBK <= 50621)

	           result = 'O';

	       else

	       if(charGBK >= 50622 && charGBK <= 50905)

	           result = 'P';

	       else

	       if(charGBK >= 50906 && charGBK <= 51386)

	           result = 'Q';

	       else

	       if(charGBK >= 51387 && charGBK <= 51445)

	           result = 'R';

	       else

	       if(charGBK >= 51446 && charGBK <= 52217)

	           result = 'S';

	       else

	       if(charGBK >= 52218 && charGBK <= 52697)

	           result = 'T';

	       else

	       if(charGBK >= 52698 && charGBK <= 52979)

	           result = 'W';

	       else

	       if(charGBK >= 52980 && charGBK <= 53688)

	           result = 'X';

	       else

	       if(charGBK >= 53689 && charGBK <= 54480)

	           result = 'Y';

	       else

	       if(charGBK >= 54481 && charGBK <= 55289)

	           result = 'Z';

	       else

	           result = (char)(65 + (new Random()).nextInt(25));

	       if(!bUpCase)

	           result = Character.toLowerCase(result);

	       return result;

	   }

	   public  String getSpells(String characters) {
	        StringBuffer buffer = new StringBuffer();
	        for (int i = 0; i < characters.length(); i++) {

	            char ch = characters.charAt(i);
	            if ((ch >> 7) == 0) {
	                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
	            } else {
	                char spell = getFirstLetter(ch);
	                buffer.append(String.valueOf(spell));
	            }
	        }
	        return buffer.toString();
	    }

	    // 获取一个汉字的首字母
	    public  Character getFirstLetter(char ch) {

	        byte[] uniCode = null;
	        try {
	            uniCode = String.valueOf(ch).getBytes("GBK");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	            return null;
	        }
	        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
	            return null;
	        } else {
	            return convert(uniCode);
	        }
	    }

	    /**
	     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
	     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
	     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
	     */
	    private  char convert(byte[] bytes) {
	        char result = '-';
	        int secPosValue = 0;
	        int i;
	        for (i = 0; i < bytes.length; i++) {
	            bytes[i] -= GB_SP_DIFF;
	        }
	        secPosValue = bytes[0] * 100 + bytes[1];
	        for (i = 0; i < 23; i++) {
	            if (secPosValue >= secPosValueList[i]
	                    && secPosValue < secPosValueList[i + 1]) {
	                result = firstLetter[i];
	                break;
	            }
	        }
	        return result;
	    }

	   
	
}
