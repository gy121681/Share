package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.SignInfo;

/**
 * @author hang
 * 签到记录
 */
public class SignLogAdapter extends CommonAdapter<SignInfo> {
	
	private int signCount;
	private int offset;

	public SignLogAdapter(Context context, List<SignInfo> data, int signCount) {
		super(context, data, R.layout.item_sign_log);
		this.signCount = signCount;
		offset = (signCount / 7) * 7 + 1;
	}

	@Override
	public void conver(ViewHolder holder, SignInfo item, int position) {
		boolean isSigned = (position+offset) <= signCount;
		holder.getConvertView().setBackgroundResource(isSigned? R.drawable.bg_sign_sel : R.drawable.bg_sign_nor);
		holder.getView(R.id.tvSignItemDay).setEnabled(isSigned);
		holder.setText(R.id.tvSignItemDay, "第"+(position+offset)+"天");
		holder.setText(R.id.tvSignItemNum, "生活币"+item.pointCount);
	}
}
