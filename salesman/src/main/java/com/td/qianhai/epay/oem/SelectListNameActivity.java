package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.views.sortlistview.CharacterParser;
import com.td.qianhai.epay.oem.views.sortlistview.ClearEditText;
import com.td.qianhai.epay.oem.views.sortlistview.PinyinComparator;
import com.td.qianhai.epay.oem.views.sortlistview.SideBar;
import com.td.qianhai.epay.oem.views.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.td.qianhai.epay.oem.views.sortlistview.SortAdapter;
import com.td.qianhai.epay.oem.views.sortlistview.SortModel;

/**
 * 选择list集合(带搜索)
 * 
 * @author
 * 
 */
public class SelectListNameActivity extends BaseActivity implements
		OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private ArrayList<HashMap<String, Object>> listDate;
	private Intent intent;
	private Bundle bundle;
	private String titleContent;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private TextView backBtn;
	private Context mcontext;

	// List<ClsCompanyinfo> companyInfoList = null;

	private itemOnclickCallBack onclickCallBack;
	private AppContext myApplication;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppContext.getInstance().addActivity(this);
		intent = getIntent();
		bundle = intent.getExtras();
		if (bundle != null) {
			titleContent = bundle.getString("titleContent");
			listDate = (ArrayList<HashMap<String, Object>>) bundle
					.getParcelableArrayList("carrier").get(0);
			Log.e("", "sd = = "+listDate);
		}
		setContentView(R.layout.selectcompany_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		myApplication = AppContext.getInstance();
		// companyInfoList = new ArrayList<ClsCompanyinfo>();
		mcontext = this;
		initViews();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				// 刷新UI

				break;
			}
		}
	};

	private void initViews() {
		backBtn = (TextView) findViewById(R.id.bt_title_left);
		backBtn.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title_contre)).setText(titleContent);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {

				if (s != null) {
					// 该字母首次出现的位置
					int position = adapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						sortListView.setSelection(position);
					}
				}
			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				Toast.makeText(getApplication(),
//						((SortModel) adapter.getItem(position)).getName(),
//						Toast.LENGTH_SHORT).show();

				// view.setBackgroundColor(Color.parseColor("#E0EEEE"));
				SortModel model = (SortModel) adapter.getItem(position);

				Bundle bundle = new Bundle();
				bundle.putString("companyName", model.getName());
				bundle.putString("companyId", model.getCompanyId());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				if (titleContent.equals("银行卡开户省份")) {
					setResult(9, intent);
				} else if (titleContent.equals("银行卡开户城市")) {
					setResult(8, intent);
				} else if (titleContent.equals("开户所在区/县")) {
					setResult(7, intent);
				}
				overridePendingTransition(R.anim.push_left_out,
						R.anim.push_left_in);
				SelectListNameActivity.this.finish();
			}
		});

		// 动画效果
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		sortListView.setLayoutAnimation(controller);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// companyInfoList = ClsCompanyinfo.getCompanyInfoList(Common.dbh);
		// companyInfoList.addAll(myApplication.getCompanyInfoList()) ;
		SourceDateList = filledData(listDate);// 数据源

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(SelectListNameActivity.this, SourceDateList);
		sortListView.setAdapter(adapter);
		changeBtnBackground();// 修改按钮背景色
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<HashMap<String, Object>> list) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		int size = list.size();
		for (int i = 0; i < size; i++) {
			SortModel sortModel = new SortModel();
			HashMap<String, Object> map = list.get(i);
			String pinyin = null;
//			if (titleContent.equals("银行卡开户省份")) {
//				sortModel.setName(map.get("PROVNAM").toString());
//				sortModel.setCompanyId(map.get("PROVID").toString());
//				// 汉字转换成拼音
//				pinyin = characterParser.getSelling(map.get("PROVNAM")
//						.toString());
//			} else if (titleContent.equals("银行卡开户城市")) {
//				sortModel.setName(map.get("CITYNAM").toString());
//				sortModel.setCompanyId(map.get("CITYID").toString());
//				// 汉字转换成拼音
//				pinyin = characterParser.getSelling(map.get("CITYNAM")
//						.toString());
//			} else if (titleContent.equals("银行卡开户支行")) {
//				sortModel.setName(map.get("BENELX").toString());
//				sortModel.setCompanyId(map.get("BKNO").toString());
//				// 汉字转换成拼音
//				pinyin = characterParser.getSelling(map.get("BENELX")
//						.toString());
//			}
			if (titleContent.equals("银行卡开户省份")) {
				sortModel.setName(map.get("AREANAME").toString());
				sortModel.setCompanyId(map.get("AREACODE").toString());
				// 汉字转换成拼音
				pinyin = characterParser.getSelling(map.get("AREANAME")
						.toString());
			} else if (titleContent.equals("银行卡开户城市")) {
				sortModel.setName(map.get("AREANAME").toString());
				sortModel.setCompanyId(map.get("AREACODE").toString());
				// 汉字转换成拼音
				pinyin = characterParser.getSelling(map.get("AREANAME")
						.toString());
			} else if (titleContent.equals("开户所在区/县")) {
				sortModel.setName(map.get("AREANAME").toString());
				sortModel.setCompanyId(map.get("AREACODE").toString());
				// 汉字转换成拼音
				pinyin = characterParser.getSelling(map.get("AREANAME")
						.toString());
			}
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	public void setOnclickCallBack(itemOnclickCallBack onclickCallBack) {
		this.onclickCallBack = onclickCallBack;
	}

	/**
	 * 接口用于回调
	 * 
	 * @author
	 * 
	 */
	public interface itemOnclickCallBack {

		void itemOnClick(HashMap<String, String> map);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.bt_title_left) {// 回退

			this.finish();
			// overridePendingTransition(R.anim.push_left_out,
			// R.anim.push_left_in);
		}
	}

	/**
	 * 修改按钮背景图片
	 */
	public void changeBtnBackground() {

		class MyBtnOnTouch implements OnTouchListener {

			int drawable[] = null;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

//				drawable = new int[] { R.drawable.back_button_selected,
//						R.drawable.back_button_normal };

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:

					v.setBackgroundResource(drawable[0]);

					break;
				case MotionEvent.ACTION_UP:
					v.setBackgroundResource(drawable[1]);
					break;
				}

				return false;
			}
		}
		backBtn.setOnTouchListener(new MyBtnOnTouch());
	}

}
