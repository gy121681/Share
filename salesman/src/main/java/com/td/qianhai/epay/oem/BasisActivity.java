package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.BasisListAdapter;
import com.td.qianhai.epay.oem.beans.Basis;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

/**
 * 未使用
 */
public class BasisActivity extends BaseActivity implements OnScrollListener,
		OnItemClickListener {

	private String mobile;
	private View moreView;
	private View emptyView;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private BasisListAdapter adapter;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater inflater;
	private Basis entitys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basis_detail);
		((TextView) findViewById(R.id.tv_title_contre)).setText("VIP");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
//		mobile = ((AppContext) getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		mList = new ArrayList<HashMap<String, Object>>();
		adapter = new BasisListAdapter(this, mList);
		inflater = LayoutInflater.from(this);
		moreView = inflater.inflate(R.layout.load, null);
		listView = (ListView) findViewById(R.id.regular_list);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		if (mList.size() == 0) {
			emptyView = inflater.inflate(R.layout.progress_view, null);
			emptyView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			// 加载数据
			loadMore();
		}
	}

	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "没有更多记录了");
			moreView.setVisibility(View.GONE);
			return;
		}
		if (!isThreadRun) {
			isThreadRun = true;
			new Thread(run).start();
		}

	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.REGULAR_BASIS_LIST),
					mobile, String.valueOf(page), String.valueOf(PAGE_SIZE) };
			entitys = NetCommunicate.getBasis(HttpUrls.REGULAR_BASIS_LIST,
					values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
				if (list != null && list.size() != 0) {
					mList.addAll(list);
					int allNum = Integer.parseInt(entitys.getTolcnt());

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
					msg.what = 1;
					page++;
				} else {
					msg.what = 2;
				}
			} else {
				msg.what = 3;
			}
			isThreadRun = false;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				if (moreView != null) {
					listView.setVisibility(View.GONE);
					emptyView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
				}
				Toast.makeText(getApplicationContext(), "没有获取到您的订单信息",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(BasisActivity.this, "没有获取到您的订单信息");
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "订单信息获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(BasisActivity.this, "订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};

	private int lastItem;// 当前显示的最后一项

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				moreView.setVisibility(View.VISIBLE);
				loadMore();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(BasisActivity.this,
				BasisDetailedInfoActivity.class);
		Bundle bundle = new Bundle();
		Basis basis = new Basis();
		basis.setPositionList(entitys.list.get(position));
		basis.setSumprincipal(entitys.getSumprincipal());
		basis.setPagenum(entitys.getPagenum());
		basis.setNumperpage(entitys.getNumperpage());
		basis.setTolcnt(entitys.getTolcnt());
		basis.setRspcod(entitys.getRspcod());
		basis.setRspmsg(entitys.getRspmsg());
		bundle.putSerializable("basis", basis);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
