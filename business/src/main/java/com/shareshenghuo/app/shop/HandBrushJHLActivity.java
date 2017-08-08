package com.shareshenghuo.app.shop;
//package com.shareshenghuo.app.shop;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//
//import org.apache.http.entity.StringEntity;
//import org.xmlpull.v1.XmlPullParserException;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.jhl.bluetooth.ibridge.BluetoothIBridgeDevice;
//import com.jhl.jhlblueconn.BlueStateListenerCallback;
//import com.jhl.jhlblueconn.BluetoothCommmanager;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//import com.shareshenghuo.app.shop.adapter.BluetoothListAdapter;
//import com.shareshenghuo.app.shop.manager.UserInfoManager;
//import com.shareshenghuo.app.shop.network.bean.UserInfo;
//import com.shareshenghuo.app.shop.network.request.HandPayRequse;
//import com.shareshenghuo.app.shop.network.request.PayChannelsResquest;
//import com.shareshenghuo.app.shop.network.response.DownMKResponse;
//import com.shareshenghuo.app.shop.network.response.DownWKRespnose;
//import com.shareshenghuo.app.shop.network.response.HandPayResponse;
//import com.shareshenghuo.app.shop.networkapi.Api;
//import com.shareshenghuo.app.shop.util.NetCommunicate;
//import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
//import com.shareshenghuo.app.shop.util.T;
//import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
//import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
//import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
//
//public class HandBrushJHLActivity extends BaseTopActivity implements BlueStateListenerCallback{
//	
//	private TwoButtonDialog downloadDialog;
//    BluetoothCommmanager  BluetoothComm=null;
//	private Handler mMainMessageHandler;
//	private boolean bOpenDevice=false,isdis = false;
//	private TextView tv_pro,tv_search;
//	private static final int DIALOG = 3;
//	private ProgressBar pro_progress;
//	private ListView list_device ;
//	private BluetoothListAdapter ldSearshAdapter;
//	private List<String> list = new ArrayList<String>();
//	public static final String[] DEVICE_ADDRESS_FILETER = null;
////	=null;//new String[]{"liu","a60"}; //null;//;new String[]{""};搜索设备的时候的一个过滤器，为null时表示搜索所有类型
//	private  Map<String,String> mapcard=new HashMap<String,String>(); 
//	private String psamId,phone,balances = "15",feet = "",tag;
//	private static final long WAIT_TIMEOUT =15000; //超时时间  单位 毫秒
//	private String mainkey;
//	private MyEditDialog1 doubleWarnDialog1;
//	private String elesimgtype = ".jpg";
////	private OneButtonDialogWarn warnDialog;
//	private Map<String, Object> hashcard = new HashMap<String, Object>();
//	public String POINTSID,CUSTERMTEL;
//	
//	
//	@Override
//	@SuppressLint("NewApi")
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.hand_brush_activity);
////		psamId = MyCacheUtil.getshared(this).getString("TERMINALNUMBER", "");
//		UserInfo userInfo = UserInfoManager.getUserInfo(this);
//		phone = userInfo.band_mobile;
//		initview();
//		Intent it = getIntent();
//		balances = it.getStringExtra("balance");
//		psamId =it.getStringExtra("TERMNO");
//		Log.e("", ""+psamId);
//		POINTSID = it.getStringExtra("POINTSID");
//		CUSTERMTEL = it.getStringExtra("CUSTERMTEL");
//		if(it.getStringExtra("rate")!=null){
//			feet = it.getStringExtra("rate");
//		}
//		tag = it.getStringExtra("tag");
//	}
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		initTopBar("刷卡");
//		  mMainMessageHandler = new MessageHandler(Looper.myLooper());
//		tv_pro = (TextView) findViewById(R.id.tv_pro);
//		pro_progress = (ProgressBar) findViewById(R.id.pro_progress);
//		tv_search = (TextView) findViewById(R.id.tv_search);
//		list_device = (ListView) findViewById(R.id.listdevices);
//		ldSearshAdapter = new BluetoothListAdapter(this, list);
//		list_device.setAdapter(ldSearshAdapter);
//		tv_search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				initbluetooth();
//			}
//		});
//	    //设置当前窗体回调((这个的时候自动启动服务 已经不需要启动了 ))
//		BluetoothComm =BluetoothCommmanager.getInstance(this,this); 
//		//回调搜索方式
//	    BluetoothComm.ScanDevice(DEVICE_ADDRESS_FILETER,5,0);	 
//		showLogMessage("正在搜索蓝牙设备,超时时间5秒");  
////		initbluetooth();
//		
//	}
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//
//	}
//
//	private void initbluetooth() {
//		// TODO Auto-generated method stub
//		pro_progress.setVisibility(View.VISIBLE);
//		list.clear();
//		ldSearshAdapter.notifyDataSetChanged();
//		if (!bOpenDevice){    	
//    		Message updateMessage = mMainMessageHandler.obtainMessage();
//    		updateMessage.obj="";
//    		updateMessage.what=R.id.tv_pro;
//    		updateMessage.sendToTarget();    		
//    		showLogMessage("正在搜索蓝牙设备,超时时间5秒");         
//    		BluetoothComm.ScanDevice(DEVICE_ADDRESS_FILETER,5,0);    
//    	}
//    	else{
//    		isdis = true;
//			showLogMessage("正在断开连接...");
//        	BluetoothComm.DisConnectBlueDevice();
//    	    //设置当前窗体回调((这个的时候自动启动服务 已经不需要启动了 ))
//    		BluetoothComm =BluetoothCommmanager.getInstance(this,this); 
//    		//回调搜索方式
//    	    BluetoothComm.ScanDevice(DEVICE_ADDRESS_FILETER,5,0);	 
//    		showLogMessage("正在搜索蓝牙设备,超时时间5秒");  
////        	initbluetooth();
////    		   showLogMessage("请先断开连接");
//    	}	 
//	}
//	
//	public void showLogMessage(String msg) {
//		Message updateMessage = mMainMessageHandler.obtainMessage();  
//		updateMessage.obj=msg;
//		updateMessage.what=R.id.tv_pro;
//		updateMessage.sendToTarget();
//	}
//	
//	class MessageHandler extends Handler{
//		private long mLogCount = 0;
//		public MessageHandler(Looper looper){
//			super(looper);
//		}
//		@SuppressWarnings("unchecked")
//		@Override
//		public void handleMessage(Message msg){
//			switch(msg.what){
//			case R.id.tv_pro:
//				if(mLogCount>100){
//					mLogCount=0;
//					tv_pro.setText("");
//				}
//				String messageString = (String) (msg.obj);
//				tv_pro.setText(messageString);
////				int cursor = tv_pro.getSelectionStart();
////				m_editRecvData.getText().insert(cursor, messageString + "\n");
////				++mLogCount;
//				break;
//			case R.id.tv_search:
//				setpaypwd(hashcard);
//				String m = (String) (msg.obj);
//				tv_pro.setText(m);
//				break;
//			case 0:
//				pro_progress.setVisibility(View.GONE);
//				break;
//			case 0x99:
//				pro_progress.setVisibility(View.GONE);
//				tv_pro.setText("");
//				final ArrayList<BluetoothIBridgeDevice> mDevices =(ArrayList<BluetoothIBridgeDevice>) msg.obj;
//				 for (int i = 0; i < mDevices.size(); i++) {
//					 list.add(mDevices.get(i).getDeviceName());
//				 }
//				ldSearshAdapter.notifyDataSetChanged();
//				
//				list_device.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						// TODO Auto-generated method stub
//						pro_progress.setVisibility(View.VISIBLE);
//		    	  		showLogMessage("正在连接:"+mDevices.get((int)arg3).getDeviceName()+"=="+mDevices.get((int)arg3).getDeviceAddress());
//                    	BluetoothComm.ConnectDevice(mDevices.get((int)arg3).getDeviceAddress());
//					}
//				});
////				final ArrayList<BluetoothIBridgeDevice> mDevices =(ArrayList<BluetoothIBridgeDevice>) msg.obj;
////				final String[] items  =new String[mDevices.size()];
////			
////				 for (int i = 0; i < mDevices.size(); i++) {
////					 items[i] =	mDevices.get(i).getDeviceName();
////				 }
////				 new  AlertDialog.Builder(HandBrushJHLActivity.this)  
////				 .setTitle("请选择蓝牙设备" )  
////				 .setIcon(android.R.drawable.ic_dialog_info)                  
////				 .setSingleChoiceItems(items,  0 ,
////				   new  DialogInterface.OnClickListener() {  
////				      public   void  onClick(DialogInterface dialog,  int  which) {  
////				    	  	dialog.dismiss();
////				    	  	//时间
////				    	  	long end = System.currentTimeMillis();
////							SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss:SSS");
////							String hms1 = formatter.format(end);
////							Log.e("正在连接设备的起始时间===============================", hms1);
////							//显示
////							showLogMessage("T1== "+ hms1);
////			    	  		showLogMessage("正在连接:"+mDevices.get(which).getDeviceName()+"=="+mDevices.get(which).getDeviceAddress());
////							
////	                    	BluetoothComm.ConnectDevice(mDevices.get(which).getDeviceAddress());
////				    	  
////				      }  
////				   }  
////				 )  
////				 .setNegativeButton("取消" ,  /*null*/new DialogInterface.OnClickListener() {
////					
////					@Override
////					public void onClick(DialogInterface dialog, int which) {
////						// TODO Auto-generated method stub
////						dialog.cancel();
////						
////					}
////				}) 
////				 .create()
////				 .show();//AlertDialog.Builder.create().show()相当于 Dialog.show()
////				
////				
//				break;
//			case 0x98:
//				showDialog(DIALOG);
//				break;
//			}
//
//		}
//	}
//	
//	
//
//	@Override
//	public void onTimeout() {
//		// TODO Auto-generated method stub
//		showLogMessage("接收超时");
//	}   
//
//	@Override
//	public void onReadCardData(Map  hashcard) {
//		// TODO Auto-generated method stub
//		
//		Message updateMessage = mMainMessageHandler.obtainMessage();
//		updateMessage.obj="";
//		updateMessage.what=R.id.tv_search;
//		
//		
////		showLogMessage("加密信息：");	
//		Log.e("", ""+hashcard.toString());
////		mapcard=(Map<String, String>)  hashcard;
////	 	Set<?>     set     =     hashcard.entrySet();   
////        Iterator<?>  iterator  =  set.iterator();   
////        while (iterator.hasNext()) {
////        	 @SuppressWarnings("unchecked")
////		     Map.Entry<String, String> entry1=(Map.Entry<String, String>)iterator.next(); 
////        	 showLogMessage(entry1.getKey()+"=="+entry1.getValue());  
////        }
//		this.hashcard.putAll(hashcard);
//		showLogMessage("正在交易,请稍候");	
//		updateMessage.sendToTarget();
////		setpaypwd(hashcard);
//	}
//	
//	@SuppressLint("NewApi") @Override
//	public void onDeviceFound(ArrayList<BluetoothIBridgeDevice> mDevices) {//
//		// TODO Auto-generated method stub
//		if(Build.VERSION.SDK_INT > 10){
//		if (!isDestroyed()) {
//			ArrayList<BluetoothIBridgeDevice> ListName = new ArrayList<BluetoothIBridgeDevice>();
//			if (mDevices.size() ==0  ){
////				pro_progress.setVisibility(View.GONE);
//				Message updateMessage = mMainMessageHandler.obtainMessage();
//				updateMessage.obj="";
//				updateMessage.what=0;
//				updateMessage.sendToTarget();
//				showLogMessage("未找到设备设备,请确认设备是否开启/或重启蓝牙");
//				return; 
//			}
//			synchronized (mDevices) {
//			for (BluetoothIBridgeDevice device : mDevices) {
//				String map;
//				if (device.getDeviceName() != null) {
//					map = device.getDeviceName() + "=" + device.getDeviceAddress();
//				} else {
//					map = "unknown" + "=" + device.getDeviceAddress();
//				}
//				showLogMessage(map);
//				System.out.println(map);
//				ListName.add(device);
//			}
//
//			//弹出选择对话框
//			if (mDevices.size() ==0)
//				return;
//
//			Message updateMessage = mMainMessageHandler.obtainMessage();
//			updateMessage.obj=ListName;
//			updateMessage.what=0x99;
//			updateMessage.sendToTarget();
//				}
//			}
//		} else {
//			Log.d("onDeviceFound", "is Destroyed");
//		}
//	}
//	@Override
//	public void onDeviceInfo(Map<String, String> info) {
//		// TODO Auto-generated method stub
//		Message updateMessage = mMainMessageHandler.obtainMessage();
//		updateMessage.obj="";
//		updateMessage.what=R.id.tv_pro;
//		updateMessage.sendToTarget();
//		isdis = false;
//		showLogMessage("设备信息:");	
//		
//	 	Set<?>     set     =     info.entrySet();   
//        Iterator<?>  iterator  =  set.iterator();   
//        while (iterator.hasNext()){
//        	 @SuppressWarnings("unchecked")
//			Map.Entry<String, String> entry1=(Map.Entry<String, String>)iterator.next();  
//        	 showLogMessage(entry1.getKey()+"=="+entry1.getValue());  
////        	 Log.e(DEBUG_TAG,entry1.getKey()+"=="+entry1.getValue());
//        	 
//        }
//        downmainkey();
//       
//	}
//	private void downmainkey() {
//		
//		RequestParams params = new RequestParams("utf-8");
//		params.addBodyParameter("ACCOUNT", phone);
//		params.addBodyParameter("TERMTYPE", "1");
//		params.addBodyParameter("TERMNO",psamId);
//		new HttpUtils().send(HttpMethod.POST,Api.DOWNTMK, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				Log.e("", ""+resp.result); 
//				HashMap<String, Object> bean = null;
//				try {
//					bean = NetCommunicate.parseredXml(resp.result,NetCommunicate.DOWNMAINKEY);
//				} catch (XmlPullParserException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//						if (bean.get("RSPCOD").toString().equals("000000")) {
//							String value = bean.get("TMKKEYCHECKVALUE").toString();
//							mainkey = bean.get("TMKKEY").toString()+value;
//							initmainkey(mainkey);
//						} else {
//							 Toast.makeText(getApplicationContext(),bean.get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
//						}
//			}
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//			}
//		});
//		
//		
////		PayChannelsResquest req = new PayChannelsResquest();
////		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
////		req.TERMTYPE = "1";
////		req.TERMNO = psamId;
////		RequestParams params = new RequestParams();
////		try {
////			params.setBodyEntity(new StringEntity(req.toJson()));
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////		new HttpUtils().send(HttpMethod.POST, Api.DOWNTMK, params, new RequestCallBack<String>() {
////			@Override
////			public void onFailure(HttpException arg0, String arg1) {
////				ProgressDialogUtil.dismissProgressDlg();
////				T.showNetworkError(HandBrushJHLActivity.this);
////			}
////
////			@Override
////			public void onSuccess(ResponseInfo<String> resp) {
////				ProgressDialogUtil.dismissProgressDlg();
////				Log.e("", ""+resp.result.toString());
////				DownMKResponse bean = new Gson().fromJson(resp.result, DownMKResponse.class);
////				if(Api.SUCCEED == bean.result_code) {
////					if(bean.data.RSPCOD.equals("000000")){
////						String value = bean.data.TMKKEYCHECKVALUE;
////						mainkey = bean.data.TMKKEY+value;
////						initmainkey(mainkey);
////					}else{
////						T.showShort(getApplicationContext(), bean.data.RSPMSG);
////					}
////				}else{
////					T.showShort(getApplicationContext(), "请求失败");
////				}
////			}
////		});
//	}
//	
//	private void downworkkey() {
//		// TODO Auto-generated method stub
//		RequestParams params = new RequestParams("utf-8");
//		params.addBodyParameter("ACCOUNT",phone);
//		params.addBodyParameter("TERMTYPE", "1");
//		params.addBodyParameter("TERMNO",psamId);
//		new HttpUtils().send(HttpMethod.POST, Api.DOWNWK, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				Log.e("", ""+resp.result); 
//				HashMap<String, Object> bean = null;
//				try {
//					bean = NetCommunicate.parseredXml(resp.result,NetCommunicate.DOWNWORKKEY);
//				} catch (XmlPullParserException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//						if (bean.get("RSPCOD").toString().equals("000000")) {
//							String pk = bean.get("PINKEY").toString();
//							String mk = bean.get("MACKEY").toString();
//							String ek =  bean.get("ENCRYPTKEY").toString();
//							String pv = bean.get("PINCHKVALUE").toString();
//							String mv = bean.get("MAKCHKVALUE").toString();
//							String ev = bean.get("ENCCHKVALUE").toString();
//							initworkkey(pk+pv+mk+mv+ek+ev);
//						} else {
//							 Toast.makeText(getApplicationContext(),bean.get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
//						}
//			}
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//			}
//		});
//		
//		
//		
////		PayChannelsResquest req = new PayChannelsResquest();
////		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
////		req.TERMTYPE = "1";
////		req.TERMNO = psamId;
////		RequestParams params = new RequestParams();
////		try {
////			params.setBodyEntity(new StringEntity(req.toJson()));
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////		new HttpUtils().send(HttpMethod.POST, Api.DOWNWK, params, new RequestCallBack<String>() {
////			@Override
////			public void onFailure(HttpException arg0, String arg1) {
////				ProgressDialogUtil.dismissProgressDlg();
////				T.showNetworkError(HandBrushJHLActivity.this);
////			}
////
////			@Override
////			public void onSuccess(ResponseInfo<String> resp) {
////				ProgressDialogUtil.dismissProgressDlg();
////				Log.e("", ""+resp.result.toString());
////				DownWKRespnose bean = new Gson().fromJson(resp.result, DownWKRespnose.class);
////				if(Api.SUCCEED == bean.result_code) {
////					if(bean.data.RSPCOD.equals("000000")){
////						String pk = bean.data.PINKEY;
////						String mk = bean.data.MACKEY;
////						String ek = bean.data.ENCRYPTKEY;
////						String pv = bean.data.PINCHKVALUE;
////						String mv = bean.data.MAKCHKVALUE;
////						String ev = bean.data.ENCCHKVALUE;
////						
////						initworkkey(pk+pv+mk+mv+ek+ev);
////					}else{
////						T.showShort(getApplicationContext(), bean.data.RSPMSG);
////					}
////				}else{
////					T.showShort(getApplicationContext(), "请求失败");
////				}
////			}
////		});
//	}
//
//	
//
//	
//	private void initmainkey(String mainkey) {
//		// TODO Auto-generated method stub
//	    byte[] sendBuf = hexStr2Bytes(mainkey);  			
//        showLogMessage("请稍候..");
//		BluetoothComm.WriteMainKey(sendBuf);	
//	}
//	
//	private void initworkkey(String mainkey1) {
//		// TODO Auto-generated method stub
//		byte[] sendBuf = hexStr2Bytes(mainkey1);
//    	int len = mainkey1.length();
////	    showLogMessage("请稍候..");
//		BluetoothComm.WriteWorkKey(sendBuf);	
//	}
//	
//	private void Paycard() {
////		tv_pro.setText("");
//	    showLogMessage("请刷卡/插卡+交易密码..."); 
//	    BluetoothComm.SwipeCard(WAIT_TIMEOUT,Long.parseLong(balances)*100);
//	}
//	
//	
//
//
//	@Override
//	public void onError(int nResult,String MsgData) {
//		// TODO Auto-generated method stub	
//		String nReusl=String.format("%02x", nResult);	
//		showLogMessage("ERR" +nReusl +":"+MsgData+"请重试");
//		Message updateMessage = mMainMessageHandler.obtainMessage();
//		updateMessage.obj="";
//		updateMessage.what=0;
//		updateMessage.sendToTarget();  
//		
//	}
//	@Override
//	public void onLoadMasterKeySucc(Boolean isSucess) {
//		// TODO Auto-generated method stub
//		if(isSucess){
////			showLogMessage("主密钥设置成功");
//			downworkkey();	
//		}else{
//			Closepro();
//			showLogMessage("主密钥设置失败");
//		}
//			
//	}
//	@Override
//	public void onLoadWorkKeySucc(Boolean isSucess) {
//		// TODO Auto-generated method stub
//		if(isSucess){
////			showLogMessage("工作密钥设置成功");
//			Paycard();
//		}else{
//			Closepro();
//			showLogMessage("工作密钥设置失败");
//		}
//			
//		
//	}
//	@Override
//	public void getMacSucess(String macdata) {
//		// TODO Auto-generated method stub
//		showLogMessage("MAC:"+macdata.toString());
//	}
//	@Override
//	public void swipCardSucess(String cardNumber) {
//		// TODO Auto-generated method stub
//		showLogMessage("PAN:"+cardNumber.toString());										
//		Message updateMessage = mMainMessageHandler.obtainMessage();
//		updateMessage.obj="";
//		updateMessage.what=0x98;
//		updateMessage.sendToTarget();	
//	}
//	@Override
//	public void onBluetoothIng() {
//		// TODO Auto-generated method stub
//		showLogMessage("正在连接设备...");
//	}
//	@Override
//	public void onBluetoothConected() {
//		// TODO Auto-generated method stub
//		showLogMessage("连接蓝牙成功,正在获取SN号...");
//    	bOpenDevice =true;
//    	BluetoothComm.GetDeviceInfo();
//	}
//	@Override
//	public void onBluetoothConectedFail() {
//		// TODO Auto-generated method stub
//		Closepro();
//		showLogMessage("连接蓝牙设备失败...");		
//	}
//	@Override
//	public void onBluetoothDisconnected() {
//		// TODO Auto-generated method stub
//		if(!isdis){
//			Closepro();
//		}
//		bOpenDevice =false;
//	}
//	private void Closepro() {
//		// TODO Auto-generated method stub
//		showLogMessage("蓝牙已断开,请重新连接...");
//		Message updateMessage = mMainMessageHandler.obtainMessage();
//		updateMessage.obj="";
//		updateMessage.what=0;
//		updateMessage.sendToTarget();
//	}
//
//	@Override
//	public void onScanTimeout() {
//		// TODO Auto-generated method stub
//		Closepro();
//		showLogMessage("搜索超时.");
//	}
//	@Override
//	public void onBluetoothPowerOff() {
//		// TODO Auto-generated method stub
//		Closepro();
//		showLogMessage("蓝牙关闭.");
//	}
//	@Override
//	public void onBluetoothPowerOn() {
//		// TODO Auto-generated method stub
//		Closepro();
//		showLogMessage("蓝牙开启.");
//	}
//
//	/** 等待刷卡、插卡、挥卡回调 **/
//	@Override
//	public void onWaitingForCardSwipe()
//	{
//		   showLogMessage("请刷卡/插卡/挥卡..."); 
//	}
//	
//	/** 检测到刷卡插入IC卡回调 **/
//	@Override
//	public void onDetectIC()
//	{
//		   showLogMessage("IC卡插入..."); 
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		BluetoothComm.closeResource();
//		BluetoothComm.DisConnectBlueDevice();
//		BluetoothComm.StopScanDevice();
//	}
//	
//	
//	/**
//	 * 十六进制字符串转换成bytes
//	 */
//	private static byte uniteBytes(String src0, String src1) {
//		byte b0 = Byte.decode("0x" + src0).byteValue();
//		b0 = (byte) (b0 << 4);
//		byte b1 = Byte.decode("0x" + src1).byteValue();
//		byte ret = (byte) (b0 | b1);
//		return ret;  
//	}
//	public static  byte[] hexStr2Bytes(String src) {
//		int m = 0, n = 0;
//		int l = src.length() / 2;
//		byte[] ret = new byte[l];
//		for (int i = 0; i < l; i++) {
//			m = i * 2 + 1;
//			n = m + 1;
//			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
//		}
//		return ret;
//	}
//	
//	
//	
////	public void Pay(final Map map){
////		doubleWarnDialog1 = new MyEditDialog1(HandBrushJHLActivity.this,
////				R.style.MyEditDialog, "收款", "若该卡有密码,请输入密码完成交易", "确认", "无密码", "若该卡有密码,请输入密码完成交易",
////				new OnMyDialogClickListener() {
////
////					@Override
////					public void onClick(View v) {
////
////						switch (v.getId()) {
////						case R.id.btn_right:
////							doubleWarnDialog1.dismiss();
////							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
////							String paypwds = doubleWarnDialog1.getpaypwd();
//////							setpaypwd(paypwds);
//////							flags = true;
////							break;
////						case R.id.btn_left:
////							
////							String paypwd = doubleWarnDialog1.getpaypwd();
////							
////							if(paypwd==null){
////								return;
////							}
////
////							if (paypwd == null || paypwd.equals("")) {
////								Toast.makeText(getApplicationContext(),"请输入支付密码",
////										Toast.LENGTH_SHORT).show();
////								return;
////							}
////							if (paypwd.length() < 6 || paypwd.length() > 15) {
////								return;
////							}
////
////							break;
////						default:
////							break;
////						}
////
////					}
////
////				},
////		new onMyaddTextListener() {
////			
////			@Override
////			public void refreshActivity(String paypwd) {
////				
////				if (paypwd == null || paypwd.equals("")) {
////					Toast.makeText(getApplicationContext(),"请输入密码",
////							Toast.LENGTH_SHORT).show();
////					return;
////				}
////				if (paypwd.length() < 6 || paypwd.length() > 15) {
////					return;
////				}
//////				flags = false;
////				try {
////					doubleWarnDialog1.dismiss();
////				} catch (Exception e) {
////					// TODO: handle exception
////					
////				}
////				setpaypwd(paypwd,map);
////			}
////
////		});
////		doubleWarnDialog1.setCancelable(false);
////		doubleWarnDialog1.setCanceledOnTouchOutside(false);
////		doubleWarnDialog1.show();
////	}
//	
//	private void setpaypwd(Map maps) {
//		// TODO Auto-generated method stub
//		
//		String card = "";
//		if(maps.get("PAN")!=null){
//			card = maps.get("PAN").toString();
//		}
//		String track2 = "";
//		if(maps.get("Encrytrack2")!=null){
//			track2 = maps.get("Encrytrack2").toString().toUpperCase();
//		}
//		String track3 = "";
//		if(maps.get("Track3")!=null){
//			track3 = maps.get("Track3").toString().toUpperCase();
//		}
//		String pwd = "";
//		if(maps.get("Pinblock")!=null){
//			pwd = maps.get("Pinblock").toString().toUpperCase();
//			Log.e("", "pwd = = = "+pwd);
//		}
//		String balance = "";
//		if(maps.get("Amount")!=null){
//			balance = maps.get("Amount").toString();
//		}
//		String TExpDat = "";
//		if(maps.get("ExpireDate")!=null){
//			TExpDat = maps.get("ExpireDate").toString();
//		}
//		String clearingId = "";
//		String signStr = getStringDateMerge();
//		
//		String InMod  = "";
//		if(maps.get("entrymode")!=null){
//			InMod = maps.get("entrymode").toString();
//		}
//		
////		if(maps.get("CardType")!=null){
////			String aa = maps.get("CardType").toString();
////			if(aa.equals("2")){
////				InMod = "050";
////			}else if(aa.equals("1")){
////				InMod = "051";
////			}else{
////				InMod = "021";
////			}
////		}
//		String ICDat  = "";
//		if(maps.get("Track55")!=null){
//			ICDat = maps.get("Track55").toString();
//		}
//		String CrdSqn = "",indtype = "",mercnam = "",mercid = "",txndat = "",txntim = "";
//		if(maps.get("PanSeqNo")!=null){
//			CrdSqn = maps.get("PanSeqNo").toString();
//		}
//		String mac = "";
//		
//		
//		RequestParams params = new RequestParams("utf-8");
//		
//		params.addBodyParameter("ACCOUNT", phone);
//		params.addBodyParameter("TERMTYPE", "1");
//		params.addBodyParameter("CTXNAT",Long.parseLong(balances)*100+"");
//		params.addBodyParameter("POINTSID", POINTSID);
//		params.addBodyParameter("CUSTERMTEL", CUSTERMTEL);
//		params.addBodyParameter("TERMINALNUMBER", psamId);
//		params.addBodyParameter("CRDNO", card);
//		params.addBodyParameter("ENCTRACKS", "");
//		params.addBodyParameter("TRACK2", track2);
//		params.addBodyParameter("TRACK3", track3);
//		params.addBodyParameter("TPINBLK", pwd);
//		params.addBodyParameter("TEXPDAT", TExpDat);
//		params.addBodyParameter("INMOD", InMod);
//		params.addBodyParameter("CRDSQN", CrdSqn);
//		params.addBodyParameter("ICDAT", ICDat);
//		params.addBodyParameter("RANDOMNUMBER", getsixnumb());
//		
//		
////		Log.e("", "ACCOUNT"+phone);
////		Log.e("", "TERMTYPE"+ "1");
////		Log.e("", "CTXNAT"+Long.parseLong(balances)*100+"");
////		Log.e("", "POINTSID"+ POINTSID);
////		Log.e("", "CUSTERMTEL"+ CUSTERMTEL);
////		Log.e("", "TERMINALNUMBER"+ psamId);
////		Log.e("", "CRDNO"+ card);
////		Log.e("", "TRACK2"+ track2);
////		Log.e("", "TRACK3"+ track3);
////		Log.e("", "TPINBLK"+ pwd);
////		Log.e("", "TEXPDAT"+TExpDat);
////		Log.e("", "INMOD"+ InMod);
////		Log.e("", "CRDSQN"+CrdSqn);
////		Log.e("", "ICDAT"+ ICDat);
////		Log.e("", "RANDOMNUMBER"+ getsixnumb());
//		Log.e("", ""+Api.POSPAY);
//		new HttpUtils().send(HttpMethod.POST,Api.POSPAY, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				Log.e("", ""+resp.result); 
//				HashMap<String, Object> bean = null;
//				try {
//					bean = NetCommunicate.parseredXml(resp.result,NetCommunicate.HANDPAY);
//				} catch (XmlPullParserException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//						if (bean.get("RSPCOD")!=null&&bean.get("RSPCOD").toString().equals("000000")) {
//							Intent intent = new Intent(HandBrushJHLActivity.this,
//									UploadWriteActivity.class);
//							intent.putExtra("balance", balances);
//							intent.putExtra("pcsimId", psamId);
//							if(bean.get("LOGNO")!=null){
//								intent.putExtra("logno", bean.get("LOGNO").toString());
//							}
//							startActivity(intent);
//							finish();
//						} else {
//							downloadDialog = new TwoButtonDialog(HandBrushJHLActivity.this, R.style.CustomDialog,
//									"", bean.get("RSPMSG").toString(), "确定", "",true,new OnMyDialogClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											switch (v.getId()) {
//											case R.id.Button_OK:
//												downloadDialog.dismiss();
//												pro_progress.setVisibility(View.GONE);
////												BluetoothComm.DisConnectBlueDevice();
//												showLogMessage("请重试");  
//												break;
//											case R.id.Button_cancel:
//												pro_progress.setVisibility(View.GONE);
////												BluetoothComm.DisConnectBlueDevice();
//												showLogMessage("请重试");  
//												downloadDialog.dismiss();
//											default:
//												break;
//											}
//										}
//									});
//								downloadDialog.show();
////							 Toast.makeText(getApplicationContext(),bean.get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
//						}
//			}
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//			}
//		});
////		HandPayRequse req = new HandPayRequse();
////		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
////		req.TERMTYPE = "1";
////		req.CTXNAT = Long.parseLong(balances)*100+"";
////		req.POINTSID = POINTSID;
////		req.CUSTERMTEL = CUSTERMTEL;
////		req.TERMINALNUMBER = psamId;
////		req.CRDNO = card;
////		req.ENCTRACKS = "";
////		req.TRACK2 = track2;
////		req.TRACK3 = track3;
////		req.TPINBLK = pwd;
////		req.TEXPDAT= TExpDat;
////		req.INMOD = InMod;
////		req.CRDSQN = CrdSqn;
////		req.ICDAT = ICDat;
////		req.RANDOMNUMBER = getsixnumb();
////		Log.e("", ""+req.toString());
////		RequestParams params = new RequestParams();
////		try {
////			params.setBodyEntity(new StringEntity(req.toJson()));
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////		new HttpUtils().send(HttpMethod.POST, Api.POSPAY, params, new RequestCallBack<String>() {
////			@Override
////			public void onFailure(HttpException arg0, String arg1) {
////				ProgressDialogUtil.dismissProgressDlg();
////				T.showNetworkError(HandBrushJHLActivity.this);
////			}
////
////			@Override
////			public void onSuccess(ResponseInfo<String> resp) {
////				ProgressDialogUtil.dismissProgressDlg();
////				Log.e("", ""+resp.result.toString());
////				HandPayResponse bean = new Gson().fromJson(resp.result, HandPayResponse.class);
////				if(Api.SUCCEED == bean.result_code) {
////					if(bean.data.RSPCOD.equals("000000")){
//////						ToastCustom.showMessage(HandBrushJHLActivity.this, result
//////								.get(Entity.RSPMSG).toString());
//////						
////						Intent intent = new Intent(HandBrushJHLActivity.this,
////								UploadWriteActivity.class);
////						intent.putExtra("balance", balances);
////						intent.putExtra("pcsimId", psamId);
////						if(bean.data.LOGNO!=null){
////							intent.putExtra("logno", bean.data.LOGNO);
////						}
////						startActivity(intent);
////						finish();
////					}else{
////						downloadDialog = new TwoButtonDialog(HandBrushJHLActivity.this, R.style.CustomDialog,
////								"", bean.data.RSPMSG, "确定", "",true,new OnMyDialogClickListener() {
////									
////									@Override
////									public void onClick(View v) {
////										// TODO Auto-generated method stub
////										switch (v.getId()) {
////										case R.id.Button_OK:
////											downloadDialog.dismiss();
////											pro_progress.setVisibility(View.GONE);
//////											BluetoothComm.DisConnectBlueDevice();
////											showLogMessage("请重试");  
////											break;
////										case R.id.Button_cancel:
////											pro_progress.setVisibility(View.GONE);
//////											BluetoothComm.DisConnectBlueDevice();
////											showLogMessage("请重试");  
////											downloadDialog.dismiss();
////										default:
////											break;
////										}
////									}
////								});
////							downloadDialog.show();
//////						T.showShort(getApplicationContext(), bean.data.RSPMSG);
////					}
////				}else{
////					T.showShort(getApplicationContext(), "请求失败");
////				}
////			}
////		});
//	}
//	
//	public String getStringDateMerge() {
//		Date currentTime = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//		String dateString = formatter.format(currentTime);
//		String imgpash = "1"+phone+dateString+getsixnumb();
//		return imgpash;
//	}
//	
//	private String getsixnumb(){
//		Random random = new Random();
//		String result="";
//		for(int i=0;i<6;i++){
//		result+=random.nextInt(10);
//		}
//		return result;
//	}
//}
