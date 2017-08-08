package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.ConversationListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.SysMsgRequest;
import com.shareshenghuo.app.shop.network.response.FeedBackMsgResponse;
import com.shareshenghuo.app.shop.network.response.StringResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.widget.swipelistview.SwipeListView;

public class MessageActivity extends BaseTopActivity implements OnClickListener {
	
	private TextView tvSysMsgTime;
	private TextView tvSysMsgUnreadCount;
	private SwipeListView lvMsg;
	private TextView tvSysMsgUnreadCountfeed;
	private NewChatMsgWorker newMsgWorker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initView();
	}
	
	public void initView() {
		initTopBar("消息");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("联系人");
		btnTopRight1.setOnClickListener(this);
		
		lvMsg = getView(R.id.lvMsg);
		lvMsg.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		
		tvSysMsgTime = getView(R.id.tvSysMsgTime);
		tvSysMsgUnreadCount = getView(R.id.tvSysMsgUnreadCount);
		tvSysMsgUnreadCountfeed = getView(R.id.tvSysMsgUnreadCountfeed);
		
		getView(R.id.llSysMsg).setOnClickListener(this);
		getView(R.id.btnPushMsg).setOnClickListener(this);
		getView(R.id.btnPushLog).setOnClickListener(this);
		getView(R.id.llSysMsgfeed).setOnClickListener(this);
		
		newMsgWorker = new NewChatMsgWorker(this, new NewMessageCallback() {
			@Override
			public void newMessage(int which) {
				lvMsg.setAdapter(new ConversationListAdapter(MessageActivity.this, loadConversationList()));
//				getUnreadCount();
				getUnreadfeedCount();
			}
		});
		newMsgWorker.startWork();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		lvMsg.setAdapter(new ConversationListAdapter(this, loadConversationList()));
//		getUnreadCount();
		getUnreadfeedCount();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		newMsgWorker.stopWork();
	}

//	public void getUnreadCount() {
//		SysMsgRequest req = new SysMsgRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.user_type = "1";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_NOTIFY_COUNT, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				StringResponse bean = new Gson().fromJson(resp.result, StringResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					int count = Integer.parseInt(bean.data);
//					if(count > 0) {
//						tvSysMsgUnreadCount.setText(bean.data);
//						tvSysMsgUnreadCount.setVisibility(View.VISIBLE);
//					} else {
//						tvSysMsgUnreadCount.setVisibility(View.INVISIBLE);
//					}
//				}
//			}
//		});
//	}
	
	public void getUnreadfeedCount() {
		
		SysMsgRequest req = new SysMsgRequest();
		req.user_id = UserInfoManager.getUserInfo(this).shop_id;
		req.user_type = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.NOTICEANDFEEDCOUNTCONTROLLER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				FeedBackMsgResponse bean = new Gson().fromJson(resp.result, FeedBackMsgResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					int count = Integer.parseInt(bean.data.feedBackMsgNum);
					if(count > 0) {
						tvSysMsgUnreadCountfeed.setText(bean.data.feedBackMsgNum);
						tvSysMsgUnreadCountfeed.setVisibility(View.VISIBLE);
					} else {
						tvSysMsgUnreadCountfeed.setVisibility(View.INVISIBLE);
					}
					
					int count1 = Integer.parseInt(bean.data.systemMsgNum);
					if(count1 > 0) {
						tvSysMsgUnreadCount.setText(bean.data.systemMsgNum);
						tvSysMsgUnreadCount.setVisibility(View.VISIBLE);
					} else {
						tvSysMsgUnreadCount.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
	}

	/**
     * 获取会话列表
     * 
     * @param context
     * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +    */
    protected List<EMConversation> loadConversationList(){
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
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llSysMsg:
			startActivity(new Intent(this, SysMsgListActivity.class));
			break;
			
		case R.id.btnTopRight1:
			startActivity(new Intent(this, ContactListActivity.class));
			break;
			
		case R.id.btnPushMsg:
			startActivity(new Intent(this, PushMsgActivity.class));
			break;
			
		case R.id.btnPushLog:
			startActivity(new Intent(this, PushLogActivity.class));
			break;
		case R.id.llSysMsgfeed:
			startActivity(new Intent(this, SysMsgFeedbackListActivity.class));
			break;
			
			
		}
	}
}
