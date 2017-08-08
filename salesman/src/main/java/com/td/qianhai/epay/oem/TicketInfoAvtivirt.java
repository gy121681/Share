package com.td.qianhai.epay.oem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.broadcast.BlueToothReceiver;
import com.td.qianhai.mpay.utils.DateUtil;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

public class TicketInfoAvtivirt extends BaseActivity{
	
	
	private ImageView imgs;
	private TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6,tv_7;
	private String shopname,orderno,amount,paytime,paytype,shoptype;
	 Set<BluetoothDevice> pairedDevices ;
	 private ArrayList<HashMap<String, Object>> result1 = null; 
	  public  BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_info_activity);
		AppContext.getInstance().addActivity(this);
		Intent it = getIntent();
		shopname = it.getStringExtra("shopname");
		orderno = it.getStringExtra("orderno");
		amount = it.getStringExtra("amount");
		paytime = it.getStringExtra("paytime");
		paytype = it.getStringExtra("paytype");
		shoptype = it.getStringExtra("shoptype");
		
		if(bluetoothAdapter.isEnabled()){
			if(BlueToothReceiver.mService==null){
				Log.e("", " null  == = =   ");
				BlueToothReceiver.mService = new BluetoothService(TicketInfoAvtivirt.this, mHandler1);
			}else{
				Log.e("", " null1  == = =   ");
				 initzijiang() ;
			}
		}else{
			bluetoothAdapter.enable();
		}

		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("再次打印");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		imgs = (ImageView) findViewById(R.id.imgs);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		tv_7 = (TextView) findViewById(R.id.tv_7);
		if(shopname!=null){
			tv_1.setText(shopname);
		}
		if(orderno!=null){
			tv_2.setText(orderno);
		}
		if(amount!=null){
			tv_6.setText(Double.parseDouble(amount)/100+"");
		}
		if(paytime!=null){
			tv_5.setText(DateUtil.formatDateLong(paytime));
		}
		if(paytype!=null){
			if(paytype.equals("1")){
				tv_4.setText("微信支付");
				imgs.setImageResource(R.drawable.weixin);
			}else{
				tv_4.setText("支付宝支付");
				imgs.setImageResource(R.drawable.zhifub);
			}
			
		}
		if(shoptype!=null){
			if(shoptype.equals("1")){
				tv_3.setText("个人");
			}else{
				tv_3.setText("商家");
			}
		}
		
		tv_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (!bluetoothAdapter.isEnabled()) {
					bluetoothAdapter.enable();
				}
				if(bluetoothAdapter.isEnabled()){
				
				if(BlueToothReceiver.isConnect){
					initmsg(0);
					initmsg(1);
				}else{

				}
				}
			}
		});
	}
	
	private void initzijiang() {
		// TODO Auto-generated method stub
		
		pairedDevices = BlueToothReceiver.mService.getPairedDev();
		if (pairedDevices.size() > 0) {
			// findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice devices : pairedDevices) {
				if (devices.getName().length() >= 4
						&& devices.getName().substring(0, 2)
								.equalsIgnoreCase("zi")) {
					
					Log.e("", "devices = = = =  "+devices);
					BlueToothReceiver.mService.connect(devices);
				}
			}
		}
	}
	
	/**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    @SuppressLint("HandlerLeak") private final  Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:   //已连接

        			Toast.makeText(getApplicationContext(),"打印就绪",
        					Toast.LENGTH_LONG).show();
        			
					initmsg(0);
					initmsg(1);
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
            	 Log.e("", "Device connection was lost1");
            	break;
            }
        }
    };
    
    private void initmsg(int tag) {
		Log.e("", "打印开始");
		// TODO Auto-generated method stub
		String msg = "";
		String lang = "ch";
		// printImage();

		byte[] cmd = new byte[3];
		cmd[0] = 0x1b;
		cmd[1] = 0x21;
		String ordernos = orderno;
		String paytypes = paytype;
		if(paytype!=null){
			if(paytype.equals("1")){
				paytypes = "微信支付";
				printImage1();
			}else{
				paytypes = "支付宝支付";
				printImage();
			}
		}
		String shoptypes = shoptype;
		if(shoptype!=null){
			if(shoptype.equals("1")){
				shoptypes = "个人";
			}else{
				shoptypes = "商家";
			}
		}
		String paytimes = paytime;
		if(paytime!=null){
			paytimes = DateUtil.formatDateLong(paytime);
		}
		String shopnames = shopname;
		if(shopnames.equals("null")){
			shopnames = "";
		}
		String amounts = amount;
		if(amount!=null){
			String a = String .format("%.2f",Double.parseDouble(amount)/100);
			NumberFormat nf = new DecimalFormat("###,###.##");
			amounts = nf.format(Double.parseDouble(a));
		}
		cmd[2] &= 0xEF;
		BlueToothReceiver.mService.write(cmd); // 取消倍高、倍宽模式	
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
		sb.append("门店: " + shopnames + "\n");
		sb.append("订单号: " + ordernos + "\n");
		sb.append("商户类型: " + shoptypes + "\n");
		sb.append("支付类型: " + paytypes + "\n");
		sb.append("交易时间: " + paytimes + "\n");
		sb.append("===============================\n");
		sb.append(" \n");
		sb.append(" \n");
		BlueToothReceiver.mService.sendMessage(sb.toString(), "GBK");
		cmd[2] |= 0x10;
		BlueToothReceiver.mService.write(cmd); // 倍宽、倍高模式
		BlueToothReceiver.mService.sendMessage("", "GBK");
		BlueToothReceiver.mService.write(fontSizeSetBig(2));
		BlueToothReceiver.mService.sendMessage("消费金额: "+amounts + "\n", "GBK");
//		sb.append("消费金额: "+amount + "\n");
		cmd[2] &= 0xEF;
		BlueToothReceiver.mService.write(cmd); // 取消倍高、倍宽模式	
		BlueToothReceiver.mService.sendMessage("", "GBK");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" \n");
		sb1.append("      谢谢惠顾,欢迎下次光临!");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
		sb1.append(" \n");
//		 String ms = BluetoothPrintFormatUtil.printMiddleMsg(list);
		BlueToothReceiver.mService.sendMessage(sb1.toString(), "GBK");
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
    	BlueToothReceiver.mService.write(sendData);   //打印byte流数据
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
    	BlueToothReceiver.mService.write(sendData);   //打印byte流数据
    	
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

}
