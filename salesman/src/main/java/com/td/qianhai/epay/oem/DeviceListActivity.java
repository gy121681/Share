package com.td.qianhai.epay.oem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.broadcast.BlueToothReceiver;
import com.td.qianhai.epay.oem.mail.utils.BluetoothPrintFormatUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.zj.btsdk.BluetoothService;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends BaseActivity {
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
//    BluetoothService mService = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private TextView tv_go;
    private CheckBox checs ;
    private Editor editor;
	public   BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private Set<BluetoothDevice> pairedDevices ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        editor = MyCacheUtil.setshared(this);
        AppContext.getInstance().addActivity(this);
        setContentView(R.layout.dialog_search_device);   //显示列表界面
        checs = (CheckBox) findViewById(R.id.checs);
		if (!bluetoothAdapter.isEnabled()) {
			editor.putString("isopem", "-1");
			editor.commit();
			checs.setChecked(false);
			}
        AppContext.getInstance().addActivity(this);

        // Set result CANCELED incase the user backs out
//        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
//        Button scanButton = (Button) findViewById(R.id.button_scan);
//        scanButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                doDiscovery();
//                v.setVisibility(View.GONE);
//            }
//        });
    
        if(MyCacheUtil.getshared(this).getString("isopem", "").equals("0")){
        	checs.setChecked(true);
        }else{
        	checs.setChecked(false);
        }
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
        
		((TextView) findViewById(R.id.tv_title_contre)).setText("打印机设置");
		((TextView) findViewById(R.id.bt_title_right1)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.bt_title_right1)).setText("再次打印");
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.bt_title_right1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(DeviceListActivity.this,TicketListActivity.class);
				startActivity(it);
			}
		});
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		tv_go =(TextView) findViewById(R.id.tv_go);
		tv_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
			}
		});
		
		checs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
				// TODO Auto-generated method stub
				if(ischeck){
//					if (!bluetoothAdapter.isEnabled()) {
//						bluetoothAdapter.enable();}

					editor.putString("isopem", "0");
					editor.commit();

					BlueToothReceiver.isopen = true;
					
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							initdata();
//						}
//					}, 5000);
				
					
				}else{

//					if (bluetoothAdapter.isEnabled()) {
//						bluetoothAdapter.disable();}
					editor.putString("isopem", "-1");
					editor.commit();
//					Intent i = new Intent("android.bluetooth.device.action.STATE_CHANGED");
//					sendBroadcast(i);
					BlueToothReceiver.isopen = true;
				}
			}
		});
       
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        
        
        
        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.lv1);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        this.registerReceiver(mReceiver, filter);


//        BlueToothReceiver.mService = new BluetoothService(this, null);
        // Get a set of currently paired devices
//        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();
//
//        // If there are paired devices, add each one to the ArrayAdapter
//        if (pairedDevices.size() > 0) {
////            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
//            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                Log.e("", "1111111111111111111");
//            	if(device.getName().length()>=4&& device.getName().substring(0, 2)
//						.equalsIgnoreCase("zi")){
////            		
//////                    BluetoothDevice  con_dev = mService.getDevByMac(address);   
////                    
////                    mService.connect(device);
//            	}
//            }
//        } else {
//            String noDevices = "没有设备";
//            mPairedDevicesArrayAdapter.add(noDevices);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mService != null) {
//        	mService.cancelDiscovery();
//        }
//        mService = null;
//        this.unregisterReceiver(mReceiver);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
		Intent i = new Intent("android.bluetooth.device.action.STATE_CHANGED");
		sendBroadcast(i);
		if(BlueToothReceiver.mService!=null){
				initdata();
			}
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	Log.e("", "onRestart - - ");
    }
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	Log.e("", "onRestart - - ");
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	Log.e("", "onResume - - ");
		if(BlueToothReceiver.mService!=null){
			initdata();
		}
//        if(MyCacheUtil.getshared(this).getString("isopem", "").equals("0")){
//        	checs.setChecked(true);
//        }else{
//        	checs.setChecked(false);
//        }
    }
    
    
    
    private void initdata(){
//    	if(BlueToothReceiver.mService==null){
//    		 BlueToothReceiver.mService = new BluetoothService(DeviceListActivity.this, mHandler1);
//    	}
  	  pairedDevices = BlueToothReceiver.mService.getPairedDev();
      if (pairedDevices.size() > 0) {
    	  mPairedDevicesArrayAdapter.clear();
//        findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
        for (BluetoothDevice device : pairedDevices) {
      	  if(!pairedDevices.contains(device.getAddress())&&device.getName().length()>=4&& device.getName().substring(0, 2)
					.equalsIgnoreCase("zi")){
      		  mPairedDevicesArrayAdapter.add(device.getName()+"           点击连接");// + "\n" + device.getAddress());
      	  }
          
        	if(device.getName().length()>=4&& device.getName().substring(0, 2)
						.equalsIgnoreCase("zi")){
//        		
////                BluetoothDevice  con_dev = mService.getDevByMac(address);   
//                
//                mService.connect(device);
        	}
        }
    } else {
    	 mPairedDevicesArrayAdapter.clear();
        String noDevices = "没有设备";
        mPairedDevicesArrayAdapter.add(noDevices);
        
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initdata();
			}
		}, 5000);
    }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
//        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
//        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (BlueToothReceiver.mService.isDiscovering()) {
        	BlueToothReceiver.mService.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        BlueToothReceiver. mService.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {   //点击列表项，连接设备
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
//        	mService.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String address = info.substring(info.length() - 17);

//            // Create the result Intent and include the MAC address
//            Intent intent = new Intent();
//            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
//            Log.d("连接地址", address);
//            
//            // Set result and finish this Activity
//            setResult(Activity.RESULT_OK, intent);
//            finish();
        	if(BlueToothReceiver.isConnect){
        		Toast.makeText(getApplicationContext(), "设备已连接", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	
    		if(BlueToothReceiver.mService==null){
    			Log.e("", " null3  == = =   ");
    			 BlueToothReceiver.mService = new BluetoothService(DeviceListActivity.this, mHandler1);
    		}else{
//    			 initzijiang() ;
    		}
    		pairedDevices = BlueToothReceiver.mService.getPairedDev();
    		if (pairedDevices.size() > 0) {
    			// findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
    			for (BluetoothDevice devices : pairedDevices) {
    				if (devices.getName().length() >= 4
    						&& devices.getName().substring(0, 2)
    								.equalsIgnoreCase("zi")) {
    					
    					BlueToothReceiver.mService.connect(devices);
    					}
    				}
    			}

        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // If it's already paired, skip it, because it's been listed already
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                	
////                	Log.e("  - --  ", device.getName().substring(0, 2));
////                	if(device.getName().length()>=4&& device.getName().substring(0, 2)
////							.equalsIgnoreCase("zi")){
////                		
//////                        BluetoothDevice  con_dev = mService.getDevByMac(address);   
////                        
////                        mService.connect(device);
////                	}
//                	
//                	
//
//                	
//                    mNewDevicesArrayAdapter.add(device.getName() +"           点击连接"+ "\n" + device.getAddress());
//                }
//            // When discovery is finished, change the Activity title
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                setProgressBarIndeterminateVisibility(false);
////                setTitle(R.string.select_device);
//                if (mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = "没有设备";
//                    mNewDevicesArrayAdapter.add(noDevices);
//                }
//            }
//        }
//    };
    
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
//                	initmsg();
//                	
//                	Toast.makeText(getApplicationContext(), "打印设备已连接",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case BluetoothService.STATE_CONNECTING:  //正在连接
//                	Log.d("蓝牙调试","正在连接.....");
//                    break;
//                case BluetoothService.STATE_LISTEN:     //监听连接的到来
//                case BluetoothService.STATE_NONE:
//                	Log.d("蓝牙调试","等待连接.....");
//                	initmsg();
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
//            	mService.stop();
//                if (mService != null) {
//                	mService.cancelDiscovery();
//                }
//                mService = null;
//                DeviceListActivity.this.unregisterReceiver(mReceiver);
                Toast.makeText(getApplicationContext(), "Device connection was lost",
                               Toast.LENGTH_SHORT).show();
//    			btnClose.setEnabled(false);
//    			btnSend.setEnabled(false);
//    			btnSendDraw.setEnabled(false);
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
            	Toast.makeText(getApplicationContext(), "Unable to connect device",
                        Toast.LENGTH_SHORT).show();
//            	initmsg();
            	break;
            }
        }

        
    };
    
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
        
}
