package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class RichTreasureActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_name, tv_account_state, tv_Amount;//, tv_current,
			//tv_basis;
	private RelativeLayout btn_uppay_password,btn_reg_pay_password;
	// private Intent intent;
	// private Bundle bundle;
	private RichTreasureBean treasureBean;
	private TextView bt_title_left, tv_title_contre, bt_title_right;
	private RelativeLayout btn_menu_current, btn_menu_basis;
	private LinearLayout rich_lin;
	/** 用户手机号*/
	private String phone;
	
	private OneButtonDialogWarn warnDialog;

	void initView() {
		rich_lin = (LinearLayout) findViewById(R.id.rich_lin);
		tv_name = (TextView) findViewById(R.id.tv_consumer_name);
		tv_account_state = (TextView) findViewById(R.id.tv_consumer_account_state);
		tv_Amount = (TextView) findViewById(R.id.tv_Amount);
//		tv_current = (TextView) findViewById(R.id.tv_current);
//		tv_basis = (TextView) findViewById(R.id.tv_basis);
		btn_uppay_password = (RelativeLayout) findViewById(R.id.btn_uppay_password);
		btn_menu_current = (RelativeLayout) findViewById(R.id.btn_menu_current);
		btn_menu_basis = (RelativeLayout) findViewById(R.id.btn_menu_basis);
		btn_reg_pay_password = (RelativeLayout) findViewById(R.id.btn_reguppay_password);
		rich_lin.setOnClickListener(this);
		btn_menu_basis.setOnClickListener(this);
		btn_menu_current.setOnClickListener(this);
		btn_uppay_password.setOnClickListener(this);
		btn_reg_pay_password.setOnClickListener(this);
		
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		bt_title_right = (TextView) findViewById(R.id.bt_title_right);
		bt_title_right.setVisibility(View.GONE);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("钱包");
		bt_title_left.setOnClickListener(this);


//		if (treasureBean.getCheckamt().length() == 1) {
//			tv_current.setText("0.0" + treasureBean.getCheckamt());
//		} else if (treasureBean.getCheckamt().length() == 2) {
//			tv_current.setText("0." + treasureBean.getCheckamt());
//		} else {
//			tv_current.setText(treasureBean.getCheckamt().substring(0,
//					treasureBean.getCheckamt().length() - 2)
//					+ "."
//					+ treasureBean.getCheckamt().substring(
//							treasureBean.getCheckamt().length() - 2));
//		}

//		if (treasureBean.getFixamt().length() == 1) {
//			tv_basis.setText("0.0" + treasureBean.getFixamt());
//		} else if (treasureBean.getFixamt().length() == 2) {
//			tv_basis.setText("0." + treasureBean.getFixamt());
//		} else {
//			tv_basis.setText(treasureBean.getFixamt().substring(0,
//					treasureBean.getFixamt().length() - 2)
//					+ "."
//					+ treasureBean.getFixamt().substring(
//							treasureBean.getFixamt().length() - 2));
//		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// intent = getIntent();
		// bundle = intent.getExtras();
		// treasureBean = (RichTreasureBean) bundle.getSerializable("treasure");
		setContentView(R.layout.rich_treasure_main);
		AppContext.getInstance().addActivity(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
				phone);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_uppay_password:
			Intent intent = new Intent(RichTreasureActivity.this,
					UpdatePayPassActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_title_left:
//			Intent intent6 = new Intent(RichTreasureActivity.this,
//					MenuActivity.class);
//			startActivity(intent6);
			finish();
			break;

//		case R.id.bt_title_right:
//			Intent intent2 = new Intent(RichTreasureActivity.this,
//					RichTreasureDealRecordsActivity.class);
//			startActivity(intent2);
//			break;
		case R.id.btn_menu_current:
			Intent intent3 = new Intent(RichTreasureActivity.this,
					CurrentAccountInfoActivity.class);
			startActivity(intent3);
			break;
		case R.id.btn_menu_basis:
//			Intent intent4 = new Intent(RichTreasureActivity.this,
//					BasisActivity.class);
//			startActivity(intent4);
			break;
		case R.id.btn_reguppay_password:
			Intent intent5 = new Intent(RichTreasureActivity.this,
					RegetPayPwActivity.class);
			startActivity(intent5);
			break;
			
		case R.id.rich_lin:
			Intent intent7 = new Intent(RichTreasureActivity.this,
					BalanceDetailsAcitvity.class);
			intent7.putExtra("money", treasureBean.getTotamt());
			startActivity(intent7);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			Intent intent = new Intent(RichTreasureActivity.this,
//					MenuActivity.class);
//			startActivity(intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);
			
		}
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					loadingDialogWhole.dismiss();
					treasureBean = new RichTreasureBean();
					treasureBean.setLogsts(result.get("LOGSTS").toString());
					Log.e("", "result.gettoString() = = "+result.get("LOGSTS").toString());
					treasureBean.setActsts(result.get("ACTSTS").toString());
					treasureBean.setAvaamt(result.get("AVAAMT").toString());
					treasureBean.setYesterincom(result.get("YESTERINCOM")
							.toString());
					treasureBean.setTotamt(result.get("TOTAMT").toString());
					treasureBean.setFixamt(result.get("FIXAMT").toString());
					treasureBean.setCheckamt(result.get("CHECKAMT").toString());
					treasureBean.setFrzamt(result.get("FRZAMT").toString());
					treasureBean.setDptrate(result.get("DPTRATE").toString());
					treasureBean.setCumulative(result.get("CUMULATIVE")
							.toString());
//					treasureBean.setCustid(result.get("CARDID").toString());
					treasureBean.setMilincom(result.get("MILINCOM").toString());
					treasureBean.setWeekincom(result.get("WEEKINCOM")
							.toString());
					treasureBean.setMonthincom(result.get("MONTHINCOM")
							.toString());
					if(result.get("MERNAM")!=null&&!result.get("MERNAM").equals("")){
						treasureBean.setMerNam(result.get("MERNAM").toString());
					}else{
						treasureBean.setMerNam("");
					}
					treasureBean.setBanknam(result.get("BANKNAM").toString());
					treasureBean.setActcard(result.get("ACTCARD").toString());
					treasureBean.setCrdflg(result.get("CRDFLG").toString());
					treasureBean.setIsActpwout(result.get("ISACTPWOUT")
							.toString());
					((AppContext)getApplication())
							.setTreasureBean(treasureBean);
					
					tv_name.setText(treasureBean.getMerNam());
					if (treasureBean.getActsts().equals("0")) {
						tv_account_state.setText("不可用");
					} else {
						tv_account_state.setText("可用");
					}
					if (treasureBean.getTotamt().length() == 1) {
						tv_Amount.setText("0.0" + treasureBean.getTotamt());
					} else if (treasureBean.getTotamt().length() == 2) {
						tv_Amount.setText("0." + treasureBean.getTotamt());
					} else {
						tv_Amount.setText(treasureBean.getTotamt().substring(0,
								treasureBean.getTotamt().length() - 2)
								+ "."
								+ treasureBean.getTotamt().substring(
										treasureBean.getTotamt().length() - 2));
					}
					
					if (treasureBean.getLogsts().equals("1")) {
						
					} else if (treasureBean.getLogsts().equals("0")) {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(),"账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
//						ToastCustom
//								.showMessage(RichTreasureActivity.this, "账户暂未开通该功能!");
					} else {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(),"账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(RichTreasureActivity.this, "账户暂未开通该功能!"
//								+ treasureBean.getLogsts());
					}
				} else {
					loadingDialogWhole.dismiss();
					
					warnDialog = new OneButtonDialogWarn(RichTreasureActivity.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
//					ToastCustom.showMessage(RichTreasureActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			} else {
				Toast.makeText(getApplicationContext(),"数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RichTreasureActivity.this, "数据获取失败,请检查网络连接");
				loadingDialogWhole.dismiss();
			}
			super.onPostExecute(result);
			
				
		}
		
	}
}
