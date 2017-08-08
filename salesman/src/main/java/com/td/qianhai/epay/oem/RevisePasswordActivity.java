package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 登录密码修改
 * 
 * @author liangge
 * 
 */
public class RevisePasswordActivity extends BaseActivity {
	/** 提交 */
	private Button btnSubmit;
	/** 原密码、新密码、确认新密码 */
	private EditText etOldPw, etNewPw, etNewPwAgain;
	/** 商户ID */
	private String custId;

	private TextView tv_title;
	/** 返回、title中间内容 */
	private TextView bt_Back;
	
	private CheckBox e_pwd4,e_pwd5,e_pwd6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.revise_password);
		AppContext.getInstance().addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		custId = ((AppContext) this.getApplication()).getCustId();
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		e_pwd4 = (CheckBox) findViewById(R.id.e_pwd4);
		e_pwd5 = (CheckBox) findViewById(R.id.e_pwd5);
		e_pwd6 = (CheckBox) findViewById(R.id.e_pwd6);
		etOldPw = (EditText) findViewById(R.id.et_revise_password_old);
		etNewPw = (EditText) findViewById(R.id.et_revise_password_new);
		etNewPwAgain = (EditText) findViewById(R.id.et_revise_password_new_again);
		btnSubmit = (Button) findViewById(R.id.btn_revise_password_submit);
		tv_title = (TextView) findViewById(R.id.tv_title_contre);
		tv_title.setText("修改登录密码");
		
		
		e_pwd4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					etOldPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					etOldPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		

		e_pwd5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					etNewPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					etNewPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		e_pwd6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					etNewPwAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					etNewPwAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		
		
		etOldPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd4.setVisibility(View.VISIBLE);
		        	e_pwd5.setVisibility(View.GONE);
					e_pwd6.setVisibility(View.GONE);
		        }else{//失去焦点  
		        	e_pwd4.setVisibility(View.GONE);
		        }  
		    }             
		});
		etNewPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd5.setVisibility(View.VISIBLE);
					e_pwd4.setVisibility(View.GONE);
					e_pwd6.setVisibility(View.GONE);
		        }else{//失去焦点  
					e_pwd5.setVisibility(View.GONE);
		        }  
		    }             
		});
		etNewPwAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		       
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
					e_pwd6.setVisibility(View.VISIBLE);
					e_pwd4.setVisibility(View.GONE);
					e_pwd5.setVisibility(View.GONE);
		        }else{//失去焦点  
					e_pwd6.setVisibility(View.GONE);
		        }  
		    }             
		});
		
		
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					// 修改密码
					revisePassword();
				}

			}
		});
		bt_Back = (TextView) findViewById(R.id.bt_title_left);
		bt_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/** 修改密码提交 */
	private void revisePassword() {
		String oldPassword = etOldPw.getText().toString();
		if (oldPassword == null
				|| (oldPassword != null && oldPassword.equals(""))) {
			ToastCustom.showMessage(this, "请输入旧密码", Toast.LENGTH_SHORT);
			return;
		} else if (oldPassword.length() < 6) {
			ToastCustom.showMessage(this, "密码长度应为6-15位", Toast.LENGTH_SHORT);
			return;
		}

		String newPassword = etNewPw.getText().toString();
		if (newPassword == null
				|| (newPassword != null && newPassword.equals(""))) {
			ToastCustom.showMessage(this, "请输入新密码", Toast.LENGTH_SHORT);
			return;
		} else if (newPassword.length() < 6) {
			ToastCustom.showMessage(this, "密码长度应为6-15位", Toast.LENGTH_SHORT);
			return;
		}

		String againNewPassword = etNewPwAgain.getText().toString();
		if (againNewPassword == null
				|| (againNewPassword != null && againNewPassword.equals(""))) {
			ToastCustom.showMessage(this, "请再次输入新密码", Toast.LENGTH_SHORT);
			return;
		}

		if (!againNewPassword.equals(newPassword)) {
			ToastCustom.showMessage(this, "新密码不一致", Toast.LENGTH_SHORT);
			return;
		}

		if (oldPassword.equals(newPassword)) {
			ToastCustom.showMessage(this, "新密码不能和旧密码一样", Toast.LENGTH_SHORT);
			return;
		}
		String userId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
//		oldRequestO(oldPassword, newPassword);
		requestUpdateLoginPwd(userId, oldPassword, newPassword);
	}

	private void requestUpdateLoginPwd(String userId, String oldPassword, String newPassword){
		ProgressDialogUtil.showProgressDlg(this, "");
        oldPassword = PwdUtils.getEncripyPwd(oldPassword, 3);
        newPassword = PwdUtils.getEncripyPwd(newPassword, 3);
		Request.getSalesmanChangePassword(userId, oldPassword, newPassword, new CallbackString() {
			@Override
			public void onFailure(String msg) {
				ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(RevisePasswordActivity.this, msg, null);
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
                WaringDialogUtils.showWaringDialog(RevisePasswordActivity.this, "网络异常", null);
			}
		});

	}

	private void oldRequestO(String oldPassword, String newPassword){
		RevisePasswordTask task = new RevisePasswordTask();
		task.execute(HttpUrls.REVISE_PASSWORD + "", custId, oldPassword,
				newPassword);
	}

	/** 修改密码TASK */
	class RevisePasswordTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在修改中...");
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.REVISE_PASSWORD, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					ToastCustom.showMessage(RevisePasswordActivity.this,
							"修改成功", Toast.LENGTH_SHORT);
//					((AppContext) getApplication()).setCustPass(etNewPw.getText().toString());
					finish();
				} else {
					showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
}
