package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.List;

import com.td.qianhai.epay.oem.database.DBHelper;
import com.td.qianhai.epay.utils.Common;

import android.database.Cursor;

/**
 * <p>
 * 快递公司信息表 t_companyinfo
 * </p>
 * 
 * @author xiangxm 2013-12-3
 * 
 * 
 */

public class cls_companyinfo {

	/**
	 * 
	 * 编号
	 */
	public String info_cd = "";
	/**
	 * 
	 * 快递公司名称
	 */
	public String name = "";
	/**
	 * 
	 * id与code对应
	 */
	public String id = "";
	public String count = "";

	/** 帮助信息 **/
	public String helpInfo = "";
	/** 电话号码 **/
	public String phoneNumber = "";

	/**
	 * 获取快递公司各种信息
	 * 
	 * @param dbh
	 * @return
	 */
	public static List<cls_companyinfo> getCompanyInfoList(DBHelper dbh) {

		List<cls_companyinfo> resultList = new ArrayList<cls_companyinfo>();
		String sql = " select * from t_companyinfo order by info_cd ,id ";
		Cursor cursor = dbh.rawQuery(sql);

		cls_companyinfo companyinfo = null;
		while (cursor.moveToNext()) {

			companyinfo = new cls_companyinfo();
			Common.setClassValueBycursor(companyinfo, cursor);
			resultList.add(companyinfo);

		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}
		return resultList;

	}

	/**
	 * 、根据快递公司的id查询帮助信息
	 * 
	 * @param dbh
	 * @param id
	 * @return
	 */
	public static List<String> getCompanyName(DBHelper dbh, String id) {

		if (id == null || id.equals("")) {
			return null;
		}
		String sql = "select helpinfo,phonenumber from t_companyinfo where id = '"
				+ id + "'";

		Cursor cursor = dbh.rawQuery(sql);

		List<String> list = new ArrayList<String>();

		while (cursor.moveToNext()) {

			String helpinfo = cursor.getString(cursor
					.getColumnIndex("helpinfo"));
			String phonenumber = cursor.getString(cursor
					.getColumnIndex("phonenumber"));
			list.add(helpinfo);
			list.add(phonenumber);
		}
		return list;
	}

	/**
	 * 删除数据
	 * 
	 * @param dbh
	 */
	public static void deleteAll(DBHelper dbh) {
		String sql = "delete from t_companyinfo";
		dbh.execSQL(sql);
	}

	/**
	 * 追加数据
	 * 
	 * @param dbh
	 * @return
	 */
	public boolean addData(DBHelper dbh) {

		StringBuffer sql = new StringBuffer();
		sql.append(" insert into t_companyinfo ");
		sql.append(" (info_cd,name,id,count,helpinfo,phonenumber)");
		sql.append(" values ( ");
		sql.append(" '" + info_cd + "',");
		sql.append(" '" + name + "',");
		sql.append(" '" + id + "',");
		sql.append(" '" + count + "',");
		sql.append(" '" + helpInfo + "',");
		sql.append(" '" + phoneNumber + "')");
		return dbh.execSQL(sql.toString());
	}

	/**
	 * 修改数据
	 * 
	 * @param dbh
	 * @return
	 */
	public boolean updateData(DBHelper dbh) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update t_companyinfo set ");
		sql.append(" info_cd = '" + info_cd + "',");
		sql.append(" name = '" + name + "',");
		sql.append(" id = '" + id + "',");
		sql.append(" count = '" + count + "',");
		sql.append(" helpInfo = '" + helpInfo + "',");
		sql.append(" phoneNumber = '" + phoneNumber + "'");

		return dbh.execSQL(sql.toString());
	}

	/**
	 * 查询数据是否存在
	 * 
	 * @param dbh
	 * @param id
	 * @return
	 */
	public boolean checkExits(DBHelper dbh, String id) {

		Boolean bolRtn = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from  t_companyinfo ");
		sql.append(" where id = '" + id + "'");
		Cursor cursor = dbh.rawQuery(sql.toString());

		if (cursor.moveToFirst())
			bolRtn = true;

		cursor.close();
		return bolRtn;
	}

	/**
	 * 获取最大列表数
	 * 
	 * @param dbh
	 * @return
	 */
	public String getMaxIndexNo(DBHelper dbh) {

		String sql = "select ifnull(max(info_cd),0)+1 as info_cd from t_companyinfo ";
		Cursor cursor = dbh.rawQuery(sql);

		String strvalue = null;
		if (cursor.moveToNext()) {
			strvalue = cursor.getString(cursor.getColumnIndex("info_cd"));
			if (strvalue == null)
				strvalue = "1";
		} else {
			strvalue = "1";
		}
		cursor.close();
		return strvalue;
	}

}
