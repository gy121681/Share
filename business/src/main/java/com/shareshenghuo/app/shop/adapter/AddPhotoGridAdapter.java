package com.shareshenghuo.app.shop.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.PhotoBean;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;

public class AddPhotoGridAdapter extends CommonAdapter<PhotoBean> {
	
	private GridView gridView;
	
	public int maxCount = 9;
	
	private OnClickAdapterListener listener;

	public AddPhotoGridAdapter(Context context, List<PhotoBean> data, GridView gridView) {
		super(context, data, R.layout.item_add_photo);
		this.gridView = gridView;
	}

	@Override
	public void conver(ViewHolder holder, final PhotoBean item, final int position) {
		Button btnDel = holder.getView(R.id.btnDelete);
		
		ImageView ivPhoto = holder.getView(R.id.ivPhoto);
		
		ivPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(listener != null)
					listener.onClicked();
				
				if(TextUtils.isEmpty(item.url) && position==getCount()-1)
					new PickPhotoWindow(mContext).showAtBottom();
			}
		});
		
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(getCount() == maxCount)
					mData.add(new PhotoBean());
				mData.remove(item);
				notifyDataSetChanged();
			}
		});
		
		if(TextUtils.isEmpty(item.url) && position==getCount()-1) {
			holder.setImageResource(R.id.ivPhoto, R.drawable.bg_add);
			btnDel.setVisibility(View.GONE);
		} else {
			holder.setImageByURL(R.id.ivPhoto, item.url);
			btnDel.setVisibility(View.VISIBLE);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
				String path = FileUtil.getPath((Activity) mContext, data.getData());
				if (path == null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						if (bmp != null) {
							upPhoto(BitmapTool.Bitmap2File(mContext, bmp));
						}
					}
				} else {
					upPhoto(new File(path));
				}
			}
		}
	}
	
	public void upPhoto(File file) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(mContext, file.getPath(), file.getName(), 65);
            file = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
		ProgressDialogUtil.showProgressDlg(mContext, "上传图片");
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(mContext)+"");
		params.addBodyParameter("1", file);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(mContext);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						T.showShort(mContext, "上传成功");
						mData.get(getCount()-1).url = bean.data.get(0);
						
						if(getCount() < maxCount)
							mData.add(new PhotoBean());
						
						gridView.setAdapter(new AddPhotoGridAdapter(mContext, mData, gridView));
					} else {
						T.showShort(mContext, bean.result_desc);
					}
				}
			}
		});
	}
	
	public String getPhotoUrls() {
		StringBuilder sb = new StringBuilder();
		for(PhotoBean item : mData) {
			if(TextUtils.isEmpty(item.url))
				continue;
			sb.append(item.url).append(",");
		}
		if(sb.length() > 0)
			return sb.substring(0, sb.length()-1);
		return sb.toString();
	}
	
	public void setOnClickAdapterListener(OnClickAdapterListener listener) {
		this.listener = listener;
	}
	
	public interface OnClickAdapterListener {
		public void onClicked();
	}
}
