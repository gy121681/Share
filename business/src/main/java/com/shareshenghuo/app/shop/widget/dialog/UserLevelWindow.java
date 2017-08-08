package com.shareshenghuo.app.shop.widget.dialog;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.UserLevelAdapter;
import com.shareshenghuo.app.shop.network.bean.UserLevelInfo;

public class UserLevelWindow extends CommonDialog {
	
	private ListView lvData;
	
	private List<UserLevelInfo> data;
	
	private SelectLevelCallback callback;

	public UserLevelWindow(Context context, List<UserLevelInfo> data) {
		super(context, R.layout.dlg_user_level, 240, LayoutParams.WRAP_CONTENT);
		this.data = data;
	}

	@Override
	public void initDlgView() {
		lvData = getView(R.id.lvData);
		lvData.setAdapter(new UserLevelAdapter(context, data));
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(callback != null) {
					callback.selectLevel(position);
				}
				dismiss();
			}
		});
	}
	
	public void setSelectLevelCallback(SelectLevelCallback callback) {
		this.callback = callback;
	}
	
	public interface SelectLevelCallback {
		public void selectLevel(int position);
	}
}
