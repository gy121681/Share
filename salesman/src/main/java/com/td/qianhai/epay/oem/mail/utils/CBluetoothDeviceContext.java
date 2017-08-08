package com.td.qianhai.epay.oem.mail.utils;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;

/**
 * 
 * 自定义蓝牙设备类，封装地址和名称
 * 
 * @author Administrator
 * 
 */
public class CBluetoothDeviceContext {
	public String name = "";
	public String address = "";
	public BluetoothDevice device;
	private List<CBluetoothDeviceContext> discoveredDevices = new ArrayList<CBluetoothDeviceContext>();

	public CBluetoothDeviceContext(String name, String address,BluetoothDevice device) {
		this.name = name;
		this.address = address;
		this.device = device;
	}

	/**
	 * 检查是蓝牙地址是否已经存在
	 * 
	 * @return
	 */
	public boolean ifAddressExist(String addr) {
		for (CBluetoothDeviceContext devcie : discoveredDevices) {
			if (addr.equals(devcie.address))
				return true;
		}
		return false;
	}
}
