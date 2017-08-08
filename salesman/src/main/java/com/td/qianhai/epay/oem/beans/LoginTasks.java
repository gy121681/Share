package com.td.qianhai.epay.oem.beans;

import java.util.HashMap;

import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class LoginTasks {

	private String custid, userPsw, userid, appid, attStr;
	private boolean isrun;
	private Editor editor;
	private Context context;
	public static String results = "";
	private OneButtonDialogWarn warnDialog;

	public LoginTasks(Context context) {
		this.context = context;
		appid = AppContext.getInstance().getAppid();
		userid = AppContext.getInstance().getUserid();
		editor = MyCacheUtil.setshared(context);
		custid = MyCacheUtil.getshared(context).getString("Mobile", "");
		attStr = MyCacheUtil.getshared(context).getString("MERSTS", "");
		userPsw = MyCacheUtil.getshared(context).getString("pwd", "");

	}

	public String logininfo() {

		GetMerStsTask stsTask = new GetMerStsTask();
		stsTask.execute("199002", custid, userPsw, "11111111", "", "2", "",
				"", "1","","","","");
		isrun = false;
		return results;
	}

	class GetMerStsTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7], params[8],params[9],params[10],params[11],params[12] };
			return NetCommunicate.get(HttpUrls.LOGIN, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
				
					if(result.get("STS").toString().equals("-1")){
						results = result.get(Entity.RSPMSG).toString();
						
					}
					
					attStr = result.get("MERSTS").toString();
					AppContext.getInstance().setTxnsts(
							result.get("TXNSTS").toString());
					AppContext.getInstance().setMerSts(attStr);
					editor.putString("MERSTS", attStr);
					editor.putString("STS", result.get("STS").toString());
					AppContext.getInstance().setTxnsts(
							result.get("TXNSTS").toString());
					editor.putString("Txnsts", result.get("TXNSTS").toString());
					editor.putString("CustId", result.get("PHONENUMBER")
							.toString());
					editor.putString("Mobile", result.get("PHONENUMBER")
							.toString());
					editor.putString("MercNum", result.get("MERCNUM")
							.toString());
					editor.commit();

					if (result.get("NOCARDFEERATE") != null) {

						editor.putString("nocardfeerate",
								result.get("NOCARDFEERATE").toString());
						editor.putString("oemfeerate", result.get("OEMFEERATE")
								.toString());
						editor.commit();
					} else {
						editor.putString("nocardfeerate", "0.69");

						editor.commit();
					}
					if (result.get("LOGNUM") != null) {
						editor.putString("LOGNUM", result.get("LOGNUM").toString());
						editor.commit();
					}

					AppContext.getInstance().setSts(
							result.get("STS").toString());

					if (result.get("IDCARDPICSTA") != null
							&& result.get("CUSTPICSTA").toString() != null
							&& result.get("FRYHKIMGPATHSTA") != null) {
						String a = result.get("IDCARDPICSTA").toString();
						String b = result.get("CUSTPICSTA").toString();
						String c = result.get("FRYHKIMGPATHSTA").toString();
						AppContext.getInstance().setStateaudit(a + b + c);
					}

				}
			} else {
				Toast.makeText(context, "网络状况不佳", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}

}
