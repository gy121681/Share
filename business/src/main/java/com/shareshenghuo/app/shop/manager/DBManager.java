package com.shareshenghuo.app.shop.manager;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.shareshenghuo.app.shop.network.bean.UserInfo;

public class DBManager implements DbUpgradeListener {

	private static DBManager instance;
	
	private Context context;
	private DbUtils db;
	private String DB_NAME = "shirley_user";
	private int DB_VERSION = 2;
	
	private DBManager(Context context) {
		this.context = context;
		db = DbUtils.create(context.getApplicationContext(), DB_NAME, DB_VERSION, this);
	}
	
	public static synchronized DBManager getInstance(Context context) {
		if(instance == null)
			instance = new DBManager(context);
		return instance;
	}
	
	public DbUtils getDB() {
		return db;
	}

	@Override
	public void onUpgrade(DbUtils db, int arg1, int arg2) {
		try {
			db.dropTable(UserInfo.class);
			UserInfoManager.setUserId(context, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
