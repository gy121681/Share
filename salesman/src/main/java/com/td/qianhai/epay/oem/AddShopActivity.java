package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.share.app.utils.MD5Utils;
import com.share.app.utils.PwdUtils;
import com.td.qianhai.epay.oem.AddShopCityActivity.ProvinceAdapter;
import com.td.qianhai.epay.oem.beans.AddShopBean;
import com.td.qianhai.epay.oem.beans.AddShopRequest;
import com.td.qianhai.epay.oem.beans.AddShopResponce;
import com.td.qianhai.epay.oem.beans.AddShopRsp;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.UpShopInfoRequst;
import com.td.qianhai.epay.oem.mail.utils.ChexText;
import com.td.qianhai.epay.oem.mail.utils.IdCard;
import com.td.qianhai.mpay.utils.ChineseUtil;

public class AddShopActivity extends BaseActivity {

	private EditText emobile, epwd, ename, eidcard, ephone, emile;
	private EditText personname, person_idcard, person_phone;
	private TextView eibankcard, tv_go,person_post;
	private LinearLayout llenterprise;
	private HashMap<String, Object> result;
	private String
	handle_id = "",
	type = "1",
	emobiles = "",
	epwds = "",
	enames = "",
	eidcards = "",
	ephones = "",
	emiles = "",
	personnames = "",
	person_idcards = "",
	person_phones = "",
	person_posts = "",
	tag = "";
	

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addshop_activity);
		AppContext.getInstance().addActivity(this);
		AppContext.getInstance().addActivity1(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("提交商户");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		handle_id = getIntent().getStringExtra("handle_id");
		tag = getIntent().getStringExtra("tag");

		emobile = (EditText) findViewById(R.id.emobile);
		epwd = (EditText) findViewById(R.id.epwd);
		ename = (EditText) findViewById(R.id.ename);
		eidcard = (EditText) findViewById(R.id.eidcard);
		ephone = (EditText) findViewById(R.id.ephone);
		emile = (EditText) findViewById(R.id.emile);
		personname = (EditText) findViewById(R.id.personname);
		person_idcard = (EditText) findViewById(R.id.person_idcard);
		person_phone = (EditText) findViewById(R.id.person_phone);
		person_post = (TextView) findViewById(R.id.person_post);
		eibankcard = (TextView) findViewById(R.id.eibankcard);
		tv_go = (TextView) findViewById(R.id.tv_go);
		llenterprise = (LinearLayout) findViewById(R.id.llenterprise);
		eibankcard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				initdialog();

			}

		});
		person_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initdialog(person_post);
			}
		});
		tv_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					next();
				
			}
		});

		
		if(tag!=null&&tag.equals("1")){
			getinfo();
		}
	}
	
	
	

	private void getinfo() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		AddShopRequest req = new AddShopRequest();
		req.collect_shop_id = handle_id;

		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.DETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				AddShopRsp bean = new Gson().fromJson(resp.result, AddShopRsp.class);
				Log.e("", ""+resp.result);
				if(0== bean.result_code) {
//				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
				initinfo(bean.data);
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
		
	}
	
	private void initinfo(AddShopBean data) {
		// TODO Auto-generated method stub
		
		if(data.collect_type.equals("1")){
			type ="1"; 
			eibankcard.setText("个体");
			llenterprise.setVisibility(View.GONE);
		}else{
			type = "2";
			eibankcard.setText("企业");
			llenterprise.setVisibility(View.VISIBLE);
			personname.setText(data.manager_name.trim());
			person_idcard.setText(data.manager_no);
			person_phone.setText(data.manager_mobile);
			person_post.setText(data.manager_role);
		}
		
		emobile.setText(data.account);
		ename.setText(data.legal_person_name.trim());
		eidcard.setText(data.legal_person_no);
		emile.setText(data.legal_person_email);
		ephone.setText(data.legal_person_mobile);
		
		
		
	}
	

	private void initdialog() {
		// TODO Auto-generated method stub
		String[] selectPicTypeStr = { "个体", "企业" };

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		// new AlertDialog.Builder(this)
		dialog.setItems(selectPicTypeStr,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							type ="1"; 
							eibankcard.setText("个体");
							llenterprise.setVisibility(View.GONE);
							break;
						case 1:
							type = "2";
							eibankcard.setText("企业");
							llenterprise.setVisibility(View.VISIBLE);
							break;
						default:

							break;
						}
					}

				});
		dialog.show();
	}
	
	private void next() {
		// TODO Auto-generated method stub
		
		if(TextUtils.isEmpty(eibankcard.getText())){
			Texterro(eibankcard, "请选择商家类型");
			return;
		}
		
		if(checkEditEmpty(emobile, "请输入手机号")){
			return;
		}
		
		if(!isMobileNO(emobile.getText().toString())){
			Editerro(emobile, "手机号有误");
			return;
		}
		
		
		
		if(checkEditEmpty(epwd, "请输入密码")){
			return;
		}
		
		if(epwd.getText().length()<6){
			Editerro(epwd,"密码最少6位");
			return;
		}
		if(checkEditEmpty(ename, "请输入法人姓名")){
			return;
		}
		if(!ChexText.checkNameChese(ename.getText().toString())){
			Editerro(ename,"姓名非法字符或非中文格式");
			return;
		}
		
		
		if(checkEditEmpty(eidcard, "请输入身份证号")){
			return;
		}
		
		if(!new IdCard().isValidatedAllIdcard(eidcard.getText().toString())){
			Editerro(eidcard, "身份证号有误");
			return;
		}
		
		
		if(checkEditEmpty(ephone, "请输入法人手机号码")){
			return;
		}
		if(!isMobileNO(ephone.getText().toString())){
			Editerro(ephone, "手机号有误");
			return;
		}
		
		
		if(checkEditEmpty(emile, "请输入邮箱")){
			return;
		}
		
		if(!isEmail(emile.getText().toString())){
			Editerro(emile, "邮箱有误");
			return;
		}
		
		if(llenterprise.getVisibility()==View.VISIBLE){
			
			if(checkEditEmpty(personname, "请输入负责人姓名")){
				return;
			}
			if(!ChexText.checkNameChese(personname.getText().toString())){
				Editerro(personname,"姓名非法字符或非中文格式");
				return;
			}
			if(checkEditEmpty(person_idcard, "请输入负责人身份证号")){
				return;
			}
			if(!new IdCard().isValidatedAllIdcard(person_idcard.getText().toString())){
				Editerro(person_idcard, "身份证号有误");
				return;
			}
			
			
			if(checkEditEmpty(person_phone, "输入负责人手机号")){
				return;
			}
			
			if(!isMobileNO(person_phone.getText().toString())){
				Editerro(person_phone, "手机号有误");
				return;
			}
			
//			if(checkEditEmpty(person_post, "输入负责人职务")){
//				return;
//			}
			
			if(TextUtils.isEmpty(person_post.getText())){
				Texterro(person_post, "选择负责人职务");
				return;
			}
		}
		
				emobiles = emobile.getText().toString();
				epwds = epwd.getText().toString();
				enames = ename.getText().toString();
				eidcards = eidcard.getText().toString();
				emiles = emile.getText().toString();
				personnames = personname.getText().toString();
				person_idcards = person_idcard.getText().toString();
				person_phones = person_phone.getText().toString();
				person_posts = person_post.getText().toString();
				ephones = ephone.getText().toString();
	
//				new Thread(run).start();
				showLoadingDialog("请稍候...");
				
				
				if(tag!=null&&tag.equals("1")){
					changenext();
				}else{
					commit();
				}
				
				
	}
	
	private void changenext() {
		// TODO Auto-generated method stub
		UpShopInfoRequst req = new UpShopInfoRequst();
		req.collect_id = handle_id;
		req.account = emobiles;
		for( int i = 0; i < 3; i++) {
			epwds = MD5Utils.getMD5String(epwds);
		}
		req.password = epwds;
		req.legal_person_name = enames;
		req.legal_person_mobile = ephones;
		req.legal_person_email = emiles;
		req.legal_person_no = eidcards;
		req.collect_type = type;
		req.handle_id = handle_id;
		req.manager_name = personnames;
		req.manager_role = person_posts;
		req.manager_mobile = person_phones;
		req.manager_no = person_idcards;
		
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.UPDATESHOP, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				AddShopResponce bean = new Gson().fromJson(resp.result, AddShopResponce.class);
				if(0== bean.result_code) {
					
					Intent it = new Intent(AddShopActivity.this,AddShopInfoActivity.class);
					it.putExtra("tag", tag);
					it.putExtra("collect_id", handle_id);
					startActivity(it);
					
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
//					T.showShort(AddrEditActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//				T.showNetworkError(AddrEditActivity.this);
			}
		});
		
	}
	
	private void commit() {

		// TODO Auto-generated method stub
		AddShopRequest req = new AddShopRequest();
		req.account = emobiles;
		String pwd = PwdUtils.getEncripyPwd(epwds, 3);
		req.password = pwd;
		req.legal_person_name = enames;
//		req.legal_person_mobile = emobiles;
		req.legal_person_mobile = ephones;
		req.legal_person_email = emiles;
		req.legal_person_no = eidcards;
		req.collect_type = type;
		req.handle_id = handle_id;
		req.manager_name = personnames;
		req.manager_role = person_posts;
		req.manager_mobile = person_phones;
		req.manager_no = person_idcards;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.ADDSHOP, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				AddShopRsp bean = new Gson().fromJson(resp.result, AddShopRsp.class);
				if(0== bean.result_code) {
//				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
					String collect_id = "";
					collect_id = bean.data.id;
					Intent it = new Intent(AddShopActivity.this,AddShopInfoActivity.class);
					it.putExtra("collect_id", collect_id);
					startActivity(it);
				
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
		
		
	
	/**
	 * 验证邮箱合法性
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	
	private void initdialog( final TextView tv  ) {
		// TODO Auto-generated method stub

		final String[] selectPicTypeStr = { "总经理", "总监","经理","主管" };
		
		
		final Dialog dialog = new Dialog(this,R.style.MyEditDialog1);
//		dialog.setTitle("选择");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(selectPicTypeStr);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				tv.setText(selectPicTypeStr[(int)arg2]);
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(lv);
		dialog.show();
	}
	
	class ProvinceAdapter extends BaseAdapter{
		public String[]  adapter_list;
		public ProvinceAdapter(String[] selectPicTypeStr){
			adapter_list = selectPicTypeStr;
		}
		
		@Override
		public int getCount() {
			return adapter_list.length;
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
			
//			LayoutInflater inflater = LayoutInflater.from(AddShopActivity.this);
//			View view = inflater.inflate(R.layout.dailog_item, null);
//			TextView v = (TextView) view.findViewById(R.id.tvs);
//			v.setText(adapter_list[position]);
			
			TextView tv = new TextView(AddShopActivity.this);
			tv.setWidth(800);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list[position]);
			return tv;
		}
		
	}
	
}
