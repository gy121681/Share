package com.td.qianhai.epay.oem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;

public class CurrentAccountInfoActivity extends BaseActivity {

	private TextView tv_earnings_yesterday, tv_total_assets,
			tv_nearly_week_earnings, tv_in_recent_month_income,
			tv_accumulated_earnings,bt_rich_right;
	private Button into_vip;
	private RichTreasureBean treasureBean;

	// private Intent intent;
	// private Bundle bundle;

	void initView() {
		tv_earnings_yesterday = (TextView) findViewById(R.id.tv_earnings_yesterday);
		tv_total_assets = (TextView) findViewById(R.id.tv_total_assets);
		bt_rich_right = (TextView) findViewById(R.id.bt_title_right);
		tv_nearly_week_earnings = (TextView) findViewById(R.id.tv_nearly_week_earnings);
		tv_in_recent_month_income = (TextView) findViewById(R.id.tv_in_recent_month_income);
		tv_accumulated_earnings = (TextView) findViewById(R.id.tv_accumulated_earnings);
		into_vip = (Button) findViewById(R.id.into_vip);
		bt_rich_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(CurrentAccountInfoActivity.this,
				RichTreasureDealRecordsActivity.class);
				startActivity(intent2);
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
		((TextView) findViewById(R.id.tv_title_contre)).setText("收益明细");
//		withdrawals = (Button) findViewById(R.id.withdrawals);
//		withdrawals.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (treasureBean.getIsActpwout().equals("0")) {
//					Intent intent = new Intent(CurrentAccountInfoActivity.this,
//							WithdrawalActivity.class);
//					// Bundle bundle = new Bundle();
//					// bundle.putSerializable("treasureBean", treasureBean);
//					// intent.putExtras(bundle);
//					startActivity(intent);
//				} else {
//					ToastCustom.showMessage(CurrentAccountInfoActivity.this,
//							"您今天支付密码输错次数超限,请明天再来提现.");
//				}
//			}
//		});

		into_vip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CurrentAccountInfoActivity.this,
						IntoBasisActivity.class);
				startActivity(intent);
			}
		});

		if (treasureBean.getYesterincom().length() == 1) {
			tv_earnings_yesterday
					.setText("0.0" + treasureBean.getYesterincom());
		} else if (treasureBean.getYesterincom().length() == 2) {
			tv_earnings_yesterday.setText("0." + treasureBean.getYesterincom());
		} else {
			tv_earnings_yesterday.setText(treasureBean.getYesterincom()
					.substring(0, treasureBean.getYesterincom().length() - 2)
					+ "."
					+ treasureBean.getYesterincom().substring(
							treasureBean.getYesterincom().length() - 2));
		}
		if (treasureBean.getCheckamt().length() == 1) {
			tv_total_assets.setText("0.0" + treasureBean.getCheckamt());
		} else if (treasureBean.getCheckamt().length() == 2) {
			tv_total_assets.setText("0." + treasureBean.getCheckamt());
		} else {
			tv_total_assets.setText(treasureBean.getCheckamt().substring(0,
					treasureBean.getCheckamt().length() - 2)
					+ "."
					+ treasureBean.getCheckamt().substring(
							treasureBean.getCheckamt().length() - 2));
		}
		if (treasureBean.getWeekincom().length() == 1) {
			tv_nearly_week_earnings
					.setText("0.0" + treasureBean.getWeekincom());
		} else if (treasureBean.getWeekincom().length() == 2) {
			tv_nearly_week_earnings.setText("0." + treasureBean.getWeekincom());
		} else {
			tv_nearly_week_earnings.setText(treasureBean.getWeekincom()
					.substring(0, treasureBean.getWeekincom().length() - 2)
					+ "."
					+ treasureBean.getWeekincom().substring(
							treasureBean.getWeekincom().length() - 2));
		}
		if (treasureBean.getMonthincom().length() == 1) {
			tv_in_recent_month_income.setText("0.0"
					+ treasureBean.getMonthincom());
		} else if (treasureBean.getMonthincom().length() == 2) {
			tv_in_recent_month_income.setText("0."
					+ treasureBean.getMonthincom());
		} else {
			tv_in_recent_month_income.setText(treasureBean.getMonthincom()
					.substring(0, treasureBean.getMonthincom().length() - 2)
					+ "."
					+ treasureBean.getMonthincom().substring(
							treasureBean.getMonthincom().length() - 2));
		}

		if (treasureBean.getCumulative().length() == 1) {
			tv_accumulated_earnings.setText("0.0"
					+ treasureBean.getCumulative());
		} else if (treasureBean.getCumulative().length() == 2) {
			tv_accumulated_earnings
					.setText("0." + treasureBean.getCumulative());
		} else {
			tv_accumulated_earnings.setText(treasureBean.getCumulative()
					.substring(0, treasureBean.getCumulative().length() - 2)
					+ "."
					+ treasureBean.getCumulative().substring(
							treasureBean.getCumulative().length() - 2));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// intent = getIntent();
		// bundle = intent.getExtras();
		// treasureBean = (RichTreasureBean) bundle
		// .getSerializable("treasureBean");
		setContentView(R.layout.current_account_info);
		AppContext.getInstance().addActivity(this);
		// ininView();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		treasureBean = ((AppContext) getApplication()).getTreasureBean();
		initView();
	}
}
