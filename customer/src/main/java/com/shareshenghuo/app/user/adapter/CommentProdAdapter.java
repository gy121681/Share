package com.shareshenghuo.app.user.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.AddPhotoGridAdapter.OnClickAdapterListener;
import com.shareshenghuo.app.user.entity.PhotoBean;
import com.shareshenghuo.app.user.network.bean.OrderProdInfo;
import com.shareshenghuo.app.user.widget.MyGridView;

public class CommentProdAdapter extends CommonAdapter<OrderProdInfo> {
	
	public View[] itemView;
	
	public AddPhotoGridAdapter photoAdapter;
	
	private int index = -1;

	public CommentProdAdapter(Context context, List<OrderProdInfo> data) {
		super(context, data, R.layout.item_prod_comment);
		itemView = new View[data.size()];
	}

	@Override
	public void conver(ViewHolder holder, OrderProdInfo item, final int position) {
		holder.setImageByURL(R.id.ivProdPhoto, item.thum_photo);
		holder.setText(R.id.tvProdName, item.product_name);
		holder.setText(R.id.tvProdPrice, "¥"+item.zhe_kou_price);
		
		MyGridView gvPhoto = holder.getView(R.id.gvProdShow);
		if(gvPhoto.getAdapter() == null) {
			List<PhotoBean> data = new ArrayList<PhotoBean>();
			data.add(new PhotoBean());
			final AddPhotoGridAdapter adapter = new AddPhotoGridAdapter(mContext, data, gvPhoto);
			adapter.setOnClickAdapterListener(new OnClickAdapterListener() {
				@Override
				public void onClicked() {
					photoAdapter = adapter;
				}
			});
			gvPhoto.setAdapter(adapter);
		} else {
			gvPhoto.setAdapter(gvPhoto.getAdapter());
		}
		
		itemView[position] = holder.getConvertView();
		
		EditText edContent = holder.getView(R.id.edProdContent);
		edContent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP)
					index = position;
				return false;
			}
		});
		edContent.clearFocus();
		if(index!=-1 && index==position) {
			edContent.requestFocus();
			edContent.setSelection(edContent.getText().length());
		}
	}
}
