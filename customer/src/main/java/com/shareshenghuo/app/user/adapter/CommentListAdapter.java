package com.shareshenghuo.app.user.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar;

import com.shareshenghuo.app.user.ImagePagerActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CommentInfo;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.widget.MyGridView;

public class CommentListAdapter extends CommonAdapter<CommentInfo> {

	public CommentListAdapter(Context context, List<CommentInfo> data) {
		super(context, data, R.layout.item_article);
	}

	@Override
	public void conver(ViewHolder holder, CommentInfo item, int position) {
		holder.getView(R.id.llCommentBottomData).setVisibility(View.GONE);
		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		holder.setText(R.id.tvCommentName, item.user_name);
		holder.setText(R.id.tvCommentDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvCommentContent, item.content);
		
		RatingBar rtScore = holder.getView(R.id.rtCommentScore);
		rtScore.setVisibility(item.score>0? View.VISIBLE : View.GONE);
		rtScore.setProgress(item.score);
		
		if(TextUtils.isEmpty(item.reply_content)) {
			holder.getView(R.id.llReplyContainer).setVisibility(View.GONE);
		} else {
			holder.getView(R.id.llReplyContainer).setVisibility(View.VISIBLE);
			holder.setText(R.id.tvReplyContent, "商家回复："+item.reply_content);
			holder.setText(R.id.tvReplyTime, DateUtil.getTime(item.reply_time, 0));
		}
		
		MyGridView gridPhoto = holder.getView(R.id.gvLifeItemPhoto);
		if(!TextUtils.isEmpty(item.comment_photo)) {
			final ArrayList<String> photos = new ArrayList<String>();
			Collections.addAll(photos, item.comment_photo.split(","));
			gridPhoto.setAdapter(new LifePhotoAdapter(mContext, photos));
			gridPhoto.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
					Intent it = new Intent(mContext, ImagePagerActivity.class);
					it.putExtra("title", "浏览");
					it.putExtra("position", position);
					it.putStringArrayListExtra("urls", photos);
					mContext.startActivity(it);
				}
			});
		} else {
			gridPhoto.setAdapter(null);
		}
	}
}
