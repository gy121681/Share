package com.shareshenghuo.app.shop.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.support.v4.util.Pair;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

public class MessageManager {
	
	public static int unreadCount = 0;	//未读消息数
	
	/**
	 * @return 未读消息数
	 */
	public static int getUnreadCount() {
		unreadCount = 0;
		List<EMConversation> emConversationList = loadConversationsWithRecentChat();
        for (int i=0; i<emConversationList.size();i++){
        	unreadCount += emConversationList.get(i).getUnreadMsgCount();
        }
		return unreadCount;
	}
	
	public static List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
//                Log.e("zzh---111","type="+conversation.getType()+"-----"+"chat type= "+conversation.getAllMessages().get(0).getChatType());
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }
	
	/**
     * 根据最后一条消息的时间排序
     *
     * @param
     */
    public static void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
