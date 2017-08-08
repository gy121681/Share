package com.td.qianhai.epay.oem.thread;

import java.util.HashMap;

import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;

/**
 * 版本更新检测线程
 * 
 * @author liangge
 * 
 */
public class VersionUpdateVerifyThread extends BaseThread {

	/** 传入后台的值 */
	private String[] values;

	/** 返回结果 */
	public HashMap<String, Object> result;

	public VersionUpdateVerifyThread(String... params) {
		super();
		String[] values = { params[0], params[1] };
		this.values = values;
	}

	/** 后台执行结果 */
	private synchronized HashMap<String, Object> doVersionUpdateRequest(
			String[] values) {
		return NetCommunicate.getET(HttpUrls.VERSION_UPDATE, values);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		threadState = STATE_RUNNING;
		result = doVersionUpdateRequest(values);
		threadState = STATE_RUNNING;
	}
}
