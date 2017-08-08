package com.td.qianhai.epay.oem.broadcast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.td.qianhai.epay.oem.BluetoothPrintActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.UserActivity;
import com.td.qianhai.epay.oem.beans.AllBean;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.BluetoothPrintFormatUtil;
import com.td.qianhai.epay.oem.mail.utils.CBluetoothDeviceContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.mpay.utils.DateUtil;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BlueToothReceiver extends BroadcastReceiver {
	private List<CBluetoothDeviceContext> discoveredDevices ;
	  public static BluetoothService mService = null;
	  private Context contexts;
	  private ArrayList<HashMap<String, Object>> result1 = null; 
	  private  Handler handler;
	  private Runnable runnable;
	  public  BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
	  private String stat = "start";
	  public  static boolean isopen  ;
	  Set<BluetoothDevice> pairedDevices ;
	  public static boolean isConnect = false;
	  
	
	// 监听蓝牙状态
	@Override
	public void onReceive(Context context, Intent intent) {
		contexts = context;
		String action = intent.getAction();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				initfile();
				Looper.loop();
			}
		}).start();
		
       if(pairedDevices!=null){
       	 pairedDevices.clear();
       }
		Log.e("", "自定义");
		
//		if (bluetoothAdapter.isEnabled()) {
//		if(isConnect==false){
	    	if(mService==null){
//	    		Log.e("", "111111111");
	    		mService = new BluetoothService(context, mHandler);
//	    		initzijiang();
//	    	}else{
//	    		Log.e("", "22222222");
//	    		initzijiang();
	    	}
//		}
//		}
		BluetoothDevice device = intent
				.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
        	
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                	Log.e("", "mService.stop();");
//                	if(handler!=null){
//                		handler.removeCallbacks(runnable);
//                	}
                	if(mService!=null){
                		mService.stop();
                	}
                	stat = "start";
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                	Log.e("", "mService.stop();");
                	if(mService!=null){
                		mService.stop();
                		
                	}
                	stat = "start";
                    break;
                case BluetoothAdapter.STATE_ON:
//                    if(mService!=null){
//                        mService.cancelDiscovery();
//                        mService = null;
//                    }
//                    if(pairedDevices!=null){
//                    	 pairedDevices.clear();
//                    }
//                	if(mService==null){
//                		mService = new BluetoothService(context, mHandler);
//                		Log.e("", "mService = null");
//                	}
//                	initzijiang();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;
            }
        }
       
//        if(mService!=null){
//            mService.cancelDiscovery();
//            mService = null;
//        }



    	 
		Log.i("TAG---BlueTooth", "接收到蓝牙状态改变广播！！");
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//			CBluetoothDeviceContext tempDevice = new CBluetoothDeviceContext(
//					device.getName() == null ? device.getAddress()
//							: device.getName(), device.getAddress(),device);
//			if (AllBean.getDiscoveredDevices() == null) {
//				discoveredDevices = new ArrayList<CBluetoothDeviceContext>();
//				AllBean.setDiscoveredDevices(discoveredDevices);
//			}
//			discoveredDevices = AllBean.getDiscoveredDevices();
//			if (device != null
//					&& device.getAddress() != null
//					&& device.getName() != null
//					&& !ifAddressExist(device.getAddress())
//					&& device.getName().length() >= 6
//					&& device.getName().substring(0, 6)
//							.equalsIgnoreCase("C-ME30")
//							) {
//				if(device.getBondState()==BluetoothDevice.BOND_NONE){
//					 try {    
//                         Method createBondMethod = BluetoothDevice.class.getMethod("createBond");    
//                         createBondMethod.invoke(device);    
//                     } catch (Exception e) {     
//                         e.printStackTrace();    
//                     }    
//				}
//				discoveredDevices.add(tempDevice);
//				AllBean.setDiscoveredDevices(discoveredDevices);
//				Toast.makeText(context.getApplicationContext()	, "搜索到新设备 : "+tempDevice.name, Toast.LENGTH_SHORT).show();
//			} else {
//				ToastCustom.showMessage(context, "设备信息获取失败 ");
//			}

		} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//			Toast.makeText(context, device.getName() + "已连接", Toast.LENGTH_LONG)
//					.show();
//			BluetoothPrintActivity.bluetoothAdapter.cancelDiscovery();
//			if (AllBean.bluetoothMacMain == null
//					|| AllBean.bluetoothMacMain.equals("")) {
//				AllBean.bluetoothMacMain = device.getAddress();
//				AllBean.bluetoothNameMain = device.getName();
//			}
//			AllBean.bluetoothName = device.getName();
//			AllBean.bluetoothMac = device.getAddress();
//			AllBean.bluetoothConn = 1;
//			
//			((AppContext) context.getApplicationContext())
//			.setBluetoothConn(1);
			
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
				.equals(action)) {
			Toast.makeText(context, device.getName() + "正在断开蓝牙连接。。。",
					Toast.LENGTH_LONG).show();
//			AllBean.tag = 0;
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//			Toast.makeText(context, device.getName() + "蓝牙连接已断开！！！!!!!!！",
//					Toast.LENGTH_LONG).show();
////			CopyOfDevicesActivitys.context.finish();
//			BluetoothPrintActivity.bluetoothAdapter.disable();
//			AllBean.setDiscoveredDevices(null);
//			AllBean.bluetoothConn = 0;
//			
//			((AppContext) context.getApplicationContext())
//			.setBluetoothConn(0);
//			AllBean.tag = 0;
			
			// ((AppContext) context.getApplicationContext()).setCustId(null);
			// // 商户ID赋为空
			// ((AppContext) context.getApplicationContext()).setPsamId(null);
			// ((AppContext) context.getApplicationContext()).setMacKey(null);
			// ((AppContext) context.getApplicationContext()).setPinKey(null);
			// ((AppContext) context.getApplicationContext()).setMerSts(null);
			// ((AppContext) context.getApplicationContext()).setMobile(null);
			// ((AppContext)
			// context.getApplicationContext()).setEncryPtkey(null);
			// ((AppContext) context.getApplicationContext()).setStatus(null);
			// ((AppContext) context.getApplicationContext()).setCustPass(null);
			// ((AppContext) context.getApplicationContext())
			// .setVersionSerial(null);
			// ((AppContext)
			// context.getApplicationContext()).setIsNetworkConn(0);
			// ((AppContext)
			// context.getApplicationContext()).setOpenNetwork(null);
			// ((AppContext) context.getApplicationContext()).setCameraCount(0);
			// AppManager.getAppManager().AppExit(context);
		}
		
	}
	
	
	private void initfile() {
		// TODO Auto-generated method stub
		
		File fil = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/DCIM/");
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!fil.exists()) {
			fil.mkdir();
		}
		
			File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"+"weixin.png");    
	    	if(!file.exists())    
	    	{    
					saveBitmap2file(drawableToBitamp(R.drawable.weixin),"weixin.png");

	    		
	    	} 
	    	File file1=new File(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"+"zhifub.png");    
	    	if(!file1.exists())    
	    	{    
						saveBitmap2file(drawableToBitamp(R.drawable.zhifub),"zhifub.png");
	    	} 
	}


    protected Bitmap drawableToBitamp(int drawableResource) {
    BitmapFactory.Options opt = new BitmapFactory.Options();
    opt.inPreferredConfig = Bitmap.Config.RGB_565;
    opt.inPurgeable = true;
    opt.inInputShareable = true;
    InputStream is = contexts.getResources().openRawResource(drawableResource);
    BitmapFactory.decodeStream(is, null, opt);
    return BitmapFactory.decodeStream(is, null, opt);
}

	private void initzijiang() {
		// TODO Auto-generated method stub
		if(mService!=null){
		
		pairedDevices = mService.getPairedDev();
		if (pairedDevices.size() > 0) {
			// findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice devices : pairedDevices) {
				if (devices.getName().length() >= 4
						&& devices.getName().substring(0, 2)
								.equalsIgnoreCase("zi")) {
					
					Log.e("", "devices = = = =  "+devices);
						mService.connect(devices);
				}
			}
			}
		}
	}


	/**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    @SuppressLint("HandlerLeak") private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:   //已连接
                	isConnect = true;
//                	ti = new Timer();
//                	ti.schedule(new TimerTask() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							Log.e("", "运行了");
//							new Thread(run1).start();
//							
//						}
//					}, 5000);
                	
                	
                	 final Timer timer = new Timer();
				      TimerTask task = new TimerTask(){
				         public void run(){
		           				if(MyCacheUtil.getshared(contexts).getString("isopem", "").equals("0")){
		           					
		           					new Thread(run1).start();
		           					
		           				}
				         }
				      };
//				       
				      timer.schedule(task, 2000,5000);

//           		 handler = new Handler();
//           		 runnable = new Runnable() {
//           			 
//           			@Override
//           			public void run() {
//           				// TODO Auto-generated method stub
//           				// 在此处添加执行的代码
//
//
//           				handler.postDelayed(this, 5000);// 20是延时时长
//           			}
//           		};
//
//           		handler.postDelayed(runnable, 2000);// 打开定时器，执行操作
//            		final Handler handler1 = new Handler();
//            		Runnable runnable1 = new Runnable() {
//            			@Override
//            			public void run() {
//            				// TODO Auto-generated method stub
//            				// 在此处添加执行的代码
//            				Log.e("", "运行了2");
//            				new Thread(run1).start();
//            				handler1.postDelayed(this, 20000);// 50是延时时长
//            			}
//            		};
//            		handler1.postDelayed(runnable1, 20000);// 打开定时器，执行操作
                	Log.e("", "答应设备已连接");
        			Toast.makeText(contexts,"打印设备已连接",
        					Toast.LENGTH_LONG).show();
                    break;
                case BluetoothService.STATE_CONNECTING:  //正在连接
                	Log.d("蓝牙调试","正在连接.....");
                    break;
                case BluetoothService.STATE_LISTEN:     //监听连接的到来
                case BluetoothService.STATE_NONE:
                	Log.d("蓝牙调试","等待连接.....");
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
            	isConnect = false;
                Log.e("", "Device connection was lost");
//                mService = null;
//                DeviceListActivity.this.unregisterReceiver(mReceiver);
            	Log.e("", "Device connection was lost");
//    			btnClose.setEnabled(false);
//    			btnSend.setEnabled(false);
//    			btnSendDraw.setEnabled(false);
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
//    			Toast.makeText(contexts,"无法连接设备。。。",
//    					Toast.LENGTH_LONG).show();
//            	mService = null;
//            	mService = new BluetoothService(contexts, mHandler);
//	    		initzijiang();
            	 Log.e("", "Device connection was lost1");
            	break;
            }
        }
    };
        
    
	private void initmsg(HashMap<String, Object> result1,int tag) {
		Log.e("", "打印开始");
		// TODO Auto-generated method stub
		String msg = "";
		String lang = "ch";
		// printImage();

		byte[] cmd = new byte[3];
		cmd[0] = 0x1b;
		cmd[1] = 0x21;
		
		
		String paytype = "";
		if(result1.get("paytype")!=null){
			paytype = result1.get("paytype").toString();
			if(paytype.equals("1")){
				paytype = "微信支付";
				printImage1();
			}else{
				paytype = "支付宝支付";
				printImage();
			}
		}
		String shoptype = "";
		if(result1.get("shoptype")!=null){
			shoptype = result1.get("shoptype").toString();
			if(shoptype.equals("1")){
				shoptype = "个人";
			}else{
				shoptype = "商家";
			}
		}
		String paytime = "";
		if(result1.get("paytime")!=null){
			paytime = result1.get("paytime").toString();
			paytime = DateUtil.formatDateLong(paytime);
		}

		
		String amount = "";
		if(result1.get("amount")!=null){
			amount = result1.get("amount").toString();
			String a = String .format("%.2f",Double.parseDouble(amount)/100);
			NumberFormat nf = new DecimalFormat("###,###.##");
			amount = nf.format(Double.parseDouble(a));
		}
		cmd[2] &= 0xEF;
		mService.write(cmd); // 取消倍高、倍宽模式	
//		mService.sendMessage("", "GBK");
//		mService.write(cmd); // 取消倍高、倍宽模式	
		StringBuffer sb = new StringBuffer();
		if(tag==0){
			sb.append("       结账存单,顾客留存\n");
		}else{
			sb.append("       结账存单,收银员留存\n");
		}
		sb.append(" \n");
		sb.append(" \n");
		sb.append("门店: " + result1.get("shopname").toString() + "\n");
		sb.append("订单号: " + result1.get("orderno").toString() + "\n");
		sb.append("商户类型: " + shoptype + "\n");
		sb.append("支付类型: " + paytype + "\n");
		sb.append("交易时间: " + paytime + "\n");
		sb.append("===============================\n");
		sb.append(" \n");
		sb.append(" \n");
		mService.sendMessage(sb.toString(), "GBK");
		cmd[2] |= 0x10;
		mService.write(cmd); // 倍宽、倍高模式
		mService.sendMessage("", "GBK");
		mService.write(fontSizeSetBig(2));
		mService.sendMessage("消费金额: "+amount + "\n", "GBK");
//		sb.append("消费金额: "+amount + "\n");
		cmd[2] &= 0xEF;
		mService.write(cmd); // 取消倍高、倍宽模式	
		mService.sendMessage("", "GBK");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" \n");
		sb1.append("      谢谢惠顾,欢迎下次光临!");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
//		 String ms = BluetoothPrintFormatUtil.printMiddleMsg(list);
		mService.sendMessage(sb1.toString(), "GBK");
	}
	
	private void initmsg1() {
		Log.e("", "打印开始");
		// TODO Auto-generated method stub
		String msg = "";
		String lang = "ch";
		// printImage();

		byte[] cmd = new byte[3];
		cmd[0] = 0x1b;
		cmd[1] = 0x21;
		
		
		printImage();

		cmd[2] &= 0xEF;
		mService.write(cmd); // 取消倍高、倍宽模式	
	
		StringBuffer sb = new StringBuffer();
		sb.append("       结账存单,收银员留存\n");
		sb.append(" \n");
		sb.append(" \n");
		sb.append("门店: " + "哈哈哈" + "\n");
		sb.append("订单号: " + "xixix" + "\n");
		sb.append("商户类型: " +"hehe" + "\n");
		sb.append("支付类型: " + "gaga" + "\n");
		sb.append("交易时间: " + "2016" + "\n");
		sb.append("===============================\n");
		sb.append(" \n");
		sb.append(" \n");
		mService.sendMessage(sb.toString(), "GBK");
		cmd[2] |= 0x10;
		mService.write(cmd); // 倍宽、倍高模式
		mService.sendMessage("", "GBK");
		mService.write(fontSizeSetBig(2));
		mService.sendMessage("消费金额: "+"52.00" + "\n", "GBK");
		cmd[2] &= 0xEF;
		mService.write(cmd); // 取消倍高、倍宽模式	
		mService.sendMessage("", "GBK");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" \n");
		sb1.append("      谢谢惠顾,欢迎下次光临!");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
//		 String ms = BluetoothPrintFormatUtil.printMiddleMsg(list);
		mService.sendMessage(sb1.toString(), "GBK");
	
	}
    
    private void printImage1() {
		// TODO Auto-generated method stub
    	Log.e("", "打印图片开始");
    	byte[] sendData = null;
    	PrintPic pg = new PrintPic();
    	pg.initCanvas(384);     
    	pg.initPaint();
    	pg.drawImage(30, 0, Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/DCIM/"+"weixin.png");
    	sendData = pg.printDraw();
    	mService.write(sendData);   //打印byte流数据
	}

	//打印图形
    @SuppressLint("SdCardPath")
	private void printImage() {
    	Log.e("", "打印图片开始");
		
    	byte[] sendData = null;
    	PrintPic pg = new PrintPic();
    	pg.initCanvas(384);     
    	pg.initPaint();
    	pg.drawImage(35, 0, Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/DCIM/"+"zhifub.png");
    	sendData = pg.printDraw();
    	mService.write(sendData);   //打印byte流数据
    	
    }
    
    /**
      * 字体变大为标准的n倍
     * @param num
      * @return
      */
     public static byte[] fontSizeSetBig(int num)
      {
              byte realSize = 0;
            switch (num)
              {
             case 1:
                 realSize = 0;break;
             case 2:
                 realSize = 15;break;
             case 3:
                realSize = 34;break;
             case 4:
                 realSize = 51;break;
            case 5:
                 realSize = 68;break;
             case 6:
                 realSize = 85;break;
            case 7:
                realSize = 102;break;
             case 8:
                 realSize = 119;break;
                 }
             byte[] result = new byte[3];
               result[0] = 29;
            result[1] = 33;
              result[2] = realSize;
             return result;
     }
   
    
	private static void saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 80;
		OutputStream m_fileOutPutStream = null;
//		try {
//			stream = new FileOutputStream(Environment
//					.getExternalStorageDirectory().getAbsolutePath()
//					+ "/DCIM/"
//					+ filename);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			m_fileOutPutStream = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+"/DCIM/"
					+ filename);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
		
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查是蓝牙地址是否已经存在
	 * 
	 * @return
	 */
	public boolean ifAddressExist(String addr) {
		for (CBluetoothDeviceContext devcie : discoveredDevices) {
			Log.e("", devcie.address);
			Log.e("", addr + "---------------------------");
			if (addr.equals(devcie.address))
				return true;
		}
		return false;
	}
	
	
	Runnable run1 = new Runnable() {
    
		@Override
		public void run() {
			String mobile =  MyCacheUtil.getshared(contexts).getString("MercNum", ""); 
			ArrayList<HashMap<String, Object>> list = null;
			try {
				Log.e("", "isopen = = "+isopen);
				if(isopen){
					stat = "start";
					isopen = false;
				}
				Log.e("", "stat = = = "+stat);
				String[] values = {mobile,stat};

				 result1 = NetCommunicate.executeHttpPostgetjsonlist(HttpUrls.SMALLTICKETLIST,
						HttpKeys.SMALLTICKETLIST_BACK,HttpKeys.SMALLTICKETLIST_ASK,values,0);
			} catch (HttpHostConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
			if(result1!=null){
				
					msg.what = 0;
			}else{
				
				msg.what = -1;
			}
			stat = "";
			handler1.sendMessage(msg);
			
		}
	};
	
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.e("", "打印准备");
//				printImage();
//				initmsg1();
				if(result1.size()>=1){
					for (int i = 0; i < result1.size(); i++) {
						initmsg(result1.get(i),0);
						initmsg(result1.get(i),1);
//						isopen = false;
					}
				}

				break;
			case -1:
				
				break;
			default:
				break;
			}
		};
	};
}
