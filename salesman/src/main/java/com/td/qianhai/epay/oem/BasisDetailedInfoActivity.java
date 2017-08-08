package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Basis;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.SuccessHintDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.DateUtil;

public class BasisDetailedInfoActivity extends BaseActivity {

	private Intent intent;
	private Bundle bundle;
	private TextView tv_basis_account, tv_currency, tv_dptmnam, tv_modsts,
			tv_opendat, tv_enddat, tv_principal, tv_earnings;// tv_svakind,tv_period,tv_interest_rates,tv_expsumamt
	private Button bt_withdrawal;
	private Basis basis;
	private SuccessHintDialog hintDialog;
	private OneButtonDialogWarn warnDialog;
	private RichTreasureBean treasureBean;
	private String mobile, dptlogno;

	void initView() {
		treasureBean = ((AppContext) getApplication()).getTreasureBean();
//		mobile = ((AppContext) getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		tv_basis_account = (TextView) findViewById(R.id.tv_basis_account);
		// tv_svakind = (TextView) findViewById(R.id.tv_svakind);
		tv_currency = (TextView) findViewById(R.id.tv_currency);
		tv_dptmnam = (TextView) findViewById(R.id.tv_dptmnam);
		tv_modsts = (TextView) findViewById(R.id.tv_modsts);
		tv_opendat = (TextView) findViewById(R.id.tv_opendat);
		tv_enddat = (TextView) findViewById(R.id.tv_enddat);
		tv_principal = (TextView) findViewById(R.id.tv_principal);
		// tv_period = (TextView) findViewById(R.id.tv_period);
		// tv_interest_rates = (TextView) findViewById(R.id.tv_interest_rates);
		tv_earnings = (TextView) findViewById(R.id.tv_earnings);
		// tv_expsumamt = (TextView) findViewById(R.id.tv_expsumamt);
		bt_withdrawal = (Button) findViewById(R.id.bt_withdrawal);
		((TextView) findViewById(R.id.tv_title_contre)).setText("VIP详细信息");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		bt_withdrawal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntoWithdrawalTask intoWithdrawalTask = new IntoWithdrawalTask();
				intoWithdrawalTask.execute(HttpUrls.CURRENT_TRANSFER + "",
						mobile, dptlogno);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		intent = getIntent();
		bundle = intent.getExtras();
		basis = (Basis) bundle.getSerializable("basis");
		setContentView(R.layout.basis_account_info);
		initView();
		dptlogno = basis.getPositionList().get("DPTLOGNO").toString();
		tv_basis_account.setText(basis.getPositionList().get("DPTLOGNO")
				.toString());
		String svakind = basis.getPositionList().get("SVAKIND").toString();
		String currency = basis.getPositionList().get("CURRENCY").toString();
		if (currency.equals("01")) {
			tv_currency.setText("人民币");
		} else if (currency.equals("02")) {
			tv_currency.setText("外币");
		}

		// if (svakind.equals("01")) {
		// tv_svakind.setText("整存整取");
		// } else if (svakind.equals("02")) {
		// tv_svakind.setText("零存整取");
		// } else if (svakind.equals("03")) {
		// tv_svakind.setText("存本取息");
		// } else {
		// tv_svakind.setText("活期存款");
		// }

		tv_dptmnam.setText(basis.getPositionList().get("DPTMNAM").toString());
		String sts = basis.getPositionList().get("MODSTS").toString();
		if ("1".equals(sts)) {
			tv_modsts.setText("正常");
		} else if ("2".equals(sts)) {
			tv_modsts.setText("停息");
		} else if ("3".equals(sts)) {
			tv_modsts.setText("冻结");
		} else if ("4".equals(sts)) {
			tv_modsts.setText("失败");
		}

		String opDate = DateUtil
				.strToFormatStr(basis.getPositionList().get("OPENDAT")
						.toString(), "yyyy-MM-dd");
		tv_opendat.setText(opDate);
		String enDate = DateUtil.strToFormatStr(
				basis.getPositionList().get("ENDDAT").toString(), "yyyy-MM-dd");
		tv_enddat.setText(enDate);
		Double principal = Double.parseDouble(basis.getPositionList()
				.get("PRINCIPAL").toString());
		tv_principal.setText(principal / 100 + "元");
		// tv_period.setText(basis.getPositionList().get("DPTCYCLE").toString()
		// + "个月");
		// Double dptrate = Double.parseDouble(basis.getPositionList()
		// .get("DPTRATE").toString());
		// tv_interest_rates.setText(dptrate / 100 + "元");
		Double expinterest = Double.parseDouble(basis.getPositionList()
				.get("EXPINTEREST").toString());
		tv_earnings.setText(expinterest / 100 + "元");

		// Double expsumamt = Double.parseDouble(basis.getPositionList()
		// .get("EXPSUMAMT").toString());
		// tv_expsumamt.setText(expsumamt / 100 + "元");
	}

	/**
	 * 取出到活期
	 * 
	 * @author liangge
	 * 
	 */
	class IntoWithdrawalTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2] };
			return NetCommunicate.getMidatc(HttpUrls.CURRENT_TRANSFER, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {

					treasureBean.setAvaamt(result.get("TOTAMT").toString());
					treasureBean.setAvaamt(result.get("AVAAMT").toString());
					treasureBean.setCheckamt(result.get("CHECKAMT").toString());
					treasureBean.setFixamt(result.get("FIXAMT").toString());
					((AppContext) getApplication())
							.setTreasureBean(treasureBean);

					hintDialog = new SuccessHintDialog(
							BasisDetailedInfoActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									switch (v.getId()) {
									case R.id.buttonOk:
										hintDialog.dismiss();
										// 取消Intent动画
										Intent intent = new Intent(
												BasisDetailedInfoActivity.this,
												RichTreasureActivity.class);
										startActivity(intent);
										break;
									}
								}
							});
					hintDialog.setCancelable(false);
					hintDialog
							.setOnKeyListener(new DialogInterface.OnKeyListener() {
								@Override
								public boolean onKey(DialogInterface dialog,
										int keyCode, KeyEvent event) {
									if (keyCode == KeyEvent.KEYCODE_SEARCH) {
										return true;
									} else {
										return true; // 默认返回 false
									}
								}
							});
					hintDialog.setCanceledOnTouchOutside(false);
					hintDialog.show();
				} else {
					warnDialog = new OneButtonDialogWarn(
							BasisDetailedInfoActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(BasisDetailedInfoActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
}
