package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;

public class AddOilCardActivity extends BaseTopActivity {
	private EditText card_name,card_1,card_2;
	private TextView  tvs,commit;
	private ImageView img_sh;
	private String cardname = "",cardnum1 = "",cardnum2 = "";
	private  String[] selectPicTypeStr;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.add_oilcard_activity);
//		initview();
	}

//	private void initview() {
//		// TODO Auto-generated method stub
//		initTopBar("添加油卡");
//		card_name = getView(R.id.card_name);
//		card_1 = getView(R.id.card_1);
//		card_2 = getView(R.id.card_2);
//		card_2.setTextIsSelectable(false);
//		img_sh = getView(R.id.img_sh);
//		commit = getView(R.id.commit);
//		tvs = getView(R.id.tvs);
////		cardchannelName = getView(R.id.cardchannelName);
//		commit.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				initdata();
//			}
//		});
//		
//		card_2.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					cardnum1 = card_1.getText().toString();
//					if(cardnum1.length()>=16){
//						card_1.setText(cardnum1.substring(0,4)+"**** ****"+cardnum1.substring(cardnum1.length()-4));
//					}else{
//						T.showShort(getApplicationContext(), "卡号长度16-25位");
//					}
//				}else{
//					card_1.setText(cardnum1);
//					if(!card_2.getText().toString().equals(cardnum1)){
//						T.showShort(getApplicationContext(), "两次卡号不一致");
//					}
//				}
//			}
//		});
//		card_1.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					cardnum2 = card_2.getText().toString();
//					if(cardnum2.length()>=16){
//						card_2.setText(cardnum2.substring(0,4)+"**** ****"+cardnum2.substring(cardnum2.length()-4));
//					}
//				}else{
//					card_2.setText(cardnum2);
//				}
//			}
//		});
//		
//		tvs.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(selectPicTypeStr!=null&&selectPicTypeStr.length>0){
//					initdialog(selectPicTypeStr,tvs);
//				}else{
//					init();
//				}
//			}
//		});
//		card_1.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(s.length()>5){
//					int a = getcardchannelName(card_1.getText().toString().trim());
//					switch (a) {
//					case 1:
//						img_sh.setVisibility(View.VISIBLE);
////						sp.setSelection(1,true);
//						tvs.setText("中国石油");
//						img_sh.setImageResource(R.drawable.sy_logo_bg);
//						break;
//					case 2:
////						sp.setSelection(2,true);
//						img_sh.setVisibility(View.VISIBLE);
//						tvs.setText("中国石化");
//						img_sh.setImageResource(R.drawable.sh_logo_bg);
//						break;
//					case 3:
////						cardchannelName.setText("中国石化");
////						img_sh.setImageResource(R.drawable.sh_logo_bg);
//						break;
//					default:
//						break;
//					}
//				}else{
////					cardchannelName.setText("");
////					img_sh.setVisibility(View.GONE);
//				}
//				
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
//	
//	private void initdata() {
//		// TODO Auto-generated method stub
////		card_2.setText(cardnum2);
////		card_1.setText(cardnum1);
//		
////		if(ViewUtil.checkEditEmpty(card_1, "请输入卡号")){
////			T.showShort(getApplicationContext(), "请输入卡号");
////			return;
////		}
//		card_1.clearFocus();
//		card_2.clearFocus();
//		card_name.requestFocus();
//		
//		if(TextUtils.isEmpty(card_1.getText().toString())){
//			T.showShort(getApplicationContext(), "请输入卡号");
//			return;
//		}
//		if(TextUtils.isEmpty(card_2.getText().toString())){
//			T.showShort(getApplicationContext(), "请输重复输入卡号");
//			return;
//		}
//		
//		
//		
////		if(ViewUtil.checkEditEmpty(card_2, "请输重复输入卡号")){
////			T.showShort(getApplicationContext(), "请输重复输入卡号");
////			return;
////		}
//		if(card_1.getText().toString().length()<16){
////			ViewUtil.showError(card_1,"卡号长度有误");
//			T.showShort(getApplicationContext(), "卡号长度16-25位");
//			return;
//		}
//		if(card_2.getText().toString().length()<16){
////			ViewUtil.showError(card_2,"卡号长度有误");
//			T.showShort(getApplicationContext(), "卡号长度16-25位");
//			return;
//		}
//		if(!card_2.getText().toString().equals(card_1.getText().toString())){
////			ViewUtil.showError(card_2,"两次卡号不一致");
//			T.showShort(getApplicationContext(), "两次卡号不一致");
//			return;
//		}
//
//		if(TextUtils.isEmpty(tvs.getText())){
//			T.showShort(getApplicationContext(), "请选择油卡公司名称");
//			return;
//		}
//		
//		if(ViewUtil.checkEditEmpty(card_name, "请输入姓名")){
//			T.showShort(getApplicationContext(), "请输入姓名");
//			return;
//		}
//		
//		if(ChexText.judgetext(card_name.getText().toString())){
//			ViewUtil.showError(card_name,"禁止输入特殊字符");
//			T.showShort(getApplicationContext(), "禁止输入特殊字符");
//			return;
//		}
//
//		
//		
//
//		
//		AddOilCardRequest req = new AddOilCardRequest();
//		req.userId = UserInfoManager.getUserInfo(this).id+"";
//		req.cardNo = card_2.getText().toString();
//		req.accountName = card_name.getText().toString();
//		req.channelName = tvs.getText().toString();
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.ADDOILCARD, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				AddOilCardResponse bean = new Gson().fromJson(resp.result, AddOilCardResponse.class);
//				if(Api.SUCCEED == bean.result_code){
////					finish();
//					if(bean.data.RSPCOD.equals("000000")){
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//						finish();
//					}else{
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//					}
//				}
//			}
//		});
//		
//	}
//	private void updateView(List<AddOilCardBean> data) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public int getcardchannelName(String card){
//		int a = 0;
//		
//		if(card.substring(0,1).equals("9")){
//			a = 1;
//		}else if(card.substring(0,6).equals("100011")){
//			a = 2;
//		}else {
//			a = 3;
//		}
//		return a;
//	}
//
//	
//	private void initdialog( final String[] selectPicTypeStr ,final TextView tv) {
//		// TODO Auto-generated method stub
//
////		final String[] selectPicTypeStr = { "中国石油", "中国石化","中还有" };
//		
//		
//		final Dialog dialog = new Dialog(this,R.style.MyEditDialog1);
////		dialog.setTitle("选择");
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		ListView lv = new ListView(this); 
//		ProvinceAdapter pAdapter = new ProvinceAdapter(selectPicTypeStr,AddOilCardActivity.this);
//		lv.setAdapter(pAdapter);
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				if(selectPicTypeStr[(int)arg2].equals("中国石油")){
//					img_sh.setVisibility(View.VISIBLE);
//					img_sh.setImageResource(R.drawable.sy_logo_bg);
//				}else if(selectPicTypeStr[(int)arg2].equals("中国石化")){
//					img_sh.setVisibility(View.VISIBLE);
//					img_sh.setImageResource(R.drawable.sh_logo_bg);
//				}else if(selectPicTypeStr[(int)arg2].equals("中海油")){
//					img_sh.setVisibility(View.VISIBLE);
//					img_sh.setImageResource(R.drawable.zhonghaiyou);
//				}else{
//					img_sh.setVisibility(View.GONE);
//				}
//				tv.setText(selectPicTypeStr[(int)arg2]);
//				dialog.dismiss();
//			}
//
//		});
//		
//		dialog.setContentView(lv);
//		dialog.show();
//		
//	}
////	if(selectPicTypeStr.length>0){
////		initdialog
////	}
//	private void init() {
//		// TODO Auto-generated method stub
//		AddOilCardRequest req = new AddOilCardRequest();
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.GETOILCARDCHANNEL, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				MyOilResponse bean = new Gson().fromJson(resp.result, MyOilResponse.class);
//				Log.e("", " - - -  "+resp.result);
//				if(Api.SUCCEED == bean.result_code){
//				
//					if(bean.data.size()>0){
//						selectPicTypeStr = new String[bean.data.size()];
//						for (int i = 0; i < bean.data.size(); i++) {
//							selectPicTypeStr[i] = bean.data.get(i).channel_name;
//						}
//					}
//					initdialog(selectPicTypeStr,tvs);
//				}
//			}
//		});
//		
//	}
//	
}
