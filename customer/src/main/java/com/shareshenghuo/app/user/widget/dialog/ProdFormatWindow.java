package com.shareshenghuo.app.user.widget.dialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.OrderProdInfo;
import com.shareshenghuo.app.user.network.bean.ProdFormatInfo;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.AddToCartRequest;
import com.shareshenghuo.app.user.network.request.ProdDetailRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.ProdFormatResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.TransferTempDataUtil;
import com.shareshenghuo.app.user.widget.NumberPickLayout;
import com.shareshenghuo.app.user.widget.XCFlowLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ProdFormatWindow extends CommonDialog implements OnClickListener {
	
	private ImageView ivPhoto;
	private TextView tvPrice;
	private TextView tvRepertory;
	private LinearLayout llFormatContainer;
	private List<XCFlowLayout> flowFormats;	// 商品规格流式布局
	private NumberPickLayout npCount;
	
	private ProdInfo prodInfo;
	
	private ProdFormatInfo selectedFormatInfo = null;	// 待买规格
	
	private RadioButton[] selectedBtns; //各层级规格选中按钮
	
	private boolean noCart;

	public ProdFormatWindow(Context context, ProdInfo prodInfo, boolean noCart) {
		super(context, R.layout.dlg_prod_format, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.prodInfo = prodInfo;
		this.noCart = noCart;
	}

	@Override
	public void initDlgView() {
		ivPhoto = getView(R.id.ivProdFormatPhoto);
		tvPrice = getView(R.id.tvProdFormatPrice);
		tvRepertory = getView(R.id.tvProdFormatRepertory);
		npCount = getView(R.id.npProdCount);
		
		llFormatContainer = getView(R.id.llProdFormatContainer);
		flowFormats = new ArrayList<XCFlowLayout>();
		selectedBtns = new RadioButton[5];
		
		npCount.setMinNumber(1);
		
		ImageLoadManager.getInstance(context).displayImage(prodInfo.thum_photo, ivPhoto);
		tvPrice.setText("¥"+prodInfo.default_new_price);
		tvRepertory.setText("库存"+prodInfo.all_product_repertory);
		
		getView(R.id.btnClose).setOnClickListener(this);
		getView(R.id.btnAddToCart).setOnClickListener(this);
		getView(R.id.btnBuyItNow).setOnClickListener(this);
		
		if(noCart)
			getView(R.id.btnAddToCart).setVisibility(View.GONE);
		
		loadData();
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(context, "");
		ProdDetailRequest req = new ProdDetailRequest();
		req.product_id = prodInfo.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_FORMAT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(context);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdFormatResponse bean = new Gson().fromJson(resp.result, ProdFormatResponse.class);
				if(Api.SUCCEED==bean.result_code) {
					updateProdFormat(bean.data, 0);
					
					if(flowFormats.size()>0 && flowFormats.get(0).getChildCount()>0) {
						((RadioButton) flowFormats.get(0).getChildAt(0)).setChecked(true);
					}
					if(flowFormats.size()>1 && flowFormats.get(1).getChildCount()>0) {
						((RadioButton) flowFormats.get(1).getChildAt(0)).setChecked(true);
					}
				} else {
					T.showShort(context, bean.result_desc);
					odismiss();
				}
			}
		});
	}
	
	public void updateProdFormat(List<ProdFormatInfo> data, int index)  {
		if(flowFormats.size() <= index) {
			XCFlowLayout flow = new XCFlowLayout(context);
			MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        lp.topMargin = BitmapTool.dp2px(context, 6);
	        lp.bottomMargin = BitmapTool.dp2px(context, 6);
	        flowFormats.add(flow);
	        llFormatContainer.addView(flow, lp);
		} else {
			flowFormats.get(index).removeAllViews();
		}
		
		if(data==null || data.size()<=0)
			return;
		
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, BitmapTool.dp2px(context, 30));
        lp.leftMargin = 20;
        lp.topMargin = 20;
		for(ProdFormatInfo item : data) {
			RadioButton btn = (RadioButton) LayoutInflater.from(context).inflate(R.layout.rb_prod_format, null);
            btn.setText(item.format_name);
            btn.setOnCheckedChangeListener(new OnFormatCheckListener(item, index));
            flowFormats.get(index).addView(btn,lp);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnClose:
			odismiss();
			break;
			
		case R.id.btnAddToCart:
			if(checkFormat())
				addToCart();
			break;
			
		case R.id.btnBuyItNow:
			if(checkFormat())
				buyItNow();
			break;
		}
	}
	
	public boolean checkFormat() {
		if(selectedFormatInfo == null) {
			T.showShort(context, "请选择商品规格");
			return false;
		}
		
		if(npCount.getCurNumber() > selectedFormatInfo.product_repertory) {
			T.showShort(context, "库存不足");
			return false;
		}
		
		return true;
	}
	
	public void addToCart() {
		ProgressDialogUtil.showProgressDlg(context, "");
		AddToCartRequest req = new AddToCartRequest();
		req.user_id = UserInfoManager.getUserId(context)+"";
		req.product_id = prodInfo.id+"";
		req.shop_id = prodInfo.shop_id+"";
		req.product_count = npCount.getCurNumber()+"";
		if(selectedFormatInfo.parent_id == 0) {
			req.product_format_id = selectedFormatInfo.id+"";
		} else {
			req.product_format_id = selectedFormatInfo.parent_id+"";
			req.child_format_id = selectedFormatInfo.id+"";
		}
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_TO_CART, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(context, "添加购物车成功");
					odismiss();
				} else {
					T.showShort(context, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(context);
			}
		});
	}
	
	private String[] items = {"外卖订单", "到店消费"};
	
	public void buyItNow() {
		new AlertDialog.Builder(context).setTitle("请选择消费类型")
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int which) {
				List<OrderProdInfo> data = new ArrayList<OrderProdInfo>();
				OrderProdInfo info = new OrderProdInfo();
				info.product_id = prodInfo.id;
				info.product_name = prodInfo.product_name;
				info.product_count = npCount.getCurNumber();
				info.per_price = selectedFormatInfo.product_new_price;
				info.thum_photo = selectedFormatInfo.thum_photo;
				if(selectedFormatInfo.parent_id == 0) {
					info.product_format_id = selectedFormatInfo.id+"";
				} else {
					info.product_format_id = selectedFormatInfo.parent_id+"";
					info.child_format_id = selectedFormatInfo.id+"";
				}
				data.add(info);
				TransferTempDataUtil.getInstance().setData(data);
				
//				Intent it = new Intent(context, SubmitOrderActivity.class);
//				it.putExtra("orderType", which+1);
//				it.putExtra("shopId", prodInfo.shop_id);
//				context.startActivity(it);
//				odismiss();
			}
		})
		.show();
	}
	
	private class OnFormatCheckListener implements OnCheckedChangeListener {
		int index;	//规格当前层级
		ProdFormatInfo formatInfo;
		
		public OnFormatCheckListener(ProdFormatInfo formatInfo, int index) {
			this.formatInfo = formatInfo;
			this.index = index;
		}

		@Override
		public void onCheckedChanged(CompoundButton btn, boolean b) {
			if(b) {
				//若选中同层级规格按钮，更新已选中按钮样式
				if(selectedBtns[index]!=null)
					selectedBtns[index].setChecked(false);
				selectedBtns[index] = (RadioButton) btn;
				
				if(formatInfo.child_format_list!=null && formatInfo.child_format_list.size()>0) {
					// 当前规格有子规格
					selectedFormatInfo = null;
					tvPrice.setText("¥"+0);
					tvRepertory.setText("库存"+0);
				} else {
					selectedFormatInfo = formatInfo;
					ImageLoadManager.getInstance(context).displayImage(formatInfo.thum_photo, ivPhoto);
					tvPrice.setText("¥"+formatInfo.product_new_price);
					tvRepertory.setText("库存"+formatInfo.product_repertory);
				}
				updateProdFormat(formatInfo.child_format_list, index+1);
			}
		}
	}


	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
	}
}
