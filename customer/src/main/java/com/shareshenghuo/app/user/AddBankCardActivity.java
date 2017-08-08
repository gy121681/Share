package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.BankBean;
import com.shareshenghuo.app.user.network.bean.FindBindBean;
import com.shareshenghuo.app.user.network.bean.MyBankCardBean;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.BaseRequest;
import com.shareshenghuo.app.user.network.response.BankInfoResponse;
import com.shareshenghuo.app.user.network.response.GetPaySupportCardResponse;
import com.shareshenghuo.app.user.network.response.MyBankCardResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.networkapi.Request;
import com.shareshenghuo.app.user.networkapi.CallbackObject;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.example.widget.MyTextWatcher;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.sensetime.bankcard.BankCard;
import com.sensetime.bankcard.BankCardActivity;
import com.sensetime.card.CardActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddBankCardActivity extends BaseTopActivity{
	
	private Button llWalletRecharge;
	private EditText eidcard,edname;
	private ImageView img_camara;
	private GridView gv;
	private List<BankBean> banklist;
	private LayoutInflater mInflater;
	private MyAdapter mAdapter;
	private int EXAMPLE_REQUEST_CODE = 1;
	private int scanRectOffset;
	private LinearLayout bank_list;
	private String tag;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.add_bank_cardactivity);
		tag = getIntent().getStringExtra("tag");
		initview();
		scanRectOffset = 0;
		if(!TextUtils.isEmpty(tag)){
			initcardlist();
		}
		initinfo();
	}

	private void initinfo() {
		// TODO Auto-generated method stub
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		
		if(userInfo!=null&&userInfo.person_no!=null&&userInfo.person_no.length()>4){
			String f = userInfo.person_no.substring(0,4);
			String l = userInfo.person_no.substring(userInfo.person_no.length()-4);
			setText(R.id.edname, userInfo.real_name+" ("+f+" **** **** "+l+")");
		}
		
	}

	private void initcardlist() {
		// TODO Auto-generated method stub
		
		BaseRequest req = new BaseRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETPAYSUPPORTCARDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AddBankCardActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				GetPaySupportCardResponse bean = new Gson().fromJson(resp.result, GetPaySupportCardResponse.class);
				
				if(Api.SUCCEED == bean.result_code) {
					initlist(bean.data);
//					if(bean.data.RSPCOD.equals("000000")){
//						upNum(bean.data);
//					}else{
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//					}
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}

		});
		
	}
	
	private void initlist(List<BankBean> data) {
		// TODO Auto-generated method stub
		banklist.addAll(data);
		mAdapter.notifyDataSetChanged();
		bank_list.setVisibility(View.VISIBLE);
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("添加银行卡");
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		img_camara = getView(R.id.img_camara);
		bank_list = getView(R.id.bank_list);
		bank_list.setVisibility(View.GONE);
		edname = getView(R.id.edname);
		eidcard = getView(R.id.eidcard);
		gv = (GridView) findViewById(R.id.gridviews1);
		banklist = new ArrayList<BankBean>();
		eidcard.addTextChangedListener(new MyTextWatcher(eidcard));
		mAdapter = new MyAdapter();
		gv.setAdapter(mAdapter);
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				initdata();
				
			}
		});
		img_camara.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pcard();
			}
		});
	}
	
	private void initdata() {
		// TODO Auto-generated method stub
		
//		if(TextUtils.isEmpty(edname.getText().toString())){
//			T.showShort(getApplicationContext(), "请输入姓名");
//			return;
//		}
		
//		if(ChexText.judgetext(edname.getText().toString())){
////			ViewUtil.showError(card_name,"禁止输入特殊字符");
//			T.showShort(getApplicationContext(), "禁止输入特殊字符");
//			return;
//		}
		
		if(TextUtils.isEmpty(eidcard.getText().toString())){
			T.showShort(getApplicationContext(), "请输入或者扫描卡号");
			return;
		}
		if(eidcard.getText().toString().length()<16){
			T.showShort(getApplicationContext(), "卡号最少16位");
			return;
		}
//        oldRequest();
        requestBankInfo();
	}

    /**
     * 使用新api请求银行卡信息
     */
	private void requestBankInfo(){
        String card_no = eidcard.getText().toString().replace(" ", "");
        ProgressDialogUtil.showProgressDlg(this, "");
        Request.getServiceQueryBankCardInfo(card_no.trim(), UserInfoManager.getUserInfo(this).id + "",
                new CallbackObject<BankInfoResponse>() {
                    @Override
                    public void onSuccess(BankInfoResponse data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        if (data != null) {
                            toNextStep(data.result.nature, data.result.bank);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }
                });
    }

    private void toNextStep(String type, String bankName){
        Intent it = new Intent(AddBankCardActivity.this,AddcardInfoActivity.class);
        if (type.contains("借记") || type.contains("储蓄")){
            type = "1";
        }
        it.putExtra("type", type);
        it.putExtra("card", eidcard.getText().toString().trim());
        it.putExtra("cardname", bankName);
        startActivity(it);
    }

    /**
     * 原请求银行卡
     */
	private void oldRequest(){

        String card_no = eidcard.getText().toString().replace(" ", "");

        ProgressDialogUtil.showProgressDlg(this, "");
        MyBankCardBean req = new MyBankCardBean();
        req.user_id = UserInfoManager.getUserInfo(this).id+"";
        req.user_type = "1";
        req.bank_name = UserInfoManager.getUserInfo(this).real_name;
        req.card_no = card_no.trim();
        Log.e("", ""+req.toString());

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.GETBANKNAME, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(AddBankCardActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", ""+resp.result);
                MyBankCardResponse bean = new Gson().fromJson(resp.result, MyBankCardResponse.class);
                if(Api.SUCCEED == bean.result_code) {
//					if(bean.data.RSPCOD.equals("000000")){
                    if(!TextUtils.isEmpty(tag)&&tag.equals("1")){
                        if(bean.data.is_quickpay!=null&&bean.data.is_quickpay.equals("1")){
                            upNum(bean.data);
                            finish();
                        }else{
                            T.showShort(getApplicationContext(), "此卡不支持快捷支付,请绑定其他卡");
                        }
                    }else if(!TextUtils.isEmpty(tag)&&tag.equals("2")){
                        if(bean.data.is_support_withdraw!=null&&bean.data.is_support_withdraw.equals("1")){
                            upNum(bean.data);
                            finish();
                        }else{
                            T.showShort(getApplicationContext(), "此卡不支持提现,请绑定其他卡");
                        }
                    }else{
                        upNum(bean.data);
                        finish();
                    }

//					}else{
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//					}
                }else{
                    T.showShort(getApplicationContext(), "网络异常");
                }
            }
        });
    }

	private void upNum(FindBindBean data) {
		// TODO Auto-generated method stub
		Intent it = new Intent(AddBankCardActivity.this,AddcardInfoActivity.class);
		it.putExtra("type", data.card_type);
		it.putExtra("card", eidcard.getText().toString().trim());
		it.putExtra("cardname", data.bank_name);
		startActivity(it);
	}
	
	
	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return banklist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return banklist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item, null);
				holder = new ViewHolder();
				holder.textview1 = (TextView) convertView.findViewById(R.id.textview1);
				holder.textview2 = (TextView) convertView.findViewById(R.id.textview2);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
				convertView.setTag(holder);
				
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String  text = banklist.get(position).bank_name;
			if(text!=null){
				ViewUtil.setbank(holder.image, text);
			}
			if(banklist.get(position).card_type!=null){
				String type  = banklist.get(position).card_type;
				if(type.equals("1")){
					holder.textview1.setVisibility(View.VISIBLE);
					holder.textview2.setVisibility(View.GONE);
					holder.textview1.setText(text+"储蓄卡");
				}else if(type.equals("2")){
					holder.textview2.setVisibility(View.VISIBLE);
					holder.textview1.setVisibility(View.GONE);
					holder.textview2.setText(text+"信用卡");
				}else if(type.equals("3")){
					holder.textview1.setVisibility(View.VISIBLE);
					holder.textview2.setVisibility(View.VISIBLE);
					holder.textview1.setText(text+"储蓄卡");
					holder.textview2.setText(text+"信用卡");
				}
			}else{
				holder.textview2.setVisibility(View.GONE);
				holder.textview1.setText(text);
			}
			
			
			return convertView;
		}

		class ViewHolder {
			public TextView textview1,textview2;
			public ImageView image;
		}
	}
	
	public void pcard(){
		Intent scanIntent = new Intent(AddBankCardActivity.this, BankCardActivity.class);
		scanIntent.putExtra(CardActivity.EXTRA_BACK_DRAWABLE_ID, R.drawable.scan_back);

		scanIntent.putExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED, true);
		/**返回矫正过的图像或者返回原图（可选）
		 * *设置返回矫正过的图像，使用BankCardActivity.EXTRA_CARD_IMAGE_RECTIFIED
		 * *设置为返回原图，使用BankCardActivity.EXTRA_CARD_IMAGE
		 */		
		scanIntent.putExtra(CardActivity.EXTRA_SCAN_ORIENTATION, CardActivity.ORIENTATION_PORTRAIT);
		scanIntent.putExtra(CardActivity.EXTRA_SCAN_RECT_OFFSET, scanRectOffset);
		/**竖屏模式下支持扫描框位置微调
		 * *通过设置参数BankCardActivity.EXTRA_SCAN_RECT_OFFSET（int）微调位置
		 * *阈值为-75到75；+75表示向上移动的最大值，-75表示向下移动的最大值
		 */
		scanIntent.putExtra(CardActivity.EXTRA_SCAN_TIPS, "请将银行卡正面放入扫描框内");				
		startActivityForResult(scanIntent, EXAMPLE_REQUEST_CODE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
		case CardActivity.RESULT_CARD_INFO: {
			BankCard bankCard = data.getParcelableExtra(CardActivity.EXTRA_SCAN_RESULT);
			byte[] imageBytes = data.getByteArrayExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED);
			if (imageBytes != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

				//获取imageBytes图像数据后，可使用bitmap工具类处理
				Bitmap.Config bitmapConfig = bitmap.getConfig();
				// set default bitmap config if none
				if (bitmapConfig == null) {
					bitmapConfig = Bitmap.Config.ARGB_8888;
				}
				// resource bitmaps are imutable, so we need to convert it to
				// mutable one
				bitmap = bitmap.copy(bitmapConfig, true);
//				bankcardImageView.setImageBitmap(bitmap);
				
//				int width = bitmap.getWidth();
//				int height = bitmap.getHeight();
//				numRectPos是银行卡号所在位置，相对矫正后的银行卡图像。
//				numRectPos[0]对应bottom，numRectPos[1]对应left，numRectPos[2]对应right，numRectPos[4]对应top
//				int[] numRectPos = bankCard.getNumRectPos();
//				Bitmap cardNumImage =Bitmap.createBitmap(bitmap, numRectPos[1], numRectPos[3], numRectPos[2]-numRectPos[1], numRectPos[0]-numRectPos[3]);
//				cardNumImageView.setImageBitmap(cardNumImage);
			}

//			bankNameTextView.setText(bankCard.getBankName());
//			bankIDtTextView.setText(bankCard.getBankIdentificationNumber());
//			cardNameTextView.setText(bankCard.getCardName());
//			cardTypeTextView.setText(bankCard.getCardType());
			
			if (bankCard == null) {
				Toast.makeText(getApplicationContext(), "银行卡识别结果出现异常", Toast.LENGTH_LONG).show();
			} else {
				//StringBuffer cardNumber = new StringBuffer(bankCard.getNumber());
				StringBuffer cardNumber = new StringBuffer();
				int group = bankCard.getNumber().length() %4 == 0 ? bankCard.getNumber().length()/4: bankCard.getNumber().length()/4 + 1;
				for (int i=0; i < group; i++) {
					if (i == group - 1) {
						cardNumber.append(bankCard.getNumber().substring(i*4, bankCard.getNumber().length()));
					} else {
						cardNumber.append(bankCard.getNumber().substring(i*4, i*4+4));
					}
					cardNumber.append(" ");
				}
				eidcard.setText(cardNumber.toString());
			}
		}
			break;
		case CardActivity.RESULT_CAMERA_NOT_AVAILABLE: {
			Toast.makeText(getApplicationContext(), "摄像头不可用，或用户拒绝授权使用", Toast.LENGTH_LONG).show();
		}
			break;
		case Activity.RESULT_CANCELED:
//			Toast.makeText(getApplicationContext(), "扫描被取消", Toast.LENGTH_LONG).show();
			break;
		case CardActivity.RESULT_RECOGNIZER_INIT_FAILED:
			Toast.makeText(getApplicationContext(), "算法SDK初始化失败：可能是模型路径错误，SDK权限过期，包名绑定错误", Toast.LENGTH_LONG).show();
			break;
		case CardActivity.RESULT_AUTHENTICATION_FAILED:
			Toast.makeText(getApplicationContext(), "API账户验证错误：请检查网络以及您的API ID和API Secret信息", Toast.LENGTH_LONG).show();
			break;
		default:
			Toast.makeText(getApplicationContext(), "未知结果", Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	
}
