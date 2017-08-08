package com.td.qianhai.epay.oem.fragment;

import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
	protected String tag = "BaseFragment";
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}