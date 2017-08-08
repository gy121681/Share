package com.td.qianhai.epay.oem.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 该类提供数据库的初始化和升级操作，当程序第一次安装时， 系统会调用该类的<code>onCreate(SQLiteDatabase db)</code>
 * 方法来进行相应的初始化操作。当程序升级时，如果有需要修改表的 结构，可以修改<code>DBVERSION</code>的值，当系统发现
 * <code>DBVERSION</code> 的值发生改变时会调用
 * <code>onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)</code>
 * 方法来进行相应的操作
 * 
 * @author liang
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "pay.db"; // 数据库名

	private static final int DBVERSION = 1; // 数据库版本

	public static DBHelper dbHelper;

	public SQLiteDatabase db;

	private static Context _context;

	// private boolean isUpgrade; //用来升级数据库表

	protected DBHelper(Context context) {

		super(context, DBNAME, null, DBVERSION);
		Log.e("DBHelper", "创建SQLite数据库");
	}

	public static DBHelper getDBHelperInstance(Context context) {
		_context = context;

		if (null == dbHelper) {

			dbHelper = new DBHelper(context);
		}

		return dbHelper;
	}

	/**
	 * 应用程序第一次安装时会调用此方法，且只调用一次
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 断点续传
		db.execSQL("CREATE TABLE fileDownloading(_id integer primary key autoincrement,downPath varchar(100),threadId INTEGER,downLength INTEGER)");

	}

	/**
	 * SQLite数据库
	 * 
	 * @return
	 */
	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * 当数据库版本发生变化时会调用此方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
	}

	/************************** 分割线 *******************************/

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		_context = context;

	}

	public DBHelper(Context context, String name) {
		this(context, name, null, DBVERSION);
		db = this.getReadableDatabase();
//		File file = new File(Environment.getExternalStorageDirectory()
//				+ "/mypackage" + "/" + name + ".sqllite");
	}

	public void insertTmpData() {
		
	}
	
	// 数据库的查询函数
	public Cursor rawQuery(String sql) {
		return db.rawQuery(sql, null);
	}

	public boolean execSQL(String sql) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast toast = Toast.makeText(_context.getApplicationContext(),
					"android.database.sqlite.SQLiteException",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			Log.i("sqlerr_log------->", e.toString());
			Log.i("err_sql------->", sql);
			return false;
		}
		return true;
	}

	public boolean execSQL(String sql, boolean Throw) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			if (Throw)
				throw e;
			return false;
		}
		return true;
	}

	// 封装系统的执行sql语句的函数
	public boolean execSQL(String sql, Object[] object) {
		try {
			db.execSQL(sql, object);
		} catch (SQLException e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(_context.getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		return true;
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}

	public void close() {
		db.close();
	}

	// public void createview() {
	// String sql =
	// "select name from sqlite_master where type='view' and name='v_detail'";
	// Cursor cursor = this.rawQuery(sql);
	//
	// if (cursor.moveToFirst()) {
	// } else
	// db.execSQL("CREATE VIEW v_detail AS SELECT * from t_detail union all SELECT * from t_detail_bak");
	// }

}
