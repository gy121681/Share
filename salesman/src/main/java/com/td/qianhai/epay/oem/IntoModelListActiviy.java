package com.td.qianhai.epay.oem;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.adapter.IntoBasisListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.BasisModel;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class IntoModelListActiviy extends BaseActivity {
	private ListView listView;
	private IntoBasisListAdapter adapter;
	private BasisModel basisModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rich_treasure_detail);
		AppContext.getInstance().addActivity(this);
		((TextView) findViewById(R.id.tv_title_contre)).setText("定存模式列表");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		listView = (ListView) findViewById(R.id.basis_list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				intent.putExtra("basis", basisModel.list.get(position));
				setResult(8, intent);
				finish();
			}
		});
		LoadOfListTask ofListTask = new LoadOfListTask();
		ofListTask.execute(HttpUrls.BASIS_LIST + "");
	}

	/**
	 * 获取定期模式列表
	 * 
	 * @author liangge
	 * 
	 */
	class LoadOfListTask extends AsyncTask<String, Integer, BasisModel> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected BasisModel doInBackground(String... params) {
			String[] values = { params[0] };
			return NetCommunicate.getBasisModel(HttpUrls.BASIS_LIST, values);
		}

		@Override
		protected void onPostExecute(BasisModel result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.getRspcod().equals(Entity.STATE_OK)) {
					adapter = new IntoBasisListAdapter(
							IntoModelListActiviy.this, result.list);
					basisModel = result;
					if (adapter != null)
						listView.setAdapter(adapter);
//					else
//						ToastCustom.showMessage(IntoModelListActiviy.this,
//								"定存模式获取失败");
				} else {
//					ToastCustom.showMessage(IntoModelListActiviy.this,
//							"定存模式获取失败");
				}
			} else {

			}
			super.onPostExecute(result);
		}
	}
}