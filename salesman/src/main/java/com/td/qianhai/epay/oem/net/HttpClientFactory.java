package com.td.qianhai.epay.oem.net;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
	private static DefaultHttpClient client = null;;
	
	public static DefaultHttpClient getHttpClient() {
		if (client == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
			HttpConnectionParams.setSoTimeout(httpParameters, 20000);  
			ConnManagerParams.setMaxTotalConnections(httpParameters, 1);
			client = new DefaultHttpClient(httpParameters);
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

		}
		return client;
	}
	
	public static DefaultHttpClient getHttpClient1() {
		if (client == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
			HttpConnectionParams.setSoTimeout(httpParameters, 20000);  
			ConnManagerParams.setMaxTotalConnections(httpParameters, 1);
			client = new DefaultHttpClient(httpParameters);
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));

		}
		return client;
	}
	public static void clearAllCookies() {
		((DefaultHttpClient) client).getCookieStore().clear();
//		CookieUtils.clearAll();
	}
}
