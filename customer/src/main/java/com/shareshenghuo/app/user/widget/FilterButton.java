//package com.shareshenghuo.app.user.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.shareshenghuo.app.user.R;
//
//public class FilterButton extends LinearLayout {
//	
//	private Context mContext;
//	
//	private TextView tvTitle;
//	private LinearLayout llDire;
//	private ImageView ivUp;
//	private ImageView ivDown;
//	private View vSlider;
//	
//	private boolean isSelected = false;
//	private boolean direction = false; // true:up/asc  false:down/desc
//
//	public FilterButton(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//		init(context, attrs, defStyleAttr);
//	}
//
//	public FilterButton(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context, attrs, 0);
//	}
//
//	public FilterButton(Context context) {
//		super(context);
//		init(context, null, 0);
//	}
//
//	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//		this.mContext = context;
//		LayoutInflater.from(context).inflate(R.layout.layout_filter_button, this);
//		this.setClickable(true);
//		tvTitle = (TextView) findViewById(R.id.tvFltBtnTitle);
//		llDire = (LinearLayout) findViewById(R.id.llFltBtnDire);
//		ivUp = (ImageView) findViewById(R.id.ivFltBtnUp);
//		ivDown = (ImageView) findViewById(R.id.ivFltBtnDown);
//		vSlider = (View) findViewById(R.id.vFltBtnSlider);
//		
//		if(attrs != null)
//			initAttr(attrs);
//	}
//	
//	private void initAttr(AttributeSet attrs) {
//		TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.FilterButton, 0, 0);
//		
//		String text = array.getString(R.styleable.FilterButton_button_text);
//		if(!TextUtils.isEmpty(text))
//			tvTitle.setText(text);
//		
//		boolean isSelected = array.getBoolean(R.styleable.FilterButton_button_selected, false);
//		setSelected(isSelected);
//	}
//	
//	public void setSelected(boolean isSelected) {
//		this.isSelected = isSelected;
//		if(isSelected) {
//			tvTitle.setTextColor(mContext.getResources().getColor(R.color.blue_dark));
//			vSlider.setVisibility(View.VISIBLE);
//			llDire.setVisibility(View.VISIBLE);
//		} else {
//			tvTitle.setTextColor(mContext.getResources().getColor(R.color.black_light));
//			vSlider.setVisibility(View.INVISIBLE);
//			llDire.setVisibility(View.GONE);
//		}
//	}
//	
//	public boolean isSelected() {
//		return isSelected;
//	}
//	
//	public boolean divert() {
//		direction = !direction;
//		if(direction) {
//			ivUp.setVisibility(View.VISIBLE);
//			ivDown.setVisibility(View.INVISIBLE);
//		} else {
//			ivUp.setVisibility(View.INVISIBLE);
//			ivDown.setVisibility(View.VISIBLE);
//		}
//		return direction;
//	}
//	
//	/**
//	 * @return  true:up  false:down 
//	 */
//	public boolean getDirection() {
//		return direction;
//	}
//}
