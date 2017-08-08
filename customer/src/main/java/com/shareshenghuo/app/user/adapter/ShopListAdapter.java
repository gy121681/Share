package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.networkapi.Api;
import com.squareup.picasso.Picasso;

public class ShopListAdapter extends CommonAdapter<ShopInfo> {
	public Context context;
	public ShopListAdapter(Context context, List<ShopInfo> data) {
		super(context, data, R.layout.item_shop);
		this.context = context;
	}

	@Override
	public void conver(ViewHolder holder, ShopInfo item, int position) {
		
		holder.setText(R.id.area_name, item.area_name);
		holder.setText(R.id.child_name, item.shop_child_type_name);
		
		ImageView img = holder.getView(R.id.ivShopLogo);
		 Picasso.with(context).load(Api.IMG_HOST+item.logo).placeholder(R.drawable.share_c_business_details_photo_default).resize(180,180).centerCrop().into(img);
//		 Picasso.with(context).load(Api.IMG_HOST+item.logo).into(img);
		
//		imageView.loadImage(imageLoader, url);
//		
//		ImageLoader imageLoader = ImageLoaderFatory.create(context);
		
//		holder.displayfi(R.id.ivShopLogo, item.logo);
		
//		holder.setImageByURL(R.id.ivShopLogo, item.logo);
		holder.setText(R.id.tvShopName, item.shop_name);
		holder.setText(R.id.tvShopContent, item.introduction);
		holder.setText(R.id.tvShopBrowseCount, item.browse_count+"");
		holder.setText(R.id.tvShopCommentCount, item.comment_count+"");
		if(item.consumption_per_person==null||item.consumption_per_person.equals("null")){
			holder.setText(R.id.tvShopPerFee, "人均¥0.00");
		}else{
			holder.setText(R.id.tvShopPerFee, "人均¥"+item.consumption_per_person);
		}
	
		if(item.range > 1)
			holder.setText(R.id.tvShopRange, item.range+"km");
		else
			holder.setText(R.id.tvShopRange, (item.range*1000)+"m");
		
		holder.getView(R.id.ivShopTagHui).setVisibility(item.is_have_active==1? View.VISIBLE:View.GONE);
		holder.getView(R.id.ivShopTagQuan).setVisibility(item.is_have_coupon==1? View.VISIBLE:View.GONE);
		holder.getView(R.id.ivShopTagQing).setVisibility(item.is_muslim==1? View.VISIBLE:View.GONE);
		holder.getView(R.id.ivShopTagSong).setVisibility(item.is_push==1? View.VISIBLE:View.GONE);
		holder.getView(R.id.ivShopTagIntegrity).setVisibility(item.is_integrity==1? View.VISIBLE:View.GONE);
		holder.getView(R.id.ivShopTagAuth).setVisibility(item.is_authentication==1? View.VISIBLE:View.GONE);

		holder.getView(R.id.ivShopTagHui).setVisibility(View.GONE);
		holder.getView(R.id.ivShopTagQuan).setVisibility(View.GONE);
		holder.getView(R.id.ivShopTagQing).setVisibility(View.GONE);
		holder.getView(R.id.ivShopTagSong).setVisibility(View.GONE);

		holder.getView(R.id.ivintegral).setVisibility(View.GONE);
		holder.getView(R.id.ivShopTagAuth).setVisibility(View.GONE);
		holder.getView(R.id.ivShopTagIntegrity).setVisibility(View.GONE);
		holder.getView(R.id.ivintegral1).setVisibility(View.GONE);

		ImageView imag = holder.getView(R.id.ivintegral);
//		if(item.is_consumption!=null&&item.is_consumption.equals("1")){
//			holder.getView(R.id.ivintegral1).setVisibility(View.VISIBLE);
//		}else{
//			holder.getView(R.id.ivintegral1).setVisibility(View.GONE);
//		}
		
		if(item.is_discount_type_three.equals("1")){
			holder.getView(R.id.ivintegral).setVisibility(View.VISIBLE);
			imag.setImageResource(R.drawable.share_c_business_label_100);
			return;

		}
		if(item.is_discount_type_two.equals("1")){
			holder.getView(R.id.ivintegral).setVisibility(View.VISIBLE);
			imag.setImageResource(R.drawable.share_c_business_label_50);
			return;
		}
		if(item.is_discount_type_one.equals("1")){
			holder.getView(R.id.ivintegral).setVisibility(View.VISIBLE);
			imag.setImageResource(R.drawable.share_c_business_label_25);
			return;
		}
		


		
//		holder.getView(R.id.ivintegral).setVisibility(item.is_discount_type_one==1? View.VISIBLE:View.GONE);
//		holder.getView(R.id.ivintegral).setVisibility(item.is_discount_type_two==1? View.VISIBLE:View.GONE);
//		holder.getView(R.id.ivintegral).setVisibility(item.is_discount_type_one==1? View.VISIBLE:View.GONE);
		
	}
}
