package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.Constans;
import com.shareshenghuo.app.shop.network.bean.BankListBean;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.AutRequset;
import com.shareshenghuo.app.shop.network.request.CityRequest;
import com.shareshenghuo.app.shop.network.request.ReBankCardRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.BankInfoResponse;
import com.shareshenghuo.app.shop.network.response.BanklListResponse;
import com.shareshenghuo.app.shop.network.response.MyBankCardResponse2;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.networkapi.CallbackObject;
import com.shareshenghuo.app.shop.networkapi.Request;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class RealnameActivity extends BaseTopActivity implements OnClickListener{

	private EditText eibankcardnum,edname,eidcard;
	
	private TextView eibankcard,recardname,recard;
	
	private String banknum = "";
	
	private BanklListResponse bean;
	
	private String legal_person_name = "",egal_person_no = "";
	
	private String tag = "0";
	
	private ImageView card_img;
	
	private LinearLayout llname,llcard;

    private TwoButtonDialog downloadDialog;

	private EditText mEdtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption_series_activitys);
		
        tag = getIntent().getStringExtra("tag");
		legal_person_name = UserInfoManager.getUserInfo(this).legal_person_name;
		egal_person_no = UserInfoManager.getUserInfo(this).legal_person_no;
		
		if(tag!=null&&tag.equals("1")){
			  initview1();
		}else{
			  initview();
		}
		if(bean==null){
			initbank();
		}

    }

	
	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("添加银行卡");
		eibankcard = getView(R.id.eibankcard);
		edname = getView(R.id.edname);
		eidcard = getView(R.id.eidcard);
		edname.setText(legal_person_name);
		eidcard.setText(egal_person_no);
		eibankcardnum = getView(R.id.eibankcardnum);
		btnTopRight1.setVisibility(View.GONE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				authentication();
			}

		});
		getView(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authentication();
			}
		});
		eibankcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				initdialog();
			}
		});
		mEdtMobile = getView(R.id.edt_mobile);
	}
	
	private void initview1() {
		initTopBar("修改银行卡");
		eibankcard = getView(R.id.eibankcard);
		edname = getView(R.id.edname);
        edname.setText(legal_person_name);
		recardname = getView(R.id.recardname);
		recard =getView(R.id.recard);
		llcard = getView(R.id.llcard);
		llcard.setVisibility(View.VISIBLE);
		card_img = getView(R.id.card_img);
		
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).card_no)){
			String card = UserInfoManager.getUserInfo(this).card_no;
			if(card.length()>4){
				recard.setText(card.substring(0, 4)+" **** **** "+card.substring(card.length()-4));
			}
			
		}
		
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).bank_name)){
			String bank = UserInfoManager.getUserInfo(this).bank_name;
			recardname.setText(bank);
			setbank(card_img,bank);
		}
//		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).real_name)){
//			String bank = UserInfoManager.getUserInfo(this).real_name;
//			recardname.setText(bank);
//		}
		
		eidcard = getView(R.id.eidcard);
        eidcard.setText(egal_person_no);
		llname = getView(R.id.llname);
		eibankcardnum = getView(R.id.eibankcardnum);
		btnTopRight1.setVisibility(View.GONE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rebank();
			}

		});
		getView(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rebank();
			}
		});
		eibankcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initdialog();
			}
		});
		
	}
	
	
	private void initdialog() {
		// TODO Auto-generated method stub
		if(bean==null||bean.data==null){
			initbank();
			return;
		}

		final Dialog dialog = new Dialog(this, R.style.dialog1);
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(bean.data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				banknum = bean.data.get((int)arg2).bank_name;
				eibankcard.setText(bean.data.get((int)arg2).bank_name);
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(lv);
		dialog.show();
	}

	private void initbank(){
		
		CityRequest req = new CityRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

    /**
     * 添加银行卡
     */
	private void authentication() {
		
		if(banknum.equals("")){
			eibankcard.setError("");
			return;
		}

		if(ViewUtil.checkEditEmpty(eibankcardnum, "请填写银行卡号"))
			return;
		
		if(eibankcardnum.getText().toString().length()<10){
			T.showShort(this, "银行卡10到19位");
			return;
		}
        String mobile = mEdtMobile.getText().toString().trim();
        if (!isMobileNO(mobile)) {
            T.showShort(this, "请输入有效的手机号");
            return;
        }
        requestBankInfo();
//        oldAddBankCard();
	}

    /**
     * 验证手机合法性
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 使用新api请求银行卡信息
     */
    private void requestBankInfo(){
        String card_no = eibankcardnum.getText().toString();
        ProgressDialogUtil.showProgressDlg(this, "");
        Request.getServiceQueryBankCardInfo(card_no.trim(), UserInfoManager.getUserInfo(this).shop_id+"" + "",
                new CallbackObject<BankInfoResponse>() {
                    @Override
                    public void onSuccess(BankInfoResponse data) {
                        if (data != null) {
							if (data.result.nature.contains("借记") || data.result.nature.contains("储蓄")){
								addBankCard(data.result.bank);
							} else {
								toast("请选择借记卡或者储蓄卡");
								ProgressDialogUtil.dismissProgressDlg();
							}
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }
                });
    }

	private void addBankCard(String banknames){
		String card_no = eibankcardnum.getText().toString().trim();
        String mobile = mEdtMobile.getText().toString().trim();
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		Request.getServiceAddBankCard(UserInfoManager.getUserInfo(this).id + "",
				card_no.trim(), "1", banknames, userInfo.legal_person_name,
				userInfo.legal_person_no, mobile,
				"", "", "", new CallbackObject<MyBankCardResponse2>() {
					@Override
					public void onSuccess(MyBankCardResponse2 data) {
						ProgressDialogUtil.dismissProgressDlg();
						if(data.RSPCOD.equals(Constans.RSPCOD_SUCCESS)){
                            initDialog("",data.RSPMSG, "", "确定");
                        }else{
                            initDialog("绑卡失败",data.RSPMSG, "确定", "");
                        }
					}

					@Override
					public void onFailure(String msg) {
						toast(msg);
						ProgressDialogUtil.dismissProgressDlg();
					}

					@Override
					public void onNetError(int code, String msg) {
						ProgressDialogUtil.dismissProgressDlg();
						toast("网络异常");
					}
				});
	}


    private void initDialog(String title,String content,String left,String right) {
        // TODO Auto-generated method stub
        downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
                title, content, left, right,true,new OnMyDialogClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.Button_OK:
                        downloadDialog.dismiss();
                        break;
                    case R.id.Button_cancel:
                        finish();
                        downloadDialog.dismiss();
                    default:
                        break;
                }
            }
        });
        downloadDialog.show();
    }

    /**
     * 原添加银行卡
     */
	private void oldAddBankCard(){
        // TODO Auto-generated method stub
        AutRequset req = new AutRequset();
        req.userId = UserInfoManager.getUserInfo(this).shop_id+"";
        req.userType = "2";
        req.realName = "";
        req.personNo = "";
        req.bankName = banknum;
        req.cardNo =	eibankcardnum.getText().toString();
        req.provinceCode = "";
        req.cityCode = "";
        req.areaCode = "";
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.FICATION, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }
            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
                if(Api.SUCCEED == bean.result_code){
                    if(bean.data.RSPCOD.equals("000000")){
                        T.showShort(getApplicationContext(), "添加成功");
                        finish();
                    }else{
                        T.showShort(getApplicationContext(), bean.data.RSPMSG);
                    }
                }
            }
        });
    }


    /**
     * 修改银行卡
     */
	private void rebank() {
		
		if(ViewUtil.checkEditEmpty(eidcard, "请填身份证号")){
			return;
		}
		
		if(banknum.equals("")){
			eibankcard.setError("");
			return;
		}
		if(ViewUtil.checkEditEmpty(eibankcardnum, "请填写银行卡号"))
			return;
        String mobile = mEdtMobile.getText().toString().trim();
        if (!isMobileNO(mobile)) {
            T.showShort(this, "请输入有效的手机号");
            return;
        }
		// TODO Auto-generated method stub
		ReBankCardRequest req = new ReBankCardRequest();
		req.userShopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.userType = "2";
		req.personNo = egal_person_no;
		req.bankName = banknum;
		req.cardNo =	eibankcardnum.getText().toString();
		req.bank_mobile = mobile;
		req.PERSON_NO = legal_person_name;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.USERCARDNOCHANGE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
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
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).bank_name);
			return tv;
		}
		
	}
	
	private void setbank(ImageView v,String bank){
		
		if (bank!= null&&bank.length()>=2) {
			if (bank.contains("招商")) {
				v.setImageResource(R.drawable.ps_cmb);
			}else if(bank.contains("农业")) {
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("农行")){
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("北京")){
				v.setImageResource(R.drawable.ps_bjb);
			}else if(bank.equals("中国银行")){
//				if(bank.substring(0,4).equals("中国银行")){
//					v.setImageResource(R.drawable.ps_ccb);
//				}else{
					v.setImageResource(R.drawable.ps_boc);
//				}
				
			}else if(bank.contains("建设")){
				v.setImageResource(R.drawable.ps_ccb);
			}else if(bank.contains("光大")){
				v.setImageResource(R.drawable.ps_cebb);
			}else if(bank.contains("兴业")){
				v.setImageResource(R.drawable.ps_cib);
			}else if(bank.contains("中信")){
				v.setImageResource(R.drawable.ps_citic);
			}else if(bank.contains("民生")){
				v.setImageResource(R.drawable.ps_cmbc);
			}else if(bank.contains("交通")){
				v.setImageResource(R.drawable.ps_comm);
			}else if(bank.contains("华夏")){
				v.setImageResource(R.drawable.ps_hxb);
			}else if(bank.contains("广东发展")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("广发")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("邮政")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("邮储")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("平安")){
				v.setImageResource(R.drawable.ps_spa);
			}else if(bank.contains("浦东")){
				v.setImageResource(R.drawable.ps_spdb);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("上海")){
				v.setImageResource(R.drawable.ps_sh);
			}else{
				v.setImageResource(R.drawable.ps_unionpay);
			}
			
		}else{
			v.setImageResource(R.drawable.ps_unionpay);
		}
		
		
	}
	
}
