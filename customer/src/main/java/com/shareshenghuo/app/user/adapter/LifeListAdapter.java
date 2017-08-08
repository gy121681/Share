package com.shareshenghuo.app.user.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.shareshenghuo.app.user.ArticleDetailActivity;
import com.shareshenghuo.app.user.ImagePagerActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class LifeListAdapter extends CommonAdapter<ArticleInfo> {

	public LifeListAdapter(Context context, List<ArticleInfo> data) {
		super(context, data, R.layout.item_article);
	}

	@Override
	public void conver(ViewHolder holder, final ArticleInfo item, int position) {
		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		holder.setText(R.id.tvCommentName, item.nick_name);
		holder.setText(R.id.tvCommentDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvCommentContent, item.content);
		holder.setText(R.id.tvArticleBrowseCount, item.browse_count+"");
		holder.setText(R.id.tvArticleCommentCount, item.comment_count+"");
		holder.setText(R.id.tvArticleLikeCount, item.user_like_count+"");
		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
		GridView gvPhoto = (GridView)holder.getView(R.id.gvLifeItemPhoto);
		if(!TextUtils.isEmpty(item.photo)) {
			final ArrayList<String> photos = new ArrayList<String>();
			String[] urls = item.photo.split(",");
			for(String url : urls)
				photos.add(url);
			gvPhoto.setAdapter(new LifePhotoAdapter(mContext, photos));
			gvPhoto.setOnItemClickListener(new OnItemClickListener() {
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
			gvPhoto.setAdapter(null);
		}
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, ArticleDetailActivity.class);
				it.putExtra("articleInfo", item);
				mContext.startActivity(it);
			}
		});
	}
}
