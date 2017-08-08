package com.shareshenghuo.app.user;


import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.Constans;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.AddBankCardRequest;
import com.shareshenghuo.app.user.network.response.MyBankCardResponse;
import com.shareshenghuo.app.user.network.response.MyBankCardResponse2;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.networkapi.Request;
import com.shareshenghuo.app.user.networkapi.CallbackObject;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.ImageDialog;
import com.shareshenghuo.app.user.widget.dialog.ImagedialogListener;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddcardInfoActivity extends BaseTopActivity implements OnClickListener{

	
	private TwoButtonDialog downloadDialog;
	private RelativeLayout re_banktype;
	private TextView tv_card_propty,tv_card_propty1,eibankcard;
	private ImageDialog imgdialog;
	private String type,card;
	private LinearLayout ll_credit;
	private EditText emobile,eibankcardnum,edate,ecvn2;
    private final int RESULT_CODE=101;
    private final int REQUEST_CODE=1;
    private String banknames;
    private String userName;
    private String userPersonNo;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.addcard_info_activity);
        UserInfo userInfo = UserInfoManager.getUserInfo(this);
        if (userInfo != null) {
            userName = userInfo.real_name;
            userPersonNo = userInfo.person_no;
        }
		type = getIntent().getStringExtra("type");
		card = getIntent().getStringExtra("card");
		banknames = getIntent().getStringExtra("cardname");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("添加银行卡");
		re_banktype = getView(R.id.re_banktype);
		tv_card_propty = getView(R.id.tv_card_propty);
		tv_card_propty1 = getView(R.id.tv_card_propty1);
		ll_credit = getView(R.id.ll_credit);
		eibankcardnum = getView(R.id.eibankcardnum);
		eibankcard = getView(R.id.eibankcard);
		emobile = getView(R.id.emobile);
		edate = getView(R.id.edate);
		ecvn2 = getView(R.id.ecvn2);
		getView(R.id.llWalletRecharge).setOnClickListener(this);
		tv_card_propty.setOnClickListener(this);
		tv_card_propty1.setOnClickListener(this);
//		re_banktype.setOnClickListener(this);
		
		if(type!=null&&type.equals("1")){
			ll_credit.setVisibility(View.GONE);
		}else{
			ll_credit.setVisibility(View.VISIBLE);
		}
		if(card!=null){
			eibankcardnum.setText(card);
			eibankcardnum.setEnabled(false);
		}
		if(banknames!=null){
    		if(type!=null&&type.equals("1")){
    			ll_credit.setVisibility(View.GONE);
    			eibankcard.setText(banknames+" (储蓄卡)");
    		}else{
    			eibankcard.setText(banknames+" (信用卡)");
    			ll_credit.setVisibility(View.VISIBLE);
    		}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_card_propty:
			showimagedialog(R.drawable.card_propty_img, "输入日期格式: 1215");
		
			break;
		case R.id.tv_card_propty1:
			showimagedialog(R.drawable.card_cvv_img, "卡背面后三位数字,即CVN2码");
			break;
		case R.id.re_banktype:
			startActivityForResult((new Intent(AddcardInfoActivity.this,ChooseCardTypeActivity.class)),REQUEST_CODE);
			break;
		case R.id.llWalletRecharge:
			addcard();
			break;
		default:
			break;
		}
		
	}

	private void addcard() {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(eibankcard.getText().toString())){
			T.showShort(getApplicationContext(), "请选择卡类型");
			return;
		}
		
		if(TextUtils.isEmpty(emobile.getText().toString())){
			T.showShort(getApplicationContext(), "银行预留手机号不能为空");
			return;
		}
		if(emobile.getText().toString().length()<11){
			T.showShort(getApplicationContext(), "手机号有误");
			return;
		}
		
		if(type.equals("2")){
			if(TextUtils.isEmpty(edate.getText().toString())){
				T.showShort(getApplicationContext(), "有效期不能为空");
				return;
			}
			if(edate.getText().toString().length()<4){
				T.showShort(getApplicationContext(), "卡有效期有误");
				return;
			}
			if(TextUtils.isEmpty(ecvn2.getText().toString())){
				T.showShort(getApplicationContext(), "CVN2码不能为空");
				return;
			}
		}
        requestAddBankCard();
//		oldAddBankCard();
	}

	private void requestAddBankCard(){
        ProgressDialogUtil.showProgressDlg(this, "");
		String card_no = card.replace(" ", "");
		String validMonth = "";
		String validYear = "";
		String safeCode = "";
		if(type.equals("2")){
			String m = edate.getText().toString().substring(0, 2);
			String y = edate.getText().toString().substring(2, 4);
			validMonth = m;
			validYear = y;
			safeCode = ecvn2.getText().toString();
		}
		Request.getServiceAddBankCard(UserInfoManager.getUserInfo(this).id + "",
				card_no.trim(), type, banknames, userName, userPersonNo, emobile.getText().toString().trim(),
				validYear, validMonth, safeCode, new CallbackObject<MyBankCardResponse2>() {
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

	/**
	 * 原添加银行卡请求
	 */
	private void oldAddBankCard(){
		String card_no = card.replace(" ", "");
		ProgressDialogUtil.showProgressDlg(this, "");
		AddBankCardRequest req = new AddBankCardRequest();
		req.user_id = UserInfoManager.getUserInfo(this).id+"";
		req.user_type = "1";
		req.bank_name = banknames;
		req.card_type = type;
		req.card_no = card_no.trim();
		req.bank_mobile = emobile.getText().toString();
		if(type.equals("2")){
			String m = edate.getText().toString().substring(0, 2);
			String y = edate.getText().toString().substring(2, 4);
			req.valid_month = m;
			req.valid_year = y;
			req.safe_code = ecvn2.getText().toString();
		}

		Log.e("", " - - -   "+req.toString());

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.PAYBINDCARD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AddcardInfoActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				MyBankCardResponse bean = new Gson().fromJson(resp.result, MyBankCardResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){

						initDialog("",bean.data.RSPMSG, "", "确定");
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//						finish();
//						upNum(bean.data);
					}else{
						initDialog("绑卡失败",bean.data.RSPMSG, "确定", "");
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}else{
					T.showShort(getApplicationContext(), "网络异常");
				}
			}
		});
	}

	private void initDialog(String title,String content,String left,String right) {
	// TODO Auto-generated method stub
	downloadDialog = new TwoButtonDialog(AddcardInfoActivity.this, R.style.CustomDialog,
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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE) {
            if(resultCode==RESULT_CODE) {
            	String bankname=data.getStringExtra("bankname");
                String result=data.getStringExtra("type");
                banknames = bankname;
                type = result;
        		if(result!=null&&result.equals("1")){
        			ll_credit.setVisibility(View.GONE);
        			eibankcard.setText(bankname+" (借记卡)");
        		}else{
        			eibankcard.setText(bankname+" (信用卡)");
        			ll_credit.setVisibility(View.VISIBLE);
        		}
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
	}

	public void showimagedialog(int id ,String content){
		 imgdialog = new ImageDialog(AddcardInfoActivity.this, R.style.CustomDialog,content,id,new ImagedialogListener() {
				
				@Override
				public void ImagedialogListener(View v) {
					// TODO Auto-generated method stub
					
					imgdialog.dismiss();
				}
			});
			 imgdialog.show();
	}

}
