package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.td.qianhai.epay.oem.AddShopInfoActivity.ProvinceAdapter;
import com.td.qianhai.epay.oem.adapter.AddPhotoGridAdapter;
import com.td.qianhai.epay.oem.beans.AddShopBean;
import com.td.qianhai.epay.oem.beans.AddShopInfoBean;
import com.td.qianhai.epay.oem.beans.AddShopInfoResponse;
import com.td.qianhai.epay.oem.beans.AddShopRequest;
import com.td.qianhai.epay.oem.beans.AddShopResponce;
import com.td.qianhai.epay.oem.beans.AddShopRsp;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.PhotoBean;
import com.td.qianhai.epay.oem.beans.UpShopInfoRequst;
import com.td.qianhai.epay.oem.mail.utils.ChexText;
import com.td.qianhai.epay.oem.views.MapLayout;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.ChineseUtil;

public class AddShopPhotoActivity extends BaseActivity implements
		OnClickListener {

	private GridView gvArticlePhoto, gvArticlePhoto1, gvArticlePhoto2,
			gvArticlePhoto3, gvArticlePhoto4, gvArticlePhoto5;

	private AddPhotoGridAdapter adapter, adapter1, adapter2, adapter3,
			adapter4, adapter5;
	public static int num = 0;
	private MapLayout llmap, llmap1, llmap2, llmap3, llmap4;;
	private ScrollView scroll;
	private TextView tv_go, tv1;
	private EditText ed2, ed3, ed5, ed4;
	private int[] photonum = { 3, 2, 2, 2, 4, 1 };
	private String collect_id = "", tag = "";
	private AddShopBean data;
	private OneButtonDialogWarn warnDialog;
	private String bank_address;
	private String bankCode;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addshopphoto_activity);
		AppContext.getInstance().addActivity(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		tag = getIntent().getStringExtra("tag");
		collect_id = getIntent().getStringExtra("collect_id");
		((TextView) findViewById(R.id.tv_title_contre)).setText("提交商户");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv1 = (TextView) findViewById(R.id.tv1);
		ed2 = (EditText) findViewById(R.id.ed2);
		ed3 = (EditText) findViewById(R.id.ed3);
		ed4 = (EditText) findViewById(R.id.ed4);
		ed5 = (EditText) findViewById(R.id.ed5);
		findViewById(R.id.explain).setOnClickListener(this);
		tv1.setOnClickListener(this);
		ed4.setOnClickListener(this);
		tv_go = (TextView) findViewById(R.id.tv_go);
		tv_go.setOnClickListener(this);
		// llmap = (MapLayout) findViewById(R.id.llmap);
		// llmap1 = (MapLayout) findViewById(R.id.llmap1);
		// llmap2 = (MapLayout) findViewById(R.id.llmap2);
		// llmap3 = (MapLayout) findViewById(R.id.llmap3);
		// llmap4 = (MapLayout) findViewById(R.id.llmap4);
		scroll = (ScrollView) findViewById(R.id.scroll);
		// llmap.setScrollView(scroll);
		// llmap1.setScrollView(scroll);
		// llmap2.setScrollView(scroll);
		// llmap3.setScrollView(scroll);
		// llmap4.setScrollView(scroll);
		gvArticlePhoto = (GridView) findViewById(R.id.gvArticlePhoto);
		gvArticlePhoto1 = (GridView) findViewById(R.id.gvArticlePhoto1);
		gvArticlePhoto2 = (GridView) findViewById(R.id.gvArticlePhoto2);
		gvArticlePhoto3 = (GridView) findViewById(R.id.gvArticlePhoto3);
		gvArticlePhoto4 = (GridView) findViewById(R.id.gvArticlePhoto4);
		gvArticlePhoto5 = (GridView) findViewById(R.id.gvArticlePhoto5);
		List<PhotoBean> data = new ArrayList<PhotoBean>();
		data.add(new PhotoBean());
		List<PhotoBean> data1 = new ArrayList<PhotoBean>();
		data1.add(new PhotoBean());
		List<PhotoBean> data2 = new ArrayList<PhotoBean>();
		data2.add(new PhotoBean());
		List<PhotoBean> data3 = new ArrayList<PhotoBean>();
		data3.add(new PhotoBean());
		List<PhotoBean> data4 = new ArrayList<PhotoBean>();
		data4.add(new PhotoBean());
		List<PhotoBean> data5 = new ArrayList<PhotoBean>();
		data5.add(new PhotoBean());

		adapter = new AddPhotoGridAdapter(this, data, gvArticlePhoto,
				photonum[0], 1);
		adapter1 = new AddPhotoGridAdapter(this, data1, gvArticlePhoto1,
				photonum[1], 2);
		adapter2 = new AddPhotoGridAdapter(this, data2, gvArticlePhoto2,
				photonum[2], 3);
		adapter3 = new AddPhotoGridAdapter(this, data3, gvArticlePhoto3,
				photonum[3], 4);
		adapter4 = new AddPhotoGridAdapter(this, data4, gvArticlePhoto4,
				photonum[4], 5);
		adapter5 = new AddPhotoGridAdapter(this, data5, gvArticlePhoto5,
				photonum[5], 6);
		gvArticlePhoto.setAdapter(adapter);
		gvArticlePhoto1.setAdapter(adapter1);
		gvArticlePhoto2.setAdapter(adapter2);
		gvArticlePhoto3.setAdapter(adapter3);
		gvArticlePhoto4.setAdapter(adapter4);
		gvArticlePhoto5.setAdapter(adapter5);

		// if(tag!=null&&!tag.equals(""))
		// {
		getinfo();
		// }

	}

	private void getinfo() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		AddShopRequest req = new AddShopRequest();
		req.collect_shop_id = collect_id;

		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.DETAILS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						AddShopRsp bean = new Gson().fromJson(resp.result,
								AddShopRsp.class);
						if (0 == bean.result_code) {
							// Toast.makeText(getApplicationContext(), "提交",
							// Toast.LENGTH_SHORT).show();
							initinfo(bean.data);
							data = bean.data;
						} else {
							Toast.makeText(getApplicationContext(),
									bean.result_desc, Toast.LENGTH_SHORT)
									.show();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						Toast.makeText(getApplicationContext(), "请求失败",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void initinfo(AddShopBean data) {
		// TODO Auto-generated method stub

		// if(tag!=null&&!tag.equals("0")){
		// ed3.setText(data.legal_person_name);
		// return;
		// }

		tv1.setText(data.bank_name);
		ed2.setText(data.alipay_account);
		if (data.collect_type.equals("1")) {
			ed3.setText(data.legal_person_name.trim());
		} else {
			ed3.setText(data.legal_person_name.trim());
			ed3.setEnabled(true);
		}

		ed5.setText(data.bank_mobile);
		ed4.setText(data.bank_address);

		if (data.photo_list.size() > 0) {

			for (int i = 0; i < data.photo_list.size(); i++) {

				if (data.photo_list.get(i).photo_type.equals("1")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter4.getmData().get(0).url)) {
						adapter4.getmData().remove(0);
					}

					adapter4.getmData().add(bean);

				} else if (data.photo_list.get(i).photo_type.equals("3")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter1.getmData().get(0).url)) {
						adapter1.getmData().remove(0);
					}
					adapter1.getmData().add(bean);

				} else if (data.photo_list.get(i).photo_type.equals("5")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter3.getmData().get(0).url)) {
						adapter3.getmData().remove(0);
					}
					adapter3.getmData().add(bean);

				} else if (data.photo_list.get(i).photo_type.equals("6")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter.getmData().get(0).url)) {
						adapter.getmData().remove(0);
					}
					adapter.getmData().add(bean);

				} else if (data.photo_list.get(i).photo_type.equals("7")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter2.getmData().get(0).url)) {
						adapter2.getmData().remove(0);
					}
					adapter2.getmData().add(bean);

				} else if (data.photo_list.get(i).photo_type.equals("8")) {
					PhotoBean bean = new PhotoBean();
					bean.url = data.photo_list.get(i).shop_photo;
					if (TextUtils.isEmpty(adapter5.getmData().get(0).url)) {
						adapter5.getmData().remove(0);
					}
					adapter5.getmData().add(bean);

				}

			}
			// if(adapter.getmData().size()>photonum[0]){
			// adapter.getmData().clear();
			// }
			// if(adapter1.getmData().size()>photonum[1]){
			// adapter1.getmData().clear();
			// }
			// if(adapter2.getmData().size()>photonum[2]){
			// adapter2.getmData().clear();
			// }
			// if(adapter3.getmData().size()>photonum[3]){
			// adapter3.getmData().clear();
			// }
			// if(adapter4.getmData().size()>photonum[4]){
			// adapter4.getmData().clear();
			// }

			// if(adapter1.getmData().size()>photonum[1]){
			// adapter1.getmData().remove(adapter1.getmData().size()-1);
			// }
			// if(adapter3.getmData().size()>photonum[2]){
			// adapter3.getmData().remove(adapter1.getmData().size()-1);
			// }

			if (adapter3.getmData().size() < photonum[3]) {
				adapter3.getmData().add(new PhotoBean());
			}
			if (adapter4.getmData().size() < photonum[4]) {
				adapter4.getmData().add(new PhotoBean());
			}
			if (adapter.getmData().size() < photonum[0]) {
				adapter.getmData().add(new PhotoBean());
			}
			if (adapter1.getmData().size() < photonum[1]) {
				adapter1.getmData().add(new PhotoBean());
			}
			if (adapter2.getmData().size() < photonum[2]) {
				adapter2.getmData().add(new PhotoBean());
			}

			adapter4.notifyDataSetChanged();
			adapter1.notifyDataSetChanged();
			adapter.notifyDataSetChanged();
			adapter2.notifyDataSetChanged();
			adapter3.notifyDataSetChanged();
			adapter5.notifyDataSetChanged();
		}
		// adapter3.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 100 && data != null) {
			bankCode = data.getStringExtra("code");
			bank_address = data.getStringExtra("bank_key");
			ed4.setText(bank_address);
			// Log.e("", data.getStringExtra("bank_key"));
			// Log.e("", data.getStringExtra("code"));
		}

		switch (num) {
		case 1:
			adapter.onActivityResult(requestCode, resultCode, data);

			break;
		case 2:
			adapter1.onActivityResult(requestCode, resultCode, data);
			break;
		case 3:
			adapter2.onActivityResult(requestCode, resultCode, data);
			break;
		case 4:
			adapter3.onActivityResult(requestCode, resultCode, data);
			break;
		case 5:
			adapter4.onActivityResult(requestCode, resultCode, data);
			break;
		case 6:
			adapter5.onActivityResult(requestCode, resultCode, data);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_go:
			if (TextUtils.isEmpty(tv1.getText())) {
				Texterro(tv1, "请选择所在银行");
				Toast.makeText(getApplicationContext(), "请选择所在银行",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (checkEditEmpty(ed2, "请输入结算银行储蓄卡号")) {
				Toast.makeText(getApplicationContext(), "请输入结算银行储蓄卡号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (data != null && data.collect_type != null
					&& data.collect_type.equals("1")) {
				if (checkEditEmpty(ed3, "请输入结算用户名")) {
					Toast.makeText(getApplicationContext(), "请输入结算用户名",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!ChexText.checkNameChese(ed3.getText().toString())) {
					Editerro(ed3, "姓名非法字符或非中文格式");
					return;
				}
			}

			if (TextUtils.isEmpty(ed4.getText())) {
				Toast.makeText(getApplicationContext(), "请选择银行所在支行",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (checkEditEmpty(ed5, "请输入银行预留手机号")) {
				Toast.makeText(getApplicationContext(), "请输入银行预留手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!isMobileNO(ed5.getText().toString())) {
				Editerro(ed5, "手机号有误");
				return;
			}

			if (!data.collect_type.equals("1")) {
				if (checkEditEmpty(ed3, "请输入结算用户名")) {
					Toast.makeText(getApplicationContext(), "请输入结算用户名",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!ChexText.checkNameChese(ed3.getText().toString())) {
					Editerro(ed3, "姓名非法字符或非中文格式");
					return;
				}
			}

			if (!adapter.isnumm() || adapter.getCount() != photonum[0]) {
				Toast.makeText(getApplicationContext(), "身份正照片数量有误",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!adapter1.isnumm() || adapter1.getCount() != photonum[1]) {
				Toast.makeText(getApplicationContext(), "门店门头照数量有误",
						Toast.LENGTH_SHORT).show();
				return;
			}
//			if (adapter2.getCount() <= 1) {
//				Toast.makeText(getApplicationContext(), "请拍摄商家/业务员承诺书",
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
			if (!adapter3.isnumm() || adapter3.getCount() != photonum[3]) {
				Toast.makeText(getApplicationContext(), "营业执照类照片数量有误",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!adapter4.isnumm() || adapter4.getCount() != photonum[4]) {
				Toast.makeText(getApplicationContext(), "门店内景照数量有误",
						Toast.LENGTH_SHORT).show();
				return;
			}

			SpannableString msps = new SpannableString("是否确认所有信息填写正确，所拍图片足够清晰?");
			showDoubleWarnDialog(msps);

			break;
		case R.id.tv1:
			initbank();
			break;
		case R.id.explain:
			warnDialog = new OneButtonDialogWarn(

					AddShopPhotoActivity.this,
					R.style.CustomDialog,
					"所在支行填写说明",
					"1.请正确填写结算储蓄卡开户的所在支行全程,填错会导致商家货款收取失败;\n2.若填写错误可拨打对应银行客服电话进行支行地址查询后填写;\n3.若输入的支行名称无法通过,请更换银行卡后重新填写支行地址.",
					"知道了", new OnMyDialogClickListener() {
						@Override
						public void onClick(View v) {
							warnDialog.dismiss();
						}
					});

			warnDialog.show();
			break;
		case R.id.ed4:
			// if(TextUtils.isEmpty(tv1.getText())){//选择支行
			// Texterro(tv1, "请选择所在银行");
			// Toast.makeText(getApplicationContext(), "请选择所在银行",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			// Intent it = new
			// Intent(AddShopPhotoActivity.this,SearchActivty.class);
			// it.putExtra("bankname", tv1.getText().toString());
			// startActivityForResult(it, 100);

			break;
		default:
			break;
		}

	}

	protected void showDoubleWarnDialog(SpannableString msg) {
		doubleWarnDialog = new TwoButtonDialogStyle2(AddShopPhotoActivity.this,
				R.style.CustomDialog, "提示", msg, "确定", "取消",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(true);
		doubleWarnDialog.setCanceledOnTouchOutside(true);
		doubleWarnDialog.show();
	}

	protected void doubleWarnOnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();
			break;
		case R.id.btn_right:
			change();
			doubleWarnDialog.dismiss();
			break;
		default:
			break;
		}
	}

	private void initbank() {

		AddShopRequest req = new AddShopRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.TFSUPPORTBANK, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						AddShopInfoResponse bean = new Gson().fromJson(
								resp.result, AddShopInfoResponse.class);
						if (0 == bean.result_code) {
							initdialog(bean.data);
						}
					}
				});
	}

	private void initdialog(final List<AddShopInfoBean> data) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this, R.style.MyEditDialog1);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setTitle("选择");
		ListView lv = new ListView(this);
		ProvinceAdapter pAdapter = new ProvinceAdapter(data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tv1.setText(data.get((int) arg2).bank_name);
				dialog.dismiss();
			}
		});

		dialog.setContentView(lv);
		dialog.show();
	}

	private void change() {
		// if(!adapter5.isnumm()||adapter5.getCount()!=photonum[5]){
		// Toast.makeText(getApplicationContext(), "请添加特殊行业照片",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }

		String[] pohto1 = new String[photonum[0]];
		String[] pohto2 = new String[photonum[1]];
		// String [] pohto3 = new String[photonum[2]];
		String[] pohto4 = new String[photonum[3]];
		// if(adapter3.getmData().size()>0&&TextUtils.isEmpty(adapter3.getmData().get(adapter3.getmData().size()-1).url)){
		// pohto4 = new String[adapter3.getmData().size()-1];
		// }else{
		// pohto4 = new String[adapter3.getmData().size()];
		// }
		String[] pohto3;

		if (adapter2.getmData().size() > 0
				&& TextUtils.isEmpty(adapter2.getmData().get(
						adapter2.getmData().size() - 1).url)) {
			pohto3 = new String[adapter2.getmData().size() - 1];
		} else {
			pohto3 = new String[adapter2.getmData().size()];
		}

		String[] pohto6;

		if (adapter5.getmData().size() > 0
				&& TextUtils.isEmpty(adapter5.getmData().get(
						adapter5.getmData().size() - 1).url)) {
			pohto6 = new String[adapter5.getmData().size() - 1];
		} else {
			pohto6 = new String[adapter5.getmData().size()];
		}

		String[] pohto5 = new String[photonum[4]];

		for (int i = 0; i < pohto1.length; i++) {
			try {
				pohto1[i] = adapter.getmData().get(i).url;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		for (int i = 0; i < pohto2.length; i++) {
			try {
				pohto2[i] = adapter1.getmData().get(i).url;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		for (int i = 0; i < pohto3.length; i++) {
			try {
				if (!TextUtils.isEmpty(adapter2.getmData().get(i).url)) {
					pohto3[i] = adapter2.getmData().get(i).url;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		for (int i = 0; i < pohto4.length; i++) {
			try {
				if (!TextUtils.isEmpty(adapter3.getmData().get(i).url)) {
					pohto4[i] = adapter3.getmData().get(i).url;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		for (int i = 0; i < pohto5.length; i++) {
			try {
				pohto5[i] = adapter4.getmData().get(i).url;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		for (int i = 0; i < pohto6.length; i++) {
			try {
				pohto6[i] = adapter5.getmData().get(i).url;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		showLoadingDialog("请稍候...");
		UpShopInfoRequst req = new UpShopInfoRequst();
		// req.bankCode = bankCode;
		req.collect_id = collect_id;
		req.id_card_list = pohto1;
		req.mendian_photo_list = pohto2;
		req.fawu_list = pohto3;
		req.yingye_list = pohto4;
		req.neijing_list = pohto5;
		req.teshuhy_list = pohto6;
		req.bank_mobile = ed5.getText().toString();
		req.bank_name = tv1.getText().toString();
		req.alipay_account = ed2.getText().toString();
		req.bank_user_name = ed3.getText().toString();
		// req.bank_address = bank_address;
		req.bank_address = ed4.getText().toString();// 所在支行

		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println("=====商家提交,数据:" + req.toJson().toString());
		System.out.println("======商家提交,地址:" + HttpUrls.UPDATESHOP);
		new HttpUtils().send(HttpMethod.POST, HttpUrls.UPDATESHOP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
						}
						AddShopResponce bean = new Gson().fromJson(resp.result,
								AddShopResponce.class);
						System.out.println("====商家提交,返回:" + resp.result);
						if (0 == bean.result_code) {
							// Toast.makeText(getApplicationContext(),
							// "资料提交成功,请等待审核", Toast.LENGTH_SHORT).show();

							warnDialog = new OneButtonDialogWarn(

									AddShopPhotoActivity.this,
									R.style.CustomDialog,
									"提交成功",
									"  恭喜!您的商家申请已成功提交!\n审核专员会在3个工作日内为您审核，请耐心等待。   超过3个工作日未审核 ，请联系客服热线02888265063",
									"确定", new OnMyDialogClickListener() {
										@Override
										public void onClick(View v) {
											warnDialog.dismiss();
											AppContext.getInstance()
													.finishactivity();
											finish();

										}
									});

							warnDialog.setCancelable(false);
							warnDialog.setCanceledOnTouchOutside(false);
							warnDialog.show();
						} else {
							Toast.makeText(getApplicationContext(),
									bean.result_desc, Toast.LENGTH_SHORT)
									.show();
							// T.showShort(AddrEditActivity.this,
							// bean.result_desc);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						Toast.makeText(getApplicationContext(), "请求失败",
								Toast.LENGTH_SHORT).show();
						// T.showNetworkError(AddrEditActivity.this);
					}
				});

	}

	class ProvinceAdapter extends BaseAdapter {
		public List<AddShopInfoBean> adapter_list;

		public ProvinceAdapter(List<AddShopInfoBean> b) {
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
			// LayoutInflater inflater =
			// LayoutInflater.from(AddShopPhotoActivity.this);
			// View view = inflater.inflate(R.layout.dailog_item, null);
			// TextView v = (TextView) view.findViewById(R.id.tvs);
			// v.setText(adapter_list.get(position).bank_name);

			TextView tv = new TextView(AddShopPhotoActivity.this);
			tv.setWidth(800);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).bank_name);
			return tv;
		}

	}

	public String[] ArrayTest(String[] array) {

		String temp;
		int len = array.length;
		for (int i = 0; i < len / 2; i++) {
			temp = array[i];
			array[i] = array[len - 1 - i];
			array[len - 1 - i] = temp;
		}
		return array;
	}

}
