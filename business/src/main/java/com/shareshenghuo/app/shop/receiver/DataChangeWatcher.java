package com.shareshenghuo.app.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 数据改变刷新列表
 * Created by hang on 2015/11/19.
 */
public class DataChangeWatcher {

    private static String ACT_DATA_CHANGED = "com.shareshenghuo.app.shop.receiver.DataChangeWatcher";

    private Context context;
    private DataChangeCallback callback;
    private DataChangeReceiver receiver;
    private IntentFilter filter;

    public int which;

    public DataChangeWatcher(Context context, DataChangeCallback callback) {
        this.context = context;
        this.callback = callback;
        receiver = new DataChangeReceiver();
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
     * 通知刷新列表
     */
    public static void sendDataChangeBroadcast(Context context) {
        context.sendBroadcast(new Intent(ACT_DATA_CHANGED));
    }

    public class DataChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(callback != null)
                callback.refreshData(which);
        }
    }

    public interface DataChangeCallback {
        public void refreshData(int which);
    }
}
