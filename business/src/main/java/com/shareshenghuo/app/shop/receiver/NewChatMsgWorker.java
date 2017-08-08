package com.shareshenghuo.app.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 监听聊天消息
 * Created by hang on 2016/1/28.
 */
public class NewChatMsgWorker {

    private static String ACT_DATA_CHANGED = "com.shareshenghuo.app.shop.receiver.NewChatMsgWorker";

    private Context context;
    private NewMessageCallback callback;
    private NewChatMsgReceiver receiver;
    private IntentFilter filter;

    public int which;

    public NewChatMsgWorker(Context context, NewMessageCallback callback) {
        this.context = context;
        this.callback = callback;
        receiver = new NewChatMsgReceiver();
        filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(ACT_DATA_CHANGED);
    }

    public void startWork() {
        if(receiver != null)
            context.registerReceiver(receiver, filter);
    }

    public void stopWork() {
        if(receiver != null)
            context.unregisterReceiver(receiver);
    }

    /**
     * 有新消息通知更新UI
     */
    public static void sendNewMessageBroadcast(Context context) {
        context.sendBroadcast(new Intent(ACT_DATA_CHANGED));
    }

    public class NewChatMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(callback != null)
                callback.newMessage(which);
        }
    }

    public interface NewMessageCallback {
        public void newMessage(int which);
    }
}
