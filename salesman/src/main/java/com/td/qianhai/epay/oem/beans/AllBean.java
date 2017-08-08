package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;
import java.util.List;

import com.td.qianhai.epay.oem.mail.utils.CBluetoothDeviceContext;


public class AllBean implements Serializable {
	/** 终端号 */
	public static String psamId = "";
	/** 蓝牙是否开启 */
	public static String isOpen = "";
	/** 连接的蓝牙名称 */
	public static String bluetoothName = "";
	/** 连接的蓝牙mac */
	public static String bluetoothMac = "";
	/** 连接的蓝牙名称(程序初始化界面第一次蓝牙) */
	public static String bluetoothNameMain = "";
	/** 连接的蓝牙mac(程序初始化界面第一次蓝牙) */
	public static String bluetoothMacMain = "";
	/** 蓝牙连接状态(0未连接/1连接) */
	public static int bluetoothConn = 0;
	
	public static int tag = 1;
	/** 蓝牙搜索存储(新大陆) */
	public static List<CBluetoothDeviceContext> discoveredDevices;
	
	public static List<CBluetoothDeviceContext> getDiscoveredDevices() {
		return discoveredDevices;
	}

	public static void setDiscoveredDevices(
			List<CBluetoothDeviceContext> discoveredDevices) {
		AllBean.discoveredDevices = discoveredDevices;
	}

	public static String getPsamId() {
		return psamId;
	}

	public static void setPsamId(String psamId) {
		AllBean.psamId = psamId;
	}

	public static String getIsOpen() {
		return isOpen;
	}

	public static void setIsOpen(String isOpen) {
		AllBean.isOpen = isOpen;
	}

	public static String getBluetoothName() {
		return bluetoothName;
	}

	public static void setBluetoothName(String bluetoothName) {
		AllBean.bluetoothName = bluetoothName;
	}

	public static String getBluetoothMac() {
		return bluetoothMac;
	}

	public static void setBluetoothMac(String bluetoothMac) {
		AllBean.bluetoothMac = bluetoothMac;
	}

	public static String getBluetoothNameMain() {
		return bluetoothNameMain;
	}

	public static void setBluetoothNameMain(String bluetoothNameMain) {
		AllBean.bluetoothNameMain = bluetoothNameMain;
	}

	public static String getBluetoothMacMain() {
		return bluetoothMacMain;
	}

	public static void setBluetoothMacMain(String bluetoothMacMain) {
		AllBean.bluetoothMacMain = bluetoothMacMain;
	}

	public static int getBluetoothConn() {
		return bluetoothConn;
	}

	public static void setBluetoothConn(int bluetoothConn) {
		AllBean.bluetoothConn = bluetoothConn;
	}
	
	
}
