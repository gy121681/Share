package com.td.qianhai.epay.oem.net;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.td.qianhai.epay.oem.ActCodeBean;
import com.td.qianhai.epay.oem.beans.BankListBean;
import com.td.qianhai.epay.oem.beans.Basis;
import com.td.qianhai.epay.oem.beans.BasisModel;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.CreditListBean;
import com.td.qianhai.epay.oem.beans.DealRecordsEntity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.MoneyListBean;
import com.td.qianhai.epay.oem.beans.MyAgtBean;
import com.td.qianhai.epay.oem.beans.MyCircleBean;
import com.td.qianhai.epay.oem.beans.NewAuthenticationBean;
import com.td.qianhai.epay.oem.beans.OrderPayBean;
import com.td.qianhai.epay.oem.beans.PhoneChargeBean;
import com.td.qianhai.epay.oem.beans.PromotionEarningBean;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.beans.RateListBean;
import com.td.qianhai.epay.oem.beans.ReturnActcodeBean;
import com.td.qianhai.epay.oem.beans.RichTreasureBillBean;
import com.td.qianhai.epay.oem.beans.ShareDetailsBean;
import com.td.qianhai.epay.oem.beans.TransactionTypeBean;
import com.td.qianhai.epay.oem.beans.WechatListBean;
import com.td.qianhai.epay.utils.CipherUtil;
import com.td.qianhai.epay.utils.EposAesUtil;
import com.td.qianhai.epay.utils.EposCryptUtil;

import android.net.Uri;
import android.util.Log;
import android.util.Xml;

public class NetCommunicate {

	public static HashMap<String, Object> getUpload(int status,
			String[] values, File[] files) {
		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		System.out.println("---------------------------------");
		Log.v("test", "keys:" + keys + "\n" + "values:" + values + "\n");
		// try {
		// Log.v("test", strToXml(rspKeys, values,"key","key2"));
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		String result = null;
		try {
			result = executeHttpPostUpLoadFile(HttpUrls.HOST_POSM + status
					+ HttpUrls.SUFFIX_UPLOAD, keys, values, files);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
			return map;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "读写错误！");
			e.printStackTrace();
			return map;
		} catch (IllegalStateException e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
			return map;
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
			return map;
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		} else if (result.equals("503")) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				System.out.println("进入都这里");
				map = parseXml(result, rspKeys);
			} else {
				System.out.println("进入下面都这里");
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static HashMap<String, Object> getET(int status, String[] values) {
		String rom = CipherUtil.getRomdan(32);
		String key = CipherUtil.MD5STR(rom);
		String key2 = CipherUtil.XOREncrypt(rom);

		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String xmlStr = null;
		try {
			Log.v("test", "keys:" + keys + "\n" + "values:" + values + "\n"
					+ "key:" + key + "\n" + "key2" + key2);
			xmlStr = strToXml(keys, values, key, key2);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status + HttpUrls.SUFFIX,
					xmlStr, key);
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e2.printStackTrace();
			return map;
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				System.out.println("进入都这里");
				map = parseXml(result, rspKeys);
			} else {
				System.out.println("进入下面都这里");
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return map;
	}

	public static HashMap<String, Object> get(int status, String[] values) {

		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status + HttpUrls.SUFFIX,
					keys, values);
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e2.printStackTrace();
			return map;
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		System.out.println(result);//打印网络请求返回数据
		try {
			if (null != rspKeys) {
				System.out.println("进入都这里");
				map = parseXml(result, rspKeys);
			} else {
				System.out.println("进入下面都这里");
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return map;
	}

	// public static HashMap<String, Object> getPay(int status, String[] values)
	// {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// HashMap<String, Object> map = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// System.out.println("rom:" + rom);
	// System.out.println("key:" + key);
	// System.out.println("key2" + key2);
	// xmlStr = strToXml(keys, values, key, key2);
	// System.out.println("xmlStr" + xmlStr);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_PAY + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "网络异常！");
	// return map;
	// }
	// try {
	// if (null != rspKeys) {
	// map = parseXml(result, rspKeys);
	// } else {
	// map = parseXml(result);
	// }
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return map;
	// }

	public static HashMap<String, Object> getPay(int status, String[] values) {

		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			result = result.replace("&", "&amp;");
			Log.e("result", result);
			if (null != rspKeys) {
				map = parseXml(result, rspKeys);
			} else {
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return map;
	}

	// public static HashMap<String, Object> getPosp(int status, String[]
	// values) {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// HashMap<String, Object> map = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// System.out.println("rom:" + rom);
	// System.out.println("key:" + key);
	// System.out.println("key2" + key2);
	// xmlStr = strToXml(keys, values, key, key2);
	// System.out.println("xmlStr" + xmlStr);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// } catch (Exception e) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "请求服务器异常！");
	// e.printStackTrace();
	// }
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_POSP + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e1) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "请求服务器异常！");
	// e1.printStackTrace();
	// }
	// if (result == null) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "网络异常！");
	// return map;
	// }
	// try {
	// if (null != rspKeys) {
	// map = parseXml(result, rspKeys);
	// } else {
	// map = parseXml(result);
	// }
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return map;
	// }

	public static HashMap<String, Object> getPosp(int status, String[] values) {

		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_POSP + status
					+ HttpUrls.SUFFIX, keys, values);

		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (Exception e1) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e1.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = parseXml(result, rspKeys);
			} else {
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return map;
	}

	// public static DealRecordsEntity getQuery(int status, String[] values) {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// DealRecordsEntity entitys = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// xmlStr = strToXml(keys, values, key, key2);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_POSP + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e) {
	// entitys = new DealRecordsEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// entitys = new DealRecordsEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("网络异常！");
	//
	// return entitys;
	// }
	// try {
	//
	// entitys = parseDealXml(result, rspKeys);
	//
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return entitys;
	// }

	public static DealRecordsEntity getQuery(int status, String[] values) {
		DealRecordsEntity entitys = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_POSP + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			entitys = new DealRecordsEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			entitys = new DealRecordsEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("网络异常！");

			return entitys;
		}
		try {

			entitys = parseDealXml(result, rspKeys);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return entitys;
	}
	
	public static NewAuthenticationBean getauthentication(int status, String[] values) {
		NewAuthenticationBean entitys = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_POSP + status
					+ HttpUrls.SUFFIX, keys, values);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			entitys = new NewAuthenticationBean();
			entitys.setRspcod("0");
			entitys.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			entitys = new NewAuthenticationBean();
			entitys.setRspcod("0");
			entitys.setRspmsg("网络异常！");

			return entitys;
		}
		try {

			entitys = parseAuthenXml(result, rspKeys);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return entitys;
	}

	/**
	 * 未加密的请求格式
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static HashMap<String, Object> getMidatc(int status, String[] values) {
		Log.e("", "=========values" + values);
		Log.e("", "=========status" + status);
		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络通讯错误！");
			Log.e("error", "网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "读写错误！");
			Log.e("error", "读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			Log.e("error", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			Log.e("error", "网络异常！");
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = parseXml(result, rspKeys);
			} else {
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// public static ProvinceEntity getQueryProvince(int status, String[]
	// values) {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// ProvinceEntity entitys = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// xmlStr = strToXml(keys, values, key, key2);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// System.out.println("requst: " + xmlStr);
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_POSP + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e) {
	// entitys = new ProvinceEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// entitys = new ProvinceEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("网络异常！");
	//
	// return entitys;
	// }
	// try {
	//
	// // entitys = parseDealXml(result, rspKeys);
	// if (status == HttpUrls.QUERY_BANK_NAME) {
	// entitys = parseProvinceXml(result, rspKeys);
	// } else if (status == HttpUrls.QUERY_BANK_INFO) {
	// entitys = parseBankInfoXml(result, rspKeys);
	// }
	//
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return entitys;
	// }

	public static ProvinceEntity getQueryProvince(int status, String[] values) {
		ProvinceEntity entitys = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_POSP + status
					+ HttpUrls.SUFFIX, keys, values);

		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (Exception e) {
			entitys = new ProvinceEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			entitys = new ProvinceEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("网络异常！");

			return entitys;
		}
		try {

			// entitys = parseDealXml(result, rspKeys);
			if (status == HttpUrls.QUERY_BANK_NAME) {
				entitys = parseProvinceXml(result, rspKeys);
			} else if (status == HttpUrls.QUERY_BANK_INFO) {
				entitys = parseBankInfoXml(result, rspKeys);
			}

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return entitys;
	}

	// public static CityEntity getQueryCity(int status, String[] values) {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// CityEntity entitys = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// xmlStr = strToXml(keys, values, key, key2);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// System.out.println("requst: " + xmlStr);
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_POSP + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e) {
	// entitys = new CityEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// entitys = new CityEntity();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("网络异常！");
	//
	// return entitys;
	// }
	// try {
	//
	// // entitys = parseDealXml(result, rspKeys);
	//
	// entitys = parseCityXml(result, rspKeys);
	//
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return entitys;
	// }

	public static CityEntity getQueryCity(int status, String[] values) {

		CityEntity entitys = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_POSP + status
					+ HttpUrls.SUFFIX, keys, values);

		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (Exception e) {
			entitys = new CityEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			entitys = new CityEntity();
			entitys.setRspcod("0");
			entitys.setRspmsg("网络异常！");

			return entitys;
		}
		try {

			// entitys = parseDealXml(result, rspKeys);

			entitys = parseCityXml(result, rspKeys);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return entitys;
	}

	/**
	 * 参数转化为XML报文
	 * 
	 * @param keys
	 * @param values
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String strToXml(String[] keys, String[] values, String key,
			String key2) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		StringBuilder body = new StringBuilder(
				"<?xml version='1.0' encoding='UTF-8'?>");
		body.append("<EPOSPROTOCOL>");
		for (int i = 0; i < values.length; i++) {
			System.out.println(keys[i]);
			body.append("<");
			body.append(keys[i]);
			body.append(">");
			body.append(values[i]);
			body.append("</");
			body.append(keys[i]);
			body.append(">");

		}
		body.append("</EPOSPROTOCOL>");

		StringBuilder packagemac = new StringBuilder(
				EposCryptUtil.encryptToMD5(body.toString()));
		sb.append("<?xml version='1.0' encoding='UTF-8'?><EPOSPROTOCOL>");
		for (int i = 0; i < values.length; i++) {
			sb.append("<");
			sb.append(keys[i]);
			sb.append(">");
			sb.append(values[i]);
			sb.append("</");
			sb.append(keys[i]);
			sb.append(">");

		}
		sb.append("<PACKAGEMAC>");
		sb.append(packagemac.toString());
		sb.append("</PACKAGEMAC></EPOSPROTOCOL>");
		// outPutStreamTest("CameraXML.txt",sb.toString());
		Log.e("test", sb.toString());
		System.out.println("requst:/n" + sb.toString());
		return key2 + EposAesUtil.encryptString1(sb.toString(), key);
	}

	// private static void outPutStreamTest(String fileName,String str) {
	//
	// try {
	// FileOutputStream os = new FileOutputStream(
	// Environment.getExternalStorageDirectory().getAbsolutePath()
	// + "/DCIM/"+fileName, true);
	// os.write(str.getBytes());
	//
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * http
	 * 
	 * @param urlStr
	 * @param str
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeHttpPost(String urlStr, String str, String key)
			throws ClientProtocolException, IOException, IllegalStateException,
			Exception {

		Log.e("url", urlStr);
		HttpPost httpPost = new HttpPost(urlStr);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("requestParam", str));
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
//			Log.d("result:", EposAesUtil.decryptString(result, "dynamicode"));
			return result;
		}
		return null;
	}

	/**
	 * http 不加密 上传文件
	 * 
	 * @param urlStr
	 * @param str
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeHttpPostUpLoadFile(String urlStr,
			String[] node, String[] values, File[] files)
			throws ClientProtocolException, IOException, IllegalStateException,
			HttpHostConnectException, Exception {
		Log.e("", " 1 1 1  = = ="+node.length);
		Log.e("", " 22  2 = = ="+files.length);
		for (int i = 1; i < node.length; i++) {
			if (i == 1)
				urlStr = urlStr + "?" + node[i] + "=" + values[i];
			else
				urlStr = urlStr + "&" + node[i] + "=" + values[i];
		}
		
		Log.e("url", urlStr);
		PostMethod p = new PostMethod();
		URI uri = new URI(urlStr);
		p.setURI(uri);
		// 设置param
		Part[] params = new Part[node.length + files.length];
		// 传节点
		for (int i = 0; i < node.length; i++) {
			System.out.println(node[i]);
			System.out.println(values[i]);
			StringPart stringPart = new StringPart(node[i], values[i]);
			// 中文要用这个
			stringPart.setCharSet("UTF-8");
			params[i] = stringPart;
		}
		// 传文件
		for (int i = 0; i < files.length; i++) {
			FilePart filePart = new FilePart(files[i].getName(), files[i]);
			filePart.setCharSet("UTF-8");
			params[node.length + i] = filePart;
		}
		try {
			MultipartRequestEntity post = new MultipartRequestEntity(params,
					p.getParams());
			p.setRequestEntity(post);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(10000);
			client.getHttpConnectionManager().getParams().setSoTimeout(35000);
			int result = client.executeMethod(p);
			if (result == 200) {
				// System.out.println("Upload File OK");
				// // （5）读取response头信息
				// Header headerResponse = p.getResponseHeader("response_key");
				// String headerStr = headerResponse.getValue();
				// // （6）读取内容
				// byte[] responseBody = p.getResponseBody();
				// （7） 处理内容
				// System.out.println(headerStr);
				Log.e("", "--------" + p.getResponseBodyAsString() + "--------");
				// System.out.println(new String(responseBody));
				return p.getResponseBodyAsString();
			} else {
				String error = "File Upload Error HttpCode=" + result;
				System.out.println(error);
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			p.releaseConnection();
		}


		// PostMethod filePost = new PostMethod(urlStr);
		// try {
		// // 通过以下方法可以模拟页面参数提交
		// for (int i = 0; i < node.length; i++) {
		// filePost.setParameter(node[i], values[i]);
		// }
		// // filePost.setParameter("name", "中文");
		// // filePost.setParameter("pass", "1234");
		// for (int i = 0; i < files.length; i++) {
		// FilePart filePart = new FilePart(files[i].getName(), files[i]);
		//
		// filePart.setCharSet("GBK");
		// params[attrs.length + i] = filePart;
		// }
		// Part[] parts = { new FilePart(upFile.getName(), upFile) };
		// Part[] parts2 = { new FilePart(upFile2.getName(), upFile2) };
		// filePost.setRequestEntity(new MultipartRequestEntity(parts,
		// filePost.getParams()));
		// filePost.setRequestEntity(new MultipartRequestEntity(parts2,
		// filePost.getParams()));
		// HttpClient client = new HttpClient();
		// client.getHttpConnectionManager().getParams()
		// .setConnectionTimeout(10000);
		// client.getHttpConnectionManager().getParams().setSoTimeout(35000);
		// int status = client.executeMethod(filePost);
		// if (status == HttpStatus.SC_OK) {
		// System.out.println("上传成功");
		// // （5）读取response头信息
		// Header headerResponse = filePost
		// .getResponseHeader("response_key");
		// String headerStr = headerResponse.getValue();
		// // （6）读取内容
		// byte[] responseBody = filePost.getResponseBody();
		// // （7） 处理内容
		// System.out.println(headerStr);
		// System.out.println(new String(responseBody));
		// // 上传成功
		// } else {
		// System.out.println("上传失败");
		// // 上传失败
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// } finally {
		// filePost.releaseConnection();
		// }

		// HttpPost httpPost = new HttpPost(urlStr);
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// for (int i = 0; i < node.length; i++) {
		// params.add(new BasicNameValuePair(node[i], values[i]));
		// }
		// httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		// HttpResponse httpResponse =
		// HttpClientFactory.getHttpClient().execute(
		// httpPost);
		// if (httpResponse.getStatusLine().getStatusCode() == 200) {
		// String result = EntityUtils.toString(httpResponse.getEntity());
		// Log.d("result:", EposAesUtil.decryptString(result, "dynamicode"));
		// return EposAesUtil.decryptString(result, "dynamicode");
		// } else if (httpResponse.getStatusLine().getStatusCode() == 503) {
		// Log.v("HttpError", "503Error");
		// return "503";
		// } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
		// Log.v("HttpError", "400Error");
		// return null;
		// } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
		// Log.v("HttpError", "500Error");
		// return null;
		// } else {
		// Log.v("HttpError", "其他ERROR");
		// return null;
		// }
	}

	/**
	 * http 不加密
	 * 
	 * @param urlStr
	 * @param str
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeHttpPost(String urlStr, String[] node,
			String[] values) throws ClientProtocolException, IOException,
			IllegalStateException, HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		Log.e("url", "  = = = ="+urlStr);
		Log.e("", " = = = node = = ="+node);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < node.length; i++) {
			Log.e(node[i], values[i]);
			params.add(new BasicNameValuePair(node[i], values[i]));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
//			Log.d("result:", EposAesUtil.decryptString(result, "dynamicode"));
			Log.d("result", result);
			return result;
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return "503";
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}

	public static HashMap<String, Object> parseXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new HashMap<String, Object>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.put("RSPCOD", parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.put("RSPMSG", parser.nextText().toString());
				} else if ("MERSTS".equals(parser.getName())) {
					map.put("MERSTS", parser.nextText().toString());
				} else if("LOGNUM".equals(parser.getName())){
					map.put("LOGNUM", parser.nextText().toString());
				}else if("RSPCOD".equals(parser.getName())){
					map.put("RSPCOD", parser.nextText().toString());
				}else if("STS".equals(parser.getName())){
					map.put("STS", parser.nextText().toString());
				}else if("REGFLAG".equals(parser.getName())){
					map.put("REGFLAG", parser.nextText().toString());
				}else if("NOCARDFEERATE".equals(parser.getName())){
					map.put("NOCARDFEERATE", parser.nextText().toString());
				}else if("ORDERID".equals(parser.getName())){
					map.put("ORDERID", parser.nextText().toString());
				}else if("ISACTFIRUSE".equals(parser.getName())){
					map.put("ISACTFIRUSE", parser.nextText().toString());
				}else if("TXNSTS".equals(parser.getName())){
					map.put("TXNSTS", parser.nextText().toString());
				}else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							System.out.println(rspKeys[i]);
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		
		return map;
	}

	public static HashMap<String, Object> parseXml(String in)
			throws XmlPullParserException, IOException {
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new HashMap<String, Object>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.put("RSPCOD", parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.put("RSPMSG", parser.nextText().toString());
				}

				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return map;
	}

	public static NewAuthenticationBean parseAuthenXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		NewAuthenticationBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new NewAuthenticationBean();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				}else if("IDCARDPIC".equals(parser.getName())){
					entitys.setIDCARDPIC(parser.nextText().toString());

				}else if("IDCARDPICSTA".equals(parser.getName())){
					entitys.setIDCARDPICSTA(parser.nextText().toString());

				}else if("IDCARDBACKPHOTO".equals(parser.getName())){
					entitys.setIDCARDBACKPHOTO(parser.nextText().toString());

				}else if("IDCARDBANKPICSTA".equals(parser.getName())){
					entitys.setIDCARDBANKPICSTA(parser.nextText().toString());

				}else if("BANKCARDPHOTO".equals(parser.getName())){
					entitys.setBANKCARDPHOTO(parser.nextText().toString());

				}else if("FRYHKIMGPATHSTA".equals(parser.getName())){
					entitys.setFRYHKIMGPATHSTA(parser.nextText().toString());

				}else if("PERSONPHOTO".equals(parser.getName())){
					entitys.setPERSONPHOTO(parser.nextText().toString());

				}else if("CUSTPICSTA".equals(parser.getName())){
					entitys.setCUSTPICSTA(parser.nextText().toString());

				}else if("MERCNAM".equals(parser.getName())){
					entitys.setMERCNAM(parser.nextText().toString());

				}else if("CORPORATEIDENTITY".equals(parser.getName())){
					entitys.setCORPORATEIDENTITY(parser.nextText().toString());

				}else if("BANKACCOUNT".equals(parser.getName())){
					entitys.setBANKACCOUNT(parser.nextText().toString());

				}else if("BANKNAME".equals(parser.getName())){
					entitys.setBANKNAME(parser.nextText().toString());

				}else if("IMG_URL".equals(parser.getName())){
					entitys.setIMG_URL(parser.nextText().toString());

				}else if("PROVID".equals(parser.getName())){
					entitys.setPROVID(parser.nextText().toString());

				}else if("PROVNAME".equals(parser.getName())){
					entitys.setPROVNAME(parser.nextText().toString());

				}else if("CITYID".equals(parser.getName())){
					entitys.setCITYID(parser.nextText().toString());

				}else if("CITYNAME".equals(parser.getName())){
					entitys.setCITYNAME(parser.nextText().toString());

				}else if("AREAID".equals(parser.getName())){
					entitys.setAREAID(parser.nextText().toString());

				}else if("AREANAME".equals(parser.getName())){
					entitys.setAREANAME(parser.nextText().toString());

				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
//					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return entitys;
	}
	
	public static DealRecordsEntity parseDealXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		DealRecordsEntity entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new DealRecordsEntity();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("NUMPERPAGE".equals(parser.getName())) {
					entitys.setNumPergage(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				}
				for (int i = 0; i < rspKeys.length; i++) {
					if (null != rspKeys[i]
							&& rspKeys[i].equals(parser.getName())) {
						map.put(rspKeys[i], parser.nextText().toString());
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return entitys;
	}

	// /**
	// * 获取找回密码的验证码
	// *
	// * @param status
	// * @param values
	// * @return
	// */
	// public static HashMap<String, Object> getVerification(int status,
	// String[] values) {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// HashMap<String, Object> map = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// Log.v("test", "keys:" + keys + "\n" + "values:" + values + "\n"
	// + "key:" + key + "\n" + "key2" + key2);
	// xmlStr = strToXml(keys, values, key, key2);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST + status + HttpUrls.SUFFIX,
	// xmlStr, key);
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (IllegalStateException e2) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "请求服务器异常！");
	// e2.printStackTrace();
	// return map;
	// } catch (Exception e) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// map = new HashMap<String, Object>();
	// map.put("RSPCOD", "0");
	// map.put("RSPMSG", "网络异常！");
	// return map;
	// }
	// try {
	// if (null != rspKeys) {
	// System.out.println("进入都这里");
	// map = parseVerificationXml(result, rspKeys);
	// } else {
	// System.out.println("进入下面都这里");
	// map = parseXml(result);
	// }
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	//
	// return map;
	// }

	/**
	 * 获取找回密码的验证码
	 * 
	 * @param status
	 * @param values
	 * @return
	 */
	public static HashMap<String, Object> getVerification(int status,
			String[] values) {
		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status + HttpUrls.SUFFIX,
					keys, values);
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e2.printStackTrace();
			return map;
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				System.out.println("进入都这里");
				map = parseVerificationXml(result, rspKeys);
			} else {
				System.out.println("进入下面都这里");
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 获取找回密码的xml pull解析
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static HashMap<String, Object> parseVerificationXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new HashMap<String, Object>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.put("RSPCOD", parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.put("RSPMSG", parser.nextText().toString());
				} else if ("PHONENUMBER".equals(parser.getName())) {
					map.put("PHONENUMBER", parser.nextText().toString());
				} else if ("IDCARDNUMBER".equals(parser.getName())) {
					map.put("IDCARDNUMBER", parser.nextText().toString());
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return map;
	}

	public static ProvinceEntity parseBankInfoXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {

		ProvinceEntity entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new ProvinceEntity();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("ISSNO".equals(parser.getName())) {
					entitys.setIssno(parser.nextText().toString());
				} else if ("ISSNAM".equals(parser.getName())) {
					entitys.setIssnam(parser.nextText().toString());
				} else if ("PROVNO".equals(parser.getName())) {
					entitys.setProvid(parser.nextText().toString());
				} else if ("PROVNAME".equals(parser.getName())) {
					entitys.setPronam(parser.nextText().toString());
				} else if ("CITYID".equals(parser.getName())) {
					entitys.setCityid(parser.nextText().toString());
				} else if ("CITYNAM".equals(parser.getName())) {
					entitys.setCitynam(parser.nextText().toString());
				} else if ("BKNO".equals(parser.getName())) {
					entitys.setBkno(parser.nextText().toString());
				} else if ("BENELX".equals(parser.getName())) {
					entitys.setBenelx(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				}

				for (int i = 0; i < rspKeys.length; i++) {
					if (null != rspKeys[i]
							&& rspKeys[i].equals(parser.getName())) {
						map.put(rspKeys[i], parser.nextText().toString());
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return entitys;
	}

	public static ProvinceEntity parseProvinceXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		ProvinceEntity entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new ProvinceEntity();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("ISSNO".equals(parser.getName())) {
					entitys.setIssno(parser.nextText().toString());
				} else if ("ISSNAM".equals(parser.getName())) {
					entitys.setIssnam(parser.nextText().toString());
				} else if ("DCFLAG".equals(parser.getName())) {
					entitys.setDcflag(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return entitys;
	}

	public static CityEntity parseCityXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		CityEntity entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new CityEntity();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("NUMPERPAGE".equals(parser.getName())) {

				} else if ("TOLCNT".equals(parser.getName())) {

				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				}
				for (int i = 0; i < rspKeys.length; i++) {
					if (null != rspKeys[i]
							&& rspKeys[i].equals(parser.getName())) {
						map.put(rspKeys[i], parser.nextText().toString());
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	// /**
	// * 订单请求返回
	// *
	// * @param status
	// * @param values
	// * @return
	// */
	// public static OrderPayBean getQueryOrderPay(int status, String[] values)
	// {
	// String rom = CipherUtil.getRomdan(32);
	// String key = CipherUtil.MD5STR(rom);
	// String key2 = CipherUtil.XOREncrypt(rom);
	//
	// OrderPayBean entitys = null;
	// String[] keys = HttpKeys.getAsk(status);
	// String[] rspKeys = HttpKeys.getBack(status);
	// String xmlStr = null;
	// try {
	// xmlStr = strToXml(keys, values, key, key2);
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// System.out.println("requst: " + xmlStr);
	// String result = null;
	// try {
	// result = executeHttpPost(HttpUrls.HOST_PAY + status
	// + HttpUrls.SUFFIX_POSP, xmlStr, key);
	//
	// } catch (ClientProtocolException e1) {
	//
	// e1.printStackTrace();
	// } catch (IOException e1) {
	//
	// e1.printStackTrace();
	// } catch (Exception e) {
	// entitys = new OrderPayBean();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("请求服务器异常！");
	// e.printStackTrace();
	// }
	// if (result == null) {
	// entitys = new OrderPayBean();
	// entitys.setRspcod("0");
	// entitys.setRspmsg("网络异常！");
	//
	// return entitys;
	// }
	// try {
	//
	// // entitys = parseDealXml(result, rspKeys);
	//
	// entitys = parseOrderPayXml(result, rspKeys);
	//
	// } catch (XmlPullParserException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// return entitys;
	// }

	/**
	 * 订单请求返回
	 * 
	 * @param status
	 * @param values
	 * @return
	 */
	public static OrderPayBean getQueryOrderPay(int status, String[] values) {

		OrderPayBean entitys = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
			Log.e("", "result = = = ="+result);

		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (Exception e) {
			entitys = new OrderPayBean();
			entitys.setRspcod("0");
			entitys.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			entitys = new OrderPayBean();
			entitys.setRspcod("0");
			entitys.setRspmsg("网络异常！");

			return entitys;
		}
		try {

//			 entitys = parseDealXml(result, rspKeys);

			entitys = parseOrderPayXml(result, rspKeys);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return entitys;
	}

	/**
	 * 订单列表
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static OrderPayBean parseOrderPayXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		
		Log.e("", "++111111");
		OrderPayBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new OrderPayBean();

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("NUMPERPAGE".equals(parser.getName())) {
					entitys.setNumberPage(parser.nextText().toString());
				} else if ("PHONENUMBER".equals(parser.getName())) {
					entitys.setMobile(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
						         map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		Log.e("", "++14444444"+entitys.toString());
		return entitys;
		
		
	}

	/**
	 * 未加密的请求格式(致富宝账单特殊接口)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static RichTreasureBillBean getRichTreasureBill(int status,
			String[] values) {
		RichTreasureBillBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new RichTreasureBillBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new RichTreasureBillBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new RichTreasureBillBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new RichTreasureBillBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = richTreasureBillXml(result, rspKeys);
			} else {
				map = richTreasureBillXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	/**
	 * 未加密的请求格式(费率列表)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static MyCircleBean getMyCircleBean(int status,
			String[] values) {
		MyCircleBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new MyCircleBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new MyCircleBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new MyCircleBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new MyCircleBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = richMyCircleBeanXml(result, rspKeys);
			} else {
				map = richMyCircleBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static MyCircleBean richMyCircleBeanXml(String in)
			throws XmlPullParserException, IOException {
		MyCircleBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new MyCircleBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	/**
	 * 账单返回实体节点获取
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static MyCircleBean richMyCircleBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		MyCircleBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new MyCircleBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				}else if("TOTSHRAMT".equals(parser.getName())){
					entitys.setTotshramt(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	
	/**
	 * 未加密的请求格式(费率列表)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static RateListBean getRateListBean(int status,
			String[] values) {
		RateListBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new RateListBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new RateListBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new RateListBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new RateListBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = richRateListBeanXml(result, rspKeys);
			} else {
				map = richRateListBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	

	/**
	 * 致富宝账单返回实体节点获取
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static RichTreasureBillBean richTreasureBillXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		RichTreasureBillBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new RichTreasureBillBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("NUMPERPAGE".equals(parser.getName())) {
					entitys.setNumperpage(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("EXPENDITURE".equals(parser.getName())) {
					entitys.setExpenditure(parser.nextText().toString());
				} else if ("INCOME".equals(parser.getName())) {
					entitys.setIncome(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	/**
	 * 账单返回实体节点获取
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static RateListBean richRateListBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		RateListBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new RateListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					
					entitys.setRspmsg(parser.nextText().toString());
				}else if("NOCARDFEERATMOD".equals(parser.getName())){
					entitys.setNocr(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	public static RateListBean richRateListBeanXml(String in)
			throws XmlPullParserException, IOException {
		RateListBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new RateListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	public static RichTreasureBillBean richTreasureBillXml(String in)
			throws XmlPullParserException, IOException {
		RichTreasureBillBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new RichTreasureBillBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}

	/**
	 * 获取找回密码的验证码(不加密)Midatc
	 * 
	 * @param status
	 * @param values
	 * @return
	 */
	public static HashMap<String, Object> getVerificationMidatc(int status,
			String[] values) {
		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX, keys, values);
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e2.printStackTrace();
			return map;
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				System.out.println("进入都这里");
				map = parseVerificationMidatcXml(result, rspKeys);
			} else {
				System.out.println("进入下面都这里");
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 获取找回密码的xml pull解析 Midatc
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static HashMap<String, Object> parseVerificationMidatcXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new HashMap<String, Object>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.put("RSPCOD", parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.put("RSPMSG", parser.nextText().toString());
				} else if ("PHONENUMBER".equals(parser.getName())) {
					map.put("PHONENUMBER", parser.nextText().toString());
				} else if ("IDCARDNUMBER".equals(parser.getName())) {
					map.put("IDCARDNUMBER", parser.nextText().toString());
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return map;
	}

	/**
	 * 未加密的请求格式(致富宝获取定存模式列表信息)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static BasisModel getBasisModel(int status, String[] values) {
		BasisModel map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new BasisModel();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new BasisModel();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new BasisModel();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new BasisModel();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			map = parseBasisModelXml(result, rspKeys);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取定存模式列表
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static BasisModel parseBasisModelXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		BasisModel entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				entitys = new BasisModel();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					System.out.println("RSPCODRSPCODRSPCODRSPCOD");
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							System.out.println(parser.getName());
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return entitys;
	}

	/**
	 * 未加密的请求格式(致富宝定期特殊接口)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static Basis getBasis(int status, String[] values) {
		Basis map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new Basis();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new Basis();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new Basis();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new Basis();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = basisListXml(result, rspKeys);
			} else {
				map = basisListXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 我的定期信息接口
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Basis basisListXml(String in, String[] rspKeys)
			throws XmlPullParserException, IOException {
		Basis entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new Basis();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("NUMPERPAGE".equals(parser.getName())) {
					entitys.setNumperpage(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("SUMPRINCIPAL".equals(parser.getName())) {
					entitys.setSumprincipal(parser.nextText().toString());
				} else if ("PAGENUM".equals(parser.getName())) {
					entitys.setPagenum(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							System.out.println(parser.getName());
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	/**
	 * 如果致富宝未定义返回节点自动去获取RSPCOD、RSPMSG
	 * 
	 * @param in
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Basis basisListXml(String in) throws XmlPullParserException,
			IOException {
		Basis map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new Basis();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	/**
	 * 未加密的请求格式(手机充值列表)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static PhoneChargeBean getPay1(int status,
			String[] values) {
		PhoneChargeBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost1(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys);
			Log.e("", "result = = = = = "+result);
		} catch (ClientProtocolException e) {
			map = new PhoneChargeBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new PhoneChargeBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new PhoneChargeBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new PhoneChargeBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = phonechargeBeanXml(result, rspKeys);
			} else {
				map = phonechargeBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * http 不加密
	 * 
	 * @param urlStr
	 * @param str
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeHttpPost1(String urlStr, String[] node) throws ClientProtocolException, IOException,
			IllegalStateException, HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		Log.e("", "  = = = ="+urlStr);
		Log.e("", " = = = node = = ="+node);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		for (int i = 0; i < node.length; i++) {
//			Log.e(node[i], values[i]);
//			params.add(new BasicNameValuePair(node[i], values[i]));
//		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
//			Log.d("result:", EposAesUtil.decryptString(result, "dynamicode"));
			return result;
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return "503";
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> executeHttpPostnull(String urlStr, String[] myprofitback,String [] node,String [] values)
			throws ClientProtocolException, IOException, IllegalStateException,
			HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		Log.e("url", urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		 for (int i = 0; i < node.length; i++) {
		 Log.e(node[i], values[i]);
		 params.add(new BasicNameValuePair(node[i], values[i]));
		 }
		 Log.e("", ""+httpPost.getURI());
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			Log.e("", "result = =  "+result);
			return getjson(result,myprofitback);
			
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}
	public static ArrayList<HashMap<String, Object>> executeHttpPostnull(String urlStr, String[] myprofitback)
			throws ClientProtocolException, IOException, IllegalStateException,
			HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		Log.e("url", urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// for (int i = 0; i < node.length; i++) {
		// Log.e(node[i], values[i]);
		// params.add(new BasicNameValuePair(node[i], values[i]));
		// }
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient1().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			Log.e("", "result = =  "+result);
			return getjson(result,myprofitback);
			
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> executeHttpPostnull1(String urlStr, String[] myprofitback)
			throws ClientProtocolException, IOException, IllegalStateException,
			HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Log.e("url", ""+urlStr);
		// for (int i = 0; i < node.length; i++) {
		// Log.e(node[i], values[i]);
		// params.add(new BasicNameValuePair(node[i], values[i]));
		// }
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			Log.e("", "result = =  "+result);
			return getjson(result,myprofitback);
			
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}
	
	//json解析
	public static ArrayList<HashMap<String, Object>> getjson(String json, String[] myprofitback) throws JSONException{
		ArrayList<HashMap<String, Object>> array = null; 
		if (json == null) {
			return null;
		} else {
		
		array = new ArrayList<HashMap<String,Object>>();
		
		JSONArray jsondata = new JSONArray(json);  
		for (int i = 0; i < jsondata.length(); i++) {
			JSONObject jsonobject = jsondata.getJSONObject(i);
			HashMap<String, Object> map  = new HashMap<String, Object>();
			for (int j = 0; j < myprofitback.length; j++) {
				map.put(myprofitback[j], jsonobject.get(myprofitback[j]));
				
			}
			array.add(map);
			
		}
		
	}
	return array;
		
	}
	
	
	
	/**
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static PhoneChargeBean phonechargeBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		PhoneChargeBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new PhoneChargeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	public static PhoneChargeBean phonechargeBeanXml(String in)
			throws XmlPullParserException, IOException {
		PhoneChargeBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new PhoneChargeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	
	/**
	 * 未加密的请求格式(行用卡列表)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static CreditListBean getPay2(int status,
			String[] values) {
		CreditListBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys,values);
		} catch (ClientProtocolException e) {
			map = new CreditListBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new CreditListBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new CreditListBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new CreditListBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = CreditBeanXml(result, rspKeys);
			} else {
				map = CreditBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 未加密的请求格式(行用卡列表)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static BankListBean getBankList(int status,
			String[] values) {
		BankListBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys,values);
		} catch (ClientProtocolException e) {
			map = new BankListBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new BankListBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new BankListBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new BankListBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = BankCardBeanXml(result, rspKeys);
			} else {
				map = BankCardBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * http 不加密
	 * 
	 * @param urlStr
	 * @param str
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeHttpPost2(String urlStr, String[] node,String[] values) throws ClientProtocolException, IOException,
			IllegalStateException, HttpHostConnectException, Exception {
		HttpPost httpPost = new HttpPost(urlStr);
		Log.e("url", urlStr);
		Log.e("", " = = = node = = ="+node);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < node.length; i++) {
			Log.e(node[i], values[i]);
			params.add(new BasicNameValuePair(node[i], values[i]));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
				httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
//			Log.d("result:", EposAesUtil.decryptString(result, "dynamicode"));
			return result;
		} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
			Log.v("HttpError", "503Error");
			return "503";
		} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
			Log.v("HttpError", "400Error");
			return null;
		} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
			Log.v("HttpError", "500Error");
			return null;
		} else {
			Log.v("HttpError", "其他ERROR"
					+ httpResponse.getStatusLine().getStatusCode());
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static CreditListBean CreditBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		CreditListBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new CreditListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	public static CreditListBean CreditBeanXml(String in)
			throws XmlPullParserException, IOException {
		CreditListBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new CreditListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	
	/**
	 * 
	 * @param in
	 * @param rspKeys
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static BankListBean BankCardBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		BankListBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new BankListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	public static BankListBean BankCardBeanXml(String in)
			throws XmlPullParserException, IOException {
		BankListBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new BankListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
//	public static HashMap<String, Object> getPhoneCharge(int status, String[] values) {
//
//		HashMap<String, Object> map = null;
//		String[] keys = HttpKeys.getAsk(status);
//		String[] rspKeys = HttpKeys.getBack(status);
//		String result = null;
//		try {
//			result = executeHttpPost(HttpUrls.HOST_PAY + status
//					+ HttpUrls.SUFFIX_POSP, keys, values);
//		} catch (ClientProtocolException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		} catch (Exception e) {
//			map = new HashMap<String, Object>();
//			map.put("RSPCOD", "0");
//			map.put("RSPMSG", "请求服务器异常！");
//			e.printStackTrace();
//		}
//		if (result == null) {
//			map = new HashMap<String, Object>();
//			map.put("RSPCOD", "0");
//			map.put("RSPMSG", "网络异常！");
//			return map;
//		}
//		try {
//			result = result.replace("&", "&amp;");
//			Log.e("result", result);
//			if (null != rspKeys) {
//				map = parseXml(result, rspKeys);
//			} else {
//				map = parseXml(result);
//			}
//		} catch (XmlPullParserException e) {
//
//			e.printStackTrace();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//		return map;
//	}
	
	/**
	 * 未加密的请求格式
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static HashMap<String, Object> getAgentMidatc(int status, String[] values) {
		Log.e("", "=========values" + values);
		Log.e("", "=========status" + status);
		HashMap<String, Object> map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络通讯错误！");
			Log.e("error", "网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "读写错误！");
			Log.e("error", "读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "请求服务器异常！");
			Log.e("error", "请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new HashMap<String, Object>();
			Log.e("error", "网络异常！");
			map.put("RSPCOD", "0");
			map.put("RSPMSG", "网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = parseXml(result, rspKeys);
			} else {
				map = parseXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 未加密的请求格式(致富宝账单特殊接口)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static MyAgtBean getSubAgentBill(int status,
			String[] values) {
		MyAgtBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e) {
			map = new MyAgtBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new MyAgtBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new MyAgtBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new MyAgtBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = subAgtXml(result, rspKeys);
			} else {
				map = subAgtXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static MyAgtBean subAgtXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		MyAgtBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new MyAgtBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if("TRANDETAIL".equals(parser.getName())){
					map = new HashMap<String, Object>();
				}else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	public static MyAgtBean subAgtXml(String in)
			throws XmlPullParserException, IOException {
		MyAgtBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new MyAgtBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	
	
	/**
	 * 未加密的请求格式(致富宝账单特殊接口)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static ReturnActcodeBean ActcodeBill(int status,
			String[] values) {
		ReturnActcodeBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e) {
			map = new ReturnActcodeBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new ReturnActcodeBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new ReturnActcodeBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new ReturnActcodeBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = ActcodeXml(result, rspKeys);
			} else {
				map = ActcodeXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static ReturnActcodeBean ActcodeXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		ReturnActcodeBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new ReturnActcodeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if("TRANDETAIL".equals(parser.getName())){
					map = new HashMap<String, Object>();
				}else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	public static ReturnActcodeBean ActcodeXml(String in)
			throws XmlPullParserException, IOException {
		ReturnActcodeBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new ReturnActcodeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	/**
	 * 未加密的请求格式(致富宝账单特殊接口)
	 * 
	 * @param status
	 *            请求的交易码
	 * @param values
	 *            请求的数据
	 * @return
	 */
	public static ActCodeBean ActcodelistBill(int status,
			String[] values) {
		ActCodeBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST_PAY + status
					+ HttpUrls.SUFFIX_POSP, keys, values);
		} catch (ClientProtocolException e) {
			map = new ActCodeBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new ActCodeBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new ActCodeBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new ActCodeBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = ActcodelistXml(result, rspKeys);
			} else {
				map = ActcodelistXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static ActCodeBean ActcodelistXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		ActCodeBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new ActCodeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if("TRANDETAIL".equals(parser.getName())){
					map = new HashMap<String, Object>();
				}else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	public static ActCodeBean ActcodelistXml(String in)
			throws XmlPullParserException, IOException {
		ActCodeBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new ActCodeBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	
	public static ShareDetailsBean getShareDetailsBean(int status,
			String[] values) {
		ShareDetailsBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new ShareDetailsBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new ShareDetailsBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new ShareDetailsBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new ShareDetailsBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = ShareDetailsBeanXml(result, rspKeys);
			} else {
				map = ShareDetailsBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static  ShareDetailsBean ShareDetailsBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		ShareDetailsBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new ShareDetailsBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	public static ShareDetailsBean ShareDetailsBeanXml(String in)
			throws XmlPullParserException, IOException {
		ShareDetailsBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new ShareDetailsBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	public static PromotionEarningBean getPromotionEarningBean(int status,
			String[] values) {
		PromotionEarningBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new PromotionEarningBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new PromotionEarningBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new PromotionEarningBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new PromotionEarningBean();
			map.setRspcod("0");
			map.setRspmsg("网络异常！");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = PromotionEarningBeanXml(result, rspKeys);
			} else {
				map = PromotionEarningBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static  PromotionEarningBean PromotionEarningBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		PromotionEarningBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new PromotionEarningBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					entitys.setRspmsg(parser.nextText().toString());
				} else if ("TOLCNT".equals(parser.getName())) {
					entitys.setTolcnt(parser.nextText().toString());
				} else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}
	
	public static PromotionEarningBean PromotionEarningBeanXml(String in)
			throws XmlPullParserException, IOException {
		PromotionEarningBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new PromotionEarningBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	public static MoneyListBean getMoneyListBean(int status,
			String[] values) {
		MoneyListBean map = null;
		String[] keys = HttpKeys.getAsk(status);
		String[] rspKeys = HttpKeys.getBack(status);
		String result = null;
		try {
			result = executeHttpPost(HttpUrls.HOST + status
					+ HttpUrls.SUFFIX_MIDATC, keys, values);
		} catch (ClientProtocolException e) {
			map = new MoneyListBean();
			map.setRspcod("0");
			map.setRspmsg("网络通讯错误！");
			e.printStackTrace();
		} catch (IOException e) {
			map = new MoneyListBean();
			map.setRspcod("0");
			map.setRspmsg("读写错误！");
			e.printStackTrace();
		} catch (Exception e) {
			map = new MoneyListBean();
			map.setRspcod("0");
			map.setRspmsg("请求服务器异常！");
			e.printStackTrace();
		}
		if (result == null) {
			map = new MoneyListBean();
			map.setRspcod("0");
			map.setRspmsg("当前网络不可用,请检查网络设置");
			return map;
		}
		try {
			if (null != rspKeys) {
				map = richMoneyListBeanXml(result, rspKeys);
			} else {
				map = richMoneyListBeanXml(result);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static MoneyListBean richMoneyListBeanXml(String in,
			String[] rspKeys) throws XmlPullParserException, IOException {
		MoneyListBean entitys = null;
		HashMap<String, Object> map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				// map=new HashMap<String, Object>();
				entitys = new MoneyListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					entitys.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					
					entitys.setRspmsg(parser.nextText().toString());
				}else if("NOCARDFEERATMOD".equals(parser.getName())){
					
				}else if ("TRANDETAIL".equals(parser.getName())) {
					map = new HashMap<String, Object>();
				} else {
					for (int i = 0; i < rspKeys.length; i++) {
						if (null != rspKeys[i]
								&& rspKeys[i].equals(parser.getName())) {
							map.put(rspKeys[i], parser.nextText().toString());
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("TRANDETAIL".equals(parser.getName())) {
					entitys.list.add(map);
					map = null;
				}
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return entitys;
	}

	public static MoneyListBean richMoneyListBeanXml(String in)
			throws XmlPullParserException, IOException {
		MoneyListBean map = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(in));
		int event = parser.getEventType();// 产生第一个事件
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
				map = new MoneyListBean();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("RSPCOD".equals(parser.getName())) {
					map.setRspcod(parser.nextText().toString());
				} else if ("RSPMSG".equals(parser.getName())) {
					map.setRspmsg(parser.nextText().toString());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				break;

			default:
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;
	}
	
	  public static TransactionTypeBean getTransactionTypeBean(int status,
				String[] values) {
	    	TransactionTypeBean map = null;
			String[] keys = HttpKeys.getAsk(status);
			String[] rspKeys = HttpKeys.getBack(status);
			String result = null;
			try {
				result = executeHttpPost(HttpUrls.HOST + status
						+ HttpUrls.SUFFIX_MIDATC, keys, values);
			} catch (ClientProtocolException e) {
				map = new TransactionTypeBean();
				map.setRspcod("0");
				map.setRspmsg("网络通讯错误！");
				e.printStackTrace();
			} catch (IOException e) {
				map = new TransactionTypeBean();
				map.setRspcod("0");
				map.setRspmsg("读写错误！");
				e.printStackTrace();
			} catch (Exception e) {
				map = new TransactionTypeBean();
				map.setRspcod("0");
				map.setRspmsg("请求服务器异常！");
				e.printStackTrace();
			}
			if (result == null) {
				map = new TransactionTypeBean();
				map.setRspcod("0");
				map.setRspmsg("当前网络不可用,请检查网络设置");
				return map;
			}
			try {
				if (null != rspKeys) {
					map = richTransactionTypeBean(result, rspKeys);
				} else {
					map = richTransactionTypeBean(result);
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return map;
		}
	    
	    public static TransactionTypeBean richTransactionTypeBean(String in,
				String[] rspKeys) throws XmlPullParserException, IOException {
	    	TransactionTypeBean entitys = null;
			HashMap<String, Object> map = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(in));
			int event = parser.getEventType();// 产生第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
					// map=new HashMap<String, Object>();
					entitys = new TransactionTypeBean();
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("RSPCOD".equals(parser.getName())) {
						entitys.setRspcod(parser.nextText().toString());
					} else if ("RSPMSG".equals(parser.getName())) {
						entitys.setRspmsg(parser.nextText().toString());
					} else if ("TRANDETAIL".equals(parser.getName())) {
						map = new HashMap<String, Object>();
					} else {
						for (int i = 0; i < rspKeys.length; i++) {
							if (null != rspKeys[i]
									&& rspKeys[i].equals(parser.getName())) {
								map.put(rspKeys[i], parser.nextText().toString());
							}
						}
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					if ("TRANDETAIL".equals(parser.getName())) {
						entitys.list.add(map);
						map = null;
					}
					break;

				default:
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}
			return entitys;
		}
	    
	    public static TransactionTypeBean richTransactionTypeBean(String in)
				throws XmlPullParserException, IOException {
	    	TransactionTypeBean map = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(in));
			int event = parser.getEventType();// 产生第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
					map = new TransactionTypeBean();
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("RSPCOD".equals(parser.getName())) {
						map.setRspcod(parser.nextText().toString());
					} else if ("RSPMSG".equals(parser.getName())) {
						map.setRspmsg(parser.nextText().toString());
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					break;

				default:
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}
			return map;
		}
	    
		public static ArrayList<HashMap<String, Object>> getList(int status, String[] values,String[] rspKeys) {
			Log.e("", "=========values" + values);
			Log.e("", "=========status" + status);
			ArrayList<HashMap<String, Object>> map = null;
			String[] keys = HttpKeys.getAsk(status);
//			String[] rspKeys = HttpKeys.getBack(status);
			String result = null;
			try {
				result = executeHttpPost(HttpUrls.HOST + status
						+ HttpUrls.SUFFIX_MIDATC, keys, values);
			} catch (ClientProtocolException e) {
//				map = new ArrayList<HashMap<String,Object>>();
//				map.get(0).put("RSPCOD", "0");
//				map.get(0).put("RSPMSG", "网络通讯错误！");
				Log.e("error", "网络通讯错误！");
				e.printStackTrace();
			} catch (IOException e) {
//				map = new ArrayList<HashMap<String,Object>>();
//				map.get(0).put("RSPCOD", "0");
//				map.get(0).put("RSPMSG", "读写错误！");
				Log.e("error", "读写错误！");
				e.printStackTrace();
			} catch (Exception e) {
//				map = new ArrayList<HashMap<String,Object>>();
//				map.get(0).put("RSPCOD", "0");
//				map.get(0).put("RSPMSG", "请求服务器异常！");
				Log.e("error", "请求服务器异常！");
				e.printStackTrace();
			}
			if (result == null) {
//				map = new ArrayList<HashMap<String,Object>>();
//				Log.e("error", "当前网络不可用,请检查网络设置");
//				map.get(0).put("RSPCOD", "0");
//				map.get(0).put("RSPMSG", "当前网络不可用,请检查网络设置");
				return map;
			}
			try {
				if (null != rspKeys) {
					map = getListXml(result, rspKeys);
				} else {
					map = getListXml(result);
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return map;
		}
		
		
		public static ArrayList<HashMap<String, Object>> getListXml(String in,
				String[] rspKeys) throws XmlPullParserException, IOException{
			ArrayList<HashMap<String, Object>> datalist = null;
			HashMap<String, Object> map = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(in));
			String tol = "";
			int event = parser.getEventType();// 产生第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
					// map=new HashMap<String, Object>();
					datalist = new ArrayList<HashMap<String,Object>>();
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("RSPCOD".equals(parser.getName())) {
					} else if ("RSPMSG".equals(parser.getName())) {
					} else if ("TOLCNT".equals(parser.getName())) {
						tol = parser.nextText().toString();
					} else if ("TRANDETAIL".equals(parser.getName())) {
						map = new HashMap<String, Object>();

					} else {
						for (int i = 0; i < rspKeys.length; i++) {
							if (null != rspKeys[i]
									&& rspKeys[i].equals(parser.getName())) {
								map.put(rspKeys[i], parser.nextText().toString());
							}
						}
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					if ("TRANDETAIL".equals(parser.getName())) {
						datalist.add(map);
						map = null;
					}
					break;

				default:
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}
			Log.e("", ""+datalist);
			Log.e("", ""+datalist.size());
			if(datalist.size()>0){
				datalist.get(0).put("TOLCNT",tol);
			}
			return datalist;
			
		}
		
		public static ArrayList<HashMap<String, Object>> getListXml(String in) throws XmlPullParserException, IOException{
			ArrayList<HashMap<String, Object>> datalist = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(in));
			int event = parser.getEventType();// 产生第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件 :
					datalist = new ArrayList<HashMap<String,Object>>();
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("RSPCOD".equals(parser.getName())) {
					} else if ("RSPMSG".equals(parser.getName())) {
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					break;

				default:
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}
			return datalist;
			
		}
		
		public static HashMap<String, Object> executeHttpPostnewpay(String urlStr, String[] myprofitback,String [] node,String [] values)
				throws ClientProtocolException, IOException, IllegalStateException,
				HttpHostConnectException, Exception {
			Log.e("url", urlStr);
			HttpPost httpPost = new HttpPost(urlStr);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			 for (int i = 0; i < node.length; i++) {
			 Log.e(node[i], values[i]);
			 params.add(new BasicNameValuePair(node[i], values[i]));
			 }
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
					httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("", "result = =  "+result);
				return getjson1(result,myprofitback);
				
			} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
				Log.v("HttpError", "503Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
				Log.v("HttpError", "400Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
				Log.v("HttpError", "500Error");
				return null;
			} else {
				Log.v("HttpError", "其他ERROR"
						+ httpResponse.getStatusLine().getStatusCode());
				return null;
			}
		}
		
		public static HashMap<String, Object> executeHttpPostnulls(String urlStr, String[] myprofitback,String [] node,String [] values)
				throws ClientProtocolException, IOException, IllegalStateException,
				HttpHostConnectException, Exception {
			for (int i = 0; i < node.length; i++) {
				if (i == 0)
					urlStr = urlStr + "?" + node[i] + "=" + values[i];
				else
					urlStr = urlStr + "&" + node[i] + "=" + values[i];
			}
			Log.e("url", urlStr);
			HttpPost httpPost = new HttpPost(urlStr);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// for (int i = 0; i < node.length; i++) {
			// Log.e(node[i], values[i]);
			// params.add(new BasicNameValuePair(node[i], values[i]));
			// }
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
					httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("", "result = =  "+result);
				return getjson1(result,myprofitback);
				
			} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
				Log.v("HttpError", "503Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
				Log.v("HttpError", "400Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
				Log.v("HttpError", "500Error");
				return null;
			} else {
				Log.v("HttpError", "其他ERROR"
						+ httpResponse.getStatusLine().getStatusCode());
				return null;
			}
		}
		
//		//json解析
//		public static ArrayList<HashMap<String, Object>> getjson(String json, String[] myprofitback) throws JSONException{
//			ArrayList<HashMap<String, Object>> array = null; 
//			if (json == null) {
//				return null;
//			} else {
//			array = new ArrayList<HashMap<String,Object>>();
//			JSONArray jsondata = new JSONArray(json);  
//			for (int i = 0; i < jsondata.length(); i++) {
//				JSONObject jsonobject = jsondata.getJSONObject(i);
//				HashMap<String, Object> map  = new HashMap<String, Object>();
//				for (int j = 0; j < myprofitback.length; j++) {
//					map.put(myprofitback[j], jsonobject.get(myprofitback[j]));
//					
//				}
//				array.add(map);
//			}
//		}
//		return array;
//			
//		}
		
		//json解析
		public static HashMap<String, Object> getjson1(String json, String[] myprofitback)
				throws JSONException {
			HashMap<String, Object> ss= new HashMap<String, Object>();
 			String str = null;
			if (json == null) {
				return null;
			} else {
				JSONObject jsonobject = new JSONObject(json);
				for (int i = 0; i < myprofitback.length; i++) {
						try {
							ss.put(myprofitback[i], jsonobject.getString(myprofitback[i]));
						} catch (Exception e) {
							// TODO: handle exception
						}
				}
			}
			Log.e("", ""+ss.toString());
			return ss;
		}
		
		public static List<WechatListBean> executeHttpPostgetjson(String urlStr, String[] myprofitback,String [] node,String [] values,int a)
				throws ClientProtocolException, IOException, IllegalStateException,
				HttpHostConnectException, Exception {
			HttpPost httpPost = new HttpPost(urlStr);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			 for (int i = 0; i < node.length; i++) {
//			 Log.e(node[i], values[i]);
			 params.add(new BasicNameValuePair(node[i], values[i]));
			 }
			 Log.e("url", urlStr);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
					httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("", "result = =  "+result);
				if(a==0){
					return getjsonlist1(result,myprofitback);
				}else{
					return getjsonlistf(result,myprofitback);
				}
				
				
			} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
				Log.v("HttpError", "503Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
				Log.v("HttpError", "400Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
				Log.v("HttpError", "500Error");
				return null;
			} else {
				Log.v("HttpError", "其他ERROR"
						+ httpResponse.getStatusLine().getStatusCode());
				return null;
			}
		}
		
		
		public static ArrayList<HashMap<String, Object>> executeHttpPostgetjsonlist(String urlStr, String[] myprofitback,String [] node,String [] values,int a)
				throws ClientProtocolException, IOException, IllegalStateException,
				HttpHostConnectException, Exception {
			HttpPost httpPost = new HttpPost(urlStr);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			 for (int i = 0; i < node.length; i++) {
//			 Log.e(node[i], values[i]);
			 params.add(new BasicNameValuePair(node[i], values[i]));
			 }
			 Log.e("url", urlStr);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(
					httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("", "result = =  "+result);
				if(a==0){
					return getjsonlist2(result,myprofitback);
				}else if(a==1){
					return getjsonlist3(result,myprofitback);
				}else{
					return getjsonlist4(result,myprofitback);
				}
				
				
			} else if (httpResponse.getStatusLine().getStatusCode() == 503) {
				Log.v("HttpError", "503Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
				Log.v("HttpError", "400Error");
				return null;
			} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
				Log.v("HttpError", "500Error");
				return null;
			} else {
				Log.v("HttpError", "其他ERROR"
						+ httpResponse.getStatusLine().getStatusCode());
				return null;
			}
		}
		
		//json解析多数据组
		public static List<WechatListBean> getjsonlist(String json, String[] myprofitback) throws JSONException{
			 List<WechatListBean> listwecbean = new ArrayList<WechatListBean>();
			
			if (json == null) {
				return null;
			} else {
				
				JSONArray jsondata = new JSONArray(json);  
				for (int i = 0; i < jsondata.length(); i++) {
					ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String,Object>>(); 
					JSONObject jsonobject = jsondata.getJSONObject(i);
					WechatListBean wacahtbean = new WechatListBean();
					wacahtbean.setPayDate(jsonobject.getString("payDate"));
					wacahtbean.setTotalFee(jsonobject.getString("totalFee"));
					wacahtbean.setOrderCount(jsonobject.getString("orderCount"));
					JSONArray arr = jsonobject.getJSONArray("infos");
					for (int j = 0; j < arr.length(); j++) {
						JSONObject jsonob = arr.getJSONObject(j);
						HashMap<String, Object> map  = new HashMap<String, Object>();
							map.put("totalFee", jsonob.get("totalFee"));
							map.put("payTime", jsonob.get("payTime"));
						
						array.add(map);
					}
					wacahtbean.list.addAll(array);
					
					listwecbean.add(wacahtbean);
				}
			}
		return listwecbean;
		}
		
		
		//json解析多数据组
		public static ArrayList<HashMap<String, Object>> getjsonlist2(String json, String[] myprofitback) throws JSONException{
			ArrayList<HashMap<String, Object>> listwecbean = new ArrayList<HashMap<String,Object>>();
			
			if (json == null) {
				return null;
			} else {
				
				JSONArray jsondata = new JSONArray(json);  
//				HashMap< String, Object> hs = null;
				for (int i = 0; i < jsondata.length(); i++) {
					
					HashMap< String, Object> hs = new HashMap<String, Object>();
					JSONObject jsonobject = jsondata.getJSONObject(i);
					for (int k  = 0; k < myprofitback.length; k++) {
						hs.put(myprofitback[k], jsonobject.get(myprofitback[k]));
					}
//					ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String,Object>>(); 
//					JSONObject jsonobject = jsondata.getJSONObject(i);
//					WechatListBean wacahtbean = new WechatListBean();
//					wacahtbean.setPayDate(jsonobject.getString("payDate"));
//					wacahtbean.setTotalFee(jsonobject.getString("totalFee"));
//					wacahtbean.setOrderCount(jsonobject.getString("orderCount"));
//					JSONArray arr = jsonobject.getJSONArray("infos");
//					for (int j = 0; j < arr.length(); j++) {
//						JSONObject jsonob = arr.getJSONObject(j);
//						HashMap<String, Object> map  = new HashMap<String, Object>();
//							map.put("totalFee", jsonob.get("totalFee"));
//							map.put("payTime", jsonob.get("payTime"));
//						
//						array.add(map);
//					}
//					wacahtbean.list.addAll(array);
					listwecbean.add(hs);
				}
			
			}
		return listwecbean;
		}
		
				public static ArrayList<HashMap<String, Object>> getjsonlist3(String json, String[] myprofitback) throws JSONException{
					ArrayList<HashMap<String, Object>> listwecbean = new ArrayList<HashMap<String,Object>>();
					
					if (json == null) {
						return null;
					} else {
						JSONArray obj = new JSONArray(json);
						String monthTotalCount = "";
						String monthTotalFee = "";
						if(obj.getJSONObject(0).get("monthTotalCount")!=null){
							monthTotalCount = obj.getJSONObject(0).get("monthTotalCount").toString();
							monthTotalFee  = obj.getJSONObject(0).get("monthTotalFee").toString();
						}
						JSONArray jsondata = obj.getJSONObject(0).getJSONArray("payList");  
//						HashMap< String, Object> hs = null;
						for (int i = 0; i < jsondata.length(); i++) {
							
							HashMap< String, Object> hs = new HashMap<String, Object>();
							hs.put("monthTotalCount", monthTotalCount);
							hs.put("monthTotalFee", monthTotalFee);
							JSONObject jsonobject = jsondata.getJSONObject(i);
							for (int k  = 0; k < myprofitback.length; k++) {
								hs.put(myprofitback[k], jsonobject.get(myprofitback[k]));
							}
							listwecbean.add(hs);
						}
					
					}
				return listwecbean;
				}
				
				//json解析多数据组
				public static ArrayList<HashMap<String, Object>> getjsonlist4(String json, String[] myprofitback) throws JSONException{
					ArrayList<HashMap<String, Object>> listwecbean = new ArrayList<HashMap<String,Object>>();
					
					if (json == null) {
						return null;
					} else {
						JSONObject jso = new JSONObject(json);
//						JSONArray obj = new JSONArray(json);
						String monthTotalCount = "";
						String monthTotalFee = "";
						if(jso.get("dayTotalFee")!=null){
							monthTotalCount = jso.get("dayTotalCount").toString();
							monthTotalFee  = jso.get("dayTotalFee").toString();
						}
						JSONArray jsondata = jso.getJSONArray("payList");  
//						HashMap< String, Object> hs = null;
						for (int i = 0; i < jsondata.length(); i++) {
							
							HashMap< String, Object> hs = new HashMap<String, Object>();
							hs.put("dayTotalCount", monthTotalCount);
							hs.put("dayTotalFee", monthTotalFee);
							JSONObject jsonobject = jsondata.getJSONObject(i);
							for (int k  = 0; k < myprofitback.length; k++) {
								hs.put(myprofitback[k], jsonobject.get(myprofitback[k]));
							}
							listwecbean.add(hs);
						}
					
					}
				return listwecbean;
				}
		
		//json解析多数据组
				public static List<WechatListBean> getjsonlist1(String json, String[] myprofitback) throws JSONException{
					 List<WechatListBean> listwecbean = new ArrayList<WechatListBean>();
					
					if (json == null) {
						return null;
					} else {
						
						JSONArray jsondata = new JSONArray(json);  
						for (int i = 0; i < jsondata.length(); i++) {
							ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String,Object>>(); 
							JSONObject jsonobject = jsondata.getJSONObject(i);
							WechatListBean wacahtbean = new WechatListBean();
							wacahtbean.setPayDate(jsonobject.getString("payDate"));
							wacahtbean.setTotalFee(jsonobject.getString("totalFee"));
							wacahtbean.setOrderCount(jsonobject.getString("orderCount"));
							JSONArray arr = jsonobject.getJSONArray("infos");
							for (int j = 0; j < arr.length(); j++) {
								JSONObject jsonob = arr.getJSONObject(j);
								HashMap<String, Object> map  = new HashMap<String, Object>();
								for (int k = 0; k < myprofitback.length; k++) {
									map.put(myprofitback[k], jsonob.get(myprofitback[k]));
								}
									
								
//									map.put("payTime", jsonob.get("payTime"));
								
								array.add(map);
							}
							wacahtbean.list.addAll(array);
							
							listwecbean.add(wacahtbean);
						}
					}
				return listwecbean;
				}
				
				public static List<WechatListBean> getjsonlistf(String json, String[] myprofitback) throws JSONException{
					 List<WechatListBean> listwecbean = new ArrayList<WechatListBean>();
					
					if (json == null) {
						return null;
					} else {
						
						JSONArray jsondata = new JSONArray(json);  
						for (int i = 0; i < jsondata.length(); i++) {
							ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String,Object>>(); 
							JSONObject jsonobject = jsondata.getJSONObject(i);
							WechatListBean wacahtbean = new WechatListBean();
							wacahtbean.setTotalFee(jsonobject.getString("monthTotalFee"));
							wacahtbean.setOrderCount(jsonobject.getString("monthTotalCount"));
							JSONArray arr = jsonobject.getJSONArray("payList");
							for (int j = 0; j < arr.length(); j++) {
								JSONObject jsonob = arr.getJSONObject(j);
								HashMap<String, Object> map  = new HashMap<String, Object>();
								for (int k = 0; k < myprofitback.length; k++) {
									map.put(myprofitback[k], jsonob.get(myprofitback[k]));
								}
								
//									map.put("payTime", jsonob.get("payTime"));
								
								array.add(map);
							}
							wacahtbean.list.addAll(array);
							
							listwecbean.add(wacahtbean);
						}
					}
				return listwecbean;
				}
}
