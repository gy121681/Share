package com.shareshenghuo.app.shop.manager;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.model.EaseNotifier;
import com.easemob.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.easemob.util.EMLog;
import com.shareshenghuo.app.shop.ChatActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.app.Constant;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.util.Util;

public class IMManager {
	
	protected static final String TAG = "IMManager";
	
	private static IMManager instance;
	
	private Context applicationContext;
	
	protected EMEventListener eventListener;
	private EaseUI easeUI;
	
	private IMManager() {
		
	}

	public static synchronized IMManager getInstance() {
		if(instance  == null)
			instance = new IMManager();
		return instance;
	}
	
	public void init(Context context) {
		this.applicationContext = context;
		EMChat.getInstance().init(applicationContext);
		EaseUI.getInstance().init(applicationContext);
		
		easeUI = EaseUI.getInstance();
		//调用easeui的api设置providers
	    setEaseUIProviders();
	  //设置chat options
	    setChatoptions();
//		//注册通话广播接收者
//		applicationContext.registerReceiver(callReceiver, callFilter);    
//        //注册连接监听
//        EMChatManager.getInstance().addConnectionListener(connectionListener);       
//        //注册群组和联系人监听
//        registerGroupAndContactListener();
        //注册消息事件监听
        registerEventListener();
	}
	
	protected void setEaseUIProviders() {
		//需要easeui库显示用户头像和昵称设置此provider
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                //获取user信息，demo是从内存的好友列表里获取，
                //实际开发中，可能还需要从服务器获取用户信息,
                //从服务器获取的数据，最好缓存起来，避免频繁的网络请求
                return EaseUserManager.getEaseUser(username);
            }
        });
        
		easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {
			@Override
			public String getTitle(EMMessage message) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getSmallIcon(EMMessage message) {
				// TODO Auto-generated method stub
				return R.drawable.ic_launcher;
			}
			
			@Override
			public Intent getLaunchIntent(EMMessage message) {
				//设置点击通知栏跳转事件
                Intent intent = new Intent(applicationContext, ChatActivity.class);
                //有电话时优先跳转到通话页面
//                if(isVideoCalling){
//                    intent = new Intent(applicationContext, VideoCallActivity.class);
//                }else if(isVoiceCalling){
//                    intent = new Intent(applicationContext, VoiceCallActivity.class);
//                }else{
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // 单聊信息
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // 群聊信息
                        // message.getTo()为群聊id
                        intent.putExtra("userId", message.getTo());
                        if(chatType == ChatType.GroupChat){
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        }else{
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }
                    }
//                }
                return intent;
			}
			
			@Override
			public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getDisplayedText(EMMessage message) {
				// 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = Util.getMessageDigest(message, applicationContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                String nick = EaseUserManager.getEaseUser(message.getFrom()).getNick();
                return nick+":"+ticker;
			}
		});
	}
	
	private void setChatoptions(){
	    //easeui库默认设置了一些options，可以覆盖
	    EMChatOptions options = EMChatManager.getInstance().getChatOptions();
	    options.setNoticeBySound(false);
	    options.setNoticedByVibrate(true);
	}
	
	/**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;
            
            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if(event.getData() instanceof EMMessage){
                    message = (EMMessage)event.getData();
                    EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                }
                
                switch (event.getEvent()) {
                case EventNewMessage:
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if(!easeUI.hasForegroundActivies()){
                        getNotifier().onNewMsg(message);
                    }
                    MessageManager.unreadCount++;
                    NewChatMsgWorker.sendNewMessageBroadcast(applicationContext);
                    break;
                case EventOfflineMessage:
                    if(!easeUI.hasForegroundActivies()){
                        EMLog.d(TAG, "received offline messages");
                        List<EMMessage> messages = (List<EMMessage>) event.getData();
                        getNotifier().onNewMesg(messages);
                    }
                    break;
                // below is just giving a example to show a cmd toast, the app should not follow this
                // so be careful of this
                case EventNewCMDMessage:
                { 
                    
                    EMLog.d(TAG, "收到透传消息");
                    //获取消息body
                    CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action;//获取自定义action
                    
                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
                    final String str = applicationContext.getString(R.string.receive_the_passthrough);
                    
                    final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
                    
                    if(broadCastReceiver == null){
                        broadCastReceiver = new BroadcastReceiver(){

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                Toast.makeText(applicationContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                            }
                        };
                        
                      //注册广播接收者
                        applicationContext.registerReceiver(broadCastReceiver,cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str+action);
                    applicationContext.sendBroadcast(broadcastIntent, null);
                    
                    break;
                }
                case EventDeliveryAck:
                    message.setDelivered(true);
                    break;
                case EventReadAck:
                    message.setAcked(true);
                    break;
                // add other events in case you are interested in
                default:
                    break;
                }
                
            }
        };
        
        EMChatManager.getInstance().registerEventListener(eventListener);
    }
    
    /**
	 * 获取消息通知类
	 * @return
	 */
	public EaseNotifier getNotifier(){
	    return easeUI.getNotifier();
	}
}
