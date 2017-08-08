package com.td.qianhai.epay.oem.interfaces;

import java.util.HashMap;

/**
 * 后台返回监听
 * @author liangge
 *
 */
public interface OnPayResultListener {
	public void backResult(HashMap<String, Object> result);
}
