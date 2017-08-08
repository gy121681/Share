package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.FeedBackListBean;
import com.shareshenghuo.app.shop.util.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;


public class SysMsgfeedbackAdapter extends CommonAdapter<FeedBackListBean> {

	public SysMsgfeedbackAdapter(Context context, List<FeedBackListBean> data) {
		super(context, data, R.layout.feedback_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final FeedBackListBean item, int position) {
		holder.setText(R.id.tvMsgTime, DateUtil.getTime(item.create_time));
		holder.setText(R.id.tvMsgContent, "反馈回复: \n"+item.reply_context);
		holder.setText(R.id.tvMsgIsReply, "我的反馈: "+item.content);
		
		
		
//		if(item.user_id!=0 && item.is_push==0) {
//			//帖子回复
//			holder.setText(R.id.tvMsgName, item.reply_person);
//			holder.getView(R.id.tvMsgName).setVisibility(View.VISIBLE);
//			holder.getView(R.id.tvMsgIsReply).setVisibility(View.VISIBLE);
//		} else {
//			holder.getView(R.id.tvMsgName).setVisibility(View.GONE);
//			holder.getView(R.id.tvMsgIsReply).setVisibility(View.GONE);
//		}
//		
//		holder.getConvertView().setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if(item.user_id!=0 && item.is_push==0) {
//					Intent it = new Intent(mContext, ReplyActivity.class);
//					it.putExtra("life_circle_id", item.life_clrcle_id);
//					it.putExtra("reply_id", item.reply_id);
//					it.putExtra("reply_name", item.reply_person);
//					mContext.startActivity(it);
//				}
//			}
//		});
	}
}
