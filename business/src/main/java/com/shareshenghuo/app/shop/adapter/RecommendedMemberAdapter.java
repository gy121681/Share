package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.Recommendedmember;
import com.shareshenghuo.app.shop.util.DateUtil;

/**
 * @author hang
 * 收藏的商家
 */
public class RecommendedMemberAdapter extends CommonAdapter<Recommendedmember> {

	public RecommendedMemberAdapter(Context context, List<Recommendedmember> data) {
		super(context, data, R.layout.item_favority_member);
	}

	@Override
	public void conver(ViewHolder holder, final Recommendedmember item, int position) {
		holder.setImageResource(R.id.ivShopLogo, R.drawable.share_b_mine_head_moren);
		if(holder.getView(R.id.ivShopLogo).getTag()!=null&&holder.getView(R.id.ivShopLogo).getTag().equals(R.id.ivShopLogo)){
			holder.setImageByURL(R.id.ivShopLogo, item.user_photo);
		}else{
			holder.setImageByURL(R.id.ivShopLogo, item.user_photo);
			holder.getView(R.id.ivShopLogo).setTag(R.id.ivShopLogo);  
		}
		
		String names =""; 
		if(!TextUtils.isEmpty( item.real_name)){
			names =  item.real_name;
		}else if(!TextUtils.isEmpty( item.nick_name)){
			names =  item.nick_name;
		}else{
			names =  "未实名认证";
		}
		holder.setText(R.id.name, names);
		holder.setText(R.id.tvShopName, DateUtil.getTime(item.create_time,2));
		if(!TextUtils.isEmpty( item.account)){
			holder.setText(R.id.tvShopCollectCount, item.account);
		}else{
			holder.setText(R.id.tvShopCollectCount, "未知");
		}
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
		
		holder.getView(R.id.btnShopCollect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				new AlertDialog.Builder(mContext).setMessage("是否取消关注？")
//					.setNegativeButton("否", null)
//					.setPositiveButton("取消关注", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							cancelCollect(item);
//						}
//					})
//					.show();
			}
		});
	}
	
//	public void cancelCollect(final FavorityShopInfo item) {
//		ProgressDialogUtil.showProgressDlg(mContext, "");
//		CollectRequest req = new CollectRequest();
//		req.collect_id = item.shop_id+"";
//		req.user_id = UserInfoManager.getUserId(mContext)+"";
//		req.collect_type = "1";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_COLLECT, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(mContext, "成功");
//					remove(item);
//					notifyDataSetChanged();
//				} else {
//					T.showShort(mContext, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(mContext);
//			}
//		});
//	}
}
