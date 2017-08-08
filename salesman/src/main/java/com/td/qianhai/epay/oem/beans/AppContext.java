package com.td.qianhai.epay.oem.beans;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;

import cn.jpush.android.api.JPushInterface;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.frontia.api.FrontiaPush;
import com.td.qianhai.epay.oem.database.DBHelper;
/**
 * 全局应用程序类：用于保存和调用全变量配置及访问网络数据
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppContext extends MultiDexApplication {
	private String mercNum; // 商户号
	private String mobile; // 手机号
	private String custId; // 商户ID
	private String psamId; // 终端号
	private String merSts; // 用户认证状态
	private String pinKey; // pkey
	private String macKey;// mkey
	private String encryPtkey;
	private String status;
	private int isNetworkConn;// 网络是否连接
	private String custPass;
	private String openNetwork;// 是否打开移动网络
	private int cameraCount;
	private String versionSerial;// 版本号
	private RichTreasureBean treasureBean; //钱包实体
	private String sts = "";
	private List<Activity> activityList = new LinkedList<Activity>();
	private List<Activity> activityList1 = new LinkedList<Activity>();
	public static boolean isunlock = true;
	private String nocr;
	public static boolean isscreenstatus = false;
	public static boolean iscreditcardlist = false;
	public static boolean isjup = true;
	private String stateaudit;
	private String txnsts;
	private boolean isSuccess = false;
	private int bluetoothConn = 0;
	
	public int getBluetoothConn() {
		return bluetoothConn;
	}

	public void setBluetoothConn(int bluetoothConn) {
		this.bluetoothConn = bluetoothConn;
	}

	public static FrontiaPush mPush;
	
	private String msgtitle;
	
	private String msgcontent;
	
	//推送id
	private String appid = "";
	
	private String userid = "";
	
	private String nocein = "0.00";
	
	private String username = "";
	
	private String aecuserinfo = "";
	
	private String agentid;
	
	private String currol = "0";
	
	private String[] ratelist;
	
	private String[] flashlist;
	
	public String[] getFlashlist() {
		return flashlist;
	}

	public void setFlashlist(String[] flashlist) {
		this.flashlist = flashlist;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String[] getRatelist() {
		return ratelist;
	}

	public void setRatelist(String[] ratelist) {
		this.ratelist = ratelist;
	}
	
	public String getCurrol() {
		return currol;
	}

	public void setCurrol(String currol) {
		this.currol = currol;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getTxnsts() {
		return txnsts;
	}

	public void setTxnsts(String txnsts) {
		this.txnsts = txnsts;
	}

	public String getStateaudit() {
		return stateaudit;
	}

	public void setStateaudit(String stateaudit) {
		this.stateaudit = stateaudit;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	

	public String getAecuserinfo() {
		return aecuserinfo;
	}

	public void setAecuserinfo(String aecuserinfo) {
		this.aecuserinfo = aecuserinfo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNocein() {
		return nocein;
	}

	public void setNocein(String nocein) {
		this.nocein = nocein;
	}

	public String getNocr() {
		return nocr;
	}

	public void setNocr(String nocr) {
		this.nocr = nocr;
	}

	public String getSts() {
		return sts;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}

	public RichTreasureBean getTreasureBean() {
		return treasureBean;
	}

	public void setTreasureBean(RichTreasureBean treasureBean) {
		this.treasureBean = treasureBean;
	}

	private List<cls_companyinfo> companyInfoList = null;
	
	public String getMercNum() {
		return mercNum;
	}

	public void setMercNum(String mercNum) {
		this.mercNum = mercNum;
	}

	public String getVersionSerial() {
		return versionSerial;
	}

	public void setVersionSerial(String versionSerial) {
		this.versionSerial = versionSerial;
	}

	public int getCameraCount() {
		return cameraCount;
	}

	public void setCameraCount(int cameraCount) {
		this.cameraCount = cameraCount;
	}

	public String getOpenNetwork() {
		return openNetwork;
	}

	public void setOpenNetwork(String openNetwork) {
		this.openNetwork = openNetwork;
	}

	public String getCustPass() {
		return custPass;
	}

	public void setCustPass(String custPass) {
		this.custPass = custPass;
	}

	public String getMerSts() {
		return merSts;
	}

	public void setMerSts(String merSts) {
		this.merSts = merSts;
	}

	public int getIsNetworkConn() {
		return isNetworkConn;
	}

	public void setIsNetworkConn(int isNetworkConn) {
		this.isNetworkConn = isNetworkConn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPinKey() {
		return pinKey;
	}

	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}

	public String getEncryPtkey() {
		return encryPtkey;
	}

	public void setEncryPtkey(String encryPtkey) {
		this.encryPtkey = encryPtkey;
	}

	public String getPsamId() {
		return psamId;
	}

	public void setPsamId(String psamId) {
		this.psamId = psamId;
	}
	
	public String getMsgtitle() {
		return msgtitle;
	}

	public void setMsgtitle(String msgtitle) {
		this.msgtitle = msgtitle;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		/**
		 * 暂时不用
		 */
		// AppExceptionUtil exceptionUtil=AppExceptionUtil.getInstance();
		// exceptionUtil.init(getApplicationContext());
//		Frontia.init(getApplicationContext(), "Q8htwUGPqpfpU5uFo4zGdp4C");
//		mPush = Frontia.getPush();
			 
//			 boolean isWorking = mPush.isPushWorking();
//			 Log.e("", " - isWorking -"+isWorking);
//			 if(!isWorking){
////				 mPush.start("accessToken");
//				 mPush.start();
//			 }
			 
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);   
			 
				myApplication = this;
				new Thread(task).start();
				
				try {
					Class.forName("android.os.AsyncTask");
					} catch (ClassNotFoundException e) {
					e.printStackTrace();
					}
			
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * �?��当前系统声音是否为正常模�?
	 * 
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	/**
	 * �?��网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 判断当前版本是否兼容目标版本的方�?
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 获取App安装包信�?
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	private Runnable task = new Runnable() {

		@Override
		public void run() {
//			try {
//				Common.loadDatabase(AppContext.this);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String hasLoadDB = Common.readConfig(AppContext.this,
//					Common.HASLOAD_DATABASE, "0");
//			Common.dbh = new DBHelper(AppContext.this, "mypackage");
//			if (hasLoadDB.equals("0")) {
//
//				Common.readData2Db(AppContext.this);
//				Common.writeConfig(AppContext.this, Common.HASLOAD_DATABASE,
//						"1");
//			}
			// companyInfoList = new ArrayList<cls_companyinfo>();
			// companyInfoList = cls_companyinfo.getCompanyInfoList(Common.dbh);
		}
	};

	private static AppContext myApplication;

	public synchronized static AppContext getInstance() {

		return myApplication;
	}

	public List<cls_companyinfo> getCompanyInfoList() {

		return companyInfoList;
	}

	public void setCompanyInfoList() {
//		this.companyInfoList = cls_companyinfo.getCompanyInfoList(Common.dbh);
	}
	
	
	/**
	 * 添加Activity到列表中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	
	public void addActivity1(Activity activity) {
		activityList1.add(activity);
	}
	
	public void finishactivity(){
		for (Activity activity : activityList1) {
			if(activity!=null){
				activity.finish();
			}
			
		}
	}

	/**
	 * 退出应用程序
	 */
	public void exit() {
		
		for (Activity activity : activityList) {
			if(activity!=null){
				activity.finish();
			}
		}
		activityList.clear();
	}
	
}
