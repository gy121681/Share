package com.shareshenghuo.app.user.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;

public class NumberPickLayout extends LinearLayout implements OnClickListener {
	
	private Context context;
	
	private Button btnReduce, btnAdd;
	private TextView tvNum;
	
	private int minNum = 0;
	private int curNum;
	
	private OnNumberChangeListener onNumberChangeListener;

	public NumberPickLayout(Context context) {
		super(context);
		init(context);
	}
	
	public NumberPickLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NumberPickLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_number_pick, this);
		
		btnReduce = (Button) findViewById(R.id.btnNumberReduce);
		btnAdd = (Button) findViewById(R.id.btnNumberAdd);
		tvNum = (TextView) findViewById(R.id.tvNumber);
		
		initData();
		
		btnReduce.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
	}
	
	private void initData() {
		curNum = minNum;
		btnReduce.setEnabled(false);
		tvNum.setText(minNum+"");
	}
	
	public int getMinNumber() {
		return minNum;
	}
	
	public void setMinNumber(int num) {
		minNum = num;
		if(curNum <= num) {
			initData();
		}
	}
	
	public int getCurNumber() {
		return curNum;
	}
	
	public void setCurNumber(int num) {
		this.curNum = num;
		tvNum.setText(curNum+"");
		if(curNum > minNum)
			btnReduce.setEnabled(true);
	}
	
	@Override
	public void setEnabled(boolean b) {
		btnAdd.setEnabled(b);
		btnReduce.setEnabled(b);
		
		btnAdd.setVisibility(b? View.VISIBLE : View.INVISIBLE);
		btnReduce.setVisibility(b? View.VISIBLE : View.INVISIBLE);
	}

	public OnNumberChangeListener getOnNumberChangeListener() {
		return onNumberChangeListener;
	}

	public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
		this.onNumberChangeListener = onNumberChangeListener;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
	case R.id.btnNumberReduce:
		curNum--;
		tvNum.setText(curNum+"");
		if(curNum <= minNum)
			btnReduce.setEnabled(false);
		if(onNumberChangeListener != null)
			onNumberChangeListener.onChanged(curNum, false);
		break;
		
	case R.id.btnNumberAdd:
		curNum++;
		tvNum.setText(curNum+"");
		if(curNum > minNum)
			btnReduce.setEnabled(true);
		if(onNumberChangeListener != null)
			onNumberChangeListener.onChanged(curNum, true);
		break;
		}
	}
	
	public interface OnNumberChangeListener {
		public void onChanged(int curNum, boolean isAdd);
	}
}
