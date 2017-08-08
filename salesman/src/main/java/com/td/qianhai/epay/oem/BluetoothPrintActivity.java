package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.td.qianhai.epay.oem.adapter.LDSearshAdapter;
import com.td.qianhai.epay.oem.beans.AllBean;
import com.td.qianhai.epay.oem.mail.utils.BluetoothPrintFormatUtil;
import com.td.qianhai.epay.oem.mail.utils.CBluetoothDeviceContext;
import com.td.qianhai.epay.oem.views.dialog.SearchDeviceDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnLDSearchListClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.zj.btsdk.BluetoothService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BluetoothPrintActivity extends BaseActivity{
	
	/** 蓝牙适配器 */
	public  static BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private List<CBluetoothDeviceContext> discoveredDevices = new ArrayList<CBluetoothDeviceContext>();
	private LDSearshAdapter ldSearshAdapter;
	private ProgressBar loading_process;
	private SearchDeviceDialog menuDialog;
	public BluetoothDevice device;
	private String deviceToConnect;
	private BluetoothService mService = null;
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_print_activity);
//		mService = new BluetoothService(this, mHandler);
		initview();
		startDiscovery();
	}

	private void initview() {
		// TODO Auto-generated method stub
		loading_process = (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);
		
	}
	
	
	
	
	/**
	 * 启动蓝牙搜索
	 */
	public void startDiscovery() {
		if (bluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(), "未找到蓝牙驱动", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		bluetoothAdapter.startDiscovery();
		mHandler1.sendEmptyMessage(31);
	}
	
	
	@SuppressLint("HandlerLeak")
	public Handler mHandler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				break;
			case 31:
				Intent serverIntent = new Intent(BluetoothPrintActivity.this,DeviceListActivity.class);      //运行另外一个类的活动
				startActivityForResult(serverIntent,1);
				
//				loading_process.setVisibility(View.GONE);
//				showLoadingDialog("正在搜索设备...");
//			Thread searchBlue = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					Looper.prepare();
//					try {
//
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
////						mHandler.sendEmptyMessage(17);
//					} finally {
//
////						bluetoothAdapter.cancelDiscovery();
//							loadingDialogWhole.dismiss();
//						
//					}
//					selectBtAddrToInit();
//
//					Looper.loop();
//				}
//			});
//			searchBlue.start();
				break;
			case 32:
				
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						Looper.prepare();
						initController();
//						Looper.loop();
//					}
//
//				}).start();
				break;
			case 8:
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						 Log.d("BlueToothTestActivity", "连接打印设备...");  
//						Looper.prepare();
						BluetoothDevice  con_dev = mService.getDevByMac(device.getAddress()); 
						 mService.connect(con_dev);
//						Looper.loop();
//					}
//
//				}).start();
				
				break;
			case 17:
				break;
			}
		};
	};
	
	// 弹出已配对蓝牙对话框,点击链接相应设备
	public void selectBtAddrToInit() {
		bluetoothAdapter.cancelDiscovery();
		// ToastCustom.showMessage(UpdateDetectionActivity.this, "停止搜索...");
		// Log.e("buletoolh", "停止搜索...");
		discoveredDevices = AllBean.getDiscoveredDevices();
		if (discoveredDevices == null) {
			showDoubleWarnDialog("提示", new SpannableString("未发现设备"), "重新搜索",
					"取　　消");
			return;
		}
		int i = 0;
		String[] bluetoothName = new String[discoveredDevices.size()];
		for (CBluetoothDeviceContext device : discoveredDevices) {
			bluetoothName[i++] = device.name;
		}
		if (bluetoothName.length < 1) {
			showDoubleWarnDialog("提示", new SpannableString("未发现设备"), "重新搜索",
					"取　　消");
			return;
		}
		ldSearshAdapter = new LDSearshAdapter(this, discoveredDevices);
		ldSearshAdapter.notifyDataSetChanged();

		menuDialog = new SearchDeviceDialog(BluetoothPrintActivity.this,
				R.style.CustomDialog, "重新搜索", "退出搜索",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.btn2:
							menuDialog.dismiss();
							mHandler1.sendEmptyMessage(30);
							break;

						case R.id.btn3:
							// closeApp();
							finish();
							break;
						}
					}
				}, ldSearshAdapter, new OnLDSearchListClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						menuDialog.dismiss();
						
						
						deviceToConnect = discoveredDevices.get(position).address;
						
						device = discoveredDevices.get(position).device;
						Log.e("", "deviceToConnect = = = = ="+deviceToConnect);
						
						mHandler1.sendEmptyMessage(32);
						

					}
				});
		menuDialog.setCancelable(false);
		menuDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		menuDialog.setCanceledOnTouchOutside(false);
		if(menuDialog!=null){
			menuDialog.show();
		}
		
	}
	
	private void initController() {
//		if(device.getBondState()==BluetoothDevice.BOND_NONE){
//			 try {    
//					Toast.makeText(getApplicationContext(), "开始配对", Toast.LENGTH_SHORT).show();
//                   Method createBondMethod = BluetoothDevice.class.getMethod("createBond");    
//                   createBondMethod.invoke(device);   
//               	Toast.makeText(getApplicationContext(), "配对成功", Toast.LENGTH_SHORT).show();
//                connect(device);    
//               } catch (Exception e) {     
//                   e.printStackTrace();    
//               	Toast.makeText(getApplicationContext(), "配对失败", Toast.LENGTH_SHORT).show();
//               }    
//		}else if(device.getBondState()==BluetoothDevice.BOND_BONDED){
                   // 连接      
                  connect(device);    
//		}
	}
	
    private void connect(BluetoothDevice btDev) {  
    	BluetoothSocket  btSocket = null;
    	
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  
        try {  
        	bluetoothAdapter.cancelDiscovery();
        	  btSocket = btDev.createRfcommSocketToServiceRecord(uuid);  
            Log.d("BlueToothTestActivity", "开始连接...");  
            btSocket.connect();  
            
            Log.d("BlueToothTestActivity", "连接...");  
            if(AllBean.bluetoothConn ==1){
            	 Log.d("BlueToothTestActivity", "连接完成...");  
//    			BluetoothDevice  con_dev = mService.getDevByMac(device.getAddress()); 
//					BluetoothDevice  con_dev = mService.getDevByMac(device.getAddress()); 
					 mService.connect(btDev);
               
                
            }
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            
            try {    
            	btSocket.close();  
            	btSocket = null;  
            } catch (IOException e2) {    
                // TODO: handle exception    
            } 
        }  
    }  
	
//	private void connect(BluetoothDevice device) throws IOException {  
//	    // 固定的UUID   
//	    final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";  
//	    UUID uuid = UUID.fromString(SPP_UUID);  
//	    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);  
//	    socket.connect();  
//	}
    
//    @Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (mService != null) 
//			mService.stop();
//		mService = null; 
//	}
	
//	class ClickEvent implements View.OnClickListener {
//		public void onClick(View v) {
//			if (v == btnSearch) {			
//				Intent serverIntent = new Intent(PrintDemo.this,DeviceListActivity.class);      //运行另外一个类的活动
//				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
//			} else if (v == btnSend) {
//                String msg = edtContext.getText().toString();
//                if( msg.length() > 0 ){
//                    mService.sendMessage(msg+"\n", "GBK");
//                }
//			} else if (v == btnClose) {
//				mService.stop();
//			} else if (v == btnSendDraw) {
//                String msg = "";
//                String lang = getString(R.string.strLang);
//				//printImage();
//				
//            	byte[] cmd = new byte[3];
//        	    cmd[0] = 0x1b;
//        	    cmd[1] = 0x21;
//            	if((lang.compareTo("en")) == 0){	
//            		cmd[2] |= 0x10;
//            		mService.write(cmd);           //倍宽、倍高模式
//            		mService.sendMessage("Congratulations!\n", "GBK"); 
//            		cmd[2] &= 0xEF;
//            		mService.write(cmd);           //取消倍高、倍宽模式
//            		msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
//                          +"  the company is a high-tech enterprise which specializes" +
//                          " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
//                         
//            		mService.sendMessage(msg,"GBK");
//            	}else if((lang.compareTo("ch")) == 0){
//            		cmd[2] |= 0x10;
//            		mService.write(cmd);           //倍宽、倍高模式
//        		    mService.sendMessage("恭喜您！\n", "GBK"); 
//            		cmd[2] &= 0xEF;
//            		mService.write(cmd);           //取消倍高、倍宽模式
//            		msg = "  您已经成功的连接上了我们的蓝牙打印机！\n\n"
//            		+ "  本公司是一家专业从事研发，生产，销售商用票据打印机和条码扫描设备于一体的高科技企业.\n\n";
//            	    
//            		mService.sendMessage(msg,"GBK");	
//            	}
//			}
//		}
//	}
	
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
                	initmsg();
                	
                	Toast.makeText(getApplicationContext(), "答应设备已连接",
                            Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Device connection was lost",
                               Toast.LENGTH_SHORT).show();
//    			btnClose.setEnabled(false);
//    			btnSend.setEnabled(false);
//    			btnSendDraw.setEnabled(false);
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
            	Toast.makeText(getApplicationContext(), "Unable to connect device",
                        Toast.LENGTH_SHORT).show();
            	
            	break;
            }
        }

		private void initmsg() {
			// TODO Auto-generated method stub
            String msg = "";
            String lang = "ch";
			//printImage();
			
        	byte[] cmd = new byte[3];
    	    cmd[0] = 0x1b;
    	    cmd[1] = 0x21;
        	if((lang.compareTo("en")) == 0){	
        		cmd[2] |= 0x10;
        		mService.write(cmd);           //倍宽、倍高模式
        		mService.sendMessage("Congratulations!\n", "GBK"); 
        		cmd[2] &= 0xEF;
        		mService.write(cmd);           //取消倍高、倍宽模式
        		msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                      +"  the company is a high-tech enterprise which specializes" +
                      " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
                     
        		mService.sendMessage(msg,"GBK");
        	}else if((lang.compareTo("ch")) == 0){
        		cmd[2] |= 0x10;
        		mService.write(cmd);           //倍宽、倍高模式
    		    mService.sendMessage("恭喜您！\n", "GBK"); 
        		cmd[2] &= 0xEF;
        		mService.write(cmd);           //取消倍高、倍宽模式
        		msg = "  您已经成功的连接上了我们的蓝牙打印机！\n\n"
        		+ "  本公司是一家专业从事研发，生产，销售商用票据打印机和条码扫描设备于一体的高科技企业.\n\n";
        		
        		LinkedHashMap<String, LinkedList<String>> list =  new LinkedHashMap<String, LinkedList<String>>();
        		LinkedList<String> linstr = new LinkedList<String>();
        		linstr.add("1$2$3");
//        		list.put("aa", linstr);
        		linstr.add("4$5$6");
        		list.put("bb", linstr);
        		linstr.add("7$8$9");
        		list.put("cc", linstr);
        		
        	    String ms = BluetoothPrintFormatUtil.printMenuMSG(list);
        		mService.sendMessage(ms,"GBK");	
        	}
		}
        
    };
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {    
        switch (requestCode) {
        case 2:      //请求打开蓝牙
            if (resultCode == Activity.RESULT_OK) {   //蓝牙已经打开
            	Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            } else {                 //用户不允许打开蓝牙
            	finish();
            }
            break;
        case  1:     //请求连接某一蓝牙设备
        	if (resultCode == Activity.RESULT_OK) {   //已点击搜索列表中的某个设备项
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //获取列表项中设备的mac地址
               BluetoothDevice  con_dev = mService.getDevByMac(address);   
                
                mService.connect(con_dev);
            }
            break;
        }
    } 
}
