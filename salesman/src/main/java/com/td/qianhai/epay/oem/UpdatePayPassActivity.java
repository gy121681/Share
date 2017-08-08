package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.PwdUtils;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

/**
 * 修改支付密码
 * 
 * @author Administrator
 * 
 */
public class UpdatePayPassActivity extends BaseActivity implements
		OnClickListener {

	private EditText et_up1, et_up2, et_oldPass;
	private Button btn_next;
	private String oldPass, possword, possword2, actid;
	private CheckBox e_pwd1,e_pwd2,e_pwd3;

	void initView() {
		
		e_pwd1 = (CheckBox) findViewById(R.id.e_pwd1);
		e_pwd2 = (CheckBox) findViewById(R.id.e_pwd2);
		e_pwd3 = (CheckBox) findViewById(R.id.e_pwd3);
//		actid = ((AppContext) UpdatePayPassActivity.this.getApplication()).getMobile();
		actid = MyCacheUtil.getshared(this).getString("Mobile", "");
		et_oldPass = (EditText) findViewById(R.id.et_upoldpass);
		et_up1 = (EditText) findViewById(R.id.et_up1);
		et_up2 = (EditText) findViewById(R.id.et_up2);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		et_up1.setOnClickListener(this);
		et_up2.setOnClickListener(this);
		et_oldPass.setOnClickListener(this);
		
		e_pwd1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					et_up2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					et_up2.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		

		e_pwd2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					et_up1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					et_up1.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		e_pwd3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					et_oldPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					et_oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		
		et_up2.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd1.setVisibility(View.VISIBLE);
		        	e_pwd2.setVisibility(View.GONE);
					e_pwd3.setVisibility(View.GONE);
		        }else{//失去焦点  
		        	e_pwd1.setVisibility(View.GONE);
		        }  
		    }             
		});
		et_oldPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd3.setVisibility(View.VISIBLE);
					e_pwd1.setVisibility(View.GONE);
					e_pwd2.setVisibility(View.GONE);
		        }else{//失去焦点  
					e_pwd3.setVisibility(View.GONE);
		        }  
		    }             
		});
		et_up1.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd2.setVisibility(View.VISIBLE);
					e_pwd1.setVisibility(View.GONE);
					e_pwd3.setVisibility(View.GONE);
		        }else{//失去焦点  
					e_pwd2.setVisibility(View.GONE);
		        }  
		    }             
		});
		
		((TextView) findViewById(R.id.bt_title_left))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		((TextView) findViewById(R.id.tv_title_contre)).setText("修改支付密码");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.virtual_account_up_pass);
		AppContext.getInstance().addActivity(this);
		initView();
	}

	void verify() {
		oldPass = et_oldPass.getText().toString();
		if (oldPass == null || oldPass.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入原支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(UpdatePayPassActivity.this, "请输入原支付密码！");
			return;
		} 
//		else if (oldPass.length() < 6) {
//			ToastCustom.showMessage(UpdatePayPassActivity.this,
//					"密码长度有误,请输入6～15个数字、字母或特殊符号！");
//			return;
//		}

		possword = et_up1.getText().toString();
		if (possword == null || possword.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入新支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(UpdatePayPassActivity.this, "请输入新支付密码！");
			return;
		} 
//		else if (possword.length() < 6) {
//			ToastCustom.showMessage(UpdatePayPassActivity.this,
//					"密码长度有误,请输入6～15个数字、字母或特殊符号！");
//			return;
//		}

		possword2 = et_up2.getText().toString();
		if (possword2 == null || possword2.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入确认新支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(UpdatePayPassActivity.this, "请输入确认新支付密码！");
			return;
		} else if (!possword.equals(possword2)) {
			Toast.makeText(getApplicationContext(),"新支付密码与确认支付密码不一致，请重新输入",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(UpdatePayPassActivity.this,
//					"新支付密码与确认支付密码不一致，请重新输入！");
			return;
		}
//		oldRequest();
		String userId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
		requestUpdatePayPassword(userId, oldPass, possword);
	}

	private void requestUpdatePayPassword(String userId, String oldPwd, String newPwd) {
		oldPwd = PwdUtils.getEncripyPwd(oldPwd, 3);
		newPwd = PwdUtils.getEncripyPwd(newPwd, 3);
        ProgressDialogUtil.showProgressDlg(this, "");
        Request.getSalesmanUpdatePayPassword(userId, oldPwd, newPwd, new CallbackString() {
			@Override
			public void onFailure(String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(UpdatePayPassActivity.this, msg, null);
			}

			@Override
			public void onSuccess(String data) {
                ProgressDialogUtil.dismissProgressDlg();
                toast("操作成功");
                finish();
			}

			@Override
			public void onNetError(int code, String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(UpdatePayPassActivity.this, "网络异常", null);

			}
		});
	}

	private void oldRequest(){
		UpPayPassTask task = new UpPayPassTask();
		task.execute(HttpUrls.SET_PAY_PASS + "", actid, oldPass, possword);
	}

	/**
	 * 修改支付密码AsyncTask
	 * 
	 * @author Administrator
	 * 
	 */
	class UpPayPassTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getMidatc(HttpUrls.UP_PAY_PASS, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					Toast.makeText(getApplicationContext(),"修改支付密码成功",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UpdatePayPassActivity.this, "修改支付密码成功");
//					Intent it = new Intent(UpdatePayPassActivity.this,
//							MenuActivity.class);
//					startActivity(it);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),result
							.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UpdatePayPassActivity.this, result
//							.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			verify();
			break;
		}
	}
}
