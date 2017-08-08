package com.shareshenghuo.app.user.widget.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shareshenghuo.app.user.R;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 双按钮带titel(中间textView无限制下拉)Dialog样式2
 * 
 * @author liangge
 * 
 */
public class ButtonDialogStyleNotice extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content,titles;
	/** 确认、取消 */
	private TextView bt_affirm;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, affirmStr, cancelStr, contents, info;
	private SpannableString content;
	/** dialog监听器 */
	private RelativeLayout bt_cancel;
	public OnMyDialogClickListener onMyDialogClickListener;
	private Context context;

	public ButtonDialogStyleNotice(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public ButtonDialogStyleNotice(Context context, int theme, String title,
			SpannableString content, String contents, String info,
			String affirmStr, String cancelStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.context = context;
		this.content = content;
		this.cancelStr = cancelStr;
		this.affirmStr = affirmStr;
		this.contents = contents;
		this.info = info;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}
	
	

	public ButtonDialogStyleNotice(Context context, String title,
			SpannableString content, String affirmStr, String cancelStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.title = title;
		this.content = content;
		this.cancelStr = cancelStr;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		titles = (TextView) findViewById(R.id.titles);
		titles.setText(contents);
		tv_content = (TextView) findViewById(R.id.tv_prompt);
		bt_affirm = (TextView) findViewById(R.id.btn_left);
		bt_cancel = (RelativeLayout) findViewById(R.id.btn_right);
		try {
			String aa = info.substring(0, 8);
			tv_title.setText(transferDate(aa));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		tv_content.setText("    " + stringFilter(content.toString()));
		bt_affirm.setText(affirmStr);
		// bt_cancel.setText(cancelStr);
		bt_affirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
	}

	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String transferDate(String s) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = sdf.parse(s);
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		StringBuffer sb = new StringBuffer();
		sb.append(transfer(ca.get(Calendar.YEAR) + "",true)).append("年");
		if ((ca.get(Calendar.MONTH) + 1) >= 10) {
			sb.append("十");
			sb.append(transfer(((ca.get(Calendar.MONTH) + 1) % 10) + "",true));
			sb.append("月");
		} else {
			sb.append(transfer((ca.get(Calendar.MONTH) + 1) + "",true)).append("月");
		}
		if (ca.get(Calendar.DAY_OF_MONTH) >= 10) {
			sb.append(transfer((ca.get(Calendar.DAY_OF_MONTH) / 10) + "",false));
			sb.append("十");
			sb.append(transfer((ca.get(Calendar.DAY_OF_MONTH) % 10) + "",false));
			sb.append("日");
		} else {
			sb.append(transfer(ca.get(Calendar.DAY_OF_MONTH) + "",false)).append("日");
		}
		return sb.toString();
	}

	public static String transfer(String input,boolean tag) {
		String ss[] = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		if(!tag){
			ss[0] = "";
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			String index = String.valueOf(input.charAt(i));
			if (index.matches("\\d")) {
				sb.append(ss[Integer.parseInt(index)]);
			} else {
				sb.append(index);
			}

		}
		return sb.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onbutton_dialog_notice);
		initView();
		
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
